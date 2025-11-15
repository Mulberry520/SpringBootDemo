package com.mulberry.controller;

import com.mulberry.common.CommonConst;
import com.mulberry.common.R;
import com.mulberry.dto.LoginDTO;
import com.mulberry.dto.UpdateDTO;
import com.mulberry.dto.UserDTO;
import com.mulberry.service.FileService;
import com.mulberry.service.UserService;
import com.mulberry.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final FileService fileService;
    private final StringRedisTemplate redisTemplate;

    public UserController(
            PasswordEncoder passwordEncoder,
            UserService userService,
            JwtUtil jwtUtil,
            FileService fileService,
            StringRedisTemplate redisTemplate
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.fileService = fileService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/register")
    public R<Void> register(@RequestBody @Valid UserDTO user) {
        if (userService.findByName(user.getUsername()) != null) {
            return R.error("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername().trim());
        int affected =  userService.register(user);
        return R.success("Successfully create " + affected + " user");
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody @Valid LoginDTO loginInfo) {
        String username = loginInfo.getUsername().trim();
        UserDTO target = userService.findByName(username);
        if (target == null) {
            return R.error("Not exists this user");
        }

        if (!passwordEncoder.matches(loginInfo.getPassword(), target.getPassword())) {
            return R.error("Wrong password");
        }

        String token = jwtUtil.generateToken(username);
        redisTemplate.opsForValue().set(token, username, jwtUtil.getExpiration(), TimeUnit.MILLISECONDS);
        return R.success("Keep your token", token);
    }

    @GetMapping("/logout")
    public R<String> logout(
            @RequestParam(name = "isLogout", defaultValue = "false") Boolean isLogout,
            @RequestHeader("Authorization") String authorization
    ) {
        System.out.println("isLogout = " + isLogout);
        if (isLogout) {
            String token = authorization.substring(CommonConst.OAuthToken.length());
            redisTemplate.delete(token);
            return R.success("Logout safely");
        }
        return R.success("Haven't logout now");
    }

    @GetMapping("/userinfo")
    public R<UserDTO> userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return R.success(userService.findByName(username));
    }

    @PatchMapping("/userinfo")
    public R<String> update(
            @RequestBody  @Valid UpdateDTO updates,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader("Authorization") String authorization
    ) {
        String username = userDetails.getUsername();
        updates.setUsername(username);
        String errInfo = userService.updateBasicInfo(updates);
        if (errInfo != null) {
            return R.error(errInfo);
        }

        String token = authorization.substring(CommonConst.OAuthToken.length());
        redisTemplate.delete(token);
        return R.success();
    }


    @GetMapping("/avatar")
    public R<String> getAvatar(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String avatar = userService.getAvatarUrl(userDetails.getUsername());
        if (avatar == null || avatar.length() < 20) {
            return R.error("You haven't post a valid avatar");
        }

        String signedUrl = fileService.generateSignedUrl(avatar);
        return R.success("This is your avatar's temp url", signedUrl);
    }

    @PatchMapping("/avatar")
    public R<String> updateAvatar(
            @RequestParam("file") MultipartFile avatar,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String avatarUrl = fileService.ossSave(avatar, FileService.Category.AVATAR);
            userService.updateAvatar(userDetails.getUsername(), avatarUrl);
            return R.success();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}

package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.dto.LoginDTO;
import com.mulberry.dto.UpdateDTO;
import com.mulberry.dto.UserDTO;
import com.mulberry.service.FileService;
import com.mulberry.service.UserService;
import com.mulberry.util.JwtUtil;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final FileService fileService;

    public UserController(PasswordEncoder passwordEncoder, UserService userService, JwtUtil jwtUtil, FileService fileService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.fileService = fileService;
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
        return R.success(jwtUtil.generateToken(username));
    }

    @GetMapping("/userinfo")
    public R<UserDTO> userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return R.success(userService.findByName(username));
    }

    @PatchMapping("/userinfo")
    public R<String> update(
            @RequestBody  @Valid UpdateDTO updates,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        updates.setUsername(userDetails.getUsername());
        String errInfo = userService.updateBasicInfo(updates);
        if (errInfo != null) {
            return R.error(errInfo);
        }
        return R.success();
    }

    @PatchMapping("/avatar")
    public R<Void> updateAvatar(
            @RequestParam("avatarName") String avatar,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userService.updateAvatar(userDetails.getUsername(), avatar);
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
        return R.success(signedUrl);
    }
}

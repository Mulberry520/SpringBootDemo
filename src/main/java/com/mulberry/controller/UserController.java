package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.dto.LoginDTO;
import com.mulberry.dto.UpdateDTO;
import com.mulberry.dto.UserDTO;
import com.mulberry.service.UserService;
import com.mulberry.util.JwtUtil;
import jakarta.validation.Valid;
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

    public UserController(PasswordEncoder passwordEncoder, UserService userService, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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

    @PostMapping("/update")
    public R<String> update(
            @RequestBody  @Valid UpdateDTO updates,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        UserDTO updateUer = userService.findByName(username);
        if (!passwordEncoder.matches(updates.getPassword(), updateUer.getPassword())) {
            return R.error("Input correct password to chang infos");
        }

        String newPassword = updates.getNewPassword();
        if (newPassword != null) {
            if (!newPassword.equals(updates.getRepeatPassword())) {
                return R.error("Repeat the same password to change");
            }
            updateUer.setPassword(passwordEncoder.encode(newPassword));
        }
        updateUer.setNickname(updates.getNickname());
        updateUer.setEmail(updates.getEmail());
        userService.updateBasicInfo(updateUer);

        return R.success("Successfully update your info");
    }
}

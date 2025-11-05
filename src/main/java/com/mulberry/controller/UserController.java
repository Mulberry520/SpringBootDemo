package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.dto.LoginDTO;
import com.mulberry.dto.UserDTO;
import com.mulberry.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public R<Void> register(@RequestBody @Valid UserDTO user) {
        if (userService.findByName(user.getUsername()) != null) {
            return R.error("Username already exists");
        }

        String encodedPasswd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPasswd);
        int affected =  userService.register(user);
        return R.success("Successfully insert " + affected + " user");
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody @Valid LoginDTO user) {
        String name = user.getUsername();
        UserDTO target = userService.findByName(name);
        if (target == null) {
            return R.error("Not exists this user");
        }

        String passwd = passwordEncoder.encode(user.getPassword());
        if (!target.getPassword().equals(passwd)) {
            return R.error("Wrong password");
        }

        return R.success("JWT token");
    }
}

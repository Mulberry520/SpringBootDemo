package com.mulberry.service.impl;

import com.mulberry.dto.UpdateDTO;
import com.mulberry.dto.UserDTO;
import com.mulberry.mapper.UserMapper;
import com.mulberry.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO findByName(String name) {
        return mapper.selectByName(name);
    }

    @Override
    public int register(UserDTO user) {
        return mapper.insertWithBasicInfo(
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getEmail()
        );
    }

    @Override
    public String updateBasicInfo(UpdateDTO updates) {
        String username = updates.getUsername();
        UserDTO updateUser = mapper.selectByName(username);
        if (updateUser == null) {
            return "Current user has been changed, please log back in";
        }
        String password = updates.getPassword();
        if (!passwordEncoder.matches(password, updateUser.getPassword())) {
            return "Input correct password to change infos";
        }

        String newPassword = updates.getNewPassword();
        String newNickname = updates.getNickname();
        String newEmail = updates.getEmail();
        if (newPassword != null) {
            if (newPassword.equals(password)) {
                return "New password can't be the same as the old";
            }
            if (!newPassword.equals(updates.getRepeatPassword())) {
                return "Repeat the new password again to change password";
            }
            updateUser.setPassword(passwordEncoder.encode(newPassword));
        }
        if (newNickname != null) {
            updateUser.setNickname(newNickname);
        }
        if (newEmail != null) {
            updateUser.setEmail(newEmail);
        }

        mapper.updateBasicInfo(
                updateUser.getUsername(),
                updateUser.getPassword(),
                updateUser.getNickname(),
                updateUser.getEmail()
        );
        return null;
    }

    @Override
    public int updateAvatar(String username, String avatarUrl) {
        return mapper.updateAvatar(username, avatarUrl);
    }

    @Override
    public Integer getUserId(String username) {
        return mapper.selectIdByName(username);
    }

    @Override
    public String getAvatarUrl(String username) {
        return mapper.selectPicByName(username);
    }
}

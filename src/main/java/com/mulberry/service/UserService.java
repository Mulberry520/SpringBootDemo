package com.mulberry.service;

import com.mulberry.dto.UpdateDTO;
import com.mulberry.dto.UserDTO;

public interface UserService {
    UserDTO findByName(String name);

    int register(UserDTO user);

    String updateBasicInfo(UpdateDTO updates);

    int updateAvatar(String username, String avatarUrl);

    Integer getUserId(String username);

    String getAvatarUrl(String username);
}

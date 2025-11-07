package com.mulberry.service;

import com.mulberry.dto.UserDTO;

public interface UserService {
    UserDTO findByName(String name);

    int register(UserDTO user);

    int updateBasicInfo(UserDTO updates);
}

package com.mulberry.service.impl;

import com.mulberry.dto.UserDTO;
import com.mulberry.entity.User;
import com.mulberry.mapper.UserMapper;
import com.mulberry.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;

    public UserServiceImpl(UserMapper mapper) {
        this.mapper = mapper;
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
    public int updateBasicInfo(UserDTO user) {
        return mapper.updateBasicInfo(
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getEmail()
        );
    }
}

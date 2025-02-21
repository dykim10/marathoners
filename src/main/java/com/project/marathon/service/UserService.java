package com.project.marathon.service;

import com.project.marathon.entity.User;
import com.project.marathon.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Optional<User> getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User updateLastLogin(String userId) {
        return userMapper.lastLoginDateUpdate(userId);
    }
}

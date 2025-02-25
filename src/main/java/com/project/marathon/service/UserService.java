package com.project.marathon.service;

import com.project.marathon.dto.UserResponse;
import com.project.marathon.entity.User;
import com.project.marathon.enums.UserStatus;
import com.project.marathon.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Optional<UserResponse> getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User updateLastLogin(String userId) {
        return userMapper.lastLoginDateUpdate(userId);
    }

    public UserResponse checkUser(String type, String value) {
        UserResponse response = new UserResponse();

        if ("userId".equals(type) && userMapper.countByUserId(value) > 0) {
            response.setUserExists(UserStatus.USERID_EXISTS);
        } else if ("userEmail".equals(type) && userMapper.countByUserEmail(value) > 0) {
            response.setUserExists(UserStatus.EMAIL_EXISTS);
        } else {
            response.setUserExists(UserStatus.NOT_FOUND);
        }

        return response;
    }

}

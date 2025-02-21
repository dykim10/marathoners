package com.project.marathon.service;

import com.project.marathon.entity.User;
import com.project.marathon.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateLastLogin(String userId) {
        userRepository.lastLoginDateUpdate(userId);
    }
}

package com.project.marathon.controller;

import com.project.marathon.dto.UserResponse;
import com.project.marathon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/check-user")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> checkUser(@RequestBody Map<String, String> request) {
        String type = request.get("type");
        String value = request.get("value");

        logger.info("type => {}  value => {}", type , value);
        UserResponse response = userService.checkUser(type, value);
        return ResponseEntity.ok(response);
    }
}

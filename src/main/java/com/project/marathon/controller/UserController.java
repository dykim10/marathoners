package com.project.marathon.controller;

import com.project.marathon.dto.UserRequest;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.enums.UserStatus;
import com.project.marathon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/check-user")
    public ResponseEntity<UserResponse> checkUser(@RequestBody Map<String, String> request) {
        String type = request.get("type");
        String value = request.get("value");

        logger.info("type => {}  value => {}", type , value);
        UserResponse response = userService.checkUser(type, value);
        return ResponseEntity.ok(response);
    }

    // ✅ 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {

        logger.info("userRequest => {}", userRequest);

        UserResponse response = userService.registerUser(userRequest);
        if (response.getUserRegStatus() == UserStatus.USER_REGISTER_SUCCESS) {
            return ResponseEntity.ok(response); // ✅ 성공 시 200 OK 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // ✅ 실패 시 500 반환
        }
    }
}

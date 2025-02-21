package com.project.marathon.controller;

import com.project.marathon.dto.ErrorResponse;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login") // ✅ JSON 요청 받도록 설정
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String userId = loginRequest.get("userId");
        String password = loginRequest.get("password");
        logger.info("🔹 로그인 요청: userId={}, password=****", userId);

        // ✅ 인증 서비스 호출
        //UserResponse authResponse = authService.login(userId, password);
        String authResponse = authService.login(userId, password);

        if (authResponse == null) {
            return ResponseEntity.status(401).body(new ErrorResponse(401, "인증 실패: 아이디 또는 비밀번호가 올바르지 않습니다."));
        }


        return ResponseEntity.ok(authResponse);

        //return ResponseEntity.ok().body(new UserResponse());
    }
}


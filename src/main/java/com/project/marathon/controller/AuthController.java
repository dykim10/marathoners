package com.project.marathon.controller;

import com.project.marathon.dto.ErrorResponse;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login") // ✅ JSON 요청 받도록 설정
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String userId = loginRequest.get("userId");
        String password = loginRequest.get("password");

        // ✅ 인증 서비스 호출
        UserResponse authResponse = authService.login(userId, password);

        if (authResponse == null) {
            return ResponseEntity.status(401).body(new ErrorResponse(401, "인증 실패: 아이디 또는 비밀번호가 올바르지 않습니다."));
        }


        return ResponseEntity.ok(authResponse);
    }
}

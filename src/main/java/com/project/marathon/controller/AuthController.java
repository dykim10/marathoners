package com.project.marathon.controller;

import com.project.marathon.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")  // ✅ "/api" 경로 설정 확인
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")  // ✅ "/api/login"이 올바르게 매핑되도록 설정
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String userId = loginRequest.get("userId");
        String password = loginRequest.get("password");

        // ✅ 서비스에서 반환된 데이터를 확인
        Map<String, String> response = authService.authenticateUser(userId, password);
        if (response == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인 실패. 아이디 또는 비밀번호를 확인하세요."));
        }

        return ResponseEntity.ok(response);
    }
}

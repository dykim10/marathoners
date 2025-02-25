package com.project.marathon.controller;

import com.project.marathon.dto.ErrorResponse;
import com.project.marathon.dto.LoginRequest;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest
                , HttpServletRequest request
                , HttpServletResponse response
        ) {
        try {
            UserResponse userResponse = authService.login(loginRequest.getUserId(), loginRequest.getPassword(),request, response);
            return ResponseEntity.ok(userResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패: " + e.getMessage());
        }
    }

    //session 확인
    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> getSession(HttpServletRequest request) {
        return authService.getSession(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }

}


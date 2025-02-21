package com.project.marathon.service;

import com.project.marathon.entity.Users;
import com.project.marathon.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public Map<String, String> authenticateUser(String userId, String password) {
        try {

            System.out.println("ASDF1111");

            // ✅ Spring Security에서 인증을 수행
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password)
            );

            System.out.println("222");

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("ASDF333");

            // ✅ 사용자 조회
            Optional<Users> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }
            Users user = userOptional.get();

            // ✅ 응답 데이터 생성 (JWT 발급하는 경우 추가 가능)
            Map<String, String> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("role", user.getUserRole());
            response.put("message", "로그인 성공");

            return response;

        } catch (Exception e) {
            System.out.println("🚨 로그인 실패: " + e.getMessage());
            return null;  // 로그인 실패 시 null 반환
        }
    }
}

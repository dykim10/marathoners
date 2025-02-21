package com.project.marathon.service;

import com.project.marathon.dto.UserResponse;
import com.project.marathon.dto.UserRequest;
import com.project.marathon.entity.User;
import com.project.marathon.repository.UserRepository;
import com.project.marathon.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private UserService userService;
    private UserResponse userResponse;


    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public UserResponse login(String userId, String password) {
        try {
            // ✅ 사용자 인증 처리
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password)
            );

            // ✅ 인증 성공 시, SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ JWT 토큰 생성
            String token = jwtTokenProvider.generateToken(authentication);

            // ✅ DB에서 사용자 정보 조회
            Optional<User> optionalUser = userRepository.findByUserId(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                //마지막 로그인 시간 업데이트
                userService.updateLastLogin(user.getUserId());

                UserResponse userResponse = new UserResponse();
                userResponse.setToken(token);
                userResponse.setUserId(user.getUserId());
                userResponse.setUserName(user.getUserName());
                userResponse.setUserRole(user.getUserRole());

                // ✅ 사용자 정보 + JWT 토큰 반환
                return userResponse;

            } else {
                return null; // 사용자 정보 없음
            }
        } catch (Exception e) {
            return null; // 인증 실패
        }
    }
}

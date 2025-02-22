package com.project.marathon.service;

import com.project.marathon.dto.UserResponse;
import com.project.marathon.entity.User;
import com.project.marathon.mapper.UserMapper;
import com.project.marathon.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                       UserMapper userMapper, PasswordEncoder passwordEncoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public UserResponse login(String userId, String password) {
        try {
            // ✅ 사용자 정보 조회
            UserResponse user = userMapper.findByUserId(userId);
            logger.info("user => {}", user);

            if (user == null) {
                logger.error("❌ 인증 실패: 사용자를 찾을 수 없습니다. userId={}", userId);
                throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
            }

            // ✅ 비밀번호 검증
            if (!passwordEncoder.matches(password, user.getUserPassword())) {
                logger.error("❌ 인증 실패: 비밀번호 불일치 userId={}", userId);
                throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
            }

            // ✅ Spring Security 인증 처리
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password)
            );

            // ✅ 인증 성공 시, SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ JWT 토큰 생성
            String token = jwtTokenProvider.generateToken(authentication);

            // ✅ 마지막 로그인 시간 업데이트
            User updateUser = userService.updateLastLogin(user.getUserId());

            // ✅ UserResponse 객체 생성 및 반환
            UserResponse userResponse = new UserResponse();
            userResponse.setToken(token);
            userResponse.setUserId(user.getUserId());
            userResponse.setUserName(user.getUserName());
            userResponse.setUserRole(user.getUserRole());
            userResponse.setUserLastLoginDt(updateUser.getUserLastLoginDt());

            //logger.info("✅ 로그인 성공: userId={}", userId);
            return userResponse;

        } catch (BadCredentialsException e) {
            logger.error("❌ 인증 실패: {}", e.getMessage());
            throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            logger.error("❌ 로그인 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("로그인 중 오류가 발생했습니다.");
        }
    }
}

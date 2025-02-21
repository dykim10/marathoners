package com.project.marathon.service;

import com.project.marathon.dto.UserResponse;
import com.project.marathon.entity.User;
import com.project.marathon.mapper.UserMapper;
import com.project.marathon.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // ✅ 추가

    private UserService userService;
    private UserResponse userResponse;


    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder; // ✅ 초기화 필수
    }

//    public UserResponse login(String userId, String password) {
//        try {
//
//            // ✅ 사용자 인증 처리
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(userId, password)
//            );
//
//            System.out.println("service init");
//
//            // ✅ 인증 성공 시, SecurityContext에 저장
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // ✅ JWT 토큰 생성
//            String token = jwtTokenProvider.generateToken(authentication);
//
//            // ✅ DB에서 사용자 정보 조회
//            Optional<User> optionalUser = userMapper.findByUserId(userId);
//            //
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//
//                //마지막 로그인 시간 업데이트
//                userService.updateLastLogin(user.getUserId());
//
//                UserResponse userResponse = new UserResponse();
//                userResponse.setToken(token);
//                userResponse.setUserId(user.getUserId());
//                userResponse.setUserName(user.getUserName());
//                userResponse.setUserRole(user.getUserRole());
//
//                // ✅ 사용자 정보 + JWT 토큰 반환
//                return userResponse;
//
//            } else {
//                return null; // 사용자 정보 없음
//            }
//        } catch (Exception e) {
//            return null; // 인증 실패
//        }
//    }


    public String login(String userId, String password) {
        // 사용자 정보 조회
        User user = userMapper.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("인증 실패: 사용자를 찾을 수 없습니다. userId={}", userId);
                    return new RuntimeException("인증 실패: 아이디 또는 비밀번호가 올바르지 않습니다. 1 ");
                });

        logger.info("✅ 사용자 정보 확인: userId={}, 암호화된 비밀번호={}", user.getUserId(), user.getUserPassword());

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            logger.error("인증 실패: 비밀번호 불일치");
            throw new RuntimeException("인증 실패: 아이디 또는 비밀번호가 올바르지 않습니다. 2 ");
        }

        // Spring Security를 통한 인증 처리
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password)
            );
            logger.info("✅ 인증 성공: userId={}", userId);
            return "로그인 성공!";
        } catch (Exception e) {
            logger.error("인증 실패: {}", e.getMessage());
            throw new RuntimeException("인증 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }

}

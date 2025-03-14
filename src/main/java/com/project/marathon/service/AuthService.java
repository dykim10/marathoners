package com.project.marathon.service;

import com.project.marathon.dto.UserResponse;
import com.project.marathon.dto.UserSessionDto;
import com.project.marathon.entity.User;
import com.project.marathon.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager
            , UserMapper userMapper
            , PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse login(String userId, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 사용자 정보 조회
            UserResponse user = userMapper.findByUserId(userId);
            logger.info("user => {}", user);

            if (user == null) {
                logger.error("인증 실패: 사용자를 찾을 수 없습니다. userId={}", userId);
                throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
            }

            // 비밀번호 검증
            if (!passwordEncoder.matches(password, user.getUserPassword())) {
                logger.error("인증 실패: 비밀번호 불일치 userId={}", userId);
                throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
            }

            // Spring Security 인증 처리
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password)
            );

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // 인증 성공 시, SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true); // 세션 생성
            //logger.info("세션 발급됨 - JSESSIONID: {}", session.getId());
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            // localhost 환경에서는 Secure 속성 제거
            String setCookie = "JSESSIONID=" + session.getId() + "; Path=/; HttpOnly; SameSite=None";
            if (request.isSecure()) {
                setCookie += "; Secure"; // HTTPS 환경에서는 Secure 추가
            }


            // 응답 쿠키에 JSESSIONID 추가
            response.addHeader("Set-Cookie", setCookie);
            //logger.info(" 응답 헤더에 JSESSIONID 추가됨 ==> {} ", setCookie);

            // 마지막 로그인 시간 업데이트
            User updateUser = userMapper.lastLoginDateUpdate(user.getUserId());

            // UserResponse 객체 생성 및 반환
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(user.getUserId());
            userResponse.setUserName(user.getUserName());
            userResponse.setUserRole(user.getUserRole());
            userResponse.setUserLastLoginDt(updateUser.getUserLastLoginDt());

            //logger.info("로그인 성공: userId={}", userId);
            return userResponse;

        } catch (BadCredentialsException e) {
            //logger.error("인증 실패: {}", e.getMessage());
            throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            //logger.error("로그인 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("로그인 중 오류가 발생했습니다.");
        }
    }

    //세션 확인 로직 (기존 컨트롤러에서 이동)
    public ResponseEntity<?> getSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.info("세션 없음 - 요청된 JSESSIONID: {}", request.getRequestedSessionId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "세션 없음"));
        }

        Object securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext == null) {
            logger.info("SPRING_SECURITY_CONTEXT 없음 - JSESSIONID: {}", session.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "SPRING_SECURITY_CONTEXT 없음"));
        }

        // SecurityContextHolder에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            logger.info("인증되지 않음 - JSESSIONID: {}", session.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "인증되지 않은 사용자"));
        }

        // 사용자 정보 추출
        String userId = authentication.getName();

        // 🔹 DB에서 사용자 정보 조회
        UserResponse user = userMapper.findByUserId(userId);
        if (user == null) {
            logger.info("DB에서 사용자 정보를 찾을 수 없음 - userId: {}", userId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "사용자 정보 없음"));
        }

        String userRole = user.getUserRole(); // DB에서 가져온 Role
        logger.info("세션 유지됨 - JSESSIONID: {}, 사용자: {}, 역할: {}", session.getId(), userId, userRole);

        // UserSessionDto 생성
        UserSessionDto userSessionDto = new UserSessionDto(userId, userRole);

        return ResponseEntity.ok(userSessionDto);
    }

    // 로그아웃 로직 (기존 컨트롤러에서 이동)
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            //logger.info("세션 삭제됨 - JSESSIONID: {}", request.getRequestedSessionId());
        } else {
            //logger.info("세션 없음 - 이미 로그아웃됨");
        }

        // localhost 환경에서는 Secure 속성 제거
        String deleteCookie = "JSESSIONID=; Path=/; HttpOnly; SameSite=None; Max-Age=0";
        if (request.isSecure()) {
            deleteCookie += "; Secure"; // HTTPS 환경에서는 Secure 추가
        }

        response.setHeader("Set-Cookie", deleteCookie);
        //logger.info("JSESSIONID 쿠키 삭제됨");

        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }
}

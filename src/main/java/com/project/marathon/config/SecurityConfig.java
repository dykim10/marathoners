package com.project.marathon.config;

import com.project.marathon.mapper.UserMapper;
import com.project.marathon.security.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    //사용자 정보 서비스 설정
    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return new CustomUserDetailsService(userMapper);
    }

    //패스워드 암호화 설정 (BCrypt 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //DaoAuthenticationProvider 등록
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    //AuthenticationManager 수동 등록
    @Bean
    public AuthenticationManager customAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = authenticationProvider(userDetailsService, passwordEncoder);
        return new ProviderManager(List.of(authProvider));
    }

    //CORS 설정 (프론트엔드와 연동)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://port-next-frontend-m7cqh44n99825c47.sel4.cloudtype.app"
        ));
        configuration.setAllowCredentials(true); // ✅ 쿠키 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Set-Cookie")); // ✅ `Set-Cookie` 헤더를 클라이언트에서 확인 가능하도록 추가

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //Spring Security 설정

    /*
    스프링 시큐리티 세션 활성화 내용
        * JWT 사용 시 → SessionCreationPolicy.STATELESS
        * 세션 사용 시 → SessionCreationPolicy.IF_REQUIRED 또는 ALWAYS
        * 대규모 서비스 (부하 분산 필요 시) → Redis 세션 저장 (spring-session-data-redis 활용)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // CORS 설정 적용
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // ✅ 세션을 필요할 때만 유지
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/**").permitAll() //api 주소 호출은 오픈
                    .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            logger.error("securityFilterChain : 403 Forbidden 발생! 요청 URL: {}", request.getRequestURI());
                            response.sendError(403, "접근이 거부되었습니다.");
                        })
                );

        return http.build();
    }
}

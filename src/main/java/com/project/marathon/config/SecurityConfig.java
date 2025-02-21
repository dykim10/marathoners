package com.project.marathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    // ✅ AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ✅ 패스워드 암호화 설정 (BCrypt 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //사용자 정보 서비스 설정 (실제 구현 필요)
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UnsupportedOperationException("UserDetailsService를 구현해야 합니다.");
        };
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
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //Spring Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // CORS 설정 적용
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login").permitAll() // 로그인 엔드포인트는 인증 없이 허용
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

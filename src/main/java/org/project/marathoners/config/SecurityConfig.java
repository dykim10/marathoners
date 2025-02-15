package org.project.marathoners.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(request -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(List.of("http://localhost:3004")); // ✅ Vite 서버 허용
				config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
				config.setAllowedHeaders(List.of("*"));
				config.setAllowCredentials(true);
				return config;
			}))
			.csrf(csrf -> csrf.disable()) // ✅ REST API에서는 CSRF 보호 비활성화
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/h2-console/**", "/api/login", "/api/register").permitAll() // ✅ /api/login 허용
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginProcessingUrl("/api/login") // ✅ 로그인 엔드포인트 변경
				.successHandler((request, response, authentication) -> { // ✅ 성공 시 JSON 응답
					response.setContentType("application/json");
					response.getWriter().write("{\"message\": \"Login successful\"}");
				})
				.failureHandler((request, response, exception) -> { // ✅ 실패 시 JSON 응답
					response.setContentType("application/json");
					response.setStatus(401);
					response.getWriter().write("{\"error\": \"Invalid username or password\"}");
				})
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/api/logout") // ✅ 로그아웃 엔드포인트 변경
				.logoutSuccessHandler((request, response, authentication) -> { // ✅ 성공 시 JSON 응답
					response.setContentType("application/json");
					response.getWriter().write("{\"message\": \"Logout successful\"}");
				})
				.permitAll()
			);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

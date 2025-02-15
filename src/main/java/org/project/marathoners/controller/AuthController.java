package org.project.marathoners.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

	@GetMapping("/user")
	public Map<String, Object> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> response = new HashMap<>();
		
		if (authentication != null && authentication.isAuthenticated()) {
			response.put("username", authentication.getName());
			response.put("roles", authentication.getAuthorities());
		} else {
			response.put("error", "Not authenticated");
		}
		return response;
	}
}

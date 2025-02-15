package org.project.marathoners.service;

import org.project.marathoners.entity.User;
import org.project.marathoners.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		return org.springframework.security.core.userdetails.User
						.withUsername(user.getUsername())
						.password(user.getPassword())
						.roles(user.getRole())
						.build();
	}

	/**
	 * 
	 * @param username -> 이름
	 * @param rawPassword -> 비번
	 * @param role -> 역할
	 */
	public void registerUser(String username, String rawPassword, String role) {
		String encodedPassword = passwordEncoder.encode(rawPassword); // ✅ 평문을 암호화
		userMapper.insertUser(username, encodedPassword, role);
	}
}

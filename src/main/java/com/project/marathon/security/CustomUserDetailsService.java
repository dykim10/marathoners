package com.project.marathon.security;

import com.project.marathon.mapper.UserMapper;
import com.project.marathon.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            logger.error("❌ 인증 실패: 사용자를 찾을 수 없습니다1111. userId={}", userId);
            throw new RuntimeException("인증 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getUserPassword(),
                Collections.emptyList()
        );
    }
}

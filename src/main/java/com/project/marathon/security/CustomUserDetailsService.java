package com.project.marathon.security;

import com.project.marathon.repository.UserRepository;
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
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> userEntityOptional = userRepository.findByUserId(userId);

        if (userEntityOptional.isEmpty()) {
            //여기
            logger.error("❌ 사용자 조회 실패: userId={} (DB에서 사용자 정보 없음)", userId);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        }

        logger.info("✅ 사용자 조회 성공: userId={} (DB에서 정보 가져옴)", userId);
        User user = userEntityOptional.get();

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getUserPwd(),
                Collections.emptyList() // 권한(roles) 추가 가능
        );
    }
}

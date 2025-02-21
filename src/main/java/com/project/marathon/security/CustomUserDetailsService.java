//package com.project.marathon.security;
//
//import com.project.marathon.entity.Users;
//import com.project.marathon.repository.UserRepository;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        Users user = userRepository.findByUserId(userId);
//        if (user == null) {
//            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId);
//        }
//        return new CustomUserDetails(user);
//    }
//}
//

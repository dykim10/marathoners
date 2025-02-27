package com.project.marathon.service;

import com.project.marathon.controller.UserController;
import com.project.marathon.dto.UserRequest;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.entity.User;
import com.project.marathon.enums.UserStatus;
import com.project.marathon.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // ✅ BCrypt 추가

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Optional<UserResponse> getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User updateLastLogin(String userId) {
        return userMapper.lastLoginDateUpdate(userId);
    }

    public UserResponse checkUser(String type, String value) {
        UserResponse response = new UserResponse();

        if ("userId".equals(type) && userMapper.countByUserId(value) > 0) {
            response.setUserExists(UserStatus.USERID_EXISTS);
        } else if ("userEmail".equals(type) && userMapper.countByUserEmail(value) > 0) {
            response.setUserExists(UserStatus.EMAIL_EXISTS);
        } else {
            response.setUserExists(UserStatus.NOT_FOUND);
        }

        return response;
    }

    public UserResponse registerUser(UserRequest userRequest) {

        UserResponse response = new UserResponse();

        //중복 검사
        if (userMapper.countByUserId(userRequest.getUserId()) > 0) {
            response.setUserExists(UserStatus.USERID_EXISTS);
            response.setMessage("이미 사용 중인 아이디입니다.");
            return response;
        }
        if (userMapper.countByUserEmail(userRequest.getUserEmail()) > 0) {
            response.setUserExists(UserStatus.EMAIL_EXISTS);
            response.setMessage("이미 등록된 이메일입니다.");
            return response;
        }

        if (userRequest.getUserUuid() == null) {
            logger.info("? => {}" ,userRequest.getUserUuid());
            userRequest.setUserUuid(UUID.randomUUID()); //UUID 자동 생성
        }

        //비밀번호 암호화 & 암호화된 비밀번호로 객체 업데이트
        String encodedPassword = passwordEncoder.encode(userRequest.getUserPassword());
        userRequest.setUserPassword(encodedPassword);

        //회원가입 처리 (DB에 저장)
        int result = userMapper.insertUser(userRequest);

        if(result > 0) {
            response.setUserRegStatus(UserStatus.USER_REGISTER_SUCCESS);
            response.setMessage("회원가입이 완료되었습니다.");
        } else {
            response.setUserRegStatus(UserStatus.USER_REGISTER_FAIL);
            response.setMessage("회원가입에 실패하였습니다. 다시 시도해주세요..");
        }
        return response;
    }

    public UserResponse getUserById(String userId){

        //회원정보
        UserResponse response = userMapper.findByUserId(userId);
        return response;
    }

    public UserResponse modifyUser(UserRequest userRequest){

        UserResponse response = new UserResponse();
        logger.info("service ::: userRequest => {}", userRequest);

        //이메일 중복검사 수정.
        if (userRequest.getUserEmail() != null && !userRequest.getUserEmail().isEmpty()) { // null 체크 추가
            if (userMapper.countByUserEmail(userRequest.getUserEmail()) > 0) {
                response.setUserExists(UserStatus.EMAIL_EXISTS);
                response.setMessage("이미 등록된 이메일입니다.");
                return response;
            }
        }

        //비밀번호 수정으로 입력이 되었다면 암호화 저장처리..
        if (userRequest.getUserPassword() != null && !userRequest.getUserPassword().isEmpty()) { //null 체크 추가
            //비밀번호 암호화 & 암호화된 비밀번호로 객체 업데이트
            String encodedPassword = passwordEncoder.encode(userRequest.getUserPassword());
            userRequest.setUserPassword(encodedPassword);
        }
        logger.info("mapper ::: userRequest => {}", userRequest);

        //회원정보 처리 (DB에 저장)
        int result = userMapper.modifyUser(userRequest);
        logger.info("result ::: userRequest => {}", userRequest);
        if(result > 0) {
            response.setUserRegStatus(UserStatus.USER_REGISTER_SUCCESS);
            response.setMessage("회원가입이 완료되었습니다.");
        } else {
            response.setUserRegStatus(UserStatus.USER_REGISTER_FAIL);
            response.setMessage("회원가입에 실패하였습니다. 다시 시도해주세요..");
        }
        return response;
    }

}

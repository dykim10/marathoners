package com.project.marathon.service;

import com.project.marathon.controller.UserController;
import com.project.marathon.dto.UserRequest;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.entity.User;
import com.project.marathon.enums.UserStatus;
import com.project.marathon.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //BCrypt 추가
    private final AuthService authService; //AuthService 추가

    public UserService(UserMapper userMapper, AuthService authService) {
        this.userMapper = userMapper;
        this.authService = authService;
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

        //회원정보 처리 (DB에 저장)
        int result = userMapper.modifyUser(userRequest);
        if(result > 0) {
            response.setUserRegStatus(UserStatus.USER_REGISTER_SUCCESS);
            response.setMessage("회원가입이 완료되었습니다.");
        } else {
            response.setUserRegStatus(UserStatus.USER_REGISTER_FAIL);
            response.setMessage("회원가입에 실패하였습니다. 다시 시도해주세요..");
        }
        return response;
    }

    public UserResponse deleteUser(String userUuid, HttpServletRequest request, HttpServletResponse response) {
        UserResponse userResponse  = new UserResponse();

        //회원정보 처리 (DB에 저장)
        int result = userMapper.deleteUser(userUuid);

        if(result > 0) {
            userResponse.setUserRegStatus(UserStatus.USER_REGISTER_SUCCESS);
            userResponse.setMessage("회원탈퇴 처리가 완료되었습니다.");
            
            //세션 및 쿠키 제거
            authService.logout(request, response);
        } else {
            userResponse.setUserRegStatus(UserStatus.USER_REGISTER_FAIL);
            userResponse.setMessage("회원탈퇴 처리에 실패하였습니다. 다시 시도해주세요..");
        }
        return userResponse;
    }

    public List<UserResponse> getUserList(String keyword, int page, int rows) {
        int offset = (page - 1) * rows;
        List<UserResponse> userlist =  userMapper.getUserList(keyword, rows, offset);

        logger.info(userlist.toString());
        return userlist;
    }

    public int getTotalUserCount(String keyword) {
        int totalCnt = userMapper.getTotalUserCount(keyword);
        logger.info("totalCnt => {} ", totalCnt);
        return totalCnt;
    }

}

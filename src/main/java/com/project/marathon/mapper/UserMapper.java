package com.project.marathon.mapper;

import com.project.marathon.dto.UserRequest;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<UserResponse> findByUsername(@Param("userName") String userName);
    void insertUser(UserRequest user);

    @org.apache.ibatis.annotations.ResultType(com.project.marathon.dto.UserResponse.class) // 패키지 경로 명확하게 지정
    UserResponse findByUserId(@Param("userId") String userId);

    //마지막로그인시간 업데이트
    User lastLoginDateUpdate(String userId);
}

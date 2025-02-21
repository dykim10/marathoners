package com.project.marathon.mapper;

import com.project.marathon.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByUsername(@Param("userName") String userName);
    void insertUser(User user);

    Optional<User> findByUserId(String userId); //Optional<User> 사용

    //마지막로그인시간 업데이트
    void lastLoginDateUpdate(String userId);
}

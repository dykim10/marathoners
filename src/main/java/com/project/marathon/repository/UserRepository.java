package com.project.marathon.repository;

import com.project.marathon.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {
    Users findByUserName(@Param("userName") String userName);
    void insertUser(Users user);

    Optional<Users> findByUserId(@Param("userId") String userId);
}

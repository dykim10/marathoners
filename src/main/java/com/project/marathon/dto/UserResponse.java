package com.project.marathon.dto;

import com.project.marathon.entity.User;
import com.project.marathon.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

@Data
@EqualsAndHashCode(callSuper = true) // 부모 클래스 필드까지 포함
@Alias("UserResponse") // MyBatis에서 'UserResponse'라는 별칭으로 사용
public class UserResponse extends User {
    private String token;
    private UserStatus userExists;  //아이디, 이메일 중복 검사 확인 변수
    private UserStatus userRegStatus;   //회원가입 상태 변수
    private String message;
}
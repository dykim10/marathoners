package com.project.marathon.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data  // Lombok 사용 (Getter/Setter 자동 생성)
public class User {
    private UUID userUuid = UUID.randomUUID(); // UUID 기본값 설정
    private String userId;                  // 유저 아이디
    private String userEmail;               // 유저 이메일 주소
    private String userName;                // 유저 성명
    private String userPassword;                 // 유저 비밀번호
    private String userHp;                  // 휴대전화번호
    private LocalDateTime userRegDt;        // 유저 가입일
    private LocalDateTime userUnRegDt;      // 유저 탈퇴일
    private LocalDateTime userLastLoginDt;  // 마지막 로그인 일자
    private String userStatus;              // 유저 상태
    private String userCi;                  // 유저 CI, DI 키값
    private String userRole;                // 유저 역할
    private String userIdentificationYn;    // 본인인증 여부

}

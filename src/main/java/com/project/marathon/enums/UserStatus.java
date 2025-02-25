package com.project.marathon.enums;

public enum UserStatus {
    USERID_EXISTS,    //  userId가 이미 있다.
    EMAIL_EXISTS,   //  userEmail이 이미 있다.
    NOT_FOUND,       //  사용할 수 있다.
    USER_REGISTER_SUCCESS,    //회원가입 DB insert 성공
    USER_REGISTER_FAIL        //회원가입 DB insert 실패
}

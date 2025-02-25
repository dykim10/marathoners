package com.project.marathon.dto;

import com.project.marathon.entity.User;

import java.util.UUID;

public class UserRequest extends User {
    public UserRequest() {
        if (super.getUserUuid() == null) {
            super.setUserUuid(UUID.randomUUID()); // ✅ UUID가 없으면 자동 생성
        }
    }
}

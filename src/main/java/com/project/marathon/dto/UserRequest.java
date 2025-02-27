package com.project.marathon.dto;

import com.project.marathon.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true) // 부모 클래스 필드까지 포함
@Alias("UserRequest") // MyBatis에서 'UserResponse'라는 별칭으로 사용
public class UserRequest extends User {
    public UserRequest() {
        if (super.getUserUuid() == null) {
            super.setUserUuid(UUID.randomUUID()); // ✅ UUID가 없으면 자동 생성
        }
    }
}

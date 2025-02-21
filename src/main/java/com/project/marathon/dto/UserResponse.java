package com.project.marathon.dto;

import com.project.marathon.entity.User;
import lombok.Data;

@Data
public class UserResponse extends User {
    private String token;

}
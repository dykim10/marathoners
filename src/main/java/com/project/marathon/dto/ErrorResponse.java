package com.project.marathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;    // HTTP 상태 코드
    private String message; // 에러 메시지
}

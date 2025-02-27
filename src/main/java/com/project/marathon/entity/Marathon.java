package com.project.marathon.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class Marathon {

    private UUID mrUuid = UUID.randomUUID();    // 마라톤대회 UUID 기본값 설정
    private String mrName;                      // 대회 명
    private String mrStartDate;                 // 대회 일자 YYYY-MM-DD
    private String mrLocation;                  // 대회 장소
    private String mrContents;                  // 대회 내용
    private String mrCompany;                   // 대회 주관사
}

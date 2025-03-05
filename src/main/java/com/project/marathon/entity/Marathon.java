package com.project.marathon.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Data
public class Marathon {
    // 대회 코드 uuid
    private UUID mrUuid = UUID.randomUUID();    // 마라톤대회 UUID 기본값 설정
    // 대회명
    private String mrName;
    // 대회일자 YYYY-MM-DD
    private LocalDate mrStartDt;
    // 대회 장소
    private String mrLocation;
    // 대회 소개-설명-안내
    private String mrContent;
    // 대회 주관사
    private String mrCompany;
    // 대회 등록 관리자
    private String mrRegAdm;
    // 대회 관리 등록일
    private String mrRegDt;
    // 대회 관리 수정일
    private String mrModDt;
    // 대회 종료 전N/후Y 여부
    private String mrFinalStatus;
    // 대회 활성화 여부
    private String mrUseYn;
    // 대회 기타 정보
    private String mrEtcMemo;
    // 대회 홈페이지 URL
    private String mrHomepageUrl;

    // 대회정보 수정 관리자
    private String mrModAdm;
    
}

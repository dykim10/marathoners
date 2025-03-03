package com.project.marathon.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MarathonRequestDto {
    private UUID mrUuid = UUID.randomUUID(); // UUID 기본값 설정
    private String mrName;
    private String mrStartDt;
    private String mrLocation;
    private String mrCompany;
    private String mrContent;
    private String mrHomepageUrl;
    private List<RaceCourseDetailDto> raceCourseDetails;
}

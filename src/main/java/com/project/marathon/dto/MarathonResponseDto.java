package com.project.marathon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarathonResponseDto {
    private String mrUuid;
    private String mrName;
    private String mrStartDt;
    private String mrLocation;
    private String mrCompany;
    private String mrContent;
    private String mrHomepageUrl;
    private List<RaceCourseDetailDto> raceCourseDetails;
}

package com.project.marathon.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceCourseDetailDto {
    private int seq;
    private int mrCourseVersion;            //코스수정 버전
    private int mrCoursePrice;              //코스가격
    private int mrCourseCapacity;           //모집인원

    private String mrUuid;
    private String mrCourseType;            //코스종류
    private String mrCourseTypeEtcText;     //기타코스
}
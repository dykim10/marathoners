package com.project.marathon.dto;

import com.project.marathon.entity.Marathon;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true) // 부모 클래스 필드까지 포함
public class MarathonRequestDto extends Marathon {

    private String keyword;
    private int offset;
    private int rows;
    private String year;
    private String month;

    private List<RaceCourseDetailDto> raceCourseDetails;
}

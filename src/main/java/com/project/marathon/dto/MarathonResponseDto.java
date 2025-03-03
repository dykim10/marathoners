package com.project.marathon.dto;

import com.project.marathon.entity.Marathon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // 부모 클래스 필드까지 포함
public class MarathonResponseDto extends Marathon {
    private Integer rownum;  // ✅ 추가: 번호 필드
    private List<RaceCourseDetailDto> raceCourseDetails;
}

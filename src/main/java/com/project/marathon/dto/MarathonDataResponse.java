package com.project.marathon.dto;

import com.project.marathon.entity.Marathon;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

@Data


public class MarathonDataResponse extends Marathon {


    //TO-DO 대회 진행 코스를 리스트형태로 보관해서 노출해주는 작업 스킬
    //private List<CourseDto> courceList;
}
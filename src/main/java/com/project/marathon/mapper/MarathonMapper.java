package com.project.marathon.mapper;

import com.project.marathon.dto.MarathonRequestDto;
import com.project.marathon.dto.MarathonResponseDto;
import com.project.marathon.dto.RaceCourseDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MarathonMapper {
    //tb_marathon_race에 1행 INSERT 후 UUID 반환
    int insertMarathonRace(MarathonRequestDto requestDto);

    //tb_marathon_course에 raceDetail 데이터 INSERT
    void insertMarathonCourse(String mrUuid, @Param("raceCourseDetails") List<RaceCourseDetailDto> raceCourseDetails);

    List<MarathonResponseDto> getMarathonRaceList(MarathonRequestDto requestDto);
    int getMarathonRaceTotalCount(MarathonRequestDto requestDto);

    MarathonResponseDto getMarathonDetail(String mrUuid);
    List<RaceCourseDetailDto> getLatestRaceCourseList(String mrUuid);

}

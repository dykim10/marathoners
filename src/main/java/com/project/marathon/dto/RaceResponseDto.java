package com.project.marathon.dto;

import lombok.Data;

import java.util.List;

@Data
public class RaceResponseDto {
    private List<MarathonResponseDto> raceList;
    private int totalRows;

}

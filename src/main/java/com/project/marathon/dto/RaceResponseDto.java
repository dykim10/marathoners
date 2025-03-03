package com.project.marathon.dto;

import lombok.Data;

import java.util.List;

@Data
public class RaceResponseDto {
    private List<MarathonResponseDto> raceList;
    private int totalRows; // 전체 데이터 개수
    private int totalPages; // 전체 페이지 개수
    private int currentPage; // 현재 페이지 번호
    private int rowsPerPage; // 한 페이지에 표시할 개수

}

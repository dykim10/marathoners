package com.project.marathon.dto;

import lombok.Data;

@Data
public class UserSearchDto {

    private String keyword;
    private int page;
    private int rows;
}

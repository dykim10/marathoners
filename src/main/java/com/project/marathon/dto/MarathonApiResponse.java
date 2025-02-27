package com.project.marathon.dto;

import lombok.Data;
import java.util.List;

@Data
public class MarathonApiResponse {
    private List<MarathonDataResponse> data; // API 응답에서 "data" 필드명을 정확하게 맞춰야 함
}

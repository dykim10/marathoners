package com.project.marathon.controller;

import com.project.marathon.dto.MarathonDataResponse;
import com.project.marathon.service.MarathonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MarathonController {
    private final MarathonService marathonService;

    public MarathonController(MarathonService marathonService) {
        this.marathonService = marathonService;
    }

    @GetMapping("/marathon/fetch")
    public String fetchMarathonData() {
        marathonService.fetchAndPrintMarathonData();
        return "Data fetched and printed in console";
    }

    /**
     * DB 대회 리스트 전달.
     * @return
     */
    @GetMapping
    public ResponseEntity<List<MarathonDataResponse>> getMarathonList() {
        List<MarathonDataResponse> marathonData = marathonService.getMarathonDataList();

        if (marathonData.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없을 경우 204 응답
        }

        return ResponseEntity.ok(marathonData); // 정상 데이터 반환 (HTTP 200)
    }

}

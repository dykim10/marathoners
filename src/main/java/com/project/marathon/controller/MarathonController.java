package com.project.marathon.controller;

import com.project.marathon.dto.MarathonDataResponse;
import com.project.marathon.dto.MarathonRequestDto;
import com.project.marathon.dto.MarathonResponseDto;
import com.project.marathon.service.MarathonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marathon")
public class MarathonController {
    private final MarathonService marathonService;

    public MarathonController(MarathonService marathonService) {
        this.marathonService = marathonService;
    }


    /**
     * DB 대회 리스트 전달.
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<MarathonDataResponse>> getMarathonList() {
        List<MarathonDataResponse> marathonData = marathonService.getMarathonDataList();

        if (marathonData.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없을 경우 204 응답
        }

        return ResponseEntity.ok(marathonData); // 정상 데이터 반환 (HTTP 200)
    }

    /**
     * DB 대회 등록 후 전달.
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerMarathon(@RequestBody MarathonRequestDto requestDto) {
        try {
            MarathonResponseDto response = marathonService.registerMarathon(requestDto);
            return ResponseEntity.ok(response); // ✅ HTTP 200 + MarathonResponseDto 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패: " + e.getMessage());
        }
    }
}

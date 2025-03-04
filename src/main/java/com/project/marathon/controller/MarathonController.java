package com.project.marathon.controller;

import com.project.marathon.dto.MarathonRequestDto;
import com.project.marathon.dto.MarathonResponseDto;
import com.project.marathon.dto.RaceResponseDto;
import com.project.marathon.dto.UserResponse;
import com.project.marathon.enums.UserStatus;
import com.project.marathon.service.MarathonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marathon")
public class MarathonController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final MarathonService marathonService;

    public MarathonController(MarathonService marathonService) {
        this.marathonService = marathonService;
    }


    /**
     * DB 대회 리스트 전달.
     * @return
     */
    @PostMapping("/list")
    public ResponseEntity<?> getMarathonList(@RequestBody MarathonRequestDto requestDto) {

        RaceResponseDto raceResponseDto = marathonService.getMarathonDataList(requestDto);
        return ResponseEntity.ok(raceResponseDto); // 정상 데이터 반환 (HTTP 200)
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

    /**
     *
     * @param mrUuid
     * @return
     */
    @GetMapping("/detail/{mrUuid}")
    public ResponseEntity<RaceResponseDto> getRaceDetail(@PathVariable String mrUuid) {
        RaceResponseDto raceDetail = marathonService.getMarathonDetailWithCourses(mrUuid);

        if (raceDetail.getRaceInfo() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(raceDetail);
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyMarathon(@RequestBody MarathonRequestDto requestDto) {
        logger.info("controller ::: userRequest => {}", requestDto);

        RaceResponseDto response = marathonService.updateMarathonRace(requestDto);
        return ResponseEntity.ok(response); // 성공 시 200 OK 반환
    }

}

package com.project.marathon.service;

import com.project.marathon.dto.MarathonApiResponse;
import com.project.marathon.dto.MarathonDataResponse;

import com.project.marathon.dto.MarathonRequestDto;
import com.project.marathon.dto.MarathonResponseDto;
import com.project.marathon.mapper.MarathonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor

public class MarathonService {
    private static final Logger logger = LoggerFactory.getLogger(MarathonService.class);
    private final WebClient webClient;
    private final MarathonMapper marathonMapper;

        @Value("${public.api.service-key}")
    private String serviceKey;

    private static final String BASE_URL = "https://api.odcloud.kr/api/15138980/v1/uddi:eedc77c5-a56b-4e77-9c1d-9396fa9cc1d3";
    //https://api.odcloud.kr/api/15138980/v1/uddi:eedc77c5-a56b-4e77-9c1d-9396fa9cc1d3?serviceKey=Zcfv%2Fo8xqsCuuPEht%2FsTuNPf0GJK43LLyi2fGmOagiVXG2Xl%2BEVTzhkuOhQvngART1BeBn7bVhCZROt3iR44bQ%3D%3D&otherParam=value&page=1&perPage=100
    // WebClient.Builder를 주입받아 사용
    @Autowired
    public MarathonService(WebClient.Builder webClientBuilder, MarathonMapper marathonMapper) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.marathonMapper = marathonMapper;
    }

    public void fetchAndPrintMarathonData() {
        // serviceKey를 URL 인코딩
        //String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
        String encodedServiceKey = serviceKey;
        String requestUrl = BASE_URL + "?serviceKey=" + encodedServiceKey + "&page=1&perPage=100";
        logger.info("Request URL: {}", requestUrl);

        // 즉, Mono는 공공 데이터 API의 JSON 응답을 MarathonApiResponse 객체로 변환하는 역할을 합니다.
        //🚀 즉, Mono가 없으면 WebClient의 응답을 처리할 수 없으며, 데이터를 가져올 수 없음!
        List<MarathonDataResponse> marathonDataList = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey", encodedServiceKey) // URL 인코딩된 serviceKey 사용
                        .queryParam("page", 1)
                        .queryParam("perPage", 100)
                        .build())
                .retrieve()
                .bodyToMono(MarathonApiResponse.class)
                .map(MarathonApiResponse::getData)
                .block(); // 동기 처리

        if (marathonDataList != null) {
            marathonDataList.forEach(System.out::println); // 데이터 출력
        } else {
            System.out.println("No data received from API.");
        }
    }

    /**
     * DB에서 불러오는 list 추후 활용할 것. 미리 만들어두는..
     * @return
     */
    public List<MarathonDataResponse> getMarathonDataList() {
        // 1. 데이터를 API 또는 DB에서 가져와야 함 (예제에서는 더미 데이터 사용)
        List<MarathonDataResponse> marathonDataResponseList = new ArrayList<>();

        // 더미 데이터 추가 (실제 API 호출 또는 DB 조회로 대체)
//        marathonDataResponseList.add(new MarathonDataResponse("서울 마라톤", "서울", "2025-04-10", "대한육상연맹"));
//        marathonDataResponseList.add(new MarathonDataResponse("부산 국제 마라톤", "부산", "2025-05-20", "부산육상연맹"));

        return marathonDataResponseList; // ResponseEntity 사용 X
    }

    public MarathonResponseDto registerMarathon(MarathonRequestDto requestDto) {
        try {
            // UUID 자동 생성;
            requestDto.setMrUuid(UUID.randomUUID());

            // tb_marathon_race 테이블에 1행 INSERT
            int result = marathonMapper.insertMarathonRace(requestDto);
            if (result <= 0) {
                throw new RuntimeException("❌ tb_marathon_race INSERT 실패!");
            }

            // tb_marathon_course 테이블에 raceDetail 데이터 INSERT
            if (requestDto.getRaceCourseDetails() != null && !requestDto.getRaceCourseDetails().isEmpty()) {
                marathonMapper.insertMarathonCourse(requestDto.getMrUuid().toString(), requestDto.getRaceCourseDetails());
                log.info("✅ tb_marathon_course INSERT 성공!");
            }

            MarathonResponseDto res = new MarathonResponseDto();
            res.setMrUuid(requestDto.getMrUuid().toString());

            return res;

        } catch (Exception e) {
            //예외 발생 시 전체 트랜잭션 롤백
            throw new RuntimeException("마라톤 등록 중 오류 발생: " + e.getMessage(), e);
        }
    }

}

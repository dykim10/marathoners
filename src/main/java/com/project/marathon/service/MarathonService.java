package com.project.marathon.service;

import com.project.marathon.dto.*;
import com.project.marathon.mapper.MarathonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarathonService {
    private static final Logger logger = LoggerFactory.getLogger(MarathonService.class);

    private final MarathonMapper marathonMapper;

    @Value("${public.api.service-key}")
    private String serviceKey;

    private static final String BASE_URL = "https://api.odcloud.kr/api/15138980/v1/uddi:eedc77c5-a56b-4e77-9c1d-9396fa9cc1d3";
    //https://api.odcloud.kr/api/15138980/v1/uddi:eedc77c5-a56b-4e77-9c1d-9396fa9cc1d3?serviceKey=Zcfv%2Fo8xqsCuuPEht%2FsTuNPf0GJK43LLyi2fGmOagiVXG2Xl%2BEVTzhkuOhQvngART1BeBn7bVhCZROt3iR44bQ%3D%3D&otherParam=value&page=1&perPage=100
    // WebClient.Builder를 주입받아 사용

    /**
     * localhost 에서 정부공공데이터를 접근하는게, 작동 시 문제 소지가 있을 것 같아서 중단.
     **/
//    public void fetchAndPrintMarathonData() {
//        // serviceKey를 URL 인코딩
//        //String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
//        String encodedServiceKey = serviceKey;
//        String requestUrl = BASE_URL + "?serviceKey=" + encodedServiceKey + "&page=1&perPage=100";
//        logger.info("Request URL: {}", requestUrl);
//
//        // 즉, Mono는 공공 데이터 API의 JSON 응답을 MarathonApiResponse 객체로 변환하는 역할을 합니다.
//        //🚀 즉, Mono가 없으면 WebClient의 응답을 처리할 수 없으며, 데이터를 가져올 수 없음!
//        List<MarathonDataResponse> marathonDataList = webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .queryParam("serviceKey", encodedServiceKey) // URL 인코딩된 serviceKey 사용
//                        .queryParam("page", 1)
//                        .queryParam("perPage", 100)
//                        .build())
//                .retrieve()
//                .bodyToMono(MarathonApiResponse.class)
//                .map(MarathonApiResponse::getData)
//                .block(); // 동기 처리
//
//        if (marathonDataList != null) {
//            marathonDataList.forEach(System.out::println); // 데이터 출력
//        } else {
//            System.out.println("No data received from API.");
//        }
//    }

    /**
     * DB에서 불러오는 list 추후 활용할 것. 미리 만들어두는..
     * @return
     */
    public RaceResponseDto getMarathonDataList(MarathonRequestDto requestDto) {

        // 새로운 RaceResponseDto 객체 생성
        RaceResponseDto raceResponseDto = new RaceResponseDto();
        List<MarathonResponseDto> datalist = marathonMapper.getMarathonRaceList(requestDto);
        int totalRows = marathonMapper.getMarathonRaceTotalCount(requestDto); // 전체 데이터 개수 조회

        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalRows / requestDto.getRows());

        // MarathonResponseDto 리스트를 RaceResponseDto에 할당
        raceResponseDto.setRaceList(datalist);
        raceResponseDto.setTotalRows(totalRows);
        raceResponseDto.setTotalPages(totalPages);

        // Controller로 반환 (ResponseEntity 사용 X)
        return raceResponseDto;
    }

    public MarathonResponseDto registerMarathon(MarathonRequestDto requestDto) {
        try {
            // UUID 자동 생성;
            requestDto.setMrUuid(UUID.randomUUID());

            // tb_marathon_race 테이블에 1행 INSERT
            int result = marathonMapper.insertMarathonRaceData(requestDto);
            if (result <= 0) {
                throw new RuntimeException("tb_marathon_race INSERT 실패!");
            }

            // tb_marathon_course 테이블에 raceDetail 데이터 INSERT
            if (requestDto.getRaceCourseDetails() != null && !requestDto.getRaceCourseDetails().isEmpty()) {
                marathonMapper.insertMarathonCourse(requestDto.getMrUuid().toString(), requestDto.getRaceCourseDetails());
                log.info("tb_marathon_course INSERT 성공!");
            }

            MarathonResponseDto res = new MarathonResponseDto();
            res.setMrUuid(requestDto.getMrUuid());

            return res;

        } catch (Exception e) {
            //예외 발생 시 전체 트랜잭션 롤백
            throw new RuntimeException("마라톤 등록 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public RaceResponseDto getMarathonDetailWithCourses(String mrUuid) {

        // 기본 대회 정보 가져오기
        MarathonResponseDto raceInfo = marathonMapper.getMarathonDetail(mrUuid);

        // 최신 코스 정보 리스트 가져오기 (mr_course_version 기준 최신순)
        List<RaceCourseDetailDto> raceCourseList = marathonMapper.getLatestRaceCourseList(mrUuid);

        RaceResponseDto resDto = new RaceResponseDto();
        resDto.setRaceInfo(raceInfo);
        resDto.setRaceCourseDetailList(raceCourseList);
        return resDto;
    }

    public RaceResponseDto updateMarathonRace(MarathonRequestDto requestDto) {

        int resultCode = marathonMapper.updateMarathonRaceData(requestDto);

        RaceResponseDto resDto = new RaceResponseDto();
        return resDto;
    }

    /**
     * 크롤링
     */
    private final String URL = "http://www.roadrun.co.kr/schedule/list.php";
    public void crawlAndSaveMarathonEvents() {
        try {
            // 1. URL에서 HTML 가져오기
            Document doc = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(5000)
                    .get();

            // 2. 첫 번째 테이블 찾기
            Element firstTable = doc.select("table").first();
            if (firstTable == null) {
                System.out.println("첫 번째 테이블을 찾을 수 없습니다.");
                return;
            }

            // 3. 첫 번째 테이블의 세 번째 <tr> 찾기
            Elements trElements = firstTable.select("tr");
            if (trElements.size() < 3) {
                System.out.println("세 번째 <tr>를 찾을 수 없습니다.");
                return;
            }
            Element thirdTr = trElements.get(2);

            // 4. 첫 번째 <td> 찾기
            Element firstTd = thirdTr.select("td").first();
            if (firstTd == null) {
                System.out.println("첫 번째 <td>를 찾을 수 없습니다.");
                return;
            }

            // 5. 내부 <table> 구조 접근
            Element nestedTable = firstTd.select("table").last(); // 가장 안쪽 테이블 선택
            if (nestedTable == null) {
                System.out.println("내부 테이블을 찾을 수 없습니다.");
                return;
            }

            // 6. 대상 데이터 (리스트 시작 부분) 접근
            Element targetTable = nestedTable.select("tbody").first();
            if (targetTable == null) {
                System.out.println("리스트 테이블을 찾을 수 없습니다.");
                return;
            }

            Elements rows = targetTable.select("tr"); // 대상 데이터

            System.out.println("🎯 크롤링된 데이터 리스트: ");
            for (Element row : rows) {
                System.out.println("🔹 " + row.text());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MarathonEventDto> getAllEvents() {
        return marathonMapper.getAllMarathonEvents();
    }

}

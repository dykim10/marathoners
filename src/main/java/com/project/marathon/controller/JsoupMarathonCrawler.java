package com.project.marathon.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.nio.charset.StandardCharsets;

public class JsoupMarathonCrawler {
    public static void main(String[] args) {
        try {
            // 1. Jsoup으로 웹페이지 가져오기 (UTF-8 설정)
            Connection.Response response = Jsoup.connect("https://www.sub-3.com/g5/bbs/board.php?bo_table=tb_meetings")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .header("Accept-Language", "ko-KR,ko;q=0.9")
                    .header("Accept-Charset", "UTF-8")
                    .ignoreContentType(true)  // Content-Type 무시
                    .execute();

            // 2. 응답을 UTF-8로 변환 후 HTML 파싱
            Document doc = Jsoup.parse(new String(response.bodyAsBytes(), StandardCharsets.UTF_8));

            // 3. 페이지 인코딩 확인
            System.out.println("페이지 인코딩 >>> " + doc.charset());

            // 4. HTML에서 마라톤 대회 일정 추출 (테이블 탐색)
            Elements rows = doc.select("table tbody tr");

            System.out.println(">>>  크롤링된 마라톤 대회 일정:");
            for (Element row : rows) {
                Elements columns = row.select("td");
                if (columns.size() >= 4) { // 데이터가 4개 이상 존재할 경우
                    String date = columns.get(0).text().trim();
                    String name = columns.get(1).text().trim();
                    String location = columns.get(2).text().trim();
                    String website = columns.get(3).select("a").attr("href");

                    System.out.println(date);
                    System.out.println(name);
                    System.out.println(location);
                    System.out.println(website);
                    System.out.println("---------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

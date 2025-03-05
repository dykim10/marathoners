package com.project.marathon.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.nio.charset.Charset;

/**
 * encoding 이슈로 포기. 25.03.05
 * ---------------------------
 * ��¥:
 * ��ȸ��:
 * ���:
 * ������Ʈ:
 * ---------------------------

public class JsoupMarathonCrawler {
    public static void main(String[] args) {
        try {
            // 1. Jsoup으로 웹페이지 가져오기 (EUC-KR 인코딩 지정)
            Connection.Response response = Jsoup.connect("http://www.roadrun.co.kr/schedule/list.php")
                    .header("Content-Type", "text/html; charset=EUC-KR")
                    .ignoreContentType(true)  // 컨텐츠 타입 무시
                    .execute();

            // 2. 응답을 EUC-KR로 변환 후 HTML 파싱
            Document doc = Jsoup.parse(
                    new String(response.bodyAsBytes(), Charset.forName("EUC-KR"))
            );

            // 3. 마라톤 대회 일정이 포함된 테이블 탐색
            Elements rows = doc.select("table tbody tr");

            System.out.println("> 크롤링된 마라톤 대회 일정:");
            for (Element row : rows) {
                Elements columns = row.select("td");
                if (columns.size() >= 4) {
                    String date = columns.get(0).text().trim();
                    String name = columns.get(1).text().trim();
                    String location = columns.get(2).text().trim();
                    String website = columns.get(3).select("a").attr("href");

                    System.out.println("날짜: " + date);
                    System.out.println("대회명: " + name);
                    System.out.println("장소: " + location);
                    System.out.println("웹사이트: " + website);
                    System.out.println("---------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
 */
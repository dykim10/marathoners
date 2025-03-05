package com.project.marathon.controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SeleniumMarathonCrawler {

    public static void main(String[] args) {
        // 1. Chrome WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        // 2. 브라우저 실행 옵션 설정 (헤드리스 모드)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // UI 없이 실행 (배포 환경에서 필요)
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        // 3. WebDriver 실행
        WebDriver driver = new ChromeDriver(options);
        driver.get("http://www.roadrun.co.kr/schedule/list.php");

        // 4. Selenium으로 HTML 가져오기
        String pageSource = driver.getPageSource();
        driver.quit(); // WebDriver 종료

        try {
            // 5. Jsoup으로 HTML 파싱
            Document doc = Jsoup.parse(pageSource);

            // 6. 마라톤 대회 일정이 포함된 테이블 탐색
            Elements rows = doc.select("table tbody tr");

            System.out.println("🎯 크롤링된 마라톤 대회 일정:");
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

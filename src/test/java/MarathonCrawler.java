import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MarathonCrawler {
    public static void main(String[] args) {
        String url = "http://www.roadrun.co.kr/schedule/list.php";

        try {
            // 1. HTML 가져오기
            Document document = Jsoup.connect(url).get();

            // 2. 특정 테이블 가져오기 (예제: 일정 테이블의 클래스 또는 태그)
            Element table = document.select("table").first(); // 가장 첫 번째 테이블 선택
            if (table != null) {
                Elements rows = table.select("tr"); // 행 가져오기

                System.out.println(rows);
//                // 3. 각 행에서 데이터 추출
//                for (Element row : rows) {
//                    Elements cols = row.select("td");
//                    if (cols.size() > 1) {
//                        String date = cols.get(0).text(); // 날짜
//                        String event = cols.get(1).text(); // 대회명
//                        String location = cols.get(2).text(); // 장소
//                        String link = cols.get(1).select("a").attr("href"); // 대회 상세 링크
//
//                        System.out.println("날짜: " + date);
//                        System.out.println("대회명: " + event);
//                        System.out.println("장소: " + location);
//                        System.out.println("링크: " + link);
//                        System.out.println("----------------------------");
//                    }
//                }
            } else {
                System.out.println("일정 테이블을 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

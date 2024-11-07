package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//주제 : 실시간 주식 정보 웹 크롤링 해서 얻기 


@WebServlet("/stock.do")
public class NaverStock extends HttpServlet{

	protected void doHandle(HttpServletRequest request, 
							 HttpServletResponse response) 
									 throws ServletException, IOException {
		//웹크롤링(페이지 전체 정보를 가져온다)할 요청할 전체 주소 저장
		String URL = "https://finance.naver.com/item/main.naver?code=005930";
		
		Document doc;//웹크롤링 해서 저장할 변수 
		
	
		try {
			//웹 크롤링할 전체 URL주소의 전체 코드를 얻어 온다.
			doc = Jsoup.connect(URL).get();
			
			//실시간 주식 정보가 보이는~ 날짜 시간 추출
			//Document객체의  select메소드를 통해서 특정요소들을  담은 Elements배열객체를 반환
			Elements elem = doc.select(".date");
			/*
			<em class="date">2024.10.31 14:35 <span>기준(장중)</span></em>
			<span class="date">(2024.06)</span>
			<span class="date">(2024.06)</span>
			*/
			
			//System.out.println( elem.text() );
				//"2024.10.31 14:41 기준(장중) (2024.06) (2024.06)"
			String[] str = elem.text().split(" ");
			//["2024.10.31", "14:41", "기준(장중)", "(2024.06)", "(2024.06)"]
			//      0           1        2               3          4  index
/*			
			for(int i=0;  i<str.length;   i++) {
				System.out.println(str[i]);
				
				2024.10.31    index 0
				14:46         index 1
				기준(장중)      index 2 
				(2024.06)     index 3
				(2024.06)     index 4
				
			}
*/		
//--------------------------------------------------------------------------
			
		Elements todaylist  = doc.select(".new_totalinfo dl>dd");
	//	System.out.println(todaylist);	
		
		
		String juga = todaylist.get(3).text().split(" ")[1];
		String DungRakrate = todaylist.get(3).text().split(" ")[6];
		String siga = todaylist.get(5).text().split(" ")[1];
		String goga = todaylist.get(6).text().split(" ")[1];
		String zeoga = todaylist.get(8).text().split(" ")[1];
		
		String georaeryang = todaylist.get(10).text().split(" ")[1];
		String stype = todaylist.get(3).text().split(" ")[3];
		String vsyesterday = todaylist.get(3).text().split(" ")[4];
		
		System.out.println("삼성전자 주가------------------");
		System.out.println("주가:"+juga);
		System.out.println("등락률:"+DungRakrate);
		System.out.println("시가:"+siga);
		System.out.println("고가:"+goga);
		System.out.println("저가:"+zeoga);
		System.out.println("거래량:"+georaeryang);
		System.out.println("타입:"+stype);
		System.out.println("전일대비:"+vsyesterday);
		System.out.println("가져오는 시간:"+str[0]+str[1]);
		
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, 
						 HttpServletResponse response) 
								 throws ServletException, IOException {
		doHandle(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, 
						  HttpServletResponse response) 
								  throws ServletException, IOException {
		doHandle(request, response);
	}	
}














package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/NaverSearchAPI.do")
public class SearchAPI extends HttpServlet {

// 핸들러 역할	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		//1. 인증정보 설정
		String clientId = "8GhH6nonoWAQzFzDUayF"; //애플리케이션 클라이언트 아이디
        String clientSecret = "J9qyhKvwAI"; //애플리케이션 클라이언트 시크릿
        
        
        //2. 검색조건 설정
        int startNum = 0;
        String text = null;  // 입력한 검색어
        
        try {
        	//검색 시작위치 받아오기
        	startNum = Integer.parseInt(req.getParameter("startNum"));
        	
        	//디자인 검색요청화면에서 입력한 검색어 얻기
        	String searchText = req.getParameter("keyword");
        	
        	// 검색어 받는 곳과, 한글처리
            text = URLEncoder.encode(searchText, "UTF-8");
            
            
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }
        
        
        //3. API 요청 주소 조합 (주소만들기) - ? 뒤에올 파라미터 값 작성  query=검색어, display=한번에 표시할 검색결과개수, start=검색시작위치
        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text
        			   + "&display=10&start="+startNum;    // JSON 데이터 응답받기 위한 네이버 서버에 요청할 주소
      
        //4. API 호출
        
        Map<String, String> requestHeaders = new HashMap<>();
        
        // 클라이언트 ID와 시크릿을 Http프로토콜로 요청할 때 Http요청메세지의 Header영역에 설정
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        
        // API를 호출해서 JSON데이터를 문자열 형태로 응답 받는다.
        String responseBody = get(apiURL,requestHeaders);

        //웹브라우저로 응답할 데이터 유형 설정
        resp.setContentType("text/thml; charset=utf-8");

        //getWriter객체 얻어 검색결과 보냄
        resp.getWriter().write(responseBody);
       
        
	}//service 닫기
	
	
	
//get 메소드
    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
            	
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
                
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


 // connect 메소드   
    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            
            return (HttpURLConnection)url.openConnection();
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


// readBody 메소드
    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

	
	
	
	
}

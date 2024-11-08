package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.CarDAO;
import Vo.CarConfirmVo;
import Vo.CarListVo;
import Vo.CarOrderVo;

//MVC중에서 C의 역할 


//index.jsp를 요청하면  location.href="/Car/Main";에 의해 CarController로
//CarMain.jsp(VIEW)화면을 재요청합니다.


			 
			 
@WebServlet("/Car/*")
public class CarController extends HttpServlet {

	private CarDAO cardao;	
 // private MemberDAO memberdao;
	
	@Override
	public void init() throws ServletException {
		cardao = new CarDAO();
//      memberdao = new MemberDAO();
	}
	
	
	
	@Override
	protected void doGet(HttpServletRequest request, 
						 HttpServletResponse response) 
								 throws ServletException, IOException {
		doHandle(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, 
						  HttpServletResponse response) 
								  throws ServletException, IOException {
		doHandle(request,response);
	}
	
	protected void doHandle(HttpServletRequest request, 
			  				HttpServletResponse response) 
			  						throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		//웹브라우저로 출력할 출력 스트림 생성
		PrintWriter pw = response.getWriter();
		
		
						
		String action = request.getPathInfo();//클라이언트가 요청한 2단계주소 얻기
		System.out.println("요청한 2단계 주소:"+ action);
			
		// /Main <- CarMain.jsp(VIEW) 메인화면 요청 
		// /bb   <- 예약하기  메뉴를 클릭해서 전체검색 또는 카테고리별 검색 화면 요청
		// /CarList.do <- 전체 차량 조회 요청 
		// /carcategory.do <- 차량 유형별(소형 또는 중형 또는 대형 중 조회) ! 조회 요청
		// /CarInfo.do <- carno번호에 해당하는 차량 한대의 정보 조회 요청
		// /CarOption.do <- 렌트를 위해 옵션 을 추가로 선택하는 화면 요청
		// /CarOptionResult.do  <- 추가한 옵션 금액 + 기본 금액  계산 요청
		// /CarOrder.do <- 비회원 예약 요청
		// /cc  <- 예약확인 하기 위해 예약당시 입력했던 비회원 핸드폰번호, 비밀번호를 
		//         입력하여 예약확인을 요청하는 디자인 VIEW를 요청!
		// /CarReserveConfirm.do <- 입력한 휴대폰번호와 비밀번호로 예약할 렌트 정보 조회 요청!
		// /update.do <- 예약한 정보 수정하기위해 예약한 아이디로 예약정보 조회요청!
		// /updatePro.do <- 입력한 정보 수정(UPDATE) 요청!!!
		// /delete.do <- 예약 취소를 위해 비밀번호를 입력해서 예약취소 요청하는 VIEW보여줘~요청!
		// /deletePro.do <- 예약 취소 (DELETE) 요청
		
		// //NaverSearchAPI.do  <-  네이버 서치 API 검색화면 요청
		
						
		//재요청할 경로 주소를 저장할 변수
		String nextPage = null;
		
		if(action.equals("/Main")) {//CarMain.jsp 메인화면 2단계 요청주소를 받으면
			
			nextPage = "/CarMain.jsp";
		
		}else if(action.equals("/bb")) {
//			/Car/bb?center=CarReservation.jsp
			
			//중앙화면 요청한 파라미터  주소얻기
			String center = request.getParameter("center");
			
			//request내장객체에 CarReservation.jsp 저장(바인딩)
			request.setAttribute("center", center);
			
			//메인 화면 재요청 을 위해 경로 저장
			nextPage = "/CarMain.jsp";
		
	    //전체 차량 조회 2단계 요청 주소를 받았을때		
		}else if(action.equals("/CarList.do")) {
			
			//모든 차량 조회 명령을 위해 
			//CarDAO객체의 getAllCarList()메소드 호출!
			//참고. 조회된 모든 차량 정보가 Model의 역할을 한다.
			Vector<CarListVo> vector = cardao.getAllCarList();
			
			//View 중앙화면에 조회된 Vector의 정보를 보여주기 위해
			//request내장객체에 바인딩
			request.setAttribute("v", vector);
			//View 중앙화면에 보일 파일명을 request내장객체에 바인딩
			request.setAttribute("center", "CarList.jsp");
			
			nextPage="/CarMain.jsp";
		
		//소형, 중형, 대형 중 선택한 유형의 차량 조회 요청주소를 받았을떄 
		}else if(action.equals("/carcategory.do")) {
			
			//소형(Small), 중형(Mid), 대형(Big) 중 선택한 option의 value속성값 얻기
			//요청한 값 얻기
			String category = request.getParameter("carcategory");
				   
			//선택한 유형의 차량을 조회 하기 위해  명령
			Vector vector = cardao.getCategoryList(category);
			
			request.setAttribute("v", vector);
			request.setAttribute("center", "CarList.jsp");
			
			nextPage="/CarMain.jsp";
			
	    //CarList.jsp조회된 화면에서 렌트 예약을 위해
	    //차량 한대의 정보를 감싸고 있는 <a>링크를 클릭하여
		//조회 요청을 받았을때 
		}else if(action.equals("/CarInfo.do")) {			
// /Car/CarInfo.do?carno=${vo.carno}			
			//조회시 사용할  차번호 얻기
			int carno = Integer.parseInt(request.getParameter("carno"));
			
			//요청받은 carno차번호에 해당하는 차량 정보를 조회 하기 위해
			//CarDAO객체의 getOneCar메소드 호출시 인자로 carno차번호 전달해서 조회해 옴
			CarListVo vo = cardao.getOneCar(carno);
						
			request.setAttribute("vo", vo);
			request.setAttribute("center", "CarInfo.jsp");
						
			nextPage="/CarMain.jsp";
		
		}else if(action.equals("/CarOption.do")) {
			
			
		//String carqty = request.getParameter("carqty");
		//int carno = Integer.parseInt(request.getParameter("carno"));
		//int carprice = Integer.parseInt(request.getParameter("carprice"));
		//String carimg = request.getParameter("carimg");
			
			//중앙 옵션 추가 요청 화면 VIEW 명을 request에 바인딩
			request.setAttribute("center", "CarOption.jsp");
			
			nextPage="/CarMain.jsp";
		
		
		}else if(action.equals("/CarOptionResult.do")) {
			//렌트 예약을 위해 선택했던 값들 얻기
			//요청한 값 얻기
			int carno = Integer.parseInt(request.getParameter("carno"));
			String carbegindate = request.getParameter("carbegindate");
			int carqty = Integer.parseInt(request.getParameter("carqty"));
			int carprice = Integer.parseInt(request.getParameter("carprice"));
			int carreserveday = Integer.parseInt(request.getParameter("carreserveday"));
			int carins = Integer.parseInt(request.getParameter("carins"));
			int carwifi = Integer.parseInt(request.getParameter("carwifi"));
			int carnave = Integer.parseInt(request.getParameter("carnave"));
			int carbabyseat = Integer.parseInt(request.getParameter("carbabyseat"));
			
			//응답할 값 마련 (렌트 할 금액 최종 계산, 비즈니스 로직 처리)
			
			//차량 기본 금액 계산 =  대여수량 * 대여금액 * 대여기간
			int totalreserve = carqty * carprice * carreserveday;
			
			//추가한 옵션 금액 계산
			int totaloption = 
			(carins + carwifi + carbabyseat) * carreserveday * 10000 * carqty;
			
			//웹브라우저로 응답할 VIEW(CarOrder.jsp)중앙화면에 보여주기 위해
			//CarOrderVO객체를 생성해서 각인스턴스변수에 저장 시킴
			CarOrderVo vo = new CarOrderVo();
			vo.setCarno(carno);
			vo.setCarqty(carqty);
			vo.setCarreserveday(carreserveday);
			vo.setCarbegindate(carbegindate);
			vo.setCarins(carins);
			vo.setCarwifi(carwifi);
			vo.setCarnave(carnave);
			vo.setCarbabyseat(carbabyseat);
			
			request.setAttribute("vo", vo);
			request.setAttribute("totalreserve",totalreserve);//기본 총금액
			request.setAttribute("totaloption", totaloption);//추가한 옵션 총금액 
			
			
			//세션 메모리 영역 얻기 
			HttpSession session = request.getSession();
			
			String id = (String)session.getAttribute("id");
			
			if(id == null) {//로그인이 안된 비회원의 총결제 금액을 보여주자
				
				request.setAttribute("center", "CarOrder.jsp");
			
			}else {//로그인후 회원으로 총결제 금액을 보여주자
				
	
				request.setAttribute("center", "LoginCarOrder.jsp");				
			}
			
			nextPage = "/CarMain.jsp";
		
			
		}else if(action.equals("/CarOrder.do")) {//예약요청을 받았을때
			
			//렌트 예약을 위해 선택했던 값들 얻기
			//요청한 값 얻기
			int carno = Integer.parseInt(request.getParameter("carno"));
			String carbegindate = request.getParameter("carbegindate");
			int carqty = Integer.parseInt(request.getParameter("carqty"));
//			int carprice = Integer.parseInt(request.getParameter("carprice"));
			int carreserveday = Integer.parseInt(request.getParameter("carreserveday"));
			int carins = Integer.parseInt(request.getParameter("carins"));
			int carwifi = Integer.parseInt(request.getParameter("carwifi"));
			int carnave = Integer.parseInt(request.getParameter("carnave"));
			int carbabyseat = Integer.parseInt(request.getParameter("carbabyseat"));
			
			CarOrderVo vo = new CarOrderVo();
			vo.setCarno(carno);
			vo.setCarqty(carqty);
			vo.setCarreserveday(carreserveday);
			vo.setCarbegindate(carbegindate);
			vo.setCarins(carins);
			vo.setCarwifi(carwifi);
			vo.setCarnave(carnave);
			vo.setCarbabyseat(carbabyseat);
			
			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			
			if(id == null) {//비회원으로 예약할때~
				//비회원 으로 예약시 입력했던 핸드폰번호와 비밀번호 얻기 
				String memberphone = request.getParameter("memberphone");
				String memberpass = request.getParameter("memberpass");
				
				vo.setMemberphone(memberphone);
				vo.setMemberpass(memberpass);
				
			}else {//회원으로 예약할떄~
				//아이디 비밀번호 얻기 
				String memberid = request.getParameter("memberid");
				String memberpass= request.getParameter("memberpass");
				
				vo.setId(memberid);
				vo.setMemberpass(memberpass);
				
			}
			//CarDAO객체의 insertCarOrder메소드 호출시~
			//매개변수로 CarOrderVo객체와  HttpSession내장객체 를 전달해서
			//INSERT작업 명령 
			cardao.insertCarOrder(vo,session);
			
			//출력 스트림 PrintWriter객체 생성
			PrintWriter out = response.getWriter();
			out.print("<script>");
			out.print(" alert('예약되었습니다.'); ");
			out.print(" location.href='"+request.getContextPath()
										+"/Car/CarList.do'");
			out.print("</script>");
			
			return;
		
		// /cc  <- 예약확인 하기 위해 예약당시 입력했던 비회원 핸드폰번호, 비밀번호를 
		//         입력하여 예약확인을 요청하는 디자인 VIEW를 요청!	
		}else if(action.equals("/cc")) {
			//   /Car/cc?center=CarReserveConfirm.jsp
	
			//중앙 VIEW화면에 보여줄 요청할 값얻기
			String center =  request.getParameter("center");
			// "CarReserveConfirm.jsp"
			
			//request에 "CarReserveConfirm.jsp" 중앙 VIEW화면 주소를 바인딩(저장)
			request.setAttribute("center", center);
			
			
			nextPage = "/CarMain.jsp";
		
		
		// 입력한 휴대폰번호와 비밀번호로 예약한  렌트 정보 조회 요청!
		}else if(action.equals("/CarReserveConfirm.do")) {
			
			//요청한 값 얻기 
			//(입력한 휴대폰 번호와  비밀번호 얻기)
			String memberphone = request.getParameter("memberphone");
			String memberpass = request.getParameter("memberpass");
			
			
			//예약한 정보를 조회 하기 위해 CarDAO객체의 getAllCarOrder 메소드 호출
			//매개변수로 입력한 휴대번호와 비밀번호를 전달 하여 SELECT문장을 만든 뒤 
			//조회한 정보들을 각각 CarConfirmVo객체에 담아 Vector배열에 최종 저장 후 반환 받자
			Vector<CarConfirmVo> vector = 
			
					cardao.getAllCarOrder(memberphone, memberpass);
			
			//조회한 예약정보들을  중앙VIEW(CarReserveResult.jsp)에 보여주기 위해
			//먼저 ~  request내장객체에 바인딩 (저장)
			request.setAttribute("v", vector);
			
			//중앙 VIEW(예약한 정보를 보여줄 CarReserveResult.jsp)주소를
			//request내장객체에 바인딩(저장)
			request.setAttribute("center", "CarReserveResult.jsp");
			
			request.setAttribute("memberphone", memberphone);
			request.setAttribute("memberpass", memberpass);
			
			nextPage = "/CarMain.jsp";
		
//	     예약한 정보 수정하기위해 예약한 아이디로 예약정보 조회요청!	
		}else if(action.equals("/update.do")) {
			
	//<a href="${contextPath}/Car/update.do?orderid=${vo.orderid}&carimg=${vo.carimg}&memberpass=${requestScope.memberpass}&memberphone=${requestScope.memberphone}">
	   //예약수정
	//</a>			
			//요청한 값 얻기
			int orderid = Integer.parseInt(request.getParameter("orderid"));
			String carimg = request.getParameter("carimg");
			String memberphone = request.getParameter("memberphone");
			String memberpass = request.getParameter("memberpass");
			
			//예약 아이디를 이용해 예약한 정보를 DB에서 조회하기 위해
			//CarDAO객체의 getOneOrder메소드 호출할때 
			//매개변수로 orderid를 전달 하여 조회 해 오자
			CarConfirmVo vo = cardao.getOneOrder(orderid);
						 vo.setCarimg(carimg);
					
			//중앙화면 VIEW("CarConfirmUpdate.jsp")에 조회된 예약정보를 보여주기 위해
			//일단~~~ request내장객체 영역에 CarConfirmVO객체 바인딩
			request.setAttribute("vo", vo);
			
			request.setAttribute("memberphone", memberphone);
			request.setAttribute("memberpass", memberpass);
			
			request.setAttribute("center", "CarConfirmUpdate.jsp");//중앙 VIEW
			
			
			nextPage = "/CarMain.jsp";
			
		
		//입력한 정보를 DB에 UPDATE수정 해주세요!
		}else if(action.equals("/updatePro.do")) {
			
			//수정을 위해 입력한 정보들은 request내장객체 메모리에 저장되어 있으므로
			//DB에 UPDATE시키기 위해 CarDAO객체의 carOrderUpdate메소드 호출할때
			//매개변수로 request객체 메모리의 주소를 전달해 UPDATE시키자
			int result = cardao.carOrderUpdate(request);
				
			int orderid = Integer.parseInt(request.getParameter("orderid"));
			String memberphone = request.getParameter("memberphone");
			String carimg = request.getParameter("carimg");
			
			
			if(result == 1) {
				pw.print("<script>");
				pw.print(" alert('예약 정보가 수정 되었습니다.'); ");
				pw.print(" location.href='"+ request.getContextPath() +"/Car/update.do?orderid="+orderid+"&carimg="+carimg+"&memberphone="+memberphone+"'");	
				pw.print("</script>");
				
				return;
			}else {
				
				pw.print("<script>");
				pw.print(" alert('예약정보 수정 실패'); ");
				pw.print(" history.back(); ");
				pw.print("</script>");
				
				return;
			}
			
		//예약 삭제를 위해 비밀번호를 입력하는 화면 요청!	
		}else if(action.equals("/delete.do")) {
//Car/delete.do?orderid=4&memberphone=01012341234&center=Delete.jsp
			
			//요청한 값 얻기 ( 비밀번호 입력하는 화면 VIEW "Delete.jsp" 얻기)
			String center = request.getParameter("center");
		
			
			//request내장객체에 바인딩
			request.setAttribute("center", center);//"Delete.jsp"
		
			
			nextPage="/CarMain.jsp";
		
		//예약 (취소)삭제 요청 을 받았다면
		}else if(action.equals("/deletePro.do")) {
			
			//요청한 값 얻기 
			//삭제시 사용할 예약 아이디, 
			//삭제를 위해 입력한 비밀번호, 
			//삭제시 사용할 예약자의 핸드폰번호
			int orderid = Integer.parseInt(request.getParameter("orderid"));
			String memberpass = request.getParameter("memberpass");
			String memberphone = request.getParameter("memberphone");
					
			//응답할 값 마련 
			//예약정보를 삭제(취소)하기 위해 CarDAO객체의 OrderDelete메소드 호출할때
			//매개변수로 삭제할 예약아이디와 입력한 비밀번호 전달하여 DB에서 DELETE시키자
			//삭제에 성공하면 OrderDelete 메소드의 반환값은 
			//삭제에 성공한 레코드 개수 1을 반환받고
			//실패하면 0을 반환 받습니다.	
			int result = cardao.OrderDelete(orderid, memberpass);
			
			if(result == 1) {
				pw.print("<script>");
				pw.print(" alert('예약 정보가 삭제 되었습니다.'); ");
				pw.print(" location.href='"+ request.getContextPath() +"/Car/CarReserveConfirm.do?memberphone="+memberphone+"&memberpass="+memberpass+"';");	
				pw.print("</script>");
				
				return;
			}else {
				
				pw.print("<script>");
				pw.print(" alert('예약정보 삭제 실패'); ");
				pw.print(" history.back(); ");
				pw.print("</script>");
				
				return;
			}
			
			
		// 검색버튼 눌렀을 때 중앙화면 조회(네이버 API)	
		}else if(action.equals("/NaverSearchAPI.do")) {
			
			//1. 인증정보 설정
			String clientId = "8GhH6nonoWAQzFzDUayF"; //애플리케이션 클라이언트 아이디
	        String clientSecret = "J9qyhKvwAI"; //애플리케이션 클라이언트 시크릿
	        
	        
	        //2. 검색조건 설정
	        int startNum = 0;
	        String text = null;  // 입력한 검색어
	        
	        try {
	        	//검색 시작위치 받아오기
	        	startNum = Integer.parseInt(request.getParameter("startNum"));
	        	
	        	//디자인 검색요청화면에서 입력한 검색어 얻기
	        	String searchText = request.getParameter("keyword");
	        	
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
	        
	        request.setAttribute("searchData", responseBody);
	        request.setAttribute("center", "SearchResult.jsp");

	        //웹브라우저로 응답할 데이터 유형 설정
	        response.setContentType("text/thml; charset=utf-8");

	        //getWriter객체 얻어 검색결과 보냄
	        response.getWriter().write(responseBody);
	       
	        nextPage = "/CarMain.jsp";
	        
		}//service 닫기
		
			
		
		
		//디스패처 방식 포워딩(재요청)
		RequestDispatcher dispatch = 
				          request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	
	}//doHandle메소드 	
	
	
	
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


















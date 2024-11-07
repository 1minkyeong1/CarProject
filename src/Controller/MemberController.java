package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.CarDAO;
import Service.MemberService;
import Vo.CarConfirmVo;
import Vo.CarListVo;
import Vo.CarOrderVo;

//.. 사장 

//MVC중에서 C의 역할 

			 
@WebServlet("/member/*")
public class MemberController extends HttpServlet {

	//부장
	MemberService memberservice;
	
	@Override
	public void init() throws ServletException {
		
		memberservice = new MemberService();
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
		PrintWriter out = response.getWriter();
	
		
		//조건에 따라서 포워딩 또는 보여줄 VIEW주소 경로를 저장할 변수
		String nextPage = null;
		
		//요청한 중앙화면 뷰 주소를 저장할 변수
		String center = null;
		
		
		//1.Top.jsp 화면에서 회원가입 링크를 클릭하여 회원 가입 중앙 화면 VIEW요청 주소를 받는다
		//  요청 한 전체 URL중에서 2단계 요청한 주소 얻기
		//  /member/join.me?center=members/join.jsp
		//2단계 요청 주소 -> "/join.me"
		
		//2.join.jsp 화면에서 회원가입을 하기전 입력한 아이디가 DB의 테이블에 존재하는지
		//  확인 하는 요청 전체  URL중에서 2단계 요청한 주소 얻기
		// /member/joinIdCheck.me
		//2단계 요청 주소 -> "/joinIdCheck.me"
		
		
		//3.join.jsp 화면에서 가입할 새 회원 정보를 입력하고 가입요청을 받았을때 
		// 전체 URL 중에서 2단계 요청한 주소 얻기
		//  /member/joinPro.me
		//2단계 요청한 주소 -> "/joinPro.me"
		
		//4.Top.jsp 상단의 로그인 버튼을 클릭하여 아이디,비밀번호 를 입력받을수 있는 중앙 VIEW요청을 받았을때
		// 전체 URL중에서 2단계 요청한 주소 얻기
		//  /member/login.me
		//2단계 요청한 주소 ->  "/loin.me"
		
		//5. login.jsp화면에서 아이디,비밀번호를 입력후 로그인 요청 
		//   전체 URL중에서 2단계 요청한 주소 얻기
		//  /member/loginPro.me
		//2단계 요청한 주소 -> "/loginPro.me"
		
		//6. Top.jsp화면 상단의  로그아웃 링크를 클릭해서 로그아웃 요청
		//   전체 URL중에서 2단계 요청한 주소 얻기
		//  /member/logout.me
		//2단계 요청한 주소 ->  "/logout.me"
		
		
		String action = request.getPathInfo();//2단계 요청주소 
		System.out.println("요청한 2단계 주소 : " + action);
		
		switch(action) {
			
			case "/join.me": //회원가입 중앙 VIEW 요청
				
				//부장---
				center = memberservice.serviceJoinName(request);
				//"members/join.jsp"
				
				//request객체에 "members/join.jsp" 중앙화면 뷰 주소 바인딩
				request.setAttribute("center", center);
				
				nextPage = "/CarMain.jsp";
				
				break;
				
			case "/joinIdCheck.me":	//아이디 중복 체크 요청!			
			//부장 한테 시키자
				//입력한 아이디가 DB에 저장되어 있는지 확인 하는 작업
				//true -> 중복,  false -> 중복 아님   둘중 하나를 반환 받음 
				boolean result = memberservice.serviceOverLappedId(request);
				
				//아이디 중복결과를 다시 한번 확인 하여 조건값을 
				//join.jsp파일과 연결된 join.js파일에 작성해 놓은 
				//$.ajax메소드 내부의 
				//success:function의 data매개변수로 웹브라우저를 거쳐 보냅니다!
				if(result == true) {
					out.write("not_usable");
					return;
				}else if(result == false) {
					out.write("usable");
					return;
				}
				break;
				
			case "/joinPro.me"://회원가입 2단계 요청주소와 같다면?
				
				//부장에게 시킴
				memberservice.serviceInsertMember(request);
								
				nextPage="/CarMain.jsp";
			
				break;
			
			case "/login.me": //로그인 요청화면 
				
				//부장에게 시킴
				center = memberservice.serviceLoginMember();
//				"members/login.jsp"
				
				//중앙 화면 VIEW 주소 바인딩
				request.setAttribute("center", center);//"members/login.jsp"
				
				//재요청할 전체 메인화면 주소를 저장
				nextPage = "/CarMain.jsp";
				
				break;
			
			case "/loginPro.me": //로그인 요청을 한 2단계 요청주소일때
				
				//부장에게~~
				//check값이 1이면 입력한 아이디,비밀번호가 DB에 존재함
				//         0이면 입력한 아이디만 DB에 존재함
				//         -1이면 입력한 아이디 DB에 존재하지 않음 
				int check = memberservice.serviceUserCheck(request);
				
				if(check == 0) {//아이디 맞음, 비밀번호틀림
					out.println("<script>");
					out.println(" window.alert('비밀번호 틀림'); ");
					out.println(" history.go(-1);");
					out.println("</script>");
					return;
			
				}else if(check == -1) {//아이디 틀림
					out.println("<script>");
					out.println(" window.alert('아이디 틀림'); ");
					out.println(" history.go(-1);");
					out.println("</script>");
					return;
				}
				
				//메인화면 재요청 VIEW주소 저장
				nextPage = "/CarMain.jsp";				
				break;
				
			case "/logout.me":	//로그 아웃  2단계 요청 주소를 받았을때
				//부장시키자
				//- 로그아웃 화면을 보여주기위해 session영역에 저장된 아이디를 제거
				memberservice.serviceLogOut(request);
				
				//메인화면 재요청을 해서 로그아웃된 상단을 보여주기위해 주소저장
				//중앙 페이지는 "Center.jsp"가 나올 것이다.
				nextPage = "/CarMain.jsp";
				
				break;
				
				
			default:
				break;
		}
	
		//디스패처 방식 포워딩(재요청)
		RequestDispatcher dispatch = 
				          request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	
	}//doHandle메소드 	
}


















package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.BoardService;
import Vo.BoardVo;
import Vo.MemberVo;



//.. 사장 

//MVC중에서 C의 역할 

//게시판 관련 기능 요청이 들어오면 호출되는 사장님(컨트롤러)			 
@WebServlet("/Board/*")
public class BoardController extends HttpServlet {

	//부장
	BoardService boardservice;
	
	@Override
	public void init() throws ServletException {
		
		boardservice = new BoardService();
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
		//한글처리
		request.setCharacterEncoding("UTF-8");
		//웹브라우저로 응답할 데이터 종류 설정 
		response.setContentType("text/html;charset=utf-8");
		//웹브라우저와 연결된 출력 스트림 생성
		PrintWriter out = response.getWriter();
			
		//조건에 따라서 포워딩 또는 보여줄 VIEW주소 경로를 저장할 변수
		String nextPage = null;		
		//요청한 중앙화면 뷰 주소를 저장할 변수
		String center = null;
		
		//클라이언트가 BoardController로 요청한 전체 주소 중에서
		//2단계 요청한 주소 얻어 action변수에 저장
		String action = request.getPathInfo();
		// "/list.bo"  <- DB의 Board테이블에 저장된 모든 글목록 조회 요청!
		// "/searchlist.bo" <- DB의 Board테이블에 저장된 글목록을 조회하되 
		//					     입력한 검색기준열값과 검색어를 포함하는 글목록 조회 요청!
		// "/write.bo" <- 새글입력화면 VIEW(중앙화면) 요청! 
		// "/writePro.bo" <- 입력한 새글 정보를 DB의 Board테이블에 추가 요청!
		// "/read.bo"   <- list.jsp요청화면에서 조회된 글제목 하나를 클릭했을때
		//                 글번호를 이용해 글 하나를 조회 해서 중앙화면에 보여줘~~ 요청!
		// "/password.do" <- 글상세화면(read.jsp)에서 글 수정또는 글삭제를 위해 
		//                   글 비밀번호를 입력 해서 DB의 Board테이블에 저장된 비밀번호와 비교해서
		//					 비밀번호가 저장되어있느냐 , 저장되어 있지 안느냐? 판단 AJAX요청!
		// "/updateBoard.do" <- 글상세 화면(read.jsp)에서 수정할 글 내용입력하고
		//						수정버튼을 눌러 ajax로 수정요청! 했을때
		// "/deleteBoard.do" <- 글상세 화면(read.jsp)에서 글삭제 버튼을 눌러 ajax삭제요청했을때 
		// "/reply.do" <- 주글에 대한 답변글을 작성할수 있는 화면 요청
		// "/replyPro.do"  <- 주글에 대한 답변글 DB의 board테이블에 추가요청
		

		System.out.println("요청한 2단계 주소 : " + action);
		
		ArrayList list = null;
		BoardVo vo = null;
		
		switch(action) {
			case "/list.bo": //모든 글 조회 
				
				HttpSession session_ = request.getSession();
				String loginid = (String)session_.getAttribute("id");
				
				//부장 호출!
				list = boardservice.serviceBoardList();
				
				//list.jsp페이지의 페이징 처리 부분에서 
				//이전 또는 다음 또는 각페이지 번호중 하나를 클릭했을때 요청받는 값 얻기
				String nowPage = request.getParameter("nowPage");
				String nowBlock = request.getParameter("nowBlock");
				
				
				
				request.setAttribute("list", list);
				request.setAttribute("center", "board/list.jsp");
				request.setAttribute("id", loginid);
				request.setAttribute("nowPage", nowPage);
				request.setAttribute("nowBlock", nowBlock);
				
				
				
				nextPage = "/CarMain.jsp";
	
				break;

				
			case "/searchlist.bo": //검색 키워드로  글조회
				
			 //요청한 값 얻기 (조회를 위해 선택한 Option의 값 , 입력한 검색어)
			 String key = request.getParameter("key");
			 String word = request.getParameter("word");
				
				
			 //부장 호출
			 //검색 기준열의 값과 입력한 검색어 단어를 포함하고 있는 글목록 조회 명령!
		     list = boardservice.serviceBoardKeyWord(key, word);
				
		     //VIEW중앙화면에 조회된 글목록을 보여주기 위해
		     //request내장객체에 조회된 정보 바인딩
		     request.setAttribute("list", list);
		     //VIEW중앙화면 주소경로 바인딩
		     request.setAttribute("center", "board/list.jsp");
		     
		     //재요청할 메인 페이지 주소 경로 변수에 저장
		     nextPage = "/CarMain.jsp";
		     
		     	break;
				
			case "/write.bo"://새글을 입력할수 있는 화면 요청
				
				HttpSession session = request.getSession();
				String memberid = (String)session.getAttribute("id");
				
				//부장호출
				//새글을 입력할수 있는 화면에 로그인한 사람(글쓰는 사람)의 정보를 보여주기 위해
				//조회 해 오자 
				MemberVo membervo = boardservice.serviceMemberOne(memberid);
				
				//글쓰기 중앙화면(VIEW)에 조회된 회원의 이름, 이메일, 아이디 등을 보여주기 위해
				//MemberVo객체를 request내장객체에 바인딩
				request.setAttribute("membervo", membervo);
				
				//글쓰기 중앙화면(VIEW)경로를 request내장객체에 바인딩
				request.setAttribute("center", "board/write.jsp");
				
				request.setAttribute("nowPage", request.getParameter("nowPage"));
				request.setAttribute("nowBlock", request.getParameter("nowBlock"));
				
				
				//재요청할 전체 VIEW경로 저장
				nextPage = "/CarMain.jsp";
				
				break;
		     	
			case "/writePro.bo": //입력한 새글 정보 DB에 추가 
				
				//요청한 값 얻기
				String writer = request.getParameter("w");
				String email = request.getParameter("e");
				String title = request.getParameter("t");
				String content = request.getParameter("c");
				String pass = request.getParameter("p");
				String id = request.getParameter("i");
				
				//요청한 값들 BoardVo객체의 각변수에 저장
				vo = new BoardVo();
				vo.setB_name(writer);
				vo.setB_email(email);
				vo.setB_title(title);
				vo.setB_content(content);
				vo.setB_pw(pass);
				vo.setB_id(id);
				
				//부장 호출
				//웹브라우저에 응답할 값 마련(DB에 새글 정보 추가에 성공 또는 실패 관련 조건 데이터)
				//result == 1  -> insert 성공
				//result == 0  -> insert 실패  
				int result = boardservice.serviceInsertBoard(vo);
				
				//1 -> "1"로 변환 하거나 또는  0 -> "0"문자열로 변환해서 저장
				String go = String.valueOf(result);
				
				if(go.equals("1")) {//"1"  insert 성공했다면?
					
					out.print(go);//"1"전달    writer.jsp요청한 페이지로 응답
					
				}else {//"0"  insert 실패 했다면? 
					
					out.print(go);//"0"전달  writer.jsp요청한 페이지로 응답 
				}
				
				return;//doHandle메소드 빠져나가게 해서 밑의 포워딩 안되게 
				
			case "/read.bo"://글제목을 클릭해 글번호를 이용한 글조회 요청!
							//조회한 글정보 중앙화면에 보여줍시다
				
				//list.jsp에서 글제목을 클릭했을 때 요청한 3개의 값 얻기
				String b_idx_ = request.getParameter("b_idx");//글번호
				String nowPage_ = request.getParameter("nowPage");//현재 페이지번호
				String nowBlock_ = request.getParameter("nowBlock");
				//현재 보이고 있는 페이지번호가 속한 
				//블럭위치 번호 
				
				
				//부장 호출
				//글제목을 눌렀을때 조회된 레코드(글)에 관한 글정보 하나 조회 요청
				vo = boardservice.serviceBoardRead(b_idx_);
				
				//조회된 글 하나의 정보를 보여줄 중앙 VIEW 경로  request에 바인딩
				request.setAttribute("center", "board/read.jsp");
				
				//조회된 글 하나의 정보(BoardVO객체) request에 바인딩
				request.setAttribute("vo", vo);
				
				//중앙 VIEW board/read.jsp페이지에 전달후 사용하기 위한
				//nowPage, nowBlock, b_idx 각각 바인딩
				request.setAttribute("nowPage", nowPage_);
				request.setAttribute("nowBlock", nowBlock_);
				request.setAttribute("b_idx", b_idx_);
				
				nextPage="/CarMain.jsp";
				
			    break;
			
			case "/password.do": //글 수정 또는 삭제를 위해 입력한 글의 비밀번호가
								 //DB에 존재하는지 체크 요청
				//요청한 값 2개 얻기
				String b_idx = request.getParameter("b_idx");
				String password = request.getParameter("pass");
				
				//부장 호출
				//글을 수정, 삭제 하기 위한  수정버튼과 삭제버튼 활성화를 위해
				//입력한 글의 비밀번호가 DB에 있는지 체크가 위해 호출!
				boolean resultPass = boardservice.servicePassCheck(b_idx,password);
				//true :입력한 글의 비밀번호가 DB에 존재함
				//false:입력한 글의 비밀번호가 DB에 존재하지 않음 
				
				if(resultPass == true) {
					out.write("비밀번호맞음");
					return;
				}else {//false
					out.write("비밀번호틀림");
					return;
				}
			
			case "/updateBoard.do": //글 수정 요청 
				
				//글 수정시 입력한 값 얻기
				String idx_  = request.getParameter("idx");
				String email_ = request.getParameter("email");
				String title_  = request.getParameter("title");
				String content_ = request.getParameter("content");
				
				//부장 호출
				//수정시 입력한 위변수값들을 DB의 Board테이블에 있는 열의 값으로 저장되게 수정요청
				int result_ = boardservice.serviceUpdateBoard(idx_, email_, 
															  title_, content_);
				
				
				if(result_ == 1) { //UPDATE성공
					out.write("수정성공");//웹브라우저창을 거쳐서 
										//read.jsp에서 호출한 $.ajax메소드 내부의
										//success:function(data){}의 
									    //data매개변수로 "수정성공" 보낸다 
					return;
				}else {//UPDATE실패
					out.write("수정실패");
					return;
				}
				
			case "/deleteBoard.do": //글 삭제  2단계 요청 주소일때 
				//삭제를 위해 요청한 글번호 얻기
				String delete_idx = request.getParameter("b_idx");
				
				//부장 호출
				//글삭제 요청!시 삭제할 글번호를 전달해 
				//삭제에 성공하면 "삭제성공" 메세지 반환받고,
				//삭제에 실패하면 "삭제실패" 메세지를 반환받자
				String result__ = boardservice.serviceDeleteBoard(delete_idx);
				
				out.write(result__); //AJAX
				
				return;
				
			case "/reply.do":   // 주글에 대한 답변글을 작성할수 있는 화면 요청
				//요청한 주글(부모글) 글번호 얻는다
				String b_idx__ = request.getParameter("b_idx");
				//요청한 답변글을 작성할 사람의 아이디 얻는다
				String reply_id_ = request.getParameter("id");
				
				//부장호출
				//로그인한 회원이 주글에 대한 답변그을 작성할수 있도록 하기 위해
				//로그인 한 회원 아이디를 BoardService의 메소드 호출시 매개변수로 전달해
				//아이디 에 해당하는 회원정보를 조회함
				MemberVo reply_vo = boardservice.serviceMemberOne(reply_id_);
				
				//부모글번호와 조회한 답변글 작성자 정보를  request에 바인딩
				request.setAttribute("b_idx", b_idx__); //주글(부모) 글번호 
				request.setAttribute("vo", reply_vo);//답변글 작성하는 사람 정보 
				
				//중앙화면(답변글을 작성할수 있는 중앙 VIEW) 경로를  request에 바인딩
				request.setAttribute("center", "board/reply.jsp");
				
				nextPage="/CarMain.jsp";
				
				break;
				
				
			case "/replyPro.do":  //주글에 대한 답변글 DB의 board테이블에 추가요청	
				
				//요청한 값들 (주글(부모글) 글번호 + 작성한 추가할 답변글 정보) 얻기
				//주글 글번호
				String super_b_idx = request.getParameter("super_b_idx");
				//답변글 작성자 아이디
				String reply_id = request.getParameter("id");
				//답변글 작성자 이름
				String reply_name = request.getParameter("writer");
				//답변글 작성자 이메일
				String reply_email = request.getParameter("email");
				//답변글 제목
				String reply_title = request.getParameter("title");
				//답변글 내용
				String reply_content = request.getParameter("content");
				//답변글의 비밀번호
				String reply_pass = request.getParameter("pass");
				
				
				//부장님 호출
				//
				boardservice.serviceReplyInsertBoard(super_b_idx,
													 reply_id,
													 reply_name,
													 reply_email,
													 reply_title,
													 reply_content,
													 reply_pass	);
			
				//답변글 추가에 성공하면 다시 전체 글 목록 조회해서 보여주기 위핸 재요청 주소 nextPage변수에 저장
				nextPage = "/Board/list.bo";
				
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


















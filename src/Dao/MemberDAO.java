package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import Vo.CarConfirmVo;
import Vo.CarListVo;
import Vo.CarOrderVo;
import Vo.MemberVo;

//MVC중에서 M을 얻기 위한 클래스 

//DB와 연결하여 비즈니스로직 처리하는 클래스 
public class MemberDAO {
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	//컨넥션풀 얻는 생성자
	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/oracle");
			
		}catch(Exception e) {
			System.out.println("커넥션풀 얻기 실패 : " + e.toString());
		}
	}
	//자원해제(Connection, PreparedStatment, ResultSet)기능의 메소드 
	private void closeResource() {
		try {
				if(con != null) { con.close(); }
				if(pstmt != null) { pstmt.close(); }
				if(rs != null) { rs.close(); }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//아이디 중복 체크 
	public boolean overlappedId(String id) {
		
		boolean result = false;
		
		try {
			  con = ds.getConnection();  //DB연결
			  
			  //오라클의 decode함수 사용하여 
			  //입력한 ID에 해당하는 데이터를 검색하는데  조회된 레코드가 있으면
			  //true리턴, 조회된 레코드가 없으면 false를 반환 하는데
			  //검색한 갯수가 1(검색한 레코드가 존재하면)이면 'true'를 반환
			  //검색이 안되면 'false'를 반환 
			  String sql  = "select decode( count(*), 1, 'true', 'false' ) as result "
			  			  + " from member "
			  			  + " where id=?";
			  
			  pstmt = con.prepareStatement(sql);
			  pstmt.setString(1, id);
			  
			  //select전체 문장을 DB에 전송하여 실행한 조회된 결과데이터를 ResultSet임시객체에 담아 반환
			  rs = pstmt.executeQuery();
			  
			  if(rs.next()) {//조회된 제목 행의 커서(화살표)가 조회된 행 줄로 내려왔을때 있으면?
				  
				  String value  = rs.getString("result"); //"false" 또는 "true"
				  result = Boolean.parseBoolean(value); //"false"-> false변환해서 저장
				  										//"true"-> true변환해서 저장 
			  }
			
		}catch (Exception e) {
			System.out.println("MembeDAO의 overlappedId메소드 내부에서 오류:"+e);
			e.printStackTrace();
		}finally {
			closeResource();//자원해제
		}
		
		return result;//"true" 또는 "false" 부장(MemberService)에게 반환 
	}	
	//새 회원 추가 
	public void insertMember(MemberVo vo) {
		try {
			con = ds.getConnection();//DB연결
			//매개변수로 전달 받은 MemerVO객체의 각변수값 을 얻어 
			//INSERT문장을 완성 시킵니다
			String sql = "INSERT INTO MEMBER(id, pass, name, reg_date, "
										 + "age, gender, address, email, tel, hp)"
					   + " VALUES(?, ?, ?, sysdate, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPass());
			pstmt.setString(3, vo.getName());
			pstmt.setInt(4, vo.getAge());
			pstmt.setString(5, vo.getGender());
			pstmt.setString(6, vo.getAddress());
			pstmt.setString(7, vo.getEmail());
			pstmt.setString(8, vo.getTel());
			pstmt.setString(9, vo.getHp());
			
			//PreparedStatement실행객체 메모리에 설정된 전체 INSERT문장 실행
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			System.out.println("MembeDAO의 insertMember메소드 에서 오류");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		
	}
	
	//로그인 요청시 입력한 아이디, 비밀번호가 DB의 member테이블에 있는지 확인
	public int userCheck(String login_id, String login_pass) {
		
		int check = -1;
		
		try {
			 con = ds.getConnection(); //DB연결
			 //member테이블의 id열값이 입력한 아이디에 해당되면
			 //해당 행의 모든 열값 조회
			 String sql = "select * from member where id=?";
			 pstmt = con.prepareStatement(sql);
			 pstmt.setString(1, login_id);
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()) {//입력한 아이디로 조회한 행이 있으면?(아이디가 있으면?)
				  
				 //입력한 비밀번호와 조회된 비밀번호를 비교해서 있으면?(비밀번호가 있으면?)
				 if(login_pass.equals(rs.getString("pass"))) {
					 
					 check = 1;
					 
				 }else {//입력한 아이디는 존재하나 비밀번호가 없으면?
					 
					 check = 0;
				 }
				 
				 
			 }else {//입력한 아이디가 DB의 테이블에 없다
				 
				 check = -1;
			 }
			 	
		} catch (Exception e) {
			System.out.println("MemberDAO의 userCheck메소드에서 오류");
			e.printStackTrace();
		} finally {
			//자원해제 
			closeResource();
		}
	
		return check;//MemberService(부장)에게 결과 반환
	}
	
	
	//로그인한 회원 아이디를 이용해 회원정보 조회 (글작성 화면에 조회한 회원정보를 보여주기 위해..)
	public MemberVo memberOne(String memberid) {
		
		MemberVo vo = null;
		
		try {
			
			con = ds.getConnection();  // DB연결
			
			String sql = "select email, name, id from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberid);
			rs  = pstmt.executeQuery();
			
			if(rs.next()) {  //로그인한 아이디로 조회한 행이 있으면?
				
				vo = new MemberVo();
				vo.setEmail(rs.getString("email"));
				vo.setName(rs.getString("name"));
				vo.setId(rs.getString("id"));
			}
						
		} catch (Exception e) {
			System.out.println("MemberDao의 memberOne메소드");
			e.printStackTrace();
		} finally {
			closeResource();
		}
	
		return vo;    //BoardService로 리턴(보고)
	}
	
	
	
}//MemberDAO클래스 


















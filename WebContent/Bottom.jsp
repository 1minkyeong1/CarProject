<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>     
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String contextPath = request.getContextPath();
%>

		<!-- 빨간 라인 선 -->
		<hr width="1000" color="red">
		
		<!-- 아래 로고 이미지 부분 -->
		<a href="#">
			<img src="<%=contextPath%>/img/bo.jpg" width="500" height="50">
		</a>
		
		<font size="2">
			<b>
				<!-- "회사소개" 이미지 링크 -->
				<a href="#">
					<img src="<%=contextPath%>/img/sodog.jpg">
				</a>
				<!-- "개인정보 취급방침" 이미지 링크 와 함께 텍스트 나열 -->
				<a href="#">
					<img src="<%=contextPath%>/img/info.jpg"> | 사이버 신문고  | 이용약관 | 인재채용
				</a>
			</b>
			<br><br>
			<!-- 글씨 작게 -->
			<small>
				(주) SM렌탈 사업자 등록번호 214-98754-9874 통신 판매업신고 번호 :
				 제 2010-충남-05호 <br>
				
				서울시 강남구 역삼동 역삼빌딩 2층 21호 <br><br>
				
				대표전화 : 02-3456-6574<br>
				FAX : 01-3254-9874 
				
			</small>
			
		</font>
		
		
		
		
</body>
</html>











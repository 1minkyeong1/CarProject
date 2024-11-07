<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>     
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>   
 
<%
	request.setCharacterEncoding("UTF-8");
	String contextPath = request.getContextPath();
%>    
    
    <!-- /CarReserveConfirm.do  <- 입력한 휴대폰번호와 비밀번호로 예약할 렌트 정보 조회 요청! 2단계 요청 주소명-->
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>결제 를 위해 계산된 총금액을 보여주는 VIEW</title>
</head>
<body>

<%--결제후 예약요청 버튼 을 눌러 예약 요청  --%>
<form action="<%=contextPath%>/Car/CarOrder.do" method="post">

	<%--예약할 정보들을 input type="hidden"으로 전달 --%>
	<input  type="hidden"   name="carno"  value="${requestScope.vo.carno}"/>
	<input  type="hidden"   name="carqty"  value="${requestScope.vo.carqty}"/>
	<input  type="hidden"   name="carreserveday"  value="${requestScope.vo.carreserveday}"/>
	<input  type="hidden"   name="carbegindate"  value="${requestScope.vo.carbegindate}"/>
	<input  type="hidden"   name="carins"  value="${requestScope.vo.carins}"/>
	<input  type="hidden"   name="carwifi"  value="${requestScope.vo.carwifi}"/>
	<input  type="hidden"   name="carnave"  value="${requestScope.vo.carnave}"/>
	<input  type="hidden"   name="carbabyseat"  value="${requestScope.vo.carbabyseat}"/>
	

	<div align="center">
		<%--<결제하기> 이미지 --%>
		<img src="<%=contextPath%>/img/haki.jpg">
		
		<p>
			<font size="13" color="blue">
				차량 기본 금액 비용 :  ￦ ${requestScope.totalreserve}원
			</font>
		</p>
		<p>
			<font size="13" color="blue">
				차량 옵션 금액 비용 :  ￦ ${requestScope.totaloption}원
			</font>
		</p>		
		<p>
			<font size="13" color="blue">
				총 비용 :  ￦ ${totalreserve + totaloption}원
			</font>
		</p>	
		<p>
			비회원 전화번호 입력 : <input type="text" name="memberphone">
			&nbsp;&nbsp;&nbsp;
			비회원 비밀번호 입력 : <input type="password" name="memberpass"><br><br>
			<input type="submit" value="결제후 예약요청" align="center">
		</p>	
	</div>
</form>


</body>
</html>
















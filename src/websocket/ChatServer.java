package websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

//@ServerEndpoint 에너테이션으로 웹 소켓 서버의 요청명을 지정하여, 
//해당 요청명으로 접속하는 클라이언트를
//이 서블릿 서버페이지가 처리하게 합니다.

//요청 주소명이 /ChatingServer이므로 이 웹소켓에 접속하기 위한 전체 URL은 다음과 같습니다.
//->  ws://호스트:포트번호/컨텍스트루트/ChatingServer

//참고 - 웹 소켓은 http프로토콜이 아닌 ws프로트콜을 사용합니다.

@ServerEndpoint("/ChatingServer")
public class ChatServer {
	
	//현재 연결된 클라이언 세션영역을 저장하는 Set.
	private static Set<Session> clients = 
			Collections.synchronizedSet(new HashSet<Session>());
	
	//클라이언트가 접속할때 자동으로 호출되는 메서드 정의
	@OnOpen
	public void onOPen(Session session) {
		//접속한 클라이언트의 세션 메모리 영역을 위 Set에 추가
		clients.add(session);
		//접속한 클라이언트를 판단할 세션ID 를 출력
		System.out.println("웹 소켓 연결:" + session.getId());
	}
	//클라이언트로 부터 메세지를 받았을때 자동으로 호출되는 메서드 정의 
	@OnMessage
	public void onMessage(String message, Session session) 
							throws IOException {
		System.out.println("메세지 전송 : " +  session.getId() + " : " + message);
		
		//동기화된 client Set에 대해 모든 클라이언트에게 메세지 전송
		synchronized (clients) {
			
			for(Session client  : clients) {
				//메세지를 보낸 클라이언트는 제외하고 다른 클라이언트 창 화면에 메세지 전송
				if(!client.equals(session)) {
					client.getBasicRemote().sendText(message);//메세지 전송
				}
			}
		}
	}
	
	//클라이언트와의 연결이 종료되었을때 자동으로 호출되는 메서드 
	@OnClose
	public void onClose(Session session) {
		//연결이 종료된 클라이언트의 세션영역을 Set에서 제거
		clients.remove(session);
		System.out.println("웹소켓 종료 : " + session.getId());//종료 로그 출력
	}
	
	//에러가 발생했을때 자동으로 호출되는 메서드
	@OnError
	public void OnError(Throwable e) {
		System.out.println("에러 발생"); //에러 발생 로그 출력
		e.printStackTrace();//에러 스택 추적을 콘솔에 출력
	}

}













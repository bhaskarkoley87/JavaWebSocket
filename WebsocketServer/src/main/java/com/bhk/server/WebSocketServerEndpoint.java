package com.bhk.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat")
public class WebSocketServerEndpoint {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static HashMap<String, Client> socketSessions = new HashMap<String, Client>();

	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected ... " + session.getId());
		System.out.println(session.getQueryString().split("=")[1]);
		String chatName = session.getQueryString().split("=")[1];
		
		socketSessions.put(session.getId().toString(), new Client(chatName, session));
		
		StringBuffer allClient = new StringBuffer();
		
		
		
		/*for (Client ssn : socketSessions.values()) {
			allClient.append(ssn.getId() + ";");
		}*/
		
		
		
		for (Client clnt : socketSessions.values()) {
			Session ssn = clnt.getSession();
			List<Map.Entry<String, Client>> othrClient = socketSessions.entrySet().stream().filter(v -> {
				if(!v.getValue().session.getId().equals(ssn.getId()))
						return true;
				return false;
			}).collect(Collectors.toList());

			//String otherClnt = allClient.toString().replace(ssn.getId() + ";", "");

			if (ssn.isOpen()) {
				try {
					ssn.getBasicRemote().sendText(othrClient.toString());
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}

		}

	}

	@OnMessage
	public void onMessage(String message, Session session) {
		switch (message) {

		case "quit":

			try {
				session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Chat ended"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			break;

		}

		try {

			if (message.contains("~%~%")) {
				String[] data = message.split("~%~%");
				Session ssn = socketSessions.get(data[0]).getSession();
				if (ssn.isOpen())
					ssn.getBasicRemote().sendText(session.getId()+"~%~%"+data[1]);
			} else {
				System.out.println(session);
				session.getBasicRemote().sendText(message);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
		socketSessions.remove(session.getId().toString());
		StringBuffer allClient = new StringBuffer();
		/*for (Session ssn : socketSessions.values()) {
			allClient.append(ssn.getId() + ";");
		}*/
		for (Client clnt : socketSessions.values()) {
			Session ssn = clnt.getSession();
			List<Map.Entry<String, Client>> othrClient = socketSessions.entrySet().stream().filter(v -> {
				if(!v.getValue().session.getId().equals(ssn.getId()))
						return true;
				return false;
			}).collect(Collectors.toList());

			//String otherClnt = allClient.toString().replace(ssn.getId() + ";", "");

			if (ssn.isOpen()) {
				try {
					ssn.getBasicRemote().sendText(othrClient.toString());
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}

		}
	}

	

}
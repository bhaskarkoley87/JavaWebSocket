package com.bhk.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.Session;

public class TestChatClientApp {

	public static void main(String[] args) {
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter your chat name : ");
			String userName = "";
			try {
				userName = bufferRead.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// open websocket
			System.out.println(userName);
			final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(
					new URI("ws://localhost:8025/websockets/chat?chatName="+userName));

			// add listener
			clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {
					System.out.println(message);
				}
			});
			Thread.sleep(5000);
			// send message to websocket
			
			while (true) {

				try {
					String userInput = bufferRead.readLine();
					if (userInput.equals("~~~CloseChat~~~")) {
						Session ssn = clientEndPoint.userSession;
						ssn.close();
					} else {
						clientEndPoint.sendMessage(userInput);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// wait 5 seconds for messages from websocket

		} catch (InterruptedException ex) {
			System.err.println("InterruptedException exception: " + ex.getMessage());
		} catch (URISyntaxException ex) {
			System.err.println("URISyntaxException exception: " + ex.getMessage());
		}
	}
}

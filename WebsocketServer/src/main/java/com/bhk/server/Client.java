package com.bhk.server;

import javax.websocket.Session;

public class Client {
	String name;
	Session session;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Client() {
		
	}
	public Client(String name, Session session) {		
		this.name = name;
		this.session = session;
	}
	@Override
	public String toString() {		
		return this.name;
	}
	
	

}

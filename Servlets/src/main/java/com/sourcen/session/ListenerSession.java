package com.sourcen.session;

import javax.servlet.http.*;

public class ListenerSession implements HttpSessionListener {
	public ListenerSession() {
	}
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		// Get the session 
		HttpSession session = sessionEvent.getSession();
		try {
		System.out.println("Session created: "+session);
		session.setAttribute("foo","devashree");
		} catch (Exception e) {
		System.out.println("Error in setting session attribute: " + e.getMessage());
		}
		}
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		// Get the session that was invalidated
		HttpSession session = sessionEvent.getSession();
		// Log a message
		System.out.println("Session invalidated: "+session);
		System.out.println("The name is: " + session.getAttribute("foo"));
	}
}

package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Sample17 extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {  
		 response.setContentType("text/html");
		 PrintWriter out = response.getWriter();
		 out.println("testing");
		/*Cookie c1 = new Cookie("userName", "prasanthi"); 
	    Cookie c2 = new Cookie("password", "sourav"); 
	    response.addCookie(c1); 
	    response.addCookie(c2); 
	    Cookie[] cookies = request.getCookies(); 
	    int length = cookies.length; 
	    for (int i=0; i<length; i++) {
	      Cookie cookie = cookies[i]; 
	      out.println("<B>Cookie Name:</B> " + cookie.getName() + "<BR>"); 
	      out.println("<B>Cookie Value:</B> " + cookie.getValue() + "<BR>"); 
	    } */
	    
	    
	}
}

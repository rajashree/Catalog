package com.servlets;

import javax.servlet.*;

import java.io.*;
public class DeviSecond3 extends GenericServlet{
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
	{
		res.setContentType("text/html");
		PrintWriter out= res.getWriter();
		String userName=req.getParameter("user");
		if(userName.equals(""))
		System.out.println("Null");
		String pass=req.getParameter("pwd");
		
		if(userName.equals("test") && pass.equals("test"))
		{	
			out.println("<h1>valid</h1>");
						
		}
		else
		{
			out.println("<h1> invalid</h1>");
			
		}
		
	}
	

}

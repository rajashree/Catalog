package com.sourcen.session;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ServletListenerSession extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session= request.getSession();
		String str = (String)session.getAttribute("foo");
		pw.println("The name is " + str);
	}
}
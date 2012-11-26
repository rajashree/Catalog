package com.servlets;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.*;

public class GMapServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException, ServletException{
		
		/*response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.print("HelloWorld!!!!!!!");*/
		resp.setContentType("text/html");
        req.setAttribute("zipcode","95138");
        String zipcode=(String) req.getAttribute("zipcode");
        String redirectStr = "Googlemap.jsp?zipcode="+zipcode;
        System.out.println( "redirecting to " + redirectStr );
        resp.sendRedirect( resp.encodeRedirectURL(redirectStr) );
		
	}
}
package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
public class Hidden12 extends HttpServlet
{
  public void doPost(HttpServletRequest req,HttpServletResponse res) 
  {
	 res.setContentType("text/html");
	 String str=req.getParameter("name");
	 try{
	 PrintWriter pw=res.getWriter();
	 
	 pw.println("<html><body><form name=\"second\" action=\"/Servlets/Second12.jsp\" method=\"post\">");
	 pw.println("COLOR : <input type=\"text\" name=\"color\"/>");
	 pw.println("<input type=\"hidden\" name=\"secname\" value='"+str+"'>");
	 pw.println("<br><input type=\"submit\" name=\"submit\">");
	 pw.println("</form></body></html>");
  
  }
	 catch(IOException e)
	 {
		 System.out.println(e.getMessage());
		 
	 }
}
}
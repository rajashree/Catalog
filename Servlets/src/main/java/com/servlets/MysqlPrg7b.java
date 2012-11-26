package com.servlets;

import javax.servlet.http.*;
import java.io.*;

public class MysqlPrg7b extends HttpServlet {
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException
	{  
		res.setContentType("text/html");
	   PrintWriter out=res.getWriter();
	   try{
	         
		  HttpSession ss=req.getSession(true);
		  
		  String nn=req.getParameter("nam");
		  ss.setAttribute("na1",nn);
		  
		  out.println("<html><body><form method='post' action='MysqlPrg7c'>");
		  out.println("Sno : <input type='text' name='sn'><br>");
		 
		  out.println("Sub1: <input type='text' name='s1'><br>");
		  
		  out.println("Sub2 : <input type='text' name='s2'><br>");
		  out.println("Sub3 :<input type='text' name='s3'><br>");
		  out.println("<input type='submit'><br>");
		  out.println("</form></body></html>");
		  
	     		
	}
	   catch(Exception ex)
	   {}

}
}

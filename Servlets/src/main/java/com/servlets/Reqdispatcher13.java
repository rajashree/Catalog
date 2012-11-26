package com.servlets;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
public class Reqdispatcher13  extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
	  res.setContentType("text/html");	
	  
	  
	  try{
		  String n1=req.getParameter("nan");
		  String p1=req.getParameter("pwd");
		  
		  
		  if(n1.equalsIgnoreCase("test") && p1.equalsIgnoreCase("test"))
		  {
			 RequestDispatcher rd=req.getRequestDispatcher("/url113");
			 rd.forward(req,res);
						  
		  }
		  else
			  
		     res.sendRedirect("invalid");
		  
		  
		  
	     }catch(Exception ep){}
	  
	  
	}

}

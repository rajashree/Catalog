package com.servlets;
import javax.servlet.http.*;
import java.io.*;

public class Third1 extends HttpServlet {
     
	  public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	  {
		  PrintWriter out=res.getWriter();
		  HttpSession ss=req.getSession();
		  try{
			  String dd=(String)ss.getAttribute("c1");
			  out.println("Your Favourite Color is:"+dd);
			  	  
			  		  }
		  catch(Exception ex)
		  {}
	  }
}

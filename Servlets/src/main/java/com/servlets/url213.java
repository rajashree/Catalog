package com.servlets;

import javax.servlet.http.*;
import java.io.*;
public class url213 extends HttpServlet{
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		PrintWriter out=res.getWriter();
		HttpSession se=req.getSession();
		
		try{
		   
			String np=(String)se.getAttribute("n1");
			//String cp=(String)se.getAttribute("c1");
			
			out.println("Name is:"+np);
			
										}
		catch(Exception ex)
		{}
		
	}

}

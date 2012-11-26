package com.servlets;

import javax.servlet.http.*;
import java.io.*;

public class MysqlPrg7d extends HttpServlet {
	
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try{
			 HttpSession ss=req.getSession();
			 
			 String na=(String)ss.getAttribute("na1");
			 String num1=(String)ss.getAttribute("su1");
			 String num2=(String)ss.getAttribute("su2");
			 String num3=(String)ss.getAttribute("su3");
			 
			 out.println("Your Name is:"+na);
			 out.println("<br>");
			 out.println("Marks in first subject is:"+Integer.parseInt(num1));
			 out.println("<br>");
			 out.println("Marks in second subject is:"+Integer.parseInt(num2));
			 out.println("<br>");
			 out.println("Marks in third subject is:"+Integer.parseInt(num3));
			 
		}
		catch(Exception ex)
		{ out.println(ex.getMessage());
		}
			
		
		
		
	}

}

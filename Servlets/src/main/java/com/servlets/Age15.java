package com.servlets;
import javax.servlet.http.*;
import java.io.*;

public class Age15 extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{String Age = null;int age = 0;
		PrintWriter out=res.getWriter();
		res.setContentType("text/html");
		try
		{
		 age=Integer.parseInt(req.getParameter("a1"));
			
			
			req.setAttribute("Age",age);
			if (age<30)
				out.println("<html><body>" +
				             "<form method='post' action='/Servlets/Young15'>" +
				             " Name:: <input type='text' name='n1'> " +
				             "<input type='submit'> </html>");
			else
				out.println("<html><body>" +
			             "<form method='post' action='/Servlets/Old15'>" +
			             " Name:: <input type='text' name='n2'> " +
			             "<input type='submit'> </html>");
				
			
		}
		catch(Exception ex){}
  
	out.println("Heloooooooooo!!!!!!!!!!!!"+age);
	}

}
package com.servlets;

import javax.servlet.http.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
public class MysqlPrg7a extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException
	{
		res.setContentType("text/html");
		
		try{
			String na=req.getParameter("nam");
			String pas=req.getParameter("pas");
			
			if(na.equalsIgnoreCase("Raje") && pas.equalsIgnoreCase("Raje"))
			{
				RequestDispatcher rd=req.getRequestDispatcher("/MysqlPrg7b");
				rd.forward(req,res);
			}
			
			else
				res.sendRedirect("MysqlPrg7b.html");
				
				
			}
			
		catch(Exception ex){}	
			
		
		
	}
	

}

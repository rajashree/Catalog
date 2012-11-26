package com;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControlServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String controller=req.getParameter("controller");
		if(controller.equals("Login"))			
		{
			RequestDispatcher rd=req.getRequestDispatcher("./LoginVerify");
			rd.forward(req,res);
		}
		else if(controller.equals("POGeneration"))			
		{
			RequestDispatcher rd=req.getRequestDispatcher("./POGeneration");
			rd.include(req,res);
			res.sendRedirect("Failure.jsp");			
		}   
		else if(controller.equals("POSubmit"))			
		{
			RequestDispatcher rd=req.getRequestDispatcher("./POSubmit");			
			rd.include(req,res);
			res.sendRedirect("Failure.jsp");
		}	
		else if(controller.equals("ASN-Generate"))			
		{
			RequestDispatcher rd=req.getRequestDispatcher("./ASNGenerate");			
			rd.include(req,res);
			res.sendRedirect("Failure.jsp");
		}	
	}	
}

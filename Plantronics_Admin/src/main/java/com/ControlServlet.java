package com;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControlServlet extends HttpServlet
{

    private static final long serialVersionUID = -6428906736725862343L;

    public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String controller=req.getParameter("controller");
		if(controller.equals("Login"))			
		{
			RequestDispatcher rd=req.getRequestDispatcher("/LoginVerify");
			rd.forward(req,res);
		}
		if(controller.equals("ClientLoginDetails"))			
		{
			RequestDispatcher rd=req.getRequestDispatcher("./ClientLoginDetails");
			rd.forward(req,res);
		}
		
		
	}	
}

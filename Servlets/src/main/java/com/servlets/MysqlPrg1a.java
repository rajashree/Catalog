package com.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;

public class MysqlPrg1a  extends HttpServlet{
    private static final long serialVersionUID = -3429838910767133990L;
    ServletContext context;
	public void init()
	{
	  ServletConfig conn=getServletConfig();
	  context =conn.getServletContext();
		
		
	}
	
	public void service(HttpServletRequest req,HttpServletResponse res)throws IOException
	{
		
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try{
			Connection c1=(Connection)getServletContext().getAttribute("connect");
			String na1=req.getParameter("n1");
			String co1=req.getParameter("co");
			
			Statement st=c1.createStatement();
			st.execute("insert into person values('"+na1+"','"+co1+"')");
			RequestDispatcher rd=req.getRequestDispatcher("/MysqlPrg1b");
			rd.forward(req,res);
		}
		catch(Exception ex)
		{
			
		}
	}

}

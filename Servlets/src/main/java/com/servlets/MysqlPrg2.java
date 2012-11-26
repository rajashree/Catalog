package com.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class MysqlPrg2 extends HttpServlet{
	int num ,sal;
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	{
		num=Integer.parseInt( req.getParameter("number"));
		sal=Integer.parseInt( req.getParameter("salary"));
		try
		{
			Class.forName("com.mysql.jdbc.driver");
		}
		catch(Exception e){}
		try
		{
			PrintWriter pw=res.getWriter();
			
			Connection k=DriverManager.getConnection("jdbc:mysql://localhost/Servlet","root","indian");
			Statement s=k.createStatement();
			int r=s.executeUpdate("insert into ServletEx2 values("+num+","+sal+")");
			if(r==0)
			{
				
				pw.println("<html><body>Insertion failed");
				pw.println("<br><a MysqlPrg2.html>back</a></body></html>");
			}
			else
			{
				
				pw.println("<html><body>Insertion success");
				pw.println("<br><a href=MysqlPrg2.html>back</a></body></html>");
			}
		}
		catch(Exception e){
			
			System.out.println(e);
		}
	}

}

package com.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
public class MysqlPrg5a_empinsert extends HttpServlet {

	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		String eno=req.getParameter("eno");
		String ename=req.getParameter("ename");
		String esal=req.getParameter("esal");
		
		int no=Integer.parseInt(eno);
		int sal=Integer.parseInt(esal);
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println(ex.getMessage());
		}
		try{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost/Servlet","root","indian");
			Statement st=con.createStatement();
			
			st.execute("insert into ServletEx5_Employee values("+no+",'"+ename+"',"+sal+")");
			
			
			out.println("<a href=\""+
					res.encodeUrl("MysqlPrg5_test.html")+"\">" +
						"Home </a>"	);
					
			out.println("<center><b>"+eno+"Successfully Inserted");
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
	
}

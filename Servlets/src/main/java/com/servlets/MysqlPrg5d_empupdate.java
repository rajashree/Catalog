package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MysqlPrg5d_empupdate extends HttpServlet {

	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		String ename=req.getParameter("ename");
		
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
			st.execute("update  ServletEx5_Employee set ename='Hardcoded Value' where ename ='"+ename+"'");
			out.println("<a href='MysqlPrg5_test.html'>home</a>");
			out.println("<center><b>"+ename  +" Successfully Updated");
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}

}

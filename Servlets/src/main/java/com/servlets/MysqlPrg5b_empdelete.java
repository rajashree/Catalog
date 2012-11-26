package com.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class MysqlPrg5b_empdelete extends HttpServlet {
	
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		String eno=req.getParameter("eno");
		int no=Integer.parseInt(eno);
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
			st.execute("delete from ServletEx5_Employee where eno ="+no);
			out.println("<a href='MysqlPrg5_test.html'>home</a>");
			out.println("<center><b>"+no +" Successfully Deleted");
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}

	

}

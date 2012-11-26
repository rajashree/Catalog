package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MysqlPrg5c_empdisplay extends HttpServlet{

	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		
		PrintWriter out=res.getWriter();
		res.setContentType("text/html");
		
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
			ResultSet rs=st.executeQuery("select * from ServletEx5_Employee");
			out.println("<html><body>");
			out.println("<a href='MysqlPrg5_test.html'>home</a> <center><h2>Employee Set</h2><table border='1' bgcolor='yellow'>");
			out.println("<tr><td>EmpNumber</td><td>EmpName</td><td>EmpSalary</td></tr>");
			while (rs.next())
			{
				out.println("<tr><td>");
				out.println(rs.getInt(1));
				out.println("</td><td>");
				out.println(rs.getString(2));
				out.println("</td><td>");
				out.println(rs.getInt(3));
			}
			out.println("</td></tr></table></center></body></html>");
			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
}

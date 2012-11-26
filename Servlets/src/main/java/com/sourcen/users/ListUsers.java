package com.sourcen.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Driver;

public class ListUsers extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		try{
			Class.forName("com.mysql.jdbc.Driver");
		
			Driver d = new Driver();
			DriverManager.registerDriver(d);
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/servlets", "root", "");
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from users;");
			out.println("<html><body><a href='./AddUser.jsp'>AddUser</a>");
			out.println("<table border='1' bgcolor='yellow'><tr><td>id</td><td>Name</td><td>Age</td><td>Remove</td></tr>");
			while (rs.next())
			{
				out.println("<tr><td>");
				out.println(rs.getString(1));
				out.println("</td>");
				out.println("<td>");
				out.println(rs.getString(2));
				out.println("</td>");
				out.println("<td>");
				out.println(rs.getString(3));
				out.println("</td><td><a href='./RemoveUser?id="+rs.getString(1)+"'> X</a></td></tr>");
				
			}
			out.println("</td></tr></table></body></html>");
		}catch(Exception ex){}
		
		
	}
}

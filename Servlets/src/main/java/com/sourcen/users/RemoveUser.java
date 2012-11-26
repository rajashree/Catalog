package com.sourcen.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Driver;

public class RemoveUser extends HttpServlet{
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
			int rs = s.executeUpdate("delete from users where id='"+request.getParameter("id")+"';");
			System.out.println(":::::::::::::::"+rs);
			response.sendRedirect("./ListUsers");
		}catch(Exception ex){}
		
	}
}

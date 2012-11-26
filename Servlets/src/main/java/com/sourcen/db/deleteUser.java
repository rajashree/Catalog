package com.sourcen.db;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class deleteUser extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		Connection conn = null;
		try {
			String username = "root";
			String password = "";
			String url = "jdbc:mysql://localhost/servlets";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
			System.out.println ("Database connection established");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement stt;
		try {
			stt = conn.createStatement();
			int id = Integer.parseInt(req.getParameter("id"));
			stt.executeUpdate("DELETE from user where uid='"+id+"'");
			res.sendRedirect("./listUsers");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

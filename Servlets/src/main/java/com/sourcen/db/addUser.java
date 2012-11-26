package com.sourcen.db;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class addUser extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		Connection conn = null;
		try{
			String username = "root";
			String password = "";
			String url = "jdbc:mysql://localhost/servlets";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
			System.out.println ("Database connection established");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		try{
			Statement stt = conn.createStatement();
			int id = Integer.parseInt(req.getParameter("id"));
			String name = req.getParameter("name");
			String pwd = req.getParameter("pwd");
			//String queryStr = "INSERT into user values ("+id+",'"+name+"','"+pwd+"')";
			//stt.executeUpdate(queryStr);
			PreparedStatement ps = conn.prepareStatement("INSERT into user values(?,?,?)");
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(3, pwd);
			int i = ps.executeUpdate();
			if(i!=0)
				System.out.println("Insertion is successful");
			else
				System.out.println("Insertion failed");
			stt.close();
			conn.close();
			res.sendRedirect("./listUsers");
			
	    }
	    catch (SQLException E) {
		
	    }
	}
}

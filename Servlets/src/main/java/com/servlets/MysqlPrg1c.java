package com.servlets;

import javax.servlet.*;

import java.sql.*;
public class MysqlPrg1c implements ServletContextListener{
	public void contextInitialized(ServletContextEvent se)
	{  
		try{
			Class.forName(se.getServletContext().getInitParameter("driverclassname"));
		 }
		catch(ClassNotFoundException ex){}
	 	try{
	 		
	 		Connection c=DriverManager.getConnection("jdbc:mysql://localhost/Servlet","root","indian");
	 		se.getServletContext().setAttribute("connect",c);
	 	}
	 	catch(SQLException ex){}
	}
	public void contextDestroyed(ServletContextEvent se)
	{
		
		  
		try{
			 Connection ce=(Connection)se.getServletContext().getAttribute("connect");
		  	ce.close();
		}
		catch(Exception ex){}
	}
	

}

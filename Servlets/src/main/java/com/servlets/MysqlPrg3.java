package com.servlets;

import javax.servlet.ServletConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;

public class MysqlPrg3 extends HttpServlet {
			
		public Connection con;
		Statement st;
		public void init()
		{
			ServletConfig config=getServletConfig();
		try{
			String Driver=config.getInitParameter("DriverClassName");
			String test=config.getServletName();
			System.out.println("config.getServletName "+test);
			Enumeration tes=config.getInitParameterNames();
              while(tes.hasMoreElements())
			System.out.println("config.getInitParameterNames()"+tes.nextElement());
			Class.forName(Driver);
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println("Class exception"+ex.getMessage());
		}
		try{
			con=DriverManager.getConnection("jdbc:mysql://localhost/Servlet","root","indian");
			st=con.createStatement();
			}
		catch(Exception ex){
			System.out.println("exception"+ex.getMessage());
		}
		}
		
		public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException
		{   res.setContentType("text/html");
			PrintWriter out=res.getWriter();
			try{
				String name=req.getParameter("name");
				String pass=req.getParameter("pass");
			st.execute("insert into ServletEx3 values('"+name+"','"+pass+"')");
			out.println("Successfully Inserted");
		   }
			catch(Exception ex)
			{
				
			}
		
		
		}
	}

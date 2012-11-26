package com.servlets;
import javax.servlet.http.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import java.sql.*;
public class MysqlPrg6a  extends HttpServlet
{ 
  public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
  {  
	  res.setContentType("text/html");
	  PrintWriter out=res.getWriter();
	  try{
		   String driver="com.mysql.jdbc.Driver";
		   Class.forName(driver);
	    }
	  catch(ClassNotFoundException ex)
	  {}
	  try{
		  boolean flag=false;
		  String na=req.getParameter("n1");
		  String pa=req.getParameter("pwd");
		  
		  Connection con=DriverManager.getConnection("jdbc:mysql://localhost/Servlet","root","indian");
		  Statement st=con.createStatement();
		  
		  ResultSet rs=st.executeQuery("select * from ServletEx6a");
		     while(rs.next())
		     {
			  if(na.equalsIgnoreCase(rs.getString(1)) && pa.equalsIgnoreCase(rs.getString(2)))
			  { flag=true;
			    RequestDispatcher rd=req.getRequestDispatcher("/MysqlPrg6b");
			    rd.forward(req,res);
			  }
		     }
			  if(!flag)
				  res.sendRedirect("MysqlPrg6_error.html");
			    
				  
			 
			
		  }
		
	 catch(Exception ex)
	 {
		out.println(ex.getMessage()); 
	 }
	  
  }
}

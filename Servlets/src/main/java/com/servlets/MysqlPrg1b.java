package com.servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
public class MysqlPrg1b extends HttpServlet {
	
   public void service(HttpServletRequest req,HttpServletResponse res)throws IOException
   {
	   res.setContentType("text/html");
	   PrintWriter out=res.getWriter();
	   try{
		   Connection c1=(Connection)getServletContext().getAttribute("connect");
		   Statement st=c1.createStatement();
		   ResultSet rs;
		   rs=st.executeQuery("select * from person");
		   int i=1;
		   
		   while(rs.next())
		   {  
			  out.println("Data of"+i+"record is:"+"<br>");
			  out.println("Name is:"+rs.getString(1)+"<br>");
			  out.println("Color is:"+rs.getString(2)+"<br><br><br>");
			  i++;
			   
		   }
		   
		   
	   }
	   catch(Exception ex){}
   }
}

package com.servlets;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class MysqlPrg6b extends HttpServlet {
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException
  	{ 
		res.setContentType("text/html");
  	  	PrintWriter out=res.getWriter();
  	  	try {
  	  		String driver="com.mysql.jdbc.Driver";
  	  		Class.forName(driver);
  	  	}
  	  	catch(ClassNotFoundException ex)
  	  	{out.println(ex.getMessage());
  	  	}
  	  	try {
    	  		
  	  		   
  	  		    Connection con=DriverManager.getConnection("jdbc:mysql://localhost/Servlet","root","indian");
  	  		    Statement st=con.createStatement();
  	  		    ResultSet rs=st.executeQuery("select * from ServletEx6b");
  	  		    
  	  		    out.println("<html><body><center>Student Report</center><br>");
  	  		     out.println("<u>Names</u><br>");
  	  		     
  	  		    while(rs.next())
  	  		    { 
  	  		    	out.println("<a href=\"" +
  	  		    		   res.encodeURL("/Servlets/MysqlPrg6c?sno="+rs.getInt(1))+"\">" +
  	  		    		   rs.getString(2)+"<br>"+"</a>"
  	  		    			);
  	  		    	
  	  		    	
  	  		    	
  	  		    }
  	  		
  	  	    }
  	  catch(Exception ex){
  		            out.println(ex.getMessage());
  	                   }
		
	}

}

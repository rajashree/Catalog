package com.servlets;
import java.io.*;
import javax.servlet.http.*;
import java.sql.*;

public class MysqlPrg6c extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try{
			Class.forName("com.mysql.jdbc.Driver");
		   }
		catch(ClassNotFoundException ex)
		{out.println(ex.getMessage());}
		try{
			int sn=Integer.parseInt(req.getParameter("sno"));
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost/Servlet","root","indian");
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from ServletEx6b where sno="+sn);
			while(rs.next())
			{
			    out.println("Welcome"+rs.getString(2)+"<br>");
			    out.println("Your no is:" + rs.getInt(1)+"<br>");
			    out.println("Your Favourite color is"+rs.getString(3));
				
			
			}
			
			
		}catch(Exception ep){
			ep.getMessage();
		}
	}

}

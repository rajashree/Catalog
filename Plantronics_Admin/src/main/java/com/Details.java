package com;

import com.mysql.jdbc.Driver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class Details extends HttpServlet
{

    private static final long serialVersionUID = -7941263076846812461L;

    public void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		PrintWriter out=res.getWriter();
		res.setContentType("text/html");
		try{
			Class.forName("com.mysql.jdbc.Driver");
		
			Driver d = new Driver();
			DriverManager.registerDriver(d);
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/raje", "root", "indian");
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from users;");
			out.println("<html><body>");
			out.println("<table border='1' bgcolor='yellow'><tr><td>EmpName</td><td>EmpSalary</td></tr>");
			while (rs.next())
			{
				
				out.println("<tr><td>");
				//out.println(rs.getString(2));
				List l = new ArrayList();
				l = (List) rs;
				
				out.println("</td><td>");
				out.println(rs.getString(1));
			}
			out.println("</td></tr></table></body></html>");
		}catch(Exception ex){}
	}	
}

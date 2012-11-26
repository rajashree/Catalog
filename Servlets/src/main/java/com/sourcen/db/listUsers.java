package com.sourcen.db;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;



import java.sql.*;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class listUsers extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		Connection conn = null;
		res.setContentType("text/html");
	    
	    ServletOutputStream out = res.getOutputStream();
	    out.println("<html>");
	    out.println("<head><title>User Management</title></head>");
	    out.println("<body>");
	    out.println("<h1>Users List :</h1>");
	    try {
	    	String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/servlets";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");

	    }
	    catch (Exception E) {
		E.printStackTrace();
	    }
	    
	    try {
	    	Statement Stmt = conn.createStatement ();
	    	Stmt.executeQuery ("SELECT * from user");
	    	ResultSet RS = Stmt.getResultSet ();
	    	ResultSetMetaData rsmd = RS.getMetaData();
	    	int colcount = rsmd.getColumnCount();
	    	for(int i= 1; i<=colcount; i++){
	    		out.println("column name -> "+ rsmd.getColumnName(i));
	    	}
	    	out.println("<table><th><td>UId</td><td>Name</td><td>Password</td><td>Delete</td></th>");
	    	
			while (RS.next()) {
			   out.println("<tr>");
			   out.println("<td>"+RS.getString(1)+"</td>");
			   out.println("<td>"+RS.getString(2)+"</td>");
			   out.println("<td>"+RS.getString(3)+"</td>");
			   out.println("<td>"+"<a href='./deleteUser?id="+RS.getString(1)+"'>X</a>"+"</td>");
			   out.println("</tr>");
			}
			out.println("</table>");
			// Clean up after ourselves
			RS.close();
			Stmt.close();
			conn.close();
			
	    }
	    catch (SQLException E) {
		
	    }
	   //ServletContext sc = this.getServletContext();
	   //RequestDispatcher rd = sc.getRequestDispatcher("/jsp/addUser.jsp");
	   //rd.forward(req, res);
	    out.println("<a href='./jsp/addUser.jsp'>Add User</a><br/>"); 
	    out.println("</body></html>"); 
	    out.println("Time displayed in spanish <br/>");
	    res.setHeader("Content-Language", "es");
	    Locale locale = new Locale("es","");
	    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG , locale);
	    df.setTimeZone(TimeZone.getDefault());
	    out.println(df.format(new Date(0)));
	    
	    String acceptLanguage = req.getHeader("Accept-Language");
	    String acceptCharset = req.getHeader("Accept-Charset");
	    out.println("<h2>Accept Language and Accept Charset</h2>");
	    out.println("AcceptLanguage: " + acceptLanguage+"<br>");
	    out.println("AcceptCharset" + acceptCharset);
	}
	
	public String getServletInfo() {
	  return "Create a page that says <i>Hello World</i> and send it back";
	}
}

package com;

import com.mysql.jdbc.Driver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginVerify extends HttpServlet
{


    private static final long serialVersionUID = 6270272357112364427L;
    int flag=1;
    int status=0;
   
	public String ValidateLogin(String username, String password) {
		if (username != null && password != null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Driver d = new Driver();
				DriverManager.registerDriver(d);
				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost/Plantronics", "root", "");
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("select * from Admin where UserName='"+username+"'");
				if(rs.next())
				{
					ResultSet rset = s
							.executeQuery("select * from Admin where UserName='"
									+ username
									+ "' and PassWord='"
									+ password
									+ "'");
					if (rset.next()) {
						flag=0;
						
					}else{
						status=2;
                		flag++;
                		System.out.println("Invalid Password");
					}
				}else{
					status=1;
                	flag++;    
					System.out.println("Invalid Username");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if(flag==0)
			return "true";
		else
			return "false";
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String username = req.getParameter("username");
		String userpwd = req.getParameter("password");
		HttpSession session=req.getSession(true);
		String result = ValidateLogin(username,userpwd);
		System.out.println("LOGIN RESULT"+result);
		if(result.equals("true"))
        {
    		
    		session.setAttribute("status", "success");
    		//transfer the control 
    		
    		//res.sendRedirect("./Details");
    			res.sendRedirect("ProductList.jsp");
    				
        }
        else
        {
        	res.sendRedirect("LoginFailure.jsp?status="+status+"&name="+username);
        	
        }	        
	}	
}

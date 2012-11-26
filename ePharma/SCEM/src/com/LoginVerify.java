package com;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rdta.tlapi.xql.*;

public class LoginVerify extends HttpServlet
{
	private Connection conn = null;      
    private String userName = "admin";
    private String password = "admin";
    private static String url = "xql:rdtaxql://localhost:3408";
    private Statement statement = null;
    private ResultSet rs=null;
	private static final long serialVersionUID = 1L;
	
	String username=null;
	String userpwd=null;
	String type=null;
	int flag=1;
    int status=0;
	
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		username=req.getParameter("username");
		userpwd=req.getParameter("password");
		type=req.getParameter("type");
		HttpSession session=req.getSession(true);
		
		try
		{
				String XQuery="let $x :=(for $i in collection('tig:///Role/USER')/users/user where $i/UserName ='";
				XQuery=XQuery+username+"' return $i)return data($x/password)";

				try
				{
					DataSource dataSource = (DataSource) DataSourceFactory.getDataSource(url,null);
					conn = dataSource.getConnection(userName, password);				
				}
				catch(XQLException ex)
				{
					session.setAttribute("status", ex.getMessage());
					res.sendRedirect("Failure.jsp");
				}
				
               	statement = conn.createStatement();                
               	rs=statement.execute(XQuery);
                if(rs.next())
                {
                	//username valid
                	if(rs.getString().equals(userpwd))
                	{
                		//password is valid                		
                		flag=0;
                	}
                	else
                	{
                		status=2;
                		flag++;                		
                	}
                }                	
                else
                {
                	status=1;
                	flag++;                	
                }                	
        }
		catch(XQLConnectionException ex)
        {
			session.setAttribute("status", ex.getMessage());
			res.sendRedirect("Failure.jsp");
		}        	
        catch (XQLException ex)
        {
        	session.setAttribute("status", ex.getMessage());
			res.sendRedirect("Failure.jsp");
		}        
        finally
        {
	        try
	        {
	        	if(conn!=null)
	        		conn.close();
	        	if(flag==0)
	            {
	        		session.setAttribute("type", type);
	        		session.setAttribute("status", "success");
	        		//transfer the control to seller/buyer
	        		
            		if(type.equals("seller"))
            		{
            			res.sendRedirect("Seller.jsp");
            		}
            		else
            		{
            			res.sendRedirect("Buyer.jsp");
            		}       		
	            }
	            else
	            {
	            	res.sendRedirect("LoginFailure.jsp?status="+status+"&name="+username);
	            }	        	
	        }
	        catch (XQLException ex)
	        {
	        	session.setAttribute("status", ex.getMessage());
				res.sendRedirect("Failure.jsp");
			}
        }
    }	
}

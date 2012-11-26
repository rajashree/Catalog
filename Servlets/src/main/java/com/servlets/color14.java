package com.servlets;
import javax.servlet.http.*;
import java.io.*;


public class color14  extends HttpServlet {
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		HttpSession ses=req.getSession(true);
		try
		{
		  String nam=req.getParameter("na");
		  String co=req.getParameter("col");
		  
		   ses.setAttribute("n1",nam);
		   ses.setAttribute("c1",co);
		  
		  out.println("<html> <body><form method='post' action=\"/Servlets/one14\">" +
				       "<input type='submit'></form></body> </html>" );
			
			
		}
		catch(Exception ex)
		{
			out.println(ex.getMessage());
		}
						
	}

}

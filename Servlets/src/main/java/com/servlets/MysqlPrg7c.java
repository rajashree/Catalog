package com.servlets;
import javax.servlet.http.*;

import java.io.*;

public class MysqlPrg7c extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try{
			 out.println("<html><body><b><center>welcome to result servlet</b></center>");
			 
			HttpSession ss=req.getSession();
			 
			 int nn=Integer.parseInt(req.getParameter("sn"));
			 int n1=Integer.parseInt(req.getParameter("s1"));
			 int n2=Integer.parseInt(req.getParameter("s2"));
			 int n3=Integer.parseInt(req.getParameter("s3"));
			 
			 
			 
			 
			 
			 ss.setAttribute("seno",Integer.toString(nn));
			 ss.setAttribute("su1",Integer.toString(n1));
			 ss.setAttribute("su2",Integer.toString(n2));
			 ss.setAttribute("su3",Integer.toString(n3));
			 
			 
			 out.println("<a href=\"" +
			 		      res.encodeURL("/Servlets/MysqlPrg7e")+"\">" +
			 		      "click here to view total </a><br>");
			 out.println("<a href=\"" +
					       res.encodeURL("/Servlets/MysqlPrg7d") +"\">" +
					       "click here to view marks </a>"  );
			 
			 
			 out.println("</body></html>");
			 
					                
		}
		catch(Exception ex){
			String message = ex.getMessage();
			res.sendError(
			       HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			       message);
		}
	}
}
		
		
			 
			 
			 
			 
			 
			
			
		   
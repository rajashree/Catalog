package com.servlets;
import javax.servlet.http.*;
import java.io.*;


public class url113 extends HttpServlet {
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
	HttpSession ses =req.getSession(true);
		try{
			String nn=req.getParameter("nan");
			String cc=req.getParameter("col");
			
			ses.setAttribute("n1",nn);
			ses.setAttribute("c1",cc);
			
			out.println("<b><center>Welcome to HomePage</center></b>");
			
			out.println("<p><a href=\"" +
					res.encodeURL("/Servlets/url213")+ "\">" +
					"click here to see name </a><br>");
			 
		    out.println("<p><a href=\"" +
		    		res.encodeUrl("/Servlets/url313")+"\">"+
		    		"click here to see color</a>");
		    		     
		    
		                
			
			
			
		}
		catch(Exception ex)
		{
			out.println(ex.getMessage());
		}
						
	}

}

package com.servlets;



	import javax.servlet.*;

	import java.io.*;
	public class servlet_config10 extends GenericServlet{
		ServletConfig t;
		public void init(ServletConfig s){
			t=s;
		}
		public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
		{
			res.setContentType("text/html");
			String val=t.getInitParameter("raj");
			PrintWriter out= res.getWriter();
			out.println("<h1>"+val+"</h1>" );
				
					
		}
		

	}


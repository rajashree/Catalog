package com.servlets;
import javax.servlet.http.*;
import java.io.*;
public class one14  extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
		PrintWriter out=res.getWriter();
		HttpSession ss=req.getSession();
		try{
			String nn=(String)ss.getAttribute("n1");
			String cc=(String)ss.getAttribute("c1");
			out.println("Your Name is:"+nn);
			out.println("Your Favourite Colour is:"+cc);
			
		}
		catch(Exception ex)
		{}
	}

}

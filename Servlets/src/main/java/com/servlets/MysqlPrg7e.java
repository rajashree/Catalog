package com.servlets;
import javax.servlet.http.*;
import java.io.*;
public class MysqlPrg7e extends HttpServlet {
	
	 public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException
	 {
		 res.setContentType("text/html");
		 PrintWriter out=res.getWriter();
		 
		 try{
			  HttpSession ss=req.getSession();
			  
			 String na=(String)ss.getAttribute("na1");
			 String numb=(String)ss.getAttribute("seno");
			 String num1=(String)ss.getAttribute("su1");
			 String num2=(String)ss.getAttribute("su2");
			 String num3=(String)ss.getAttribute("su3");
			 
			 out.println("Welcome to"+na);
			 out.println("<br>");
			 int tot=0;
			 tot=Integer.parseInt(num1)+Integer.parseInt(num2)+Integer.parseInt(num3);
			 out.println("Total is"+tot);
			 
			 
			 
		 }
		 catch(Exception ex)
		 {}
		 
	 }
	

}

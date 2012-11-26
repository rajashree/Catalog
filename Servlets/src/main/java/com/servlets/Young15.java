package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Young15 extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException{
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html");
		
		String age = (String) req.getAttribute("Age");
		String name = (String) req.getAttribute("n1");
	
		pw.println(" Name ::: "+name);
		pw.println("Age ::: "+age);
		
	}

}

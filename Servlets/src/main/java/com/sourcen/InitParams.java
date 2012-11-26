package com.sourcen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitParams extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		Enumeration enu = getServletConfig().getInitParameterNames();
		PrintWriter pw = res.getWriter();
		while(enu.hasMoreElements()){
			String pname = (String)enu.nextElement();
			pw.println("param names::::"+ pname);			
			pw.println("param values::::"+getServletConfig().getInitParameter(pname));
		}
	}

}

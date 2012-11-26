package com.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Ch4_e_QueryString extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
		res.sendRedirect(res.encodeRedirectURL("/Servlets/Ch4_e_ParameterSnoop?Name=hello&Password=hello"));
	}

}

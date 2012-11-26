package com.sourcen.cookies;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookies extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res){
		Cookie cookie = null;
		Cookie[] cookies = req.getCookies();
		PrintWriter pw = null;
		try {
			pw = res.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Boolean cookieSet = false;
		if(cookies!=null )
		for(int i=0;i<cookies.length;i++){
			if(cookies[i].getName().equals("dev")){
				cookieSet =true;
				cookie = cookies[i];
			}
		}
		Date date = new Date();
		if(!cookieSet){
			cookie = new Cookie("dev","Cookie Set by Devashree");
			cookie.setPath(req.getContextPath());
			cookie.setMaxAge(100);
			res.addCookie(cookie);
			pw.println("Cookie set for the first time");
		}
		pw.println("::::::::::::::::"+cookie.getName()+" "+cookie.getValue()+" "+cookie.getMaxAge()+" "+cookie.getPath());
	}
}

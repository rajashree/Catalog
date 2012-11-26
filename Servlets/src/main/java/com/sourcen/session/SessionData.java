package com.sourcen.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionData extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		PrintWriter pw = res.getWriter();
		HttpSession sess = req.getSession(true);
		String state = "";
		Integer count = 0;
		if(sess.isNew())
			state = "New Session";
		else{
			state = "Old Session";
			Integer oldCount = (Integer)sess.getAttribute("count");
			if(oldCount != null)
				count = oldCount + 1;
		}
		sess.setAttribute("count", count);	
		pw.println(state);
		pw.println("Session Creation Time "+ new Date(sess.getCreationTime()));
		pw.println("Session LastAccess Time "+ new Date(sess.getLastAccessedTime()));
		pw.println("Session value "+ sess.getValue("count"));
	}

}

package com.servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Ch4_d_VersionSnoop extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse res)
                        throws ServletException,IOException {
    res.setContentType("text/plain");
    PrintWriter out =res.getWriter();

    out.println("Servlet Version:"+getServletContext().getMajorVersion());
    out.println("Java Version:"+System.getProperty("java.runtime.version"));
  }
}

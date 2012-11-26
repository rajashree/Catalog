package com.servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ch4_e_ParameterSnoop extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    out.println("Query String:");
    out.println(req.getQueryString());
    out.println();

    out.println("Request Parameters:");
    Enumeration en = req.getParameterNames();
    while (en.hasMoreElements()) {
      String name = (String) en.nextElement();
      String values[] = req.getParameterValues(name);
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          out.println(name + " (" + i + "): " + values[i]);
        }
      }
    }
  }
} 

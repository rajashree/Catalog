package com.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ch3_a_SimpleCounter extends HttpServlet {

  int count = 0;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    count++;
    out.println("Since loading, this servlet has been accessed " +
                count + " times.");
  }
}

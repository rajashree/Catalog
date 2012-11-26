package com.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ch3_b_InitCounter extends HttpServlet {

  int count;

  public void init() throws ServletException {
    String initial = getInitParameter("initial");
    try {
      count = Integer.parseInt(initial);
    }
    catch (NumberFormatException e) {
      count = 0;
    }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                           throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    count++;
    out.println("Since loading (and with a possible initialization");
    out.println("parameter figured in), this servlet has been accessed");
    out.println(count + " times.");
  }
}

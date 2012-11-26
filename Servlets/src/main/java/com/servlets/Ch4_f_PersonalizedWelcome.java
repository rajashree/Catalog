package com.servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ch4_f_PersonalizedWelcome extends HttpServlet {

  Hashtable accesses = new Hashtable();

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    // ...Some introductory HTML...

    String remoteUser = req.getRemoteUser();

    if (remoteUser == null) {
      out.println("Welcome!");
    }
    else {
      out.println("Welcome, " + remoteUser + "!");
      Date lastAccess = (Date) accesses.get(remoteUser);
      if (lastAccess == null) {
        out.println("This is your first visit!");
      }
      else {
        out.println("Your last visit was " + accesses.get(remoteUser));
      }

      if (remoteUser.equals("PROFESSOR FALKEN")) {
        out.println("Shall we play a game?");
      }

      accesses.put(remoteUser, new Date());
    }

    // ...Continue handling the request...
  }
}

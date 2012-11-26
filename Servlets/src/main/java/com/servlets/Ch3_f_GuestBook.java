package com.servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class Ch3_f_GuestBook extends HttpServlet {

  private Vector entries = new Vector();  // User entry list
  private long lastModified = 0;          // Time last entry was added
  
  // Display the current entries, then ask for a new entry
  public void doGet(HttpServletRequest req, HttpServletResponse res) 
                               throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    printHeader(out);
    printForm(out);
    printMessages(out);
    printFooter(out);
  }

  // Add a new entry, then dispatch back to doGet()
  public void doPost(HttpServletRequest req, HttpServletResponse res) 
                                throws ServletException, IOException {
    handleForm(req, res);
    doGet(req, res);
  }

  private void printHeader(PrintWriter out) throws ServletException {
    out.println("<HTML><HEAD><TITLE>Guestbook</TITLE></HEAD>");
    out.println("<BODY>");
  }

  private void printForm(PrintWriter out) throws ServletException {
    out.println("<FORM METHOD=POST>");  // posts to itself
    out.println("<B>Please submit your feedback:</B><BR>");
    out.println("Your name: <INPUT TYPE=TEXT NAME=name><BR>");
    out.println("Your email: <INPUT TYPE=TEXT NAME=email><BR>");
    out.println("Comment: <INPUT TYPE=TEXT SIZE=50 NAME=comment><BR>");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Send Feedback\"><BR>");
    out.println("</FORM>");
    out.println("<HR>");
  }

  private void printMessages(PrintWriter out) throws ServletException {
    String name, email, comment;

    Enumeration e = entries.elements();
    while (e.hasMoreElements()) {
      GuestbookEntry entry = (GuestbookEntry) e.nextElement();
      name = entry.name;
      if (name == null) name = "Unknown user";
      email = entry.email;
      if (name == null) email = "Unknown email";
      comment = entry.comment;
      if (comment == null) comment = "No comment";
      out.println("<DL>");
      out.println("<DT><B>" + name + "</B> (" + email + ") says");
      out.println("<DD><PRE>" + comment + "</PRE>");
      out.println("</DL>");

      // Sleep for half a second to simulate a slow data source
      try { Thread.sleep(500); } catch (InterruptedException ignored) { }
    }
  }

  private void printFooter(PrintWriter out) throws ServletException {
    out.println("</BODY>");
  }

  private void handleForm(HttpServletRequest req,
                          HttpServletResponse res) {
    GuestbookEntry entry = new GuestbookEntry();

    entry.name = req.getParameter("name");
    entry.email = req.getParameter("email");
    entry.comment = req.getParameter("comment");

    entries.addElement(entry);

    // Make note we have a new last modified time
    lastModified = System.currentTimeMillis();
  }

  public long getLastModified(HttpServletRequest req) {
    return lastModified;
  }
}

class GuestbookEntry {
  public String name;
  public String email;
  public String comment;
}

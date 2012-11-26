package com.servlets;

import javax.servlet.*; 
import javax.servlet.http.*; 
import java.io.*; 
import java.util.*; 
 
public class CookieServlet16 extends HttpServlet {

  /**Process the HTTP Get request*/ 
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {  
    Cookie c1 = new Cookie("userName", "Rajashree"); 
    Cookie c2 = new Cookie("password", "RajashreePwd"); 
    response.addCookie(c1); 
    response.addCookie(c2); 
    Cookie c3=new Cookie("Rajashree","test");
    c3.setPath(request.getContextPath());
    c3.setMaxAge(120);
    response.addCookie(c3);
    response.setContentType("text/html"); 
    PrintWriter out = response.getWriter(); 
    out.println("<HTML>"); 
    out.println("<HEAD>"); 
    out.println("<TITLE>Cookie Test</TITLE>"); 
    out.println("</HEAD>"); 
    out.println("<BODY>"); 
    out.println("Please click the button to see the cookies sent to you."); 
    out.println("path"+c3.getPath());
    out.println("path"+request.getContextPath());
    out.println("<BR>"); 
    out.println("<FORM METHOD=POST>"); 
    out.println("<INPUT TYPE=SUBMIT VALUE=Submit>"); 
    out.println("</FORM>"); 
    out.println("</BODY>"); 
    out.println("</HTML>"); 
  } 
  /**Process the HTTP Post request*/ 
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws 
ServletException,IOException {
    response.setContentType("text/html"); 
    PrintWriter out = response.getWriter(); 
    out.println("<HTML>"); 
    out.println("<HEAD>"); 
    out.println("<TITLE>Cookie Test</TITLE>"); 
    out.println("</HEAD>"); 
    out.println("<BODY>"); 
    out.println("<H2>Here are all the headers.</H2>"); 

    Enumeration enum1 = request.getHeaderNames(); 
    while (enum1.hasMoreElements()) {
      String header = (String) enum1.nextElement(); 
      out.print("<B>" + header + "</B>: "); 
      out.print(request.getHeader(header) + "<BR>"); 
    } 

    out.println("<BR><BR><H2>And, here are all the cookies.</H2>"); 
    Cookie[] cookies = request.getCookies(); 
    int length = cookies.length; 
    for (int i=0; i<length; i++) {
      Cookie cookie = cookies[i]; 
      
      
      out.println("<B>Cookie Name:</B> " + cookie.getName() + "<BR>"); 
      out.println("<B>Cookie Value:</B> " + cookie.getValue() + "<BR>"); 
    } 
     out.println("</BODY>"); 
     out.println("</HTML>"); 
   } 
}



package com.servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ch5_a_KeepAlive extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {

    res.setContentType("image/gif");

    // Ask for a 16K byte response buffer; do not set the content length
   // res.setBufferSize(16 * 1024);

    PrintWriter out = res.getWriter();
    out.println("<HTML>");
    out.println("<HEAD><TITLE>Hello World</TITLE></HEAD>");
    out.println("<BODY>");
    out.println("<BIG>Less than 16K of response body</BIG><img src='file://C:/Tomcat 5.5/webapps/Servlets/WEB-INF/classes/com/servlets/Picture2.gif' alt='No Image'>Hello");
    out.println("</BODY></HTML>");
  }
}

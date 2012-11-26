package com.servlets;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.servlets.util.HTMLFilter;


public class RequestParamExample8 extends HttpServlet {


    
    
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<head>");

       
        out.println("<title>Request Parameters Example</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
        out.println("<h3>Request Parameters Example</h3>");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        out.println("The Params in this request <br>");
        if (firstName != null || lastName != null) {
            
            out.println("First Name = " + HTMLFilter.filter(firstName) + "<br>");
            out.println("Last Name = " + HTMLFilter.filter(lastName));
        } else {
            out.println("No Params in this request");
        }
        out.println("<P>");
        out.print("<form action=\"");
        out.print("RequestParamExample\" ");
        out.println("method=POST>");
        out.println(" FirstName ");
        out.println("<input type=text size=20 name=firstname>");
        out.println("<br>");
        out.println(" LastName ");
        out.println("<input type=text size=20 name=lastname>");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}

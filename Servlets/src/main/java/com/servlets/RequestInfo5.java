package com.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RequestInfo5 extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<head>");
        out.println("<title>Request Information Example</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h3>Request Information Example</h3>");
        out.println("<blockquote><table border='1'>");
       
        out.println("<tr><td>getAuthType:</td><td>" + request.getAuthType()+"</td></tr>");
        out.println("<tr><td>getCharacterEncoding:</td><td>" + request.getCharacterEncoding()+"</td></tr>");
        out.println("<tr><td>getContentLength:</td><td>" + request.getContentLength()+"</td></tr>");
        out.println("<tr><td>getContentType:</td><td>" + request.getContentType()+"</td></tr>");
        out.println("<tr><td>getContextPath:</td><td>" + request.getContextPath()+"</td></tr>");
        out.println("<tr><td>getLocalAddr:</td><td>" + request.getLocalAddr()+"</td></tr>");
        out.println("<tr><td>getLocalName:</td><td>" + request.getLocalName()+"</td></tr>");
        out.println("<tr><td>getLocalPort:</td><td>" + request.getLocalPort()+"</td></tr>");
        out.println("<tr><td>getPathInfo:</td><td>" + request.getMethod()+"</td></tr>");
        out.println("<tr><td>getPathTranslated:</td><td>" + request.getPathInfo()+"</td></tr>");
        out.println("<tr><td>getPathTranslated:</td><td>" + request.getPathTranslated()+"</td></tr>");
        out.println("<tr><td>getProtocol:</td><td>" + request.getProtocol()+"</td></tr>");
        out.println("<tr><td>getQueryString:</td><td>" + request.getQueryString()+"</td></tr>");
        out.println("<tr><td>getRemoteAddr:</td><td>" + request.getRemoteAddr()+"</td></tr>");
        out.println("<tr><td>getRemoteHost:</td><td>" + request.getRemoteHost()+"</td></tr>");
        out.println("<tr><td>getRemotePort:</td><td>" + request.getRemotePort()+"</td></tr>");
        out.println("<tr><td>getRemoteUser:</td><td>" + request.getRemoteUser()+"</td></tr>");
        out.println("<tr><td>getRequestedSessionId:</td><td>" + request.getRequestedSessionId()+"</td></tr>");
        out.println("<tr><td>getRequestURI:</td><td>" + request.getRequestURI()+"</td></tr>");
        out.println("<tr><td>getRequestURL:</td><td>" + request.getRequestURL()+"</td></tr>");
        out.println("<tr><td>getScheme:</td><td>" + request.getScheme()+"</td></tr>");
        out.println("<tr><td>getServerName:</td><td>" + request.getServerName()+"</td></tr>");
        out.println("<tr><td>getServerPort:</td><td>" + request.getServerPort()+"</td></tr>"); 
        out.println("<tr><td>getServletPath:</td><td>" + request.getServletPath()+"</td></tr>");
        out.println("</table></blockquote>");
        
        
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * We are going to perform the same operations for POST requests
     * as for GET methods, so this method just sends the request to
     * the doGet method.
     */

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        doGet(request, response);
    }
}
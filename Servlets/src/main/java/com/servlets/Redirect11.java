package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
/**
 * Perform a redirect for the page "redirect.jsp"
 */
public class Redirect11 extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
    redirect( request, response );
  }
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
    redirect( request, response );
  }
  /**
   * Set a few URL parameters and objects for the request to see what happens
   * to them during a redirect.
   */
  protected void redirect(HttpServletRequest req,HttpServletResponse resp)
    throws ServletException, IOException{
    log( "A request arrived for " + req.getServletPath() );
    // Put some objects into request scope
    resp.setContentType("image/gif");  
    req.setAttribute( "firstName", "John" );
    req.setAttribute( "lastName", "Doe" );
    String contextPath = req.getContextPath();
    String redirectStr = contextPath + "/result.jsp?firstName=John&lastName=Doe";
    log( "redirecting to " + redirectStr );
    // Always call the encodeRedirectURL method when perfoming a redirect
    resp.sendRedirect( resp.encodeRedirectURL(redirectStr) );
  }
}
 













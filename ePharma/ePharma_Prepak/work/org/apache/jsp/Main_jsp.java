package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class Main_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;


String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();

String sessionID = request.getParameter("sessionID");
		session.setAttribute("sessionID",sessionID);
		System.out.println("-------------->-"+session.getAttribute("sessionID"));


      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Frameset//EN\">\r\n");
      out.write("<html>\r\n");
      out.write("\t<head>\r\n");
      out.write("\t\t<TITLE>EPCIS Administration Console</TITLE></head>\r\n");
      out.write("\t<frameset cols=\"170,74%\" border=\"0\">\r\n");
      out.write("\t\t<frame name=\"contents\" src=\"Menu.jsp?sessionID=");
      out.print(sessionID);
      out.write("\"   noresize=\"noresize\">\r\n");
      out.write("\t\t<frame name=\"main\" src=\"./dist/admin/Introduction.html\">\r\n");
      out.write("\t\t</frameset><noframes>\r\n");
      out.write("\t\t\t<p>This page requires frames, but your browser does not support them.</p>\r\n");
      out.write("\t\t</noframes>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

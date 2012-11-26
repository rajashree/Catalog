package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class SignOut_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  
    session.setAttribute("login",null);
	session.setAttribute("display",null);
	session.invalidate();

      out.write("\r\n");
      out.write("<html><head><title>Welcome</title>\r\n");
      out.write("<style type=\"text/css\">\r\n");
      out.write("<!--\r\n");
      out.write(".font1{font-family:verdana;font-size:11px;text-decoration:none}\r\n");
      out.write(".font2{font-family:verdana;font-size:12px;text-decoration:none}\r\n");
      out.write(".font3{font-family:verdana;font-size:13px;text-decoration:none}\r\n");
      out.write(".font4{font-family:verdana;font-size:18px;text-decoration:none}\r\n");
      out.write("//-->\r\n");
      out.write("</style>\r\n");
      out.write("</head>\r\n");
      out.write("<body leftmargin=\"0\" topmargin=\"0\" alink=\"#0066ff\" bgcolor=\"#ffffff\" link=\"#0066ff\" marginheight=\"0\" marginwidth=\"0\" vlink=\"#0066ff\">\r\n");
      out.write("<form>\r\n");
      out.write("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n");
      out.write("<tbody><tr>\r\n");
      out.write("<td>\r\n");
      out.write("<br><br>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("<td bgcolor=\"#000000\" height=\"6\">\r\n");
      out.write("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td height=\"6\"></td></tr></tbody></table>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("<td align=\"center\" bgcolor=\"#ffeecc\">\r\n");
      out.write("<br><br><br>\r\n");
      out.write("<table bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"406\">\r\n");
      out.write("<tbody><tr>\r\n");
      out.write("<td>\r\n");
      out.write("<table bgcolor=\"#ffffee\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"404\">\r\n");
      out.write("<tbody><tr>\r\n");
      out.write("<td align=\"center\">\r\n");
      out.write("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td height=\"9\"></td></tr></tbody></table>\r\n");
      out.write("\r\n");
      out.write("<font class=\"font4\">You have been logged out.</font><br><br>\r\n");
      out.write("<font class=\"font3\">\r\n");
      out.write("Your session is invalid.<br><br>\r\n");
      out.write("<a href=\"Login.jsp\"><b>Please login again</b></a><br><br>\r\n");
      out.write("</font>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody></table>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody></table>\r\n");
      out.write("<br><br><br><br><br>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("<td class=\"font1\" align=\"center\" height=\"35\">\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody></table>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("</body></html>");
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

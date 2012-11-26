package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class Login_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  
    session.setAttribute("type",null);
	session.setAttribute("status",null);
	session.invalidate();

      out.write("\r\n");
      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n");
      out.write("<HTML>\r\n");
      out.write("<HEAD>\r\n");
      out.write("<TITLE> Welcome </TITLE>\r\n");
      out.write("<SCRIPT>\r\n");
      out.write("\tfunction validate()\r\n");
      out.write("\t{\r\n");
      out.write("\t\tusername=login.username.value;\r\n");
      out.write("\t\tif (username=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert(\"Enter UserName\");\r\n");
      out.write("\t\t\tdocument.login.username.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tpassword=login.password.value;\r\n");
      out.write("\t\tif (password=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert(\"Enter Password\");\r\n");
      out.write("\t\t\tdocument.login.password.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\t\t\r\n");
      out.write("\t}\r\n");
      out.write("</SCRIPT>\r\n");
      out.write("</HEAD>\r\n");
      out.write("<BODY onLoad=\"document.login.username.focus()\">\r\n");
      out.write("\r\n");
      out.write("<br><br><br><br><br><br><br><br><br>\r\n");
      out.write("<TABLE WIDTH=\"50%\" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1>\r\n");
      out.write("<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face=\"Bodoni MT\" \r\n");
      out.write("      color=white size=5><STRONG>Supply Chain \r\n");
      out.write("    Management</STRONG></FONT></TD></TR>\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD><TABLE WIDTH=\"100%\" ALIGN=center BORDER=0 CELLSPACING=0 CELLPADDING=0>\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD align=left valign=center><IMG id=IMG1 style=\"WIDTH: \r\n");
      out.write("            239px; HEIGHT: 153px\" height=153 alt=\"\" src=\"Images\\Home.jpg\" width=215 ></TD>\r\n");
      out.write("\t\t<TD align=middle valign=center>\r\n");
      out.write("\t\t\t<form id=\"login\" name=\"login\" action=\"./ControlServlet\" method=post onsubmit=\"return validate(this);\">\r\n");
      out.write("\t\t\t<input type=\"hidden\" value=\"Login\" id=\"controller\" name=\"controller\">\t\t\t\r\n");
      out.write("\t\t\t<table width=\"100%\" align=right>\r\n");
      out.write("\t\t\t<tr><td><font color=\"navy\"><b>User Name</b></font></td><td><input name='username' id='username' width=\"20\" ></td></tr>\r\n");
      out.write("\t\t\t<tr><td><font color=\"navy\"><b>Password</b></font></td><td><input type=password name=\"password\"  id=\"password\" width=\"20\"></td></tr>\r\n");
      out.write("\t\t\t<tr><td><font color=\"navy\"><b>User Type</b></font></td><td><select name=\"type\" id=\"type\"><option value=\"buyer\" selected>Buyer&nbsp;</option><option value=\"seller\" >Seller</option></select></td></tr>\r\n");
      out.write("\t\t\t<tr><td colspan=2 align=middle><input type=submit value=\"Login\" id=\"login\" name=\"login\"></td></tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t</form>\t\t\r\n");
      out.write("\t\t</TD>\r\n");
      out.write("\t</TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("</TD>\r\n");
      out.write("</TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("</BODY>\r\n");
      out.write("</HTML>\r\n");
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

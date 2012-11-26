package org.apache.jsp.dist;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class login_002ddist_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>Raining Data ePharma</title>\r\n");
      out.write("<META HTTP-EQUIV=\"PRAGMA\" CONTENT=\"NO-CACHE\">\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\r\n");
      out.write("<link href=\"/ePharma/assets/epedigree1.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
      out.write("if( window != window.top ){\r\n");
      out.write("  window.top.location = window.location;\r\n");
      out.write(" }\r\n");
      out.write("<!--\r\n");
      out.write("function MM_goToURL() { //v3.0\r\n");
      out.write("  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;\r\n");
      out.write("  for (i=0; i<(args.length-1); i+=2) eval(args[i]+\".location='\"+args[i+1]+\"'\");\r\n");
      out.write("}\r\n");
      out.write("//-->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("if( window != window.top ){\r\n");
      out.write("  window.top.location = window.location;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write(" \r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body >\r\n");
      out.write("\r\n");
      out.write("<!-- Top Header -->\r\n");
      out.write("<div id=\"bg\">\r\n");
      out.write("\t<div class=\"roleIcon-prePack\">&nbsp;</div>\r\n");
      out.write("  <div class=\"logo\"><img src=\"/ePharma/assets/images/logos_combined.jpg\"></div>\r\n");
      out.write("</div>\r\n");
      out.write("  \r\n");
      out.write("<!-- Left channel -->\r\n");
      out.write("<div id=\"leftgray_home\">\r\n");
      out.write("  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("    <tr> \r\n");
      out.write("      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Welcome</td>\r\n");
      out.write("    </tr>\r\n");
      out.write("    <tr> \r\n");
      out.write("      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\">&nbsp;</td>\r\n");
      out.write("      <td valign=\"top\" class=\"td-left\"></td>\r\n");
      out.write("    </tr> \r\n");
      out.write("    <tr valign=\"bottom\"> \r\n");
      out.write("      <td height=\"80\" colspan=\"2\" class=\"td-left\"><p>&nbsp;</p>\r\n");
      out.write("        <p>&nbsp;</p>\r\n");
      out.write("        <p>&nbsp;</p>\r\n");
      out.write("        <p>&nbsp;</p>\r\n");
      out.write("        <p>&nbsp;</p>\r\n");
      out.write("        <p><img src=\"/ePharma/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></p>\r\n");
      out.write("      </td>\r\n");
      out.write("    </tr>\r\n");
      out.write("  </table>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<div id=\"rightwhite_home\">\r\n");
      out.write("  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("    <tr> \r\n");
      out.write("      <td width=\"1%\" valign=\"middle\" class=\"td-rightmenu\"><img src=\"/ePharma/assets/images/space.gif\" width=\"10\" height=\"10\"></td>\r\n");
      out.write("\t  \r\n");
      out.write("\t  <!-- Messaging -->\r\n");
      out.write("      <td width=\"99%\" valign=\"middle\" class=\"td-rightmenu\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("          <tr> \r\n");
      out.write("            <td align=\"left\"></td>\r\n");
      out.write("            <td align=\"right\"></td>\r\n");
      out.write("\t\t\t\r\n");
      out.write("          </tr>\r\n");
      out.write("        </table></td>\r\n");
      out.write("    </tr>\r\n");
      out.write("    <tr> \r\n");
      out.write("      <td>&nbsp;</td>\r\n");
      out.write("      <form action=\"/ePharma/doUserLogin.do\" method=post>\r\n");
      out.write("      <td><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("          <tr> \r\n");
      out.write("            <td><img src=\"/ePharma/assets/images/space.gif\" width=\"30\" height=\"12\"></td>\r\n");
      out.write("            <td rowspan=\"2\">&nbsp;</td>\r\n");
      out.write("          </tr>\r\n");
      out.write("          <tr> \r\n");
      out.write("            <td class=\"td-typeblack\">Login</td>\r\n");
      out.write("            <td align=\"right\" class=\"td-typegray\"></td>\r\n");
      out.write("          </tr>\r\n");
      out.write("\t\t  <tr>\r\n");
      out.write("\t\t  \t<td align=\"left\" class=\"td-menu\">User Name: <input name=\"uname\" type=\"text\" size=\"30\">&nbsp;&nbsp;&nbsp;&nbsp;Password: <input name=\"password\" type=\"password\" size=\"30\">&nbsp;&nbsp;&nbsp;&nbsp;<input name=\"submit\" type=\"submit\" class=\"fButton\" value=\"Login\"></td>\r\n");
      out.write("\t\t  </tr>\r\n");
      out.write("        </table></td>\r\n");
      out.write("\t\t<td>&nbsp;</td>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("    </tr>\r\n");
      out.write("  </table>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<div id=\"footer_home\"> \r\n");
      out.write("  <table width=\"100%\" height=\"24\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("    <tr> \r\n");
      out.write("      <td height=\"24\" bgcolor=\"D0D0FF\" class=\"td-menu\">&nbsp;&nbsp;Copyright 2005. \r\n");
      out.write("        Raining Data.</td>\r\n");
      out.write("    </tr>\r\n");
      out.write("  </table>\r\n");
      out.write("</div>\r\n");
      out.write("</body>\r\n");
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

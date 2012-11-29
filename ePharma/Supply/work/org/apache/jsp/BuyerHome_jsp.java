package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class BuyerHome_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/BuyerNav.jsp");
  }

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

      out.write("\n");
      out.write("<html>\n");
      out.write("  <body bgcolor=\"white\">\n");
      out.write("\t");
      out.write("<table border=0 cellspacing=0 cellpadding=0 width=100% height=\"20%\">\n");
      out.write("<tr>\n");
      out.write("  <td align=right nowrap><font size=3>\n");
      out.write("  <a href=\"signIn\">logoff</a>|\n");
      out.write("  <a href=\"BuyerReport\">Reports</a>|\n");
      out.write("  <a href=\"BuyerHome\">Home</a>\n");
      out.write("</font></td></tr></table>\n");
      out.write("\n");
      out.write("\n");
      out.write("    <table align=\"center\" width=50% border=2>\n");
      out.write("       \n");
      out.write("          <th colspan=2><font size=6>Buyer Home Page</font> </th>\n");
      out.write("      \n");
      out.write("       \n");
      out.write("      <tr><td align=\"center\"><a href=\"POGenerate\"><font size=5>GeneratePO</font></a></td></tr>\n");
      out.write("      <tr><td align=\"center\"><a href=\"POSubmit\"><font size=5>SubmitPO</a></font></td></tr>\n");
      out.write("           \n");
      out.write("    </table>\n");
      out.write("  </body>\n");
      out.write("\n");
      out.write("</html>\n");
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

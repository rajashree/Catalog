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

      out.write("<html>\n");
      out.write("<script language=\"javascript\" src=\"myvalidate.js\">\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("<body bgcolor=\"white\">\n");
      out.write("<form action=\"Login\" method=\"post\" name=\"myform\">\n");
      out.write("<table align=\"center\" ><tr><td><font size=6>Supply Chain USE-CASE</font></td></tr></table>\n");
      out.write("  <table border=2 align=\"center\" width=\"50%\" ><br><br>\n");
      out.write("    <th colspan=2 align=\"center\"><h3>User Login Page</h3></th>\n");
      out.write("   <tr>\n");
      out.write("   <td> UserName:</td><td> <input type=\"text\" name=\"userName\" size=\"8\"></td></tr>\n");
      out.write("   <tr>\n");
      out.write("   <td> PassWord:</td><td> <input type=\"password\" name=\"password\" size=\"8\"></td></tr>\n");
      out.write("   <tr><td>Type:</td><td>\n");
      out.write("      <select name=\"categery\">\n");
      out.write("       <option value=buyer>Buyer</option>\n");
      out.write("       <option value=seller>Seller</option>\n");
      out.write("      </select></td>\n");
      out.write("    </tr>\n");
      out.write("   <tr><td><input type=\"hidden\" name=\"choice\" value=1></td></tr>\n");
      out.write("   <tr><td colspan=2 align=center><input type=\"button\" value=\"LOGIN\" onclick=\"validate2()\"></td></tr>\n");
      out.write("  </table>\n");
      out.write("</form>\n");
      out.write("</body>\n");
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

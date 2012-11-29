package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class POGenerate_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n");
      out.write("<script language=\"javascript\" src=\"myvalidate.js\">\n");
      out.write("</script>\n");
      out.write("<body bgcolor=\"white\">\n");
      out.write("<table border=0 cellspacing=0 cellpadding=0 width=100% height=\"20%\">\n");
      out.write("<tr>\n");
      out.write("  <td align=right nowrap><font size=3>\n");
      out.write("  <a href=\"signIn\">logoff</a>|\n");
      out.write("  <a href=\"BuyerReport\">Reports</a>|\n");
      out.write("  <a href=\"BuyerHome\">Home</a>\n");
      out.write("</font></td></tr></table>\n");
      out.write("\n");
      out.write("<form action=\"Login\" method=\"post\" name=\"myform\">\n");
      out.write("<table align=\"center\" border=1  width=50%>\n");
      out.write("  <th colspan=2> <font size=6>  Purchase Order Information</font></th>\n");
      out.write("      \n");
      out.write("     <tr>\n");
      out.write("   <td>UserId:</td>\n");
      out.write("   <td><input type=\"text\" name=\"userId\" size=10></td>\n");
      out.write("  </tr>  \n");
      out.write("   \n");
      out.write("   <tr>\n");
      out.write("   <td>MailId:</td>\n");
      out.write("   <td><input type=\"text\" name=\"mailId\" size=10 onBlur='lose(mailId)'></td>\n");
      out.write("  </tr> \n");
      out.write("  \n");
      out.write("      \n");
      out.write("  <tr>\n");
      out.write("   <td>OrderId:</td>\n");
      out.write("   <td><input type=\"text\" name=\"orderId\" size=10></td>\n");
      out.write("  </tr> \n");
      out.write("  \n");
      out.write("  <tr>\n");
      out.write("   <td>OrderDescription:</td>\n");
      out.write("   <td><input type=\"text\" name=\"orderDescription\" size=10></td>\n");
      out.write("  </tr> \n");
      out.write("\n");
      out.write(" <tr>\n");
      out.write("   <td>DateofPurchase:</td>\n");
      out.write("   <td><input type=\"text\" name=\"dateofPurchase\" size=10></td>\n");
      out.write("  </tr> \n");
      out.write("\n");
      out.write("  <tr>\n");
      out.write("   <td>ItemId:</td>\n");
      out.write("   <td><input type=\"text\" name=\"itemId\" size=10></td>\n");
      out.write("  </tr> \n");
      out.write("\n");
      out.write("\n");
      out.write("  <tr>\n");
      out.write("   <td>ItemDescription:</td>\n");
      out.write("   <td><input type=\"text\" name=\"itemDescription\" size=10></td>\n");
      out.write("  </tr> \n");
      out.write("  <tr>\n");
      out.write("   <td>ItemQuantity:</td>\n");
      out.write("   <td><input type=\"text\" name=\"itemQuantity\" size=10 onBlur='checknum(itemQuantity)'></td>\n");
      out.write("  </tr> \n");
      out.write("  \n");
      out.write("  <tr>\n");
      out.write("   <td>ItemPrice:</td>\n");
      out.write("   <td><input type=\"text\" name=\"itemPrice\" size=10 onBlur='checknum(itemPrice)'></td>\n");
      out.write("  </tr> \n");
      out.write("\n");
      out.write("  <tr><td><input type=\"hidden\" name=\"choice\" value=3></td></tr>\n");
      out.write("  <tr><td> <input type=\"button\" value=\"Generate\" onClick=\"validate2()\"></td>\n");
      out.write("    <td> <input type=\"reset\" value=\"Reset\"></td></tr>\n");
      out.write("\n");
      out.write("</table>\n");
      out.write("</form>\n");
      out.write("<body>\n");
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

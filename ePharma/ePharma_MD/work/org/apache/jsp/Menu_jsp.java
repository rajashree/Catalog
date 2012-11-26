package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class Menu_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title>EAG-EPCIS Administration Console</title>\r\n");
      out.write("<style>\r\n");
      out.write(" \r\n");
      out.write("\ta, A:link, a:visited, a:active, A:hover\r\n");
      out.write("\t\t{color: #000000; text-decoration: none; font-family: Tahoma, Verdana; font-size: 12px}\r\n");
      out.write("</style>\r\n");
      out.write("\r\n");
      out.write("<link href=\"assets/epedigree_edited.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"><style type=\"text/css\">\r\n");
      out.write("<!--\r\n");
      out.write("body {\r\n");
      out.write("\tbackground-color: #eeeeee;\r\n");
      out.write("}\r\n");
      out.write("-->\r\n");
      out.write("</style></head>\r\n");
      out.write("\r\n");
      out.write("<body bottommargin=\"0\" topmargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" marginheight=\"0\" marginwidth=\"0\">\r\n");
      out.write("<script language=\"JavaScript\" src=\"js/tree.js\"></script>\r\n");
      out.write("<script language=\"JavaScript\" src=\"js/tree_tpl.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"7\" cellspacing=\"1\" bgcolor=\"#CCCCCC\">\r\n");
      out.write("  <tr>\r\n");
      out.write("    <td height=\"28\" class=\"td-mapCatalogTreeTables\"><div align=\"left\">Admin Console</div></td></tr>\r\n");
      out.write("<!--\r\n");
      out.write("For Groups and Users\r\n");
      out.write("\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td align=\"left\" valign=\"top\">\r\n");
      out.write("\t<script language=\"JavaScript\">\r\n");
      out.write("\t\r\n");
      out.write("\tvar TREE_ITEMS = [\r\n");
      out.write("\t\t['Resources', './dist/admin/Introduction.html',\r\n");
      out.write("\t\t\t\t['Users', './dist/admin/Introduction.html',\r\n");
      out.write("\t\t\t\t\t['Add', 'AddUser.do?exec=AddNewUser&module=ADMIN_USERS&action=Insert'],\r\n");
      out.write("\t\t\t\t\t['Edit', 'AddUser.do?exec=EditUserList&module=ADMIN_USERS'],\r\n");
      out.write("\t\t\t\t]\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t\t,\r\n");
      out.write("\t\t\t\t ['Groups', './dist/admin/Introduction.html',\r\n");
      out.write("\t\t\t\t\t['Add', 'AddGroup.do?exec=AddNewGroup&module=ADMIN_GROUPS&action=Insert'],\r\n");
      out.write("\t\t\t\t\t['Edit', 'AddGroup.do?exec=EditGroupList&module=ADMIN_GROUPS'],\r\n");
      out.write("\t\t\t\t],\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t],\r\n");
      out.write("];\r\n");
      out.write("\r\n");
      out.write("\t\tnew tree (TREE_ITEMS, TREE_TPL);\r\n");
      out.write("\r\n");
      out.write("\t</script>\t</td>\r\n");
      out.write("</tr>-->\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td align=\"left\" valign=\"top\">\r\n");
      out.write("\t<script language=\"JavaScript\">\r\n");
      out.write("\t\r\n");
      out.write("\tvar TREE_ITEMS = [\r\n");
      out.write("\t\t['Resources', './dist/admin/Introduction.html',\r\n");
      out.write("\t\t\t\t['Users', './dist/admin/Introduction.html',\r\n");
      out.write("\t\t\t\t\t['Add', 'AddUser.do?exec=AddNewUser&module=ADMIN_USERS&action=Insert'],\r\n");
      out.write("\t\t\t\t\t['Edit', 'AddUser.do?exec=EditUserList&module=ADMIN_USERS'],\r\n");
      out.write("\t\t\t\t]\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t],\r\n");
      out.write("];\r\n");
      out.write("\r\n");
      out.write("\t\tnew tree (TREE_ITEMS, TREE_TPL);\r\n");
      out.write("\r\n");
      out.write("\t</script>\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("  <tr>\r\n");
      out.write("    <td class=\"tableRow_Off\"><div align=\"center\"><IMG src=\"assets/images/logo_poweredby.gif\"/> </div></td>\r\n");
      out.write("\t\r\n");
      out.write("  </tr>\r\n");
      out.write("</table>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write("\r\n");
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

package org.apache.jsp.dist.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class AdminMenu_jsp extends org.apache.jasper.runtime.HttpJspBase
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
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = (String)request.getAttribute("pagenm");
String tp_company_nm = (String)request.getParameter("tp_company_nm");
System.out.println("tp_company_nm: "+tp_company_nm);
System.out.println("sessionID: "+sessionID);


      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Frameset//EN\">\r\n");
      out.write("<html>\r\n");
      out.write("\t<head>\r\n");
      out.write("\t\t<TITLE>ePharma - Admin Console</TITLE>\r\n");
      out.write("\t</head>\r\n");
      out.write("\t<frameset rows=\"73,*\" border=\"0\">\r\n");
      out.write("\t\t<frame name=\"search\" scrolling=\"no\" src=\"/ePharma/dist/admin/LoadAdminmenu.jsp?pagenm=epcadmin&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("\" noresize=\"noresize\">\t\t\t\t\r\n");
      out.write("\t\t<frame name=\"resultsFRM\" scrolling=\"auto\" src=\"./Main.jsp?sessionID=");
      out.print(sessionID);
      out.write("\" noresize=\"noresize\">\t\t\r\n");
      out.write("\t</frameset>\r\n");
      out.write("\t\r\n");
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

package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class Buyer_jsp extends org.apache.jasper.runtime.HttpJspBase
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

 
    if(session.getAttribute("type")==null)
    {
	    response.sendRedirect("Failure.jsp");
	}
	else
	{
		if(session.getAttribute("type").equals("seller"))
	    {
	    	session.setAttribute("status", "Access Violation for Buyer");
	    	response.sendRedirect("Failure.jsp");    	
	    }
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n");
      out.write("<HTML>\r\n");
      out.write("<HEAD>\r\n");
      out.write("<TITLE> Welcome </TITLE>\r\n");
      out.write("<STYLE type=text/css>\r\n");
      out.write("\ta { text-decoration: none }\r\n");
      out.write("</STYLE>\r\n");
      out.write("<link rel=\"stylesheet\" href=\"css\\Buttons.css\" type=\"text/css\">\r\n");
      out.write("</HEAD>\r\n");
      out.write("<BODY>\r\n");
      out.write("\r\n");
      out.write("<br><br><br><br><br><br><br><br><br>\r\n");
      out.write("\r\n");
      out.write("<TABLE cellSpacing=0 cellPadding=0 width=\"50%\" align=center border=0 bgcolor=\"white\" background=\"\">\r\n");
      out.write("  <TR><TD vAlign=center align=right>\r\n");
      out.write("  \t  <IMG id=IMG3 alt=\"\" src=\"Images\\blue_right_arrow.gif\">&nbsp;\r\n");
      out.write("  \t  <STRONG><A href=\"SignOut.jsp\"><FONT color=navy>Sign Out\r\n");
      out.write("      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("\r\n");
      out.write("<TABLE WIDTH=\"50%\" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1>\r\n");
      out.write("<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face=\"Bodoni MT\" \r\n");
      out.write("      color=lightblue size=5><STRONG> Buyer \r\n");
      out.write("    Home</STRONG></FONT></TD></TR>\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD><TABLE WIDTH=\"100%\" ALIGN=center BORDER=0 CELLSPACING=0 CELLPADDING=0>\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD align=middle valign=center><IMG id=IMG1  alt=\"\" src=\"Images\\Buyer.jpg\"  ></TD>\r\n");
      out.write("\t\t<TD align=middle valign=center>\r\n");
      out.write("\t\t<TABLE WIDTH=\"100%\" BORDERCOLOR=slategray ALIGN=center BORDER=0 CELLSPACING=0 CELLPADDING=0 \r\n");
      out.write("           >\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD><form id=\"POGeneration\" name=\"POGeneration\" action=\"POGeneration.jsp\">\r\n");
      out.write("<input type=\"submit\" value=\"Generate PO\" class=\"btn\" style=\"WIDTH: 110px; HEIGHT: 26px\" size=26>\r\n");
      out.write("</form>\r\n");
      out.write("</TD>\r\n");
      out.write("\t</TR>\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD><form id=\"POSubmit\" name=\"POSubmit\" action=\"POSubmit.jsp\">\r\n");
      out.write("<input type=\"submit\" value=\"Submit PO\" class=\"btn\" style=\"WIDTH: 110px; HEIGHT: 26px\" size=26>\r\n");
      out.write("</form>\r\n");
      out.write("</TD>\r\n");
      out.write("\t</TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("\t\t\r\n");
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

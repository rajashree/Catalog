package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class POGeneration_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>:::</title>\r\n");
      out.write("<link rel=\"stylesheet\" href=\"css\\Common.css\" type=\"text/css\">\r\n");
      out.write("<script type=\"text/javascript\" src=\"js/calendarDateInput.js\"></script>\r\n");
      out.write("<SCRIPT>\r\n");
      out.write("\tfunction valid_numbers(f)\r\n");
      out.write("\t{\r\n");
      out.write("\t\tif (!/^\\d*$/.test(f.value))\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert(\"Only integer numbers allowed!\");\r\n");
      out.write("\t\t\tf.value = f.value.replace(/[^\\d]/g,\"\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\tfunction validate()\r\n");
      out.write("\t{\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tuserid=POGeneration.UserID.value;\r\n");
      out.write("\t\tif (userid=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('UserId should not be empty');\r\n");
      out.write("\t\t\tdocument.POGeneration.UserID.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\torderid=POGeneration.OrderID.value;\r\n");
      out.write("\t\tif (orderid=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('OrderID should not be empty');\r\n");
      out.write("\t\t\tdocument.POGeneration.OrderID.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\titemid=POGeneration.ItemID.value;\r\n");
      out.write("\t\tif (itemid=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('ItemID should not be empty');\r\n");
      out.write("\t\t\tdocument.POGeneration.ItemID.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\r\n");
      out.write("\r\n");
      out.write("\t\tqty=POGeneration.ItemQty.value;\r\n");
      out.write("\t\tif (qty=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('ItemQty should not be empty');\r\n");
      out.write("\t\t\tdocument.POGeneration.ItemQty.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tprice=POGeneration.ItemPrice.value;\r\n");
      out.write("\t\tif (price=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('ItemPrice should not be empty');\r\n");
      out.write("\t\t\tdocument.POGeneration.ItemPrice.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar mdiv = document.getElementById(\"DateOfPurchase\");\r\n");
      out.write("\t\tmdiv.value = POGeneration.orderdate1.value;\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tmailid=POGeneration.MailID.value;\r\n");
      out.write("\t\tif (mailid=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\tdocument.POGeneration.MailID.value=\"[None]\";\t\t\t\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\torderdesc=POGeneration.OrderDescription.value;\r\n");
      out.write("\t\tif (orderdesc=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\tdocument.POGeneration.OrderDescription.value=\"[None]\";\t\t\t\r\n");
      out.write("\t\t}\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t\titemdesc=POGeneration.ItemDescription.value;\r\n");
      out.write("\t\tif (itemdesc=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\tdocument.POGeneration.ItemDescription.value=\"[None]\";\t\t\t\r\n");
      out.write("\t\t}\t\r\n");
      out.write("\t}\r\n");
      out.write("</SCRIPT>\r\n");
      out.write("<STYLE type=text/css>\r\n");
      out.write("\ta { text-decoration: none }\r\n");
      out.write("</STYLE>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<br><br>\r\n");
      out.write("<TABLE cellSpacing=0 cellPadding=0 width=\"50%\" align=center border=0 bgcolor=\"white\" background=\"\">\r\n");
      out.write("  <TR><TD vAlign=center align=right>\r\n");
      out.write("  \t  <IMG id=IMG3 alt=\"\" src=\"Images\\blue_right_arrow.gif\">&nbsp;\r\n");
      out.write("  \t  <STRONG><A href=\"Buyer.jsp\"><FONT color=navy>Buyer Home\r\n");
      out.write("      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("<TABLE WIDTH=\"40%\" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 >\r\n");
      out.write("<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face=\"Bodoni MT\" color=lightblue size=5>\r\n");
      out.write("<STRONG> Purchase Order Generation</STRONG></FONT></TD></TR>\r\n");
      out.write("\t\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD><TABLE WIDTH=\"50%\" ALIGN=right BORDER=0 CELLSPACING=0 CELLPADDING=0 style=\"WIDTH: 75%\">\r\n");
      out.write("\t<TR>\r\n");
      out.write("\t\t<TD align=left width=\"50%\">\r\n");
      out.write("\t\t\t<form id=\"POGeneration\" name=\"POGeneration\" onsubmit=\"return validate(this);\" action=\"./ControlServlet\" method=post>\r\n");
      out.write("\t\t\t<input type=\"hidden\" value=\"POGeneration\" id=\"controller\" name=\"controller\">\r\n");
      out.write("\t\t\t<table  align=left id=TABLE1 style=\"WIDTH: 50%; HEIGHT: 324px\" width=\"50%\">\r\n");
      out.write("\t\t\t<tr><td nowrap class=\"Smallfont\">fields marked with an asterisk <span class=\"Era_asterisk\">*</span> are required.</td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\"><span class=\"Era_asterisk\">*</span>&nbsp;UserID</td><td width=\"50%\"><input name='UserID' id='UserID' width=\"20\" onkeyup=\"valid_numbers(this)\"></td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\">&nbsp;&nbsp;&nbsp;MailID</td><td width=\"50%\"><input name='MailID' id='MailID' width=\"20\" ></td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\"><span class=\"Era_asterisk\">*</span>&nbsp;OrderID</td><td width=\"50%\"><input name='OrderID' id='OrderID' width=\"20\" ></td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\">&nbsp;&nbsp;&nbsp;OrderDescription</td><td width=\"50%\"><input name='OrderDescription' id='OrderDescription' width=\"20\"></td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\"><span class=\"Era_asterisk\">*</span>&nbsp;DateOfPurchase</td><td width=\"50%\"><input type=\"hidden\"name='DateOfPurchase' id='DateOfPurchase' width=\"20\" >\r\n");
      out.write("\t\t\t\t<script>DateInput('orderdate1', true, 'YYYY-MM-DD')</script>\t\t\t\t\r\n");
      out.write("\t\t\t</td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\"><span class=\"Era_asterisk\">*</span>&nbsp;ItemID</td><td width=\"50%\"><input name='ItemID' id='ItemID' width=\"20\" ></td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\">&nbsp;&nbsp;&nbsp;ItemDescription</td><td width=\"50%\"><input name='ItemDescription' id='ItemDescription' width=\"20\" ></td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\"><span class=\"Era_asterisk\">*</span>&nbsp;ItemQty</td><td width=\"50%\"><input name='ItemQty' id='ItemQty' width=\"20\" onkeyup=\"valid_numbers(this)\"></td></tr>\r\n");
      out.write("\t\t\t<tr><td class=\"fields\" width=\"50%\"><span class=\"Era_asterisk\">*</span>&nbsp;ItemPrice</td><td width=\"50%\"><input name='ItemPrice' id='ItemPrice' width=\"20\" ></td></tr>\r\n");
      out.write("\t\t\t<tr><td colspan=2 bgcolor=black></td></tr>\r\n");
      out.write("\t\t\t<tr><td colspan=2 align=middle><input type=submit value=\"Generate\" id=\"Generate\" name=\"Generate\">&nbsp;&nbsp;<input type=reset value=\"Clear\" id=\"Clear\" name=\"Clear\"></td></tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t\t</form>\t\t\r\n");
      out.write("\t\t</TD>\r\n");
      out.write("\t\t<TD width=\"50%\"><IMG style=\"WIDTH: 113px; HEIGHT: 264px\" height=114 src=\"Images\\POGeneration.jpg\" width=113 ></TD>\t\t\r\n");
      out.write("\t</TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("</TD>\r\n");
      out.write("</TR>\r\n");
      out.write("</TABLE>\t\r\n");
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

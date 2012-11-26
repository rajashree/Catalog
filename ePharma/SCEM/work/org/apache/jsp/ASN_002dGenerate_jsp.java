package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class ASN_002dGenerate_jsp extends org.apache.jasper.runtime.HttpJspBase
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
		if(session.getAttribute("type").equals("buyer"))
	    {
	    	session.setAttribute("status", "Access Violation for Seller");
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
      out.write("\t{\t\t\r\n");
      out.write("\t\tconsignid=ASN_Generate.ConsignmentID.value;\r\n");
      out.write("\t\tif (consignid=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('ConsignmentID should not be empty');\r\n");
      out.write("\t\t\tdocument.ASN_Generate.ConsignmentID.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar mdiv = document.getElementById(\"ShipmentDate\");\r\n");
      out.write("\t\tmdiv.value = ASN_Generate.orderdate1.value;\t\t\t\r\n");
      out.write("\r\n");
      out.write("\t\tqty=ASN_Generate.Rx-ItemQty.value;\r\n");
      out.write("\t\tif (qty=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('ItemQty should not be empty');\r\n");
      out.write("\t\t\tdocument.ASN_Generate.Rx-ItemQty.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tprice=ASN_Generate.Rx-ItemPrice.value;\r\n");
      out.write("\t\tif (price=='')\r\n");
      out.write("\t\t{\r\n");
      out.write("\t\t\talert('ItemPrice should not be empty');\r\n");
      out.write("\t\t\tdocument.ASN_Generate.Rx-ItemPrice.focus();\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t}\t\r\n");
      out.write("\t}\r\n");
      out.write("</SCRIPT>\r\n");
      out.write("<STYLE type=text/css>\r\n");
      out.write("\ta { text-decoration: none }\r\n");
      out.write("</STYLE>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<br><br>\r\n");
      out.write("<TABLE cellSpacing=0 cellPadding=0 width=\"60%\" align=center border=0 bgcolor=\"white\" background=\"\">\r\n");
      out.write("  <TR><TD vAlign=center align=right>\r\n");
      out.write("  \t  <IMG id=IMG3 alt=\"\" src=\"Images\\blue_right_arrow.gif\">&nbsp;\r\n");
      out.write("  \t  <STRONG><A href=\"Seller.jsp\"><FONT color=navy>Seller Home\r\n");
      out.write("      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("<form id=ASN_Generate name=ASN_Generate onsubmit=\"return validate(this);\" action=\"./ControlServlet\" method='post' >\r\n");
      out.write("<input type='hidden' value='ASN-Generate' id='controller' name='controller'>\r\n");
      out.write("<TABLE WIDTH=\"60%\" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 style=\"WIDTH: 60%\">\r\n");
      out.write("<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face=\"Bodoni MT\" color=white size=5>\r\n");
      out.write("<STRONG> ASN Generation</STRONG></FONT></TD>\r\n");
      out.write("</TR>\r\n");
      out.write("<TR><TD>\r\n");
      out.write(" <TABLE cellSpacing=1 cellPadding=1 width=\"60%\" align=center border=1 style=\"BORDER-LEFT-COLOR: purple; BORDER-BOTTOM-COLOR: purple; WIDTH: 60%; BORDER-TOP-COLOR: purple; BORDER-RIGHT-COLOR: purple\" \r\n");
      out.write("      id=TABLE1 >                    \r\n");
      out.write(" <TR>\r\n");
      out.write("  <TD><FONT face=Verdana color=darkgreen><STRONG>Information Provided by Buyer</STRONG></FONT><BR>\r\n");
      out.write("    <TABLE cellSpacing=2 cellPadding=2 width=\"100%\" align=center border=0>\r\n");
      out.write("      <TR><TD bgColor=gray colSpan=4></TD></TR>                  \r\n");
      out.write("      <TR>\r\n");
      out.write("        <TD class=fields width=\"50%\"><FONT \r\n");
      out.write("          color=#f68622>&nbsp; </FONT>&nbsp;UserID</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=UserID readOnly \r\n");
      out.write("          name=UserID width=\"20\" value=");
out.print(request.getParameter("UserID"));
      out.write("></TD>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;MailID</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=MailID name=MailID \r\n");
      out.write("          width=\"20\" readOnly value=");
out.print(request.getParameter("MailID"));
      out.write("></TD></TR>\r\n");
      out.write("      <TR>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;OrderID</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=OrderID name=OrderID \r\n");
      out.write("          width=\"20\" readOnly value=");
 out.print(request.getParameter("OrderID")); 
      out.write("></TD>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;OrderDescription</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=OrderDescription \r\n");
      out.write("          name=OrderDescription width=\"20\" readOnly value=");
 out.print(request.getParameter("OrderDescription")); 
      out.write("></TD></TR>\r\n");
      out.write("      <TR>\r\n");
      out.write("        <TD></TD>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;DateOfPurchase</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=DateOfPurchase \r\n");
      out.write("          name=DateOfPurchase width=\"20\" readOnly value=");
 out.print(request.getParameter("DateOfPurchase")); 
      out.write("></TD>\r\n");
      out.write("        <TD></TD></TR>\r\n");
      out.write("      <TR>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;ItemID</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=ItemID name=ItemID \r\n");
      out.write("          width=\"20\" readOnly value=");
out.print( request.getParameter("ItemID")); 
      out.write("></TD>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;ItemDescription</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=ItemDescription \r\n");
      out.write("          name=ItemDescription width=\"20\" readOnly value=");
 out.print(request.getParameter("ItemDescription")); 
      out.write("></TD></TR>\r\n");
      out.write("      <TR>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;ItemQty</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=ItemQty \r\n");
      out.write("          onkeyup=valid_numbers(this) name=ItemQty \r\n");
      out.write("          width=\"20\" readOnly value=");
 out.print(request.getParameter("ItemQty")); 
      out.write("></TD>\r\n");
      out.write("        <TD class=fields \r\n");
      out.write("          width=\"50%\">&nbsp;&nbsp;&nbsp;ItemPrice</TD>\r\n");
      out.write("        <TD width=\"50%\"><INPUT id=ItemPrice name=ItemPrice \r\n");
      out.write("          width=\"20\" readOnly value=");
 out.print(request.getParameter("ItemPrice")); 
      out.write("></TD></TR>     \r\n");
      out.write("      \r\n");
      out.write("\t</TABLE>\r\n");
      out.write("\t</TD></TR>\r\n");
      out.write("\t<TR><TD><FONT face=Verdana color=black><STRONG>Enter Information to Generate ASN</STRONG></FONT>\r\n");
      out.write("\t<TABLE style=\"WIDTH: 100%\" cellSpacing=1 cellPadding=1 width=\"80%\" align=center border=0>                    \r\n");
      out.write("\t\t<TR><TD bgColor=gray colSpan=2></TD></TR>\r\n");
      out.write("\t\t<TR>\r\n");
      out.write("\t\t  <TD class=fields width=\"50%\"><SPAN \r\n");
      out.write("\t\t    class=Era_asterisk>*</SPAN>&nbsp;ConsignmentID</TD>\r\n");
      out.write("\t\t  <TD width=\"50%\"><INPUT id=ConsignmentID name=ConsignmentID \r\n");
      out.write("\t\t  width=\"20\"></TD></TR>\r\n");
      out.write("\t\t<TR>\r\n");
      out.write("\t\t  <TD class=fields width=\"50%\"><SPAN \r\n");
      out.write("\t\t    class=Era_asterisk>*</SPAN>&nbsp;ShipmentDate</TD>\r\n");
      out.write("\t\t  <TD width=\"50%\"><INPUT id=ShipmentDate type=hidden \r\n");
      out.write("\t\t    name=ShipmentDate width=\"20\">\r\n");
      out.write("\t\t    <script>DateInput('orderdate1', true, 'YYYY-MM-DD')</script>\r\n");
      out.write("\t\t  </TD></TR>\r\n");
      out.write("\t\t<TR>\r\n");
      out.write("\t\t  <TD class=fields width=\"50%\"><SPAN \r\n");
      out.write("\t\t    class=Era_asterisk>*</SPAN>&nbsp;ItemQty</TD>\r\n");
      out.write("\t\t  <TD width=\"50%\"><INPUT id=Rx-ItemQty  name=Rx-ItemQty \r\n");
      out.write("\t\twidth=\"20\" value=");
 out.print(request.getParameter("ItemQty")); 
      out.write("></TD></TR>\r\n");
      out.write("\t\t<TR>\r\n");
      out.write("\t\t  <TD class=fields width=\"50%\"><SPAN \r\n");
      out.write("\t\t    class=Era_asterisk>*</SPAN>&nbsp;ItemPrice</TD>\r\n");
      out.write("\t\t  <TD width=\"50%\"><INPUT id=Rx-ItemPrice name=Rx-ItemPrice \r\n");
      out.write("\t\t    width=\"20\" value=");
 out.print(request.getParameter("ItemPrice")); 
      out.write("></TD></TR>\r\n");
      out.write("\t\t<TR><TD bgColor=gray colSpan=2></TD></TR>\r\n");
      out.write("\t\t<TR><TD bgColor=gray colSpan=2></TD></TR>\r\n");
      out.write("\t\t <TR><TD align=middle colspan=2><input type=submit value=\"Generate ASN\" id=\"Generate\" name=\"Generate\">&nbsp;&nbsp;<input type=reset value=\"Clear\" id=\"Clear\" name=\"Clear\"></TD></TR>\r\n");
      out.write("\t</TABLE>\r\n");
      out.write("\t</TD></TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("\r\n");
      out.write("</TD></TR>\r\n");
      out.write("</TABLE></body></form></HTML>\r\n");
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

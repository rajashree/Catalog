package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.rdta.tlapi.xql.*;
import java.util.*;
import java.io.*;

public final class ASNReconciliation_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<STYLE type=text/css>\r\n");
      out.write("\ta { text-decoration: none }\r\n");
      out.write("</STYLE>\r\n");
      out.write("<script>\r\n");
      out.write("\tvar req;\t\r\n");
      out.write("\tfunction showRecord()\r\n");
      out.write("\t{ \r\n");
      out.write("    \t   document.getElementById(\"consolidationMessage\").innerHTML=\"<img src='Images/Process.gif'>\";\r\n");
      out.write("\t   \t   var idField = document.myform.consolidation.options[document.myform.consolidation.selectedIndex].value;\r\n");
      out.write("\t   \t   if(document.myform.consolidation.selectedIndex==0)\r\n");
      out.write("\t   \t   {\r\n");
      out.write("\t   \t   \t\tdocument.getElementById(\"consolidationMessage\").innerHTML=\"<b>...</b>\";\r\n");
      out.write("\t   \t   }\r\n");
      out.write("\t   \t   else\r\n");
      out.write("\t   \t   {\r\n");
      out.write("\t\t\t   var url = \"./GetRecord?id=\"+encodeURIComponent(idField);\r\n");
      out.write("\t\t\t   if (typeof XMLHttpRequest != \"undefined\")\r\n");
      out.write("\t\t\t   {\r\n");
      out.write("\t\t\t       req = new XMLHttpRequest();\r\n");
      out.write("\t\t\t   }else if (window.ActiveXObject)\r\n");
      out.write("\t\t\t   {\r\n");
      out.write("\t\t\t       req = new ActiveXObject(\"Microsoft.XMLHTTP\");\r\n");
      out.write("\t\t\t   }\r\n");
      out.write("\t\t\t   req.open(\"GET\", url, true);\r\n");
      out.write("\t\t\t   req.onreadystatechange = callback;\r\n");
      out.write("\t\t\t   req.send(null);\t\t   \r\n");
      out.write("\t\t   }\r\n");
      out.write("\t}\t\r\n");
      out.write("\tfunction callback()\r\n");
      out.write("\t{\r\n");
      out.write("\t    if (req.readyState == 4)\r\n");
      out.write("\t    {\r\n");
      out.write("\t        if (req.status == 200)\r\n");
      out.write("\t        {\r\n");
      out.write("\t            document.getElementById(\"consolidationMessage\").innerHTML=req.responseText;\r\n");
      out.write("\t            \r\n");
      out.write("\t        }\r\n");
      out.write("\t    }\r\n");
      out.write("\t}\t\r\n");
      out.write("</script>\r\n");
      out.write("<link rel=\"stylesheet\" href=\"css\\Common.css\" type=\"text/css\">\r\n");
      out.write("</HEAD>\r\n");
      out.write("<body onload=\"showRecord()\">\r\n");
      out.write("<br><br>\r\n");
      out.write("<TABLE cellSpacing=0 cellPadding=0 width=\"60%\" align=center border=0 bgcolor=\"white\" background=\"\">\r\n");
      out.write("  <TR><TD vAlign=center align=right>\r\n");
      out.write("  \t  <IMG id=IMG3 alt=\"\" src=\"Images\\blue_right_arrow.gif\">&nbsp;\r\n");
      out.write("  \t  <STRONG><A href=\"Seller.jsp\"><FONT color=navy>Seller Home\r\n");
      out.write("      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("<form name=myform> \r\n");
      out.write("<TABLE WIDTH=\"60%\" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 >\r\n");
      out.write("<TR><TD vAlign=center colspan=2 align=middle bgcolor=midnightblue><FONT face=\"Bodoni MT\" color=white size=5>\r\n");
      out.write("<STRONG> ASN Reconsolation</STRONG></FONT></TD></TR>\r\n");
      out.write("<TR>\r\n");
      out.write("<TD class=fields width=\"50%\"><SPAN \r\n");
      out.write("\t\t    class=Era_asterisk>*</SPAN>&nbsp;Select PurchaseorderID</TD>\r\n");
      out.write("\t\t  <TD width=\"50%\">\r\n");
      out.write("<select name=\"consolidation\" onchange=\"showRecord()\">\r\n");
      out.write("<option>[Select OrderID]\r\n");
      out.write("\r\n");
		
		Connection conn = null;    
		Statement statement = null;
		ResultSet rs=null;  		
	    String userName = "admin";
	    String password = "admin";
	    String url = "xql:rdtaxql://localhost:3408";
	    String XQuery="let $x :=(for $i in collection('tig:///Seller/ASN')/ASNI return $i)";
	    XQuery=XQuery+"return data($x//OrderID)";
	  
		try
		{		
			DataSource dataSource = (DataSource) DataSourceFactory.getDataSource(url,null);
		   	conn = dataSource.getConnection(userName, password);
	   		statement = conn.createStatement();	   		         	   		
    		rs=statement.execute(XQuery);    	
    		while(rs.next())
    		{
    			out.println("<option value='"+rs.getString()+"'>"+rs.getString()+"");
		 	}	
		 }	 	
		catch(com.rdta.tlapi.xql.XQLConnectionException e)
		{
			session.setAttribute("status", e.getMessage());
    		response.sendRedirect("Failure.jsp");
		}
		catch(com.rdta.tlapi.xql.XQLException e)
		{
			session.setAttribute("status", e.getMessage());
    		response.sendRedirect("Failure.jsp");
		}	

      out.write("\r\n");
      out.write("</select>\r\n");
      out.write("</TD>\r\n");
      out.write("<TR>\r\n");
      out.write("<TD colspan=2 align=\"center\"><div id=\"consolidationMessage\"><b>...</b></div></TD>\r\n");
      out.write("</TR></TABLE>\r\n");
      out.write("</form>\r\n");
      out.write("</BODY>\r\n");
      out.write("</HTML>\r\n");
      out.write("</html>");
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

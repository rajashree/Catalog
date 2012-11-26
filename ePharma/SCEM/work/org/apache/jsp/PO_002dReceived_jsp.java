package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.rdta.tlapi.xql.*;
import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import com.rdta.util.io.StreamHelper;

public final class PO_002dReceived_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>:::</title>\r\n");
      out.write("<link rel=\"stylesheet\" href=\"css\\Common.css\" type=\"text/css\">\r\n");
      out.write("<STYLE type=text/css>\r\n");
      out.write("\ta { text-decoration: none }\r\n");
      out.write("</STYLE>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<br><br>\r\n");
      out.write("<TABLE cellSpacing=0 cellPadding=0 width=\"90%\" align=center border=0 bgcolor=\"white\" background=\"\">\r\n");
      out.write("  <TR><TD vAlign=center align=right>\r\n");
      out.write("  \t  <IMG id=IMG3 alt=\"\" src=\"Images\\blue_right_arrow.gif\">&nbsp;\r\n");
      out.write("  \t  <STRONG><A href=\"Seller.jsp\"><FONT color=navy>Seller Home\r\n");
      out.write("      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>\r\n");
      out.write("</TABLE>\r\n");
      out.write("<TABLE WIDTH=\"90%\" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 >\r\n");
      out.write("<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face=\"Bodoni MT\" color=white size=5>\r\n");
      out.write("<STRONG> Submit Purchase Order</STRONG></FONT></TD></TR>\r\n");
      out.write("<TR><TD>\r\n");
      out.write("\t<TABLE WIDTH=\"100%\" ALIGN=right BORDER=0 CELLSPACING=0 CELLPADDING=0 >\r\n");
      out.write("\t\t<TR bgcolor=lavenderblush class=tablefont1>\r\n");
      out.write("\t\t<TD>UserID</TD>\r\n");
      out.write("\t\t<TD>MailID</TD>\r\n");
      out.write("\t\t<TD>OrderID</TD>\r\n");
      out.write("\t\t<TD>OrderDescription</TD>\r\n");
      out.write("\t\t<TD>DateOfPurchase</TD>\r\n");
      out.write("\t\t<TD>ItemID</TD>\r\n");
      out.write("\t\t<TD>ItemDescription</TD>\r\n");
      out.write("\t\t<TD>ItemQty</TD>\r\n");
      out.write("\t\t<TD>ItemPrice</TD>\r\n");
      out.write("\t\t<TD></TD>\r\n");
      out.write("\t\t</TR>\r\n");
      out.write("\t\t<TR><TD colspan=10><font size=\"1\"></font></TD></TR>\r\n");
      out.write("\t\t<TR><TD colspan=10><form></form><font size=\"1\"></font></TD></TR>\r\n");
      out.write("\t");
		
		Connection conn = null;    
		Statement statement = null;
		ResultSet rs=null;  		
	    String userName = "admin";
	    String password = "admin";
	    String url = "xql:rdtaxql://localhost:3408";
	    String XQuery="let $x :=(for $i in collection('tig:///Seller/PO-RX')/POI return $i)";
	    XQuery=XQuery+"return $x";
		try
		{
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();		  
		     DocumentBuilder parser = null;
				try 
				{
					parser = factory.newDocumentBuilder();
				}
				catch (ParserConfigurationException e)
				{
					session.setAttribute("status", e.getMessage());
    				response.sendRedirect("Failure.jsp"); 
				}
			
			DataSource dataSource = (DataSource) DataSourceFactory.getDataSource(url,null);
		   	conn = dataSource.getConnection(userName, password);
	   		statement = conn.createStatement();           	   		
    		rs=statement.execute(XQuery);
    		while(rs.next())
    		{
    			InputStream stream=rs.getStream();	    		
				byte[] temp = null;
				
				try
				{
					temp = StreamHelper.copy(stream);					
				}
				catch (IOException e)
				{
					session.setAttribute("status", e.getMessage());
    				response.sendRedirect("Failure.jsp");
				}
				InputStream result = new ByteArrayInputStream(temp);				
				
				try
				{
					Document document = parser.parse(result);
					Element root = document.getDocumentElement();
					NodeList purchaseorders=root.getElementsByTagName("PO"); 					 
				    for (int i = 0; i < purchaseorders.getLength(); i++)
				    {	      
				    	out.println("<TR bgcolor=LightGrey class=tablefont2><form id='POSubmit' name='POSubmit' action='ASN-Generate.jsp' method='post'>");   					
    			
				    	Element purchase = (Element) purchaseorders.item(i);
				        String att=purchase.getAttribute("status");
				        
				        if(att.equals("checked"))
				        	continue;				        	                 
				        NodeList userids = purchase.getElementsByTagName("UserID");				        
				        out.println("<td>"+userids.item(0).getFirstChild().getNodeValue()+"</td>");  
				        
				        NodeList mailids = purchase.getElementsByTagName("MailID");
				        out.println("<td>"+mailids.item(0).getFirstChild().getNodeValue()+"</td>");
				        
				        NodeList orderids = purchase.getElementsByTagName("OrderID");
				        out.println("<td>"+orderids.item(0).getFirstChild().getNodeValue()+"</td>");
				        
				        NodeList orderdescs = purchase.getElementsByTagName("OrderDescription");
				        out.println("<td>"+orderdescs.item(0).getFirstChild().getNodeValue()+"</td>");
				        
				        NodeList dops = purchase.getElementsByTagName("DateOfPurchase");
				        out.println("<td>"+dops.item(0).getFirstChild().getNodeValue()+"</td>");
				        
				        NodeList itemids = purchase.getElementsByTagName("ItemID");
				        out.println("<td>"+itemids.item(0).getFirstChild().getNodeValue()+"</td>");
				        
				        NodeList itemdescs = purchase.getElementsByTagName("ItemDescrpition");
				        out.println("<td>"+itemdescs.item(0).getFirstChild().getNodeValue()+"</td>");
				        
				        NodeList itemqtys = purchase.getElementsByTagName("ItemQty");
				        out.println("<td>"+itemqtys.item(0).getFirstChild().getNodeValue()+"</td>");
				        
				        NodeList itemprices = purchase.getElementsByTagName("ItemPrice");
				        out.println("<td>"+itemprices.item(0).getFirstChild().getNodeValue()+"</td><td>");	    			
		    			
		    			out.println("<input type='hidden' name='UserID' id='UserID'  value='"+userids.item(0).getFirstChild().getNodeValue()+"'>");
		    			out.println("<input type='hidden' name='MailID' id='MailID' value='"+mailids.item(0).getFirstChild().getNodeValue()+"'>");
						out.println("<input type='hidden' name='OrderID' id='OrderID' value='"+orderids.item(0).getFirstChild().getNodeValue()+"'>");
						out.println("<input type='hidden' name='OrderDescription' id='OrderDescription' value='"+orderdescs.item(0).getFirstChild().getNodeValue()+"'>");
						out.println("<input type='hidden' name='DateOfPurchase' id='DateOfPurchase' value='"+dops.item(0).getFirstChild().getNodeValue()+"'>");
						out.println("<input type='hidden' name='ItemID' id='ItemID' value='"+itemids.item(0).getFirstChild().getNodeValue()+"'>");
						out.println("<input type='hidden' name='ItemDescription' id='ItemDescription' value='"+itemdescs.item(0).getFirstChild().getNodeValue()+"'>");
						out.println("<input type='hidden' name='ItemQty' id='ItemQty' value='"+itemqtys.item(0).getFirstChild().getNodeValue()+"'>");
						out.println("<input type='hidden' name='ItemPrice' id='ItemPrice' value='"+itemprices.item(0).getFirstChild().getNodeValue()+"'>");
		    			
				        out.println("<input type=submit value='Generate ASN' id='Generate ASN' name='Generate ASN'></form></td>");
				        out.println("</TR><TR><TD colspan=10></TD></TR></TR><TR><TD colspan=10></TD></TR>"); 
				    }
				}
				catch (SAXException e)
				{
					session.setAttribute("status", e.getMessage());
    				response.sendRedirect("Failure.jsp");
				}
				catch (IOException e)
				{
					session.setAttribute("status", e.getMessage());
    				response.sendRedirect("Failure.jsp");
				}				
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
      out.write("\t</TABLE>\r\n");
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

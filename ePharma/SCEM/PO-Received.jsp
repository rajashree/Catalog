<% 
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
%>
<%@ page import="com.rdta.tlapi.xql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.xml.parsers.*" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="org.xml.sax.*" %>
<%@ page import="com.rdta.util.io.StreamHelper" %>
<html>
<head>
<title>:::</title>
<link rel="stylesheet" href="css\Common.css" type="text/css">
<STYLE type=text/css>
	a { text-decoration: none }
</STYLE>
</head>
<body>
<br><br>
<TABLE cellSpacing=0 cellPadding=0 width="90%" align=center border=0 bgcolor="white" background="">
  <TR><TD vAlign=center align=right>
  	  <IMG id=IMG3 alt="" src="Images\blue_right_arrow.gif">&nbsp;
  	  <STRONG><A href="Seller.jsp"><FONT color=navy>Seller Home
      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>
</TABLE>
<TABLE WIDTH="90%" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 >
<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face="Bodoni MT" color=white size=5>
<STRONG> Submit Purchase Order</STRONG></FONT></TD></TR>
<TR><TD>
	<TABLE WIDTH="100%" ALIGN=right BORDER=0 CELLSPACING=0 CELLPADDING=0 >
		<TR bgcolor=lavenderblush class=tablefont1>
		<TD>UserID</TD>
		<TD>MailID</TD>
		<TD>OrderID</TD>
		<TD>OrderDescription</TD>
		<TD>DateOfPurchase</TD>
		<TD>ItemID</TD>
		<TD>ItemDescription</TD>
		<TD>ItemQty</TD>
		<TD>ItemPrice</TD>
		<TD></TD>
		</TR>
		<TR><TD colspan=10><font size="1"></font></TD></TR>
		<TR><TD colspan=10><form></form><font size="1"></font></TD></TR>
	<%		
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
	%>
	</TABLE>
</TD>
</TR>
</TABLE>	
</body>
</html>


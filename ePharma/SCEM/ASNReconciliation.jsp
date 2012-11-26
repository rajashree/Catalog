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

<html>
<head>
<STYLE type=text/css>
	a { text-decoration: none }
</STYLE>
<script>
	var req;	
	function showRecord()
	{ 
    	   document.getElementById("consolidationMessage").innerHTML="<img src='Images/Process.gif'>";
	   	   var idField = document.myform.consolidation.options[document.myform.consolidation.selectedIndex].value;
	   	   if(document.myform.consolidation.selectedIndex==0)
	   	   {
	   	   		document.getElementById("consolidationMessage").innerHTML="<b>...</b>";
	   	   }
	   	   else
	   	   {
			   var url = "./GetRecord?id="+encodeURIComponent(idField);
			   if (typeof XMLHttpRequest != "undefined")
			   {
			       req = new XMLHttpRequest();
			   }else if (window.ActiveXObject)
			   {
			       req = new ActiveXObject("Microsoft.XMLHTTP");
			   }
			   req.open("GET", url, true);
			   req.onreadystatechange = callback;
			   req.send(null);		   
		   }
	}	
	function callback()
	{
	    if (req.readyState == 4)
	    {
	        if (req.status == 200)
	        {
	            document.getElementById("consolidationMessage").innerHTML=req.responseText;
	            
	        }
	    }
	}	
</script>
<link rel="stylesheet" href="css\Common.css" type="text/css">
</HEAD>
<body onload="showRecord()">
<br><br>
<TABLE cellSpacing=0 cellPadding=0 width="60%" align=center border=0 bgcolor="white" background="">
  <TR><TD vAlign=center align=right>
  	  <IMG id=IMG3 alt="" src="Images\blue_right_arrow.gif">&nbsp;
  	  <STRONG><A href="Seller.jsp"><FONT color=navy>Seller Home
      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>
</TABLE>
<form name=myform> 
<TABLE WIDTH="60%" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 >
<TR><TD vAlign=center colspan=2 align=middle bgcolor=midnightblue><FONT face="Bodoni MT" color=white size=5>
<STRONG> ASN Reconsolation</STRONG></FONT></TD></TR>
<TR>
<TD class=fields width="50%"><SPAN 
		    class=Era_asterisk>*</SPAN>&nbsp;Select PurchaseorderID</TD>
		  <TD width="50%">
<select name="consolidation" onchange="showRecord()">
<option>[Select OrderID]

<%		
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
%>
</select>
</TD>
<TR>
<TD colspan=2 align="center"><div id="consolidationMessage"><b>...</b></div></TD>
</TR></TABLE>
</form>
</BODY>
</HTML>
</html>
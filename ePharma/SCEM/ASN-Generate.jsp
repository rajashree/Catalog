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

<html>
<head>
<title>:::</title>
<link rel="stylesheet" href="css\Common.css" type="text/css">
<script type="text/javascript" src="js/calendarDateInput.js"></script>
<SCRIPT>
	function valid_numbers(f)
	{
		if (!/^\d*$/.test(f.value))
		{
			alert("Only integer numbers allowed!");
			f.value = f.value.replace(/[^\d]/g,"");
		}
	}
	
	function validate()
	{		
		consignid=ASN_Generate.ConsignmentID.value;
		if (consignid=='')
		{
			alert('ConsignmentID should not be empty');
			document.ASN_Generate.ConsignmentID.focus();
			return false;
		}
		
		var mdiv = document.getElementById("ShipmentDate");
		mdiv.value = ASN_Generate.orderdate1.value;			

		qty=ASN_Generate.Rx-ItemQty.value;
		if (qty=='')
		{
			alert('ItemQty should not be empty');
			document.ASN_Generate.Rx-ItemQty.focus();
			return false;
		}
		
		price=ASN_Generate.Rx-ItemPrice.value;
		if (price=='')
		{
			alert('ItemPrice should not be empty');
			document.ASN_Generate.Rx-ItemPrice.focus();
			return false;
		}	
	}
</SCRIPT>
<STYLE type=text/css>
	a { text-decoration: none }
</STYLE>
</head>
<body>
<br><br>
<TABLE cellSpacing=0 cellPadding=0 width="60%" align=center border=0 bgcolor="white" background="">
  <TR><TD vAlign=center align=right>
  	  <IMG id=IMG3 alt="" src="Images\blue_right_arrow.gif">&nbsp;
  	  <STRONG><A href="Seller.jsp"><FONT color=navy>Seller Home
      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>
</TABLE>
<form id=ASN_Generate name=ASN_Generate onsubmit="return validate(this);" action="./ControlServlet" method='post' >
<input type='hidden' value='ASN-Generate' id='controller' name='controller'>
<TABLE WIDTH="60%" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 style="WIDTH: 60%">
<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face="Bodoni MT" color=white size=5>
<STRONG> ASN Generation</STRONG></FONT></TD>
</TR>
<TR><TD>
 <TABLE cellSpacing=1 cellPadding=1 width="60%" align=center border=1 style="BORDER-LEFT-COLOR: purple; BORDER-BOTTOM-COLOR: purple; WIDTH: 60%; BORDER-TOP-COLOR: purple; BORDER-RIGHT-COLOR: purple" 
      id=TABLE1 >                    
 <TR>
  <TD><FONT face=Verdana color=darkgreen><STRONG>Information Provided by Buyer</STRONG></FONT><BR>
    <TABLE cellSpacing=2 cellPadding=2 width="100%" align=center border=0>
      <TR><TD bgColor=gray colSpan=4></TD></TR>                  
      <TR>
        <TD class=fields width="50%"><FONT 
          color=#f68622>&nbsp; </FONT>&nbsp;UserID</TD>
        <TD width="50%"><INPUT id=UserID readOnly 
          name=UserID width="20" value=<%out.print(request.getParameter("UserID"));%>></TD>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;MailID</TD>
        <TD width="50%"><INPUT id=MailID name=MailID 
          width="20" readOnly value=<%out.print(request.getParameter("MailID"));%>></TD></TR>
      <TR>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;OrderID</TD>
        <TD width="50%"><INPUT id=OrderID name=OrderID 
          width="20" readOnly value=<% out.print(request.getParameter("OrderID")); %>></TD>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;OrderDescription</TD>
        <TD width="50%"><INPUT id=OrderDescription 
          name=OrderDescription width="20" readOnly value=<% out.print(request.getParameter("OrderDescription")); %>></TD></TR>
      <TR>
        <TD></TD>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;DateOfPurchase</TD>
        <TD width="50%"><INPUT id=DateOfPurchase 
          name=DateOfPurchase width="20" readOnly value=<% out.print(request.getParameter("DateOfPurchase")); %>></TD>
        <TD></TD></TR>
      <TR>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;ItemID</TD>
        <TD width="50%"><INPUT id=ItemID name=ItemID 
          width="20" readOnly value=<%out.print( request.getParameter("ItemID")); %>></TD>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;ItemDescription</TD>
        <TD width="50%"><INPUT id=ItemDescription 
          name=ItemDescription width="20" readOnly value=<% out.print(request.getParameter("ItemDescription")); %>></TD></TR>
      <TR>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;ItemQty</TD>
        <TD width="50%"><INPUT id=ItemQty 
          onkeyup=valid_numbers(this) name=ItemQty 
          width="20" readOnly value=<% out.print(request.getParameter("ItemQty")); %>></TD>
        <TD class=fields 
          width="50%">&nbsp;&nbsp;&nbsp;ItemPrice</TD>
        <TD width="50%"><INPUT id=ItemPrice name=ItemPrice 
          width="20" readOnly value=<% out.print(request.getParameter("ItemPrice")); %>></TD></TR>     
      
	</TABLE>
	</TD></TR>
	<TR><TD><FONT face=Verdana color=black><STRONG>Enter Information to Generate ASN</STRONG></FONT>
	<TABLE style="WIDTH: 100%" cellSpacing=1 cellPadding=1 width="80%" align=center border=0>                    
		<TR><TD bgColor=gray colSpan=2></TD></TR>
		<TR>
		  <TD class=fields width="50%"><SPAN 
		    class=Era_asterisk>*</SPAN>&nbsp;ConsignmentID</TD>
		  <TD width="50%"><INPUT id=ConsignmentID name=ConsignmentID 
		  width="20"></TD></TR>
		<TR>
		  <TD class=fields width="50%"><SPAN 
		    class=Era_asterisk>*</SPAN>&nbsp;ShipmentDate</TD>
		  <TD width="50%"><INPUT id=ShipmentDate type=hidden 
		    name=ShipmentDate width="20">
		    <script>DateInput('orderdate1', true, 'YYYY-MM-DD')</script>
		  </TD></TR>
		<TR>
		  <TD class=fields width="50%"><SPAN 
		    class=Era_asterisk>*</SPAN>&nbsp;ItemQty</TD>
		  <TD width="50%"><INPUT id=Rx-ItemQty  name=Rx-ItemQty 
		width="20" value=<% out.print(request.getParameter("ItemQty")); %>></TD></TR>
		<TR>
		  <TD class=fields width="50%"><SPAN 
		    class=Era_asterisk>*</SPAN>&nbsp;ItemPrice</TD>
		  <TD width="50%"><INPUT id=Rx-ItemPrice name=Rx-ItemPrice 
		    width="20" value=<% out.print(request.getParameter("ItemPrice")); %>></TD></TR>
		<TR><TD bgColor=gray colSpan=2></TD></TR>
		<TR><TD bgColor=gray colSpan=2></TD></TR>
		 <TR><TD align=middle colspan=2><input type=submit value="Generate ASN" id="Generate" name="Generate">&nbsp;&nbsp;<input type=reset value="Clear" id="Clear" name="Clear"></TD></TR>
	</TABLE>
	</TD></TR>
</TABLE>

</TD></TR>
</TABLE></body></form></HTML>

<% 
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
		
		userid=POGeneration.UserID.value;
		if (userid=='')
		{
			alert('UserId should not be empty');
			document.POGeneration.UserID.focus();
			return false;
		}
		
		orderid=POGeneration.OrderID.value;
		if (orderid=='')
		{
			alert('OrderID should not be empty');
			document.POGeneration.OrderID.focus();
			return false;
		}
		
		itemid=POGeneration.ItemID.value;
		if (itemid=='')
		{
			alert('ItemID should not be empty');
			document.POGeneration.ItemID.focus();
			return false;
		}

		qty=POGeneration.ItemQty.value;
		if (qty=='')
		{
			alert('ItemQty should not be empty');
			document.POGeneration.ItemQty.focus();
			return false;
		}
		
		price=POGeneration.ItemPrice.value;
		if (price=='')
		{
			alert('ItemPrice should not be empty');
			document.POGeneration.ItemPrice.focus();
			return false;
		}		
		
		var mdiv = document.getElementById("DateOfPurchase");
		mdiv.value = POGeneration.orderdate1.value;	
		
		mailid=POGeneration.MailID.value;
		if (mailid=='')
		{
			document.POGeneration.MailID.value="[None]";			
		}
		
		orderdesc=POGeneration.OrderDescription.value;
		if (orderdesc=='')
		{
			document.POGeneration.OrderDescription.value="[None]";			
		}	
		
		itemdesc=POGeneration.ItemDescription.value;
		if (itemdesc=='')
		{
			document.POGeneration.ItemDescription.value="[None]";			
		}	
	}
</SCRIPT>
<STYLE type=text/css>
	a { text-decoration: none }
</STYLE>
</head>
<body>
<br><br>
<TABLE cellSpacing=0 cellPadding=0 width="50%" align=center border=0 bgcolor="white" background="">
  <TR><TD vAlign=center align=right>
  	  <IMG id=IMG3 alt="" src="Images\blue_right_arrow.gif">&nbsp;
  	  <STRONG><A href="Buyer.jsp"><FONT color=navy>Buyer Home
      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>
</TABLE>
<TABLE WIDTH="40%" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 >
<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face="Bodoni MT" color=lightblue size=5>
<STRONG> Purchase Order Generation</STRONG></FONT></TD></TR>
	
	<TR>
		<TD><TABLE WIDTH="50%" ALIGN=right BORDER=0 CELLSPACING=0 CELLPADDING=0 style="WIDTH: 75%">
	<TR>
		<TD align=left width="50%">
			<form id="POGeneration" name="POGeneration" onsubmit="return validate(this);" action="./ControlServlet" method=post>
			<input type="hidden" value="POGeneration" id="controller" name="controller">
			<table  align=left id=TABLE1 style="WIDTH: 50%; HEIGHT: 324px" width="50%">
			<tr><td nowrap class="Smallfont">fields marked with an asterisk <span class="Era_asterisk">*</span> are required.</td></tr>
			<tr><td class="fields" width="50%"><span class="Era_asterisk">*</span>&nbsp;UserID</td><td width="50%"><input name='UserID' id='UserID' width="20" onkeyup="valid_numbers(this)"></td></tr>
			<tr><td class="fields" width="50%">&nbsp;&nbsp;&nbsp;MailID</td><td width="50%"><input name='MailID' id='MailID' width="20" ></td></tr>
			<tr><td class="fields" width="50%"><span class="Era_asterisk">*</span>&nbsp;OrderID</td><td width="50%"><input name='OrderID' id='OrderID' width="20" ></td></tr>
			<tr><td class="fields" width="50%">&nbsp;&nbsp;&nbsp;OrderDescription</td><td width="50%"><input name='OrderDescription' id='OrderDescription' width="20"></td></tr>
			<tr><td class="fields" width="50%"><span class="Era_asterisk">*</span>&nbsp;DateOfPurchase</td><td width="50%"><input type="hidden"name='DateOfPurchase' id='DateOfPurchase' width="20" >
				<script>DateInput('orderdate1', true, 'YYYY-MM-DD')</script>				
			</td></tr>
			<tr><td class="fields" width="50%"><span class="Era_asterisk">*</span>&nbsp;ItemID</td><td width="50%"><input name='ItemID' id='ItemID' width="20" ></td></tr>
			<tr><td class="fields" width="50%">&nbsp;&nbsp;&nbsp;ItemDescription</td><td width="50%"><input name='ItemDescription' id='ItemDescription' width="20" ></td></tr>
			<tr><td class="fields" width="50%"><span class="Era_asterisk">*</span>&nbsp;ItemQty</td><td width="50%"><input name='ItemQty' id='ItemQty' width="20" onkeyup="valid_numbers(this)"></td></tr>
			<tr><td class="fields" width="50%"><span class="Era_asterisk">*</span>&nbsp;ItemPrice</td><td width="50%"><input name='ItemPrice' id='ItemPrice' width="20" ></td></tr>
			<tr><td colspan=2 bgcolor=black></td></tr>
			<tr><td colspan=2 align=middle><input type=submit value="Generate" id="Generate" name="Generate">&nbsp;&nbsp;<input type=reset value="Clear" id="Clear" name="Clear"></td></tr>
			</table>
			</form>		
		</TD>
		<TD width="50%"><IMG style="WIDTH: 113px; HEIGHT: 264px" height=114 src="Images\POGeneration.jpg" width=113 ></TD>		
	</TR>
</TABLE>
</TD>
</TR>
</TABLE>	
</body>
</html>


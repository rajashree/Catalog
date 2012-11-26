<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<% HttpSession sess = request.getSession();%>
<%@ page import="java.util.List"%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree - Shipping Manager - Sign Pedigree</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
	</head>
	
	<body bgcolor="#C0C0C0">
	<br>
		
		<%String  sig= (String)request.getAttribute("Sig");
         	System.out.println("the signature"+sig);
         	%>
		<table border="0" cellspacing="0" cellpadding="0" width="80%" ID="Table1" align="center">
			<tr>
				<td></td>
				<td rowspan="2">&nbsp;</td>
			</tr>
			<tr>
         	
			<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
								</table>
				<td>
					<!-- info goes here -->
					<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
						bgcolor="#C0C0C0">
						<tr bgcolor="#C0C0C0">
						<%if(sig=="true"){%>
							<td class="td-typeblack"><center><STRONG>Signature already exists...!</STRONG></center></td>
						
						<%}else if(sig.equals("nosign")){%>
							<td class="td-typeblack"><center><STRONG>Signature has not Created...!</STRONG></center></td>
						<%}else{%>
						<td class="td-typeblack"><center><STRONG>Pedigree(s) Signed...!</STRONG></center></td>
						<%}%>
						</tr>
						
								<TR class="tableRow-On">
							<TD class="td-menu"></TD>
							<tr bgcolor="#C0C0C0"><td></td></tr>
						   <tr bgcolor="#C0C0C0">
							<TD class="td-menu" align="center"><html:button property="button1" value="Close" onclick="window.close()" /></TD>
						</TR>
										
		</table>
	   </body>
</html>
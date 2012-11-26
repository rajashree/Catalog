<TITLE>R & D - eList</TITLE>
<HEAD>

<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

<SCRIPT>

function redirect() {
	window.location = 'ManageElement.do?catalogGenID=""';
}

function submitform(operation)
{
	document.updateAndDelete.operationType.value = operation;

	//alert(document.updateAndDelete.operationType.value );
	document.updateAndDelete.submit();

	return t;
}


function goback(targetUrl)
{

	document.updateAndDelete.operationType.value =  "FIND";
    document.updateAndDelete.action = targetUrl;
	document.updateAndDelete.target="_top";
	document.updateAndDelete.submit();

	return true;
}


	
</SCRIPT>
</HEAD>


<%String catalogGenId=request.getParameter("catalogGenId");
System.out.println("####################################"+catalogGenId);
System.out.println("####################################"+(String)request.getAttribute("tpGenId"));
System.out.println("################################tpGenID "+request.getParameter("tpGenId"));
%>


<body>
	<table align="center" border="1" cellspacing="1" cellpadding="1" width="90%">
		 <br>
		<tr bgcolor="white">
			<td align="center"><FONT face="Arial" color="#cc0033" size="2"><STRONG>Selected Attribute</STRONG></FONT></td>
		</tr>
		<tr bgcolor="D3E5ED">
			<td>
				<br>
				
				<form name= "updateAndDelete" action="UpdateStandardSchemaElementTree.do" method="post" target="_top"  >

					<input type="hidden" name="catalogGenId" value="<%=catalogGenId%>">
					<input type="hidden" name="xpath" value="fghfg">
					<input type="hidden" name="operationType" value="">
					<INPUT id="hidden10" type="hidden" name="GenId" value="<%=request.getParameter("tpGenId")%>">
					
					<table align="center" cellSpacing="1" cellPadding="3" >

						<tr class="titleBlack">
							<td  colspan="4"> <b>Selected Attribute Location : fghfg </b></td>
						</tr>
						<tr>

							<TD>Attribute Name:</TD>
							<td colspan="3"><input type="text" name="name" value="fghfg" size='30'></td>
						</tr>

					
						
						<tr>
							

							<td colspan="4">

							
								<input type="button"  value="Go Back" onClick="return goback('StandardCatalogList.do')">

							

							<input type="button" value="ADD" onClick="return submitform('ADD')">
							
							

							 <input type="button" value="Update" onClick="return submitform('UPDATE')">

							<input type="button" value="Delete" onClick="return submitform('DELETE')">

							<input type="button"  value="Save Tree" onClick="return submitform('SAVE')">
							
																	
						</td>
						</tr>

						

					</table>
				</form>
			</td>
			
			
		</tr>
		
	</table>
	
	
</body>
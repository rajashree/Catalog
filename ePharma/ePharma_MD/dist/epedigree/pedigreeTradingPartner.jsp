<%@include file='../../includes/jspinclude.jsp'%>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>

<%
System.out.println("**Inside jsp**");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String accessLevel = (String)request.getAttribute("status");

String tp_company_nm = "";
if(pagenm == null) { pagenm = "pedigree"; }
%>

<script language="javascript">

	function submitForm(){
		
		
		return true;
	}
	
	function save(){
		frm = document.forms[0];
		if(check(frm)){						
					var buttonName = frm.buttonName.value;
					frm.action = "<html:rewrite action='/dist/epedigree/PedigreeTradingPartner.do?buttonName='/>"+buttonName;	
	               				
		}
		frm.submit();
			
}
			
		function check(frm){
				
				
				var name = frm.name.value;
				var deaNumber = frm.deaNumber.value;
				var notificationDescr = frm.notificationDescr.value;
				var notificationURI = frm.notificationURI.value;
				var destination = frm.destination.value;
				var localFolder = frm.localFolder.value;
				var notifyURI = frm.notifyURI.value;
				var userName = frm.userName.value;
				var pwd = frm.pwd.value; 			
				
				if(name==""){
					alert("Please enter the Name");
					return false;
				}
				if(deaNumber==""){
					alert("Please enter the DEA Number");
					return false;
				}
				if(notificationDescr==""){
					alert("Please enter the Notification Description");
					return false;
				}
				if(notificationURI==""){
					alert("Please enter the Notification URI");
					return false;
				}
				if(destination==""){
					alert("Please enter the Destination Routing Code");
					return false;
				}
				if(localFolder==""){
					alert("Please enter the LocalFolder");
					return false;
				}
				if(notifyURI==""){
					alert("Please enter the Notify URI");
					return false;
				}
				if(userName==""){
					alert("Please enter the UserName");
					return false;
				}
				if(pwd==""){
					alert("Please enter the Password");
					return false;
				}
				
			 return true;

			}
			
</script>

<html:html>
	<head>
		<title>Raining Data ePharma - ePedigree</title>
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<%@include file='../epedigree/topMenu.jsp'%>
		
<html:form action="/dist/epedigree/PedigreeTradingPartner.do" method="post">

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
	<tr> 
		<td width="0%"></td>
	    <td width="100%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
  	</tr>

  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="../../assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
                	<tr> 
                  		<td align="left">
						<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
						<tr class="tableRow_Header"> 
        	        	  <td class="title" colspan="4">Pedigree Trading Partner Details</td>
            	    </tr>		
               	    <tr> 
         				 <td><img src="../../assets/images/space.gif" width="20" height="1"></td>
				    </tr>
					
					<TR class="tableRow_On">
						<TD>Name:</TD>
						<TD><html:text property="name"/></TD>
						<TD>DEA Number:</td>
						<TD><html:text property="deaNumber"/></TD>
					</TR>
					
					<TR class="tableRow_Off">
						<TD>Notification Description:</TD>
						<TD>
							<html:select property="notificationDescr" > 
								<html:option value="">Select One...</html:option>
								<html:option value="ftp">FTP</html:option>
								<html:option value="sftp">SFTP</html:option>
								<html:option value="folder">Folder</html:option>
							</html:select>
						</TD>
						<TD>Notification URI:</td>
						<TD><html:text property="notificationURI"/></TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Destination Routing Code:</td>
						<TD><html:text property="destination"/></TD>
						<TD>Local Folder:</td>
						<TD><html:text property="localFolder"/></TD>
					</TR>
					
					<tr> 
         				 <td><img src="../../assets/images/space.gif" width="30" height="2"></td>
				    </tr>
				</TABLE>
				
				<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
       	        	  <td class="title" colspan="4">Notification Information:</td>
	           	    </tr>		
            	    <TR class="tableRow_Off" colspan="2">
						<TD >Notify URI:</TD>
						<TD><html:text property="notifyURI"/></TD>
						<td/><td/>
					</TR>
					<TR class="tableRow_On">
						<TD>User Name:</TD>
						<TD><html:text property="userName"/></TD>
						<TD>Password:</td>
						<TD><html:password property="pwd"/></TD>
					</TR>
					<tr> 
         				 <td><img src="../../assets/images/space.gif" width="30" height="2"></td>
				    </tr>
            	</TABLE>
            	
            	<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
       	        	  <td class="title" colspan="4">Configuration Elements:</td>
	           	    </tr>

            	    <TR class="tableRow_On">
						<TD class="td-menu"><b>For Manual Usecase:</b></TD>
						<TD/>
						<TD class="td-menu"><b>For DropShip Usecase:</b></TD>
						<TD/>
					</TR>
					<TR class="tableRow_Off">
						<TD width="30%">Container Code:</TD>
						<TD>
							<html:select property="containerCodeMU"> 
								<html:option value="ponumber">PONumber</html:option>
								<html:option value="invoicenumber">InvoiceNumber</html:option>
							</html:select>
						</TD>
						<TD width="30%">Container Code:</TD>
						<TD>
							<html:select property="containerCodeDU"> 
								<html:option value="ponumber">PONumber</html:option>
								<html:option value="invoicenumber">InvoiceNumber</html:option>
							</html:select>
						</TD>
					</TR>
					<TR class="tableRow_On" colspan="2">
						<TD width="30%">Shipment Handle:</TD>
						<TD>
							<html:select property="shipmentHandleMU"> 
								<html:option value="ponumber">PONumber</html:option>
								<html:option value="invoicenumber">InvoiceNumber</html:option>
							</html:select>
						</TD>
						<TD width="30%">Shipment Handle:</TD>
						<TD>
							<html:select property="shipmentHandleDU"> 
								<html:option value="ponumber">PONumber</html:option>
								<html:option value="invoicenumber">InvoiceNumber</html:option>
							</html:select>
						</TD>
					</TR>
					<tr> 
         				 <td><img src="../../assets/images/space.gif" width="30" height="2"></td>
				    </tr>
            	</TABLE>
			<%
				 String updatePTP = (String)request.getAttribute("UpdatePTP");
				 System.out.println("Update PTP : "+updatePTP);
				if(updatePTP != null && updatePTP.equals("true")) {
			%>	
			<Center><html:submit property="buttonName" value="Update"/> </Center>
			<%}else{%>
			
           <Center><html:button property="buttonName"  value="Save" onclick="save();"/> </Center>
           <%}%>
         </td>
        </tr>
      </table>

</html:form>
<%@include file='onLoad.jsp'%>	
<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
	
</html:html>
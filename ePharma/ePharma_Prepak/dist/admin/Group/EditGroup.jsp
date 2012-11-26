<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ page language="java" import="com.rdta.Admin.servlet.RepConstants" %>
<%
	String sessionID=(String)request.getAttribute("sessionID");
	if(sessionID==null){sessionID="";}
%>
<html:html>
<head>
<TITLE>RDTA-FRID Location Management</TITLE>
	<style type="text/css" media="all"> @import "includes/page.css";
	@import "../includes/page.css"; @import "assets/epedigree1.css";
	@import "includes/nav.css"; @import "../includes/nav.css"; </style>
	
</style>

</head>
<script language="JavaScript">
		<!--
			function AddNew() {
				frm = document.forms[0];
				var action="<%= RepConstants.ACCESS_INSERT %>";
				frm.action = "<html:rewrite  action='/AddGroup.do?exec=AddNewGroup'/>"+"&action="+action;
				frm.submit();
			}	
			function ViewGroup() {
				frm = document.forms[0];
				var sessionID=frm.sessionID.value;
				var elements = frm.elements;
				var elementsLength = elements.length;
				var rdVal=0; 				
				for (var i = 0; i < elementsLength; i++) { 
					if((elements[i].type=="radio" )&& (elements[i].checked))
						rdVal=elements[i].value; 
				} 
				frm.action = "<html:rewrite  action='/AddGroup.do?exec=fetchDetails&sessionID='/>"+sessionID+"&groupID="+rdVal;
				frm.submit();
			}	
		//-->
</script>
<BR>
<body>


<TABLE align="center" cellSpacing="1" cellPadding="1" border="1">

<html:form action="/AddGroup" method="post">
	<bean:size id="totRecords" name="Groups" />
	<input type="hidden" name="totRecords" value="<%= totRecords %>">
	
	<TR class="tableRow_Header">
		<TD colspan="2">
			<P align="center"><STRONG>Edit Groups</STRONG></P>
		</TD>
	</TR>
	
	<TR bgcolor="D3E5ED">
		
		<TD>
			<P align="center"><STRONG>GroupName</STRONG></P>
		</TD>
		<TD>
			<P align="center"><STRONG>View/Edit</STRONG></P>
		</TD>
	</TR>
	
	 <logic:equal name="totRecords" value="0">
		<tr> 
			<td align="left" class="Information" colspan="2"> 
				There are no Records to Edit.											
			</td>
		</tr> 					
	</logic:equal> 
	
	<logic:notEqual name="totRecords" value="0">

	<%
		int i = 0;
	%>
	<logic:iterate name ="Groups" id="group">
		<tr class="<%= i % 2 == 0 ? "tableRow_Off" : "tableRow_On" %>">	
		<td>
			<bean:write name="group" property="groupName" />
		</td>
		<td>
				<a href="AddGroup.do?exec=fetchDetails&groupID=<bean:write name="group" property="groupID"/>"><Strong>View / Edit</Strong></a>
		</td>
		</tr>
			
	<% i++; %>
	
	</logic:iterate>
	</logic:notEqual> 
	 <logic:notEqual name="totRecords" value="0">
	<TR>
		<TD colspan="2">
			<P align="center"><EM><FONT color="#000099">Click on the View/Edit to edit it...</FONT></EM></P>
		</TD>
	</TR>
	</logic:notEqual>
	<TR class="tableRow_Off">
		<TD align="center" colspan="2">
			
		<html:button property="button" styleClass="Button" value="New" onclick="AddNew();"/>
		</TD>
	</TR>
</TABLE>
</html:form>
</body>
</html:html>



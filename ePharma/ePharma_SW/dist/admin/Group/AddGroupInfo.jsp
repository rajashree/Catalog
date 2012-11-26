<%@ page session="true"%>
<%@ page autoFlush="true" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ page language="java" import="com.rdta.Admin.servlet.RepConstants" %>
<%@ page import="java.util.List,java.util.Map,com.rdta.Admin.Group.ArrayUtils" %>

<%
	String sessionID =(String)request.getParameter("sessionID");
%>
<html:html>
<head>
<TITLE>RDTA-FRID User Management</TITLE>
<script language="JavaScript" type="text/javascript">
<!--

	
	function selectAll(){
		fmobj=document.forms[0];
		if(fmobj.check.checked){
			for (var i=0;i<fmobj.elements.length;i++) {
			var e = fmobj.elements[i];
	  		if ((e.type=='checkbox') && (!e.disabled) ) {
	      			e.checked = true;
	    			}
	    	}
    		return true;
    	}
		else{
			for (var i=0;i<fmobj.elements.length;i++) {
			var e = fmobj.elements[i];
				if ((e.type=='checkbox') && (!e.disabled) ) {
	      			e.checked = false;
	    		}
	    	}
			return true;
		}
		
	}
	

	
	function selectAllSub(id1,permission){
		
		var type=id1.type;
		var id=id1.id;
		var alltypes=document.getElementsByTagName('input');
		for(i=0;i<alltypes.length;i++){
			var cur=alltypes[i];
			if(cur.type==type && cur.id==id && cur.value==permission){
			cur.checked=id1.checked;
			}
		}
		return true;
	}
	
		
	function save(value) {				
				frm = document.forms[0];
				var groupID=frm.groupID.value;
				var action="<%= RepConstants.ACCESS_INSERT %>";
				if( groupID !="" &&  groupID !="null" )
					action="<%=RepConstants.ACCESS_UPDATE %>";
				if(check(frm)){	
				frm.action = "<html:rewrite  action='/AddGroup.do?exec=SaveGroup&groupID='/>"+groupID+"&action="+action;							frm.submit();
				}				
		}
		
		function check(frm){
				frm = document.forms[0];
				var isValid=true;
				var val1=frm.groupName.value;
				if(val1==""){
					alert("Please enter the group name");
					isValid=false;
				}
				
				if(!isValid) {
					return isValid;
				}
				
			 return isValid;

			}
		
		
			
		function allSelect(List)
		{	
			if( List instanceof Array ){
			   
				for (i=0;i<List.length;i++)
				{	
				var name1=List[i];
				
				checks=document.getElementsByName(name1);
				for (j=0;j<checks.length;j++){
				 
			     checks[j].checked = true;
			    }
			   }
			   
			}
			else{
			  for (i=0;i<List.length;i++)
			  {
			     List[i].checked = true;
			  }
			 }
			  return true;
		}	
		function clearSelect(List)
		{	
			if( List instanceof Array ){
			    
				for (i=0;i<List.length;i++)
				{	
				name=List[i];
				checks=document.getElementsByName(name);
				for (j=0;j<checks.length;j++){
				 
			     checks[j].checked = false;
			  }
			}
			}
			else{
			  for (i=0;i<List.length;i++)
			  {
			     List[i].checked = false;
			  }return true;
			 }
		}	
		
		function main() {
				frm = document.forms[0];
				var groupID=frm.groupID.value;
				if((groupID !=="") && (groupID !=="null")){
					frm.action = "<html:rewrite  action='/AddGroup.do?exec=AddNewGroup&sessionID='/>"+sessionID;
					frm.submit();
				}
		}
	

-->
</script>
<style type="text/css" media="all"> @import "includes/page.css";
	@import "../includes/page.css"; @import "assets/epedigree1.css";
	@import "includes/nav.css"; @import "../includes/nav.css"; </style>
	
</head>	
<body>
	<html:form action="/AddGroup" method="post" enctype="multipart/form-data">
	
	<TABLE align="center" cellSpacing="1" cellPadding="1" border="1" width="450">
	<TBODY>
			<TR class="tableRow_Header">
				<TD colspan="5">
					<P align="center"><STRONG>Add Group</STRONG></P>
				</TD>
			</TR>
			
			<TR bgcolor="D3E5ED">
				<TD align="center" colSpan="5">
					<ul id="globalnav">
					<li>
						<a href="javascript:main()" class="here">Group</a></li>
					</ul>
			   </TD>
			</TR>
			
			<tr class="tableRow_Off">
			
				<td colspan="5"><html:hidden property="groupID" style="BACKGROUND-COLOR: #cccccc" /></td>
				
			</tr>
			
			
			
			<tr class="tableRow_Off">
			<td> Group Name:</td>
			    <td colspan="4">
			    	<html:text property="groupName" size="30" maxlength="35" styleClass="FormLabelCap" /><br>
    			</td>
			</tr>
			<tr class="tableRow_Off">
				<td width="250"><STRONG>ModuleName</STRONG></td>
				<td><STRONG>Insert</STRONG></td>
				<td><STRONG>Update</STRONG></td>
				<td><STRONG>Delete</STRONG></td>
				<td><STRONG>Read</STRONG></td>
			</tr>
			
			
			<tr><td colspan="5">
			<div style="height:300;width:450;overflow:scroll">
			<table cellSpacing="5" cellPadding="1">
			
			
			<tr class="tableRow_Off"><td colspan="5">
			<TABLE border="1" width="450">
			
			
			
			</TABLE>
   			</td></tr>
   			
			
				
				<logic:iterate id="accessMap" name="GroupForm" property="fields" >
				<bean:define id="accessMapKey" name="accessMap" property="key" />
				<bean:define id="accessMapValue" name="accessMap" property="value" />
				
				<tr class="tableRow_Off"><td colspan="5">
				<TABLE border="1" width="450">
				<%int check=0;%>
				<logic:iterate id="groupData" name="accessMapValue" type="com.rdta.Admin.Group.GroupData" >
					
					<logic:empty property="groupID" name="GroupForm">
					<% if(check==0){%>
					<tr class="tableRow_Off">
						<td width="250"><font color="blue"><STRONG><%=groupData.getName()%></STRONG></font></td>
						<%int i=0;%>
					
						<logic:iterate id="permission" property="permissions" name="GroupForm">
								<td><input type="checkbox" id="<%=accessMapKey%>" name="<%=groupData.getName()%>" onclick="selectAllSub(this,'<%=permission%>')" value="<bean:write name="permission"/>"/>
				  				</td>
		   				</logic:iterate>
					</tr>
					<% }else {%>
					<tr class="tableRow_Off">
						<td width="250"><STRONG><%=groupData.getName()%></STRONG</font></td>
						<%int i=0;%>
						<logic:iterate id="permission" property="permissions" name="GroupForm">
									<td><input type="checkbox" id="<%=accessMapKey%>" name="<%=groupData.getName()%>" value="<bean:write name="permission"/>"/>
					  				</td> 
						</logic:iterate>
					</tr>
					<% } %>
					</logic:empty>
					
					
					
					
					<logic:notEmpty property="groupID" name="GroupForm">
					<% String accessPermission[]=groupData.getPermissions();
					%>
					
					<% if(check==0){%>
					<tr class="tableRow_Off">
						<td width="250"><font color="blue"><STRONG><%=groupData.getName()%></STRONG></font></td>
						<%int i=0;%>
						<logic:iterate id="permission" property="permissions" name="GroupForm">
							<%if(ArrayUtils.contains(accessPermission,String.valueOf(permission))){
							%>
								<td><input type="checkbox" id="<%=accessMapKey%>" name="<%=groupData.getName()%>" onclick="selectAllSub(this,'<%=permission%>')" value="<bean:write name="permission"/>" checked/>
				  				</td> 
				  			<%}else{ %>
				  				<td><input type="checkbox" id="<%=accessMapKey%>" name="<%=groupData.getName()%>" onclick="selectAllSub(this,'<%=permission%>')" value="<bean:write name="permission"/>"/>
				  				</td> 
			  				<%}%>
		   				</logic:iterate>
					</tr>
					<% }else {%>
					<tr class="tableRow_Off">
						<td width="250"><STRONG><%=groupData.getName()%></STRONG</font></td>
						<%int i=0;%>
						<logic:iterate id="permission" property="permissions" name="GroupForm">
							<%if(ArrayUtils.contains(accessPermission,String.valueOf(permission))){%>
								<td><input type="checkbox" id="<%=accessMapKey%>" name="<%=groupData.getName()%>" value="<bean:write name="permission"/>" checked/>
				  				</td> 
			  				<%}else{ %>
			  					<td><input type="checkbox" id="<%=accessMapKey%>" name="<%=groupData.getName()%>" value="<bean:write name="permission"/>"/>
				  				</td> 
				  			<%}%>
		   				</logic:iterate>
					</tr>
					<% } %>
					</logic:notEmpty>
					
					
					
					
				<%check++;%>	
				</logic:iterate>
					
				</TABLE>
   				</td></tr>	
			</logic:iterate> <br>
			
			
		
			
			</table>
			</div></td></tr>
			
			<tr class="tableRow_Off">
			<td > Check All:</td><td>
			<input type="checkbox" name="check" value="checkall" onclick="selectAll();"/>
			<td colspan="4"></td>
			</tr>
	
	
	
	
	  <tr align="center" class="tableRow_Off">
	  	<td colspan="5">
	  	<logic:notPresent name="groupID">
	    	<html:button property="button" styleClass="Button" value="Save" onclick="save(groupID.value);">
			</html:button>
		      <input type="reset"  value="Clear" class="button">
	    	
		</logic:notPresent>
		<logic:present name="groupID">
			<html:button property="button" styleClass="Button" value="Update" onclick="save(groupID.value);">
			</html:button></td>
		</logic:present>
	  </tr>
	
	</TBODY>
	</TABLE>
			
			 
</html:form>
</body>
</html:html>

			
			
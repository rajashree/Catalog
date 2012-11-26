<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>

<%@ page language="java" import="com.rdta.commons.xml.XMLUtil" %>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.rdta.commons.persistence.*" %>
<%@ page import="com.rdta.Admin.servlet.RepConstants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ page import="java.util.List"%>

<%
String userID=(String)request.getAttribute("userID");
String pictFile=(String)request.getAttribute("pictFile");
String validUser=(String)request.getAttribute("validUser");
String userName=(String)request.getAttribute("userName");
QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css" media="all"> @import "includes/page.css";
	@import "../includes/page.css"; @import "assets/epedigree1.css";
	@import "includes/nav.css"; @import "../includes/nav.css"; </style>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>User Group</title>


<script language="JavaScript" type="text/JavaScript">
<!--




function MM_jumpMenu(targ,selObj,restore){ //v3.0

  if (restore) selObj.selectedIndex=0;
}

var NS4 = (navigator.appName == "Netscape" && parseInt(navigator.appVersion) < 5);

function addOption(theSel, theText, theValue)
{
  var newOpt = new Option(theText, theValue);
  var selLength = theSel.length;
  theSel.options[selLength] = newOpt;
}

function deleteOption(theSel, theIndex)
{ 
  var selLength = theSel.length;
  if(selLength>0)
  {
    theSel.options[theIndex] = null;
  }
}


function moveOptions(theSelFrom, theSelTo)
{
  
  var selLength = theSelFrom.length;
  var selectedText = new Array();
  var selectedValues = new Array();
  var selectedCount = 0;
  var i;
  
  // Find the selected Options in reverse order
  // and delete them from the 'from' Select.
  for(i=selLength-1; i>=0; i--)
  {
    if(theSelFrom.options[i].selected)
    {
      selectedText[selectedCount] = theSelFrom.options[i].text;
      selectedValues[selectedCount] = theSelFrom.options[i].value;
      deleteOption(theSelFrom, i);
      selectedCount++;
    }
  }
  
  // Add the selected text/values in reverse order.
  // This will add the Options to the 'to' Select
  // in the same order as they were in the 'from' Select.
  for(i=selectedCount-1; i>=0; i--)
  {
    addOption(theSelTo, selectedText[i], selectedValues[i]);
  }
  
  if(NS4) history.go(0);
	}

		function save(value) {
				
				frm = document.forms[0];
				var userID=frm.userID.value;
				var action="<%= RepConstants.ACCESS_INSERT %>";
				if( userID !="" &&  userID !="null" )
					action="<%=RepConstants.ACCESS_UPDATE %>";
				
				if(check(frm)){						
					frm.action = "<html:rewrite action='/AddUser.do?exec=SaveUser&userID='/>"+userID+"&action="+action;		allSelect(document.forms[0].accessLevel);
	                if(document.forms[0].tradingPartners!=null){  
						allSelect(document.forms[0].tradingPartners);
						allSelect(document.forms[0].bgLocation);					
					}
					frm.submit();
					
		}
}
			
		function check(frm){
				frm = document.forms[0];
				var val1=frm.userName.value;
				 
				var firstName=frm.firstName.value;
				var lastName=frm.lastName.value;
				var password=frm.password.value;
				
				if(firstName==""){
					alert("Please enter the firstName");
					return false;
				}
				if(lastName==""){
					alert("Please enter the lastName");
					return false;
				}
				if(val1==""){
					alert("Please enter the user name");
					return false;
				}
				if(password==""){
					alert("Please enter the password");
					return false;
				}
				
			 return true;

			}
			
		function main() {
				frm = document.forms[0];
				var locID=frm.locationID.value;
				if((locID !=="") && (locID !=="null")){
					frm.action = "<html:rewrite  action='/Location.do?exec=AddLocation&sessionID='/>"+sessionID;
					frm.submit();
				}
			}
		function allSelect(List)
		{		

			  for (i=0;i<List.length;i++)
			  {
			     List.options[i].selected = true;
			     
			  }return true;
		}	
//-->
</script>
</head>



<body>

	
<html:form action="/AddUser" method="post" enctype="multipart/form-data">
<html:hidden property="userID"/>
<input type="hidden" name="username" value="<%=userName%>"/>
<TABLE align="center" cellSpacing="3" cellPadding="1" border="1">
<TR class="tableRow_Header">
					<TD colspan="6">
						<P align="center"><STRONG>System User Detail Screen </STRONG></P>
					</TD>
				</TR>
 
  <tr class="tableRow_Off">
    <td><table width="100%"  cellspacing="15" cellpadding="0" border="1">
        <tr class="tableRow_Off">
          <td width="50%" valign="top"><table width="100%"   cellspacing="0" cellpadding="3">
              <tr class="tableRow_Header">
                <td colspan="4"><STRONG>User Info </STRONG></td>
              </tr>
              <tr class="tableRow_Off">
                <td class="style2"><div align="right"><STRONG>First Name:</STRONG></div></td>
                <td><div align="left">
                    <html:text property="firstName"/>
                </div></td>
              </tr>
              <tr class="tableRow_Off">
                <td><STRONG>Last Name : </STRONG></td>
                <td><div align="left">
                    <html:text property="lastName"/>
                </div></td>
              </tr>
              <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Phone:</STRONG></div></td>
                <td><div align="left">
                    <html:text property="phone"/>
                </div></td>
              </tr>
              <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Fax:</STRONG></div></td>
                <td><div align="left">
                    <html:text property="fax"/>
                </div></td>
              </tr>
              <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Email:</STRONG></div></td>
                <td><div align="left">
                    <html:text property="email"/>
                </div></td>
              </tr>

			  <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Signer:</STRONG></div></td>
                <td><html:checkbox property="signer"/></td>
              </tr>

          </table></td>
          <td valign="top"><table width="100%"  cellspacing="0" cellpadding="3">
              <tr class="tableRow_Header">
                <td colspan="2"><STRONG>Login Info</STRONG></td>
              </tr>
             
             
              <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Username:</STRONG></div></td>                
                <td><html:text property="userName"/></td> 				
              </tr>
              				
				<logic:equal name="validUser" value="true">
					<tr>
					<td colspan="4" align ="center"><font color="red">UserName <%=userName%> already exists.Please select another User Name</font></td></tr> 
				</logic:equal>
              

              
              <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Password: </STRONG></div></td>
                <td><html:password property="password"/></td>
              </tr>
              <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Disabled:</STRONG></div></td>
                <td><html:checkbox property="disabled"/></td>
              </tr>
              <tr class="tableRow_Off">
                <td><div align="right"><STRONG>Role:</STRONG></div></td>
                <td><html:select property="userRole"  onchange="MM_jumpMenu('parent',this,0)">  
				 <!-- <html:option value="" disabled="true">Select</html:option> -->
		    	 <html:option value="InternalUser">InternalUser</html:option>    			   
		    	 <html:option value="Director of Pharmacy">Director of Pharmacy</html:option> 
		    	 <html:option value="Operations Manager">Operations Manager</html:option> 
		    	 <html:option value="Inventory Control Supervisor">Inventory Control Supervisor</html:option> 
		    	 <html:option value="Director of Regulatory Affairs">Director of Regulatory Affairs</html:option> 
    	 		 </html:select></td>
               </tr>                                      
				<TR class="tableRow_Off">
					<td><div align="right"><STRONG>Company:</STRONG></div></td>
	                <td><html:select property="belongsToCompany" >  	                	 
						<html:option value="Southwood">Southwood</html:option> 
	    	 		 </html:select></td>

				</TR>
				<tr class="tableRow_Off">
					<td>
						<div align="right"><Strong>Digital Sign:</Strong> <BR><BR>						
						 <html:file property="signatureFile"/>
						</div>
					</td>
					<logic:present name="pictFile">
                		<td>
							<img src="<%=request.getContextPath()%>/servlet/showImageFromTL?dbname=EAGRFID&collname=UserSign&topnode=User&nodename=UserSign&UserID=<bean:write property='userID' name='UserForm'/>"/>
						</td>
					</logic:present>
              </tr>
			

			
          </table></td>
        </tr>
    </table></td>
  </tr>
  
  <tr class="tableRow_Off">
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
      <tr class="tableRow_Header">
        <td colspan="3"><STRONG>Group Access</STRONG></td>
        </tr>
      <tr class="tableRow_Off">
        <td><div align="center"><STRONG>Available Groups</STRONG></div></td>
        <td>&nbsp;</td>
        <td><div align="center"><STRONG>Asigned to Groups</STRONG></div></td>
      </tr>
      
    
      <tr class="tableRow_Off">
        <td><div align="center">
          <select name="Groups" size="5" multiple style="width: 100px;">  
			<logic:iterate id="element" name="GroupNotInsertedList" type="com.rdta.Admin.User.UserData" scope="request">	     
		    	<option value="<bean:write name="element" property="id"/>"><bean:write name="element" property="name" /></option> 
	    	</logic:iterate>
			</select>
        </div></td>
    
        <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
          <tr class="tableRow_Off">
            <td><div align="center">
              <input type="button" name="Button" value="   >>   " onClick="moveOptions(this.form.Groups, this.form.accessLevel);">
            </div></td>
          </tr>
          <tr class="tableRow_Off">
            <td><div align="center">
              <input type="button" name="Button" value="   <<   " onClick="moveOptions(this.form.accessLevel, this.form.Groups);">
            </div></td>
          </tr>
        </table></td>
    
        <td><div align="center">
         <select name="accessLevel" size="5" multiple style="width: 100px;">  
   		 
   		  	<logic:iterate id="element4" name="GroupInsertedList" type="com.rdta.Admin.User.UserData" scope="request">	     
			    <option value="<bean:write name="element4" property="id"/>"><bean:write name="element4" property="name" /></option> 
		    	</logic:iterate>
		  
     	 </select>
        </div></td>
      </tr>
    </table></td>
  </tr>
  <tr class="tableRow_Off">				
				<logic:empty property="userID" name="UserForm">
					<td><div align="center">
         				<html:button property="button" styleClass="Button" value="Save" onclick="save(userID.value);"/>
    				</div></td>
				
				</logic:empty>
				
				<logic:notEmpty property="userID" name="UserForm">
				<td><div align="center">
         				<html:button property="button" styleClass="Button" value="Update" onclick="save(userID.value);"/>
    			</div></td>
				</logic:notEmpty>	 
									
	</tr>
  
  
</table>

</html:form>
</body>
</html>

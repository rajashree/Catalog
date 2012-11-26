<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>User Group</title>

<style type="text/css" media="all"> @import "includes/page.css";
	@import "../includes/page.css"; @import "assets/epedigree1.css";
	@import "includes/nav.css"; @import "../includes/nav.css"; </style>
	
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
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
			
				var pic=frm.signatureFile.value;
				if(check(frm)){
					frm.action = "<html:rewrite action='/AddUser.do?exec=SaveUser&userID='/>"+userID+"&Picture="+pic;
					allSelect(document.forms[0].accessLevel);
					allSelect(document.forms[0].tradingPartners);
					allSelect(document.forms[0].bgLocation);
					allSelect(document.forms[0].bgLocationsAt);
					frm.submit();
		}
}
			
		function check(frm){
				frm = document.forms[0];
				var msg=new Array(2);
				var msgCnt = 0;
				var isValid=true;
				var val1=frm.userName.value;
				if(val1==""){
					msg[msgCnt++]="Please enter the user name";
					isValid=false;
				}
				
				if(!isValid) {
					
					return isValid;
				}
				
			 return isValid;

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
<table width="778" border="1" align="center" cellpadding="10" cellspacing="0" bordercolor="#999999" bgcolor="#CCCCCC" class="style2">
  <tr class="tableRow_Header">
    <td bgcolor="#0066FF"><div align="center" class="style1">System User Detail Screen </div></td>
  </tr>
  <tr>
    <td><table width="100%"  border="0" cellspacing="15" cellpadding="0">
        <tr>
          <td width="50%" valign="top"><table width="100%"  border="0" cellspacing="0" cellpadding="3">
              <tr class="tableRow_Header">
                <td colspan="2">User Info </td>
              </tr>
              <tr>
                <td class="style2"><div align="right">First Name:</div></td>
                <td><div align="left">
                    <html:text property="firstName"/>
                </div></td>
              </tr>
              <tr>
                <td><div align="right">Last Name : </div></td>
                <td><div align="left">
                    <html:text property="lastName"/>
                </div></td>
              </tr>
              <tr>
                <td><div align="right">Phone:</div></td>
                <td><div align="left">
                    <html:text property="phone"/>
                </div></td>
              </tr>
              <tr>
                <td><div align="right">Fax:</div></td>
                <td><div align="left">
                    <html:text property="fax"/>
                </div></td>
              </tr>
              <tr>
                <td><div align="right">Email:</div></td>
                <td><div align="left">
                    <html:text property="email"/>
                </div></td>
              </tr>
          </table></td>
          <td valign="top"><table width="100%"  border="0" cellspacing="0" cellpadding="3">
              <tr class="tableRow_Header">
                <td colspan="2">Login Info</td>
              </tr>
              <tr>
                <td><div align="right">Username:</div></td>
                <td><html:text property="userName"/></td>
              </tr>
              <tr>
                <td><div align="right">Password: </div></td>
                <td><html:password property="password"/></td>
              </tr>
              <tr>
                <td><div align="right">Disabled</div></td>
                <td><html:text property="disabled"/></td>
              </tr>
              <tr>
                <td><div align="right">Role</div></td>
                <td><html:select property="userRole" style="width: 100px; onChange="MM_jumpMenu('parent',this,0)">  
			
			     <html:option value="" disabled="true">Select</option> 
		    	 <html:option value="InternalUser">InternalUser</option>     
			     <html:option value="Pharmacy">Pharmacy</option>     
		    	 <html:option value="Hospital">Hospital</option>     
			     <html:option value="MailPharmacy">MailPharmacy</option>     	
			     <html:option value="Repackager">Repackager</option>    
			     <html:option value="Packager">Packager</option>   
    	 		 </select></td>
               </tr>
               
              <tr>
                <td><div align="right">Digital Sign <br>
                      <br>
                      <html:file property="signatureFile"/>
                </div></td>
                <% String context=request.getContextPath();
					String imgname="<img src='"+context+"/User/"+userID+".jpeg'>";%>
                <td><input name="imageField" type="image" src="<%=imgname%>" width="150" height="75" border="0"></td>
              </tr>
          </table></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
      <tr class="tableRow_Header">
        <td colspan="3">Keys</td>
        </tr>
      <tr>
        <td width="14%"><div align="right">Private : 
          
        </div></td>
        <td width="60%"><div align="center">
          <html:text property="private" style="BACKGROUND-COLOR: #cccccc" readonly="true" width="400"/>
         </div></td>
        <td width="26%" rowspan="2">
        <html:button property="Keys" styleClass="Button" value="    Generate Keys    " onclick="generate()">
        </td>
      </tr>
      <tr>
        <td><div align="right">Public : </div></td>
        <td><div align="center">
          <html:text property="public" style="BACKGROUND-COLOR: #cccccc" readonly="true" width="400"/>
        </div></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
      <tr class="tableRow_Header">
        <td colspan="3">Group Access </td>
        </tr>
      <tr>
        <td><div align="center">Available Groups </div></td>
        <td>&nbsp;</td>
        <td><div align="center">Asigned to Groups </div></td>
      </tr>
      
      

		<logic:notEmpty property="userID" name="UserForm">
    	<%	List groupList = (List)request.getAttribute("GroupNotInsertedList");
      		request.setAttribute("GroupList", groupList);
		 %>
		</logic:notEmpty>
		
		
		 <logic:notEmpty property="userID" name="UserForm">
    	<%	List ls1=(List)request.getAttribute("GroupInsertedList");
		  	request.setAttribute("GroupInsertedList",ls1);
		 %>
		 </logic:notEmpty>
 	
		
      <tr>
        <td><div align="center">
          <select name="Groups" size="5" multiple style="width: 100px;">  
			<logic:iterate id="element" name="GroupList" scope="request" type="com.rdta.Admin.User.UserData" >	     
		    	<option value="<bean:write name="element" property="id"/>"><bean:write name="element" property="name" /></option> 
	    	</logic:iterate>
			</select>
        </div></td>
        
        <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
          <tr>
            <td><div align="center">
              <input type="button" name="Button" value="   Asign   " onClick="moveOptions(this.form.Groups, this.form.accessLevel);">
            </div></td>
          </tr>
          <tr>
            <td><div align="center">
              <input type="button" name="Button" value="   Remove   " onClick="moveOptions(this.form.accessLevel, this.form.Groups);">
            </div></td>
          </tr>
        </table></td>
        
        <td><div align="center">
         <select name="accessLevel" size="5" multiple style="width: 100px;">  
   		  <logic:notEmpty property="userID" name="UserForm">
   		  	<logic:iterate id="element4" name="GroupInsertedList" scope="request" type="com.rdta.Admin.User.UserData" >	     
			    <option value="<bean:write name="element4" property="id"/>"><bean:write name="element4" property="name" /></option> 
		    	</logic:iterate>
		    </logic:notEmpty>	
     	 </select>
        </div></td>
      </tr>
    </table></td>
  </tr>
  
  
  
  
  
  
  <!--**************************** Here is logic for Trading Partner*************-->
		
		
     	<logic:notEmpty property="userID" name="UserForm">
    	<%List ls=(List)request.getAttribute("TradingPartnerNotInsertedList");
		  	request.setAttribute("TradingPartnerList",ls);
		 %>
		</logic:notEmpty>
		
		 	
    	 <logic:notEmpty property="userID" name="UserForm">
    	<%	List ls1=(List)request.getAttribute("TradingPartnerInsertedList");
		  	request.setAttribute("TradingPartnerInsertedList",ls1);
		 %>
		 </logic:notEmpty>
		

  <tr>
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
      <tr class="tableRow_Header">
        <td colspan="3" class="style1">Data Access Controls (Visible only if user is being viewed is an internal User!!! ) </td>
      </tr>
      <tr>
        <td><div align="center">Available Trading partners </div></td>
        <td>&nbsp;</td>
        <td><div align="center">Selected Trading Partners </div></td>
      </tr>
      <tr>
        <td><div align="center">
         <select name="TradingPartners" size="5" multiple style="width: 100px;">  
			 <logic:iterate id="element1" name="TradingPartnerList" scope="request" type="com.rdta.Admin.User.UserData" >	     
		    	<option value="<bean:write name="element1" property="id" />"><bean:write name="element1" property="name" /></option> 
		    </logic:iterate>
   	 	</select>
        </div></td>
        <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td><div align="center">
                  <input type="button" name="Button" value="   Asign   " onClick="moveOptions(this.form.TradingPartners,this.form.tradingPartners );">
              </div></td>
            </tr>
            <tr>
              <td><div align="center">
                  <input type="button" name="Button" value="   Remove   " onClick="moveOptions(this.form.tradingPartners, this.form.TradingPartners);">
              </div></td>
            </tr>
        </table></td>
        
        <td><div align="center">
            <select name="tradingPartners" size="5" multiple style="width: 100px;">   
		 	<logic:notEmpty property="userID" name="UserForm"> 
   		    <logic:iterate id="element7" name="TradingPartnerInsertedList" scope="request" type="com.rdta.Admin.User.UserData" >	     
		    	<option value="<bean:write name="element7" property="id" />"><bean:write name="element7" property="name" /></option> 
		    </logic:iterate>
		  	</logic:notEmpty>
     		</select>
        </div></td>
      </tr>
    </table></td>
  </tr>
  
  
  
  
  
  
  
  
  <!--******************Here Iam using a another method for get inserted and not insert values**-->
     	
     	
		<logic:notEmpty property="userID" name="UserForm">
    	<%
		  	List ls=(List)request.getAttribute("locationGroupNotInsertedList");
		  	request.setAttribute("LocationGroupList",ls);
		 %>
		 
    	</logic:notEmpty>
    		
   	 <logic:notEmpty property="userID" name="UserForm">
    	<%
		  	List ls1=(List)request.getAttribute("locationGroupInsertedList");
		  	request.setAttribute("LocationGroupNotInsertedList",ls1);
		  	
		 %>
		 </logic:notEmpty>
  
  
  
    
  <tr>
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
      <TR class="tableRow_Header">
        <td colspan="3" class="style1">System Locations  (Visible only if user is being viewed is an internal User!!! ) </td>
      </tr>
      <tr>
        <td><div align="center">Available Locations </div></td>
        <td>&nbsp;</td>
        <td><div align="center">Selected Locations </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <select name="BGLocation" size="5" multiple style="width: 100px;">  
				<logic:iterate id="element3" name="LocationGroupList" scope="request" type="com.rdta.Admin.User.UserData" >	     
		    		<option value="<bean:write name="element3" property="id" />"><bean:write name="element3" property="name" /></option> 
		    	</logic:iterate>
		 	</select>
        </div></td>
        <td><table width="100%"  border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td><div align="center">
                  <input type="button" name="Button" value="   Asign   " onClick="moveOptions(this.form.BGLocation, this.form.bgLocation);">
              </div></td>
            </tr>
            <tr>
              <td><div align="center">
                  <input type="button" name="Button" value="   Remove   " onClick="moveOptions(this.form.bgLocation, this.form.BGLocation);">
              </div></td>
            </tr>
        </table></td>
        <td><div align="center">
            <select name="bgLocation" size="5" multiple style="width: 100px;">
   		  		<logic:notEmpty property="userID" name="UserForm"> 
   		    		<logic:iterate id="element6" name="LocationGroupNotInsertedList" scope="request" type="com.rdta.Admin.User.UserData" >	     
		    			<option value="<bean:write name="element6" property="id" />"><bean:write name="element6" property="name" /></option> 
		    		</logic:iterate>
		  		</logic:notEmpty>
     		</select>
        </div></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><div align="center">
      <input name="Button" type="button" class="style1" value="          SAVE          ">
    </div></td>
  </tr>
</table>

</form>
</body>
</html>

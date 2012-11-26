<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/alert.js" type="text/javascript"></script>
<script src="assets/js/community.js" type="text/javascript"></script>	
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
</head>
<body style="background:#FFFFFF; overflow:hidden; width:505px;">
<html:form action="selectedContactsAction" styleId="myform" method="post" onsubmit="return addContacts();">
	<div class="alert_info" style="overflow:hidden; width:505px;">
		<div style="display:none">
     	<logic:present name="count">
	   		<html-el:hidden styleId="count" property="count" value="${requestScope.count}" />
		</logic:present>
        <input type="checkbox" name="glbid" id="glbid"/>
   	</div>
		
	
	  <!-- Navigations -->
	  
	  <div class="form_ap01">
	  	<div class="header">
    		<div class="text">
    			<logic:present name="Mail">
    				<logic:equal name="Mail" value="Gmail">
    					Your Gmail Contacts
    				</logic:equal>
    				<logic:equal name="Mail" value="Yahoo">
    					Your Yahoo Contacts
    				</logic:equal>
    				<logic:equal name="Mail" value="Rediff">
    					Your Rediff Contacts
    				</logic:equal>
    			</logic:present>
    		</div>
		</div>
		<div class="cntnt">
    		<div class="paragraph">Select which contacts to invite from the list below, 
    			<a onclick="javascript:getContacts_Load();" class="link">try another email account</a>
    		</div>
		</div>
	    <!-- FIRST NAME -->
	    <div class="table_alert" style="padding-left:0px;">
	      <table cellpadding="0" cellspacing="0" border="0" width="389">
	      	<tr>
	        	<td colspan="3" class="header">
	                <div class="links">
	                  <div class="select">
	                    <div class="text">select :</div>
	                  </div>
	                  <div class="all">
	                    <a onclick="alertCheck(1);" class="text" id="all_link">all</a>
	                  </div>
	                  <div class="none">
	                    <a onclick="alertCheck(2);" class="text" id="none_link">none</a>
	                  </div>
	                </div>
	            </td>
	        </tr>
			<logic:present name="Contacts">
				<logic:iterate id="cbean" name="Contacts" indexId="a">
					<tr>
					    <td class="text" style="padding-top:5px;">
					     	<input type="checkbox" name='Check' id='Check' value="<bean:write name="cbean" property="contact_email"/>"/>
					    </td>
					    <td class="text" style="padding-top:5px;">
					    	<span class="name">
							    <bean:write name="cbean" property="contact_name"/>
					    	</spam>
					    </td>
					    <td class="text" style="padding-top:5px;">
					    	<span class="name">
							    <bean:write name="cbean" property="contact_email"/>
						    </span>
					    </td>
					</tr>
		        </logic:iterate>
			</logic:present>
		</table>
	</div>
		     <logic:notPresent name="Contacts">
		     	No Contacts Found 
		     </logic:notPresent>
	    </div>
	    <div class="button_acb">
	        <div class="add">
	        	<html:submit>Add to Invite</html:submit>
	        </div>
	        
	        <div class="back">
	        	<input type="button" value="Cancel" onclick="backToInvite();"/>
	        </div>
	    </div>
	  </div>
	<logic:present name="previousContacts">
		<html-el:hidden styleId="prev_contact" property="prev_contact" value="${requestScope.previousContacts}" />
	</logic:present>
	<logic:notPresent name="previousContacts">
		<html-el:hidden styleId="prev_contact" property="prev_contact" value=""/>
	</logic:notPresent>
	<input type="hidden" name="queary" id="queary" value="">
	</html:form>
	<div style="display:none">
		<form id="backToInviteForm" action="./backToInviteAction.ice">
			<input type="hidden" id="backToInvite" name="backToInvite"/>
			<input type="submit"/>
		</form>
		<form id="contactfrm" action="./SelectProvider.ice">
			<logic:present name="previousContacts">
				<html-el:hidden styleId="prev_contact1" property="prev_contact1" value="${requestScope.previousContacts}" />
			</logic:present>
			<logic:notPresent name="previousContacts">
				<html-el:hidden styleId="prev_contact1" property="prev_contact1" value=""/>
			</logic:notPresent>
			<input type="submit"/>
		</form>
		<form id="backToHomeForm" action="./Home_Frame.ice">
			<input type="hidden" id="backToHome" name="backToHome"/>
			<input type="submit"/>
		</form>
	</div>
</body>
</html>

<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired" />
</logic:notPresent>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@page import="com.snipl.ice.utility.UserUtility,java.util.HashMap"%>
<%@page import="com.snipl.ice.config.ICEEnv"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/alert.js" type="text/javascript"></script>
<script src="assets/js/community.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
</head>
<body class="body_controller" style="overflow:hidden;">
<html:form action="selectedContactsAction" styleId="myform" method="post" onsubmit="return addContacts();" style="overflow:hidden;">
<div class="alert_info" style="overflow:hidden; width:515px;">
  <!-- Navigations -->
  
  <div class="form_ap01">
  	<div class="header">
    	<div class="text">Your Friends List</div>
    </div>
    <div class="cntnt">
    	<div class="paragraph">Select which users to invite the list below. or, choose <a onclick="javascript=backToInvite();" class="link">another option</a> to find more friends</div>
    </div>
    <!-- FIRST NAME -->
    <div class="table_alert" style="padding-left:0px;" style="overflow:hidden;">
      <table cellpadding="0" cellspacing="0" border="0" width="389" style="overflow:hidden;">
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
                <div style="display:none">
                	<logic:present name="prev_contact_friend">
						<html-el:hidden styleId="prev_contact" property="prev_contact" value="${requestScope.prev_contact_friend}" />
					</logic:present>
					<logic:notPresent name="prev_contact_friend">
						<html-el:hidden styleId="prev_contact" property="prev_contact" value=""/>
					</logic:notPresent>
     				<logic:present name="count">
          				<html-el:hidden styleId="count" property="count" value="${requestScope.count}" />
        			</logic:present>
        			<input type="checkbox" name="glbid" id="glbid"/>
   				</div>
            </td>
        </tr>
        
        <logic:present name="icemembers">
			<html:form action="CheckForm.ice" styleId="myform" style="overflow:hidden;">
					<logic:iterate id="icebean" name="icemembers" indexId="a">
						<tr>
							<td class="text" style="padding-top:5px;">
								<input type="checkbox" name="Check" id="Check" value="<bean:write name="icebean" property="contactemail"/>"/>
							</td>
							<td class="text" style="padding-top:5px;">
								<span class="name"><bean:write name="icebean" property="contactname"/></span>
							</td>
          					<td class="text" style="padding-top:5px;">
          						<span class="email"><bean:write name="icebean" property="contactemail"/></span>
          					</td>
						</tr>
					</logic:iterate>
			</html:form>							
		</logic:present>
        <logic:present name="community" >
			<html:form action="CheckForm.ice" styleId="myform">
					<logic:iterate id="commbean" name="community" indexId="b">
						<tr>
							<td class="text" style="padding-top:5px;">
								<input type="checkbox" name="Check" id="Check" value="<bean:write name="commbean" property="contactemail"/>"/>
							</td>
							<td class="text" style="padding-top:5px;">
								<span class="name"><bean:write name="commbean" property="contactname"/></span>
							</td>
          					<td class="text" style="padding-top:5px;">
          						<span class="email"><bean:write name="commbean" property="contactemail"/></span>
          					</td>
						</tr>
					</logic:iterate>
			</html:form>							
		</logic:present>
     </table>
    </div>
    <div class="button_acb">
    	<logic:present name="community" >
	        <div class="add">
	            <html:submit>Add to Invite</html:submit>
	        </div>
	    </logic:present>
	    <logic:present name="icemembers" >
	        <div class="add">
	            <html:submit>Add to Invite</html:submit>
	        </div>
	    </logic:present>
        <div class="clear">
            <input type="button" value="Cancel" onclick="backToInvite();" />
        </div>
    </div>
  </div>
  <input type="hidden" name="queary" id="queary" value=""></input>
  </div>
  </html:form>
  <div style="display:none">
		<form id="gobackfrm" action="./InviteCommunity.ice">
			<input type="submit"/>
		</form>
  </div>
  <div style="display:none">
	<form id="backToInviteForm" action="./backToInviteAction.ice">
		<input type="hidden" id="backToInvite" name="backToInvite"/>
		<input type="submit"/>
	</form>
  </div>
</body>
</html>

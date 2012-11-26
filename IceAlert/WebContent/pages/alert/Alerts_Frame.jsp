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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="alert_info">
  <!-- Navigations -->
  <div class="alert_nav_margin">
    <div class="alert_nav">
      <div class="recieved">
      	<html:link page="/ViewReceivedAlertsDB.ice" styleClass="text_active" target="_parent" styleId="r_link">Received</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
      	<html:link page="/ViewSentAlertDB.ice" styleClass="text" styleId="s_link">Sent</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
      	<html:link page="/ViewReceivedInviteDB.ice" styleClass="text" styleId="i_link">Invitations</html:link>
      </div>
    </div>
  <div class="form_alert">
    <div class="button_c">
      <logic:present name="count">
 			<logic:equal name="count" value="0">
 				<html:button property="delIce" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Delete</html:button>
 			</logic:equal>
 			<logic:notEqual name="count" value="0">
 				<logic:present name="rinvitelist">
 					<html:button property="delIce" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Delete</html:button>
 				</logic:present>
 				<logic:notPresent name="rinvitelist">
 					<html:button property="delIce" onclick="javascript:alter_del()">Delete</html:button>
 				</logic:notPresent>
 			</logic:notEqual>
	</logic:present>
    </div>
    <div class="button_b">
      	<input type="button" value="Community Alert" onclick = "communityalert();"/>
    </div>
    <div class="button_a">
      	<input type="button" value="Ice Alert" onclick = "icealert();"/>
    </div>
  </div>
  </div>
  <div class="form_ap01">
    <!-- FIRST NAME -->
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
     	<logic:present name="count">
          	<html-el:hidden styleId="count" property="count" value="${requestScope.count}" />
        </logic:present>
        <input type="checkbox" name="glbid" id="glbid"/>
   	</div>
    <div class="table_alert" style="padding-left:0px;">
    	<logic:present name="ralertlistflag"> 
			<div class="body">No Records Found</div>
			<script>
       			highlightAlert(1);
       		</script>
		</logic:present>
		<logic:present name="salertlistflag"> 
			<div class="body">No Records Found</div>
			<script>
       			highlightAlert(2);
       		</script>
		</logic:present>
		<logic:present name="rinvitelistflag"> 
			<div class="body">No Records Found</div>
			<script>
       			highlightAlert(3);
       		</script>
		</logic:present>
      <table cellpadding="0" cellspacing="0" border="0" width="389">
         <% if(request.getAttribute("ralertlist")!=null){ %>
         	<script>
         		highlightAlert(1);
         	</script>	
           	<logic:present name="ralertlist" >
			<html:form action="CheckForm.ice" styleId="myform">
			<input type="hidden" id="method_dummy" name="method_dummy" value="received"/> 
				<logic:iterate id="abean" name="ralertlist" indexId="a">
					<logic:equal value="0" name="abean" property="flag" >
						<tr style="cursor:pointer;">
							<td class="text" style="padding-top:5px;">
								<b><input type="checkbox" name="Check" id="Check" value="<bean:write name="abean" property="alert_id"/>"/></b>
							</td>
							<td class="text" style="padding-top:5px;" ('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><b><html-el:img src="${requestScope.ralertlist[a].alert_imgurl}" /></b></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getrid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><b><bean:write name="abean" property="sender_name"/></b></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getrid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><b><bean:write name="abean" property="subject"/></b></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getrid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><b><bean:write name="abean" property="date"/></b></span>
							</td>
						</tr>
					</logic:equal>
					<logic:notEqual value="0" name="abean" property="flag" >
						<tr style="cursor:pointer;">
							<td class="text" style="padding-top:5px;">
								<input type="checkbox" name="Check" id="Check" value="<bean:write name="abean" property="alert_id"/>"/>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getrid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><html-el:img src="${requestScope.ralertlist[a].alert_imgurl}" /></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getrid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><bean:write name="abean" property="sender_name"/></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getrid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><bean:write name="abean" property="subject"/></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getrid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><bean:write name="abean" property="date"/></span>
							</td>
						</tr>
					</logic:notEqual>
				</logic:iterate>
				</html:form>							
				<div class="prev_next">
					<html:form action="ViewReceivedAlertsDB_Frame.ice">
					<logic:present name="previous">
						<logic:notEqual name="previous" value="0">
							<div class="prev">
								<html-el:hidden property="alert_pre" value="${requestScope.previous}" />
								<html:submit property="previous" styleClass="nav_but">« Previous</html:submit>
							</div>
						</logic:notEqual>
					</logic:present>													
					<logic:present name="next">
						<logic:notEqual name="next" value="0">
							<div class="next">
				    			<html-el:hidden property="alert_next" value="${requestScope.next}" />
				    			<html:submit property="next" styleClass="text" styleClass="nav_but">Next »</html:submit>
				    		</div>												    			
						</logic:notEqual>
					</logic:present>
					</html:form>	
            	</div>
			</logic:present>
			<%} %>	
			<% if(request.getAttribute("salertlist")!=null){ %>
				<script>
         			highlightAlert(2);
         		</script>
				<logic:present name="salertlist" >
				<html:form action="CheckForm.ice" styleId="myform">
				<input type="hidden" id="method_dummy" name="method_dummy" value="sented"/> 
					<logic:iterate id="abean" name="salertlist" indexId="a">
						<tr style="cursor:pointer;">
							<td class="text" style="padding-top:5px;">
								<input type="checkbox" name="Check" id="Check" value="<bean:write name="abean" property="alert_id"/>"/>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getsid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><html-el:img src="${requestScope.salertlist[a].alert_imgurl}" /></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getsid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><bean:write name="abean" property="receiver_name"/></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getsid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><bean:write name="abean" property="subject"/></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getsid('<bean:write name="abean" property="alert_id"/>')">
								<span class="name"><bean:write name="abean" property="date"/></span>
							</td>
						</tr>
					</logic:iterate>
					</html:form>
					<div class="prev_next">
						<html:form action="ViewSentAlertDB.ice">
						<logic:present name="previous">
							<logic:notEqual name="previous" value="0">
								<div class="prev">
									<html-el:hidden property="alert_pre" value="${requestScope.previous}" />
									<div class="text"><html:submit property="previous"  styleClass="nav_but">« Previous</html:submit></div>
								</div>
							</logic:notEqual>
						</logic:present>													
						<logic:present name="next">
							<logic:notEqual name="next" value="0">
								<div class="next">
					    			<html-el:hidden property="alert_next" value="${requestScope.next}" />
					    			<div class="text"><html:submit property="next"  styleClass="nav_but">Next »</html:submit></div>
					    		</div>												    			
							</logic:notEqual>
						</logic:present>
						</html:form>	
	            </div>	
				</logic:present>
			<%} %>
			<% if(request.getAttribute("rinvitelist")!=null){ %>
				<script>
	         		highlightAlert(3);
	         	</script>
				<logic:present name="rinvitelist" >
				<html:form action="CheckForm.ice" styleId="myform">
				<input type="hidden" id="method_dummy" name="method_dummy" value="invited"/> 
					<logic:iterate id="ibean" name="rinvitelist" indexId="a">
						<tr style="cursor:pointer;">
							<td class="text" style="padding-top:5px;">
								<input type="checkbox" name="Check" id="Check" value="<bean:write name="ibean" property="owner_id"/>"/>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getiid('<bean:write name="ibean" property="community_id"/>')">
								<span class="name"><bean:write name="ibean" property="ownername"/></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getiid('<bean:write name="ibean" property="community_id"/>')">
								<span class="name"><bean:write name="ibean" property="community_name"/></span>
							</td>
							<td class="text" style="padding-top:5px;" onclick="getiid('<bean:write name="ibean" property="community_id"/>')">
								<span class="name"><bean:write name="ibean" property="date"/></span>
							</td>
						</tr>
					</logic:iterate>
					</html:form>
					<div class="prev_next">
						<html:form action="ViewReceivedInviteDB.ice">
						<logic:present name="previous">
							<logic:notEqual name="previous" value="0">
								<div class="prev">
									<html-el:hidden property="alert_pre" value="${requestScope.previous}" />
									<div class="text"><html:submit property="previous"  styleClass="nav_but">« Previous</html:submit></div>
								</div>
							</logic:notEqual>
						</logic:present>													
						<logic:present name="next">
							<logic:notEqual name="next" value="0">
								<div class="next">
					    			<html-el:hidden property="alert_next" value="${requestScope.next}" />
					    			<div class="text"><html:submit property="next"  styleClass="nav_but">Next »</html:submit></div>
					    		</div>												    			
							</logic:notEqual>
						</logic:present>
						</html:form>	
                </div>		
				</logic:present>
			<%} %>
      </table>
    </div>
  </div>
</div>
<div style="display:none">
	<form id="myform" name action="./Home.ice">
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="viewrfrm" action="./ViewRalertDB.ice" method="post">
		<input type="hidden" id="ralert" name="ralert"/>
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="viewsfrm" action="./ViewSalertDB.ice" method="post">
		<input type="hidden" id="salert" name="salert"/>
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="viewifrm" action="./ViewRinviteDB.ice" method="post">
		<input type="hidden" id="rinvite" name="rinvite"/>
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="deleterfrm" action="./DeleteRalertDB.ice" method="post">
		<input type="hidden" id="rdelinfo" name="rdelinfo"/>
		<input type="hidden" id="method" name="method"/>
		<input type="submit"/>
	</form>
</div>

<div style="display:none">
	<form id="communityfrm" action="./Community.ice?alert=true" method="post" target="_parent">
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="icefrm" action="./ListIce.ice" method="post" target="_parent">
		<input type="submit"/>
	</form>
</div>

</body>
</html>

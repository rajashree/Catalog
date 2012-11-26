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
<script src="assets/js/sms.js" type="text/javascript"></script>
<script src="assets/js/alert.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<script src="assets/js/utils.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="alert_info">
  <!-- Navigations -->
  <div class="alert_nav_margin">
    <div class="alert_nav">
      <div class="recieved"> 
      	<html:link page="/ViewReceivedAlertsDB.ice" styleClass="text" target="_parent">Received</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
        <html:link page="/ViewSentAlertDB.ice" styleClass="text">Sent</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
        <html:link page="/ViewReceivedInviteDB.ice" styleClass="text">Invitations</html:link> 
      </div>
    </div>
	<div class="form_alert">
    	<logic:present name="rinviteinfo">
    		<div class="button_c">
    			<html:button property="delIce" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Delete</html:button>
    		</div>
    	</logic:present>
    	<logic:notPresent name="rinviteinfo">
    		<div class="button_c">
    			<html:button property="delIce" onclick="javascript:alert_del()">Delete</html:button>
    		</div>
    	</logic:notPresent>
      	<div class="button_b">
	      <input type="button" value="Community Alert" onclick = "communityalert();"/>
	    </div>
	    <div class="button_a">
	      <input type="button" value="Ice Alert" onclick = "icealert();"/>
	    </div>
    </div>
  </div>
  
  <div class="form_ap02">
  
<!-- Received Alert Info  -->

  	<% if(request.getAttribute("ralertdetails")!=null){ %>
    	<input type="hidden" id="method_dummy" name="method_dummy" value="ralert"/>
        <input type="hidden" id="id_dummy" name="id_dummy" value="<bean:write name="ralertdetails" property="alert_id"/>"/>
		<logic:notPresent name="ralertdetails"> 
			<div class="body">No Records Found</div>
		</logic:notPresent>
		<logic:present name="ralertdetails" >
        	<div class="from_set">
             	<div class="from">
                	<div class="text">from :</div>
               	</div>
                <div class="fr_name">
                	<div class="text"><bean:write name="ralertdetails" property="sender_name"/></div>
             	</div>
	       	</div>
    		<div class="to_set">
	         	<div class="to">
 	            	<div class="text">to :</div>
     	       	</div>
         	    <div class="mail_id">
             		<div class="text"><bean:write name="ralertdetails" property="receiver_name"/></div>
                </div>
          	</div>
            <div class="date_set">
             	<div class="date">
                 	<div class="text">date :</div>
               	</div>
                <div class="date_number">
                 	<div class="text"><bean:write name="ralertdetails" property="datetime"/></div>
               	</div>
         	</div>
            <div class="mail_set">
            	<div class="mailed_by">
              		<div class="text">mailed by :</div>
               	</div>
                <div class="mailer_name">
                	<div class="text">Ice Alert</div>
               	</div>
           	</div>
            <div class="subject_set">
            	<div class="subject">
                	<div class="text">subject :</div>
               	</div>
                <div class="sub_text">
                	<div class="text"><bean:write name="ralertdetails" property="subject"/></div>
               	</div>
          	</div>
            <div style="padding:20px 0px 0px 0px; font-family:'trebuchet MS'; font-size:12px; font-weight:normal; text-align:left; text-decoration:none; text-transform:none; color:#00688b; width:400px; height:100px;">
            	<bean:write name="ralertdetails" property="body"/>
            </div>
		</logic:present>
	<%} %>
	
	<!-- Sent Alert Info  -->
	
	<% if(request.getAttribute("salertdetails")!=null){ %>
		<input type="hidden" id="method_dummy" name="method_dummy" value="salert"/>
		<input type="hidden" id="id_dummy" name="id_dummy" value="<bean:write name="salertdetails" property="alert_id"/>"/>
			<logic:notPresent name="salertdetails"> 
				<div class="body">No Records Found</div>
			</logic:notPresent>
			<logic:present name="salertdetails" >
          		<div class="from_set">
                	<div class="from">
                    	<div class="text">from :</div>
                   	</div>
                    <div class="fr_name">
                    	<div class="text"><bean:write name="salertdetails" property="sender_name"/></div>
                    </div>
	           	</div>
    			<div class="to_set">
	            	<div class="to">
 	                	<div class="text">to :</div>
     	           	</div>
         	        <div class="mail_id">
             	    	<div class="text"><bean:write name="salertdetails" property="receiver_name"/></div>
                 	</div>
             	</div>
                <div class="date_set">
                	<div class="date">
                    	<div class="text">date :</div>
                   	</div>
                    <div class="date_number">
                    	<div class="text"><bean:write name="salertdetails" property="datetime"/></div>
                   	</div>
               	</div>
                <div class="mail_set">
                	<div class="mailed_by">
                    	<div class="text">mailed by :</div>
                   	</div>
                    <div class="mailer_name">
                    	<div class="text">Ice Alert</div>
                   	</div>
               	</div>
                <div class="subject_set">
                	<div class="subject">
                		<div class="text">subject :</div>
                  	</div>
                    <div class="sub_text">
                    	<div class="text"><bean:write name="salertdetails" property="subject"/></div>
                   	</div>
              	</div>
                <div style="padding:20px 0px 0px 0px; font-family:'trebuchet MS'; font-size:12px; font-weight:normal; text-align:left; text-decoration:none; text-transform:none; color:#00688b; width:400px; height:100px;">
                	<bean:write name="salertdetails" property="body"/>
               	</div>
			</logic:present>
		<%} %>
		
		<!-- Invitations Info  -->
		
		<% if(request.getAttribute("rinviteinfo")!=null){ %>
			<logic:notPresent name="rinviteinfo">
				<div class="body">No Records Found</div>
			</logic:notPresent>
			<logic:present name="rinviteinfo" >
            	<div class="from_set">
                	<div class="from">
                    	<div class="text">from :</div>
                   	</div>
                    <div class="fr_name">
                    	<div class="text"><bean:write name="rinviteinfo" property="ownername"/></div>
                   	</div>
	         	</div>
                <div class="date_set">
                	<div class="date">
                		<div class="text">date :</div>
                  	</div>
                    <div class="date_number">
                    	<div class="text"><bean:write name="rinviteinfo" property="datetime"/></div>
                  	</div>
               	</div>
                <div class="mail_set">
                	<div class="mailed_by">
                    	<div class="text">mailed by :</div>
                  	</div>
                  	<div class="mailer_name">
                   		<div class="text">Ice Alert</div>
                   	</div>
              	</div>
                <div class="subject_set">
                 	<div class="subject">
                    	<div class="text">subject :</div>
                   	</div>
                	<div class="sub_text">
                    	<div class="text">Invitation to join <bean:write name="rinviteinfo" property="community_name"/></div>
                   	</div>
              	</div>
                <div style="padding:20px 0px 0px 0px; font-family:'trebuchet MS'; font-size:12px; font-weight:normal; text-align:left; text-decoration:none; text-transform:none; color:#00688b; width:400px; height:100px;">
                	<b><bean:write name="rinviteinfo" property="ownername"/></b> has invited you to join <b><bean:write name="rinviteinfo" property="community_name"/></b>
                	<br/>To join this community click on the button 
                	<div class="button">
                		<form action="./Communityjoin.ice" target="_parent">
                			<input type="hidden" name="communityid" value="<bean:write name="rinviteinfo" property="community_id"/>"/>
                			<input type="submit" value="Click Here" style="background-color:#00688B;border:medium none;color:#FFFFFF;cursor:pointer;display:block;font-family:Arial,Helvetica,sans-serif;font-size:10px;font-weight:bold;height:19px;overflow:hidden;width:70px;"/>
                		</form>
              		</div>
               	</div>
			</logic:present>
		<%} %>
   	</div>
</div>
<div style="display:none">
	<form id="deleteafrm" action="./DeleteAlertinfo.ice" method="post">
		<input type="hidden" id="adelinfo" name="adelinfo"/>
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

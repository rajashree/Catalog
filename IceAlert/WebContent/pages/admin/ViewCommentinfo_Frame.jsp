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
<script src="assets/js/sendalert.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<script src="assets/js/utils.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<!-- Navigation -->
	<div class="alert_nav_margin">
		<div class="alert_nav">
			<div class="recieved">
				<html:link page="/SendAlert_Frame.ice" styleClass="text" styleId="sendalert_link" property="sendalert_link">send alert</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
			<html:link page="/FeedbacksDB.ice" styleClass="text" styleId="feedbacks_link" property="feedbacks_link">feedbacks</html:link>&nbsp;&nbsp;|&nbsp;&nbsp;
			<html:link page="/BugsDB.ice" styleClass="text" styleId="bugs_link" property="bugs_link">bugs</html:link>
			</div>
		</div>

		<div class="form_alert">
			<div class="button_c">
				<html:button property="delIce" onclick="javascript:alert_del()">Delete</html:button>
			</div>
		</div>
	</div>


<!-- Feedback-->
<% if(request.getAttribute("feedbackdetails")!=null){ %>
	<div class="alert_info" id="feedbackdiv" name="feedbackdiv">
		<input type="hidden" id="method_dummy" name="method_dummy" value="feedback"/>
        <input type="hidden" id="id_dummy" name="id_dummy" value="<bean:write name="feedbackdetails" property="feedbackid"/>"/>
		<logic:notPresent name="feedbackdetails"> 
			<div class="body">No Records Found</div>
		</logic:notPresent>
		<logic:present name="feedbackdetails">
			<div class="form_ap02">
				<div class="from_set">
					<div class="from">
						<div class="text">user name</div>
					</div>
					<div class="fr_name">
						<div class="text">:&nbsp;&nbsp;&nbsp; <bean:write name="feedbackdetails" property="username"/></div>
					</div>
				</div>
				<div class="to_set">
					<div class="to">
						<div class="text">comment </div>
					</div>
					<div class="comment">
						<div class="colon">:</div>
						<div class="div">
							<textarea class="text_area" rows="10" readonly="readonly"><bean:write name="feedbackdetails" property="comment"/></textarea>
						</div>
					</div>
				</div>
			</div>
		</logic:present>
	</div>
<%} %>

<!-- Bugs-->
<% if(request.getAttribute("bugdetails")!=null){ %>
	<div class="alert_info" id="bugdiv" name="bugdiv">
		<input type="hidden" id="method_dummy" name="method_dummy" value="bug"/>
        <input type="hidden" id="id_dummy" name="id_dummy" value="<bean:write name="bugdetails" property="bugid"/>"/>
		<logic:notPresent name="bugdetails"> 
			<div class="body">No Records Found</div>
		</logic:notPresent>
		<logic:present name="bugdetails">
			<div class="bugs_list">
				<div class="from_set">
					<div class="from">
						<div class="text">user name</div>
					</div>
					<div class="fr_name">
						<div class="text">:&nbsp;&nbsp;&nbsp; <bean:write name="bugdetails" property="username"/></div>
					</div>
				</div>
				<div class="from_set">
					<div class="from">
						<div class="text">browser type</div>
					</div>
					<div class="fr_name">
						<div class="text">:&nbsp;&nbsp;&nbsp; <bean:write name="bugdetails" property="browsertype"/></div>
					</div>
				</div>
				<div class="from_set">
					<div class="from">
						<div class="text">category</div>
					</div>
					<div class="fr_name">
						<div class="text">:&nbsp;&nbsp;&nbsp; <bean:write name="bugdetails" property="category"/></div>
					</div>
				</div>
				<div class="to_set">
					<div class="to">
        				<div class="text">comment </div>
					</div>
					<div class="comment">
						<div class="colon">:</div>
						<div class="div">
							<textarea class="text_area" rows="8" readonly="readonly"><bean:write name="bugdetails" property="comment"/></textarea>
						</div>
					</div>
				</div>
				<logic:notEqual value="1" name="bugdetails" property="status">
					<div style="font-family:'trebuchet MS'; font-size:12px; font-weight:normal; text-align:left; text-decoration:none; text-transform:none; color:#00688b;">
                		<br/>If the bug has been fixed click on the button 
                		<div class="button">
                			<form action="./BugFixed.ice">
                				<input type="hidden" id="bugid" name="bugid" value="<bean:write name="bugdetails" property="bugid"/>"/>
                				<input type="submit" value="Click Here" style="background-color:#00688B;border:medium none;color:#FFFFFF;cursor:pointer;display:block;font-family:Arial,Helvetica,sans-serif;font-size:10px;font-weight:bold;height:19px;overflow:hidden;width:70px;"/>
                			</form>
              			</div>
               		</div>
               	</logic:notEqual>
			</div>
		</logic:present>
	</div>
<%} %>
<div style="display:none">
	<form id="deletecfrm" action="./DeleteCommentinfo.ice" method="post">
		<input type="hidden" id="cdelinfo" name="cdelinfo"/>
		<input type="hidden" id="method" name="method"/>
		<input type="submit"/>
	</form>
</div>
<logic:present name="ret_flag">
	<logic:equal value="feedback" name="ret_flag">
		<script>
			viewactivatediv(0);
		</script>		
	</logic:equal>
	<logic:equal value="bugs" name="ret_flag">
		<script>
			viewactivatediv(1);
		</script>
	</logic:equal>
</logic:present>
</body>
</html>

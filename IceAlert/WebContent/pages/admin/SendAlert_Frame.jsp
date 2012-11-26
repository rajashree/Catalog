<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired_Frame" />
</logic:notPresent>
<logic:present name="security_profile">
   	<logic:notEqual name="security_profile" value="0">
   		<logic:forward name="sessionExpaired_Frame" />
   	</logic:notEqual>
</logic:present>
<%
	session.setAttribute("security_prev","1");	
%>

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
<body style="background:#FFFFFF; width:507px; overflow:hidden;">
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
				<logic:present name="count">
 					<logic:equal name="count" value="0">
 						<html:button property="delIce" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Delete</html:button>
 					</logic:equal>
 					<logic:notEqual name="count" value="0">
 						<logic:present name="feedbacklist">
							<html:button property="delIce" onclick="javascript:admin_del()">Delete</html:button>
 						</logic:present>
 						<logic:present name="buglist">
 							<html:button property="delIce" onclick="javascript:admin_del()">Delete</html:button>
 						</logic:present>
 					</logic:notEqual>
				</logic:present>
			</div>
		</div>
	</div>
<div class="communities" style="overflow:hidden; width:505px;" id="sendalert_div" name="sendalert_div">
	
	<div class="form_send_alert">
		
  		<div id="errormsg">
  			<div id="feedback-content"></div>
  		</div>
    	<!-- Form -->
    	<div class="sb" style="padding-left:20px;">
      	<div class="search_by">
	        <div class="text">search by :</div>
      	</div>
      	<div class="t_box">
      		<select class="combo_box" name="search" id="search" width="25" onchange= "displayContent();">
				<option value="none">None</option>
				<option value="all">All</option>
				<option value="community">Community</option>
				<option value="name">Member</option>
				<option value="mobile">Mobile Number</option>
				<option value="email">Email</option>
				<option value="country">Country</option>
				<option value="occupation">Occupation</option>
			</select>
      	</div>
      	<div class="dynamic">
      		<form action="./SearchAlertDB.ice" method="post" onsubmit="return validateForm();" style="overflow: hidden;">
      			<logic:notPresent name="hidden">
					<input type="hidden" name="hidden" id="hidden" value=""/>
				</logic:notPresent>
				<logic:present name="hidden">
					<html-el:hidden styleId="hidden" property="hidden" value="${requestScope.hidden}"/>
				</logic:present>
				<div id="global"  style="overflow: hidden;">
					<div class="enter_name" id="searchnamediv">
            			<div class="text">
            				<logic:present name="hidden">
            					<logic:equal name="hidden" value="2">
            						Enter Community Name :
            					</logic:equal>
            					<logic:equal name="hidden" value="3">
            						Enter Name :
            					</logic:equal>
            					<logic:equal name="hidden" value="4">
            						Enter Mobile Number :
            					</logic:equal>
            					<logic:equal name="hidden" value="5">
            						Enter Email ID :
            					</logic:equal>
            					<logic:equal name="hidden" value="6">
            						Select a Country :
            					</logic:equal>
            					<logic:equal name="hidden" value="7">
            						Select Occupation :
            					</logic:equal>
            				</logic:present>   		
            			</div>
            		</div>            
            		<div class="t_box_en" id="searchphasediv">
            			<logic:notPresent name="Com_Occlist">
	            			<input type="text" class="text_box" id="searchphase" name="searchphase" value=""/>
            			</logic:notPresent>
            			<logic:present name="Com_Occlist">
            				<select id="searchphase" name="searchphase" class="combobox">
            					<logic:iterate id="COBean" name="Com_Occlist">
            						<option value="<bean:write name="COBean" property="value"/>">
            							<bean:write name="COBean" property="name"/>
            						</option>
            					</logic:iterate>
            				</select>
            			</logic:present>
            		</div>
            		<div class="button" id="searchbuttondiv">
            			<input type="submit" value="Search" />
            		</div>
   				</div>
   			</form>
      	</div>
 	</div>
    <div style="width:300px;">
  		<form  id="sendalertonreg" action="./SendAdminAlert.ice" method="post" onsubmit="return validateSMS();">
	    	<div class="sub">
	      		<div class="subject">
	          		<div class="text">subject :</div>
	          	</div>
	          	<div class="t_box">
	          		<input type="text" class="text_box" id="smssub" name="smssub"/>
	          	</div>                                            
	      	</div>
		  	<div class="ms">
	     		<div class="message">
	         		<div class="text">message :</div>
	         	</div>
	         	<div class="t_box">
	         		<textarea class="text_area" id="smsdata" name="smsdata"></textarea>
	         	</div>                                            
	     	</div>
         	<div class="cb">
         		<div class="check_box">
             		<input type="checkbox" name="sendice" id="sendice"/>
             	</div>
             	<div class="message">
             		<div class="text">Send Message to ICE Members also</div>
             	</div>                                                                                        
         	</div>
         	<div class="send">
           		<input type="hidden" name="msghidden" id="msghidden" value=""/>
				<input type="hidden" name="cnt" id="cnt" value=""/>
				<input type="hidden" name="stdata" id="stdata" value=""/>
        		<div class="button">                                        		
            		<input type="submit" value="Send" />
            	</div>                                                      
        	</div>
   		</form>
	</div>
	<div class="table" id="datacont"> 
    	<form name="myform" id="myform">
 			<input type="hidden" name="count" id="count" value=""/>
 				<logic:present name="slist">
 					<logic:present name="hidden">
 						<table cellpadding='0' cellspacing='0' border='0' width='100%'>
 							<logic:equal name="hidden" value="2">
   								<tr>
   									<td class='heading'>
   										<input type='checkbox' name='glbid' id='glbid' onclick='selecteveryalert();'/>
   									</td>
   									<td class='heading'>
   										<span class='header'>name</span>
   									</td>
   									<td class='heading'>
   										<span class='header'>number of members</span>
   									</td>
   									<td class='heading'>
   										<span class='header'>description</span>
   									</td>
   								</tr>
   							</logic:equal>
   							<logic:notEqual name="hidden" value="2">
   								<tr>
   									<td class='heading'>
   										<input type='checkbox' name='glbid' id='glbid' onclick='selecteveryalert();'/>
   									</td>
   									<td class='heading'>
   										<span class='header'>name</span>
   									</td>
   									<td class='heading'>
   										<span class='header'>E-Mail Id</span>
   									</td>
   									<td class='heading'>
   										<span class='header'>Mobile Number</span>
  									</td>
  								</tr>
  							</logic:notEqual>
   							<logic:iterate id="abean" name="slist" indexId="a">
   								<logic:equal name="hidden" value="2">
   									<tr>
   										<td class='text' style='padding-top:5px;'>
   											<input type='checkbox' name='Check' id='Check' value='<bean:write name="abean" property="id"/>'/>
   										</td>
   										<td class='text' style='padding-top:5px;'>
   											<span class='name'>
   												<bean:write name="abean" property="name"/>
   											</span>
   										</td>
   										<td class='text' style='padding-top:5px;'>
   											<span class='cno'>
   												<bean:write name="abean" property="no_users"/> Members
  											</span>
  										</td>
  										<td class='text' style='padding-top:5px;'>
  											<span class='email'>
  												<bean:write name="abean" property="description"/>
  											</span>
  										</td>
  									</tr>
   								</logic:equal>
   								<logic:notEqual name="hidden" value="2">
   									<tr>
   										<td class='text' style='padding-top:5px;'>
   											<input type='checkbox' name='Check' id='Check' value="<bean:write name="abean" property="id"/>"/>
   										</td>
   										<td class='text' style='padding-top:5px;'>
   											<span class='name'>
   												<bean:write name="abean" property="fst_Name"/> <bean:write name="abean" property="lst_Name"/>
    										</span>
    									</td>
    									<td class='text' style='padding-top:5px;'>
    										<span class='cno'>
    											<bean:write name="abean" property="email"/>
   											</span>
   										</td>
   										<td class='text' style='padding-top:5px;'>
   											<span class='email'>
   												<bean:write name="abean" property="mobile"/>
   											</span>
   										</td>
   									</tr>     
   								</logic:notEqual>
   							</logic:iterate>
  						</table>
 					</logic:present>
 					<script>
 						initAlert(<bean:write name="hidden"/>,<bean:write name="count"/>,'<bean:write name="searchphase"/>')
 					</script>
 				</logic:present>
 				<logic:notPresent name="slist">
 					<logic:present name="dummy">
 						<div class="body">No Records found with that Phrase</div>
 					</logic:present>
 					<script>initAlert(0,0,0)</script>
 				</logic:notPresent>
 			</form> 
  		</div>
	</div>
</div>

<!--Feeback -->
	<div class="alert_info" style="overflow:hidden; width:505px;" id="feedbackdiv" name="feedbackdiv">
	<logic:present name="ret_flag">
		<logic:equal value="feedback" name="ret_flag">
		<div class="form_ap01">
		
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
				<table cellpadding="0" cellspacing="0" border="0" width="389">
					<logic:notPresent name="feedbacklist">
						No Records Found
					</logic:notPresent> 
					<logic:present name="feedbacklist" >
						<html:form action="CheckForm.ice" styleId="fromone">
						<input type="hidden" id="method_dummy" name="method_dummy" value="feedback"/> 
						<logic:iterate id="fbean" name="feedbacklist" indexId="f">
							<logic:equal value="0" name="fbean" property="status" >
								<tr style="cursor:pointer;">
									<td class="text" style="padding-top:5px;">
										<b><input type="checkbox" name="Check" id="Check" value="<bean:write name="fbean" property="feedbackid"/>"/></b>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getfid('<bean:write name="fbean" property="feedbackid"/>')">
										<span class="name"><b><bean:write name="fbean" property="username"/></b></span>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getfid('<bean:write name="fbean" property="feedbackid"/>')">
										<span class="name"><b><bean:write name="fbean" property="comment"/></b></span>
									</td>
								</tr>
							</logic:equal>
							<logic:notEqual value="0" name="fbean" property="status" >
								<tr style="cursor:pointer;">
									<td class="text" style="padding-top:5px;">
										<input type="checkbox" name="Check" id="Check" value="<bean:write name="fbean" property="feedbackid"/>"/>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getfid('<bean:write name="fbean" property="feedbackid"/>')">
										<span class="name"><bean:write name="fbean" property="username"/></span>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getfid('<bean:write name="fbean" property="feedbackid"/>')">
										<span class="name"><bean:write name="fbean" property="comment"/></span>
									</td>
								</tr>
							</logic:notEqual>
						</logic:iterate>
						</html:form>
					</logic:present>
				</table>
			</div>
			<div class="prev_next">
				<div class="prev">
					<logic:present name="previous">
						<logic:notEqual name="previous" value="0">
							<html:link page="/FeedbacksDB.ice" styleClass="text"  paramId="previous" paramName="previous">
								« previous
							</html:link>
						</logic:notEqual>
					</logic:present>
				</div>
				<div class="next">
					<logic:present name="next">
						<logic:notEqual name="next" value="0">
							<html:link page="/FeedbacksDB.ice" styleClass="text"  paramId="next" paramName="next">
								next » 
							</html:link>
						</logic:notEqual>
					</logic:present>
				</div>
			</div>
		</div>
		</logic:equal>
	</logic:present>
</div>
<!-- Feedback End -->
<!--Bugs -->
	<div class="alert_info" style="overflow:hidden; width:505px;" id="bugdiv" name="bugdiv">
	<logic:present name="ret_flag">
		<logic:equal value="bugs" name="ret_flag">
		<div class="form_ap01">
				
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
				<table cellpadding="0" cellspacing="0" border="0" width="389">
					<logic:notPresent name="buglist">
						No Records Found
					</logic:notPresent> 
					<logic:present name="buglist" >
						<html:form action="CheckForm.ice" styleId="fromone">
						<input type="hidden" id="method_dummy" name="method_dummy" value="bug"/> 
						<logic:iterate id="bbean" name="buglist" indexId="f">
							<logic:equal value="0" name="bbean" property="status" >
								<tr style="cursor:pointer;">
									<td class="text" style="padding-top:5px;">
										<b><input type="checkbox" name="Check" id="Check" value="<bean:write name="bbean" property="bugid"/>"/></b>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getbid('<bean:write name="bbean" property="bugid"/>')">
										<span class="name"><b><bean:write name="bbean" property="username"/></b></span>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getbid('<bean:write name="bbean" property="bugid"/>')">
										<span class="name"><b><bean:write name="bbean" property="comment"/></b></span>
									</td>
								</tr>
							</logic:equal>
							<logic:notEqual value="0" name="bbean" property="status" >
								<tr style="cursor:pointer;">
									<td class="text" style="padding-top:5px;">
										<input type="checkbox" name="Check" id="Check" value="<bean:write name="bbean" property="bugid"/>"/>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getbid('<bean:write name="bbean" property="bugid"/>')">
										<span class="name"><bean:write name="bbean" property="username"/></span>
									</td>
									<td class="text" style="padding-top:5px;" onclick="getbid('<bean:write name="bbean" property="bugid"/>')">
										<span class="name"><bean:write name="bbean" property="comment"/></span>
									</td>
								</tr>
							</logic:notEqual>
						</logic:iterate>
						</html:form>
					</logic:present>
				</table>
			</div>
			<div class="prev_next">
				<div class="prev">
					<logic:present name="previous">
						<logic:notEqual name="previous" value="0">
							<html:link page="/BugsDB.ice" styleClass="text"  paramId="previous" paramName="previous">
								« previous
							</html:link>
						</logic:notEqual>
					</logic:present>
				</div>
				<div class="next">
					<logic:present name="next">
						<logic:notEqual name="next" value="0">
							<html:link page="/BugsDB.ice" styleClass="text"  paramId="next" paramName="next">
								next »
							</html:link>
						</logic:notEqual>
					</logic:present>
				</div>
			</div>
			</logic:equal>
		</logic:present>
		</div>
	<!-- Bug ends -->
<div style="display:none">
	<form id="deleterfrm" action="./DeleteCommentDB.ice" method="post">
		<input type="hidden" id="adelinfo" name="adelinfo"/>
		<input type="hidden" id="method" name="method"/>
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="feedfrm" action="./ViewFeedbackDB.ice" method="post">
		<input type="hidden" id="feedid" name="feedid"/>
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="bugfrm" action="./ViewBugDB.ice" method="post">
		<input type="hidden" id="bugid" name="bugid"/>
		<input type="submit"/>
	</form>
</div>
<logic:present name="ret_flag">
	<logic:equal value="feedback" name="ret_flag">
		<script>
			activatediv(1);
		</script>		
	</logic:equal>
	<logic:equal value="bugs" name="ret_flag">
		<script>
			activatediv(2);
		</script>
	</logic:equal>
</logic:present>
<logic:notPresent name="ret_flag">
	<script>
		activatediv(0);
	</script>
</logic:notPresent>
</body>
</html>
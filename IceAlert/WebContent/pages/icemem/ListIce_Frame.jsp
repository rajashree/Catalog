<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<!--<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired" />
</logic:notPresent>-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/ice.js" type="text/javascript"></script>	
<script src="assets/js/feedback.js" type="text/javascript"></script>	
<script src="assets/js/utils.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF; width:507px; overflow:hidden;">
<div class="ice" style="width:514px; overflow:hidden;">
<div style="display:none">
	<form id="dummyform" name="dummyform" action="./IceAlter.ice" method="post">
		<input type="hidden" name="method" id="method" />
		<input type="hidden" name="queary" id="queary" />
		<input type="submit"/>
	</form>
</div>
  <div class="form_ice">
    <div class="send_alert">
    	<logic:notPresent name="iceList">
   			<html:button property="sendAlert" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Send Alert</html:button>
   		</logic:notPresent>
   		<logic:present name="iceList">
   			<html:button property="sendAlert" onclick="javascript:Send_Ice_Alert()">Send Alert</html:button>
   		</logic:present>
    </div>
    <div class="add">
      <logic:present name="addflag">
       		<html:button property="addIce" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Add</html:button>
       	</logic:present>
       	<logic:notPresent name="addflag">
       		<html:button property="addIce"  onclick="document.location='./AddICEMembers_Frame.ice'">Add</html:button>
       	</logic:notPresent>
    </div>
    <div class="delete">
      <logic:notPresent name="iceList">
   			<html:button property="delIce" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Delete</html:button>
   		</logic:notPresent>
   		<logic:present name="iceList">
   			<html:button property="delIce" onclick="ice_del('Delete')">Delete</html:button>
   		</logic:present>
    </div>
    <div class="edit">
	<logic:notPresent name="iceList">
 			<html:button property="editIce" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Edit</html:button>
 		</logic:notPresent>
 		<logic:present name="iceList">
 			<html:button property="editIce" onclick="ice_del('Edit')">Edit</html:button>
 		</logic:present>
    </div>
  </div>
  <div style="width:100%;height:20px;padding:5px 0px 0px 0px;"> 
	<div class="error"><div id="feedback-content"></div></div>
</div>
   <logic:present name="count">
   	<html-el:hidden styleId="count" property="count" value="${requestScope.count}" />
   </logic:present>
   <html:form action="CheckForm.ice" styleId="myform">
  <!-- TABLE -->
   <logic:present name="iceList">
  <div class="table">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
     <tr>
     	<td class="heading"><input type="checkbox" name='glbid' id='glbid' onclick='selectevery();'/></td>
         <td class="heading"><span class="header">name</span></td>
         <td class="heading"><span class="header">contact no.</span></td>
         <td class="heading"><span class="header">e mail</span></td>
         <td class="heading"><span class="header">country</span></td>
     </tr>
     <logic:iterate id="iceBean" name="iceList" indexId="a">
	<tr>
		<td class="text" style="padding-top:5px;">
			<input type="checkbox" name='check' id='Check' value="<bean:write name="iceBean" property="ice_email"/>"/>
		</td>
		<td class="text" style="padding-top:5px;">
			<span class="name">
				<bean:write name="iceBean" property="ice_name"/>
			</span>
		</td>
        <td class="text" style="padding-top:5px;">
        	<span class="cno">
        		<bean:write name="iceBean" property="ice_mobile"/>
        	</span>
        </td>
        <td class="text" style="padding-top:5px;">
           	<a href="#">
           		<span class="email">
           			<bean:write name="iceBean" property="ice_email"/>
           		</span>
           	</a>
           </td>
           <td class="text" style="padding-top:5px;">
           	<span class="country">
           		<bean:write name="iceBean" property="ice_country"/>
           	</span>
           </td>
		</tr>
     </logic:iterate>
    </table>
  </div>
  </logic:present> 
</html:form>
<logic:notPresent name="iceList">
	<div class="body" style="padding:10px 10px 10px 10px;">Add first ICE member</div>
	</logic:notPresent>
  <!-- FORM -->
   <form id="smsform" action="./PersonalICEAlertAction.ice" method="post" onsubmit="return validateSMS();">                                   
 	<div class="form_ice01" id="smsdiv" style="display: none;">
       <input type="hidden" name="icedata" id="icedata" value=""/>
       <div class="send_alert_header">
       	<div class="text">send alert</div>
       </div>
       	<div class="row01">
               <div class="subject">
                   <div class="text">subject :</div>                                                
               </div>
               <div class="t_box">
               	<input type="text" class="text_box" id="smssub" name="smssub"/>
               </div>
           </div>
           <div class="row02">
               <div class="message">
                   <div class="text">message :</div>                                                
               </div>
               <div class="t_box">
               	<textarea class="text_area" id="smsdata" name="smsdata" ></textarea>
               </div>
           </div>
       <div class="button">
       	<div class="cancel">
           	<input type="button" value="Cancel" onclick="closesms();"/>
           </div>
           <div class="send">
           	<input type="submit" value="Send" />
           </div>
       </div>
       </div>
      </form>
</div>
<logic:present name="Delete">
                    	<logic:equal name="Delete" value="0">
	                    	<script>
	                    		feedback.show('Deleted Successfully',1);
					    	</script>
                    	</logic:equal>
                    	<logic:equal name="Delete" value="1">
	                    	<script>
					    		feedback.show('Deleted Abounded',1);
					    	</script>
                    	</logic:equal>
                    </logic:present>
                    <logic:present name="Add">
                    	<logic:equal name="Add" value="0">
	                    	<script>
					    		feedback.show('Added Successfully',1);
					    	</script>
                    	</logic:equal>
                    	<logic:equal name="Add" value="1">
	                    	<script>
					    		feedback.show('Added Abounded',1);
					    	</script>
                    	</logic:equal>
                    </logic:present>
                    <logic:present name="Update">
                    	<logic:equal name="Update" value="0">
	                    	<script>
					    		feedback.show('Updated Successfully',1);
					    	</script>
                    	</logic:equal>
                    	<logic:equal name="Update" value="1">
	                    	<script>
					    		feedback.show('Updated Abounded',1);
					    	</script>
                    	</logic:equal>
                    </logic:present>
                    <logic:present name="Send">
                    	<logic:equal name="Send" value="0">
	                    	<script>
					    		feedback.show('Alert sent successfully',1);
					    	</script>
                    	</logic:equal>
                    </logic:present>
</body>
</html>

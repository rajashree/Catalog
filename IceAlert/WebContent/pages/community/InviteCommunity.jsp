<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/community.js" type="text/javascript"></script>
<script src="assets/js/alert.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<script src="assets/js/utils.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body class="body_controller1" onload="initInvite();">
<div class="communities" style="overflow:hidden; width:515px;">

  <div class="form_community_invite">
  	<div id="errormsg"><div id="feedback-content"></div></div> 
  	<html:form action="InviteForCommunity.ice" method="post" onsubmit="return validateInvitation();" style="overflow: hidden;">
    	<div class="header_if">
      		<div class="text">community invite</div>
    	</div>
    	<html:hidden styleId="email_data" property="email_data" />
    	<div class="row01">
      		<div class="name">
        		<div class="text">community :</div>
      		</div>
      		<div class="t_box">
      			<logic:present  name="Commlist">
      				<html:select property="comid" styleId="comid" styleClass="combo_box">
       					<logic:iterate id="Commnbean" name="Commlist" indexId="a">
       						<html-el:option value="${requestScope.Commlist[a].id}"><bean:write name="Commnbean" property="name"/></html-el:option>
        				</logic:iterate>
        			</html:select>
      			</logic:present>
      			<logic:notPresent  name="Commlist"></logic:notPresent>
      		</div>
    	</div>
	    <div class="row02">
	      <div class="description">
	        <div class="text">from :</div>
	        <div class="label">
	          <label id="" class="label_text">
	          	<logic:present name="User_Name">
	          		<bean:write name="User_Name"/>
	          	</logic:present>
	          </label>
	        </div>
	      </div>
	    </div>
	    <div class="row03">
	      <div class="image">
	        <div class="text">To :</div>
	      </div>
	      <div class="t_box">
	      	<logic:present name="contacts">
      			<textarea name="id_email" id="id_email" class="text_area" rows="5"><bean:write name="contacts"/></textarea>
      		</logic:present>
      		<logic:notPresent name="contacts">
        		<textarea name="id_email" id="id_email" class="text_area" rows="5"></textarea>
        	</logic:notPresent>
	      </div>
	    </div>
	    <div class="row03">
	      <div class="image">
	        <div class="text">Message :</div>
	      </div>
	      <div class="t_box">
	      	<html:textarea styleClass="text_area" styleId="id_inv_msg" property="id_inv_msg" rows="7"></html:textarea>
	      </div>
	    </div>
	    <!-- REGISTER -->
	    <div class="row">
	      <div class="button">
	        <div style="float:right; padding:0px 16px 0px 5px;">
	          <input type="button" value="Cancel" onclick = "gotocommunity()"/>
	        </div>
	        <div style="float:right;">
	          <input type="submit" value="Invite"/>
	        </div>
	      </div>
	    </div>
    </html:form>
  </div>
  <div style="height:25px; overflow:hidden;"></div>
  <div class="form_invite_friends">
    <div class="heading">
      <div class="text">invite friends</div>
    </div>
    <div class="t_box">
      	<select class="combo_box" id="friendopt" name="friendopt"> 
      		<option value="none">--</option>
        	<option value="community">Communities</option>
        	<option value="icemember">ICE Members</option>
      </select>
    </div>
    <div class="button">
      <input type="button" value="Find your Friends" onclick="findFriends()" />
    </div>    
  </div>
  <div style="height:10px; overflow:hidden;"></div>
  <div class="form_import_email_address">
    <div class="heading">
      <div class="text">import email address</div>
    </div>
    <div class="links"> 
    	<a href="javascript:getContacts();" class="text" id="addcontact_link">import email address</a>
    </div>
  </div>
  <div style="display:none">
		<form id="myform" action="./InviteCommunity.ice">
			<input type="submit"/>
		</form>
	</div>
	<div style="display:none">
		<form id="contactfrm" action="./SelectProvider.ice">
			<input type="hidden" id="prev_contact" name="prev_contact"/>
			<input type="submit"/>
		</form>
	</div>
	<div style="display:none">
		<form id="searchfrdfrm" action="./FriendSearch.ice">
			<input type="hidden" id="prev_contact_friend" name="prev_contact_friend"/>
			<input type="hidden" id="friendtype" name="friendtype"/>
			<input type="submit"/>
		</form>
	</div>
	
	<div style="display:none">
		<form id="gocommfrm" action="./Community.ice" target="_parent">
			<input type="submit"/>
		</form>
	</div>
</div>
</body>
</html>

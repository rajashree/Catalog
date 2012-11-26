<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/community.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="communities" style="overflow:hidden;">
  <div class="communities_page">
  <div id="errormsg"><div id="feedback-content"></div></div> 
  <logic:present name="cview">
    <div class="cp">
      <div class="text">send community alert</div>
    </div>
     <div class="photo">
          <div class="image"><html-el:img width="74px" height="74px" src="${requestScope.cview.community_imgurl}" /></div>                                                
      </div>
      <div class="user-profile01">
          <div class="name">
          	<div class="text"><bean:write name="cview" property="community_name"/></div>
              <div class="text_nom"><bean:write name="cview" property="community_no_users"/></div>
          </div>
      </div> 
    <div class="user-profile02"  style="padding-left:20px;">
                                                <div class="description">
                                                     <div class="index">
                                                    	<div class="text">description :</div>
                                                    </div>
                                                    <div class="dsc">
                                                    	<div class="text"><bean:write name="cview" property="community_description"/></div>
                                                    </div>
                                                </div>  
                                                <div class="description">
                                                    <div class="index">
                                                    	<div class="text">created on :</div>
                                                    </div>
                                                    <div class="dsc">
                                                    	<div class="text"><bean:write name="cview" property="community_creation_date"/></div>
                                                    </div>
                                                </div>
                                               
                                                <logic:present name="mem">  
                                                	 <logic:equal name="mem" value="1">
                                                	 	<div class="body"  style="overflow: hidden;">No members in Community... <br><a href="./InviteCommunity.ice" style="overflow: hidden;cursor: pointer;">Invite friends</a></div>
                                                	 </logic:equal>
	                                                <logic:notEqual name="mem" value="1">
	                                                	 <div id="send_div">
	                                                	<form action="./SendCommunityAlert.ice" method="post" style="overflow:hidden" onsubmit="return validateCommunityAlert();">
			                                                <input type="hidden" class="text_box" id="commid" name="commid" value="<bean:write name="cview" property="commid"/>"/>
			                                                <div class="description">
			                                                    <div class="index">
			                                                    	<div class="text">subject :</div>
			                                                    </div>
			                                                    <div class="dsc">                              
			                                                    	<input type="text" class="text_box" id="subject" name="subject"/>
			                                                    </div>
			                                                </div>
			                                                <div class="description">
			                                                    <div class="index">
			                                                    	<div class="text">description :</div>
			                                                    </div>
			                                                    <div class="dsc" > 
			                                                    	<textarea class="text_area" id="description" name="description"></textarea>
			                                                    </div>
			                                                </div>
			                                                <div class="button">
			                                                	<div class="cancel">
			                                                    	<input type="button" value="Cancel" onclick="closeDiv();" />
			                                                     </div>
			                                                     <div class="send">
			                                                    	<input type="submit" value="Send" />
			                                                     </div>
			                                                </div>
		                                                </form> 
		                                                </div>
	                                                </logic:notEqual>
                                                </logic:present>
                                             </logic:present>
  </div>
</div>
</body>
</html>

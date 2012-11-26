<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/community.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="communities" style="overflow:hidden; width:515px;">
  <div class="form_com">
    <div class="button"><input type="button" value="Add" onclick="document.location='./AddCommunity.ice'"/></div>
      	<div class="button">
      		<logic:present name="owner_view">
          		<html:button property="Invite" onclick="document.location='./InviteCommunity.ice'" disabled="false">Invite</html:button>
          	</logic:present>
          	<logic:notPresent name="owner_view">
          		<html:button property="Invite" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Invite</html:button>
          	</logic:notPresent>
          </div>
          <div class="search_go">
          	<form action="./SearchCommunityDB.ice" method="post" onsubmit="return validateSearch(this);">
          	<div class="search">Search</div>
              <div class="t_box">
              	<input type="text" class="text_box" id="id_search" name="id_search"/>
              </div>
              <div class="button">
              	<input type="submit" value="Go" />
              </div>
              </form>
          </div>
  </div>
  <!-- Body -->
  <div class="communities_page" style="padding-top:10px;">
  <logic:present name="cview">
    <div class="usr_prfl">
      <div class="cp">
        <div class="text">communities page</div>
      </div>
      <div class="photo">
        <div class="image"><html-el:img width="74px" height="74px"  src="${requestScope.cview.community_imgurl}" /></div>
      </div>
      <div class="user-profile01">
        <div class="name">
        	<div class="text"><bean:write name="cview" property="community_name"/></div>
             <div class="text_nom"><bean:write name="cview" property="community_no_users"/></div>
        </div>
      </div>
      <div class="user-profile02" style="padding-left:20px;">
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
            <div class="text">owner :</div>
          </div>
          <div class="dsc">
            <div class="text"><bean:write name="cview" property="community_owner"/></div>
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
        <div class="description">
          <div class="index">
            <div class="text">comments :</div>
          </div>
          <div class="dsc">
            <div class="text">no comments</div>
          </div>
        </div>
        </div>
          <div class="set02">
                                	<div class="button">
                                		<logic:present name="owner_view">
                                 		<form action="./CommunityAlert.ice" method="post" style="overflow:hidden">
                                 			<input type="hidden" id="communityid" name="communityid" value="<bean:write name="cview" property="community_id"/>"/>
                                     		<div class="send_alert">
                                     			<input type="submit" value="Send Alert" />
                                         	</div>
                                         </form> 
	</logic:present>
	<logic:notPresent name="owner_view">
		<logic:notPresent name="sus_view">
                                  		<form action="./JoinCommunity.ice" method="post" style="overflow:hidden">
                                  			<input type="hidden" id="communityid" name="communityid" value="<bean:write name="cview" property="community_id"/>"/>
                                      		<div class="send_alert">
                                      			<input type="submit" value="Join Community" style="width:90px"/>
                                          	</div>
                                          </form> 
                                  	</logic:notPresent>
	</logic:notPresent>
                                 
    </logic:present>
      
    </div>
</body>
</html>
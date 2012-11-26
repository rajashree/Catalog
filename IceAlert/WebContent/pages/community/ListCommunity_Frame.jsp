<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@taglib prefix="logic-el"  uri="http://struts.apache.org/tags-logic-el" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/community.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="communities" style="overflow:hidden; width:514px;">
  <div class="form_com">
    <div class="button"><input type="button" value="Add" onclick="document.location='./AddCommunity.ice'"/></div>
	   <div class="button">
	   	<logic:present name="Own">
	   		<html:button property="Invite" onclick="document.location='./InviteCommunity.ice'" disabled="false">Invite</html:button>
	   	</logic:present>
	   	<logic:notPresent name="Own">
	   		<html:button property="Invite" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Invite</html:button>
	   	</logic:notPresent>
	   </div>
	   <div class="search_go">
	   	<form action="./SearchCommunityDB.ice" method="post" onsubmit="return validateSearch();">
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
  
    <div class="communities_page" style="padding:20px 0px 0px 10px;">
      <div class="cp">
        <div class="text">owned communities</div>
      </div>
       <logic:present name="Own">
		<logic:iterate id="cbean_list" name="Own" indexId="a">
			<div class="photo">
				<div class="image"><html-el:img width="74px" height="74px"  src="${requestScope.Own[a].community_imgurl}"  /></div>
				<div class="user_name">
					<html:link page="/ViewCommunity.ice" paramId="communityid" paramName="cbean_list" paramProperty="community_id"  styleClass="text">
						<bean:write name="cbean_list" property="community_name"/>
					</html:link>
				</div>
			</div>
		</logic:iterate>
	</logic:present>   
	<logic:notPresent name="Own">
		<div class="body" style="padding-left: 10px;">No owned communities </div>
	</logic:notPresent> 	
      
      <div class="more_photo">
      	<logic:present name="ownedcommunitysflag">
			<html:link page="/CommunityView_Owned.ice" styleClass="text_vm">
			View More »
			</html:link>
		</logic:present>  
      </div>
    </div>
    
    <div class="communities_page" style="padding:10px 0px 0px 10px;">
      <div class="cp">
        <div class="text">subscribed communities</div>
      </div>
      <logic:present name="sub">
		<logic:iterate id="cbean_list" name="sub" indexId="a">
			<div class="photo">
				<div class="image"><html-el:img width="74px" height="74px"  src="${requestScope.sub[a].community_imgurl}" /></div>
				<div class="user_name">
					<html:link styleClass="text" page="/ViewCommunity.ice" paramId="communityid" paramName="cbean_list" paramProperty="community_id">
						<bean:write name="cbean_list" property="community_name"/>
					</html:link>
				</div>
			</div>
		</logic:iterate>
	</logic:present> 
	<logic:notPresent name="sub">
		<div class="body" style="padding-left: 10px;">No subscribed communities </div>
	</logic:notPresent>       
      <div class="more_photo">
      	<logic:present name="subscribedcommunitiesflag">
			<html:link page="/CommunityView_Subscribed.ice" styleClass="text_vm">
				View More »
			</html:link>
		</logic:present>  
      </div>
    </div>
</div>
</body>
</html>
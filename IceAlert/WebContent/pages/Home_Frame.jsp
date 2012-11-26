<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/home.js" type="text/javascript"></script>
<script src="assets/js/utils.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Ice Alert</title>
</head>
<body style="background:#FFFFFF; overflow:hidden; width:505px;">
<div class="homepage">
  <div class="user_alert">
    <div class="user_name">
      <div class="text" style="float:left;">
      	Hi! 
       <logic:present name="security_username">
           <bean:write name="security_username"/> 
       </logic:present>
      </div>
      <div style="padding:2px 0px 0px 10px;color:#00688B;font-family:'trebuchet MS';font-size:11px;font-weight:bold;overflow:hidden;text-align:left;text-decoration:none;text-transform:none">( <bean:write name="security_iceid"/> )</div>
    </div>
    <div class="alerts">
      <div class="alert">
      	<html:link href="./ViewReceivedAlertsDB.ice" styleClass="text" target="_parent">
          You have 
            <logic:present name="inbox_count">
               <bean:write name="inbox_count"/>
            </logic:present>
             Alert Messages
         </html:link>
      </div>
      <div class="invitation" style="padding-bottom:0px;">
      	<html:link href="./ViewReceivedAlertsDB.ice?action=invite" styleClass="text" target="_parent" style="cursor:pointer;">
           You have 
           <logic:present name="invite_count">
              <bean:write name="invite_count"/>
          </logic:present>
           Community Invitations
        </html:link>
      </div>
      <div class="invitation">      	
			<logic:present name="cardstatus">
				<logic:equal name="cardstatus" value="1">
                	<div class="text"><a href="./ListIce.ice" class="text" style="cursor:pointer;" target="_parent">Add ICE Members to generate your ICE Card.</a></div>
                </logic:equal>
               	<logic:equal name="cardstatus" value="2">
<!--               		<div class="text"><a href="javascript:showPhotoSelect();" class="text">You haven't uploaded photo. Please upload to generate ICE Card.</a></div>-->
               		<div class="text"><a href="./Settings.ice?image=true;" class="text" style="cursor:pointer;" target="_parent">Upload your photo to generate your ICE CARD.</a></div>
               	</logic:equal>
               	<logic:equal name="cardstatus" value="3">
               		<div class="text"><a href="./Settings.ice" class="text" style="cursor:pointer;" target="_parent">Update your address info to generate your ICE Card.</a></div>
               	</logic:equal>
               	<logic:equal name="cardstatus" value="4">
               		<div class="text"><a href="./Settings.ice?set=medical" style="cursor:pointer;" class="text" target="_parent">Update your Medical info to generate your ICE Card.</a></div>
           		</logic:equal>
           		<logic:equal name="cardstatus" value="0">
               		<div class="text" style="text-decoration: none;cursor: default;">
               			ICE Card has been created. Please select format
               			<br/>
               			<div style="float:left;padding-left: 50px;overflow:hidden; "><img src="assets/images/pdf.gif" style="border: none;height: 10px;width: 10px;padding-right: 5px;"/><a href="./ICECard.ice?type=pdf" class="text" target="_parent" style="cursor:pointer;">PDF</a></div>
               			<div style="float:left;padding-left:75px;overflow:hidden;"><img src="assets/images/jpe.gif" style="border: none;height: 10px;width: 10px;padding-right: 5px;"/><a href="./ICECard.ice?type=jpg" class="text" target="_parent" style="cursor:pointer;">JPG</a></div>
               		</div>
           		</logic:equal>
			</logic:present>
       </div>
       <div class="form_ice_invite">
        <div class="heading">
          <div class="text">ICE alert invites</div>
          <div class="afab">
          	<a href="javascript:getContacts();" class="text" id="addcontact_link">add from address book</a>
      	  </div>
        </div>
        <div class="t_box">
        	<logic:present name="friendids">
				<input type="text" class="text_box" id="friendids" name="friendids" value="<bean:write name="friendids"/>"/>
			</logic:present>
			<logic:notPresent name="friendids">
				<input type="text" class="text_box" id="friendids" name="friendids" value=""/>
			</logic:notPresent>
        </div>
        <div class="invite">
          <input type="button" value="Invite" onclick="invitefriends();"/>
        </div>    
    </div>
    </div>    
  </div>
  <div class="user_photo">
    <div class="photo"> 
      <div class="image">
           	<IMG SRC="photo.ice" class="userimage" style="border: none;float: right;" width="74px" height="74px">
      </div>
      
<!--      <div class="location">-->
<!--        <div class="text">-->
<!--        	<logic:present name="security_iceid">-->
<!--              <b>ICE ID: </b><bean:write name="security_iceid"/> -->
<!--           </logic:present>-->
<!--        </div>-->
<!--      </div>-->
    </div>
  </div>
  <div class="parent_owned_community">
    <div class="owned_community">
      <div class="header">
        <div class="text">Owned Communities (<logic:present name="own_comm_count"><bean:write name="own_comm_count"/></logic:present><logic:notPresent name="own_comm_count">0</logic:notPresent>)</div>
      </div>
      
      	<logic:notPresent name="list"><div class="body" style="padding-left: 30px;padding-top: 10px;">Create your first community - 
       		<html:link page="/Community.ice?community_flag=add" styleClass="body" target="_parent" style="cursor:pointer;">Create</html:link></div>
       	</logic:notPresent>
       	<logic:present name="list">
       		<div  style="padding-left:20px;">
			<logic:iterate id="cbean" name="list" indexId="a">
				<div class="community_photos">
				<div class="photo">
					<div class="image">
						<html-el:img width="74px" height="74px" src="${requestScope.list[a].community_imgurl}" />
					</div>
					<div class="community_user_name">
						<html:link page="/Community.ice?community_flag=view" paramId="communityid" paramName="cbean" paramProperty="community_id" styleClass="text" target="_parent">
							<bean:write name="cbean" property="community_name"/>
						</html:link>
					</div>
				</div>   
				</div> 
			</logic:iterate>
			
		</logic:present> 
       	<logic:present name="communityflag">
			<html:form action="CommunityView_Owned.ice" type="com.snipl.ice.community.AllCommunitiesForm" styleId="nextfrm" method="post">
				<input type="hidden" id="community_flag" name="community_flag" value="home"/>				
				<div class="more_photo">
					<html:link styleClass="more" href="Community.ice?community_flag=home" target="_parent">
							More >>
					</html:link>
				</div>
			</html:form>
		</logic:present>
		</div>
    </div>
  </div>
<div style="display:none">
	<form id="myform" action="./Home_Frame.ice">
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="invitefrm" name="invitefrm" action="./FriendInvite.ice">
		<input type="hidden" id="maillist" name="maillist">
		<input type="submit"/>
	</form>
	<form id="contactfrm" action="./SelectProvider.ice">
		<input type="hidden" id="prev_contact" name="prev_contact"/>
		<input type="submit"/>
	</form>
</div>
</body>  
</html>
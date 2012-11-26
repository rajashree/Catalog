<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@taglib prefix="logic-el"  uri="http://struts.apache.org/tags-logic-el" %>
<%@page import="com.snipl.ice.utility.UserUtility;"  %>
<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired" />
</logic:notPresent>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="assets/js/popup.js" type="text/javascript"></script>
<script src="assets/js/community.js" type="text/javascript"></script>
<title>ICE Alert</title>
</head>

<body>
<div class="wrapper" id="page">
	<div class="header"></div>
    <div class="body_home">
    <!-- This is the Logo Division -->
    	<div class="logo"><h6>ice logo</h6></div> 
        <div class="remain_header">
        	<div class="alert_message"></div>
	        <div class="logout"><a href="./Logout.ice"><div class="text">Logout</div></a></div>
        </div>
	</div>
    <div class="body_home01">
    	<div class="parent">
        	<div class="margin">
            	<div class="parent_child">
                    <div class="child">
                    
                    <!-- Navigations -->
                    	<div class="navigation">
                            <ul>
                                <li><a href="./Home.ice"><div class="inactive_link_a">Home</div></a></li>
                                <li class="pipe">|</li>
                                <li><a href="./ListIce.ice"><div class="inactive_link_a">ICE</div></a></li>
                                <li class="pipe">|</li>
                                <li class="active_prv"><a href="./ViewReceivedAlertsDB.ice"><div class="inactive_link_b">Alerts</div></a></li>
                                <li class="bg_img_com"><a href="./Community.ice"><div class="active_link_com">Communities</div></a></li>
                                <li class="active_next"><html:link href="./Settings.ice"><div class="inactive_link_d">Settings</div></html:link></li>
                                <li class="pipe">|</li>
                                <li><div class="inactive_link_f"><a href="./ICEHelp.ice" class="hover">Help</a></div></li>
                                <logic:present name="security_profile">
                                	<logic:equal name="security_profile" value="0">
                                		 <li class="pipe">|</li>
                               			 <li><html:link href="./SendAlert.ice"><div class="inactive_link_e">
                                			Admin
                            			 </div></html:link></li>
                                	</logic:equal>
                                </logic:present>
                            </ul> 
                        </div>                        
                        <div class="middle_space">
                        	<div class="middle_space_left_corner"></div>
                        </div>
                        <div class="parent_body_content">
                        	<div class="body_content_top_border">
	                        	<div class="body_content">
                                    <div class="padd_left">
                                    	<div class="bound">
                                    		<%
												if(request.getParameter("community_flag")!=null)
												{
													if(request.getParameter("community_flag").toString().equalsIgnoreCase("home"))
														out.print("<iframe width='100%' height='100%' frameborder='0' src='./CommunityView_Owned.ice'></iframe>");
													else if(request.getParameter("community_flag").toString().equalsIgnoreCase("add"))
														out.print("<iframe width='100%' height='100%' frameborder='0' src='./AddCommunity.ice'></iframe>");
													else if(request.getParameter("community_flag").toString().equalsIgnoreCase("view"))
														out.print("<iframe width='100%' height='100%' frameborder='0' src='./ViewCommunity.ice?communityid="+request.getParameter("communityid")+"'></iframe>");
												}
												else if(request.getParameter("alert")!=null)
												{
													out.print("<iframe width='100%' height='100%' frameborder='0' src='./CommunityView_Owned.ice?alert=true'></iframe>");
												}
												else{
													out.print("<iframe width='100%' height='100%' frameborder='0' src='./ListCommunity_Frame.ice'></iframe>");
												}
											%>                                        	
                                        </div>
                                    </div>
                                </div>                            
                            </div>
                        </div>                        
                    </div>
				</div>                
        	</div>            
    	</div>                
       <div class="footer">
	      <div class="text">Copyright © 2007 - All Rights Reserved</div>
	      <div class="footer_links">
	      	<input type="hidden" value="<%=new UserUtility().getUserName(Integer.parseInt(session.getAttribute("security_id").toString()))%>" id="username" name="username"/>
	      	<input type="hidden" value="community" id="source" name="source"/>
	        <ul>
	          <li><a href="javascript:feedback();" class="text">feedback</a></li>
	          <li>|</li>
	          <li><a href="javascript:reportbug();" class="text">report a bug</a></li>
	          <li>|</li>
	          <li style="padding:0px;"><a href="javascript:contactUs();" class="text">contact us</a></li>
	        </ul>
	      </div>
	    </div>
	</div> 
</div>       
</body>
</html>
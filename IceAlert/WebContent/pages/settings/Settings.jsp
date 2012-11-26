<%@page import="com.snipl.ice.utility.UserUtility,java.util.HashMap"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired" />
</logic:notPresent>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/popup.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
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
                                <li><a href="./ViewReceivedAlertsDB.ice"><div class="inactive_link_b">Alerts</div></a></li>
                                <li class="pipe">|</li>
                                <li class="active_prv"><a href="./Community.ice"><div class="inactive_link_c">Communities</div></a></li>
                                <li class="bg_img_set"><a href="./Settings.ice"><div class="active_link_set">Settings</div></a></li>
                                <li class="active_next"><div class="inactive_link_f"><a href="./ICEHelp.ice" class="hover">Help</a></div></li>
                                <logic:present name="security_profile">
	                            	<logic:equal name="security_profile" value="0">
	                            		<li class="pipe">|</li>
	                           			 <li><a href="./SendAlert.ice"><div class="inactive_link_e">
	                            			Admin
	                        			 </div></a></li>
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
<!--                                    	<logic:present name="set">-->
<!--                                    		<iframe width='100%' height='100%' frameborder='0' src='./Personal.ice?medical=true'></iframe>-->
<!--                                    	</logic:present>-->
<!--                                    	<logic:notPresent name="set">-->
<!--                                    		<iframe width='100%' height='100%' frameborder='0' src='./Personal.ice'></iframe>-->
<!--                                    	</logic:notPresent>-->
                                    	<!--<iframe width="100%" height="100%" frameborder="0" src="./Personal.ice"></iframe>-->
                                        
                                        
                                        <%
	                                    	if(request.getParameter("set")!=null)
											{
	                                        	out.print("<iframe width='100%' height='100%' frameborder='0' src='./Personal.ice?medical=true'></iframe>");
											}
											else
											if(request.getParameter("image")!=null)
	                                        {
	                                        	out.print("<iframe width='100%' height='100%' frameborder='0' src='./Personal.ice?image=true'></iframe>");
	                                        }
											else
											{
												out.print("<iframe width='100%' height='100%' frameborder='0' src='./Personal.ice'></iframe>");
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
	      	<input type="hidden" value="settings" id="source" name="source"/>
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
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ page import="com.oreilly.servlet.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="assets/js/login.js" type="text/javascript"></script>
<script src="assets/js/popup.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<script src="assets/js/utils.js" type="text/javascript"></script>
<title>ICE Alert</title>
</head>

<body>
<div class="wrapper" id="page">
	<div class="header"></div>
    <div class="body_home">
    <!-- This is the Logo Division -->
    	<div class="logo"><h6>ice logo</h6></div> 
        
	</div>
    <div class="body_home01">
    	<div class="parent">
        	<div class="margin">
            	<div class="parent_child">
                    <div class="child_lp">
                    
                    <!-- Navigations -->
                        <div class="navigation">
                            <ul>
                                <li class="bg_img"><html:link href="./Index.ice"><div class="active_link">Sign In</div></html:link></li>
                                <li class="active_next"><div class="inactive_link_a"><html:link href="./About.ice" styleClass="hover">About</html:link></div></li>
                                <li class="pipe">|</li>
                                <li><div class="inactive_link_b"><html:link href="./Register.ice" styleClass="hover">Sign Up</html:link></div></li>
                                <li class="pipe">|</li>
                                <li><div class="inactive_link_c"><html:link href="./Blog.ice" styleClass="hover">Blog</html:link></div></li>
                                <li class="pipe">|</li>
                                <li><div class="inactive_link_d"><html:link href="./Help.ice" styleClass="hover">Help</html:link></div></li>
                            </ul> 
                        </div>  
                        
                        <div class="middle_space"></div>
                        <div class="parent_sign_in">
                        
                        	<div class="login_section">
                                <div class="login_area">                                	
                                	<div class="login">
                                		<div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
                                			<div class="error"><div id="feedback-content"></div></div>
                                		</div>                                		
                                		<html:form action="LoginManager.ice"  type="com.snipl.ice.security.LoginForm" onsubmit="return login();">
                                		<bean:cookie id="uname" name="StrutsCookbookUsername" value=""/>
										<bean:cookie id="pword" name="StrutsCookbookPassword" value=""/>
                                    	<div class="un">
                                        	<div class="user_name">
                                            	<div class="img"></div>
                                            </div>
                                            <div class="t_box">
                                            	<html:text styleClass="text_box" styleId ="id_username" property="id_username" value="<%=Base64Decoder.decode(uname.getValue( ))%>"/>
                                            </div>
                                        </div>
                                    	<div class="pw">
                                        	<div class="password">
                                            	<div class="img"></div>
                                            </div>
                                            <div class="t_box">
                                           		<html:password styleClass="text_box" tabindex="2" styleId ="id_pass" property="id_pass" value=""/>
                                            </div>
                                        </div>
                                        <div class="form_login">
                                        	<div class="button">
                                                <div class="rb">
                                                	<input type="reset" value="" />
                                                </div>
                                            	<div class="lb">
                                                	<input type="submit" value="" tabindex="3"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                	<div class="forgot_password">
                                    	<div><a href="javascript:forgotPass();" class="text">Forgot Password ?</a></div>
                                	</div>
                                	 <div class="remember_me">
                                        	<table border="0" cellpadding="0" cellspacing="0"><tr><td class="check_box" valign="top"><input type="checkbox" name="rememberMe" id="rememberMe"/></td><td class="text" valign="bottom">Remember Me</td></tr></table>
                                     </div>
                                     </html:form>
                                </div>
                            </div>
                            
                            <div class="ice_live">
                            	<div class="content">
                                	<ul class="points">
                                    	<li>ICE is an acronym for In Case of Emergency.</li>
                                        <li>ICE Alert allows you to stay in touch with your close one's In Case of Emergency.</li>
                                        <li>Updates you with the most important events happening around the world. </li>
										<li>We also provide an option to send alerts to every one who exist in your community list.</li>
                                        
										
                                    </ul>
                                    <div class="button">
                                    <div class="sign_up">
                                    	<input type="button" value="" onclick="document.location='./Register.ice'"/>
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
    	</div>
      	<div style="display:none">
			<form id="myform" action="./Index.ice">
				<input type="submit"/>
			</form>
		</div>
		<logic:present name="sessionExpaired">
		<%	System.out.println("in side logic tag");%>
			<script>
	    		feedback.show("sessionExpaired",1);
	    	</script>
		</logic:present>
	    <logic:present name="status">
	    	<logic:equal name="status" value="2">
		    	<script>
		    		feedback.show("Username and password do not match",1);
		    		$("id_username").focus();
		    	</script>
		    </logic:equal>
		    <logic:equal name="status" value="1">
		    	<script>
		    		feedback.show("Username and password do not match",1);
		    		$("id_username").focus();
		    	</script>
		    </logic:equal>
		    <logic:equal name="status" value="4">
		    	<script>
		    		feedback.show('Successfully logged out',1);
					$("id_username").focus();
		    	</script>
		    </logic:equal>
		     <logic:equal name="status" value="8">
		    	<script>
		    		feedback.show('Your login details have been sent.',1);
		    	</script>
		    </logic:equal>
		     <logic:equal name="status" value="9">
		    	<script>
		    		feedback.show('Please provide valid ICE Alert Id',1);
		    	</script>
		    </logic:equal>
		</logic:present>
	</div>       
</div>
</body>
</html>


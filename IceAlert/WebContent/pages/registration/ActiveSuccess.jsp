<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body>
<div class="wrapper">
  <div class="body_home">
    <!-- This is the Logo Division -->
    <div class="logo">
      <h6>ice logo</h6>
    </div>
    <div class="remain_header">
      <div class="alert_message"></div>
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
                <li>
                  <div class="inactive_link_a"><a href="./Index.ice" class="hover">Sign In</a></div>
                </li>
                <li class="pipe">|</li>
                <li class="active_prv">
                  <div class="inactive_link_a"><a href="./About.ice" class="hover">About</a></div>
                </li>
                <li class="bg_img">
                  <div class="active_link">Sign Up</div>
                </li>
                <li class="active_next">
                  <div class="inactive_link_b"><a href="./Blog.ice" class="hover">Blog</a></div>
                </li>
                <li class="pipe">|</li>
                <li>
                  <div class="inactive_link_c"><a href=".Help.ice" class="hover">Help</a></div>
                </li>
              </ul>
            </div>
            <div class="middle_space">
              <div class="middle_space_left_corner"></div>
            </div>
            <div class="parent_body_content">
              <div class="body_content_top_border">
                <div class="body_content">
                  <!-- START FORM -->
                  <div class="acc_act">
                    <div class="info">
                    	<logic:present name="ActivateAccountFlag">
							<logic:equal name="ActivateAccountFlag" value="0">
								<div class="body" style="text-align:center">Your account is now active.</div>
                      			<div class="body" style="text-align:center">Please <a href="./Index.ice" class="text">click here</a> to login</div>
							</logic:equal>
							<logic:equal name="ActivateAccountFlag" value="1">
								<div class="body" style="text-align:center">Thank you. Your account is already confirmed.</div>
                      			<div class="body" style="text-align:center">Please <a href="./Index.ice" class="text">click here</a> to login</div>	
							</logic:equal>	
						</logic:present>
						<logic:notPresent name="ActivateAccountFlag">
							<div class="body" style="text-align:center">Invalid information.</div>
                   			<div class="body" style="text-align:center">Please <a href="./Index.ice" class="text">click here</a> to login</div>
						</logic:notPresent>
                      
                    </div>
                  </div>
                  <!-- END FORM -->
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
  </div>
</div>
</body>
</html>


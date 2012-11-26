<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@taglib prefix="logic-el"  uri="http://struts.apache.org/tags-logic-el" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF; overflow:hidden; width:505px;">
<div class="alert_info" style="overflow:hidden; width:515px;">
  <!-- Navigations -->
  
  <div class="form_comment_lish">
    <!-- FIRST NAME -->
   	<div><div class="p_c" style="padding-top:10px;">
   		[ <a href="./PostComment.ice" class="text">Post comment</a> ]
   	</div></div>
    <div class="table_cl">
   	  	<logic:present name="bloglist">
	   	  	<table cellpadding="0" cellspacing="0" border="0" width="500">
			<logic:iterate id="blog_list" name="bloglist" indexId="a">
			 	<tr>
		          <td class="text"><span class="name"><bean:write name="blog_list" property="user_id"/></span> <span class="date">(<bean:write name="blog_list" property="doc"/>)</span></td>
	        	</tr>
		        <tr>
		          <td class="msg" style="padding-top:5px;"><span class="cntnt"><em><bean:write name="blog_list" property="comments"/></em></span></td>
		        </tr>
			</logic:iterate>
			</table>
		</logic:present>   
		<logic:notPresent name="bloglist">
			<div class="body" style="float: left">
				<div class="body" style="float: left">No Comments</div>			
			</div>
		</logic:notPresent>		
      </div>
      <logic:present name="bloglist">
      	<div class="p_c" style="padding-top:10px;">
   			[ <a href="./PostComment.ice" class="text">Post comment</a> ]
   		</div>
   	  </logic:present>  
  </div>
</div>
</body>
</html>

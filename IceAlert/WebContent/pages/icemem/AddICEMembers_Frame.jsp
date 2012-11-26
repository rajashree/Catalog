<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<!--<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired" />
</logic:notPresent>-->
<%
	session.setAttribute("security_prev","1");	
	boolean updateflag=false;
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/ice.js" type="text/javascript"></script>
<script src="assets/js/country.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<script src="assets/js/utils.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>

<body
<%
	if(request.getAttribute("contact_name")!=null
		&&request.getAttribute("contact_no")!=null
		&&request.getAttribute("contact_email")!=null
		&&request.getAttribute("Country")!=null
		&&request.getAttribute("S_Provider")!=null)
		{
			updateflag=true;
	%>onLoad=Ice_load('<%=request.getAttribute("contact_name")%>','<%=request.getAttribute("contact_no")%>','<%=request.getAttribute("contact_email")%>','<%=request.getAttribute("Country")%>','<%=request.getAttribute("S_Provider")%>')
	<%}%>
style="background:#FFFFFF">
<div class="communities" style="overflow:hidden; width:515px;">
<div style="height:60px; overflow:hidden;"> 
		<div class="error" style="padding:40px 0px 0px 0px; overflow:hidden;"><div id="feedback-content">
			<%
				if(request.getAttribute("flag")!=null)
				{
					if(Integer.parseInt(request.getAttribute("flag").toString())==8)
					{
						out.print("Sorry You cannot add your's info as ICE member");
					}
					else if(Integer.parseInt(request.getAttribute("flag").toString())==9)
					{
						out.print("ICE member already exist..!");
					}
				}
			%>
		</div></div>
	</div> 
  <div class="form_aim">
    <div class="header_aim">
     <div class="text">
                                	<%
	                                 		if(updateflag)
				out.print("Update ICE Members");
			else
				out.print("Add ICE Members");
		%>
											</div> 
    </div>
    
   <form id="icefrm" action="./IceSave.ice" method="post" onsubmit="return iceValidate();">
		<%if(updateflag){%>
			<input type="hidden" name="method" id="method" value="Update" />
			<input type="hidden" name="ice_email" id="ice_email" />
			<input type="hidden" name="ice_no" id="ice_no" />
		<%}else{%>
			<input type="hidden" name="method" id="method" value="Add"/>
		<%}%>
                                <div class="row">
                                    <div class="column_1">name :</div>
                                    <div class="t_box">
                                        <input type="text" class="text_box" id="id_ice_name" name="id_ice_name"/>
                                    </div>
                                </div>
                                
                                <!-- MOBILE -->
                                
                                <div class="row">
                                    <div class="column_1">mobile :</div>
                                    <div class="t_box">
                                        <div class="country_code"><input type="text" class="text_box_cc" id="mobileext"  name="mobileext" readonly="readonly"/></div>
                                        <div class="mobile_number"><input type="text" class="text_box_mn" id="id_mob" name="m_Number"/></div>
                                    </div>
                                </div>
                                
                                <!-- COUNTRY -->
                                
                                <div class="row">
                                	<input type="hidden" id="hid_combo" name="hid_combo"/>
                                    <div class="column_1">country :</div>
                                    <div class="t_box_c">
                                    	<select class="combo_box" id="div_combo6" name="div_combo6" onchange="javascript:getProviderList(this);javascript:getCountryCode(this);">
					<option value="0" selected="selected">Select</option>
					<option value="1">Afghanistan</option>
					<option value="2">Albania</option>
					<option value="3">Algeria</option>
					<option value="4">Argentina </option>
					<option value="5">Australia </option>
					<option value="6">Antarctica</option>
					<option value="7">Austria</option>
					<option value="8">Bahrain</option>
					<option value="9">Bangladesh</option>
					<option value="10">Belgium</option>
					<option value="11">Bhutan</option>
					<option value="12">Bolivia</option>
					<option value="13">Bosnia &amp;  Herzegovina</option>
					<option value="14">Botswana</option>
					<option value="15">Brazil</option>
					<option value="16">Bulgaria</option>
					<option value="17">Burkina Faso</option>
					<option value="18">Cambodia</option>
					<option value="19">Canada</option>
					<option value="20">Chile</option>
					<option value="21">China</option>
					<option value="co">Colombia</option>
					<option value="23">Congo</option>
					<option value="24">Cuba</option>
					<option value="25">Czech Republic</option>
					<option value="dj">Djibouti</option>
					<option value="dm">Dominica</option>
					<option value="do">Dominican Republic</option>
					<option value="tp">East Timor</option>
					<option value="gq">Equatorial Guinea</option>
					<option value="er">Eritrea</option>
					<option value="ee">Estonia</option>
					<option value="et">Ethiopia</option>
					<option value="fk">Falkland Islands</option>
					<option value="fo">Faroe Islands</option>
					<option value="fj">Fiji</option>
					<option value="gf">French Guiana</option>
					<option value="ge">Georgia</option>
					<option value="gh">Ghana</option>
					<option value="gi">Gibraltar</option>
					<option value="gl">Greenland</option>
					<option value="gd">Grenada</option>
					<option value="gp">Guadeloupe</option>
					<option value="gu">Guam</option>
					<option value="gt">Guatemala</option>
					<option value="gn">Guinea</option>
					<option value="gy">Guyana</option>
					<option value="ht">Haiti</option>
					<option value="hn">Honduras</option>
					<option value="is">Iceland</option>
					<option value="in">India</option>
					<option value="ir">Iran</option>
					<option value="iq">Iraq</option>
					<option value="jm">Jamaica</option>
					<option value="kz">Kazakhstan</option>
					<option value="ke">Kenya</option>
					<option value="kw">Kuwait</option>
					<option value="ly">Libya</option>
					<option value="mu">Mauritius</option>
					<option value="me">Mexico</option>
					<option value="mn">Mongolia</option>
					<option value="mm">Myanmar</option>
					<option value="na">Namibia</option>
					<option value="np">Nepal</option>
					<option value="ng">Nigeria</option>
					<option value="pa">Panama</option>
					<option value="lk">Sri Lanka</option>
					<option value="sy">Syria</option>
					<option value="si">Syria</option>
					<option value="tj">Tajikistan</option>
					<option value="tz">Tanzania</option>
					<option value="ug">Uganda</option>
					<option value="uk">United Kingdom(UK)</option>
					<option value="us">United States of America(USA)</option>
					<option value="vn">Vietnam</option>
					<option value="zw">Zimbabwe</option>
				</select>
               </div>
           </div>
                                
           <!-- PROVIDER -->
           
           <div class="row">
               <div class="column_1">provider :</div>
               <div class="t_box_c">
                   <select class="combo_box" id="div_provider" name="div_provider"><option value="Select">Select</option></select>
               </div>
           </div>
           
           <!-- EMAIL -->
           
           <div class="row">
               <div class="column_1">e mail :</div>
               <div class="t_box">
                   <input type="text" class="text_box" id="id_Email" name="email"/>
               </div>
           </div>
           
           <!-- ADD -->
           
           <div class="row">
               <div class="button">
                   <div style="float:right; width:70px;">
                   	<%if(updateflag){%>
					<input value="Update" type="submit">
					<%}else
					{%><input value="Add" type="submit">
					
					<%}%>
                   </div>
               </div>
           </div>
         </form> 
  </div>
</div>
</body>
</html>

<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.snipl.ice.registration.RegistrationAction"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script src="assets/js/registration.js" type="text/javascript"></script>
<script src="assets/js/country.js" type="text/javascript"></script>
<script src="assets/js/popup.js" type="text/javascript"></script>
<script src="assets/js/utils.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@page import="com.snipl.ice.utility.GeneralUtility"%>
<%@page import="com.snipl.ice.blog.SaveBlogCommentAction"%>
<title>ICE Alert</title>
<% 
   	String hash = GeneralUtility.getRandomString(20); 
   	String captchString = GeneralUtility.getRandomString(5);
   	RegistrationAction.addCaptcha(hash, captchString); 
%>
</head>
<body style="background:#FFFFFF; width:505px; overflow:hidden;" id="page">
<div class="communities" style="overflow:hidden; width:515px;" >
	<div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
		<div class="error"  style="overflow:hidden;"><div id="feedback-content">
			<logic:present name="error">
		   		<script>
		         	feedback.show("Email already in Use",1);
		     	</script>
	     	</logic:present>
	     	<logic:present name="code">
		   		<script>
		         	feedback.show("Please enter correct code",1);
		     	</script>
	     	</logic:present>
		</div></div>
	</div>	
  	<div class="form_reg">
  	<html:form  styleId="frmreg" action="Registration.ice" type="com.snipl.ice.registration.RegisterForm" onsubmit="return validateRegForm(this);" style="overflow:hidden">
    <!-- FIRST NAME -->
    <div class="row">
      <div class="column_1">first name :</div>
      <div class="t_box">
        <html:text styleClass="text_box" styleId="id_firstname" property="fname"/>
      </div>
    </div>
    <!-- LAST NAME -->
    <div class="row">
      <div class="column_1">last name :</div>
      <div class="t_box">
        <html:text styleClass="text_box" styleId="id_lastname" property="lname" />
      </div>
    </div>
    <!-- EMAIL -->
    <div class="row">
      <div class="column_1">e mail :</div>
      <div class="t_box" style="width:210px;">
        <html:text styleClass="text_box" styleId="id_Email" property="email" />
      </div>
      <span style="float:left;color:red;overflow: hidden;font-size: 10px;">* (your UserName)</span>
    </div>
    <!-- PASSWORD -->
    <div class="row">
      <div class="column_1">password :</div>
      <div class="t_box">
        <html:password styleClass="text_box" styleId="id_pass" property="pword" value=""/>
      </div>
    </div>
    <!-- CONFIRM PASSWORD -->
    <div class="row">
      <div class="column_1">confirm password :</div>
      <div class="t_box">
        <html:password styleClass="text_box" styleId="id_confirm_pass" property="pword1" value=""/>
      </div>
    </div>
    <!-- DOB -->
    <div class="row">
      <div class="column_1">date of birth :</div>
      <div class="dob">
        <div class="y_combo_box">
			<html:select styleId="dob_year" property="dob_year" styleClass="combo_box">
				<html:option value="0">Year</html:option>
				<html:option value="2006">2006</html:option>
				<html:option value="2005">2005</html:option>
				<html:option value="2004">2004</html:option>
				<html:option value="2003">2003</html:option>
				<html:option value="2002">2002</html:option>
				<html:option value="2001">2001</html:option>
				<html:option value="2000">2000</html:option>
				<html:option value="1999">1999</html:option>
				<html:option value="1998">1998</html:option>
				<html:option value="1997">1997</html:option>
				<html:option value="1996">1996</html:option>
				<html:option value="1995">1995</html:option>
				<html:option value="1994">1994</html:option>
				<html:option value="1993">1993</html:option>
				<html:option value="1992">1992</html:option>
				<html:option value="1991">1991</html:option>
				<html:option value="1990">1990</html:option>
				<html:option value="1989">1989</html:option>
				<html:option value="1988">1988</html:option>
				<html:option value="1987">1987</html:option>
				<html:option value="1986">1986</html:option>
				<html:option value="1985">1985</html:option>
				<html:option value="1984">1984</html:option>
				<html:option value="1983">1983</html:option>
				<html:option value="1982">1982</html:option>
				<html:option value="1981">1981</html:option>
				<html:option value="1980">1980</html:option>
				<html:option value="1979">1979</html:option>
				<html:option value="1978">1978</html:option>
				<html:option value="1977">1977</html:option>
				<html:option value="1976">1976</html:option>
				<html:option value="1975">1975</html:option>
				<html:option value="1974">1974</html:option>
				<html:option value="1973">1973</html:option>
				<html:option value="1972">1972</html:option>
				<html:option value="1971">1971</html:option>
				<html:option value="1970">1970</html:option>
				<html:option value="1969">1969</html:option>
				<html:option value="1968">1968</html:option>
				<html:option value="1967">1967</html:option>
				<html:option value="1966">1966</html:option>
				<html:option value="1965">1965</html:option>
				<html:option value="1964">1964</html:option>
				<html:option value="1963">1963</html:option>
				<html:option value="1962">1962</html:option>
				<html:option value="1961">1961</html:option>
				<html:option value="1960">1960</html:option>
				<html:option value="1959">1959</html:option>
				<html:option value="1958">1958</html:option>
				<html:option value="1957">1957</html:option>
				<html:option value="1956">1956</html:option>
				<html:option value="1955">1955</html:option>
				<html:option value="1954">1954</html:option>
				<html:option value="1953">1953</html:option>
				<html:option value="1952">1952</html:option>
				<html:option value="1951">1951</html:option>
				<html:option value="1950">1950</html:option>
				<html:option value="1949">1949</html:option>
				<html:option value="1948">1948</html:option>
				<html:option value="1947">1947</html:option>
				<html:option value="1946">1946</html:option>
				<html:option value="1945">1945</html:option>
				<html:option value="1944">1944</html:option>
				<html:option value="1943">1943</html:option>
				<html:option value="1942">1942</html:option>
				<html:option value="1941">1941</html:option>
				<html:option value="1940">1940</html:option>
				<html:option value="1939">1939</html:option>
				<html:option value="1938">1938</html:option>
				<html:option value="1937">1937</html:option>
				<html:option value="1936">1936</html:option>
				<html:option value="1935">1935</html:option>
				<html:option value="1934">1934</html:option>
				<html:option value="1933">1933</html:option>
				<html:option value="1932">1932</html:option> 
				<html:option value="1931">1931</html:option>
				<html:option value="1930">1930</html:option>
				<html:option value="1929">1929</html:option>
				<html:option value="1928">1928</html:option>
				<html:option value="1927">1927</html:option>
				<html:option value="1926">1926</html:option>
				<html:option value="1925">1925</html:option>
				<html:option value="1924">1924</html:option>
				<html:option value="1923">1923</html:option>
				<html:option value="1922">1922</html:option>
				<html:option value="1921">1921</html:option>
				<html:option value="1920">1920</html:option>
				<html:option value="1919">1919</html:option>
				<html:option value="1918">1918</html:option>
				<html:option value="1917">1917</html:option>
				<html:option value="1916">1916</html:option>
				<html:option value="1915">1915</html:option>
				<html:option value="1914">1914</html:option>
				<html:option value="1913">1913</html:option>
				<html:option value="1912">1912</html:option>
				<html:option value="1911">1911</html:option>
				<html:option value="1910">1910</html:option>
				<html:option value="1909">1909</html:option>
				<html:option value="1908">1908</html:option>
				<html:option value="1907">1907</html:option>
				<html:option value="1906">1906</html:option>
				<html:option value="1905">1905</html:option>
				<html:option value="1904">1904</html:option>
				<html:option value="1903">1903</html:option>
				<html:option value="1902">1902</html:option>
				<html:option value="1901">1901</html:option>
				<html:option value="1900">1900</html:option>
			</html:select>
        </div>
        <div class="m_combo_box">
			<html:select styleId="dob_month" property="dob_month" onkeypress="handleDateInputChange()" onmouseup="handleDateInputChange()" onchange="handleDateInputChange();" styleClass="combo_box" > 
				<html:option value="0">Month</html:option>
				<html:option value="1">Jan</html:option>
				<html:option value="2">Feb</html:option>
				<html:option value="3">Mar</html:option>
				<html:option value="4">Apr</html:option>
				<html:option value="5">May</html:option>
				<html:option value="6">Jun</html:option>
				<html:option value="7">Jul</html:option>
				<html:option value="8">Aug</html:option>
				<html:option value="9">Sep</html:option>
				<html:option value="10">Oct</html:option>
				<html:option value="11">Nov</html:option>
				<html:option value="12">Dec</html:option>
			</html:select>
        </div>
        <div class="d_combo_box">
          	<html:select styleClass="combo_box" styleId="dob_day" property="dob_day" styleClass="combo_box">
				<html:option value="0">(None)</html:option>
			</html:select>
        </div>
      </div>
    </div>
    <!-- ZIP CODE -->
    <div class="row">
      <div class="column_1">zip code :</div>
      <div class="t_box">
        <html:text styleClass="text_box" styleId="id_Zip" property="id_Zip" />
      </div>
    </div>
    
    <!-- COUNTRY -->
    <div class="row">
      <div class="column_1">country :</div>
      <div class="t_box_c">
		<html:select styleClass="combo_box" styleId="div_combo6" property="div_combo6" onchange="javascript:getProviderList(this);javascript:getCountryCode(this);">
        	<html:option value="0">Select</html:option>
			<html:option value="1">Afghanistan</html:option>
			<html:option value="2">Albania</html:option>
			<html:option value="3">Algeria</html:option>
			<html:option value="4">Argentina</html:option>
			<html:option value="5">Australia</html:option>
			<html:option value="6">Antarctica</html:option>
			<html:option value="7">Austria</html:option>
			<html:option value="8">Bahrain</html:option>
			<html:option value="9">Bangladesh</html:option>
			<html:option value="10">Belgium</html:option>
			<html:option value="11">Bhutan</html:option>
			<html:option value="12">Bolivia</html:option>
			<html:option value="13">Bosnia &amp;  Herzegovina</html:option>
			<html:option value="14">Botswana</html:option>
			<html:option value="15">Brazil</html:option>
			<html:option value="16">Bulgaria</html:option>
			<html:option value="17">Burkina Faso</html:option>
			<html:option value="18">Cambodia</html:option>
			<html:option value="19">Canada</html:option>
			<html:option value="20">Chile</html:option>
			<html:option value="21">China</html:option>
			<html:option value="co">Colombia</html:option>
			<html:option value="23">Congo</html:option>
			<html:option value="24">Cuba</html:option>
			<html:option value="25">Czech Republic</html:option>
			<html:option value="dj">Djibouti</html:option>
			<html:option value="dm">Dominica</html:option>
			<html:option value="do">Dominican Republic</html:option>
			<html:option value="tp">East Timor</html:option>
			<html:option value="gq">Equatorial Guinea</html:option>
			<html:option value="er">Eritrea</html:option>
			<html:option value="ee">Estonia</html:option>
			<html:option value="et">Ethiopia</html:option>
			<html:option value="fk">Falkland Islands</html:option>
			<html:option value="fo">Faroe Islands</html:option>
			<html:option value="fj">Fiji</html:option>
			<html:option value="gf">French Guiana</html:option>
			<html:option value="ge">Georgia</html:option>
			<html:option value="gh">Ghana</html:option>
			<html:option value="gi">Gibraltar</html:option>
			<html:option value="gl">Greenland</html:option>
			<html:option value="gd">Grenada</html:option>
			<html:option value="gp">Guadeloupe</html:option>
			<html:option value="gu">Guam</html:option>
			<html:option value="gt">Guatemala</html:option>
			<html:option value="gn">Guinea</html:option>
			<html:option value="gy">Guyana</html:option>
			<html:option value="ht">Haiti</html:option>
			<html:option value="hn">Honduras</html:option>
			<html:option value="is">Iceland</html:option>
			<html:option value="in">India</html:option>
			<html:option value="ir">Iran</html:option>
			<html:option value="iq">Iraq</html:option>
			<html:option value="jm">Jamaica</html:option>
			<html:option value="kz">Kazakhstan</html:option>
			<html:option value="ke">Kenya</html:option>
			<html:option value="kw">Kuwait</html:option>
			<html:option value="ly">Libya</html:option>
			<html:option value="mu">Mauritius</html:option>
			<html:option value="me">Mexico</html:option>
			<html:option value="mn">Mongolia</html:option>
			<html:option value="mm">Myanmar</html:option>
			<html:option value="na">Namibia</html:option>
			<html:option value="np">Nepal</html:option>
			<html:option value="ng">Nigeria</html:option>
			<html:option value="pa">Panama</html:option>
			<html:option value="lk">Sri Lanka</html:option>
			<html:option value="sy">Syria</html:option>
			<html:option value="si">Syria</html:option>
			<html:option value="tj">Tajikistan</html:option>
			<html:option value="tz">Tanzania</html:option>
			<html:option value="ug">Uganda</html:option>
			<html:option value="uk">United Kingdom(UK)</html:option>
			<html:option value="us">United States of America(USA)</html:option>
			<html:option value="vn">Vietnam</html:option>
			<html:option value="zw">Zimbabwe</html:option>
  		</html:select>
      	</div>
    </div>
    
    <!-- PHONE -->
    <div class="row">
      <div class="column_1">phone :</div>
      <div class="t_box">
        <div class="country_code">
          <html:text styleClass="text_box_cc" styleId="phoneext" property="phoneext" />
        </div>
        <div class="mobile_number">
          <html:text styleClass="text_box_mn" styleId="id_ph" property="p_Number" />
        </div>
      </div>
    </div>
    
    <!-- MOBILE -->
    <div class="row">
      <div class="column_1">mobile :</div>
      <div class="t_box">
        <div class="country_code">
          <html:text styleClass="text_box_cc" styleId="mobileext" property="mobileext" readonly="true" maxlength="5"/>
        </div>
        <div class="mobile_number">
          <html:text styleClass="text_box_mn" styleId="id_mob" property="m_Number" />
        </div>
      </div>
    </div>
    <!-- PROVIDER -->
    <div class="row">
      <div class="column_1">provider :</div>
      <div class="t_box_c">
        <html:select styleClass="combo_box" styleId="div_provider" property="div_provider"><html:option value="0">Select</html:option></html:select>
         <html:hidden property="hid_combo" styleId="hid_combo" />                                               
      </div>
    </div>
    <!-- OCCUPATION -->
    <div class="row_occ">
      <div class="column_1">occupation :</div>
      <div class="t_box">
        <html:select styleClass="combo_box" styleId="occupation" property="occupation" >
	        <html:option value="0">Choose your Profession</html:option>
	        <html:option value="Accounting/Finance">Accounting/Finance</html:option>
	        <html:option value="Administration">Administration</html:option>
	        <html:option value="Advertising">Advertising</html:option>
	        <html:option value="Business Development">Business Development</html:option>
	        <html:option value="Consultant">Consultant</html:option>d
	        <html:option value="Creative Services/Design">Creative Services/Design</html:option>
	        <html:option value="Customer Service/Support">Customer Service/Support</html:option>
	        <html:option value="Engineering">Engineering</html:option>
	        <html:option value="Health Services">Health Services</html:option>
	        <html:option value="Human Resources/Training">Human Resources/Training</html:option>
	        <html:option value="Information Technology">Information Technology</html:option>
	        <html:option value="Legal">Legal</html:option>
	        <html:option value="Management, General">Management, General</html:option>
	        <html:option value="Manufacturing">Manufacturing</html:option>
	        <html:option value="Marketing">Marketing</html:option>
	        <html:option value="Operations">Operations</html:option>
	        <html:option value="Production">Production</html:option>
	        <html:option value="Public Relations">Public Relations</html:option>
	        <html:option value="Quality Assurance">Quality Assurance</html:option>
	        <html:option value="Research">Research</html:option>
	        <html:option value="Sales">Sales</html:option>          
        </html:select>
      </div>
    </div>
    <!-- E - OCCUPATION -->
    <div class="row">
      <div class="column_1">e - occupation :</div>
      <div class="check_box" style="width:20px; overflow:hidden; float:left;">
        <html:checkbox property="echeck" styleId="echeck" onclick="javascript:viewEoccupationList()"></html:checkbox>
      </div>
      <div class="t_box" style="width:190px; float:left;">
        <html:select styleClass="combo_box" styleId="eoccupation" property="eoccupation" disabled="true">
	        <html:option value="0">Choose your Profession</html:option>
	        <html:option value="Police">Police</html:option>
	        <html:option value="Doctor">Doctor</html:option>
	        <html:option value="Army">Army</html:option>
	        <html:option value="Navy">Navy</html:option>
	        <html:option value="Airforce">Airforce</html:option>
	        <html:option value="Advertising">Advertising</html:option>
        </html:select>
      </div>
      <div class="w_box">
<!--      	<a href="javascript:whatIsThis();" class="text">what is this?</a>-->
      </div>
    </div>
    
    
    <div class="row">
      <div class="column_1">enter the code :</div>
      <div class="st_box">
       	<input type="text" class="text_box" id="captchaid" name="captchaid" />
      </div>
      <div class="secret_code">
       	<div class="img">
       		<img src="Captcha.ice?captchaRegister=<%=hash%>" style="width:130px; height:50px;overflow:hidden;" />
       	</div>
      </div>
    </div>
    
    
    
<!--    <div class="row3">					    -->
<!--		<div class="t_box">-->
<!--			<input type="text" class="text_box" id="captchaid" name="captchaid" />-->
<!--		</div>-->
<!--		<div class="t_box">-->
<!--			<img style="padding-top: 15px;" src="Captcha.ice?captchaRegister=<%=hash%>" />-->
<!--		</div>-->
<!--	</div>-->
<!--	-->
	<input type="hidden" name="captchaIds" value="<%=hash%>" />
    <div class="button">
      <div class="reset">
      	<html:reset>Reset</html:reset>
      </div>
      <div class="register">
        <html:submit>Register</html:submit>
      </div>
    </div>
    </html:form>
  </div>
</div>

</body>
</html>

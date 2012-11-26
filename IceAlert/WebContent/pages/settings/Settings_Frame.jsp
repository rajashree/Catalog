<%@page import="com.snipl.ice.utility.UserUtility,java.util.HashMap"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired_Frame" />
</logic:notPresent>
<%
	HashMap hm;
	UserUtility um = new UserUtility();
	hm=um.getUserDetails(Integer.parseInt(session.getAttribute("security_id").toString()));
	session.setAttribute("security_prev","1");	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/settings.js" type="text/javascript"></script>
<script src="assets/js/country.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
<script type="text/javascript"><!--
	function OnLoad() {
		document.getElementById("file").focus();
	}
	function myfunction()
	{
		var browser=navigator.appName;
		if(browser=="Microsoft Internet Explorer")
		document.location="buddy_popup.jsp";
		alert("Sorry Use Browse Button");
		d.file.value="";
	}
	
	function validate() {
		if(((document.getElementById("file").value.lastIndexOf(".jpg"))==-1) && ((document.getElementById("file").value.lastIndexOf(".gif"))==-1) &&((document.getElementById("file").value.lastIndexOf(".png"))==-1)&&((document.getElementById("file").value.lastIndexOf(".bmp"))==-1)&&((document.getElementById("file").value.lastIndexOf(".GIF"))==-1)&&((document.getElementById("file").value.lastIndexOf(".BMP"))==-1)&&((document.getElementById("file").value.lastIndexOf(".JPG"))==-1)&&((document.getElementById("file").value.lastIndexOf(".PNG"))==-1)) {
		   alert("Please upload only JPG, GIF, BMP, or PNG extention file");
		   return false;
		}
		var file = document.getElementById("file");
		return file.value ? true : false;
	}

//--></script>
</head>
<!--<body style="background:#FFFFFF; width:505px; overflow:hidden;" onload="load_de(), load_country('<%=hm.get("country")%>','<%=hm.get("provider")%>','<%=hm.get("Occupation")%>','<%=hm.get("year")%>','<%=hm.get("month")%>','<%=hm.get("day")%>','<%=hm.get("eoccupation")%>','<%=hm.get("bloodgroup")%>','<%=hm.get("allergies")%>','<%=hm.get("disease")%>');">-->
<body style="background:#FFFFFF; width:505px; overflow:hidden;" onload="load_country('<%=hm.get("country")%>','<%=hm.get("provider")%>','<%=hm.get("Occupation")%>','<%=hm.get("year")%>','<%=hm.get("month")%>','<%=hm.get("day")%>','<%=hm.get("eoccupation")%>','<%=hm.get("bloodgroup")%>','<%=hm.get("allergies")%>','<%=hm.get("disease")%>');">
<div class="communities" style="overflow:hidden; width:505px;">
  <div class="set_nav_margin">
    <div class="set_nav">
      <div class="edit_profile_active" id="editpro_div">
        <a href="javascript:initSettings(0);" class="text">edit profile</a>&nbsp;&nbsp;|&nbsp;&nbsp;
      </div>
      <div class="change_password" id="changepass_div">
        <a href="javascript:initSettings(1);" class="text">change password</a>&nbsp;&nbsp;|&nbsp;&nbsp;
      </div>
      <div class="change_image" id="changeimg_div"> 
        <a href="javascript:initSettings(2);" class="text">change image</a>
      </div>
      
      
    </div>
  </div>
  
  <div class="com_sub_links" id="sub_links">
      <div class="medical" id="med_div"> 
        <a href="javascript:initeditSettings(1);" class="text" id="med_a">medical info</a>
      </div>
      <div class="alert_info" id="per_div">
        <a href="javascript:initeditSettings(0);" class="text_active" id="per_a">personal info</a>&nbsp;&nbsp;|&nbsp;&nbsp;
      </div>
  </div>
  <!-- Edit Profile -->
  <div class="form_ep" id="personal">
  <form id="editfrm" action="./ContactInfo.ice" method="post" onsubmit="return accountInfo(this);">
  <div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
  	  <div id="errormsg"><div id="feedback-content"> </div></div>
  </div> 	
    <!-- FIRST NAME -->
    <div class="row">
      <div class="column_1">first name :</div>
      <div class="t_box">
        <input type="text" class="text_box" id="id_firstname" name="id_firstname" value="<%=hm.get("fname")%>"/>
      </div>
    </div>
    <!-- LAST NAME -->
    <div class="row">
      <div class="column_1">last name :</div>
      <div class="t_box">
        <input type="text" class="text_box" name="id_lastname" id="id_lastname" value="<%=hm.get("lname")%>" />
      </div>
    </div>
    <!-- DOB -->
    <div class="row">
      <div class="column_1">date of birth :</div>
      <div class="dob">
        <div class="y_combo_box">
          <select class="combo_box" id="dob_year" name="dob_year">
            	<option value="0">Year</option>
				<option value="2006">2006</option>
				<option value="2005">2005</option>
				<option value="2004">2004</option>
				<option value="2003">2003</option>
				<option value="2002">2002</option>
				<option value="2001">2001</option>
				<option value="2000">2000</option>
				<option value="1999">1999</option>
				<option value="1998">1998</option>
				<option value="1997">1997</option>
				<option value="1996">1996</option>
				<option value="1995">1995</option>
				<option value="1994">1994</option>
				<option value="1993">1993</option>
				<option value="1992">1992</option>
				<option value="1991">1991</option>
				<option value="1990">1990</option>
				<option value="1989">1989</option>
				<option value="1988">1988</option>
				<option value="1987">1987</option>
				<option value="1986">1986</option>
				<option value="1985">1985</option>
				<option value="1984">1984</option>
				<option value="1983">1983</option>
				<option value="1982">1982</option>
				<option value="1981">1981</option>
				<option value="1980">1980</option>
				<option value="1979">1979</option>
				<option value="1978">1978</option>
				<option value="1977">1977</option>
				<option value="1976">1976</option>
				<option value="1975">1975</option>
				<option value="1974">1974</option>
				<option value="1973">1973</option>
				<option value="1972">1972</option>
				<option value="1971">1971</option>
				<option value="1970">1970</option>
				<option value="1969">1969</option>
				<option value="1968">1968</option>
				<option value="1967">1967</option>
				<option value="1966">1966</option>
				<option value="1965">1965</option>
				<option value="1964">1964</option>
				<option value="1963">1963</option>
				<option value="1962">1962</option>
				<option value="1961">1961</option>
				<option value="1960">1960</option>
				<option value="1959">1959</option>
				<option value="1958">1958</option>
				<option value="1957">1957</option>
				<option value="1956">1956</option>
				<option value="1955">1955</option>
				<option value="1954">1954</option>
				<option value="1953">1953</option>
				<option value="1952">1952</option>
				<option value="1951">1951</option>
				<option value="1950">1950</option>
				<option value="1949">1949</option>
				<option value="1948">1948</option>
				<option value="1947">1947</option>
				<option value="1946">1946</option>
				<option value="1945">1945</option>
				<option value="1944">1944</option>
				<option value="1943">1943</option>
				<option value="1942">1942</option>
				<option value="1941">1941</option>
				<option value="1940">1940</option>
				<option value="1939">1939</option>
				<option value="1938">1938</option>
				<option value="1937">1937</option>
				<option value="1936">1936</option>
				<option value="1935">1935</option>
				<option value="1934">1934</option>
				<option value="1933">1933</option>
				<option value="1932">1932</option> 
				<option value="1931">1931</option>
				<option value="1930">1930</option>
				<option value="1929">1929</option>
				<option value="1928">1928</option>
				<option value="1927">1927</option>
				<option value="1926">1926</option>
				<option value="1925">1925</option>
				<option value="1924">1924</option>
				<option value="1923">1923</option>
				<option value="1922">1922</option>
				<option value="1921">1921</option>
				<option value="1920">1920</option>
				<option value="1919">1919</option>
				<option value="1918">1918</option>
				<option value="1917">1917</option>
				<option value="1916">1916</option>
				<option value="1915">1915</option>
				<option value="1914">1914</option>
				<option value="1913">1913</option>
				<option value="1912">1912</option>
				<option value="1911">1911</option>
				<option value="1910">1910</option>
				<option value="1909">1909</option>
				<option value="1908">1908</option>
				<option value="1907">1907</option>
				<option value="1906">1906</option>
				<option value="1905">1905</option>
				<option value="1904">1904</option>
				<option value="1903">1903</option>
				<option value="1902">1902</option>
				<option value="1901">1901</option>
				<option value="1900">1900</option>
          </select>
        </div>
        <div class="m_combo_box">
          	<select class="combo_box" id="dob_month" name="dob_month" onkeypress="handleDateInputChange()" onmouseup="handleDateInputChange()" onchange="handleDateInputChange();"> 
            	<option value="0">Month</option>
				<option value="01">Jan</option>
				<option value="02">Feb</option>
				<option value="03">Mar</option>
				<option value="04">Apr</option>
				<option value="05">May</option>
				<option value="06">Jun</option>
				<option value="07">Jul</option>
				<option value="08">Aug</option>
				<option value="09">Sep</option>
				<option value="10">Oct</option>
				<option value="11">Nov</option>
				<option value="12">Dec</option>
          </select>
        </div>
        <div class="d_combo_box">
          	<select class="combo_box" id="dob_day" name="dob_day">
            	<option value="0">0</option>
            	<option value="01">1</option>
            	<option value="02">2</option>
            	<option value="03">3</option>
            	<option value="04">4</option>
            	<option value="05">5</option>
            	<option value="06">6</option>
            	<option value="07">7</option>
            	<option value="08">8</option>
            	<option value="09">9</option>
            	<option value="10">10</option>
            	<option value="11">11</option>
            	<option value="12">12</option>
            	<option value="13">13</option>
            	<option value="14">14</option>
            	<option value="15">15</option>
            	<option value="16">16</option>
            	<option value="17">17</option>
            	<option value="18">18</option>
            	<option value="19">19</option>
            	<option value="20">20</option>
            	<option value="21">21</option>
            	<option value="22">22</option>
            	<option value="23">23</option>
            	<option value="24">24</option>
            	<option value="25">25</option>
            	<option value="26">26</option>
            	<option value="27">27</option>
            	<option value="28">28</option>
            	<option value="29">29</option>
            	<option value="30">30</option>
            	<option value="31">31</option>
          </select>
        </div>
      </div>
    </div>
    <!-- STREET -->
    <div class="row">
      <div class="column_1">street :</div>
      <div class="t_box">
        <input type="text" class="text_box" id="id_street" name="street"   value="<%=hm.get("street")%>"/>
      </div>
    </div>
    <!-- AREA -->
    <div class="row">
      <div class="column_1">area :</div>
      <div class="t_box">
        <input type="text" class="text_box" id="id_area" name="area"   value="<%=hm.get("area")%>"/>
      </div>
    </div>
    <!-- CITY -->
    <div class="row">
      <div class="column_1">city :</div>
      <div class="t_box">
        <input type="text" class="text_box" id="id_city" name="city"   value="<%=hm.get("city")%>"/>
      </div>
    </div>
    <!-- STATE -->
    <div class="row">
      <div class="column_1">state :</div>
      <div class="t_box">
        <input type="text" class="text_box" id="id_state" name="state"   value="<%=hm.get("state")%>"/>
      </div>
    </div>
    <!-- COUNTRY -->
    <div class="row">
      <div class="column_1">country :</div>
      <div class="t_box_c">
    	<select name="div_combo6" class="combo_box"  id="div_combo6" style="width: 250px;" onchange="javascript:getProviderList(this);javascript:getCountryCode(this);" >
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
    <!-- PHONE -->
    <div class="row">
      <div class="column_1">phone :</div>
      <div class="t_box">
        <div class="country_code">
          <input type="text" class="text_box_cc" id="phoneext" name="phoneext" class="text_box_cc" value="<%=hm.get("phextn")%>" />
        </div>
        <div class="mobile_number">
          <input type="text" class="text_box_mn" id="id_ph" name="id_ph" value="<%=hm.get("phone")%>" />
        </div>
      </div>
    </div>
    <!-- MOBILE -->
    <div class="row">
      <div class="column_1">mobile :</div>
      <div class="t_box">
        <div class="country_code">
          <input type="text" class="text_box_cc" id="mobileext" name="mobileext" readonly="readonly" value="<%=hm.get("extn")%>" disabled="true"/>
        </div>
        <div class="mobile_number">
          <input type="text" class="text_box_mn" id="id_mob" name="id_mob"  value="<%=hm.get("mobile")%>"/><input  id="mobileext_hid" value="<%=hm.get("extn")%>" name="mobileext_hid" class="mob"  type="hidden">
        </div>
      </div>
    </div>
    <!-- PROVIDER -->
    <div class="row">
      <div class="column_1">provider :</div>
      <div class="t_box_c">
        <select class="combo_box" name="div_provider" id="div_provider"><option value="Select">Select</option></select>
      </div>
    </div>
    <!-- OCCUPATION -->
    <input type="hidden" id="hid_combo"/>
    <div class="row">
      <div class="column_1">occupation :</div>
      <div class="t_box_c">
        <select name="occupation" id="occupation" class="combo_box">
				<option value="0">Choose your Profession</option>
                <option value="Accounting/Finance">Accounting/Finance</option>
                <option value="Administration">Administration</option>
                <option value="Advertising">Advertising</option>
                <option value="Business Development">Business Development</option>
                <option value="Consultant">Consultant</option>
                <option value="Creative Services/Design">Creative Services/Design</option>
                <option value="Customer Service/Support">Customer Service/Support</option>
                <option value="Engineering">Engineering</option>
                <option value="Health Services">Health Services</option>
                <option value="Human Resources/Training">Human Resources/Training</option>
                <option value="Information Technology">Information Technology</option>
                <option value="Legal">Legal</option>
                <option value="Management, General">Management, General</option>
                <option value="Manufacturing">Manufacturing</option>
                <option value="Marketing">Marketing</option>
                <option value="Operations">Operations</option>
                <option value="Production">Production</option>
                <option value="Public Relations">Public Relations</option>
                <option value="Quality Assurance">Quality Assurance</option>
                <option value="Research">Research</option>
                <option value="Sales">Sales</option>   													
		</select>
      </div>
    </div>
    <!-- E - OCCUPATION -->
    <div class="row">
      <div class="column_1">e - occupation :</div>
      <div class="check_box" style="width:20px; overflow:hidden; float:left;">
        <input type="checkbox" id="echeck" name="echeck" onclick="javascript:viewEoccupationList()" />
      </div>
      <div class="t_box" style="width:237px; float:left;">
        <select class="combo_box" id="eoccupation" name="eoccupation" style="width:232px; font-family:'trebuchet MS'; font-size:12px; font-weight:normal; text-align:left; text-decoration:none; text-transform:none; color:#00688b;">
          	<option value="0">Choose your Profession</option>
          	<option value="Police">Police</option>
           	<option value="Doctor">Doctor</option>
           	<option value="Army">Army</option>
           	<option value="Navy">Navy</option>
           	<option value="Airforce">Airforce</option>
          	<option value="Advertising">Advertising</option>
        </select>
      </div>
      <div class="w_box">
<!--      	<a href="#" class="text">what is this?</a>-->
      </div>
    </div>
    <!-- ZIP CODE -->
    <div class="row" style="padding-top:4px;">
      <div class="column_1">zip code :</div>
      <div class="t_box">
        <input type="text" class="text_box" id="id_Zip"  name="id_Zip" value="<%=hm.get("zip")%>" />
      </div>
    </div>
    <!-- REGISTER -->
    	                                    
    	<div class="row">
        	<div class="button">
        		<div style="float:right;"><input type="reset" value="Reset"/></div><div style="float:right; padding-right:10px;"> <input type="submit" value="Save"/></div>
        	</div>
        </div>
   </form>
  </div>
  
  <!-- Medical -->
  
  <div class="form_ep" id="medical">
  <form id="editfrm" action="./MedicalInfo.ice" method="post" onsubmit="return medicalInfo(this);">
  <div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
  	<div id="error"><div id="med_feedback_content"> </div></div>
  </div>
    <!-- BLOOD GROUP -->
    <div class="row">
      <div class="column_1">blood group :</div>
      <div class="t_box_c">
        <select class="combo_box" id="bloodgroup" name="bloodgroup">
          	<option value="0">Choose your Blood Group</option>
          	<option value="O+">O+</option>
           	<option value="A+">A+</option>
           	<option value="B+">B+</option>
           	<option value="AB+">AB+</option>
           	<option value="O-">O-</option>
           	<option value="A-">A-</option>
          	<option value="B-">B-</option>
          	<option value="AB-">AB-</option>
        </select>
      </div>
    </div>
    <!-- ALLERGIES -->
    <input type="hidden" name="allergies" id="allergies" />
    <div class="row" style="height:60px">
      <div class="column_1">allergies :</div>
      <div class="t_box_c" style="height:60px">
        <select class="combo_box" id="allergiess" name="allergiess" multiple="multiple" size="4" style="height:60px">
	        <option value="none">None</option>
	        <option value="adenoidectomy">adenoidectomy</option>
		   	<option value="adenoid">adenoid</option>
		    <option value="allergen">allergen</option>
		    <option value="allergen immunotherapy">allergen immunotherapy</option>
		    <option value="allergic rhinitis">allergic rhinitis</option>
		    <option value="allergy">allergy</option>
		    <option value="anaphylaxis">anaphylaxis</option>
		    <option value="angioedema">angioedema</option>
		    <option value="antibody">antibody</option>
		    <option value="antigen">antigen</option>
		    <option value="antihistamines">antihistamines</option>
		    <option value="antiinflammatory">antiinflammatory</option>
		    <option value="asthma">asthma</option>
		    <option value="bronchodilator">bronchodilator</option>
		    <option value="bronchospasm">bronchospasm</option>
		    <option value="conjunctivitis">conjunctivitis</option>
		   	<option value="dander">dander</option>
		   	<option value="decongestant">decongestant</option>
		   	<option value="dermatitis">dermatitis</option>
		   	<option value="desensitization">desensitization</option>
		   	<option value="drug allergy">drug allergy</option>
		   	<option value="eczema">eczema</option>
		    <option value="elimination diet">elimination diet</option>
		    <option value="endoscopy">endoscopy</option>
		    <option value="enzyme-linked immunosorbent assay">enzyme-linked immunosorbent assay</option>
		    <option value="eosinophil">eosinophil</option>
		    <option value="epinephrine">epinephrine</option>
		    <option value="erythema">erythema</option>
		    <option value="hay fever">hay fever</option>
		   	<option value="histamine">histamine</option>
		   	<option value="hives">hives</option>
		   	<option value="immune system">immune system</option>
		   	<option value="immunotherapy">immunotherapy</option>
		   	<option value="latex">latex</option>
		    <option value="mast cell">mast cell</option>
		    <option value="metered-dose inhaler">metered-dose inhaler</option>
		    <option value="mold">mold</option>
		    <option value="myringotomy">myringotomy</option>
		    <option value="otitis media">otitis media</option>
		    <option value="otolaryngologist">otolaryngologist</option>
		   	<option value="otoscope">otoscope</option>
		   	<option value="pollen">pollen</option>
		   	<option value="pulmonary function test">pulmonary function test</option>
		   	<option value="radioallergosorbent test">radioallergosorbent test</option>
		   	<option value="Schnitzler syndrome">Schnitzler syndrome</option>
		  	<option value="sinusitis">sinusitis</option>
		  	<option value="tympanometry">tympanometry</option>
		   	<option value="urticaria">urticaria</option>
			<option value="vasculitis">vasculitis</option>
	    </select>
      </div>
    </div>
    <!-- MEDICINES -->
    <div class="row">
      <div class="column_1">medicine :</div>
      <div class="t_box">
        <input type="text" class="text_box" id="medicines" name="medicines" value="<%=hm.get("meds")%>"/>
      </div>
    </div>
    <!-- DISEASE -->
    <input type="hidden" name="disease" id="disease" />
    <div class="row" style="height:60px">
      <div class="column_1">disease :</div>
      <div class="t_box_c" style="height:60px">
        <select class="combo_box" id="diseases" name="diseases" multiple="multiple" size="4" style="height:60px">
             	<option value="none">None</option>
             	<option value="Alzheimer">Alzheimer</option>
             	<option value="Asthma">Asthma</option>
             	<option value="Cancer">Cancer</option>
             	<option value="Cholesterol">Cholesterol</option>
             	<option value="Chronic Pain">Chronic Pain</option>
             	<option value="Cold">Cold</option>
             	<option value="Diabetes">Diabetes</option>
             	<option value="Hepatitis">Hepatitis C</option>
             	<option value="HIV">HIV</option>
             	<option value="AIDS">AIDS</option>
             	<option value="Osteoporosis">Osteoporosis</option>
             	<option value="Rheumatoid Arthritis">Rheumatoid Arthritis</option>
             	<option value="Thyroid">Thyroid</option>
             	<option value="Urology">Urology</option>
             	<option value="Hemochromatosis">Hemochromatosis</option>
             	<option value="Hepatitis B">Hepatitis B</option>
             	<option value="Jaundice">Jaundice</option>
             	<option value="Primary Biliary Cirrhosis (PBC)">Primary Biliary Cirrhosis (PBC)</option>
             	<option value="Primary Sclerosing Cholangitis (PSC)">Primary Sclerosing Cholangitis (PSC)</option>
             	<option value="Chronic Obstructive Pulmonary Disease (COPD)">COPD</option>
             	<option value="Emphysema">Emphysema</option>
             	<option value="Pneumonia">Pneumonia</option>
             	<option value="Severe Acute Respiratory Syndrome (SARS)">SARS</option>
             	<option value="Angina">Angina</option>
             	<option value="Benign Prostatic Hyperplasia (BPH)">Benign Prostatic Hyperplasia (BPH)</option>
             	<option value="Erectile Dysfunction (Impotence)">Erectile Dysfunction (Impotence)</option>
             	<option value="Prostatitis">Prostatitis</option>
             	<option value="Sexually Transmitted Diseases">Sexually Transmitted Diseases</option>
             	<option value="Attention Deficit Disorder (ADD)">Attention Deficit Disorder (ADD)</option>
             	<option value="Chickenpox">Chickenpox</option>
             	<option value="Colic">Colic</option>
             	<option value="Lactose Intolerance">Lactose Intolerance</option>
             	<option value="Measles">Measles</option>
             	<option value="Mumps">Mumps</option>
             	<option value="Tonsillectomy">Tonsillectomy</option>
             	<option value="Vaccinations">Vaccinations</option>
             	<option value="Immunizations">Immunizations</option>
             	<option value="Chronic Rhinitis">Chronic Rhinitis</option>
             	<option value="Dementia">Dementia</option>
             	<option value="Ankylosing Spondylitis">Ankylosing Spondylitis</option>
             	<option value="Fibromyalgia">Fibromyalgia</option>
             	<option value="Gout">Gout</option>
             	<option value="Lupus">Lupus</option>
             	<option value="Osteoarthritis">Osteoarthritis</option>
             	<option value="Psoriatic Arthritis">Psoriatic Arthritis</option>
             	<option value="Reactive Arthritis">Reactive Arthritis</option>
             	<option value="Rheumatoid Arthritis">Rheumatoid Arthritis</option>
             	<option value="Acupuncture">Acupuncture</option>
             	<option value="Encephalitis and Meningitis">Encephalitis and Meningitis</option>
             	<option value="Flu(Influenza)">Flu(Influenza)</option>
             	<option value="Pneumonia">Pneumonia</option>
             	<option value="SARS">SARS</option>
             	<option value="Sinusitis">Sinusitis</option>
             	<option value="Tonsillitis">Tonsillitis</option>
             	<option value="Adenoiditis">Adenoiditis</option>
             	<option value="Diabetes Mellitus">Diabetes Mellitus</option>
             	<option value="Appendicitis">Appendicitis</option>
             	<option value="Ulcerative Colitis">Ulcerative Colitis</option>
             	<option value="Constipation">Constipation</option>
             	<option value="Diarrhea">Diarrhea</option>
             	<option value="Dyspepsia (Indigestion)">Dyspepsia (Indigestion)</option>
             	<option value="Inflammatory Bowel Disease (IBD)">Inflammatory Bowel Disease (IBD)</option>
             	<option value="Intestinal Gas">Intestinal Gas</option>
             	<option value="GERD (Heartburn, Acid Reflux)">GERD (Heartburn, Acid Reflux)</option>
             	<option value="Hemorrhoids">Hemorrhoids</option>
             	<option value="Irritable Bowel Syndrome (IBS)">Irritable Bowel Syndrome (IBS)</option>
             	<option value="Laxatives for Constipation">Laxatives for Constipation</option>
             	<option value="Ulcerative Colitis">Ulcerative Colitis</option>
             	<option value="Glaucoma">Glaucoma</option>
             	<option value="Conjunctivitis">Conjunctivitis</option>
             	<option value="Sjogren's Syndrome">Sjogren's Syndrome</option>
      		</select>
      </div>
    </div>
    <!-- CONDITIONS -->
    <div class="row">
      <div class="column_1">conditions :</div>
      <div class="t_box">
        <input type="text"  class="text_box" id="conditions" name="conditions"   value="<%=hm.get("condition")%>"/>
      </div>
    </div>
    <!-- REGISTER -->
    	                                    
    	<div class="row">
        	<div class="button">
        		<div style="float:right;"><input type="reset" value="Reset"/></div><div style="float:right; padding-right:10px;"> <input type="submit" value="Save"/></div>
        	</div>
        </div>
    </form>
  </div>
  
  <!-- End --> 
  
  <!-- Change Image -->
  <div class="change_im" id="general" >
		<div class="form_upload">
			<div class="row01">
            	<div class="sub_header">
                	<div class="text">You can upload JPG, GIF, BMP or PNG file.</div>
                </div>
                <div class="slogan">
                	<div class="text">(Do not upload pictures containing celebrities, nudity, artwork or copyright images.)</div>
                </div><hr color="#00688b" />
            </div>
             	
         	<div class="usr_photo">
            	<div class="photo">
            		<div class="image">
               			<IMG SRC="photo.ice" height="74px" width="74px">
               		</div>
               		<logic:present name="user_photo">
               			<logic:equal name="user_photo" value="user">
               				<div class="delete"><a href="javascript:deletePhotoSelect();" class="text">Delete</a></div> 
               			</logic:equal>
               		</logic:present> 
           		</div>
     		</div>
     		<div class="text">
<!--        			<a href="javascript:showPhotoSelect();" style="color:#00688B;cursor:pointer;font-family:'trebuchet MS';font-size:12px;font-weight:bold;text-align:left;text-decoration:none;text-transform:capitalize;">Change</a>-->
				<%
					if(session.getAttribute("user_photo")!=null)
					if(session.getAttribute("user_photo").toString().equalsIgnoreCase("user"))
					{
				%>
					
				<%}%>
            	
            	<form id='d'  action="./updatephoto.ice"  method="post"  enctype="multipart/form-data" onsubmit="return validate()" style="overflow:hidden;">
            		<input name=aprp type="hidden" value="1">
            		<div class="row02">
            			<div class="description">
	               			<div class="text">Select Picture :</div>
               			</div>
               			<div class="t_box">
		               		<input name=file id=file type=file onkeyup=myfunction() style="overflow: hidden;" class="text_box"> 
    	           		</div> 
        	   		</div>
            	 	<div class="row03">
                		<div class="button">
                			<input type="submit" value="Upload" />
                		</div>                                            
             		</div>
            	</form>
         	</div>                      
		</div>
	</div>

  <!-- Change Password -->
  <div class="form_cp" id="pass">
  	
 	<form id="passfrm" action="./PasswordManager.ice" method="post" onsubmit="return changepass(this);">
    	<div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
  			<div id="error"><div id="pass_feedback_content"> </div></div>
  		</div>
    	<!-- OLD PASSWORD -->
        
        <div class="row">
         	<div class="column_1">old password :</div>
            <div class="t_box">
            	<input type="password" class="text_box" id="id_cur_pass" name="id_cur_pass"/>
           	</div>
      	</div>
                                        
        <!-- NEW PASSWORD -->
                                        
        <div class="row">
        	<div class="column_1">new password :</div>
            <div class="t_box">
            	<input type="password" class="text_box" id="id_new_pass" name="id_new_pass"/>
          	</div>
      	</div>
                                       
        <!-- RETYPE PASSWORD -->
        	                                
        <div class="row">
        	<div class="column_1">retype password :</div>
            <div class="t_box">
            	<input type="password" class="text_box" id="id_new_pass1"  name="id_new_pass1"/>
          	</div>
      	</div> 
                                        
        <!-- BUTTON -->
                                        
        <div class="row">
        	<div class="button">
            	<div style="float:left; padding-right:10px;">
            		<input type="submit" value="Save"/>
            	</div>
            	<div style="float:left;">
            		<input type="button" value="Cancel"/>
            	</div>
           	</div>
      	</div>                                       
	</form>
  	</div>
</div>
<div style="display:none">
	<form id="delete" action="./delete.ice">
		<input type="submit"/>
	</form>
</div>
<div style="display:none">
	<form id="myform" target="_parent" action="./UploadSettings.ice">
		<input type="submit"/>
	</form>
</div>
<%
	if(request.getAttribute("flag")!=null)
	{
		int flag=Integer.parseInt(request.getAttribute("flag").toString());
		if(flag==2)
		{
			out.print("<script>initSettings(1)</script>");
		}
		if(flag==5)
		{
			out.print("<script>initSettings(2)</script>");
		}
	}
	else
	{
		out.print("<script>initSettings(0)</script>");
	}
%>             
</body>
<% 
	if(request.getParameter("personal")!=null)
	{
		out.print("<script>initSettings(0);</script>");
	}
%>
<% 
	if(request.getParameter("medical")!=null)
	{
		out.print("<script>initeditSettings(1);</script>");
	}
%>
<% 
	if(request.getParameter("pass")!=null)
	{
		out.print("<script>initSettings(1);</script>");
	}
%>
<% 
	if(request.getParameter("image")!=null)
	{
		out.print("<script>initSettings(2);</script>");
	}
%>

</html>

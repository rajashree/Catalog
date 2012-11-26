function $(id){
	return document.getElementById(id);
}

function displayContent(){
	var value;
	var str;
	switch($("search").value)
	{
		case("none"):
					$("hidden").value = 0;
					$("msghidden").value = 0;
					$("global").style.display="none";
					$("datacont").innerHTMl="";
					$("datacont").style.display="none";
					alert("Select any option");
					break;
		case("all"):
					$("hidden").value = 1;
					$("msghidden").value = 1;
					$("datacont").innerHTMl="";
					$("datacont").style.display="none";
					$("global").style.display="none";
					break;
		case("community"):$("hidden").value = 2;
						$("msghidden").value = 2;
						$("datacont").innerHTMl="";
						$("datacont").style.display="none";
						$("searchnamediv").innerHTML="<div class='text'>Enter Community Name :</div>";
						$("global").style.display="block";
						$("searchphasediv").style.display="block";
						$("searchphasediv").innerHTML='<input type="text" class="text_box" id="searchphase" name="searchphase"/>';
						$("searchbuttondiv").style.display="block";
					 break;
		case("name"):$("hidden").value = 3; 
					 $("msghidden").value = 3;
					 $("datacont").innerHTMl="";
					 $("datacont").style.display="none";
					 $("searchnamediv").innerHTML="<div class='text'>Enter Name :</div>";
					 $("global").style.display="block";
					 $("searchphasediv").style.display="block";
					 $("searchphasediv").innerHTML='<input type="text" class="text_box" id="searchphase" name="searchphase"/>';
					 $("searchbuttondiv").style.display="block";
					 break;
		case("mobile"): $("hidden").value = 4;
						$("msghidden").value = 4;
						$("datacont").innerHTMl="";
						$("datacont").style.display="none";
						$("searchnamediv").innerHTML="<div class='text'>Enter Mobile Number :</div>";
						$("global").style.display="block";
						$("searchphasediv").style.display="block";
					 	$("searchphasediv").innerHTML='<input type="text" class="text_box" id="searchphase" name="searchphase"/>';
					 	$("searchbuttondiv").style.display="block";
					 break;
		case("email"): $("hidden").value = 5;
					 	$("msghidden").value = 5;
					 	$("datacont").innerHTMl="";
					 	$("datacont").style.display="none";
					 	$("searchnamediv").innerHTML="<div class='text'>Enter Email ID :</div>";
						$("global").style.display="block";
						$("searchphasediv").style.display="block";
					 	$("searchphasediv").innerHTML='<input type="text" class="text_box" id="searchphase" name="searchphase"/>';
					 	$("searchbuttondiv").style.display="block";
					 break;
		case("country"): $("hidden").value = 6;
						 $("msghidden").value = 6;
						 $("datacont").innerHTMl="";
						 $("global").style.display="block";
						 $("datacont").style.display="none";
						 $("searchnamediv").innerHTML="<div class='text'>Select a Country :</div>";
						 $("searchphasediv").style.display="block";
						str='<select id="searchphase" name="searchphase" class="combobox">'
								+'<option value="1">Afghanistan</option><option value="2">Albania</option>'
								+'<option value="3">Algeria</option><option value="4">Argentina </option>'
								+'<option value="5">Australia </option><option value="6">Antarctica</option>'
								+'<option value="7">Austria</option><option value="8">Bahrain</option>'
								+'<option value="9">Bangladesh</option><option value="10">Belgium</option>'
								+'<option value="11">Bhutan</option><option value="12">Bolivia</option>'
								+'<option value="13">Bosnia &amp;  Herzegovina</option><option value="14">Botswana</option>'
								+'<option value="15">Brazil</option><option value="16">Bulgaria</option>'
								+'<option value="17">Burkina Faso</option><option value="18">Cambodia</option>'
								+'<option value="19">Canada</option><option value="20">Chile</option>'
								+'<option value="21">China</option><option value="co">Colombia</option>'
								+'<option value="23">Congo</option><option value="24">Cuba</option>'
								+'<option value="25">Czech Republic</option><option value="dj">Djibouti</option>'
								+'<option value="dm">Dominica</option><option value="do">Dominican Republic</option>'
								+'<option value="tp">East Timor</option><option value="gq">Equatorial Guinea</option>'
								+'<option value="er">Eritrea</option><option value="ee">Estonia</option>'
								+'<option value="et">Ethiopia</option><option value="fk">Falkland Islands</option>'
								+'<option value="fo">Faroe Islands</option><option value="fj">Fiji</option>'
								+'<option value="gf">French Guiana</option><option value="ge">Georgia</option>'
								+'<option value="gh">Ghana</option><option value="gi">Gibraltar</option>'
								+'<option value="gl">Greenland</option><option value="gd">Grenada</option>'
								+'<option value="gp">Guadeloupe</option><option value="gu">Guam</option>'
								+'<option value="gt">Guatemala</option><option value="gn">Guinea</option>'
								+'<option value="gy">Guyana</option><option value="ht">Haiti</option>'
								+'<option value="hn">Honduras</option><option value="is">Iceland</option>'
								+'<option value="in">India</option><option value="ir">Iran</option>'
								+'<option value="iq">Iraq</option><option value="jm">Jamaica</option>'
								+'<option value="kz">Kazakhstan</option><option value="ke">Kenya</option>'
								+'<option value="kw">Kuwait</option><option value="ly">Libya</option>'
								+'<option value="mu">Mauritius</option><option value="me">Mexico</option>'
								+'<option value="mn">Mongolia</option><option value="mm">Myanmar</option>'
								+'<option value="na">Namibia</option><option value="np">Nepal</option>'
								+'<option value="ng">Nigeria</option><option value="pa">Panama</option>'
								+'<option value="lk">Sri Lanka</option><option value="sy">Syria</option>'
								+'<option value="si">Syria</option><option value="tj">Tajikistan</option>'
								+'<option value="tz">Tanzania</option><option value="ug">Uganda</option>'
								+'<option value="uk">United Kingdom(UK)</option><option value="us">USA</option>'
								+'<option value="vn">Vietnam</option><option value="zw">Zimbabwe</option>'
						+'</select>';						
						$("searchphasediv").innerHTML = str;
					 break;
					 
		case("occupation"): $("hidden").value = 7;
						 $("msghidden").value = 7;
						 $("datacont").innerHTMl="";
						 $("datacont").style.display="none";
						 $("global").style.display="block";
						 $("searchnamediv").innerHTML="<div class='text'>Select Occupation :</div>";
						 $("searchphasediv").style.display="block";
					str='<select  id="searchphase" name="searchphase">'
						+'<option value="0">Choose your Profession</option>'
                        +'<option value="Accounting/Finance">Accounting/Finance</option>'
                        +'<option value="Administration">Administration</option>'
                        +'<option value="Advertising">Advertising</option>'
                        +'<option value="Business Development">Business Development</option>'
                        +'<option value="Consultant">Consultant</option>'
                        +'<option value="Creative Services/Design">Creative Services/Design</option>'
                        +'<option value="Customer Service/Support">Customer Service/Support</option>'
                        +'<option value="Engineering">Engineering</option>'
                        +'<option value="Health Services">Health Services</option>'
                        +'<option value="Human Resources/Training">Human Resources/Training</option>'
                        +'<option value="Information Technology">Information Technology</option>'
                        +'<option value="Legal">Legal</option>'
                        +'<option value="Management, General">Management, General</option>'
                        +'<option value="Manufacturing">Manufacturing</option>'
                        +'<option value="Marketing">Marketing</option>'
                        +'<option value="Operations">Operations</option>'
                        +'<option value="Production">Production</option>'
                        +'<option value="Public Relations">Public Relations</option>'
                        +'<option value="Quality Assurance">Quality Assurance</option>'
                       	+'<option value="Research">Research</option>'
                        +'<option value="Sales">Sales</option>'							
						+'</select>';
						$("searchphasediv").innerHTML = str;
					 break;
	}
	return true;
}

function validateForm(){
	switch($("search").value){
		case "community":
			if( $("searchphase").value == "")
			{
				feedback.show('Community Name should not be null.',1);
				document.getElementById("searchphase").focus();
				return (false);
			}
			break;
		case "name":
			if( $("searchphase").value == "")
			{
				feedback.show('User Name should not be null.',1);
				document.getElementById("searchphase").focus();
				return (false);
			}
			break;
		case "mobile":
			if( $("searchphase").value == "")
			{
				feedback.show('Mobile Number should not be null.',1);
				document.getElementById("searchphase").focus();
				return (false);
			}
			getMobileNo = document.getElementById("searchphase").value;
			var check = "0123456789";
       		var checkdigits = getMobileNo;
       		var allValidnos = true;
       		for (i = 0;  i < checkdigits.length;  i++)
			{
				ch = checkdigits.charAt(i);
				for (j = 0;  j < check.length;  j++)
					if (ch == check.charAt(j))
						break;
					if (j == check.length)
					{
						allValidnos = false;
						break;
					}
			}
			if (!allValidnos)
			{
				feedback.show("Please enter only digits \'0123456789\' for mobile",1);
				document.getElementById("searchphase").focus();
		      	return false;
		    } 
		    
		    if(getMobileNo.length != 10){
		        feedback.show('Mobile Number should be 10 digits',1);
	    	   	document.getElementById("searchphase").focus();
	        	return false;
	        }
			break;			
		case "email":
			getEmail= $("searchphase").value;
			if( getEmail == "")
			{
				feedback.show('Email Address should not be null.',1);
				document.getElementById("searchphase").focus();
				return (false);
			}
			if(getEmail.isEmail() ) {
			}else{
				feedback.show('Sorry, your entry is not a valid email address.',1);
				document.getElementById("searchphase").focus();
				return (false);
			}
			break;
		case "country":
			if( $("searchphase").value == "0")
			{
				feedback.show('Please Select a Country.',1);
				document.getElementById("searchphase").focus();
				return (false);
			}
			break;
		case "occupation":
			if( $("searchphase").value == "0")
			{
				feedback.show('Please Select one Occupation.',1);
				document.getElementById("searchphase").focus();
				return (false);
			}
			break;
	}
}

function initAlert(h,c,sk){
	$("msghidden").value=h;
	switch(h){
		case 0:
			$("global").style.display="none";
			$("search").value="none";			
			break;
		case 1:
			$("global").style.display="none";
			$("search").value="all";
			break;
		case 2:
			$("global").style.display="block";
			//$("searchphasediv").style.display="none";	
			//$("searchbuttondiv").style.display="none";						
			//$("searchnamediv").innerHTML="<div class='text'>Given Search phase is :: "+s+"</div>";
			$("search").value="community";
			$("searchphase").value=sk;
			break;
		case 3:
			$("global").style.display="block";
			//$("searchphasediv").style.display="none";
			//$("searchbuttondiv").style.display="none";	
			//$("searchnamediv").innerHTML="<div class='text'>Given Search phase is :: "+s+"</div>";
			$("search").value="name";
			$("searchphase").value=sk;
			break;
		case 4:
			$("global").style.display="block";
			//$("searchphasediv").style.display="none";
			//$("searchbuttondiv").style.display="none";	
			//$("searchnamediv").innerHTML="<div class='text'>Given Search phase is :: "+s+"</div>";
			$("search").value="mobile";
			$("searchphase").value=sk;
			break;
		case 5:
			$("global").style.display="block";
			//$("searchphasediv").style.display="none";
			//$("searchbuttondiv").style.display="none";	
			//$("searchnamediv").innerHTML="<div class='text'>Given Search phase is :: "+s+"</div>";
			$("search").value="email";
			$("searchphase").value=sk;
			break;
		case 6:
			$("global").style.display="block";
			//$("searchphasediv").style.display="none";
			//$("searchbuttondiv").style.display="none";	
			//$("searchnamediv").innerHTML="<div class='text'>Given Search phase is :: "+s+"</div>";
			$("search").value="country";
			$("searchphase").value=sk;
			break;
		case 7:
			$("global").style.display="block";
			//$("searchphasediv").style.display="none";
			//$("searchbuttondiv").style.display="none";	
			//$("searchnamediv").innerHTML="<div class='text'>Given Search phase is :: "+s+"</div>";
			$("search").value="occupation";
			$("searchphase").value=sk;
			break;
	}
	
	$("count").value=c;
}

function validateSMS(){
	if($("search").value=="none")
	{
		alert("Select any option");
		return false;
	}
	if($("smssub").value=="")
	{
		 feedback.show('Subject should not be null',1);
   	   	 $("smssub").focus();
   	   	 return false;
	}
	if($("smsdata").value=="")
	{
		 feedback.show('Message data should not be null',1);
   	   	 $("smsdata").focus();
   	   	 return false;
	}	
	
	if(($("search").value=="community") || ($("search").value=="name") || ($("search").value=="mobile") || ($("search").value=="email") || ($("search").value=="country")|| ($("search").value=="occupation"))
	{
		
		count=$("count").value;
		if(count==0)
		{
			alert("No users..");
			return false;
		}
		var q="";
		var qs="";
		var j=1;
		if(count == 1)
		{
			if($("myform").Check.checked)
			{
				qs=qs+$("myform").Check.value;
				j=1;
			}		
			else{
				j=0;
			}	
		}
	    else
		{
			for (var i = 0; i < count; i++)
			{
  				if($("myform").Check[i].checked)
  				{
      				if(qs!="")
      				{
       					qs=qs+"|"+q+$("myform").Check[i].value;
       					j++;
       				}
       				else
       				{
       					qs=qs+q+$("myform").Check[i].value;
       					j++;
       				}
     			} 
  			} 
  			j=j-1;
  		}
  		if(j == 0)/// Start here
  		{
  			alert("Select One Record");
  			return false;
  		}
  		else
  		{
  			$("cnt").value=j;
  			$("stdata").value=qs;
  		}
	}
}

function selecteveryalert(){
	count=$("count").value;
	if(count == 1)
	{
		$("myform").Check.checked=$("glbid").checked;
	}
	else
	 	for (var i = 0; i < count; i++)
		{
			$("myform").Check[i].checked=$("glbid").checked;
		}
}

function activatediv(val)
{
	if(val==0){
		$("sendalert_div").style.display="block";
		$("feedbackdiv").style.display="none";
		$("bugdiv").style.display="none";
		
		$("sendalert_link").className="text_active";
		$("feedbacks_link").className="text";
		$("bugs_link").className="text";
	}
	else if(val==1){
		$("sendalert_div").style.display="none";
		$("feedbackdiv").style.display="block";
		$("bugdiv").style.display="none";
		
		$("sendalert_link").className="text";
		$("feedbacks_link").className="text_active";
		$("bugs_link").className="text";
	}
	else if(val==2){
		$("sendalert_div").style.display="none";
		$("feedbackdiv").style.display="none";
		$("bugdiv").style.display="block";
				
		$("sendalert_link").className="text";
		$("feedbacks_link").className="text";
		$("bugs_link").className="text_active";
	}

}

function selectevery(){
	//count=$("count").value;
	if($("count").value == 1)
	{
		$("fromone").Check.checked=$("glbid").checked;
	}
	else
	 	for (var i = 0; i < $("count").value; i++)
		{
			$("fromone").Check[i].checked=$("glbid").checked;
		}
}
function alertCheck(val)
{
	if(val==1)
	{
		$("glbid").checked=true;
		$("all_link").className="text_active";
		$("none_link").className="text";
	}
	if(val==2)
	{
		$("glbid").checked=false;
		$("all_link").className="text";
		$("none_link").className="text_active";	
	}
	selectevery();
}

function getfid(val){
	$("feedid").value=val;
	$("feedfrm").submit();
}

function getbid(val){
	$("bugid").value=val;
	$("bugfrm").submit();
}

function admin_del()
{
	count=$("count").value;
	var q_del="";
	if(count == 1)
	{
		if($("fromone").Check.checked)
		{
			q_del+=$("fromone").Check.value;
		}		
	}
	else
	{
	 	for (var i = 0; i < count; i++)
		{
			if($("fromone").Check[i].checked)
				q_del+=$("fromone").Check[i].value+" ";
		}
	}
	if(q_del=="")
		alert("Select one of them");
	else
	{
		$("method").value=$("method_dummy").value;
		$("adelinfo").value=q_del;
		$("deleterfrm").submit();
	}
}

function viewactivatediv(val)
{
	if(val==0){
		$("sendalert_link").className="text";
		$("feedbacks_link").className="text_active";
		$("bugs_link").className="text";
	}
	else if(val==1){
		$("sendalert_link").className="text";
		$("feedbacks_link").className="text";
		$("bugs_link").className="text_active";
	}

}

function alert_del()
{
	$("method").value=$("method_dummy").value;
	$("cdelinfo").value=$("id_dummy").value;
	$("deletecfrm").submit();
	
}


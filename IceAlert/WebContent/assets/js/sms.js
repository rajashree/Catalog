var currentProvider;
var glob;
var search_rec;
function changepass(){
	cur_pass = $("id_cur_pass").value;
	new_pass = $("id_new_pass").value;
	new_pass1 = $("id_new_pass1").value;
	
	if(cur_pass.length < 4){
        feedback.show('Current password should be >=4 digits',1);
   	   	document.getElementById("id_cur_pass").focus();
       	return false;
    }
    
    if(new_pass.length < 4){
        feedback.show('New password should be >=4 digits',1);
   	   	document.getElementById("id_new_pass").focus();
       	return false;
    }
    
    if(new_pass != new_pass1){
    	feedback.show('Both new passwords should match',1);
   	   	document.getElementById("id_new_pass").focus();
       	return false;
    }
}

function encode( uri ) {
    if (encodeURIComponent) {
        return encodeURIComponent(uri);
    }

    if (escape) {
        return escape(uri);
    }
}

function callback(){
var load;
    if (app.readyState == 4){
        if (app.status == 200){
        	    	load = app.responseXML.getElementsByTagName("Result")[0];
        	    	alert(load.childNodes.length);
            $("tab_search").style.display="block";
            if(load.childNodes.length == 0)
	    	{
	    		alert("NO Matches Found");
				return;
	    	}
	    	else
	    	{
   	           var str="<table>";
   	           search_rec=load.childNodes.length
	    		for(i=0;i<load.childNodes.length;i++)
	    			str+="<tr><td><input type='checkbox' id='idk' name='id' value='"+load.childNodes[i].childNodes[0].childNodes[0].nodeValue+"'></td><td>"+load.childNodes[i].childNodes[1].childNodes[0].nodeValue+"</td><td>"+load.childNodes[i].childNodes[2].childNodes[0].nodeValue+"</td><td>"+load.childNodes[i].childNodes[3].childNodes[0].nodeValue+"</td><td>"+load.childNodes[i].childNodes[4].childNodes[0].nodeValue+"</td><td>"+load.childNodes[i].childNodes[5].childNodes[0].nodeValue+"</td></tr>";
	    			str+="<tr><td class='clr'>SMS :</td><td><textarea class='inputclass' id='id_sms' style='width: 250px;' type='text'></textarea></td></tr><tr><td><input  value='Send' type='submit' class='btn'></td><td></td></tr></table>";
	    		 $("tab_search").innerHTML=str;
	    	}
		}
    }
}



function sendSMS(){
		var q="";
		var qs="";
		var j=0;
		if(search_rec == 0)
		{
			feedback.show('Please Select one',1);
			return false;
		}
		if($(id_sms).value!="")
		{
			if(search_rec == 1)
			{
				if(smsfrm.id[0].checked)
				{
					qs="id"+j+"="+smsfrm.id[0].value;
				}
			}
			else
			{
	
				for (var i = 0; i < search_rec; i++)
				{
							
					if(smsfrm.id[i].checked)
					{
						if(qs!="")
							qs=qs+"|"+"id"+j+"="+smsfrm.id[i].value;
						else
							qs="id"+j+"="+smsfrm.id[i].value;
						j++;
					}
				}
				j=j-1;
			}
		}
		else
		{
			feedback.show('Please Write SMS',1);
			return false;
		}
		if(qs=="")
		{
			feedback.show('Please select a user',1);
			return false;
		}
		else
		{	
			document.smsfrm_name.id_list.value=qs;
			return true;
		}

}
function doUpdate(){	
	glob=1;	
}
function accountInfo(){

		newFirstName = $("id_firstname").value;
		newLastName = $("id_lastname").value;
		newMailId = $("id_Email").value;
		newCountryCode = $("mobileext").value;
		newMobileNo = $("id_mob").value;
		newProvider = $("div_provider").value;
		newCountry = $("div_combo6").value;
		newZipCode = $("id_Zip").value;
		if(glob!=1)
			newPassword=$("id_pass").value;
         //FIRST NAME
        getTextFirstName = document.getElementById("id_firstname").value;
       	var checkOK = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
       	var checkStr = getTextFirstName;
       	var allValid = true;
       	for (i = 0;  i < checkStr.length;  i++)	{
			ch = checkStr.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
				if (ch == checkOK.charAt(j))
					break;
				if (j == checkOK.length){
					allValid = false;
					break;
				}
		}
	if (!allValid){
		feedback.show("Please enter only letters \'FirstName\'",1);
		document.getElementById("id_firstname").focus();
		flag=false;
      	return false;
    } 
    if(getTextFirstName.length>20||getTextFirstName.length<3){
        feedback.show('Cannot add!! valid entry for firstname is between 3 and 20 ',1);
        document.getElementById("id_firstname").focus();
        return false;
     }
        //LAST NAME
        getTextlastName=document.getElementById('id_lastname').value;
        if(getTextlastName.length>20||getTextlastName.length<1){
	        feedback.show('Cannot add!! valid entry for Last Name is between 1 and 20 ',1);
    	   	document.getElementById("id_lastname").focus();
        	return false;
        }
        //EMAIL
 		getEmail = document.getElementById("id_Email").value;
		if( getEmail.isEmail() ) {
					
		}else{
			feedback.show('Sorry, your entry is not a valid email address.',1);
			document.getElementById("id_Email").focus();
			return (false);
		}
		
		//MOBILE
		
		
		getMobileNo = document.getElementById("id_mob").value;
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
		
		if(getMobileNo.length != 10){
	        feedback.show('Cannot add!! valid entry for mobile number should be 10 digits',1);
    	   	document.getElementById("id_mob").focus();
        	return false;
        }
	if (!allValidnos)
	{
		feedback.show("Please enter only digits \'0123456789\' for mobile",1);
		document.getElementById("id_mob").focus();
		flag=false;
      	return false;
    } 
		if(getMobileNo.length>14){
       	feedback.show('Cannot add!! valid entry is for mobile no. is less than 14',1);
        document.getElementById("id_mob").focus();
        return false;
        }
		
		getProvider=document.getElementById("div_provider").value;
		
		getcombovalue6 = document.getElementById("div_combo6").value;
		
		//PASSWORD
		
		if(glob!=1){
			getpass = document.getElementById("id_pass").value;
	       	var allValidnos = true;       
			
			if(getpass.length < 4){
		        feedback.show('Cannot add!! valid entry for password should be >=4 digits',1);
	    	   	document.getElementById("id_pass").focus();
	        	return false;
	        }
		}
		
		//Street		
		 
		getTextStreet = $("id_street").value;
		
		if(getTextStreet.length <= 0){
        	feedback.show('Cannot add!! street should not be empty ',1);
	        $("id_street").focus();
	        return false;
     	}
     	
     	//Area		
		 
		getTextArea = $("id_area").value;
		
		if(getTextArea.length <= 0){
        	feedback.show('Cannot add!! area should not be empty ',1);
	        $("id_area").focus();
	        return false;
     	}
     	
     	//City		
		 
		getTextCity = $("id_city").value;
		
		if(getTextCity.length <= 0){
        	feedback.show('Cannot add!! city should not be empty ',1);
	        $("id_city").focus();
	        return false;
     	}
     	
     	//State		
		 
		getTextState = $("id_state").value;
		
		if(getTextState.length <= 0){
        	feedback.show('Cannot add!! state should not be empty ',1);
	        $("id_state").focus();
	        return false;
     	}
     	
     	//Country
		
		if($("div_combo6").value=="0")
		{
			feedback.show('Cannot add!! please select a country',1);
    		document.getElementById("div_combo6").focus();
        	return false;
		}
		
		//Occupation
		
		if($("occupation").value=="0")
		{
			feedback.show('Cannot add!! please select a occupation',1);
    		document.getElementById("occupation").focus();
        	return false;
		}
		else if($("occupation").value=="33")
		{
			if($("ocs_tr").value=="")
			{
				feedback.show('Cannot add!! please enter occupation',1);
	    		document.getElementById("id_ocs").focus();
	        	return false;
			}
		}
		
	//PIN CODE	
       getZipcode = document.getElementById("id_Zip").value;
         var checkzip = "0123456789";
       	var checkzipcode = getZipcode;
       	var allValidno = true;
       	for (i = 0;  i < checkzipcode.length;  i++)
	{
		ch = checkzipcode.charAt(i);
	for (j = 0;  j < checkzip.length;  j++)
		if (ch == checkzip.charAt(j))
			break;
		if (j == checkzip.length)
		{
			allValidno = false;
			break;
		}
	}
	if (!allValidno)
	{
		feedback.show("Please enter only digits \'0123456789\'",1);
		document.getElementById("id_Zip").value="";
		flag=false;
      	return false;
    } 
      if(getZipcode.length>7||getZipcode.length<1){
      	feedback.show('Cannot add!! valid entry is for pin code is less than 7',1);
        document.getElementById("id_Zip").value="";
        return false;
        }
        getmcode = document.getElementById("mobileext").value;
    
    if($("id_confirm_pass").value != $("id_pass").value){
    	feedback.show('Both passwords should match',1);
   	   	$("id_confirm_pass").focus();
       	return false;
    }   
   }
  
/* prototype*/

function $(id){
	return document.getElementById(id);
}

function initSettings(val){
	if(val==0){
		$("general").style.display="block";
		$("edit").style.display="none";
		$("pass").style.display="none";
		
		$("gen_div").className="set_nav_active";
		$("ci_div").className="set_nav_inactive";
		$("cp_div").className="set_nav_inactive";
	}
	else if(val==1){
		$("general").style.display="none";
		$("edit").style.display="none";
		$("pass").style.display="block";
		
		$("cp_div").className="set_nav_active";
		$("gen_div").className="set_nav_inactive";
		$("ci_div").className="set_nav_inactive";
	}
	else if(val==2){
		$("general").style.display="none";
		$("edit").style.display="block";
		$("pass").style.display="none";
		
		$("ci_div").className="set_nav_active";
		$("gen_div").className="set_nav_inactive";
		$("cp_div").className="set_nav_inactive";
	}
}



function load_de(){
	
}

function validateCode(){
	code = $("id_code").value;
	if(code == ""){
        feedback.show('Please Enter Code',1);
   	   	document.getElementById("id_code").focus();
       	return false;
    }
    
    var checkcode = "0123456789";
    var checkcode_ = code;
    var allValidno = true;
    for (i = 0;  i < checkcode_.length;  i++)
	{
		ch = checkcode_.charAt(i);
		for (j = 0;  j < checkcode.length;  j++)
		if (ch == checkcode.charAt(j))
			break;
		if (j == checkcode.length)
		{
			allValidno = false;
			break;
		}
	}
	if (!allValidno)
	{
		feedback.show("Please enter only digits \'0123456789\'",1);
		document.getElementById("id_code").value="";
		document.getElementById("id_code").focus();
      	return false;
    } 
}

function searchCommunity(){
	var search = $("id_search").value;
	if(search == ""){
        feedback.show('Please Search Phase',1);
   	   	document.getElementById("id_search").focus();
       	return false;
    }
}

/*Photo*/
var win;
function showPhotoSelect(){
	var str="pages/settings/buddy_popup.jsp";
	win =window.open(str,"","dialogWidth:500px;dialogHeight:500px") ;
	setTimeout("isUploadClosed()",100);
	/*var f1= document.getElementById("myform");
	alert('form'+f1);
	f1.submit();*/
}

function isUploadClosed (){
	try{
	if(win.closed){
		var f1= document.getElementById("myform");
	f1.submit();
	}else{
		setTimeout("isUploadClosed()",100);
	}
	}catch(e){
		var f1= document.getElementById("myform");
	}
}


function doOccupation(){
	if($("occupation").value=="33")
	{
		$("ocs_tr").style.display="block";
	}
	else
	{
		$("ocs_tr").style.display="none";
	}
}

function deletePhotoSelect()
{
	var answer = confirm("Are u sure you want to delete picture")
	if (answer){
		var f1= document.getElementById("delete");
		f1.submit();
	}
}

function loadHome(val,f)
{	
	if(val!="0"){
		var f1= document.getElementById("myform");
		f1.submit();
		/*if(f=="3")
			alert('Password updated successfully');
		else if(f=="4")
			alert('Contact Information updated successfully');
		else if(f=="6")
			alert('New Ice Memeber added successfully');*/
	}
}

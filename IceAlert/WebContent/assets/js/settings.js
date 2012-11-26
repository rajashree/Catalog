var currentProvider;
var glob;
var search_rec;

function load_de(){
	//$("gen_div").className="edit_profile_active";
	//$("ci_div").className="change_image";
	//$("cp_div").className="change_password";
	
	$("editpro_div").className="edit_profile_active";
	$("changepass_div").className="change_password";
	$("changeimg_div").className="change_image";
	
	initSettings(0);
}

function changepass(){
	cur_pass = $("id_cur_pass").value;
	new_pass = $("id_new_pass").value;
	new_pass1 = $("id_new_pass1").value;
	
	if(cur_pass.length < 4){
        passfeedback.show('Current password should be >=4 digits',1);
   	   	document.getElementById("id_cur_pass").focus();
       	return false;
    }
    
    if(new_pass.length < 4){
        passfeedback.show('New password should be >=4 digits',1);
   	   	document.getElementById("id_new_pass").focus();
       	return false;
    }
    
    if(new_pass != new_pass1){
    	passfeedback.show('Both new passwords should match',1);
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
/* prototype*/

function $(id){
	return document.getElementById(id);
}

function initSettings(val){
	if(val==0){
		$("general").style.display="none";
		$("personal").style.display="block";
		$("pass").style.display="none";
		$("medical").style.display="none";
		$("med_div").style.display="block";
		$("per_div").style.display="block";
		
		$("editpro_div").className="edit_profile_active";
		$("changeimg_div").className="change_image";
		$("changepass_div").className="change_password";
	}
	else if(val==1){
		$("general").style.display="none";
		$("personal").style.display="none";
		$("pass").style.display="block";
		$("medical").style.display="none";
		$("med_div").style.display="none";
		$("per_div").style.display="none";
		
		$("editpro_div").className="edit_profile";
		$("changeimg_div").className="change_image";
		$("changepass_div").className="change_password_active";
	}
	else if(val==2){
		$("general").style.display="block";
		$("personal").style.display="none";
		$("pass").style.display="none";
		$("medical").style.display="none";
		$("med_div").style.display="none";
		$("per_div").style.display="none";

		$("changeimg_div").className="change_image_active";
		$("editpro_div").className="edit_profile";
		$("changepass_div").className="change_password";
	}
}

function initeditSettings(val){
	if(val==0){
		$("personal").style.display="block";
		$("medical").style.display="none";
		
		$("per_a").className="text_active";
		$("med_a").className="text";
		
	}
	else if(val==1){
		$("personal").style.display="none";
		$("medical").style.display="block";
		
		$("per_a").className="text";
		$("med_a").className="text_active";
	}
}


function accountInfo(){
	
		
		//FIRST NAME
		newFirstName = $("id_firstname").value;
        getTextFirstName = $("id_firstname").value;
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
			$("id_firstname").focus();
			flag=false;
	      	return false;
	    } 
	    if(getTextFirstName.length>20||getTextFirstName.length<3){
	        feedback.show('Cannot add!! valid entry for firstname is between 3 and 20 ',1);
	        $("id_firstname").focus();
	        return false;
	     }
	     
	    //LAST NAME
	    newLastName = $("id_lastname").value;	   
        getTextlastName=document.getElementById('id_lastname').value;
        if(getTextlastName.length>20||getTextlastName.length<2){
	        feedback.show('Cannot add!! valid entry for Last Name is between 2 and 20 ',1);
    	   	document.getElementById("id_lastname").focus();
        	return false;
        }
        
        //DOB
		if(($("dob_year").value=="0")||($("dob_month").value=="0")||($("dob_day").value=="0"))
		{
			feedback.show('Cannot add!! please select a Valid Date of Birth',1);
    		$("eoccupation").focus();
        	return false;
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
    		$("div_combo6").focus();
        	return false;
		}
		
		
		//ZIP CODE	
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
		
		//PHONE
		
		if($("id_ph").value!="")
		{
			var getPhoneNo = $("id_ph").value;
			var check = "0123456789";
       		var checkdigits = getPhoneNo;
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
				feedback.show("Please enter only digits \'0123456789\' for Phone",1);
				$("id_ph").focus();
				flag=false;
	      		return false;
	    	}
	    }	 
	    
	    //PHONE EXT
		 
		if($("phoneext").value!="")
		{
		 
			var getPhoneExt = $("phoneext").value;
			var check = "0123456789";
	       	var checkdigits = getPhoneExt;
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
				feedback.show("Please enter only digits \'0123456789\' for Phone Extenstion",1);
				$("phoneext").focus();
				flag=false;
		      	return false;
		    }
		 
	    }
		
		//MOBILE		
		getMobileNo = $("id_mob").value;
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
    	   	$("id_mob").focus();
        	return false;
        }
		if (!allValidnos)
		{
			feedback.show("Please enter only digits \'0123456789\' for mobile",1);
			$("id_mob").focus();
			flag=false;
	      	return false;
	    } 
		if(getMobileNo.length>14){
       	feedback.show('Cannot add!! valid entry is for mobile no. is less than 14',1);
        document.getElementById("id_mob").focus();
        return false;
        }
		
		if(($("occupation").value=="0")&&($("eoccupation").value=="0"))
		{
			feedback.show('Cannot add!! please select any of the occupations',1);
    		document.getElementById("occupation").focus();
        	return false;
		}
		
		//Occupation
		/*if($("occupation").value=="0")
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
		}*/
		
		
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

function deletePhotoSelect()
{
	var answer = confirm("Are u sure you want to delete picture")
	if (answer){
		var f1= document.getElementById("delete");
		f1.submit();
	}
}

function viewEoccupationList()
{
	var c = $("echeck").checked;
	if(c!=0)
	{
		$("eoccupation").disabled=0;
		$("occupation").disabled=1;
	}
	else
	{
		$("eoccupation").disabled=1;
		$("occupation").disabled=0;
	}
}


function handleDateInputChange()
{
	var year = $("dob_year").value;
	var month = $("dob_month").value;
	
	switch(month)
	{
		case '00': month = '0';
				break;
		case '01': month = '1';
				break;
		case '02': month = '2';
				break;
		case '03': month = '3';
				break;
		case '04': month = '4';
				break;
		case '05': month = '5';
				break;
		case '06': month = '6';
				break;
		case '07': month = '7';
				break;
		case '08': month = '8';
				break;
		case '09': month = '9';
				break;		
		
	}
	var daysinmonth= new Array(0,31,28,31,30,31,30,31,31,30,31,30,31);
	if ( ( (year%4 == 0)&&(year%100 != 0) ) || (year%400 == 0) ) {
			daysinmonth[2] = 29;
	}
	var days =new Array();
	for(var i=1;i<=daysinmonth[month];i++)
	{
		days[i] = i;
	}

	document.getElementById("dob_day").innerHTML="";
	var	sel = document.getElementById("dob_day");
	for(i=0;i<days.length-1;i++)
	{
		sel.options[i] = new Option(days[i+1],days[i+1]);
	}
}

function medicalInfo(){
	
		//BLOOD GROUP
		
		if($("bloodgroup").value=="0")
		{
			medfeedback.show('Cannot add!! please select a Blood Group',1);
    		$("bloodgroup").focus();
        	return false;
		}
		// ALLERGIES
		if($("allergiess").value!="None")
		{
			var allergyArray = new Array();
			var allergyObj = $("allergiess");
			var i;
    		var allergycount = 0;
			var allergies;
	   		for (i=0; i<allergyObj.options.length; i++) {
    			if (allergyObj.options[i].selected) {
	 				allergyArray[allergycount] = allergyObj.options[i].text;
	 				//alert( selectedArray[count]);
      				allergycount++;
    			}
  			}
			allergies = allergyArray[0];
			for(i=1;i<allergyArray.length;i++)
			{
				allergies = allergies +','+ allergyArray[i];
			}
			$("allergies").value = allergies
		}
		
		//DISEASES
		if($("diseases").value!="None")
		{
			var diseaseArray = new Array();
			var diseaseObj = $("diseases");
			var i;
    		var diseasecount = 0;
			var diseases;
	   		for (i=0; i<diseaseObj.options.length; i++) {
    			if (diseaseObj.options[i].selected) {
	 				diseaseArray[diseasecount] = diseaseObj.options[i].text;
	 				//alert( selectedArray[count]);
      				diseasecount++;
    			}
  			}
			diseases = diseaseArray[0];
			for(i=1;i<diseaseArray.length;i++)
			{
				diseases = diseases +','+ diseaseArray[i];
			}
			$("disease").value = allergies
		}
		

		
        //MEDICINES	
        
		/*getTextMedicines = $("medicines").value;
		if(getTextMedicines.length <= 0){
        	$("medicines").value = "None";
	        return false;
     	}*/
     	
     	//CONDITIONS	
     		
		/*getTextConditions = $("conditions").value;
		if(getTextConditions.length <= 0){
        	$("conditions").value = "None";
	        return false;
     	}*/
   }

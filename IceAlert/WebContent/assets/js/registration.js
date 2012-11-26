
/* prototype*/

function $(id){
	return document.getElementById(id);
}


function encode( uri ) {
    if (encodeURIComponent) {
        return encodeURIComponent(uri);
    }

    if (escape) {
        return escape(uri);
    }
}

function validateRegForm(){

	 //FIRST NAME
        var getTextFirstName = $("id_firstname").value;
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
        var getTextlastName=$('id_lastname').value;
        if(getTextlastName.length>20||getTextlastName.length<2){
	        feedback.show('Cannot add!! valid entry for Last Name is between 2 and 20 ',1);
    	   	$("id_lastname").focus();
        	return false;
        }
        
        //EMAIL
 		var getEmail = $("id_Email").value;
		if( getEmail.isEmail() ) {
					
		}else{
			feedback.show('Sorry, your entry is not a valid email address.',1);
			$("id_Email").focus();
			return (false);
		}
		
		//PASSWORD
		
		var getpass = $("id_pass").value;
       	var allValidnos = true;       
		
		if(getpass.length < 4){
	        feedback.show('Cannot add!! valid entry for password should be >=4 digits',1);
    	   	$("id_pass").focus();
        	return false;
        }
        //DOB
		if(($("dob_year").value=="0")||($("dob_month").value=="0")||($("dob_day").value=="0"))
		{
			feedback.show('Cannot add!! please select a Valid Date of Birth',1);
    		$("eoccupation").focus();
        	return false;
		}
        
        //ZIP CODE	
       	var getZipcode = $("id_Zip").value;
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
			$("id_Zip").focus();
			flag=false;
	      	return false;
	    } 
      	if(getZipcode.length>7||getZipcode.length<1){
      		feedback.show('Cannot add!! valid entry is for pin code is less than 7',1);
        	$("id_Zip").focus();
        	return false;
      	}
        //Street		
		 
		/*getTextStreet = $("id_street").value;
		
		if(getTextStreet.length <= 0){
        	feedback.show('Cannot add!! street should not be empty ',1);
	        $("id_street").focus();
	        return false;
     	}*/
     	
     	//Area		
		 
		/*getTextArea = $("id_area").value;
		
		if(getTextArea.length <= 0){
        	feedback.show('Cannot add!! area should not be empty ',1);
	        $("id_area").focus();
	        return false;
     	}*/
     	
     	//City		
		 
		/*getTextCity = $("id_city").value;
		
		if(getTextCity.length <= 0){
        	feedback.show('Cannot add!! city should not be empty ',1);
	        $("id_city").focus();
	        return false;
     	}*/
     	
     	//State		
		 
		/*getTextState = $("id_state").value;
		
		if(getTextState.length <= 0){
        	feedback.show('Cannot add!! state should not be empty ',1);
	        $("id_state").focus();
	        return false;
     	}*/
     	
     	
     	
     	
     	//Country
		
		if($("div_combo6").value=="0")
		{
			feedback.show('Cannot add!! please select a country',1);
    		$("div_combo6").focus();
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
		
		var getMobileNo = $("id_mob").value;
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
	        $("id_mob").focus();
	        return false;
        }
        
        //Occupation & E occupation
        
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
    		$("occupation").focus();
        	return false;
		}*/
		
		//E Occupation
		
		/*if($("eoccupation").value=="0")
		{
			feedback.show('Cannot add!! please select a Emmergency Occupation',1);
    		$("eoccupation").focus();
        	return false;
		}*/
		
		
		
		
		
      
      //Confirm password
      if($("id_confirm_pass").value != $("id_pass").value){
    	feedback.show('Both passwords should match',1);
   	   	$("id_confirm_pass").focus();
       	return false;
     } 	
     
     $("hid_combo").value=$("div_provider").value
}

function load_de(){
	getProviderList_init();
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


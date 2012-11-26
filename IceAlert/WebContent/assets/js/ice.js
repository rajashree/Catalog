
/* prototype*/

function $(id){
	return document.getElementById(id);
}


var qs="";
var j=1;

var count;

function Send_Ice_Alert()
{
	var c=0;	
	count=$("count").value;
	
	if(count == 1)
	{
		if($("myform").Check.checked)
		{
			qs=qs+$("myform").Check.value;
			c=1;
			j=1;
		}		
		else{
			j=0;
			c=0;
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
      					qs=qs+"|"+$("myform").Check[i].value;
      					j++;
      					c++;
      				}
      				else
      				{
      					qs=qs+$("myform").Check[i].value;
      					j++;
      					c++;
      				}
    			} 
 			} 
 			j=j-1;
 		}
 		
 		if(c == 0)/// Start here
 		{
 			alert("Select atleast one ICE member");
 			return false;
 		}
 		else
 		{
		$("smsdiv").style.display="block";	
 		}  			
}

function closesms(){
	count=$("count").value;
	$("smsdiv").style.display="none";	
	for (var i = 0; i < count; i++)
	{
		$("myform").Check[i].checked=false;
	}
	$("glbid").checked=false;
}


function ice_del(val)
{
	count=$("count").value;
	var q_del="";
	var q_edit="";
	var flag_edit=0;
	if(count == 1)
	{
		if($("myform").Check.checked)
		{
			if(val=="Delete")
					q_del+=$("myform").Check.value;
				else
				{
					if(val=="Edit")
						if(q_edit=="")
							q_edit=$("myform").Check.value;
						else
						{
							flag_edit=1;
						}
				}
		}		
	}
	else
	{
	 	for (var i = 0; i < count; i++)
		{
			if($("myform").Check[i].checked)
			{
				if(val=="Delete")
					q_del+=$("myform").Check[i].value+" ";
				else
				{
					if(val=="Edit")
						if(q_edit=="")
							q_edit=$("myform").Check[i].value;
						else
						{
							flag_edit=1;
							break;
						}
				}
			}
		}
	}
	if(q_del=="" && q_edit=="")
	{
		alert("Select atleast one ICE member");
		
	}
	else
	{
		$("dummyform").method.value=val;
		if(val=="Edit")
		{
			if(flag_edit==0)
			{
				$("dummyform").queary.value=q_edit;
				var f1= document.getElementById("dummyform");
				f1.submit();
			}
			else
			{
				alert("Select only one ICE member ");
				$("glbid").checked=false;
				for (var i = 0; i < count; i++)
				{
					$("myform").Check[i].checked=false;
				}
			}
		}
		else
		{
			if(window.confirm("Are you sure to delete"))
			{
				$("dummyform").queary.value=q_del;
				var f1= document.getElementById("dummyform");
				f1.submit();
			}
		}
	
	}
}

function selectevery(){
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

function validateSMS(){
	$("icedata").value=qs;
	//
	
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
}

function iceValidate(){
	if($("id_ice_name").value=="")
	{
		 feedback.show('Name should not be null',1);
   	   	 $("id_ice_name").focus();
   	   	 return false;
	}
	
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
        feedback.show('valid entry for mobile number should be 10 digits',1);
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
       	feedback.show(' valid entry is for mobile no. is less than 14',1);
        $("id_mob").focus();
        return false;
       }
	
	if($("div_combo6").value=="0")
	{
		feedback.show('Please select country',1);
   		$("div_combo6").focus();
       	return false;
	}

	var getEmail = $("id_Email").value;
	
	if(getEmail == "")
	{
		feedback.show('Email should not be null',1);
		$("id_Email").focus();
		return (false);
	}
	if(getEmail.isEmail() ) {
				
	}else{
		feedback.show('Please enter a valid email address.',1);
		$("id_Email").focus();
		return (false);
	}
}
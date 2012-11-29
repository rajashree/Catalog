
function $(id){

	return document.getElementById(id);
}

function validateLogin()
{	
	var status=0;
	
	$("username").style.border="1px solid #d7d7d7";
	$("password").style.border="1px solid #d7d7d7";
	
	if($("username").value==""){
		status=1;
		$("username").style.border="red solid 1px";
	}
	if($("password").value=="")
	{
		status=1;
		$("password").style.border="red solid 1px";
	}
	if(status==0)
	{
		return true;
	}else{ 
		$("error_div").innerHTML = "";
		$("error_div").innerHTML = "Required field cannot be left blank";
		$("error_div").style.display = "block";
		return false;
	}
}	
function validateRegistration()
{
	var status=0;
	$("firstname").style.border="1px solid #d7d7d7";
	$("lastname").style.border="1px solid #d7d7d7";
	$("email").style.border="1px solid #d7d7d7";
	$("username").style.border="1px solid #d7d7d7";
	$("password").style.border="1px solid #d7d7d7";
	$("conformPassword").style.border="1px solid #d7d7d7";
	
	if($("firstname").value=="")
	{
		status=1;
		$("firstname").style.border="red solid 1px";
	}
	if($("lastname").value=="")
	{
		status=1;
		$("lastname").style.border="red solid 1px";
	}
	if($("email").value=="")
	{
		status=1;
		$("email").style.border="red solid 1px";
	}
	if($("username").value=="")
	{
		status=1;
		$("username").style.border="red solid 1px";
	}
	if($("password").value=="")
	{
		status=1;
		$("password").style.border="red solid 1px";
	}
	
	if($("conformPassword").value=="")
	{
		status=1;
		$("conformPassword").style.border="red solid 1px";
	}
	if($("Positive").checked==false){
		status=2;
	}
	
	if(status == 0){
		x=document.getElementsByName("password");
		if((x[0].value.length  < 6))
		{	status=3;
			$("password").style.border="red solid 1px";
			$("conformPassword").style.border="red solid 1px";
		}
		if(($("password").value) != ($("conformPassword").value)){
			status = 4;
			$("password").style.border="red solid 1px";
			$("conformPassword").style.border="red solid 1px";
		}
		if(!echeck($("email").value)){
			status=5;
			$("email").style.border="red solid 1px";
		}
		if(status==3)
		{ 
			$("error_div").innerHTML = "Passwords must be more than 5 chars";
			$("error_div").style.display = "block";
			return false;
		}	
		if(status==4)
		{ 
			$("error_div").innerHTML = "Passwords did not match";
			$("error_div").style.display = "block";
			return false;
		}
		if(status==5)
		{ 
			$("error_div").innerHTML = "Enter a valid email address";
			$("error_div").style.display = "block";
			return false;
		}		
		
	}else if(status==1)
		{ 
			$("error_div").innerHTML = "Required field is blank";
			$("error_div").style.display = "block";
			return false;
		}else if(status==2)
		{ 
			$("error_div").innerHTML = "Agree terms and conditions";
			$("error_div").style.display = "block";
			return false;
		}
	
	
	
}


function validateChangePassword()
{	
	var status=0;
	$("oldPassword").style.border="1px solid #d7d7d7";
	$("newPassword").style.border="1px solid #d7d7d7";
	$("newConfirmPassword").style.border="1px solid #d7d7d7";
	
	if($("oldPassword").value=="")
	{
		status=1;
		$("oldPassword").style.border="red solid 1px";
	}
	if($("newPassword").value=="")
	{
		status=1;
		$("newPassword").style.border="red solid 1px";
	}
	if($("newConfirmPassword").value=="")
	{
		status=1;
		$("newConfirmPassword").style.border="red solid 1px";
	}
	if(status==0)
	{
		x1=document.getElementsByName("newPassword");
		if((x1[0].value.length  < 6))
		{	status=3;
			$("newPassword").style.border="red solid 1px";
			$("newConfirmPassword").style.border="red solid 1px";
		}
		
		if(($("newPassword").value) != ($("newConfirmPassword").value)){
			status = 2;
			$("newPassword").style.border="red solid 1px";
			$("newConfirmPassword").style.border="red solid 1px";
			
		}
		if(status==2)
		{ 
			$("error_div").innerHTML = "Passwords did not match";
			$("error_div").style.display = "block";
			return false;
		}	
		if(status==3)
		{ 
			$("error_div").innerHTML = "Passwords more than 6 chars";
			$("error_div").style.display = "block";
			return false;
		}
		return true;
	}	
	if(status==1)
	{ 
		$("error_div").innerHTML = "Required field cannot be left blank";
		$("error_div").style.display = "block";
			
		return false;
	}
	
}

function validateUpdateProfile()
{	
	var status=0;
	$("firstName").style.border="1px solid #d7d7d7";
	$("lastName").style.border="1px solid #d7d7d7";
	$("email").style.border="1px solid #d7d7d7";
	
	if($("firstName").value==""){
		status=1;
		$("firstName").style.border="red solid 1px";
	}
	if($("lastName").value=="")
	{
		status=1;
		$("lastName").style.border="red solid 1px";
	}
	if($("email").value=="")
	{
		status=1;
		$("email").style.border="red solid 1px";
	}	
	if(status==0)
	{
		if(!echeck($("email").value)){
			status=2;
			$("email").style.border="red solid 1px";
		}
		if(status==2)
		{ 
			$("error_div").innerHTML = "Enter a valid email address";
			$("error_div").style.display = "block";
			return false;
		}
		return true;
	}	
	else
	{ 
		$("error_div").innerHTML = "Required field cannot be left blank";
		$("error_div").style.display = "block";
			
		return false;
	}
}		


function validateForgotPassword()
{	var status=0;
	
	$("username").style.border="1px solid #d7d7d7";
	if($("username").value==""){
		status=1;
		$("username").style.border="red solid 1px";
	}
			
	if(status==0)
	{
		return true;
	}	
	else
	{ 
		$("error_div").innerHTML = "Required field cannot be left blank";
		$("error_div").style.display = "block";
			
		return false;
	}
}	


function echeck(str) {

		var at="@"
		var dot="."
		var lat=str.indexOf(at)
		var lstr=str.length
		var ldot=str.indexOf(dot)
		if (str.indexOf(at)==-1){
		   alert("Invalid E-mail ID")
		   return false
		}

		if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
		   return false
		}

		if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
		    return false
		}

		 if (str.indexOf(at,(lat+1))!=-1){
		    return false
		 }

		 if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
		    return false
		 }

		 if (str.indexOf(dot,(lat+2))==-1){
		    return false
		 }
		
		 if (str.indexOf(" ")!=-1){
		    return false
		 }

 		 return true					
	}

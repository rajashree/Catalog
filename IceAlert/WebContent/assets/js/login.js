
function $(id){
	return document.getElementById(id);
}

function login(){
	user = $("id_username").value;
	if(user == ""){
        feedback.show('Please Enter Username',1);
   	   	$("id_username").focus();
       	return false;
    }
    
    pass = $("id_pass").value;
	if(pass == ""){
        feedback.show('Please Enter Password',1);
   	   	$("id_pass").focus();
       	return false;
    }
}

function loadLogin(status,name){
	switch(status){
		case 0:
			$("id_username").focus();
			break;
		case 1:
			 feedback.show('Invalid Username',1);
			 $("id_username").focus();
			 break;
		case 2:
			feedback.show('Invalid Password',1);
			$("id_pass").focus();
			$("id_username").value=name;
			break;
		case 3:
			feedback.show('Session Expired..!',1);
			$("id_username").focus();
			break;			
		case 4:
			feedback.show('Successfully logged out',1);
			$("id_username").focus();
			break;		
	}
	if(status != 0 )
		setTimeout("all()",1000);
}

function all(){
	document.getElementById("myform").submit();
}

function forgotPassword(){
	$("login").style.display="none";
	$("forgot").style.display="block";
	$("emailid").focus();
}

/*function back(){
	$("login").style.display="block";
	$("forgot").style.display="none";
	$("id_username").focus();
	$("msgdiv").style.display="none";
}*/
/*
function forgotPass(){
	getEmail = $("emailid").value;
	if(getEmail == "")
	{
		alert('Please enter email address');
		return (false);
	}
	if( getEmail.isEmail() ) {
				
	}else{
		alert('Invalid email address');
		$("emailid").focus();
		return (false);
	}
	$("forgot").style.display="none";
	/*$("msgdiv").style.display="block";
	$("msgdiv").innerHTML='<div class="email_text">Password has been mail to your E-Mail ID</div><div class="back"><a href="javascript:back();" class="text">Back</a></div>';
	$("forgotfrm").submit();
	$("login").style.display="block";
	$("forgot").style.display="none";
	$("id_username").focus();
	$("msgdiv").style.display="none";
}*/
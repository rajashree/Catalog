function loadComm(val,f)
{	
	if(val!="0"){
		var f1= document.getElementById("myform");
		f1.submit();		
	}
}
function closeDiv(){
	$("send_div").style.display="none";
}
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

function validateInvitation()
{
	var org_str=$("id_email").value;
	if(org_str == "")		
	{
		feedback.show('Please enter valid email id.',1);
		document.getElementById("id_email").focus();
		return (false);
	}
	var len;
	var ret="";
	if(org_str.charAt(org_str.length-1) == ",")
		len=org_str.length-1;
	else
		len=org_str.length;
	var con_str="";	
	var j=0;
	
	
	/*for(var i=0;i<len;i++)
	{	
		if(org_str.charAt(i).match(" "))
		{
			//alert("White");
		}
		else if(org_str.charAt(i).match("\n"))
		{
			//alert("New line");
		}
		else
		{
			con_str+=org_str[i];
			j++;
		}
	}*/
	
	var email_array = new Array();
	//String str=org_str.substring(0,org_str.indexOf(' ')); //  + org_str.substr(org_str.lastIndexOf(' '));
	//alert(str);
	email_array=org_str.split(",");	
	for(var i=0;i<email_array.length;i++)
	{
		if(email_array[i].isEmail() ) {	
			ret=ret+email_array[i]+"|";			
		}else{
			feedback.show('Sorry, "'+email_array[i]+'" is an invalid email id.',1);
			document.getElementById("id_email").focus();
			return (false);
		}
	}
	$("email_data").value=ret;
}

function validateSearch(){
	if($("id_search").value == "")
	{
		return false;
	}
}

function validateAddComm(){
	if(document.getElementsByName("community_name")[0].value == "")
	{
		feedback.show('Please enter Community Name.',1);
		document.getElementsByName("community_name")[0].focus();
		return false;
	}
	if(document.getElementsByName("desciption")[0].value == "")
	{
		feedback.show('Please enter Community Desciption.',1);
		document.getElementsByName("desciption")[0].focus();
		return false;
	}
	
}

function myfunction()
{
	var browser=navigator.appName;
	if(browser=="Microsoft Internet Explorer")
	document.location="./AddCommunity.ice";
	alert("Sorry Use Browse Button");
	document.getElementsByName("file")[0].value="";
}

function validateCommunityAlert(){
	if($("subject").value == "")
	{
		feedback.show('Please enter subject.',1);
		$("subject").focus();
		return false;
	}
	if($("description").value == "")
	{
		feedback.show('Please enter Desciption.',1);
		$("description").focus();
		return false;
	}
}

function initInvite(){
	$("id_inv_msg").value="";
	//$("id_email").value="";
	
}

function getContacts(){
	$("prev_contact").value=$("id_email").value;
	$("contactfrm").submit();
}

function getContacts_Load(){
$("contactfrm").submit();
}

function validateLogin(){
	user = $("id_uname").value;
	if(user == ""){
        feedback.show('Please Enter Username',1);
   	   	$("id_uname").focus();
       	return false;
    }
    
    if($("mail_providers").value=="00")
	{
		feedback.show('Please select a provider',1);
   		$("mail_providers").focus();
       	return false;
	}
	
    pass = $("id_passwd").value;
	if(pass == ""){
        feedback.show('Please Enter Password',1);
   	   	$("id_passwd").focus();
       	return false;
    }
}

function backToInvite()
{
	if($("prev_contact").value.slice(0,4)=="home" && $("prev_contact").value.slice($("prev_contact").value.length-4,$("prev_contact").value.length)=="home")
	{
		$("backToHomeForm").backToHome.value=$("prev_contact").value.slice(4,$("prev_contact").value.length-4);
		var f1= document.getElementById("backToHomeForm");
		f1.submit();
	}
	else
	{
		$("backToInviteForm").backToInvite.value=$("prev_contact").value;
		var f1= document.getElementById("backToInviteForm");
		f1.submit();
	}
}

function findFriends()
{
	if($("friendopt").value == "none")
	{
		feedback.show('Please Select any one of the options',1);
		$("friendopt").focus();
		return false;
	}else{
		$("friendtype").value= $("friendopt").value;
		$("prev_contact_friend").value=$("id_email").value;
		$("searchfrdfrm").submit();
	}	
}

function selectevery(){
	//count=$("count").value;
	if($("count").value == 1)
	{
		$("myform").Check.checked=$("glbid").checked;
	}
	else
	 	for (var i = 0; i < $("count").value; i++)
		{
			$("myform").Check[i].checked=$("glbid").checked;
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

function goback()
{
	$("gobackfrm").submit();
}

function gotocommunity()
{
	$("gocommfrm").submit();
}


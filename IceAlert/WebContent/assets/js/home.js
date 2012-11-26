  
/* prototype*/

function $(id){
	return document.getElementById(id);
}
/*Photo*/
var win;
function showPhotoSelect(){
/*	var str="pages/settings/buddy_popup.jsp";
	win =window.open(str,"","dialogWidth:500px;dialogHeight:500px") ;
	setTimeout("isUploadClosed()",100);	*/
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

function invitefriends()
{

	var org_str=$("friendids").value;
	if(org_str == "")		
	{
		alert("Please enter valid email id.");
		document.getElementById("friendids").focus();
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
	

	
	var email_array = new Array();
	//String str=org_str.substring(0,org_str.indexOf(' ')); //  + org_str.substr(org_str.lastIndexOf(' '));
	//alert(str);
	email_array=org_str.split(",");	
	for(var i=0;i<email_array.length;i++)
	{
		if(email_array[i].isEmail() ) {	
			$("maillist").value = $("friendids").value
			$("invitefrm").submit();	
		}else{
			alert('Sorry, "'+email_array[i]+'" is an invalid email id.');
			document.getElementById("friendids").focus();
			return (false);
		}
	}
}
function getContacts(){
	$("prev_contact").value="home"+$("friendids").value+"home";
	$("contactfrm").submit();
}

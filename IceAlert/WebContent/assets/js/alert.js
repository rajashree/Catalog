/* prototype*/

function $(id){
	return document.getElementById(id);
}

function highlightAlert(val){
	if(val==1)
	{
		$("r_link").className="text_active";
		$("s_link").className="text";
		$("i_link").className="text";
	}
	else if(val==2)
	{
		$("r_link").className="text";
		$("s_link").className="text_active";
		$("i_link").className="text";
	}
	else if(val==3)
	{
		$("r_link").className="text";
		$("s_link").className="text";
		$("i_link").className="text_active";
	}
}

function fun(val){
	//$("alert").value=val;
	//$("viewfrm").submit();
	//alert(val);
	//document.getElementsByName("alert_id")[2].value
	alert("hi");
}
function getrid(val){
	$("ralert").value=val;
	$("viewrfrm").submit();
}

function getsid(val){
	//alert("hi");
	$("salert").value=val;
	$("viewsfrm").submit();
}

function getiid(val){
	//alert("hi");
	$("rinvite").value=val;
	$("viewifrm").submit();
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
function availableContacts()
{
	previousEmails=document.getElementsByName( 'id_email')[0].value;
}
function addContacts()
{
	count=$("count").value;
	var val="";
	if(count == 1)
	{
		if($("myform").Check.checked)
		val=$("myform").Check.value;
	}
	else
	 	for (var i = 0; i < count; i++)
		{
			if($("myform").Check[i].checked)
				if(val=="")
					val+=$("myform").Check[i].value;
				else
					val+=","+$("myform").Check[i].value;
		}
	if(val=="")
		alert("select any one of them");
	else
	{
		if($("prev_contact").value!="")
			$("queary").value=$("prev_contact").value+","+val;
		else
			$("queary").value=val;
		return true;
	}
	return false;
}
function alter_del()
{
	count=$("count").value;
	var q_del="";
	if(count == 1)
	{
		if($("myform").Check.checked)
		{
			q_del+=$("myform").Check.value;
		}		
	}
	else
	{
	 	for (var i = 0; i < count; i++)
		{
			if($("myform").Check[i].checked)
				q_del+=$("myform").Check[i].value+" ";
		}
	}
	if(q_del=="")
		alert("Select one of them");
	else
	{
		$("method").value=$("method_dummy").value;
		$("rdelinfo").value=q_del;
		$("deleterfrm").submit();
	}
}

function alert_del()
{
	$("method").value=$("method_dummy").value;
	$("adelinfo").value=$("id_dummy").value;
	$("deleteafrm").submit();
	
}

function communityalert()
{
	$("communityfrm").submit();	
}

function icealert()
{
	$("icefrm").submit();	
}
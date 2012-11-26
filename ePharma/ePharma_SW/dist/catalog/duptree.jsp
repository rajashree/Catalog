<html>
<SCRIPT>
function AccessCheck(){

	var linkName ="<%= (String)session.getAttribute("optype")%>";
	document.goBack.action = "<%= (String )request.getAttribute("targetUrl")%>";
	if(linkName != "edit" ){
			document.goBack.linkName.value = "manageStandard";
	}
	
	document.goBack.submit();
	return true;
}


</SCRIPT>
<body  onLoad="AccessCheck();">
<form name="goBack" action="" method="post"  >
<input type="hidden" name="operationType" value="FIND" />
<input type="hidden" name="target" value="_top" />
<input type="hidden" name="tp_company_nm" value="" />
<input type= "hidden" name="linkName" value="editMaster" />
<input type= "hidden" name="pagenm" value="Catalog" />


</form>
</body>
</html>
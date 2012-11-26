<html>
<SCRIPT>
function AccessCheck(){

		var name = "<%=request.getParameter("name")%>";
		var optype = "<%=request.getParameter("optype")%>";
		var exist ="<%= request.getParameter("exist")%>";
		var mand= "<%=(String)session.getAttribute("mandatory")%>"
		
		if(name == "null"){
			return true;
		}else{
					if( exist == "true" ){
							alert(" Element name '"+name+" ' already exists !...." );
					}else if(exist == "false" ){
							if(mand == "null" || mand == "true" ) 
								alert(optype + " Successful !.. "); 
							else{
									alert("ProductName,NDC,GTIN,ManfactureName,LotNumber Are Mandatory !..");
							}
					}
		
		
		}

	return true;
}


</SCRIPT>

<body  onLoad="return AccessCheck();">


</body>
</html>
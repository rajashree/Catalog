 
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
 
//-->
</script>
<body>
<form name="InitForm" >
<input type="hidden" name="dbname" value="ePharma"/>
<input type="hidden" name="collname" value="ShippedPedigree"/>
<input type="hidden" name="mimetype" value="application/pdf"/>
<input type="hidden" name="nodename" value="binary"/>
<input type="hidden" name="topnode" value="PedigreeEnvelope"/>
 

<%
 
String docId=(String)session.getAttribute("DocId");
System.out.println("documentID"+docId);
if(docId==null)docId="";
else{
%>


<input type="hidden" name="pedigree/shippedPedigree/documentInfo/serialNumber" value="<%=docId%>"/>

<script language="JavaScript" type="text/JavaScript">
    
    	  
		document.InitForm.action = "<%=request.getContextPath()%>/servlet/showImageFromTL1?";
		
		 document.InitForm.submit();
    
      
</script>
<%}session.removeAttribute("DocId");%>
</form> 
 
		
	 

  

<%
String tpN=(String)session.getAttribute("TPNameNotValid");
    if(tpN==null)tpN="";
    if(tpN.equals("true")){
%>
<script language = "Javascript">

alert("Trading Partner Name Not Valid");

</script> 
<%}%>


<body>

<%
String str = (String)session.getAttribute("test");
System.out.println("tesssssssssst\n\n\n\n"+str);
if(str==null || str.equals("")){
%>


<form name="InitForm" >
<input type="hidden" name="dbname" value="ePharma"/>
<input type="hidden" name="collname" value="PaperPedigree"/>
<input type="hidden" name="mimetype" value="application/pdf"/>
<input type="hidden" name="nodename" value="data"/>
<input type="hidden" name="topnode" value="initialPedigree"/>


<%}else{%>
<form name="InitForm" >
<input type="hidden" name="dbname" value="ePharma"/>
<input type="hidden" name="collname" value="ReceivedFax"/>
<input type="hidden" name="mimetype" value="application/pdf"/>
<input type="hidden" name="nodename" value="FileContents"/>
<input type="hidden" name="topnode" value="InboundPostRequest"/>

<%}%>



<%
 
String docId=(String)session.getAttribute("DocId");
System.out.println("ddddddddd"+docId);
if(docId==null)docId="";
else{
%>

<%if(str==null || str.equals("")){%>
<input type="hidden" name="DocumentInfo/serialNumber" value="<%=docId%>"/>
<%}else{%>
<input type="hidden" name="InitialPedigreeId" value="<%=docId%>"/>
<%}%>


<script language="JavaScript" type="text/JavaScript">
    
    	 document.InitForm.target = "frame2";
    	 
    	 
		document.InitForm.action = "<%=request.getContextPath()%>/servlet/showImageFromTL1?";
		
		document.InitForm.submit();
    
    	 document.InitForm.target = "frame1"
		document.InitForm.action = "InitialPedigree1.do";
		document.InitForm.submit();
   
       
</script>
<%session.removeAttribute("test"); }%>
</form> 
 
		
	 

  

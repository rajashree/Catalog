 

<body>
<form name="InitForm">
</form> 
<%
response.setContentType("image/JPEG"); 
String csid=(String)request.getParameter("csid");
 
if(csid==null)csid="";
else{
%>


 
<img src="<%=request.getContextPath()%>/servlet/showImageFromTL?dbname=ePharma&collname=ReceivedFax&topnode=InboundPostRequest&nodename=FileContents&FaxControl/CSID=<%=csid%>"/>
<%}%>

 
		
	 

  

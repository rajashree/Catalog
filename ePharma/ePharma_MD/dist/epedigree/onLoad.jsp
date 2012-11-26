<html>
<head><title></title>

<script language="JavaScript" type="text/JavaScript">

function accessdenied1() {
  var status = "<%=request.getAttribute("Pedenv")%>";
  var refno = "<%=request.getParameter("selectedRow")%>";
  var attachStatus = "<%=request.getAttribute("AttachStatus")%>"
  var exists = "<%=request.getAttribute("exists")%>";
  var pedId = "PedigreeId" + "=" + "<%=request.getAttribute("PedigreeId")%>";
  var emailStatus = "<%=request.getAttribute("emailStatus")%>";
  var ptpStatus = "<%=request.getAttribute("Status")%>";

if(attachStatus == "true") alert("Attachment Uploaded Successfully!!!")

if(emailStatus == "Success") alert("Email has been sent successfully")

if(emailStatus == "failure") alert("Email Sending Failure...!")

if(ptpStatus == "Saved") alert("Pedigree Trading Partner has been added Successfully...!")

if(ptpStatus == "Updated") alert("Pedigree Trading Partner has been Updated Successfully...!")

if(status == "true"){
  var res = confirm("Pedigree Already Exists !!! Do you want to Recreate Pedigree?")
  if(res) {
  		document.createAPN.y.value="CreatePedigree";
  		document.createAPN.res.value="true";
  		document.createAPN.action = "/ePharma/dist/epedigree/ShippingManagerSearchEmpty.do?Submit3=Search"
  		document.createAPN.submit();
  }
  else return false;
  //setTimeout("MsgBox 'Pedigree Already Exists !!! Do you want to Recreate Pedigree?',4 ",0,"vbscript")
}

if(exists == "true"){
  var exists = confirm("Attachment Already Exists !!! Do you want to replace the Attachment?")
  if(exists) {
  
  		document.attachmentForm.reply.value="true";
  		document.attachmentForm.action = "/ePharma/dist/epedigree/Shipping_Attachment.do?"+pedId+"&tp_company_nm=&pagenm=pedigree&linkname=Attachment"
  		document.attachmentForm.submit();
  }
  else return false;
  //setTimeout("MsgBox 'Pedigree Already Exists !!! Do you want to Recreate Pedigree?',4 ",0,"vbscript")
}


var i = "<%=(String)request.getAttribute("APNSigStatus")%>"
if(i == "SignExists") { alert("Signature Already Exists !!"); }
if(i == "SignCreated"){alert("Signature Created !!");}
if(i == "SignNotCreated"){alert("Signature Not Created !!");}

var j = "<%=(String)request.getAttribute("AuthenticateStatusInDetail")%>"
if(j == "AlreadyAuthenticated") { alert("Pedigree Already Authenticated !!"); }
if(j == "AuthenticatedSuccessfully"){alert("Pedigree Authenticated Successfully !!");}

}
</script>

</head>

<body onLoad="return accessdenied1();">
<% System.out.println("*********Inside onLoad.jsp********"); %>
</body>
</html>


<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.*"%>




<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function goback()
{

	document.mappingsEdit.operationType.value =  "FIND";
    document.mappingsEdit.action = "UploadCatalog.do";
	document.mappingsEdit.target="_top";
	document.mappingsEdit.submit();

	return true;
}

//-->
</script>
</head>
<body>




<table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white" align="center"> 
                  <td class="td-typeblack">Exception Products Details</td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">NDC #</td>
					<td class="type-whrite">Product Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Manufacturer Name</td>
				</tr>
<% 
String str=new String("");
List list=(List)session.getAttribute("exceptionList");
Iterator iterator=list.iterator();
while(iterator.hasNext())
{
str=(String)iterator.next();
String str1[]=str.split(",");

%>

                <tr>
                
                <td><%=str1[3] %> </td>
                <td><%=str1[4] %> </td>
                <td><%=str1[12] %> </td>
                <td><%=str1[15] %> </td>
                </tr>
              
               
                
      
<%      
}


%>

</table>



<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>

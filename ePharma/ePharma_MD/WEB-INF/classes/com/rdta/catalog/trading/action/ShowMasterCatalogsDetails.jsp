<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/struts-html" prefix="html" %>
<html:html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

// This functionality to some extent is replicated in ShowMasterProductInfo.jsp

<% 
	String catalogName = request.getParameter("catalogName");
	System.out.println("catalogName is " +catalogName); 
	request.setAttribute("catalogName", catalogName);	
	String genId = (String)request.getAttribute("genId");
	String pagenm = request.getParameter("pagenm");
	boolean isMandatoryPage = ( pagenm != null && pagenm.equalsIgnoreCase("mandatory") );
 %>


<div id="rightwhite">
<center><jsp:include page="../globalIncludes/tab.jsp"/></center>
<br>
<br>
<br>

<tr>
<tr>
<tr>        
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
	</td>
  </tr>

  <tr> 
    <td>&nbsp;</td>
    
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        </table>
        
        
        
        <% 
        
        	String[] results = (String[])request.getAttribute("parsed");
        	String[] dataTypes = (String[])request.getAttribute("parsed1");
        	
        	System.out.println("The attribute results is :" +results);
        	
        	out.println("<Form name='dynamicForm' enctype='multipart/form-data' id='dynamicForm' action='SaveDynamicForm.do' onsubmit='return checkData()'>");
        	out.println("<input type='hidden' value='"+catalogName+"' name='catalogName'" );
        	out.println("<input type='hidden' value='"+genId+"' name='genId'" );
        	out.println("<b><center>Master Product Details</center></b>");
        	out.println("<TABLE cellSpacing=\"2\" cellPadding=\"3\" align='center' border=\"0\" >");
      %>
        <%
        	System.out.println("length ="+results.length/2);        	 
        	int totalSize=results.length;
        	int loopCount =0;
        	int tdsInOneRow = 2;
        	boolean noLastTD=false;
        	String operation = "save~";
        	if( totalSize % 2 == 0 ){
				loopCount = totalSize/2;       	
        	}
        	else{
        		loopCount = (totalSize/2) +1; 
        		noLastTD=true;
        	}
        	 
        
        	for(int i=0; i<loopCount;i++ ) {        	
        		
        		String value = (String)results[tdsInOneRow*i];
        		
        		boolean dontPrintLastTD = (i == loopCount-1 && noLastTD);
        		String value1=  dontPrintLastTD ? "" : (String)results[tdsInOneRow*i+1];
        		
        		String curDataType = dataTypes[ tdsInOneRow*i ];      
        		String curDataType1 = dontPrintLastTD ? "" : (String)dataTypes[tdsInOneRow*i+1];	
        		
        		String htmlelmt = curDataType.equals("image")?"<Input type='file' name='"+operation+value+"' datatype='" + curDataType + "' value=''>":"<Input type='text' name='"+operation+value+"' datatype='" + curDataType + "' value=''>";
        		String htmlelmt1 = curDataType1.equals("image")?"<Input type='file' name='"+operation+value1+"' datatype='" + curDataType1 + "' value=''>":"<Input type='text' name='"+operation+value+"' datatype='" + curDataType1 + "' value=''>";
        		
        		out.println("<tr class=\"tableRow_Header\"><td align='left' width='40'>"+value+"</td><td align='left' width='40'>"+htmlelmt+"</td>");
        		if( !dontPrintLastTD ){
        			out.println("<td align='left' width='40' >"+value1+"</td><td align='left' width='40'>"+htmlelmt1+"</td>");
        		}
        		else{
        		  out.println("<td colspan=2>&nbsp;</td>");
        		}
        		out.println("</tr>");
        		
        %>
       
      
		<%	
        			
        		System.out.println("Value is :"+value);
        	}	
        	out.println("</TABLE>");
        	out.println("<center><input type='submit' value='Save'></center>");
        	out.println("</Form>");
        	out.println("<SCRIPT language='javascript'>");
        	out.println(" var isOptional=" + (!isMandatoryPage) );
        	out.println("function checkData()");
        	out.println("{");
        	out.println(" if( isOptional) return true;");
        	out.println("  var inptFlds = document.dynamicForm.elements; ");
        	out.println(" for(i=0;i<inptFlds.length;i++){");
        	out.println(" var obj=inptFlds[i]; var inptName=obj.getAttribute('name');var dataType=obj.getAttribute('datatype');");
        	
        	out.println(" if( inptName.indexOf('save') == 0 ){");
        	out.println("  var val = obj.value; ");
        	out.println(" if( val == null || val == '' ) {");
        	out.println(" alert('Enter a value'); obj.focus();return false; "); 
        	out.println(" ");
        	out.println(" }else{");
        	out.println(" if( dataType == 'number' || dataType == 'int'  ) {");
        	out.println(" 	if( isNaN(obj.value) ){ alert('Enter a valid number'); obj.value=''; obj.focus();return false;  }");
        	out.println(" }");
        	out.println(" }}");
        	out.println("}");
        	out.println(" return true;");
        	out.println("}");
        	out.println("</SCRIPT>");
        %>
 
        
<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</html:html>        
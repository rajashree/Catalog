<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/struts-html" prefix="html" %>
<html:html>
<head>
<title>Raining Data ePharma - GCPIM</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<body>

<%
	String tp_company_nm = request.getParameter("tp_company_nm");
	if(tp_company_nm == null){
		tp_company_nm = (String)request.getAttribute("tp_company_nm");
	}	
	if ( tp_company_nm == null) tp_company_nm="";
	
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null){
		pagenm = (String)request.getAttribute("pagenm");		
	}	
	
	String insertAccess = (String)request.getAttribute("insertAccess");
	String updateAccess = (String)request.getAttribute("updateAccess");
	
	String sessionID = (String)session.getAttribute("sessionID");
		
		
%>


<%@include file='../epedigree/topMenu.jsp'%>

<% 
	
	
	String[] nodeNames = (String[])request.getAttribute("nodeNames");
	System.out.println("The nodeNames contains :"+nodeNames);
	
	String[] nodeValues = (String[])request.getAttribute("nodeValues");
	System.out.println("The nodeValues contains :"+nodeValues);
	
	String[] mandoroptnal = (String[])request.getAttribute("mandoroptnal");
	System.out.println("The mandoroptnal contains :"+mandoroptnal);
	
	String[] dataTypes = (String[])request.getAttribute("dataTypes");
	System.out.println("The dataTypes contains :"+dataTypes);
	
	String catalogName = request.getParameter("catalogName");
	if(catalogName == null){
			catalogName = (String)request.getAttribute("catalogName");
	}
	System.out.println("catalogName is " +catalogName); 
	
	request.setAttribute("catalogName", catalogName);	
	
	String genId = request.getParameter("genId");
	if(genId == null){
		genId = (String)request.getAttribute("genId");
	}		
	
	String catalogId = request.getParameter("catalogId");
	if(catalogId == null){
			catalogId = (String)request.getAttribute("catalogId");
	}
	
	String prodName = request.getParameter("prodName");
	if(prodName == null){
			prodName = (String)request.getAttribute("prodName");
	}
 %>


<div id="rightwhite">
       
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="99%" valign="top" class="td-rightmenu" style="height: 0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="5"></td>
		</tr>
	</table>
    </td>
 </tr>
	
  <tr> 
  
       
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        
        </table>
        
        
        
        <% 
        	out.println("<br>");
        	out.println("<br>");
        	out.println("<br>");
           	out.println("<Form name='dynamicForm' id='dynamicForm' action='SaveDynamicForm.do' enctype='multipart/form-data' method='post' onsubmit='return checkData()'>");
        	out.println("<input type='hidden' value='"+catalogName+"' name='catalogName'>" );
        	out.println("<input type='hidden' value='"+catalogId+"' name='catalogId'>" );
           	out.println("<input type='hidden' value='"+pagenm+"' name='pagenm'>" ); 
           	out.println("<input type='hidden' value='"+prodName+"' name='prodName'>" );     	
        	out.println("<input type='hidden' value='true' name='edited'>" );	
        	out.println("<input type='hidden' value='"+genId+"' name='genId'>" );
        	out.println("<b><center>Product Details for "+prodName+"</center></b>");
        	out.println("<TABLE cellSpacing=\"2\" cellPadding=\"3\" align='center' border=\"0\" >");
      %>
        <%
        	System.out.println("length ="+nodeNames.length/2);        	 
        	int totalSize = nodeNames.length;
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
        		
        		boolean dontPrintLastTD = (i == loopCount-1 && noLastTD);
        		
        		int ind1 = tdsInOneRow*i;
        		int ind2 = tdsInOneRow*i+1;
        		
        		
        		String name1 = nodeNames[ind1];
        		String name2 = dontPrintLastTD ? "" : nodeNames[ind2];
        		
        		String value1 = nodeValues[ind1];
        		String value2 = dontPrintLastTD ? "" : nodeValues[ind2];
        		
        		String isMandatory1 = mandoroptnal[ind1];	
        		String isMandatory2 = dontPrintLastTD ? "" :  mandoroptnal[ind2];
        		
        		String curDataType1 = dataTypes[ind1];
        		String access = (updateAccess.equals("false")) ? " disabled " : "";
        		String htmlelmt = curDataType1.equals("image")?"<Input type='file' name='"+operation+name1+"' ismandatory='"+isMandatory1+"' datatype='"+curDataType1+"' caption='"+name1+"' value=''><img name='img"+name1+"'  border=0 src='servlet/showImageFromTL?genId="+genId+"&nodename="+name1+"'><input type='hidden' name='imagePath"+name1+"' value=''>":"<Input type='text' " +access+ " name='"+operation+name1+"'  ismandatory='"+isMandatory1+"' datatype='"+curDataType1+"' caption='"+name1+"' value='"+value1+"'>";
        		
        		String curDataType2 = dontPrintLastTD ? "" :  dataTypes[ind2];;	
        		String htmlelmt1 = curDataType2.equals("image")?"<Input type='file' name='"+operation+name2+"' ismandatory='"+isMandatory2+"' datatype='"+curDataType2+"' caption='"+name2+"' value=''><img name='img"+name2+"' border=0 src='servlet/showImageFromTL?genId="+genId+"&nodename="+name2+"'><input type='hidden' name='imagePath"+name2+"' value=''>":"<Input type='text' " +access+ " name='"+operation+name2+"' ismandatory='"+isMandatory2+"' datatype='"+curDataType2+"' caption='"+name2+"' value='"+value2+"'>";
        		
        			
        		        		
        		out.println("<tr class='tableRow_Off' tr class=\"tableRow_Header\"><td align='left' width='40'>"+name1+"</td><td align='left' width='40'>"+htmlelmt+"</td>");
        		if( !dontPrintLastTD ){
        			out.println("<td align='left' width='40' >"+name2+"</td><td align='left' width='40' >"+htmlelmt1+"</td>");
        		}
        		else{
        		  out.println("<td colspan=2>&nbsp;</td>");
        		}
        		out.println("</tr>");
        		
        %>
       
      
		<%	
        			
        		
        	}	
        	out.println("</TABLE>");
        	
        	if(updateAccess.equalsIgnoreCase("true")){        	
	        	out.println("<center><input type='submit' value='Update'></center>");
	        }	
        	out.println("</Form>");
        	
        	out.println("<SCRIPT language='javascript'>");
        	out.println("function setImageHiddens(){ ");
        	out.println(" var allinputs = document.getElementsByTagName('input');");
        	out.println(" for(var i=0;i<allinputs.length;i++)");
        	out.println("{");
        	out.println("  var curObj =allinputs[i]; ");
        	out.println("   if( curObj.type ==  'file'){");
        	out.println("     var curname = curObj.name;");
        	out.println("     if( curname.indexOf('save~') >= 0 ){");
        	out.println("       var curPath= curObj.value;");
        	out.println("       var nodename = curname.substring(5);");
        	out.println("       document.getElementsByName('imagePath'+nodename)[0].value=curPath;");
        	out.println("     }");
        	out.println("   }");
        	out.println(" }");
        	out.println("}");
        	out.println("function checkData()");
        	out.println("{");
        	out.println(" setImageHiddens();"); 
        	out.println("  var inptFlds = document.dynamicForm.elements; ");
        	out.println(" for(i=0;i<inptFlds.length;i++){");
        	out.println(" var obj=inptFlds[i]; var inptName=obj.getAttribute('name');var dataType=obj.getAttribute('datatype');var ismandatory=obj.getAttribute('ismandatory'); var caption=obj.getAttribute('caption');");
        	
        	out.println(" if( inptName != null && inptName.indexOf('save') == 0 ){");
        	out.println("  var val = obj.value; ");
        	out.println(" if( (val == null || val == '') && ismandatory == 'true' ) {");
        	out.println("   if(dataType == 'image'){");
        	out.println("		var realImage = document.getElementsByName('img'+caption)[0]");
        	out.println("		if(realImage != null && realImage.width > 0){");
        	out.println(" 			continue;");
        	out.println(" 		}");        	
        	out.println("   }");
        	out.println(" alert('Enter a value for ' + caption); obj.focus();return false; "); 
        	out.println(" ");
        	out.println(" }else{");
        	out.println(" if( dataType == 'number' || dataType == 'int'  ) {");
        	out.println(" 	if( isNaN(obj.value) ){ alert('Enter a valid number for' + caption); obj.value=''; obj.focus();return false;  }");
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
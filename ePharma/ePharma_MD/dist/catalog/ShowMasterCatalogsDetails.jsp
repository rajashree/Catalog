<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/struts-html" prefix="html" %>
<html:html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<body>

<%
	String tp_company_nm = request.getParameter("tp_company_nm");
	if ( tp_company_nm == null) tp_company_nm="";
	String pagenm = request.getParameter("pagenm");
	String pagename = request.getParameter("pagename");
	if(pagename == null){
		pagename = (String)request.getAttribute("pagename");
	}	
	request.setAttribute("pagename",pagename);	
	
	System.out.println("PageName"+pagename);
	String sessionID = (String)session.getAttribute("sessionID");
%>

<% 
	//String catalogName = request.getParameter("catalogName");
	String catalogName = (String)request.getAttribute("catalogName");
	System.out.println("catalogName is " +catalogName); 
	request.setAttribute("catalogName", catalogName);	
	String genId = (String)request.getAttribute("genId");
	//String pagenm = (String)request.getAttribute("pagenm");
//	System.out.println("pagenm is "+pagenm);	
	//System.out.println("*********pagename is "+pagename);	
	
	String CountMandElms = (String)request.getAttribute("CountMandElms");
	String mandsavedornot = (String)request.getAttribute("mandsavedornot");
	
	
	boolean isMandatoryPage = ( pagename != null && pagename.equalsIgnoreCase("mandatory") );
 	String linkOn="newProduct";
 %>
 
<%@include file='../epedigree/topMenu.jsp'%>
<div id="rightwhite">
<table width="100%" border="0">
<tr> 
    <td width="100%" valign="top" class="td-rightmenu" style="height:0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="5"></td>
		</tr>
	</table>
    </td>
 </tr>
</table>

<center><jsp:include page="../globalIncludes/tab.jsp"/></center>
       
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
	</td>
  </tr>

  <tr> 
    <td>&nbsp;</td>
    
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="18"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        </table>
        
        <br>
        <br>
        <br>
        
        <% 
        
        	String[] results = (String[])request.getAttribute("parsed");
        	String[] dataTypes = (String[])request.getAttribute("parsed1");
        	
        	System.out.println("The attribute results is :" +results);
        	
        	out.println("<Form name='dynamicForm' enctype='multipart/form-data' id='dynamicForm' method='post' action='SaveDynamicForm.do' onsubmit='return checkData()'>");
        	out.println("<br>");

        	out.println("<input id ='catalogName' type='hidden' value='"+catalogName+"' name='catalogName'>" );
        	out.println("<input id ='pagename' type='hidden' value='"+pagename+"' name='pagename'>" );
        	out.println("<input id = 'pagenm' type='hidden' value='"+pagenm+"' name='pagenm'>" );
        	out.println("<input id = 'tp_company_nm' type='hidden' value='"+tp_company_nm+"' name='tp_company_nm'>" );
        	out.println("<input id = 'genId' type='hidden' value='"+genId+"' name='genId'>" );

        	out.println("<table border='0'  align='center'><tr height='3'><td><b><center>Master Product Details</center></b></td></tr><tr><td align='center'>");
        	out.println("<TABLE  cellSpacing=\"2\" cellPadding=\"3\" align='center' border=\"0\" >");
      %>
        <%
        	   	 
        	int totalSize=results.length;
        	int loopCount =0;
        	int tdsInOneRow = 2;
        	boolean noLastTD=false;
        	String operation = "save~";
        	if( totalSize % 2 == 0 ){
				loopCount = totalSize/2;       	
        	}
        	else{
        		loopCount = (totalSize/2) + 1;         		
        		noLastTD=true;
        	}
        	 System.out.println("Loop Count is :"+loopCount);
        	         
        	for(int i=0; i<loopCount;i++ ) {        	
        		
        		String value = (String)results[tdsInOneRow*i];
        		System.out.println("Value is :["+i+"]"+value);
        		boolean dontPrintLastTD = (i == loopCount-1 && noLastTD);
        		System.out.println("dontPrintLastTD :"+dontPrintLastTD+ " for "+i);
        		String value1=  dontPrintLastTD ? "" : (String)results[tdsInOneRow*i+1];
        		
        		String curDataType = dataTypes[ tdsInOneRow*i ];      
        		String curDataType1 = dontPrintLastTD ? "" : (String)dataTypes[tdsInOneRow*i+1];	
        		
        		String htmlelmt = curDataType.equals("image")?"<Input type='file' name='"+operation+value+"' datatype='" + curDataType + "' value=''><input type='hidden' name='imagePath"+value+"' value=''>":"<Input type='text' name='"+operation+value+"' datatype='" + curDataType + "' value=''>";
        		String htmlelmt1 = curDataType1.equals("image")?"<Input type='file' name='"+operation+value1+"' datatype='" + curDataType1 + "' value=''><input type='hidden' name='imagePath"+value1+"' value=''>":"<Input type='text' name='"+operation+value1+"' datatype='" + curDataType1 + "' value=''>";
        		
        		out.println("<tr class='tableRow_Off' class=\"tableRow_Header\"><td align='left' width='40'>"+value+"</td><td align='left' width='40'>"+htmlelmt+"</td>");
        		if( !dontPrintLastTD ){
        			out.println("<td align='left' width='40' >"+value1+"</td><td align='left' width='40'>"+htmlelmt1+"</td>");
        		}
        		else{
        		  out.println("<td colspan=2>&nbsp;</td>");
        		}
        		out.println("</tr>");
        		
        %>
       
      
		<%	
        			
        		
        		System.out.println("Value1 is :"+value1);
        	}	
        	out.println("</table></td></tr></table>");
        	out.println("<center><input type='submit' value='Save'></center>");
        	out.println("</Form>");
        	out.println("<SCRIPT language='javascript'>");
        	out.println(" var isOptional=" + (!isMandatoryPage) );
        	
        	out.println(" var countOfMandatory = "+(CountMandElms==null?"0":CountMandElms));
        	out.println(" var isMandatoryPageSaved = "+(mandsavedornot==null?false:mandsavedornot.equals("true")));
        	
        	out.println("	if(isOptional == false && countOfMandatory == 0 && isMandatoryPageSaved == false) ");
        	out.println("	{");
        	out.println("		document.getElementById('dynamicForm').submit();");        	
        	out.println("	}");
        	
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
        	out.println(" if( isOptional ) {");
        	out.println("  if( isMandatoryPageSaved == false && countOfMandatory > 0 ){ ");
        	out.println("    alert('Save Mandatory elements first');");
        	out.println("    return false;");
        	out.println("  } ");
        	/*out.println(" return true;");*/
        	out.println(" }");
        	out.println("  var inptFlds = document.dynamicForm.elements; ");
        	out.println(" for(i=0;i<inptFlds.length;i++){");
        	out.println(" var obj=inptFlds[i]; var inptName=obj.getAttribute('name');var dataType=obj.getAttribute('datatype');");
        	
        	out.println(" if( inptName.indexOf('save') == 0 ){");
        	out.println("  var val = obj.value; ");
        	out.println("	var caption = inptName.substring(5);");
        	out.println(" if( val == null || val == '' ) {");
        	out.println(" if(!isOptional) ");
        	out.println("  {alert('Enter a value for '+caption); obj.focus();return false;} "); 
        	out.println(" ");
        	out.println(" }else{");
        	out.println(" if( dataType == 'number' || dataType == 'int'  ) {");
        	out.println(" 	if( isNaN(obj.value) ){ alert('Enter a valid number for '+caption); obj.value=''; obj.focus();return false;  }");
        	out.println(" }");
        	out.println(" }}");
        	out.println("}");
        	out.println(" return true;");
        	out.println("}");
        	out.println("function submitForm(){");
        	out.println("	if(isOptional == false && countOfMandatory == 0 && isMandatoryPageSaved == false) ");
        	out.println("	{");
        	out.println("		document.getElementById('dynamicForm').submit();");        	
        	out.println("	}");
        	out.println("}");
        	out.println("</SCRIPT>");
        %>
 
        
<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</html:html>        

<%@ taglib uri="/struts-html" prefix="html"%>
<%@ taglib uri="/struts-bean" prefix="bean"%>
<%@ taglib uri="/struts-logic" prefix="logic"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
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

function mapPtnrs(){	
		frm=document.forms[0];
		var elements = frm.elements;
		var elementsLength = elements.length;
		var ddVal1=0;
		var ddVal2=0;
		if(validateDropDownStatus(frm)){
			for (var i = 0; i < elementsLength; i++) {	
				if(elements[i].type=="select-one" ){
					if(ddVal1==0)
						ddVal1=elements[i].value;
					else
						ddVal2=elements[i].value;					
				}
			}

			
			if(!ddVal2==0){
				if(ddVal1==ddVal2)
					alert("Please select a different Partner");
				else{
					frm.action = "<html:rewrite action='/MapTradingPartnerCat.do?exec=listCatalogs&trdId1='/>"+ddVal1+"&trdId2="+ddVal2;
					frm.submit();				
				}
			}else
				alert("Please select the Partner to be mapped");
		}
	}
	
	function validateDropDownStatus(frm) {
		var elements = frm.elements;
		var elementsLength = elements.length;
		var isSelected = false;
		var isChkdSelect = false;
		for (var i = 0; i < elementsLength; i++) {				
			if(elements[i].type=="select-one" ){	
				if(elements[i].selectedIndex >= 0){
					isChkdSelect = true;
					break;
				}
			}
		}	
		if (!isChkdSelect) {
			alert("Please select T P's");
			return false;
		}
		return true;
	}
	
//-->
</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>

              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Trading Partners</td>
                </tr>
                
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">FROM Trading Partners</td>
					<td>
					<%	int i = 0;	%>
					<select name="fromTradingPartner">
					<logic:iterate name ="<%=Constants.MAP_TRD_PTNR_KEY %>" id="map">
					<option value='<bean:write property = "trdPId" name ="map"/>'><bean:write name="map" property="trdPName" />
					<%	i++;	%>
					</logic:iterate>
					</select>
					</td>
					<td class="type-whrite">TO Trading Partners</td>
					<td>
					<select name="toTradingPartner">
					<logic:iterate name ="<%=Constants.MAP_TRD_PTNR_KEY %>" id="map">
					<option value='<bean:write property = "trdPId" name ="map"/>'><bean:write name="map" property="trdPName" />
					<%	i++;	%>
					</logic:iterate>
					</select>
					</td>
				</tr>
					
	</TABLE>
	<html:button  property="button" value ="Map Trading Partners" onclick="return 	mapPtnrs();">							
		</html:button>
            </td>


<jsp:include page="../globalIncludes/Footer.jsp" />

</html:form>
</body>
</html:html>

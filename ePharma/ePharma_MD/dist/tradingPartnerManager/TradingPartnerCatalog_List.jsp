
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
					frm.action = "<html:rewrite action='/TradingPartnerMappingAction.do?leftCatalogGenId='/>"+ddVal1+"&rightCatalogGenId="+ddVal2;
					frm.submit();				
			}else
					alert("Please select the Catalog to be mapped");
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
			alert("Please select Catalog's");
			return false;
		}
		return true;
	}
	
//-->
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />


<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
	<html:form action="/MapTradingPartnerCat" method="post">
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="left"> 
           
          </td>
          <td align="right">
            <!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
            <img src="./assets/images/space.gif" width="5"></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Trading Partners Catalogs</td>
                </tr>
                
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">FROM Trading Partners Catalogs</td>
					<td><%int i = 0;%>
						<select name="fromTradingPartnerCat">
						<logic:iterate name ="<%=Constants.MAP_TRD_CAT_KEY %>" id="cat">
						<option value='<bean:write property = "catalogGenID" name ="cat"/>'><bean:write name="cat" property="catalogName" />
						<%	i++;	%>
						</logic:iterate>
						</select>
					</td>	
					<td class="type-whrite">TO Trading Partners Catalogs</td>
					<td>
					<select name="toTradingPartnerCat">
					<logic:iterate name ="<%=Constants.MAP_TRD_CAT_KEY %>" id="cat">
					<option value='<bean:write property = "catalogGenId" name ="cat"/>'><bean:write name="cat" property="catName" />
					<%	i++;	%>
					</logic:iterate>
					</select>
				</tr>

					
	</TABLE>
	<html:button  property="button" value ="Map Catalogs" onclick="return mapPtnrs();">							
		</html:button>
            </td>
        </tr>
      </table></div>

<jsp:include page="../globalIncludes/Footer.jsp" />

</html:form>
</body>
</html:html>

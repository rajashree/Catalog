
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.session.CatalogContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.catalog.MappingNodeObject"%>
<%@ page import="com.rdta.catalog.JavaScriptTree"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Set"%>
<%@ page import="com.rdta.catalog.XMLStructure"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="java.util.ArrayList"%>

<%@ page import="com.rdta.commons.xml.XMLUtil"%>

<%@ page import="com.rdta.catalog.DataElementNode"%>
<%@ page import="com.rdta.catalog.trading.model.MappingCatalogs"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>

<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");

String checkMap=(String)request.getAttribute("checkMap");
if(checkMap==null)
checkMap="1";
System.out.println("Check Map"+checkMap);
%>
	 
<TITLE>R & D - eList</TITLE>
<style type="text/css">
<!--
body {
	background-color: #dcdcdc;
}
-->
</style><HEAD>
<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree_edited.css" rel="stylesheet" type="text/css">
<SCRIPT>
	function redirect() {
		window.location = 'ManageElement.do?catalogGenID=""';
	}



function submitform1(operation)
{
if(CatalogUpdatePrivilage())
    {
	document.mappingsEdit.operationType.value = operation;
	document.mappingsEdit.submit();

	return true;
	}
}

function submitform(operation,size,chkmap)
{
   
	document.mappingsEdit.operationType.value = operation;
 	if(size<=0)
	{
	
	  if(chkmap==2)
	  {
	  alert("Please Map the attribute")
   	 }
   	 else{
   	 
	   document.mappingsEdit.submit();
	   
   	   return true;
   	 }
   	 
	}
	else
   	{
	alert("NEED TO MAP ALL MANDATORY ELEMENTS");
   	return false;
   	}
   	 
  
}

function showValues(selSourceValue, selTargetValue) {

	document.mappingsEdit.selectedSourceName.value = selSourceValue;
	document.mappingsEdit.selectedTargetName.value = selTargetValue;
	document.mappingsEdit.submit();

	return true;

}

function goback()
{
	document.mappingsEdit.operationType.value =  "FIND";
    document.mappingsEdit.action = "TPCatalogList.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view";
	document.mappingsEdit.target="_top";
	document.mappingsEdit.submit();
	
	return true;
}
function CatalogUpdatePrivilage()
{

<%
QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
        List   accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.03','Update')");
		String viewStatus = accessList.get(0).toString();
		System.out.println("The Update is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(viewStatus.equals("true")){%>
return true;
<%}
 %>

}

function checkexists(){


 window.refresh;

<%

	 
 String existsstatus = (String)request.getAttribute("exists");
	System.out.println("exists   " +existsstatus); 
	String selectstatus = (String)request.getAttribute("selectCheckBox");
	System.out.println("exists   " +selectstatus);
	
	
%>
var j="<%=selectstatus%>";
var i = "<%=existsstatus%>";

if(j=="true"){
	alert("Please Select Check Box");
}
if(i=="true"){
	alert("already existing");
}

checkMap();

}

function checkMap()
{
<%
String mapNode = (String)request.getAttribute("mapNode");
System.out.println("exists   " +mapNode);
 %>
var k="<%=mapNode%>";
if(k=="2")
{
alert("Please Map the attributes");
}
}

function testResults (chboxname) {
if (chboxname.checked) {


if(chboxname.value.indexOf(20000)==0)
{
  
 document.mappingsEdit.operationType.value ="checked";
 document.mappingsEdit.checkedValue.value= chboxname.value;
 document.mappingsEdit.submit();
}
 
 

} else {
 
}
 
}
//<link href="assets/epedigree_edited.css" rel="stylesheet" type="text/css"/>
	
</SCRIPT>

</HEAD>


<%


        
		MappingNodeObject mappingNodeObject = null;
       
		String tpName = "";
		String tpGenId  = "";
		String genId = "";
		if(session != null) {
			 mappingNodeObject = (MappingNodeObject) session.getAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT);
			 			 
			 Node mappingNode = mappingNodeObject.getNode();
			 genId = XMLUtil.getValue(mappingNode,"genId");

            System.out.println(" tpGenId Id" + genId );

			TradingPartnerContext tpContext = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
			if(tpContext != null) {
				tpName = tpContext.getTpName();
				tpGenId = tpContext.getTpGenId();
			}
		}//session check 

	 
		
		
		CatalogContext catalogContext = null;
		if(session != null) {
		
			catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
		} 
		
		 
	
		

%>


<body onload=checkexists();>
	<table align="center" border="0" cellspacing="5" cellpadding="5" width="100%">
		
		<tr bgcolor="white">
			<td align="center" class="td-mapCatalogTreeTables">Selected Attributes</td>
		</tr>
		<tr >
			<td bgcolor="#EEEEEE">
				<br>
				
				<form name= "mappingsEdit" action="CatalogMappingEdit.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" method="post"  >
					<input type="hidden" name="checkedValue" value = "">
					<input type="hidden" name="mappingGenId" value="<%= genId %>">
					<input type="hidden" name="operationType" value="">

					<INPUT id="hidden10" type="hidden" name="tpGenId" value="<%=tpGenId%>">

					<input type="hidden" name="selectedSourceName" value="">
					<input type="hidden" name="selectedTargetName" value="">
					<table width="100%" border="0" align="center" cellpadding="5" cellspacing="1" bgcolor="#FFFFFF" class="Tablemap">
                      <tr  class="tableRow_Header">
                        <td height="30"  > <div align="center"><font class="TablemapTitle">Select</font></div></td>
						<td  > <div align="center"><font class="TablemapTitle"> Source Attribute </font></div></td>
                        <td > <div align="center"><font class="TablemapTitle">Target Attribute</font> </div></td>
                      </tr>
                      <%
						 
						List dataElements =null;
						dataElements =  mappingNodeObject.getDataElementsList();

						String sourceName = "";
						String targetName = "";
						String checkBoxName = "";
						int size = dataElements.size();
						//System.out.println("size="+size);
						//here i am checking all mandatory are included in mapping or not
						
						String mandatoryElements=(String)session.getAttribute(Constants.MANDATORY_ELEMENTS);
						 // System.out.println("Mandatory elements"+mandatoryElements);
						String[] str= new String(mandatoryElements).split(" ");
				        List list = new ArrayList();
				        List notMappedlist = new ArrayList();
				        for(int i=0;i<str.length;i++) {
				    	list.add(str[i]);
				        }
				        //System.out.println("Mandatory Listsize"+list.size());
						int msize=0;//for checking all mandatory are mapped or not
						for(int i=0; i<= size-1; i++)  {

							DataElementNode dataNode = (DataElementNode) dataElements.get(i);
							sourceName = dataNode.getAbsoluteSourceEleName();
							targetName = dataNode.getAbsoluteTargetEleName();
							
							//System.out.println("TARGETNAME"+dataNode.getTargetEleName());
		                    if(list.contains(dataNode.getTargetEleName()))
		                    {
		                    
		                  //  System.out.println("HERE WE CAME"+dataNode.getTargetEleName());
		                 //   System.out.println("INDEX OF"+list.indexOf(dataNode.getTargetEleName())); 
		                    list.remove(list.indexOf(dataNode.getTargetEleName()));
		                    
		                   
		                  
		                    }
		                   
		                    
		                     
							if(targetName != null && targetName.equalsIgnoreCase("NEED_TO_BE_DEFINED_RAJU") ){
								targetName = "";
							}
							if(sourceName != null && sourceName.equalsIgnoreCase("NEED_TO_BE_DEFINED_RAJU") ){
								sourceName = "";
							}
							

							checkBoxName = "check" + i;
                             
                             
					%>
                      <tr class="tableRow_OnMapTable"  style="height:10">
                        <%
							String cValue="";
							String cbv=sourceName+"20000"+targetName;
							String checkedValue=(String)session.getAttribute("checkedValue");
							%>
                        <%if(checkedValue!=null&&checkedValue.equals(cbv)){%>
                        <td><div align="center">
                          <input name="<%=checkBoxName%>" type="checkbox"  onClick="testResults(<%=checkBoxName%>)" value="<%=cbv %>"  checked/>                        
                        </div></td>
                        <%}else{%>
                        <td ><div align="center"> <input name="<%=checkBoxName%>" type="checkbox"  onClick="testResults(<%=checkBoxName%>)" value="<%=cbv %>"/> </div>                       </td>
                        <%}%>
                        <td ><%=targetName %> </td>
                        <td width="129" ><%= sourceName%> </td>
                      </tr>
                      <% }//for loop ends here %>
                      <%
						System.out.println("AFTER FILTERING"+(list.size()));%>
                      <tr>
                        <td  colspan="3" bgcolor="#CCCCCC"><div align="center">
                            <input name="button"  type="button" onClick="return goback()"  value="     Go Back     "> 
                            &nbsp;&nbsp;
                            <input name="button" type="button" onClick="return submitform1('DELETE')" value="     Delete     ">
                            &nbsp;&nbsp;
                            <input name="button2"  type="button" onClick="return submitform('SAVE',<%=list.size()%>,<%=checkMap%>)"  value="Save Mapping">
                        </div></td>
                      </tr>
                  </table>
					<table cellSpacing="1" cellPadding="3" align=center>
					 
					 
                     <%
                     for(int k=0;k<list.size();k++)
                     {
                     if(k==0)
                     {
                     %>
                       <tr class="titleBlack">
                       <td align="center"><FONT face="Arial" color="#cc0033" size="2"><STRONG>NEEDS TO BE MAPPED</STRONG></FONT></td>
                       </tr>
                   <%   }%> 
                       <tr>
                     <%
                     out.println("<td>"+list.get(k)+"</td>");
                     }
                     %>
                     </tr>
					</table>



					
					 
					  <%
						
						String selectedSourceName = request.getParameter("selectedSourceName");
						String selectedTargetName = request.getParameter("selectedTargetName");

						if(selectedSourceName != null && !selectedSourceName.trim().equals("") && selectedTargetName != null && !selectedTargetName.trim().equals("") ) {

							System.out.println(" selectedSourceName " + selectedSourceName );

						Node dataElementNode = mappingNodeObject.getDataElementNodeFromAbsoluteSourceName(selectedSourceName).getNode();

						List valueNodes = XMLUtil.executeQuery(dataElementNode,"values/value");
							

					%>
					  
					 
				    
					<table align="center" cellSpacing="1" cellPadding="3" >

						 	<tr class="titleBlack">
							
								<td align="center" colspan="3"> <%= selectedSourceName %> : <%= selectedTargetName %>  </td>
								
							</tr>
							<tr>
								<td >  Serial. No </td>
								<td >  Source Value </td>
								<td >  Target Value </td>
							</tr>

							
							<%
								Node valueNode ;
								for(int k=0;k<valueNodes.size();k++) {

									valueNode = (Node)valueNodes.get(k);
							%>

							<tr>
								<td> <%= k + 1 %> </td>
								<td > <%= XMLUtil.getValue(valueNode, "@sourceValue")%> </td>
								<td >  <%= XMLUtil.getValue(valueNode, "@targetValue")%></td>
							</tr>

					<%
							}//end of for loop

						}//end of if loop
					%>

				  </table>

				</form>
		  </td>
			
			


		</tr>
		
</table>

	
</body>


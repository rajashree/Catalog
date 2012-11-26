
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>

<%

   TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
%>

<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
    
%>
	
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="left">
		  
	<!--	  <A href="TradingPartnerView.do?operationType=FIND&tpGenId=<%=tpGenId%>&tpName=<%=tpName%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" class="typered-bold-link">Main</A>&nbsp;&nbsp;&nbsp;&nbsp;
		  
		  <A href="TPLocationList.do?tpGenId=<%=tpGenId %>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" class="typered-bold-link" onClick="return LocationReadPrivilage();">Locations</A>&nbsp;&nbsp;&nbsp;&nbsp;


		    <A href="TPCatalogList.do?tpGenId=<%=tpGenId %>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" class="typered-bold-link" onClick="return CatalogReadPrivilage();">Catalog</A>&nbsp;&nbsp;&nbsp;&nbsp;
		  
		  -->
		  
            <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> -->
          </td>
          <td align="right">
            <!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
            <img src="./assets/images/space.gif" width="5"></td>
        </tr>

		<INPUT id="hidden100" type="hidden" name="tpGenId" value="<%=tpGenId%>">
		<INPUT id="hidden101" type="hidden" name="tpName" value="<%=tpName%>">


      </table>
      
      
      
<script language="JavaScript" type="text/JavaScript">
<!--

function CatalogReadPrivilage()
{

<%
		QueryRunner queryrunner1 = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
        List accessList = queryrunner1.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.03','Read')");
		String viewStatus = accessList.get(0).toString();
		System.out.println("The Catalog Read is "+viewStatus);
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

function LocationReadPrivilage()
{

<%
		
        List locationList = queryrunner1.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.02','Read')");
		String locStatus = accessList.get(0).toString();
		System.out.println("The Location Read is "+locStatus);
       if(locStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(locStatus.equals("true")){%>
return true;
<%}
 %>

}

//-->
</script>
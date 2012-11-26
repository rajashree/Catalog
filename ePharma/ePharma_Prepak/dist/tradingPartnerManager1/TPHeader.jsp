
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>

<%

   TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
%>

	
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="left">
		  
		  <A href="TradingPartnerView.do?operationType=FIND&tpGenId=<%=tpGenId%>&tpName=<%=tpName%>" class="typered-bold-link">Main</A>&nbsp;&nbsp;&nbsp;&nbsp;
		  
		  <A href="TPLocationList.do?tpGenId=<%=tpGenId %>" class="typered-bold-link">Locations</A>&nbsp;&nbsp;&nbsp;&nbsp;


		    <A href="TPCatalogList.do?tpGenId=<%=tpGenId %>" class="typered-bold-link">Catalog</A>&nbsp;&nbsp;&nbsp;&nbsp;
		  
		  
		  <A href="TPServiceLevelNew.do" class="typered-bold-link">Service Level Agreement</A>&nbsp;&nbsp;&nbsp;&nbsp;
		  
		  
		  <A href="TPKPINew.do" class="typered-bold-link">KPIs</A>
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
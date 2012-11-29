<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Retailers list page</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
     <%@include file="/WEB-INF/ui/views/admin/devmode/devmode-header.jsp" %>

        <script type="text/javascript">
            function setHiddenFieldValue(obj) {

                var hiddenValue = obj.value;
                document.getElementById('retailerSiteChoosed').value = hiddenValue;
            }

            function validateSiteId(obj) {
               var siteId = document.getElementById('retailerSiteID').value;

               if( siteId == null || siteId == '' || siteId == 0  )
               {
                   console.log("No site has been chosen");
                   $('#invalid-site').show().fadeOut(2000, "linear");
                   return false;
               }


            }
        </script>
            <style>
            #invalid-site {
                margin-bottom: 20px;
                color: red;
                font-weight: bold;
            }


        </style>

</head>
<body>

	${navCrumbs}

	<div id="tabs" class="ui-tabs">
	     <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-corner-all">
	         <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="<c:url value="/admin/monitoring/dataImportProcesses.do"/>">Retailer Specific </a></li>
	         <li class="ui-state-default ui-corner-top "><a href="<c:url value="/admin/monitoring/activeProcesses.do"/>">Active Processes</a></li>
	
	     </ul>
	</div>
	
	<fieldset>
	     <legend><strong>Please choose a Retailer Site</strong></legend>
	
	     <div id="retailer">
	     <div id="invalid-site" style="display:none">
	         <span> Please select any Retailer Site.</span>
	     </div>
	
	         <form:form method="POST" action="processPerRetailer.do">
	             <table>
	                 <tr>
	                     <td>
	                         <select id="retailerSiteID" onchange="setHiddenFieldValue(this)">
	                             <option name="" value="0">Select Retailer Site</option>
	                             <c:forEach var="entry" items="${retailerSiteList}">
	                                 <option name="" value="${entry.id}">${entry.siteName}</option>
	                             </c:forEach>
	                         </select>
	                     </td>
	                     <td>
	                         <input type="hidden" id="retailerSiteChoosed" name="retailerSiteChoosed">
	                     </td>
	                 </tr>
	             </table>
	             <input type="submit" value="Submit" onclick="return validateSiteId(this);"/>
	             <a class="gbutton" href="javascript: history.back();">Cancel</a>
	         </form:form>
	     </div>
	</fieldset>
</body>
</html>
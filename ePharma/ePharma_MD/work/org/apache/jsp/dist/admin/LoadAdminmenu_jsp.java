package org.apache.jsp.dist.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class LoadAdminmenu_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {



		String genId="";

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/dist/admin/../epedigree/topMenu.jsp");
  }

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;


String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
System.out.println("In AdmMenu sessionID: "+sessionID);

//added

      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("\t<head>\r\n");
      out.write("\t\t<link href=\"../../assets/epedigree1.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
      out.write("\t</head>\r\n");
      out.write('\r');
      out.write('\n');
	
	//GET PATH TO SERVER SO CAN DYNAMICALLY CREATE HREFS
	String servPath = request.getContextPath();
	servPath = "http://"+request.getServerName()+":"+request.getServerPort()+servPath;     
	System.out.println(" tp_company_nm in toMenu.jsp: "+ tp_company_nm);
	String tpList = request.getParameter("tpList");
	if(tpList == null) tpList = "";

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write(" ");
 
   			String linkName =(String) request.getParameter("linkName"); 
   			if(linkName == null )linkName=(String) session.getAttribute("linkName");
   			else session.setAttribute("linkName",linkName);
   			if(linkName == null ) linkName="";
   			System.out.println("LInk Name : "+linkName);
   			
 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!-- Top Header -->\r\n");
      out.write("<div id=\"bg\" style=\"Z-INDEX: 100\">\r\n");
      out.write("\t<div class=\"roleIcon-southwood\"></div>\r\n");
      out.write("\t<div class=\"navIcons\">\r\n");
      out.write("\t\t<a href=\"");
      out.print(servPath);
      out.write("/logout.jsp\" target=\"_top\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logout.gif\" width=\"34\" height=\"27\" hspace=\"10\" border=\"0\"></a>\r\n");
      out.write("<!--\t<a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=eped\" target=\"_top\"><IMG height=\"27\" hspace=\"10\" src=\"");
      out.print(servPath);
      out.write("/assets/images/inbox.gif\" border=\"0\"></a>-->\r\n");
      out.write("\t\t<img src=\"");
      out.print(servPath);
      out.write("/assets/images/space.gif\" width=\"20\">\r\n");
      out.write("\t</div>\r\n");
      out.write("\t\r\n");
      out.write("\t<div class=\"logo\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logos_combined.jpg\"></div>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
 String SessionID = (String)session.getAttribute("sessionID");
System.out.println("inside topmenu.jsp");

      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<div id=\"menu\" style=\"Z-INDEX: 101\">\r\n");
      out.write("\t\t<table width=\"100%\" height=\"25\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table1\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td valign=\"middle\" background=\"");
      out.print(servPath);
      out.write("/assets/images/bg_menu.jpg\"><table width=\"400\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table2\">\r\n");
      out.write("\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("epcadmin")) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td width=\"200\" align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/AdminUser.do?pagenm=epcadmin&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("\"\" class=\"menu-link menuBlack\" target=\"_parent\">Admin Console</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td  width=\"200\" align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/AdminUser.do?pagenm=epcadmin&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("\"\" class=\"menu-link menuBlack\" target=\"_parent\">Admin Console</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } 
      out.write("\t\t\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t<td><img src=\"");
      out.print(servPath);
      out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("pedigree")) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td  width=\"200\" align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&accesslevel=PTP&linkName=PTP\" class=\"menu-link menuBlack\" target=\"_parent\">ePedigree</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td width=\"200\" align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&accesslevel=PTP&linkName=PTP\"  class=\"menu-link menuBlack\" target=\"_parent\">ePedigree</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } 
      out.write("\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t<!--<td><img src=\"");
      out.print(servPath);
      out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
      out.write("\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("pedBank")) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/dist/pedigreeBankResults.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=pedBank\" class=\"menu-link\" target=\"_parent\">Pedigree Bank \r\n");
      out.write("\t\t\t\t\t\t\t\t\t</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/dist/pedigreeBankResults.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=pedBank\" class=\"menu-link\" target=\"_parent\">Pedigree Bank \r\n");
      out.write("\t\t\t\t\t\t\t\t</a></td> ");
 } 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t-->\r\n");
      out.write("\t\t\t\t\t\t\t<td><img src=\"");
      out.print(servPath);
      out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
      out.write("\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t\r\n");
      out.write("\t");
 if(pagenm.equals("logist")) { 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t<!-- Left channel -->\r\n");
      out.write("\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
      out.write("\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("\t    <tr> \r\n");
      out.write("\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Reports</td>\r\n");
      out.write("\t    </tr>\r\n");
      out.write("\t    <tr> \r\n");
      out.write("\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("\t      <td valign=\"top\" class=\"td-left\"><p><br>\r\n");
      out.write("\t\t  <a href=\"index.html\" class=\"typeblue1-link\">Reports</a><br>\r\n");
      out.write("\t\t  <!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
      out.write("\t\t  <a href=\"Reports_Logistics.html\" class=\"typeblue1-link-sub\">Logistics \r\n");
      out.write("\t\t  Reports</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Logistics_ShipmentProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment \r\n");
      out.write("\t\t  by Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Logistics_ShipmentDate.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment \r\n");
      out.write("\t\t  by Date Span</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Logistics_ReceivingProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Receiving \r\n");
      out.write("\t\t  by Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Logistics_ReceivingDate.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Receiving \r\n");
      out.write("\t\t  by Date Span</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Logistics_ReturnsProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Returns \r\n");
      out.write("\t\t  by Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Logistics_ReturnsDate.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Returns \r\n");
      out.write("\t\t  by Date Span</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ePedigree.html\" class=\"typeblue1-link-sub\">ePedigree \r\n");
      out.write("\t\t  Reports</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ePedigree_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree \r\n");
      out.write("\t\t  per Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ePedigree_DateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree \r\n");
      out.write("\t\t  by Date Span</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ePedigree_APNTradingPartner.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;APN \r\n");
      out.write("\t\t  by Trading Partner</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ePedigree_APNDateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;APN \r\n");
      out.write("\t\t  by Date Span</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ePedigree_Commissioning.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Commissioning \r\n");
      out.write("\t\t  by Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ePedigree_FailedCommissioning.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Failed \r\n");
      out.write("\t\t  Commissioning by Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ProductRecall.html\" class=\"typeblue1-link-sub\">Product \r\n");
      out.write("\t\t  Recall Reports</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ProductRecall_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recalls \r\n");
      out.write("\t\t  per Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ProductRecall_DateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recalls \r\n");
      out.write("\t\t  by Date Span</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ProductRecall_ThreatLevel.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recalls \r\n");
      out.write("\t\t  by Threat Level</a>\r\n");
      out.write("\t\t  <a href=\"Reports_Trading.html\" class=\"typeblue1-link-sub\">Trading Partner Manager Reports</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Trading_SLAFulfillment.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;SLA fulfillment per Trading Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Trading_ShipmentTime.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment Time per 3PL carrier</a><br>\r\n");
      out.write("\r\n");
      out.write("\t\t  <a href=\"Reports_Diversion.html\" class=\"typeblue1-link-sub\">Diversion Reports</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Diversion_ASN3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ASN/RM discrepancy per 3PL</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Diversion_ASNLocation.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ASN/RM discrepancy per Location</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Diversion_TheftLocation.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Theft Alerts per Location</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Diversion_TheftProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Theft Alerts per Product</a><br>\r\n");
      out.write("\r\n");
      out.write("\t\t  <a href=\"Reports_ClinicalTrials.html\" class=\"typeblue1-link-sub\">Clinical Trials Reports</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ClinicalTrials_PatientName.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Alerts by Patient Name</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ClinicalTrials_DateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Alerts by Date Span</a><br>\r\n");
      out.write("\r\n");
      out.write("\t\t  <a href=\"Reports_Authentication.html\" class=\"typeblue1-link-sub\">Authentication Reports</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Authentication_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Authentication Failures per Product</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_Authentication_ReasonCode.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Authentication Failures per Reason Code</a><br>\r\n");
      out.write("\r\n");
      out.write("\t\t  <a href=\"Reports_ProductIntegrity.html\" class=\"typeblue1-link-sub\">Product Integrity Reports<br>\r\n");
      out.write("\t\t  </a> <a href=\"Reports_ProductIntegrity_TradingPartner.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
      out.write("\t\t  Violations per Trading Partner</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ProductIntegrity_3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
      out.write("\t\t  Violations per 3PL</a><br>\r\n");
      out.write("\t\t  <a href=\"Reports_ProductIntegrity_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
      out.write("\t\t  Violations per Product</a><br>\r\n");
      out.write("\t\t</p></td>\r\n");
      out.write("\t    </tr>\r\n");
      out.write("\t    <tr valign=\"bottom\"> \r\n");
      out.write("\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t    </tr>\r\n");
      out.write("\t  </table>\r\n");
      out.write("\t</div>\r\n");
      out.write("\r\n");
      out.write("\t<div id=\"rightwhite\"> \r\n");
      out.write("\r\n");
      out.write("\t");
 } else {    
	
	if(pagenm.equals("recall")) {     
      out.write("  \r\n");
      out.write("\t\t<!-- Left channel -->\r\n");
      out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
      out.write("\t\t\t\t<table width=\"170\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">ePedigree Manager</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div id=\"rightwhite\">\r\n");
      out.write("\t\r\n");
      out.write("\t");
 } else { 
	
	if(pagenm.equals("busintel")) {     
      out.write("  \r\n");
      out.write("\t\t<!-- Left channel -->\r\n");
      out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
      out.write("\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("\t\t    <tr> \r\n");
      out.write("\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Business Intelligence</td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t    <tr> \r\n");
      out.write("\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
      out.write("\t\t\t<a href=\"index.html\" class=\"typeblue1-link\">Business Intelligence</a><br>\r\n");
      out.write("\t\t\t<!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
      out.write("\t\t\t<a href=\"BizIntel_Alerts.html\" class=\"typeblue1-link-sub\">Alerts</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Alerts_DuplicateTags.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Duplicate Tags</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Alerts_ExchangedTags.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Exchanged Tags</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Alerts_DecommissionedTags.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Decommissioned Tags Observations</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Alerts_UnusualPurchaseOrders.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Unusual Purchase Orders</a><br>\r\n");
      out.write("\t\t\t<a href=\"BizIntel_Alerts_FailedEPedigrees.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Failed \r\n");
      out.write("\t\t\tePedigrees </a><br>\r\n");
      out.write("\t\t\t<a href=\"BizIntel_Statistics.html\" class=\"typeblue1-link-sub\">Statistics</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Statistics_ePedigreeVerification.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree Verification</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Statistics_ePedigreeFailures.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree Failures</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Statistics_Shipment.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Statistics_Revenue.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Revenue</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Statistics_ExpiredProducts.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Expired Products</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Statistics_RecallProducts.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recall Products</a><br>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t<a href=\"BizIntel_Sales.html\" class=\"typeblue1-link-sub\">Sales<br>\r\n");
      out.write("\t\t\t</a>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Sales_Geography.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales by Geography</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Sales_Period.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per Period</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_Sales_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per product</a><br>\r\n");
      out.write("\r\n");
      out.write("\t\t\t    <a href=\"BizIntel_NewProduct.html\" class=\"typeblue1-link-sub\">New Product Introduction<br>\r\n");
      out.write("\t\t\t</a>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_NewProduct_Geography.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales by Geography</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_NewProduct_Period.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per Period</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"BizIntel_NewProduct_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per product</a><br>\r\n");
      out.write("\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t    <tr valign=\"bottom\"> \r\n");
      out.write("\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t  </table>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div id=\"rightwhite\">\r\n");
      out.write("\t\r\n");
      out.write("\t");
 } else {  
	
	if(pagenm.equals("diversion")) {     
      out.write("  \r\n");
      out.write("\t\r\n");
      out.write("\t<!-- Left channel -->\r\n");
      out.write("\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
      out.write("\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("\t    <tr> \r\n");
      out.write("\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Diversion</td>\r\n");
      out.write("\t    </tr>\r\n");
      out.write("\t    <tr> \r\n");
      out.write("\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
      out.write("\t        <a href=\"index.html\" class=\"typeblue1-link\">Diversion</a><br>\r\n");
      out.write("\t        <!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
      out.write("\t        <a href=\"Diversion_Search.html\" class=\"typeblue1-link-sub\">Search</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_Search_Pedigree.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Pedigree</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_Search_EPC.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;EPC</a><br>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t        <a href=\"Diversion_Discrepancy.html\" class=\"typeblue1-link-sub\">ASN/RM Discrepancy</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_Discrepancy_3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;3PL Carrier</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_Discrepancy_ShipFrom.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Ship from Location</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_Discrepancy_ShipTo.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Ship to Location</a><br>\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\t        <a href=\"Diversion_TheftAlerts.html\" class=\"typeblue1-link-sub\">Theft Alerts<br>\r\n");
      out.write("\t        </a>\r\n");
      out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewEPC.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View EPC</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewPedigree.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View Pedigree</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewAgencyReport.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View Agency Report</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewLocations.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View Locations</a><br>\r\n");
      out.write("\t       \r\n");
      out.write("\t\t    <a href=\"Diversion_PatientInfo.html\" class=\"typeblue1-link-sub\">Patient Information Search<br>\r\n");
      out.write("\t        </a>\r\n");
      out.write("\t\t\t<a href=\"Diversion_PatientInfo_Name.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Patient Name</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_PatientInfo_ID.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Patient ID</a><br>\r\n");
      out.write("\t\t\t<a href=\"Diversion_PatientInfo_Perscriptions.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Perscriptions</a><br>\r\n");
      out.write("\t\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t    </tr>\r\n");
      out.write("\t    <tr valign=\"bottom\"> \r\n");
      out.write("\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t    </tr>\r\n");
      out.write("\t  </table>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t\r\n");
      out.write("\t<div id=\"rightwhite\">\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\t");
 } else { 
	if(pagenm.equals("integrity")) {     
      out.write(" \r\n");
      out.write("\t\r\n");
      out.write("\t\t<!-- Left channel -->\r\n");
      out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
      out.write("\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("\t\t    <tr> \r\n");
      out.write("\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Product Integrity</td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t    <tr> \r\n");
      out.write("\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
      out.write("\t\t\t<a href=\"index.html\" class=\"typeblue1-link\">Product Integrity</a><br>\r\n");
      out.write("\t\t\t<!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
      out.write("\t\t\t<a href=\"ProductIntegrity_Search.html\" class=\"typeblue1-link-sub\">Search</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"ProductIntegrity_Search_EPC.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;EPC</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"ProductIntegrity_Search_Pedigree.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Pedigree</a><br>\r\n");
      out.write("\t\t\t<a href=\"ProductIntegrity_SensorViolations.html\" class=\"typeblue1-link-sub\">Sensor \r\n");
      out.write("\t\t\tViolations </a><br>\r\n");
      out.write("\t\t\t\t<a href=\"ProductIntegrity_SensorViolations_Temperature.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Temperature</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"ProductIntegrity_SensorViolations_Vibration.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Vibration</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"ProductIntegrity_SensorViolations_Humidity.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Humidity</a><br>\r\n");
      out.write("\t\t\t<a href=\"ProductIntegrity_Reports.html\" class=\"typeblue1-link-sub\">Reports<br>\r\n");
      out.write("\t\t\t</a>\r\n");
      out.write("\t\t\t\t<a href=\"ProductIntegrity_Reports_TradingPartner.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor Violations per Trading Partner</a><br>\r\n");
      out.write("\t\t\t\t<a href=\"ProductIntegrity_Reports_3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor Violations per 3PL</a><br>\r\n");
      out.write("\t\t\t<a href=\"ProductIntegrity_Reports_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
      out.write("\t\t\tViolations per Product</a><br>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t    <tr valign=\"bottom\"> \r\n");
      out.write("\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t  </table>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\r\n");
      out.write("\t\t<div id=\"rightwhite\">\r\n");
      out.write("\t\r\n");
      out.write("\t");
 } else { 
	
	if(pagenm.equals("reports")) { 
      out.write("\r\n");
      out.write("\t\t<!-- Left channel -->\r\n");
      out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
      out.write("\t\t\t<table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">Reports</td>\t\t\t\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t<td width=\"10\" valign=\"top\" bgcolor=\"#dcdcdc\"></td>\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t<tr valign=\"bottom\">\r\n");
      out.write("\t\t\t\t\t<td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t</table>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div id=\"rightwhite2\">\r\n");
      out.write("\t\t\r\n");
      out.write("\t");
 }  else { 
	
	if(pagenm.equals("pedBank")) { 
      out.write("\r\n");
      out.write("\t\t<!-- Left channel -->\r\n");
      out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
      out.write("\t\t\t\t<table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">ePedigree Manager</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("              <tr valign=\"bottom\">\r\n");
      out.write("\t\t\t\t\t<td   colspan=\"2\" class=\"td-left\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div id=\"rightwhite2\">\r\n");
      out.write("\t\r\n");
      out.write("\t");
 } else {  
		if(pagenm.equals("track")) { 
      out.write("\r\n");
      out.write("\t\t<!-- Left channel -->\r\n");
      out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
      out.write("\t\t\t\t<table width=\"170\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\" align=\"center\">Track and Trace</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t<div id=\"rightwhite2\"></div>\r\n");
      out.write("\t");
 }  
		if(pagenm.equals("returns")) { 
      out.write("\r\n");
      out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
      out.write("\t\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("\t\t\t    <tr> \r\n");
      out.write("\t\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Returns</td>\r\n");
      out.write("\t\t\t    </tr>\r\n");
      out.write("\t\t\t    <tr> \r\n");
      out.write("\t\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("\t\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
      out.write("\t\t\t\t  \t<a href=\"../returns/returns.jsp?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=returns\" class=\"typeblue1-link\">ITEM</a><br>\r\n");
      out.write("\t\t\t\t\t<a href=\"../returns/returns_EPC.jsp?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=returns\" class=\"typeblue1-link\">CASE</a><br>\r\n");
      out.write("\t\t\t        <!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> -->        </td>\r\n");
      out.write("\t\t\t    </tr>\r\n");
      out.write("\t\t\t    <tr valign=\"bottom\"> \r\n");
      out.write("\t\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"../assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t\t\t    </tr>\r\n");
      out.write("\t\t\t  </table>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t<div id=\"rightwhite2\"> \r\n");
      out.write("\t\t\r\n");
      out.write("\t");
 } else {  
      out.write('\r');
      out.write('\n');
      out.write('	');
 System.out.println("************************************************************Inside Top menu left jsp********");
      out.write('\r');
      out.write('\n');
      out.write('	');
 if(pagenm.equals("TPManager")) { 
      out.write("\r\n");
      out.write("\t\t<!-- Left channel -->\r\n");
      out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
      out.write("\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
      out.write("\t\t    <tr> \r\n");
      out.write("\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Vendor Information Manager</td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t    <tr> \r\n");
      out.write("\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
      out.write("\t\t        <a href=\"TradingPartnerList.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=TPManager&linkName=tpmanager\" class=");
      out.print((linkName.equals("tpmanager"))?"typered4-link":"typeblue3-link");
      out.write("><strong> Vendor Information Manager</strong></a><br> \r\n");
      out.write("\t\t        ");
 if(tpList.equalsIgnoreCase("view")&&tpList != null){
		          String tpGenId1=request.getParameter("tpGenId");
String  tpName1=request.getParameter("tpName");
		        if(tpGenId1==null)tpGenId1="";
		        if(tpName1==null)tpName1="";
		        
		        
		         
      out.write("\r\n");
      out.write("\t\t     &nbsp;&nbsp;&nbsp;&nbsp;   <A href=\"TradingPartnerView.do?operationType=FIND&tpGenId=");
      out.print(tpGenId1);
      out.write("&tpName=");
      out.print(tpName1);
      out.write("&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=");
      out.print(pagenm);
      out.write("&tpList=view&linkName=tpmain\" class=");
      out.print((linkName.equals("tpmain"))?"typered4-link":"typeblue3-link");
      out.write("><strong>Main </strong></A>&nbsp;&nbsp;&nbsp;&nbsp;<br/>\r\n");
      out.write("\t\t\t &nbsp;&nbsp;&nbsp;&nbsp;\t<A href=\"TPLocationList.do?tpGenId=");
      out.print(tpGenId1);
      out.write("&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=");
      out.print(pagenm);
      out.write("&tpList=view&tlList=view&linkName=tpLocation\" class=");
      out.print((linkName.equals("tpLocation"))?"typered4-link":"typeblue3-link");
      out.write(" onClick=\"return LocationReadPrivilage();\"><strong>Locations </strong></A>&nbsp;&nbsp;&nbsp;&nbsp;<br/>\r\n");
      out.write("\t\t\t          ");

			          String tlList1=request.getParameter("tlList");
			          if(tlList1==null)tlList1="";
			          if(tlList1.equalsIgnoreCase("view"))
			          {
			          
      out.write("\r\n");
      out.write("\t\t\t  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;         <a href=\"LocationNew.do?tpGenId=");
      out.print(tpGenId1);
      out.write("&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=");
      out.print(pagenm);
      out.write("&tlList=view&tpList=view&linkName=createLoc\" class=");
      out.print((linkName.equals("createLoc"))?"typered4-link":"typeblue3-link");
      out.write(" >Create New Location</a><br> \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t          ");
}
      out.write("\r\n");
      out.write("\t\t\t \r\n");
      out.write("\t\t\t ");

			 
			 String tcList1=request.getParameter("tcList");
			 if(tcList1==null)tcList1="";
			 if(tcList1.equalsIgnoreCase("view"))
			 {
			 
      out.write("\r\n");
      out.write("\t\t\t  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;    <a href=\"CatalogNew.do?tpGenId=");
      out.print(tpGenId1);
      out.write("&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=");
      out.print(pagenm);
      out.write("&tpList=view&tcList=view&linkName=tpNewCatalog\" class=");
      out.print((linkName.equals("tpNewCatalog"))?"typered4-link":"typeblue3-link");
      out.write(" >Create New Catalog</a><br>\r\n");
      out.write("\t\t\t ");
}
			 
      out.write("\r\n");
      out.write("\t\t\t \r\n");
      out.write("\t\t\t ");

			 String catSchema=request.getParameter("catSchema");
			 if(catSchema==null)catSchema="";
			 if(catSchema.equalsIgnoreCase("view"))
			 {
			 
			 genId=request.getParameter("catalogGenId");
			  if(genId==null)genId="";
			 
      out.write("\r\n");
      out.write("\t\t &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; \t <a href=\"OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=");
      out.print( genId);
      out.write("&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=");
      out.print(pagenm);
      out.write("&tpList=view&tcList=view&linkName=catSchemaDef\" class=");
      out.print((linkName.equals("catSchemaDef"))?"typered4-link":"typeblue3-link");
      out.write(" onClick=\"return CatalogAccessPrivilage()\">\r\n");
      out.write("\t\t\t\t\tSchema Def</a><br>\r\n");
      out.write("\t\t\t\t &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;\t  <a href=\"SelectMasterCatalog.do?leftCatalogGenId=");
      out.print( genId);
      out.write("&tpGenId=");
      out.print(tpGenId1);
      out.write("&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=");
      out.print(pagenm);
      out.write("&tpList=view&tcList=view&linkName=MapToStandard\" class=");
      out.print((linkName.equals("MapToStandard"))?"typered4-link":"typeblue3-link");
      out.write(" onClick=\"return CatalogAccessPrivilage()\">Map To Standard </a> <br>\r\n");
      out.write("\t\t\t \r\n");
      out.write("\t\t\t ");

			 }
			 
      out.write("\r\n");
      out.write("\t\t\t \r\n");
      out.write("\t\t  \t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t<a href=\"TradingPartnerNew.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=TPManager&linkName=createTP\" class=");
      out.print((linkName.equals("createTP"))?"typered4-link":"typeblue3-link");
      out.write(" >Create New Vendor</a><br>\r\n");
      out.write("\t\t      </td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t    <tr valign=\"bottom\"> \r\n");
      out.write("\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"./assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t  </table>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t");
 } else if(pagenm.equals("Catalog")) {  
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\t<div id=\"leftgray4\"> \r\n");
      out.write("  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
      out.write("    <tr> \r\n");
      out.write("      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">GCPIM</td>\r\n");
      out.write("    </tr>\r\n");
      out.write("    <tr> \r\n");
      out.write("      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("      <td valign=\"top\" class=\"td-left\" ><br>\r\n");
      out.write("  \r\n");
      out.write("        <a href=\"ProductMasterSearchEmpty.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=Catalog&linkName=pSearch\" class=");
      out.print((linkName.equals("pSearch"))?"typered4-link":"typeblue3-link");
      out.write(">Product Search</a><br> \r\n");
      out.write("\t\t<a href=\"ShowMasterCatalogs.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=Catalog&linkName=newProduct\" class=");
      out.print((linkName.equals("newProduct"))?"typered4-link":"typeblue3-link");
      out.write(">Create New Product</a><br>\r\n");
      out.write("\t\t<a href=\"KitNew.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=Catalog&linkName=newKit\" class=");
      out.print((linkName.equals("newKit"))?"typered4-link":"typeblue3-link");
      out.write(">Create New Kit</a><br>\r\n");
      out.write("\t\t<a href=\"GCPIMOpenCatalogSchemaDef.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=Catalog&linkName=editMaster\" class=");
      out.print((linkName.equals("editMaster"))?"typered4-link":"typeblue3-link");
      out.write(">Edit Master Product Schema</a><br>\r\n");
      out.write("     \t\r\n");
      out.write("     \t");
 if(linkName.equals("manageStandard")||linkName.equals("newCatalog")){ 
      out.write("\r\n");
      out.write("\t\t\t\t<a href=\"GCPIMStandardCatalogList.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=Catalog&linkName=manageStandard\" class=");
      out.print((linkName.equals("manageStandard"))?"typered4-link":"typeblue3-link");
      out.write(">Manage Standard Catalogs</a><br>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"GCPIMStandardCatalogNew.do?operationType=ADD&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=Catalog&linkName=newCatalog\" class=");
      out.print((linkName.equals("newCatalog"))?"typered4-link":"typeblue3-link");
      out.write(">Create New Catalog</a>\r\n");
      out.write("\t\t\t");
}else{
      out.write("\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t\t<a href=\"GCPIMStandardCatalogList.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=Catalog&linkName=manageStandard\" class=");
      out.print((linkName.equals("manageStandard"))?"typered4-link":"typeblue3-link");
      out.write(">Manage Standard Catalogs</a><br>\r\n");
      out.write("\t\t\t");
}
      out.write("\r\n");
      out.write("\t  </td>\r\n");
      out.write("    </tr>\r\n");
      out.write("    <tr valign=\"bottom\"> \r\n");
      out.write("      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"./assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("    </tr>\r\n");
      out.write("  </table>\r\n");
      out.write("</div>\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\t");
}else{ 
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\t");
 if(pagenm.equals("pedigree")) { 
      out.write("\r\n");
      out.write("\t<!-- Left channel -->\r\n");
      out.write("\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
      out.write("\t\t<table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">ePedigree Manager</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td width=\"10\" valign=\"top\" bgcolor=\"#dcdcdc\"></td>\r\n");
      out.write("\t\t\t\t");
 //if (accessLevel.equals("true")){
      out.write("\r\n");
      out.write("\t\t\t\t<td valign=\"top\" class=\"td-left\"><br>\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t<!--\t<a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/ShippingManagerSearchEmpty.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=shippingmang\" class=");
      out.print((linkName.equals("shippingmang"))?"typered4-link":"typeblue3-link");
      out.write(">Shipping \r\n");
      out.write("\t\t\t\t\t\tManager<br>\r\n");
      out.write("\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t <a href=\"#\" class=\"typeblue3-link\">Find</a><br> \r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t<a href=\"../epedigree/PedigreeSearch.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&accesslevel=apnsearch&linkName=apnsear\" class=");
      out.print((linkName.equals("apnsear"))?"typered4-link":"typeblue3-link");
      out.write(">Search \r\n");
      out.write("\t\t\t\t\t\tPedigrees</a><br>\r\n");
      out.write("\t\t\t\t\t\t<br>-->\r\n");
      out.write("\t\t\t\t\t<a href=\"../epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&accesslevel=PTP&linkName=PTP\" class=");
      out.print((linkName.equals("PTP"))?"typered4-link":"typeblue3-link");
      out.write(">Add Pedigree Trading Partner \r\n");
      out.write("\t\t\t\t\t\t</a><br><br>\r\n");
      out.write("\t\t\t\t\t\t<a href=\"../epedigree/ListPedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&accesslevel=PTP&linkName=ListPTP\" class=");
      out.print((linkName.equals("ListPTP"))?"typered4-link":"typeblue3-link");
      out.write(">List Pedigree Trading Partner \r\n");
      out.write("\t\t\t\t\t\t</a><br>\r\n");
      out.write("\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t");
//}
      out.write("\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr valign=\"bottom\">\r\n");
      out.write("\t\t\t\t<td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t");
 System.out.println("************************************************************After Top menu left jsp********");
      out.write("\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t");
 } 
      out.write("\r\n");
      out.write("\t<div id=\"rightwhite2\">\r\n");
      out.write("\r\n");
      out.write("\t");
 } } } } } } } } }
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

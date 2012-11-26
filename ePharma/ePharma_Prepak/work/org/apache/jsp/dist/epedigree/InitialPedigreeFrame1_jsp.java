package org.apache.jsp.dist.epedigree;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.rdta.commons.CommonUtil;
import com.rdta.catalog.OperationType;
import org.w3c.dom.Node;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.Constants;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import java.util.List;

public final class InitialPedigreeFrame1_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {



		String genId="";

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(5);
    _jspx_dependants.add("/WEB-INF/struts-bean.tld");
    _jspx_dependants.add("/WEB-INF/struts-html.tld");
    _jspx_dependants.add("/WEB-INF/struts-logic.tld");
    _jspx_dependants.add("/dist/epedigree/topMenu.jsp");
    _jspx_dependants.add("/dist/epedigree/onLoad.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_html;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_form_enctype_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_hidden_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_select_property_onchange;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_option_value;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_text_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_select_value_property;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notPresent_property_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_present_property_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_write_property_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notEqual_value_property_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_text_size_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_rewrite_page_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_text_tabindex_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_text_value_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_file_tabindex_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_hidden_value_property_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_html_html = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_form_enctype_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_hidden_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_select_property_onchange = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_option_value = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_text_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_select_value_property = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notPresent_property_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_present_property_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_write_property_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notEqual_value_property_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_text_size_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_rewrite_page_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_text_tabindex_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_text_value_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_file_tabindex_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_hidden_value_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_html_html.release();
    _jspx_tagPool_html_form_enctype_action.release();
    _jspx_tagPool_html_hidden_property_nobody.release();
    _jspx_tagPool_html_select_property_onchange.release();
    _jspx_tagPool_html_option_value.release();
    _jspx_tagPool_html_text_property_nobody.release();
    _jspx_tagPool_html_select_value_property.release();
    _jspx_tagPool_logic_notPresent_property_name.release();
    _jspx_tagPool_logic_present_property_name.release();
    _jspx_tagPool_bean_write_property_name_nobody.release();
    _jspx_tagPool_logic_notEqual_value_property_name.release();
    _jspx_tagPool_html_text_size_property_nobody.release();
    _jspx_tagPool_html_rewrite_page_nobody.release();
    _jspx_tagPool_html_text_tabindex_property_nobody.release();
    _jspx_tagPool_html_text_value_property_nobody.release();
    _jspx_tagPool_html_file_tabindex_property_nobody.release();
    _jspx_tagPool_html_hidden_value_property_nobody.release();
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

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

String isUpdate=(String)request.getAttribute("updateSuccess");
if(isUpdate==null)isUpdate="";
System.out.println("isUpdate"+isUpdate);
String operationType=(String)request.getAttribute("operationType");
System.out.println("operationType"+operationType);

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
String tp_company_nm = (String)request.getParameter("tp_company_nm");
if(tp_company_nm==null)tp_company_nm="";
    String pagenm =  request.getParameter("pagenm");
    if(pagenm==null)pagenm="pedigree";
     String tpGenId = (String)session.getAttribute("tpGenId");
    String imageLocation = CommonUtil.returnEmptyIfNull((String) request.getAttribute("ImageFileLocation"));
    System.out.println("imageLocation"+imageLocation);
    String sessionID = (String)session.getAttribute("sessionID");
    

      out.write("\r\n");
      out.write("\r\n");
      out.write("   ");
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
   			if(pagenm == null) pagenm = (String)request.getAttribute("pagenm");
			if(tp_company_nm == null) tp_company_nm = (String)request.getAttribute("tp_company_nm");
   			
 
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!-- Top Header -->\r\n");
      out.write("<div id=\"bg\" style=\"Z-INDEX: 100\">\r\n");
      out.write("\t<div class=\"roleIcon-prePack\"></div>\r\n");
      out.write("\t<div class=\"navIcons\">\r\n");
      out.write("\t\t<a href=\"");
      out.print(servPath);
      out.write("/logout.jsp\" target=\"_top\"><img src=\"");
      out.print(servPath);
      out.write("/assets/images/logout.gif\" width=\"34\" height=\"27\" hspace=\"10\" border=\"0\"></a>\r\n");
      out.write("\t\t<a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=eped\" target=\"_top\"><IMG height=\"27\" hspace=\"10\" src=\"");
      out.print(servPath);
      out.write("/assets/images/inbox.gif\" border=\"0\"></a>\r\n");
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
      out.write("/assets/images/bg_menu.jpg\"><table width=\"800\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table2\">\r\n");
      out.write("\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("epcadmin")) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/AdminUser.do?pagenm=epcadmin&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("\"\" target=\"_parent\">Admin Console</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/AdminUser.do?pagenm=epcadmin&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("\"\" target=\"_parent\">Admin Console</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } 
      out.write("\t\t\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t<td><img src=\"");
      out.print(servPath);
      out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("pedigree")) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=eped\" class=\"menu-link menuBlack\" target=\"_parent\">ePedigree</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=eped\" class=\"menu-link menuBlack\" target=\"_parent\">ePedigree</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } 
      out.write("\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t<td><img src=\"");
      out.print(servPath);
      out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
      out.write("\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("TPManager")) { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"/ePharma/TradingPartnerList.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=TPManager\" class=\"menu-link\" target=\"_parent\">Trading Partner Manager</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } else { 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
      out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"/ePharma/TradingPartnerList.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=TPManager\" class=\"menu-link\" target=\"_parent\">Trading Partner Manager</a></td>\r\n");
      out.write("\t\t\t\t\t\t\t");
 } 
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\r\n");
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
      out.write("\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Trading Partner Manager</td>\r\n");
      out.write("\t\t    </tr>\r\n");
      out.write("\t\t    <tr> \r\n");
      out.write("\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
      out.write("\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
      out.write("\t\t        <a href=\"TradingPartnerList.do?tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&pagenm=TPManager&linkName=tpmanager\" class=");
      out.print((linkName.equals("tpmanager"))?"typered4-link":"typeblue3-link");
      out.write("><strong>Trading Partner Manager</strong></a><br> \r\n");
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
      out.write(" >Create New Trading Partner</a><br>\r\n");
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
      out.write("\t\t\t\t\t<a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/InitialPedigree.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=initial\" class=");
      out.print((linkName.equals("initial"))?"typered4-link":"typeblue3-link");
      out.write(" >Enter Pedigree \r\n");
      out.write("\t\t\t\t\t</a><br>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/ProcessedEnteredPedigrees.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=processedEnteredPedigrees\" class=");
      out.print((linkName.equals("processedEnteredPedigrees"))?"typered4-link":"typeblue3-link");
      out.write(" >Processed Pedigrees\r\n");
      out.write("\t\t\t\t\t</a><br>&nbsp;&nbsp;\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t<!--<a href=\"");
      out.print(servPath);
      out.write("/dist/epedigree/ShippingManagerSearchEmpty.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&linkName=shippingmang\" class=");
      out.print((linkName.equals("shippingmang"))?"typered4-link":"typeblue3-link");
      out.write(">Shipping \r\n");
      out.write("\t\t\t\t\t\tManager<br>\r\n");
      out.write("\t\t\t\t\t</a>-->\r\n");
      out.write("\t\t\t\t\t<br>\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t<a href=\"../epedigree/PedigreeSearch.do?pagenm=pedigree&tp_company_nm=");
      out.print(tp_company_nm);
      out.write("&accesslevel=apnsearch&linkName=apnsear\" class=");
      out.print((linkName.equals("apnsear"))?"typered4-link":"typeblue3-link");
      out.write(">Search Pedigrees</a><br>\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\r\n");
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
      out.write('\r');
      out.write('\n');
      //  html:html
      org.apache.struts.taglib.html.HtmlTag _jspx_th_html_html_0 = (org.apache.struts.taglib.html.HtmlTag) _jspx_tagPool_html_html.get(org.apache.struts.taglib.html.HtmlTag.class);
      _jspx_th_html_html_0.setPageContext(_jspx_page_context);
      _jspx_th_html_html_0.setParent(null);
      int _jspx_eval_html_html_0 = _jspx_th_html_html_0.doStartTag();
      if (_jspx_eval_html_html_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>\r\n");
          out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\r\n");
          out.write("<link href=\"../../assets/epedigree1.css\" type=\"text/css\" rel=\"stylesheet\" >\r\n");
          out.write("<link href=\"/ePharma/assets/images/calendar-win2k-1.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("<script language=\"JavaScript\" src=\"gen_validatorv2.js\" type=\"text/javascript\"></script>\r\n");
          out.write("<script language=\"JavaScript\" src=\"calendar.js\"></script>\r\n");
          out.write("<script language=\"JavaScript\" src=\"calendar-en.js\"></script>\r\n");
          out.write("<script language=\"JavaScript\" src=\"calendar-setup.js\"></script>\r\n");
          out.write("<script language=\"JavaScript\" src=\"callCalendar.js\"></script>\r\n");
          out.write("\r\n");
          out.write(" <script language = \"Javascript\">\r\n");
          out.write("/**\r\n");
          out.write(" * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)\r\n");
          out.write(" */\r\n");
          out.write("// Declaring valid date character, minimum year and maximum year\r\n");
          out.write("var dtCh= \"-\";\r\n");
          out.write("var dtCh1= \"/\";\r\n");
          out.write("var minYear=1900;\r\n");
          out.write("var maxYear=2100;\r\n");
          out.write("\r\n");
          out.write("function isInteger(s){\r\n");
          out.write("\tvar i;\r\n");
          out.write("    for (i = 0; i < s.length; i++){   \r\n");
          out.write("        // Check that current character is number.\r\n");
          out.write("        var c = s.charAt(i);\r\n");
          out.write("        if (((c < \"0\") || (c > \"9\"))) return false;\r\n");
          out.write("    }\r\n");
          out.write("    // All characters are numbers.\r\n");
          out.write("    return true;\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("function stripCharsInBag(s, bag){\r\n");
          out.write("\tvar i;\r\n");
          out.write("    var returnString = \"\";\r\n");
          out.write("    // Search through string's characters one by one.\r\n");
          out.write("    // If character is not in bag, append to returnString.\r\n");
          out.write("    for (i = 0; i < s.length; i++){   \r\n");
          out.write("        var c = s.charAt(i);\r\n");
          out.write("        if (bag.indexOf(c) == -1) returnString += c;\r\n");
          out.write("    }\r\n");
          out.write("    return returnString;\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("function daysInFebruary (year){\r\n");
          out.write("\t// February has 29 days in any year evenly divisible by four,\r\n");
          out.write("    // EXCEPT for centurial years which are not also divisible by 400.\r\n");
          out.write("    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );\r\n");
          out.write("}\r\n");
          out.write("function DaysArray(n) {\r\n");
          out.write("\tfor (var i = 1; i <= n; i++) {\r\n");
          out.write("\t\tthis[i] = 31\r\n");
          out.write("\t\tif (i==4 || i==6 || i==9 || i==11) {this[i] = 30}\r\n");
          out.write("\t\tif (i==2) {this[i] = 29}\r\n");
          out.write("   } \r\n");
          out.write("   return this\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("function isExpirationDate(dtStr1){\r\n");
          out.write("\t\r\n");
          out.write("\tvar pos1=dtStr1.indexOf(dtCh1)\r\n");
          out.write("\t\r\n");
          out.write("\tvar strMonth = dtStr1.substring(0,pos1)\r\n");
          out.write("\tvar strYear = dtStr1.substring(pos1+1)\r\n");
          out.write("\r\n");
          out.write("\tstrYr1=strYear\r\n");
          out.write("\tstrMonth1=strMonth\r\n");
          out.write("\t\t\r\n");
          out.write("\tif (strMonth.charAt(0)==\"0\" && strMonth.length>1) strMonth1=strMonth.substring(1)\r\n");
          out.write("\tfor (var i = 1; i <= 3; i++) {\r\n");
          out.write("\t\tif (strYr1.charAt(0)==\"0\" && strYr1.length>1) strYr1=strYr1.substring(1)\r\n");
          out.write("\t}\r\n");
          out.write("\tmonth = parseInt(strMonth1)\r\n");
          out.write("\tyear = parseInt(strYr1)\r\n");
          out.write("\r\n");
          out.write("\tif (pos1==-1){\r\n");
          out.write("\t\talert(\"The date format should be : MM/YY\")\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\tif (strMonth.length<2 || month<1 || month>12){\r\n");
          out.write("\t\talert(\"Please enter a valid month\")\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\t\r\n");
          out.write("\tif (strYear.length != 2 || year==0 || year<0 || year>99){\r\n");
          out.write("\t\talert(\"Please enter a valid 2 digit year between 00 and 99\")\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\t\r\n");
          out.write("return true\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("function isDate(dtStr){\r\n");
          out.write("\tvar daysInMonth = DaysArray(12)\r\n");
          out.write("\t\r\n");
          out.write("\tvar pos1=dtStr.indexOf(dtCh)\r\n");
          out.write("\tvar pos2=dtStr.indexOf(dtCh,pos1+1)\r\n");
          out.write("\tvar strYear=dtStr.substring(0,pos1)\r\n");
          out.write("\tvar strMonth=dtStr.substring(pos1+1,pos2)\r\n");
          out.write("\tvar strDay=dtStr.substring(pos2+1)\r\n");
          out.write("\t \r\n");
          out.write("\tstrYr=strYear\r\n");
          out.write("\tif (strDay.charAt(0)==\"0\" && strDay.length>1) strDay=strDay.substring(1)\r\n");
          out.write("\tif (strMonth.charAt(0)==\"0\" && strMonth.length>1) strMonth=strMonth.substring(1)\r\n");
          out.write("\tfor (var i = 1; i <= 3; i++) {\r\n");
          out.write("\t\tif (strYr.charAt(0)==\"0\" && strYr.length>1) strYr=strYr.substring(1)\r\n");
          out.write("\t}\r\n");
          out.write("\tmonth=parseInt(strMonth)\r\n");
          out.write("\tday=parseInt(strDay)\r\n");
          out.write("\tyear=parseInt(strYr)\r\n");
          out.write("\tif (pos1==-1 || pos2==-1){\r\n");
          out.write("\t\talert(\"The date format should be : yyyy-mm-dd\")\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\tif (strMonth.length<1 || month<1 || month>12){\r\n");
          out.write("\t\talert(\"Please enter a valid month\")\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\tif (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){\r\n");
          out.write("\t\talert(\"Please enter a valid day\")\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\tif (strYear.length != 4 || year==0 || year<minYear || year>maxYear){\r\n");
          out.write("\t\talert(\"Please enter a valid 4 digit year between \"+minYear+\" and \"+maxYear)\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\tif (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){\r\n");
          out.write("\t\talert(\"Please enter a valid date\")\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("return true\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write(" \r\n");
          out.write("\r\n");
          out.write("</script>\r\n");
          out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
          out.write("<!--\r\n");
          out.write("function MM_goToURL() { //v3.0\r\n");
          out.write("  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;\r\n");
          out.write("  for (i=0; i<(args.length-1); i+=2) eval(args[i]+\".location='\"+args[i+1]+\"'\");\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("function tpDetails(){\r\n");
          out.write("\r\n");
          out.write("\tvar frm= document.forms[0];\r\n");
          out.write("\tdocument.forms[0].operationType.value = \"TPDetails\"\r\n");
          out.write("\tfrm.action = \"InitialPedigree.do\"\r\n");
          out.write("\tfrm.submit();\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("function lookupNDC(){\r\n");
          out.write("\t\r\n");
          out.write("\tif (validate_required(document.forms[0].productCode,\"Enter ProductCode\")==false)\r\n");
          out.write("  \t{document.forms[0].productCode.focus();return false}\r\n");
          out.write("  \t \r\n");
          out.write("   \tif(document.forms[0].productCode.value.length <9) {\r\n");
          out.write("  \talert('ProductCode must contain 9 or more digits');\r\n");
          out.write("  \treturn false;\r\n");
          out.write("  \t}\r\n");
          out.write("  \t\r\n");
          out.write("  \tvar frm= document.forms[0];\r\n");
          out.write("\tdocument.forms[0].operationType.value = \"lookupNDC\"\r\n");
          out.write("\t\r\n");
          out.write("\tfrm.action = \"InitialPedigree.do\"\r\n");
          out.write("\tfrm.submit();\r\n");
          out.write("\r\n");
          out.write("\t  \r\n");
          out.write("}\r\n");
          out.write(" \r\n");
          out.write(" \r\n");
          out.write(" \r\n");
          out.write("function validate_required(field,alerttxt)\r\n");
          out.write("{ \r\n");
          out.write("with(field)\r\n");
          out.write("{\r\n");
          out.write("if(field==null||value==\"\")\r\n");
          out.write("  {alert(alerttxt);return false}\r\n");
          out.write("else {return true}\r\n");
          out.write("}\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("function validateForAttachement(field)\r\n");
          out.write("{ \r\n");
          out.write("with(field)\r\n");
          out.write("{\r\n");
          out.write("if(field==null||value==\"\")\r\n");
          out.write("  {\r\n");
          out.write("  \t\r\n");
          out.write("  \tif(confirm(\"No Paper Pedigrees Attached. Continue?\")) {\r\n");
          out.write("\t\treturn true;\r\n");
          out.write("\t}\r\n");
          out.write("  \treturn false\r\n");
          out.write("  }\r\n");
          out.write("  else \r\n");
          out.write("  {\r\n");
          out.write("  \treturn true\r\n");
          out.write("  }\r\n");
          out.write("}\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("function validate_form(thisform)\r\n");
          out.write("{\r\n");
          out.write(" \r\n");
          out.write("with (document.forms[0])\r\n");
          out.write("{\r\n");
          out.write(" \r\n");
          out.write("if(!validateAddLotInfo())\r\n");
          out.write("{\r\n");
          out.write("return false;\r\n");
          out.write("}\t\r\n");
          out.write("\t\r\n");
          out.write("if(validateForAttachement(document.forms[0].attachment)==false)\r\n");
          out.write("  {document.forms[0].attachment.focus();return false}\r\n");
          out.write("  \r\n");
          out.write("if (validate_required(document.forms[0].name,\"DrugName must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].name.focus();return false}\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].productCode,\"ProductCode   must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].productCode.focus();return false}\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].strength,\"Strength must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].strength.focus();return false}\r\n");
          out.write("  \r\n");
          out.write("if (validate_required(document.forms[0].manufacturer,\"Manufacturer must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].manufacturer.focus();return false}\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].containerSize,\"ContainerSize must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].containerSize.focus();return false}\r\n");
          out.write(" \r\n");
          out.write("if (validate_required(document.forms[0].contactName,\"ContactName must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].contactName.focus();return false}\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].dosageForm ,\"DosageForm must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].dosageForm.focus();return false}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].transactionId ,\"Document Number must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].transactionId.focus();return false}\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].transactionDate,\"Document Date must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].transactionDate.focus();return false}\r\n");
          out.write("\r\n");
          out.write("  \r\n");
          out.write("if (validate_required(document.forms[0].tpName,\"TradingPartner name must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].tpName.focus();return false}\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].address1,\"Address1   must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].address1.focus();return false}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].city,\"City   must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].city.focus();return false}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].country,\"Country   must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].country.focus();return false}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].state,\"State   must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].state.focus();return false}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("if (validate_required(document.forms[0].zipCode,\"ZipCode  must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].zipCode.focus();return false}\r\n");
          out.write("  \r\n");
          out.write("  \r\n");
          out.write("  \r\n");
          out.write("  if (validate_required(document.forms[0].contactPhone,\"Contact Phone  must be filled out!\")==false)\r\n");
          out.write("  {document.forms[0].contactPhone.focus();return false}\r\n");
          out.write("  \r\n");
          out.write("   \r\n");
          out.write("\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("  \r\n");
          out.write("  \r\n");
          out.write("var dt1=document.forms[0].transactionDate;\r\n");
          out.write("\tif (isDate(dt1.value)==false){\r\n");
          out.write("\t\tdt1.focus()\r\n");
          out.write("\t\treturn false\r\n");
          out.write("\t}\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write(" \r\n");
          out.write("}\r\n");
          out.write(" \r\n");
          out.write(" \r\n");
          out.write("function validateAddLotInfo(){\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("var lot=document.getElementsByName(\"lotNumber\")\r\n");
          out.write("\r\n");
          out.write("for(var k=0;k<lot.length;k++)\r\n");
          out.write("\t{\r\n");
          out.write("  \r\n");
          out.write("  var lotno=lot[k].value;\r\n");
          out.write(" if(lotno==null || lot[k].value==\"\")\r\n");
          out.write("{\r\n");
          out.write("\r\n");
          out.write(" alert('Lot Number must be filled out!!!')\r\n");
          out.write(" lot[k].focus();\r\n");
          out.write(" return false;\r\n");
          out.write(" \r\n");
          out.write("}\r\n");
          out.write(" \r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write(" \r\n");
          out.write("var s1=document.getElementsByName(\"quantity\")\r\n");
          out.write("\r\n");
          out.write("for(var i=0;i<s1.length;i++)\r\n");
          out.write("\t{\r\n");
          out.write("  \r\n");
          out.write("  \r\n");
          out.write(" var s=isInteger(s1[i].value);\r\n");
          out.write(" \r\n");
          out.write("if(s==false|| s1[i].value==\"\")\r\n");
          out.write("{\r\n");
          out.write("\r\n");
          out.write(" alert('Please enter Quantity as Integer!!')\r\n");
          out.write(" s1[i].focus();\r\n");
          out.write(" return false;\r\n");
          out.write(" \r\n");
          out.write("}\r\n");
          out.write(" \r\n");
          out.write("}\r\n");
          out.write("   \r\n");
          out.write(" \r\n");
          out.write(" \r\n");
          out.write("  var dt1=document.getElementsByName(\"expirationDate\")\r\n");
          out.write(" \r\n");
          out.write("\tfor(var j=0;j<dt1.length;j++)\r\n");
          out.write("\t{\r\n");
          out.write("\t\r\n");
          out.write("\tif(dt1[j].value==\"\" || dt1[j].value==null){\r\n");
          out.write("\t}else\r\n");
          out.write("\tif (isExpirationDate(dt1[j].value)==false){\r\n");
          out.write("\t\tdt1[j].focus()\r\n");
          out.write("\t \r\n");
          out.write("\t return false;\r\n");
          out.write("\t}\r\n");
          out.write("\t}\r\n");
          out.write("\t\r\n");
          out.write("\t return true;\r\n");
          out.write("\t\r\n");
          out.write("\t \r\n");
          out.write(" \r\n");
          out.write(" }\r\n");
          out.write(" \r\n");
          out.write(" \r\n");
          out.write(" \r\n");
          out.write("    function addLotInfo()\r\n");
          out.write(" {\r\n");
          out.write("\t \r\n");
          out.write("\t var chkbox = document.createElement('INPUT'); \r\n");
          out.write("        chkbox.type = 'checkbox'\r\n");
          out.write("        chkbox.id = chkbox.name = 'check'\r\n");
          out.write("     \r\n");
          out.write("\t\r\n");
          out.write("\tvar qtyName = document.createElement('SPAN')\r\n");
          out.write("\tqtyName.innerText = '* Shipped Quantity :'\r\n");
          out.write("    \t\r\n");
          out.write("\tvar qtyText = document.createElement('INPUT')\r\n");
          out.write("\t\tqtyText.type = 'TEXT'\r\n");
          out.write("\t\tqtyText.id = qtyText.name = 'quantity'\r\n");
          out.write("\t\tqtyText.tabIndex=20\t\r\n");
          out.write("\t\t\r\n");
          out.write("\r\n");
          out.write("\tvar lotName = document.createElement('SPAN')\r\n");
          out.write("\tlotName.innerText = '* Lot Number :'\r\n");
          out.write("\t\t\r\n");
          out.write("\t\t\r\n");
          out.write("\tvar lotText = document.createElement('INPUT')\r\n");
          out.write("\t\tlotText.type = 'TEXT'\r\n");
          out.write("\t\tlotText.id = lotText.name = 'lotNumber'\r\n");
          out.write("\t    lotText.tabIndex=22\r\n");
          out.write("\t    \r\n");
          out.write("\t\r\n");
          out.write("   \r\n");
          out.write("         \r\n");
          out.write("\t\r\n");
          out.write("      var  expirationDateName = document.createElement('SPAN')\r\n");
          out.write("\t expirationDateName.innerText = 'Expiration Date(MM/YY) :'\r\n");
          out.write("\t\r\n");
          out.write("\t\t\r\n");
          out.write("\r\n");
          out.write("\tvar table = document.getElementById('dynamicTable');\r\n");
          out.write("\r\n");
          out.write("\tvar newTD,newTD1,newTD2,newTD3,newTD4,newTD5,newTD6,newTD7,newTD8,newTD9;\r\n");
          out.write("\tvar newTR = document.createElement('TR');\r\n");
          out.write("\tvar newTR1 = document.createElement('TR');\r\n");
          out.write("\tnewTR.className = 'tableRow_Off'\r\n");
          out.write("\t\r\n");
          out.write("\tnewTR.appendChild( newTD = document.createElement('TD') );\r\n");
          out.write("\tnewTR.appendChild( newTD1 = document.createElement('TD') );\r\n");
          out.write("\tnewTR.appendChild( newTD2 = document.createElement('TD') );\r\n");
          out.write("\tnewTR.appendChild( newTD3 = document.createElement('TD') );\r\n");
          out.write("\tnewTR.appendChild( newTD4 = document.createElement('TD') );\r\n");
          out.write("\t\r\n");
          out.write("\r\n");
          out.write("\tnewTD.appendChild( chkbox );\r\n");
          out.write("\tnewTD1.appendChild( qtyName );\r\n");
          out.write("\tnewTD2.appendChild( qtyText );\r\n");
          out.write("\tnewTD3.appendChild ( lotName );\r\n");
          out.write("\tnewTD4.appendChild ( lotText );\r\n");
          out.write("\t\r\n");
          out.write("\t//newTR1.className = 'tableRow_Off'\r\n");
          out.write("\t//newTR1.appendChild( newTD5 = document.createElement('TD') );\r\n");
          out.write("\t//newTR1.appendChild( newTD6 = document.createElement('TD') );\r\n");
          out.write("\t//newTR1.appendChild( newTD7 = document.createElement('TD') );\r\n");
          out.write("\t//newTR1.appendChild( newTD8 = document.createElement('TD') );\r\n");
          out.write("\t//newTR1.appendChild( newTD9 = document.createElement('TD') );\r\n");
          out.write("\t\r\n");
          out.write("\tnewTD.className = newTD1.className = newTD2.className = newTD3.className = newTD4.className = \"tableRow_Off\";\r\n");
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t\t\r\n");
          out.write("\t//newTD5.className = newTD6.className = newTD7.className = newTD8.className = newTD9.className= \"tableRow_Off\";\r\n");
          out.write("\t\r\n");
          out.write("\ttable.children[0].appendChild(newTR);\r\n");
          out.write("\t//table.children[0].appendChild(newTR1);\r\n");
          out.write("\tqtyText.focus();\r\n");
          out.write("   \t\r\n");
          out.write("    \r\n");
          out.write("    \r\n");
          out.write("    \r\n");
          out.write("\tif(document.getElementById('saveDataButton'))\r\n");
          out.write("\tdocument.getElementById('saveDataButton').tabIndex=23;\r\n");
          out.write("   \r\n");
          out.write("\t\r\n");
          out.write("\r\n");
          out.write(" }\r\n");
          out.write(" \r\n");
          out.write(" function removeLotInfo()\r\n");
          out.write(" {\r\n");
          out.write("\talert(\"HI\") \r\n");
          out.write("\tvar allchecks = document.getElementsByName('check');\r\n");
          out.write("\tvar remtable = document.getElementById('dynamicTable');\r\n");
          out.write("\t//alert(remtable.rows.length);\r\n");
          out.write("\t\r\n");
          out.write("\tvar checkSel = false;\r\n");
          out.write("    //alert(allchecks.length);\r\n");
          out.write("    var length = 0;\r\n");
          out.write("    for(k =0;k<allchecks.length;k++){\r\n");
          out.write("    \t if( allchecks[k].checked ){\r\n");
          out.write("    \t \tlength = length + 1;\r\n");
          out.write("    \t }\r\n");
          out.write("    \r\n");
          out.write("    }\r\n");
          out.write("    alert(\"Length = \"+length);\r\n");
          out.write("    for(i=0;true;){\r\n");
          out.write("   \t if ( length == 0 ){\r\n");
          out.write("    \tbreak;\r\n");
          out.write("    }\r\n");
          out.write("  \t \r\n");
          out.write("  \t //if ( i >= allchecks.length ){\r\n");
          out.write("  \t \t//break;\r\n");
          out.write("  \t// }\r\n");
          out.write("  \telse if( allchecks[i].checked ){\r\n");
          out.write("   \t\tcheckSel = true;\r\n");
          out.write("   \t\tlength = length -1;\r\n");
          out.write("   \t\tremtable.deleteRow(9+i)\r\n");
          out.write("   \t\ti=0;\r\n");
          out.write("    }\r\n");
          out.write("    else {\r\n");
          out.write("    \ti++;\r\n");
          out.write("    }\r\n");
          out.write("   \r\n");
          out.write(" }\r\n");
          out.write("  alert(\"Length = \"+length);\r\n");
          out.write(" \r\n");
          out.write("if( checkSel == false ){\r\n");
          out.write("  alert(\"Please Select The Message! \"); \r\n");
          out.write("  return false;\r\n");
          out.write(" }\r\n");
          out.write(" return true;\r\n");
          out.write("\t\r\n");
          out.write(" }\r\n");
          out.write("//-->\r\n");
          out.write("</script>\r\n");
          out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
          out.write("\r\n");
          out.write("function accessdenied2() {\r\n");
          out.write("var frm= document.forms[0];\r\n");
          out.write("\tdocument.forms[0].name.focus();\r\n");
          out.write("}\r\n");
          out.write("</script>\r\n");
          out.write("\r\n");
          out.write("<script>\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("function newWindow(  htmFile  ) {\r\n");
          out.write("   msgWindow = window.open( htmFile, \"WindowName\", \"left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0\");\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("function MM_openBrWindow(theURL,winName,features) { \r\n");
          out.write("alert\r\n");
          out.write("  window.open(theURL,winName,features);\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("function newWindow1( frm  ) {\r\n");
          out.write("\t\t   msgWindow = window.open( frm.submit(), \"WindowName\", \"left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0\");\r\n");
          out.write("\t  }\r\n");
          out.write("</script>\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("function viewAttachment(){\r\n");
          out.write(" \r\n");
          out.write("           \r\n");
          out.write("        \r\n");
          out.write("\r\n");
          out.write(" var docId=\"");
          out.print((String)session.getAttribute("DocId"));
          out.write("\";\r\n");
          out.write("\r\n");
          out.write("\tdocument.InitForm.target=\"framea\"\r\n");
          out.write("    document.InitForm.action = \"");
          out.print(request.getContextPath());
          out.write("/servlet/showImageFromTL1?\";\r\n");
          out.write("\tdocument.InitForm.submit();\t \r\n");
          out.write("     \r\n");
          out.write("\t\t\t\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("}\r\n");
          out.write("</script>\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("</head>\r\n");
          out.write("\r\n");
          out.write("<body >\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write(" ");
 System.out.println("Initial Pedigree Frame1");
 String receivedFax=(String)session.getAttribute("operationType");
 System.out.println("rvc"+receivedFax);
 
 
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          //  html:form
          org.apache.struts.taglib.html.FormTag _jspx_th_html_form_0 = (org.apache.struts.taglib.html.FormTag) _jspx_tagPool_html_form_enctype_action.get(org.apache.struts.taglib.html.FormTag.class);
          _jspx_th_html_form_0.setPageContext(_jspx_page_context);
          _jspx_th_html_form_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_html_form_0.setAction("dist/epedigree/InitialPedigree.do?pagenm=pedigree");
          _jspx_th_html_form_0.setEnctype("multipart/form-data");
          int _jspx_eval_html_form_0 = _jspx_th_html_form_0.doStartTag();
          if (_jspx_eval_html_form_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("\r\n");
              out.write("  ");
              if (_jspx_meth_html_hidden_0(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("  \r\n");
              out.write("    <TABLE cellSpacing=\"1\" cellPadding=\"3\" border=\"0\" width=\"100%\">\t\t\r\n");
              out.write("    <br><br>\r\n");
              out.write("\t\t\t\t\t<tr class=\"tableRow_Header\"> \r\n");
              out.write("                  <td class=\"title\" colspan=\"4\">Trading Partner </td>\r\n");
              out.write("                </tr>\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TD>*  Name:</TD>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write(" \r\n");
              out.write("\t\t\t\t\t<td>");
              //  html:select
              org.apache.struts.taglib.html.SelectTag _jspx_th_html_select_0 = (org.apache.struts.taglib.html.SelectTag) _jspx_tagPool_html_select_property_onchange.get(org.apache.struts.taglib.html.SelectTag.class);
              _jspx_th_html_select_0.setPageContext(_jspx_page_context);
              _jspx_th_html_select_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
              _jspx_th_html_select_0.setProperty("tpName");
              _jspx_th_html_select_0.setOnchange("tpDetails();");
              int _jspx_eval_html_select_0 = _jspx_th_html_select_0.doStartTag();
              if (_jspx_eval_html_select_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_html_select_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_html_select_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_html_select_0.doInitBody();
                }
                do {
                  if (_jspx_meth_html_option_0(_jspx_th_html_select_0, _jspx_page_context))
                    return;
                  out.write("\r\n");
                  out.write("\t\t\t\t\t");
 List tpNamesss = (List)session.getAttribute("tpNames");
	  		  	       System.out.println("tp names in jsp: "+tpNamesss);
				       for(int i=0;i<tpNamesss.size();i++){
				    
                  out.write("\r\n");
                  out.write("\t\t\t\t    ");
                  //  html:option
                  org.apache.struts.taglib.html.OptionTag _jspx_th_html_option_1 = (org.apache.struts.taglib.html.OptionTag) _jspx_tagPool_html_option_value.get(org.apache.struts.taglib.html.OptionTag.class);
                  _jspx_th_html_option_1.setPageContext(_jspx_page_context);
                  _jspx_th_html_option_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_select_0);
                  _jspx_th_html_option_1.setValue(tpNamesss.get(i).toString());
                  int _jspx_eval_html_option_1 = _jspx_th_html_option_1.doStartTag();
                  if (_jspx_eval_html_option_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_html_option_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_html_option_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_html_option_1.doInitBody();
                    }
                    do {
                      out.print(tpNamesss.get(i).toString());
                      int evalDoAfterBody = _jspx_th_html_option_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_html_option_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_html_option_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_html_option_value.reuse(_jspx_th_html_option_1);
                  out.write("\r\n");
                  out.write("\t\t\t\t\t");
 } 
                  out.write("\r\n");
                  out.write("\t\t\t\t\t");
                  int evalDoAfterBody = _jspx_th_html_select_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_html_select_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_html_select_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_html_select_property_onchange.reuse(_jspx_th_html_select_0);
              out.write("</td>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t     <TD>* Address1 :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_0(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t \r\n");
              out.write("\t\t\t\t     <TD> Address2 :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_1(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>* City :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD >\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_2(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD>* State:</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_3(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t  </TD>\r\n");
              out.write("\t\t\t\t\t\t  \r\n");
              out.write("\t\t\t\t\t\t  <TD>* Country :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_4(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t  </TD>\r\n");
              out.write("\t\t\t\t\t\t  \r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD>* Zip Code:</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_5(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=2>&nbsp;</td> \r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t \t\t\t\t\r\n");
              out.write("\t\t \t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t<TD>*  Contact Name :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_6(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t     <TD> Contact Title :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_7(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t<TD>*  Contact Phone :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_8(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD>\r\n");
              out.write("\t\t\t\t     <TD> Contact E-Mail :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_9(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t</TR>\t\r\n");
              out.write("\t\t \t\t\t\r\n");
              out.write("\t\t \t\t\t\r\n");
              out.write("\t\t\t\t</TABLE>\r\n");
              out.write("  \r\n");
              out.write("  \r\n");
              out.write("  <br>\r\n");
              out.write("  <TABLE cellSpacing=\"1\" cellPadding=\"3\" border=\"0\" width=\"100%\">\t\t\r\n");
              out.write("\t\t\t\t\t<tr class=\"tableRow_Header\"> \r\n");
              out.write("                    <td class=\"title\" colspan=\"4\">Transaction information </td>\r\n");
              out.write("                \t</tr>\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t<TD>* Document Type :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>");
              if (_jspx_meth_html_select_1(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD>\r\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t  <TD >* Document Number :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_10(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t  </TD>\r\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t  <TD>* Document Date :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_11(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("<img src=\"");
              if (_jspx_meth_html_rewrite_0(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\" width=\"16\" height=\"16\" onclick=\"return showCalendar('transactionDate', '%Y-%m-%d', '24', true);\">\r\n");
              out.write("\t\t\t\t\t  </TD>\r\n");
              out.write("\t\t\t\t\t  <TD colspan=2>&nbsp;</td>  \r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t</TABLE>\r\n");
              out.write("  \r\n");
              out.write("  <br>\r\n");
              out.write("  <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" >\r\n");
              out.write("       \r\n");
              out.write("            <!-- info goes here -->\r\n");
              out.write("              <table width=\"100%\" cellSpacing=\"0\" cellPadding=\"0\" align=\"left\" border=\"0\"\r\n");
              out.write("\t\t\tbgcolor=\"white\">\r\n");
              out.write("              \r\n");
              out.write("                <tr> \r\n");
              out.write("                  <td align=\"left\">\r\n");
              out.write("\t\t\t\t\t<TABLE cellSpacing=\"1\" cellPadding=\"3\" border=\"0\" width=\"100%\" id='dynamicTable' >\t\t\r\n");
              out.write("\t\t\t\t\t<tr class=\"tableRow_Header\"> \r\n");
              out.write("                  \t<td class=\"title\" colspan=\"5\">Pedigree Details</td>\r\n");
              out.write("                \t</tr>\t\r\n");
              out.write("                \r\n");
              out.write("                \r\n");
              out.write("                \t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD>* Product Code(NDC) :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_12(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD>\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t<input type=\"button\" name=\"lndc\" value=\"Lookup NDC\" tabindex=\"2\" onclick=\"lookupNDC();\"/>\r\n");
              out.write("\t\t\t\t\t\t</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=2>&nbsp;</td> \r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("                \r\n");
              out.write("                \t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t<TD>* Drug Name:</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD>");
              if (_jspx_meth_html_text_13(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("</TD> \r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD>* Shipped Quantity :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=\"2\">");
              if (_jspx_meth_html_text_14(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</td>\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TD>* Strength :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD >\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_15(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t\t <TD>* Lot Number:</td>\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=\"2\">");
              if (_jspx_meth_html_text_16(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\t\t\r\n");
              out.write("\t\t\t\t\t\t </TD>\r\n");
              out.write("\t\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t <TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD>* Dosage Form :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_17(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t  </TD>\r\n");
              out.write("\t\t\t\t\t\t  \r\n");
              out.write("\t\t\t\t\t\t <!--<TD> Expiration date(MM/YY) :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>");
              if (_jspx_meth_html_text_18(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD>-->\r\n");
              out.write("\t\t\t\t\t\t <TD colspan=1>&nbsp;</TD>\t\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=2>&nbsp;</TD>\t\r\n");
              out.write("\t\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t  <TD>* Container Size:</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_19(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD>\r\n");
              out.write("\t\t\t\t\t\t <TD>* Manufacturer :</TD>\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=\"2\">\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_html_text_20(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD> \r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t \t\t\t  \r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t");

						String lotNos[]=(String[])session.getAttribute("lotNos");
		    			String quantity[]=(String[])session.getAttribute("quantity");
		    			String expirationDate[]=(String[])session.getAttribute("expirationDate");
						for(int k=1 ;lotNos!=null && k<lotNos.length ;k++){
					
					
              out.write("\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\"  >\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t\t<TD>* Shipped Quantity :</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              //  html:text
              org.apache.struts.taglib.html.TextTag _jspx_th_html_text_21 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_value_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
              _jspx_th_html_text_21.setPageContext(_jspx_page_context);
              _jspx_th_html_text_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
              _jspx_th_html_text_21.setProperty("quantity");
              _jspx_th_html_text_21.setValue(quantity[k]);
              int _jspx_eval_html_text_21 = _jspx_th_html_text_21.doStartTag();
              if (_jspx_th_html_text_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_html_text_value_property_nobody.reuse(_jspx_th_html_text_21);
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD>\r\n");
              out.write("\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t \t<TD>* Lot Number:</td>\r\n");
              out.write("\t\t\t\t\t\t<TD>\r\n");
              out.write("\t\t\t\t\t\t");
              //  html:text
              org.apache.struts.taglib.html.TextTag _jspx_th_html_text_22 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_value_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
              _jspx_th_html_text_22.setPageContext(_jspx_page_context);
              _jspx_th_html_text_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
              _jspx_th_html_text_22.setProperty("lotNumber");
              _jspx_th_html_text_22.setValue(lotNos[k]);
              int _jspx_eval_html_text_22 = _jspx_th_html_text_22.doStartTag();
              if (_jspx_th_html_text_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_html_text_value_property_nobody.reuse(_jspx_th_html_text_22);
              out.write("\r\n");
              out.write("\t\t\t\t\t\t</TD>\r\n");
              out.write("\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t </TR>\r\n");
              out.write("\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t");

						}
					
              out.write("  \r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t  ");
              out.write("\t\r\n");
              out.write("\t\t\r\n");
              out.write("\t\t");
              out.write("\t\t\r\n");
              out.write("\t\t\t\t \t\t\r\n");
              out.write("\t\t ");
String str=(String)session.getAttribute("test");
		 if(str==null || str.equals("") || str.equals("null")){
              out.write("\r\n");
              out.write("\t\t  \t <TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t  \t\t<TD colspan=6 >\r\n");
              out.write("\t\t\t \t\t  Paper Pedigree Attachment &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
              out.write("\t\t\t  \t\t  ");
              if (_jspx_meth_html_file_0(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("  (Only PDF Attachments accepted )\r\n");
              out.write("\t\t\t \t\t</TD>\r\n");
              out.write("\t\t\t \t\t\r\n");
              out.write("\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t\t \t\t\r\n");
              out.write("\t\t\t\t");
}else{
              out.write("\r\n");
              out.write("\t\t\t\t\r\n");
              out.write("\t\t\t\t <TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t  \t\t<TD colspan=3 >\r\n");
              out.write("\t\t\t \t\t  Paper Pedigree Attachment &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
              out.write("\t\t\t  \t\t  ");
              if (_jspx_meth_html_file_1(_jspx_th_html_form_0, _jspx_page_context))
                return;
              out.write("  (Only PDF Attachments accepted )\r\n");
              out.write("\t\t\t \t\t</TD>\r\n");
              out.write("\t\t\t \t\t<TD colspan=3 >\r\n");
              out.write("\t\t\t \t\t  <input type=\"button\" name=\"View Attachment\" value=\"View Attachment\" onClick=\"viewAttachment();\"/> \r\n");
              out.write("\t\t\t \t\t</TD>\r\n");
              out.write("\t\t\t\t</TR>");
}
              out.write("\r\n");
              out.write("\t\t   \r\n");
              out.write("\t\t     \r\n");
              out.write("\t  ");
              out.write("\t\t\t\t\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=2>* fields are mandatory</td>\r\n");
              out.write("\t\t\t\t\t\t<TD colspan=4>&nbsp;</TD>\r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t\t\t<TR class=\"tableRow_Off\">\r\n");
              out.write("\t\t\t\t \t\t\t<TD ><input type=\"button\" name=\"Add LotInfo\" value=\"AddLotInfo\" onClick=\"addLotInfo();\" tabindex=\"12\"   ></TD>\r\n");
              out.write("\t\t\t\t\t\t  \t<TD ><input type=\"button\" name=\"Remove LotInfo\" value=\"RemoveLotInfo\" onClick=\"removeLotInfo();\" ></TD>\r\n");
              out.write("\t\t\t\t\t\t   <TD colspan=4>&nbsp;</TD>\t\t \r\n");
              out.write("\t\t\t\t\t</TR>\r\n");
              out.write("\t\t\t\t\t\r\n");
              out.write("\t\t\t</TABLE>\r\n");
              out.write(" \r\n");
              out.write("\t \r\n");
              out.write("\t\t\t\t<Center>  \r\n");
              out.write("           \r\n");
              out.write("          \r\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");
              //  html:hidden
              org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_1 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_value_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
              _jspx_th_html_hidden_1.setPageContext(_jspx_page_context);
              _jspx_th_html_hidden_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
              _jspx_th_html_hidden_1.setProperty("pagenm");
              _jspx_th_html_hidden_1.setValue(pagenm);
              int _jspx_eval_html_hidden_1 = _jspx_th_html_hidden_1.doStartTag();
              if (_jspx_th_html_hidden_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_html_hidden_value_property_nobody.reuse(_jspx_th_html_hidden_1);
              out.write("\r\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");
              //  html:hidden
              org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_2 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_value_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
              _jspx_th_html_hidden_2.setPageContext(_jspx_page_context);
              _jspx_th_html_hidden_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
              _jspx_th_html_hidden_2.setProperty("tp_company_nm");
              _jspx_th_html_hidden_2.setValue(tp_company_nm);
              int _jspx_eval_html_hidden_2 = _jspx_th_html_hidden_2.doStartTag();
              if (_jspx_th_html_hidden_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_html_hidden_value_property_nobody.reuse(_jspx_th_html_hidden_2);
              out.write("\r\n");
              out.write("           \r\n");
              out.write("           ");
              //  logic:notPresent
              org.apache.struts.taglib.logic.NotPresentTag _jspx_th_logic_notPresent_1 = (org.apache.struts.taglib.logic.NotPresentTag) _jspx_tagPool_logic_notPresent_property_name.get(org.apache.struts.taglib.logic.NotPresentTag.class);
              _jspx_th_logic_notPresent_1.setPageContext(_jspx_page_context);
              _jspx_th_logic_notPresent_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
              _jspx_th_logic_notPresent_1.setProperty("documentID");
              _jspx_th_logic_notPresent_1.setName("InitialPedigreeForm");
              int _jspx_eval_logic_notPresent_1 = _jspx_th_logic_notPresent_1.doStartTag();
              if (_jspx_eval_logic_notPresent_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("            ");
                  if (_jspx_meth_html_hidden_3(_jspx_th_logic_notPresent_1, _jspx_page_context))
                    return;
                  out.write("\r\n");
                  out.write("                        <input type=\"hidden\" name=\"receivedFax\" value=\"");
                  out.print(receivedFax);
                  out.write("\" />\r\n");
                  out.write("           <input type=\"submit\" name=\"save\" tabindex=\"13\"  value=\"Save Data\" target=\"frame2\" onclick=\"return validate_form(this);\" id=\"saveDataButton\" />\r\n");
                  out.write("           ");
                  int evalDoAfterBody = _jspx_th_logic_notPresent_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_logic_notPresent_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_logic_notPresent_property_name.reuse(_jspx_th_logic_notPresent_1);
              out.write("\r\n");
              out.write("           \r\n");
              out.write("           ");

           
          String docId=(String)session.getAttribute("DocId");
		  System.out.println("Frame1"+docId);
		  
          
              out.write("\r\n");
              out.write("           \r\n");
              out.write("           ");
              //  logic:present
              org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_1 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_property_name.get(org.apache.struts.taglib.logic.PresentTag.class);
              _jspx_th_logic_present_1.setPageContext(_jspx_page_context);
              _jspx_th_logic_present_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
              _jspx_th_logic_present_1.setProperty("documentID");
              _jspx_th_logic_present_1.setName("InitialPedigreeForm");
              int _jspx_eval_logic_present_1 = _jspx_th_logic_present_1.doStartTag();
              if (_jspx_eval_logic_present_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("              <input type=\"hidden\" name=\"receivedFax\" value=\"");
                  out.print(receivedFax);
                  out.write("\" />\r\n");
                  out.write("            ");
                  if (_jspx_meth_html_hidden_4(_jspx_th_logic_present_1, _jspx_page_context))
                    return;
                  out.write("\r\n");
                  out.write("          <input type=\"submit\"  name=\"save\" tabindex=\"23\" target=\"frame2\"  value=\"Update Data\" onclick=\"return validate_form(this);\" id=\"UpdateDataButton\"/>\r\n");
                  out.write("           ");
                  int evalDoAfterBody = _jspx_th_logic_present_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_logic_present_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_logic_present_property_name.reuse(_jspx_th_logic_present_1);
              out.write("\r\n");
              out.write("          \r\n");
              out.write("           \r\n");
              out.write("           </Center>\r\n");
              out.write("             \r\n");
              out.write("            \r\n");
              out.write("         </td>\r\n");
              out.write("        </tr>\r\n");
              out.write("      </table> \r\n");
              out.write("      </td>\r\n");
              out.write("        </tr>\r\n");
              out.write("       \r\n");
              out.write("      </table> \r\n");
              out.write("     \r\n");
              out.write("<html>\r\n");
              out.write("<head><title></title>\r\n");
              out.write("\r\n");
              out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
              out.write("\r\n");
              out.write("function accessdenied1() {\r\n");
              out.write("  var status = \"");
              out.print(request.getAttribute("Pedenv"));
              out.write("\";\r\n");
              out.write("  var refno = \"");
              out.print(request.getParameter("selectedRow"));
              out.write("\";\r\n");
              out.write("  var attachStatus = \"");
              out.print(request.getAttribute("AttachStatus"));
              out.write("\"\r\n");
              out.write("  var exists = \"");
              out.print(request.getAttribute("exists"));
              out.write("\";\r\n");
              out.write("  var pedId = \"PedigreeId\" + \"=\" + \"");
              out.print(request.getAttribute("PedigreeId"));
              out.write("\";\r\n");
              out.write("  var emailStatus = \"");
              out.print(request.getAttribute("emailStatus"));
              out.write("\";\r\n");
              out.write("  var lookup = \"");
              out.print( session.getAttribute("Lookup") );
              out.write("\";\r\n");
              out.write("\r\n");
              out.write("if(lookup == \"false\") { \r\n");
              out.write("alert(\"NDC not found\") \r\n");
 session.removeAttribute("Lookup"); 
              out.write("\r\n");
              out.write("}else if (lookup == \"true\"){\r\n");
              out.write("\taccessdenied2();\r\n");
              out.write("\t");
session.removeAttribute("Lookup");
              out.write("\r\n");
              out.write("}\r\n");
              out.write("\r\n");
              out.write("\r\n");
              out.write("if(attachStatus == \"true\") alert(\"Attachment Uploaded Successfully!!!\")\r\n");
              out.write("\r\n");
              out.write("if(emailStatus == \"Success\") alert(\"Email has been sent successfully\")\r\n");
              out.write("\r\n");
              out.write("if(emailStatus == \"failure\") alert(\"Email Sending Failure...!\")\r\n");
              out.write("\r\n");
              out.write("\r\n");
              out.write("if(status == \"true\"){\r\n");
              out.write("  var res = confirm(\"Pedigree Already Exists !!! Do you want to Recreate Pedigree?\")\r\n");
              out.write("  if(res) {\r\n");
              out.write("  \t\tdocument.createAPN.y.value=\"CreatePedigree\";\r\n");
              out.write("  \t\tdocument.createAPN.res.value=\"true\";\r\n");
              out.write("  \t\tdocument.createAPN.action = \"/ePharma/dist/epedigree/ShippingManagerSearchEmpty.do?Submit3=Search\"\r\n");
              out.write("  \t\tdocument.createAPN.submit();\r\n");
              out.write("  }\r\n");
              out.write("  else return false;\r\n");
              out.write("  //setTimeout(\"MsgBox 'Pedigree Already Exists !!! Do you want to Recreate Pedigree?',4 \",0,\"vbscript\")\r\n");
              out.write("}\r\n");
              out.write("\r\n");
              out.write("if(exists == \"true\"){\r\n");
              out.write("  var exists = confirm(\"Attachment Already Exists !!! Do you want to replace the Attachment?\")\r\n");
              out.write("  if(exists) {\r\n");
              out.write("  \r\n");
              out.write("  \t\tdocument.attachmentForm.reply.value=\"true\";\r\n");
              out.write("  \t\tdocument.attachmentForm.action = \"/ePharma/dist/epedigree/Shipping_Attachment.do?\"+pedId+\"&tp_company_nm=&pagenm=pedigree&linkname=Attachment\"\r\n");
              out.write("  \t\tdocument.attachmentForm.submit();\r\n");
              out.write("  }\r\n");
              out.write("  else return false;\r\n");
              out.write("  //setTimeout(\"MsgBox 'Pedigree Already Exists !!! Do you want to Recreate Pedigree?',4 \",0,\"vbscript\")\r\n");
              out.write("}\r\n");
              out.write("\r\n");
              out.write("\r\n");
              out.write("var i = \"");
              out.print((String)request.getAttribute("APNSigStatus"));
              out.write("\"\r\n");
              out.write("if(i == \"SignExists\") { alert(\"Signature Already Exists !!\"); }\r\n");
              out.write("if(i == \"SignCreated\"){alert(\"Signature Created !!\");}\r\n");
              out.write("if(i == \"SignNotCreated\"){alert(\"Signature Not Created !!\");}\r\n");
              out.write("\r\n");
              out.write("var j = \"");
              out.print((String)request.getAttribute("AuthenticateStatusInDetail"));
              out.write("\"\r\n");
              out.write("if(j == \"AlreadyAuthenticated\") { alert(\"Pedigree Already Authenticated !!\"); }\r\n");
              out.write("if(j == \"AuthenticatedSuccessfully\"){alert(\"Pedigree Authenticated Successfully !!\");}\r\n");
              out.write("\r\n");
              out.write("}\r\n");
              out.write("</script>\r\n");
              out.write("\r\n");
              out.write("</head>\r\n");
              out.write("\r\n");
              out.write("<body onLoad=\"return accessdenied1();\">\r\n");
 System.out.println("*********Inside onLoad.jsp********"); 
              out.write("\r\n");
              out.write("</body>\r\n");
              out.write("</html>");
              out.write(" \t\r\n");
              int evalDoAfterBody = _jspx_th_html_form_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_html_form_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_html_form_enctype_action.reuse(_jspx_th_html_form_0);
          out.write("\r\n");
          out.write("\r\n");
          out.write("  \r\n");
          out.write(" <script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
          out.write(" window.refresh;\r\n");
          out.write("\r\n");
          out.write("</script>\r\n");
          out.write("\r\n");
          out.write("</body>\r\n");
          out.write("\r\n");
          out.write("\r\n");
          int evalDoAfterBody = _jspx_th_html_html_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_html_html_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_html_html.reuse(_jspx_th_html_html_0);
      out.write("\r\n");
      out.write("\r\n");

if(isUpdate.equals("true"))
{
      out.write("\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
      out.write("<!--\r\n");
      out.write(" window.onload = function() {\r\n");
      out.write("\t  window.setTimeout( \"alert('Updated Successfully')\",10);\r\n");
      out.write("\t  \r\n");
      out.write("\t }\r\n");
      out.write("\t \r\n");
      out.write("//-->\r\n");
      out.write("</script>\r\n");

request.removeAttribute("updateSuccess");
}
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

String str = (String)session.getAttribute("test");
System.out.println("tesssssssssst\n\n\n\n"+str);
if(str==null)str="";
if(str.equals("process")){
      out.write("\r\n");
      out.write("<form name=\"InitForm\" >\r\n");
      out.write("<input type=\"hidden\" name=\"dbname\" value=\"ePharma\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"collname\" value=\"PaperPedigree\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"mimetype\" value=\"application/pdf\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"nodename\" value=\"data\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"topnode\" value=\"initialPedigree\"/>\r\n");
} 


if(str==null || str.equals("") || str.equals("null")){

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form name=\"InitForm\" >\r\n");
      out.write("<input type=\"hidden\" name=\"dbname\" value=\"ePharma\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"collname\" value=\"PaperPedigree\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"mimetype\" value=\"application/pdf\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"nodename\" value=\"data\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"topnode\" value=\"initialPedigree\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"DocumentInfo/serialNumber\" value=\"");
      out.print((String)session.getAttribute("DocId"));
      out.write("\"/>\r\n");
      out.write("\r\n");
      out.write("\r\n");
}  if(str.equals("Update")){

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<form name=\"InitForm\" >\r\n");
      out.write("<input type=\"hidden\" name=\"dbname\" value=\"ePharma\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"collname\" value=\"PaperPedigree\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"mimetype\" value=\"application/pdf\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"nodename\" value=\"data\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"topnode\" value=\"initialPedigree\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"DocumentInfo/serialNumber\" value=\"");
      out.print((String)session.getAttribute("DocId"));
      out.write("\"/>\r\n");
      out.write("\r\n");
      out.write("\r\n");
} 

 if(str.equals("received")){
      out.write("\r\n");
      out.write("<form name=\"InitForm\" >\r\n");
      out.write("<input type=\"hidden\" name=\"dbname\" value=\"ePharma\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"collname\" value=\"ReceivedFax\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"mimetype\" value=\"application/pdf\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"nodename\" value=\"FileContents\"/>\r\n");
      out.write("<input type=\"hidden\" name=\"topnode\" value=\"InboundPostRequest\"/>\r\n");
      out.write("\r\n");
} 




 
 
String docId=(String)session.getAttribute("DocId");
System.out.println("ddddddddd"+docId);
if(docId==null)docId="";
else{
 

 if(str.equals("process")){
      out.write("\r\n");
      out.write("<input type=\"hidden\" name=\"DocumentInfo/serialNumber\" value=\"");
      out.print(docId);
      out.write("\"/>\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
      out.write("    \t \r\n");
      out.write("\t\t document.InitForm.target = \"framea\";\r\n");
      out.write("    \t document.InitForm.action = \"");
      out.print(request.getContextPath());
      out.write("/servlet/showImageFromTL1?\";\r\n");
      out.write("\t\t document.InitForm.submit();\r\n");
      out.write("     \r\n");
      out.write("       \r\n");
      out.write("</script>\r\n");
}if(str.equals("received")){
      out.write("\r\n");
      out.write("<input type=\"hidden\" name=\"InitialPedigreeId\" value=\"");
      out.print(docId);
      out.write("\"/>\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
      out.write(" \r\n");
      out.write("\t\t document.InitForm.target = \"framea\";\r\n");
      out.write("    \t document.InitForm.action = \"");
      out.print(request.getContextPath());
      out.write("/servlet/showImageFromTL1?\";\r\n");
      out.write("\t\t document.InitForm.submit();\r\n");
      out.write("     \r\n");
      out.write("       \r\n");
      out.write("</script>\r\n");
}if(str.equals("Update")){
      out.write("\r\n");
      out.write("<input type=\"hidden\" name=\"DocumentInfo/serialNumber\" value=\"");
      out.print(docId);
      out.write("\"/>\r\n");
}
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
session.removeAttribute("test");}
      out.write("\r\n");
      out.write("</form> \r\n");
      out.write("\r\n");
      out.write(" ");
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

  private boolean _jspx_meth_html_hidden_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_0 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
    _jspx_th_html_hidden_0.setPageContext(_jspx_page_context);
    _jspx_th_html_hidden_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_hidden_0.setProperty("documentID");
    int _jspx_eval_html_hidden_0 = _jspx_th_html_hidden_0.doStartTag();
    if (_jspx_th_html_hidden_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_hidden_property_nobody.reuse(_jspx_th_html_hidden_0);
    return false;
  }

  private boolean _jspx_meth_html_option_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_select_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:option
    org.apache.struts.taglib.html.OptionTag _jspx_th_html_option_0 = (org.apache.struts.taglib.html.OptionTag) _jspx_tagPool_html_option_value.get(org.apache.struts.taglib.html.OptionTag.class);
    _jspx_th_html_option_0.setPageContext(_jspx_page_context);
    _jspx_th_html_option_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_select_0);
    _jspx_th_html_option_0.setValue("");
    int _jspx_eval_html_option_0 = _jspx_th_html_option_0.doStartTag();
    if (_jspx_eval_html_option_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_option_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_option_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_option_0.doInitBody();
      }
      do {
        out.write("Select");
        int evalDoAfterBody = _jspx_th_html_option_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_option_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_option_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_option_value.reuse(_jspx_th_html_option_0);
    return false;
  }

  private boolean _jspx_meth_html_text_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_0 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_0.setPageContext(_jspx_page_context);
    _jspx_th_html_text_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_0.setProperty("address1");
    int _jspx_eval_html_text_0 = _jspx_th_html_text_0.doStartTag();
    if (_jspx_th_html_text_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_0);
    return false;
  }

  private boolean _jspx_meth_html_text_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_1 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_1.setPageContext(_jspx_page_context);
    _jspx_th_html_text_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_1.setProperty("address2");
    int _jspx_eval_html_text_1 = _jspx_th_html_text_1.doStartTag();
    if (_jspx_th_html_text_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_1);
    return false;
  }

  private boolean _jspx_meth_html_text_2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_2 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_2.setPageContext(_jspx_page_context);
    _jspx_th_html_text_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_2.setProperty("city");
    int _jspx_eval_html_text_2 = _jspx_th_html_text_2.doStartTag();
    if (_jspx_th_html_text_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_2);
    return false;
  }

  private boolean _jspx_meth_html_text_3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_3 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_3.setPageContext(_jspx_page_context);
    _jspx_th_html_text_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_3.setProperty("state");
    int _jspx_eval_html_text_3 = _jspx_th_html_text_3.doStartTag();
    if (_jspx_th_html_text_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_3);
    return false;
  }

  private boolean _jspx_meth_html_text_4(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_4 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_4.setPageContext(_jspx_page_context);
    _jspx_th_html_text_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_4.setProperty("country");
    int _jspx_eval_html_text_4 = _jspx_th_html_text_4.doStartTag();
    if (_jspx_th_html_text_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_4);
    return false;
  }

  private boolean _jspx_meth_html_text_5(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_5 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_5.setPageContext(_jspx_page_context);
    _jspx_th_html_text_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_5.setProperty("zipCode");
    int _jspx_eval_html_text_5 = _jspx_th_html_text_5.doStartTag();
    if (_jspx_th_html_text_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_5);
    return false;
  }

  private boolean _jspx_meth_html_text_6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_6 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_6.setPageContext(_jspx_page_context);
    _jspx_th_html_text_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_6.setProperty("contactName");
    int _jspx_eval_html_text_6 = _jspx_th_html_text_6.doStartTag();
    if (_jspx_th_html_text_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_6);
    return false;
  }

  private boolean _jspx_meth_html_text_7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_7 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_7.setPageContext(_jspx_page_context);
    _jspx_th_html_text_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_7.setProperty("contactTitle");
    int _jspx_eval_html_text_7 = _jspx_th_html_text_7.doStartTag();
    if (_jspx_th_html_text_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_7);
    return false;
  }

  private boolean _jspx_meth_html_text_8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_8 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_8.setPageContext(_jspx_page_context);
    _jspx_th_html_text_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_8.setProperty("contactPhone");
    int _jspx_eval_html_text_8 = _jspx_th_html_text_8.doStartTag();
    if (_jspx_th_html_text_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_8);
    return false;
  }

  private boolean _jspx_meth_html_text_9(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_9 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_9.setPageContext(_jspx_page_context);
    _jspx_th_html_text_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_9.setProperty("contactEmail");
    int _jspx_eval_html_text_9 = _jspx_th_html_text_9.doStartTag();
    if (_jspx_th_html_text_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_9);
    return false;
  }

  private boolean _jspx_meth_html_select_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:select
    org.apache.struts.taglib.html.SelectTag _jspx_th_html_select_1 = (org.apache.struts.taglib.html.SelectTag) _jspx_tagPool_html_select_value_property.get(org.apache.struts.taglib.html.SelectTag.class);
    _jspx_th_html_select_1.setPageContext(_jspx_page_context);
    _jspx_th_html_select_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_select_1.setProperty("transactionType");
    _jspx_th_html_select_1.setValue("<bean:write property='transactionType' name='InitialPedigreeForm' />");
    int _jspx_eval_html_select_1 = _jspx_th_html_select_1.doStartTag();
    if (_jspx_eval_html_select_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_select_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_select_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_select_1.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t ");
        if (_jspx_meth_logic_notPresent_0(_jspx_th_html_select_1, _jspx_page_context))
          return true;
        out.write(" \r\n");
        out.write("\t\t\t\t\t\t\t ");
        if (_jspx_meth_logic_present_0(_jspx_th_html_select_1, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_html_select_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_select_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_html_select_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_select_value_property.reuse(_jspx_th_html_select_1);
    return false;
  }

  private boolean _jspx_meth_logic_notPresent_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_select_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:notPresent
    org.apache.struts.taglib.logic.NotPresentTag _jspx_th_logic_notPresent_0 = (org.apache.struts.taglib.logic.NotPresentTag) _jspx_tagPool_logic_notPresent_property_name.get(org.apache.struts.taglib.logic.NotPresentTag.class);
    _jspx_th_logic_notPresent_0.setPageContext(_jspx_page_context);
    _jspx_th_logic_notPresent_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_select_1);
    _jspx_th_logic_notPresent_0.setProperty("transactionType");
    _jspx_th_logic_notPresent_0.setName("InitialPedigreeForm");
    int _jspx_eval_logic_notPresent_0 = _jspx_th_logic_notPresent_0.doStartTag();
    if (_jspx_eval_logic_notPresent_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t <option >Invoice</option>\r\n");
        out.write("\t\t\t\t\t\t\t <option >PurchaseOrder</option>\r\n");
        out.write("\t\t\t\t\t\t\t ");
        int evalDoAfterBody = _jspx_th_logic_notPresent_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_notPresent_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_logic_notPresent_property_name.reuse(_jspx_th_logic_notPresent_0);
    return false;
  }

  private boolean _jspx_meth_logic_present_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_select_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:present
    org.apache.struts.taglib.logic.PresentTag _jspx_th_logic_present_0 = (org.apache.struts.taglib.logic.PresentTag) _jspx_tagPool_logic_present_property_name.get(org.apache.struts.taglib.logic.PresentTag.class);
    _jspx_th_logic_present_0.setPageContext(_jspx_page_context);
    _jspx_th_logic_present_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_select_1);
    _jspx_th_logic_present_0.setProperty("transactionType");
    _jspx_th_logic_present_0.setName("InitialPedigreeForm");
    int _jspx_eval_logic_present_0 = _jspx_th_logic_present_0.doStartTag();
    if (_jspx_eval_logic_present_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t <option>");
        if (_jspx_meth_bean_write_0(_jspx_th_logic_present_0, _jspx_page_context))
          return true;
        out.write("</option>\r\n");
        out.write("\t\t\t\t\t\t\t ");
        if (_jspx_meth_logic_notEqual_0(_jspx_th_logic_present_0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t ");
        if (_jspx_meth_logic_notEqual_1(_jspx_th_logic_present_0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t ");
        int evalDoAfterBody = _jspx_th_logic_present_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_present_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_logic_present_property_name.reuse(_jspx_th_logic_present_0);
    return false;
  }

  private boolean _jspx_meth_bean_write_0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_0 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_0.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_0);
    _jspx_th_bean_write_0.setProperty("transactionType");
    _jspx_th_bean_write_0.setName("InitialPedigreeForm");
    int _jspx_eval_bean_write_0 = _jspx_th_bean_write_0.doStartTag();
    if (_jspx_th_bean_write_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_0);
    return false;
  }

  private boolean _jspx_meth_logic_notEqual_0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:notEqual
    org.apache.struts.taglib.logic.NotEqualTag _jspx_th_logic_notEqual_0 = (org.apache.struts.taglib.logic.NotEqualTag) _jspx_tagPool_logic_notEqual_value_property_name.get(org.apache.struts.taglib.logic.NotEqualTag.class);
    _jspx_th_logic_notEqual_0.setPageContext(_jspx_page_context);
    _jspx_th_logic_notEqual_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_0);
    _jspx_th_logic_notEqual_0.setProperty("transactionType");
    _jspx_th_logic_notEqual_0.setValue("Invoice");
    _jspx_th_logic_notEqual_0.setName("InitialPedigreeForm");
    int _jspx_eval_logic_notEqual_0 = _jspx_th_logic_notEqual_0.doStartTag();
    if (_jspx_eval_logic_notEqual_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t <option >Invoice</option>\r\n");
        out.write("\t\t\t\t\t\t\t ");
        int evalDoAfterBody = _jspx_th_logic_notEqual_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_notEqual_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_logic_notEqual_value_property_name.reuse(_jspx_th_logic_notEqual_0);
    return false;
  }

  private boolean _jspx_meth_logic_notEqual_1(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:notEqual
    org.apache.struts.taglib.logic.NotEqualTag _jspx_th_logic_notEqual_1 = (org.apache.struts.taglib.logic.NotEqualTag) _jspx_tagPool_logic_notEqual_value_property_name.get(org.apache.struts.taglib.logic.NotEqualTag.class);
    _jspx_th_logic_notEqual_1.setPageContext(_jspx_page_context);
    _jspx_th_logic_notEqual_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_0);
    _jspx_th_logic_notEqual_1.setProperty("transactionType");
    _jspx_th_logic_notEqual_1.setValue("PurchaseOrder");
    _jspx_th_logic_notEqual_1.setName("InitialPedigreeForm");
    int _jspx_eval_logic_notEqual_1 = _jspx_th_logic_notEqual_1.doStartTag();
    if (_jspx_eval_logic_notEqual_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t <option  >PurchaseOrder</option>\r\n");
        out.write("\t\t\t\t\t\t\t ");
        int evalDoAfterBody = _jspx_th_logic_notEqual_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_notEqual_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_logic_notEqual_value_property_name.reuse(_jspx_th_logic_notEqual_1);
    return false;
  }

  private boolean _jspx_meth_html_text_10(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_10 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_10.setPageContext(_jspx_page_context);
    _jspx_th_html_text_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_10.setProperty("transactionId");
    int _jspx_eval_html_text_10 = _jspx_th_html_text_10.doStartTag();
    if (_jspx_th_html_text_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_property_nobody.reuse(_jspx_th_html_text_10);
    return false;
  }

  private boolean _jspx_meth_html_text_11(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_11 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_size_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_11.setPageContext(_jspx_page_context);
    _jspx_th_html_text_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_11.setProperty("transactionDate");
    _jspx_th_html_text_11.setSize("20");
    int _jspx_eval_html_text_11 = _jspx_th_html_text_11.doStartTag();
    if (_jspx_th_html_text_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_size_property_nobody.reuse(_jspx_th_html_text_11);
    return false;
  }

  private boolean _jspx_meth_html_rewrite_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:rewrite
    org.apache.struts.taglib.html.RewriteTag _jspx_th_html_rewrite_0 = (org.apache.struts.taglib.html.RewriteTag) _jspx_tagPool_html_rewrite_page_nobody.get(org.apache.struts.taglib.html.RewriteTag.class);
    _jspx_th_html_rewrite_0.setPageContext(_jspx_page_context);
    _jspx_th_html_rewrite_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_rewrite_0.setPage("/IMG/img.gif");
    int _jspx_eval_html_rewrite_0 = _jspx_th_html_rewrite_0.doStartTag();
    if (_jspx_th_html_rewrite_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_rewrite_page_nobody.reuse(_jspx_th_html_rewrite_0);
    return false;
  }

  private boolean _jspx_meth_html_text_12(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_12 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_12.setPageContext(_jspx_page_context);
    _jspx_th_html_text_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_12.setProperty("productCode");
    _jspx_th_html_text_12.setTabindex("1");
    int _jspx_eval_html_text_12 = _jspx_th_html_text_12.doStartTag();
    if (_jspx_th_html_text_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_12);
    return false;
  }

  private boolean _jspx_meth_html_text_13(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_13 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_13.setPageContext(_jspx_page_context);
    _jspx_th_html_text_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_13.setProperty("name");
    _jspx_th_html_text_13.setTabindex("3");
    int _jspx_eval_html_text_13 = _jspx_th_html_text_13.doStartTag();
    if (_jspx_th_html_text_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_13);
    return false;
  }

  private boolean _jspx_meth_html_text_14(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_14 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_14.setPageContext(_jspx_page_context);
    _jspx_th_html_text_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_14.setProperty("quantity");
    _jspx_th_html_text_14.setTabindex("8");
    int _jspx_eval_html_text_14 = _jspx_th_html_text_14.doStartTag();
    if (_jspx_th_html_text_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_14);
    return false;
  }

  private boolean _jspx_meth_html_text_15(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_15 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_15.setPageContext(_jspx_page_context);
    _jspx_th_html_text_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_15.setProperty("strength");
    _jspx_th_html_text_15.setTabindex("4");
    int _jspx_eval_html_text_15 = _jspx_th_html_text_15.doStartTag();
    if (_jspx_th_html_text_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_15);
    return false;
  }

  private boolean _jspx_meth_html_text_16(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_16 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_16.setPageContext(_jspx_page_context);
    _jspx_th_html_text_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_16.setProperty("lotNumber");
    _jspx_th_html_text_16.setTabindex("9");
    int _jspx_eval_html_text_16 = _jspx_th_html_text_16.doStartTag();
    if (_jspx_th_html_text_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_16);
    return false;
  }

  private boolean _jspx_meth_html_text_17(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_17 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_17.setPageContext(_jspx_page_context);
    _jspx_th_html_text_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_17.setProperty("dosageForm");
    _jspx_th_html_text_17.setTabindex("5");
    int _jspx_eval_html_text_17 = _jspx_th_html_text_17.doStartTag();
    if (_jspx_th_html_text_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_17);
    return false;
  }

  private boolean _jspx_meth_html_text_18(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_18 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_18.setPageContext(_jspx_page_context);
    _jspx_th_html_text_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_18.setProperty("expirationDate");
    _jspx_th_html_text_18.setTabindex("10");
    int _jspx_eval_html_text_18 = _jspx_th_html_text_18.doStartTag();
    if (_jspx_th_html_text_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_18);
    return false;
  }

  private boolean _jspx_meth_html_text_19(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_19 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_19.setPageContext(_jspx_page_context);
    _jspx_th_html_text_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_19.setProperty("containerSize");
    _jspx_th_html_text_19.setTabindex("6");
    int _jspx_eval_html_text_19 = _jspx_th_html_text_19.doStartTag();
    if (_jspx_th_html_text_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_19);
    return false;
  }

  private boolean _jspx_meth_html_text_20(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.struts.taglib.html.TextTag _jspx_th_html_text_20 = (org.apache.struts.taglib.html.TextTag) _jspx_tagPool_html_text_tabindex_property_nobody.get(org.apache.struts.taglib.html.TextTag.class);
    _jspx_th_html_text_20.setPageContext(_jspx_page_context);
    _jspx_th_html_text_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_text_20.setProperty("manufacturer");
    _jspx_th_html_text_20.setTabindex("11");
    int _jspx_eval_html_text_20 = _jspx_th_html_text_20.doStartTag();
    if (_jspx_th_html_text_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_text_tabindex_property_nobody.reuse(_jspx_th_html_text_20);
    return false;
  }

  private boolean _jspx_meth_html_file_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:file
    org.apache.struts.taglib.html.FileTag _jspx_th_html_file_0 = (org.apache.struts.taglib.html.FileTag) _jspx_tagPool_html_file_tabindex_property_nobody.get(org.apache.struts.taglib.html.FileTag.class);
    _jspx_th_html_file_0.setPageContext(_jspx_page_context);
    _jspx_th_html_file_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_file_0.setProperty("attachment");
    _jspx_th_html_file_0.setTabindex("7");
    int _jspx_eval_html_file_0 = _jspx_th_html_file_0.doStartTag();
    if (_jspx_th_html_file_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_file_tabindex_property_nobody.reuse(_jspx_th_html_file_0);
    return false;
  }

  private boolean _jspx_meth_html_file_1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:file
    org.apache.struts.taglib.html.FileTag _jspx_th_html_file_1 = (org.apache.struts.taglib.html.FileTag) _jspx_tagPool_html_file_tabindex_property_nobody.get(org.apache.struts.taglib.html.FileTag.class);
    _jspx_th_html_file_1.setPageContext(_jspx_page_context);
    _jspx_th_html_file_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_form_0);
    _jspx_th_html_file_1.setProperty("attachment");
    _jspx_th_html_file_1.setTabindex("7");
    int _jspx_eval_html_file_1 = _jspx_th_html_file_1.doStartTag();
    if (_jspx_th_html_file_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_file_tabindex_property_nobody.reuse(_jspx_th_html_file_1);
    return false;
  }

  private boolean _jspx_meth_html_hidden_3(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_notPresent_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_3 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_value_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
    _jspx_th_html_hidden_3.setPageContext(_jspx_page_context);
    _jspx_th_html_hidden_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_notPresent_1);
    _jspx_th_html_hidden_3.setProperty("operationType");
    _jspx_th_html_hidden_3.setValue("Save");
    int _jspx_eval_html_hidden_3 = _jspx_th_html_hidden_3.doStartTag();
    if (_jspx_th_html_hidden_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_hidden_value_property_nobody.reuse(_jspx_th_html_hidden_3);
    return false;
  }

  private boolean _jspx_meth_html_hidden_4(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_present_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_4 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_value_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
    _jspx_th_html_hidden_4.setPageContext(_jspx_page_context);
    _jspx_th_html_hidden_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_present_1);
    _jspx_th_html_hidden_4.setProperty("operationType");
    _jspx_th_html_hidden_4.setValue("Update");
    int _jspx_eval_html_hidden_4 = _jspx_th_html_hidden_4.doStartTag();
    if (_jspx_th_html_hidden_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_hidden_value_property_nobody.reuse(_jspx_th_html_hidden_4);
    return false;
  }
}

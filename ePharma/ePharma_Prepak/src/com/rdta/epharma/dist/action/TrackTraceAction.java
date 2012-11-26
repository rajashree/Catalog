
/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 package com.rdta.epharma.dist.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.TLQueryRunner;

public class TrackTraceAction extends Action {

	public static ResourceBundle bundle = ResourceBundle.getBundle("com.rdta.epharma.dist.distributor");

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String searchKey = request.getParameter("searchKey");
		String criteria = request.getParameter("criteria");
		boolean success = true;
		if(criteria.equals("Pedigree")) {
			success = searchByPedigree(request);
		} else if(criteria.equals("SSCC")) {
			success = searchBySSCC(request);
		} else if(criteria.equals("EPC")) {
			success = searchByEPC(request);
		} else if(criteria.equals("SGTIN")) {
			success = searchBySGTIN(request);
		} else if(criteria.equals("OrderID")) {
			success = searchByOrderID(request);
		} else if(criteria.equals("InvoiceNum")) {
			success = searchByInvoiceNum(request);
		} else if(criteria.equals("DespatchAdv")) {
			success = searchByDespathAdv(request);
		}
		ActionForward forward = null;
		if(success)
			forward = mapping.findForward("success");
		else
			forward = mapping.findForward("failure");
		return forward;
	}

	private boolean searchByDespathAdv(HttpServletRequest request) {
		String despatchID = request.getParameter("searchKey");
		String xQuery = "";
		boolean success = false;
		String TLDatabaseName = bundle.getString("Distributor.TrackTrace.Database");//"ePharma"; // Fetch this from properties file.
		xQuery = "let $x := <TR bgcolor=\"#8494ca\"> " +
				"  <TD class=\"type-whrite\"><STRONG>ORDER ID</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CUSTODIAN</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CUSTODIAN TYPE</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>LOCATION</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CONTACT</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>TXN TYPE</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>AUTHENTICATOR</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>FROM</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>TO</STRONG></TD>" +
				"</TR> " ;
		xQuery = xQuery + "let $y := ( for $custody in collection('tig:///" + TLDatabaseName + "/APN')/APN/Pedigrees/Pedigree/Custody" +
				" for $orderID in collection('tig:///" + TLDatabaseName +"/Orders')/Order/BuyersID " +
				"for $despatchID in collection('tig:///" + TLDatabaseName +"/DespatchAdvice')/DespatchAdvice/OrderReference/BuyersID" +
				" where $custody/TransactionNumber = $orderID and contains($despatchID, '" + despatchID +
				"') and $orderID = $despatchID " +
				" return " +
				"<TR bgcolor='C8C8C8'>" +
				"<TD class='td-menu'>{data($custody/TransactionNumber)}</TD>" +
				"<TD class='td-menu'>{data($custody/Name)}</TD> " +
				"<TD class='td-menu'>{data($custody/@type)}</TD> " +
				"<TD class='td-menu'>{data($custody/Address)}</TD> " +
				"<TD class='td-menu'>{data($custody/Contact)}</TD> " +
				"<TD class='td-menu'>{data($custody/TransactionType)}</TD> " +
				"<TD class='td-menu'>{data($custody/AuthenticatorName)}</TD> " +
				"<TD class='td-menu'>{data($custody/InCustodyFromDate)}</TD> " +
				"<TD class='td-menu'>{data($custody/InCustodyToDate)}</TD> " +
				"</TR> )" +
				"return ($x, $y)";

		success = runQueryAndSetResult(request, xQuery);
		return success;
	}

	private boolean searchByInvoiceNum(HttpServletRequest request) {
		String invoiceID = request.getParameter("searchKey");
		String xQuery = "";
		boolean success = false;
		String TLDatabaseName = bundle.getString("Distributor.TrackTrace.Database");//"ePharma"; // Fetch this from properties file.
		xQuery = "let $x := <TR bgcolor=\"#8494ca\"> " +
				"  <TD class=\"type-whrite\"><STRONG>ORDER ID</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CUSTODIAN</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CUSTODIAN TYPE</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>LOCATION</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CONTACT</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>TXN TYPE</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>AUTHENTICATOR</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>FROM</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>TO</STRONG></TD>" +
				"</TR> " ;
		xQuery = xQuery + "let $y := ( for $custody in collection('tig:///" + TLDatabaseName + "/APN')/APN/Pedigrees/Pedigree/Custody" +
				" for $orderID in collection('tig:///" + TLDatabaseName +"/Orders')/Order/BuyersID " +
				"for $invoiceBuyerID in collection('tig:///" + TLDatabaseName +"/Invoices')/Invoice/OrderReference/BuyersID" +
				" where $custody/TransactionNumber = $orderID and contains($invoiceBuyerID, '" + invoiceID +
				"') and $orderID = $invoiceBuyerID " +
				" return " +
				"<TR bgcolor='C8C8C8'>" +
				"<TD class='td-menu'>{data($custody/TransactionNumber)}</TD>" +
				"<TD class='td-menu'>{data($custody/Name)}</TD> " +
				"<TD class='td-menu'>{data($custody/@type)}</TD> " +
				"<TD class='td-menu'>{data($custody/Address)}</TD> " +
				"<TD class='td-menu'>{data($custody/Contact)}</TD> " +
				"<TD class='td-menu'>{data($custody/TransactionType)}</TD> " +
				"<TD class='td-menu'>{data($custody/AuthenticatorName)}</TD> " +
				"<TD class='td-menu'>{data($custody/InCustodyFromDate)}</TD> " +
				"<TD class='td-menu'>{data($custody/InCustodyToDate)}</TD> " +
				"</TR> )" +
				"return ($x, $y)";

		success = runQueryAndSetResult(request, xQuery);
		return success;
	}

	private boolean searchByEPC(HttpServletRequest request) {
		String tagID = request.getParameter("searchKey");
		String xQuery = "";
		boolean success = false;
		String TLDatabaseName = bundle.getString("Distributor.TrackTrace.Database");//"ePharma"; // Fetch this from properties file.
		xQuery = "let $x := <TR bgcolor=\"#8494ca\"> " +
		"  <TD class=\"type-whrite\"><STRONG>EPC</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>DISCOVERY TIME</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>LAST SEEN TIME</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>ANTENNA ID</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>READER ID</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>READ COUNT</STRONG></TD>" +
		"</TR> " ;
		xQuery += "let $y := ( for $b in collection('tig:///" + TLDatabaseName +"/FilteredEvents')/RDTA-Raw-Event/Observations/Observation ";
		xQuery = xQuery + "where contains($b/TagID,'"+tagID+"') order by $b/LastSeenTime ";
		xQuery = xQuery + "return ";
		xQuery = xQuery + "<tr bgcolor='C8C8C8'>	 ";
		xQuery = xQuery + "<TD class='td-menu'>";
		xQuery = xQuery + "{data($b/TagID)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/DiscoveryTime)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{count($b/LastSeenTime)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/AntennaID)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/ReaderID)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/ReadCount)}</TD> ";
		xQuery = xQuery + "</tr> )" +
				"return ($x, $y)";

		success = runQueryAndSetResult(request, xQuery);
		return success;
	}

	private boolean searchBySGTIN(HttpServletRequest request) {
			String tagID = request.getParameter("searchKey");
			String xQuery = "";
			boolean success = false;
			String TLDatabaseName = bundle.getString("Distributor.TrackTrace.Database");//"ePharma"; // Fetch this from properties file.
			xQuery = "let $x := <TR bgcolor=\"#8494ca\"> " +
			"  <TD class=\"type-whrite\"><STRONG>SGTIN</STRONG></TD>" +
			"  <TD class=\"type-whrite\"><STRONG>DISCOVERY TIME</STRONG></TD>" +
			"  <TD class=\"type-whrite\"><STRONG>LAST SEEN TIME</STRONG></TD>" +
			"  <TD class=\"type-whrite\"><STRONG>ANTENNA ID</STRONG></TD>" +
			"  <TD class=\"type-whrite\"><STRONG>READER ID</STRONG></TD>" +
			"  <TD class=\"type-whrite\"><STRONG>READ COUNT</STRONG></TD>" +
			"</TR> " ;
			xQuery += "let $y := ( for $b in collection('tig:///" + TLDatabaseName +"/FilteredEvents')/RDTA-Raw-Event/Observations/Observation ";
			xQuery = xQuery + "where contains($b/TagID,'"+tagID+"') and contains($b/TagType,'sgtin') order by $b/LastSeenTime ";
			xQuery = xQuery + "return ";
			xQuery = xQuery + "<tr bgcolor='C8C8C8'>	 ";
			xQuery = xQuery + "<TD class='td-menu'>";
			xQuery = xQuery + "{data($b/TagID)}</TD> ";
			xQuery = xQuery + "<TD class='td-menu'>{data($b/DiscoveryTime)}</TD> ";
			xQuery = xQuery + "<TD class='td-menu'>{count($b/LastSeenTime)}</TD> ";
			xQuery = xQuery + "<TD class='td-menu'>{data($b/AntennaID)}</TD> ";
			xQuery = xQuery + "<TD class='td-menu'>{data($b/ReaderID)}</TD> ";
			xQuery = xQuery + "<TD class='td-menu'>{data($b/ReadCount)}</TD> ";
			xQuery = xQuery + "</tr> )" +
					"return ($x, $y)";

			success = runQueryAndSetResult(request, xQuery);
			return success;
	}

	private boolean searchByOrderID(HttpServletRequest request) {
		String orderID = request.getParameter("searchKey");
		String xQuery = "";
		boolean success = false;
		String TLDatabaseName = bundle.getString("Distributor.TrackTrace.Database");//"ePharma"; // Fetch this from properties file.
		xQuery = "let $x := <TR bgcolor=\"#8494ca\"> " +
				"  <TD class=\"type-whrite\"><STRONG>ORDER ID</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CUSTODIAN</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CUSTODIAN TYPE</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>LOCATION</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>CONTACT</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>TXN TYPE</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>AUTHENTICATOR</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>FROM</STRONG></TD>" +
				"  <TD class=\"type-whrite\"><STRONG>TO</STRONG></TD>" +
				"</TR> " ;
		xQuery = xQuery + "let $y := ( for $custody in collection('tig:///" + TLDatabaseName + "/APN')/APN/Pedigrees/Pedigree/Custody" +
				" for $order in collection('tig:///" + TLDatabaseName +"/Orders')/Order/BuyersID" +
				" where $custody/TransactionNumber = $order and contains($order, '" + orderID +
				"') return " +
				"<TR bgcolor='C8C8C8'>" +
				"<TD class='td-menu'>{data($custody/TransactionNumber)}</TD>" +
				"<TD class='td-menu'>{data($custody/Name)}</TD> " +
				"<TD class='td-menu'>{data($custody/@type)}</TD> " +
				"<TD class='td-menu'>{data($custody/Address)}</TD> " +
				"<TD class='td-menu'>{data($custody/Contact)}</TD> " +
				"<TD class='td-menu'>{data($custody/TransactionType)}</TD> " +
				"<TD class='td-menu'>{data($custody/AuthenticatorName)}</TD> " +
				"<TD class='td-menu'>{data($custody/InCustodyFromDate)}</TD> " +
				"<TD class='td-menu'>{data($custody/InCustodyToDate)}</TD> " +
				"</TR> )" +
				"return ($x, $y)";

		success = runQueryAndSetResult(request, xQuery);
		return success;
	}

	private boolean searchBySSCC(HttpServletRequest request) {
		String tagID = request.getParameter("searchKey");
		String xQuery = "";
		boolean success = false;
		String TLDatabaseName = bundle.getString("Distributor.TrackTrace.Database");//"ePharma"; // Fetch this from properties file.
		xQuery = "let $x := <TR bgcolor=\"#8494ca\"> " +
		"  <TD class=\"type-whrite\"><STRONG>SSCC</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>DISCOVERY TIME</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>LAST SEEN TIME</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>ANTENNA ID</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>READER ID</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>READ COUNT</STRONG></TD>" +
		"</TR> " ;
		xQuery += "let $y := ( for $b in collection('tig:///" + TLDatabaseName +"/FilteredEvents')/RDTA-Raw-Event/Observations/Observation ";
		xQuery = xQuery + "where contains($b/TagID,'"+tagID+"') and contains($b/TagType,'sscc') order by $b/LastSeenTime ";
		xQuery = xQuery + "return ";
		xQuery = xQuery + "<tr bgcolor='C8C8C8'>	 ";
		xQuery = xQuery + "<TD class='td-menu'>";
		xQuery = xQuery + "{data($b/TagID)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/DiscoveryTime)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{count($b/LastSeenTime)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/AntennaID)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/ReaderID)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/ReadCount)}</TD> ";
		xQuery = xQuery + "</tr> )" +
				"return ($x, $y)";

/*		xQuery = "<result>	<title> " +
        		"Items by Pedigree ID. "+
				"</title><cols>" +
				"<tagID/>" +
				"<discoverytime/>" +
				"<lastseentime/>" +
				"<antennaID/>" +
				"<readerID/>" +
				"<readcount/> " +
				"</cols><headings><record>" +
				"<tagID>    Tag ID          </tagID>" +
				"<discoverytime>   Discovery Time         </discoverytime>" +
				"<lastseentime> Last Seen Time       </lastseentime>" +
				"<antennaID> Antenna ID </antennaID>" +
				"<readerID> Reader ID </readerID>" +
				"<readcount>Read Count</readcount>" +
				"</record>" +
				"</headings>" +
				" <records>";
		xQuery = xQuery + "{for $b in collection('tig:///" + TLDatabaseName +"/FilteredEvents')/RDTA-Raw-Event/Observations/Observation ";
		xQuery = xQuery + "where contains($b/TagID,'"+tagID+"') order by $b/LastSeenTime ";
		xQuery = xQuery + "return ";
		xQuery = xQuery + "<record>	 ";
		xQuery = xQuery + "<tagID>";
		xQuery = xQuery + "{data($b/TagID)}</tagID> ";
		xQuery = xQuery + "<discoverytime>{data($b/DiscoveryTime)}</discoverytime> ";
		xQuery = xQuery + "<lastseentime>{count($b/LastSeenTime)}</lastseentime> ";
		xQuery = xQuery + "<antennaID>{data($b/AntennaID)}</antennaID> ";
		xQuery = xQuery + "<readerID>{data($b/ReaderID)}</readerID> ";
		xQuery = xQuery + "<readcount>{data($b/ReadCount)}</readcount> ";
		xQuery = xQuery + "</record> }";
		xQuery += "</records>" +
				"</result>";
*/
		success = runQueryAndSetResult(request, xQuery);
		return success;
	}

	private boolean searchByPedigree(HttpServletRequest request) {

		String pedigreeDocID = request.getParameter("searchKey");
		String xQuery = "";
		boolean success = false;
		String TLDatabaseName = bundle.getString("Distributor.TrackTrace.Database");//"ePharma"; // Fetch this from properties file.
		xQuery = "let $x := <TR bgcolor=\"#8494ca\"> " +
		"  <TD class=\"type-whrite\"><STRONG>Pedigree Doc ID</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>MANUFACTURER</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>PRODUCT</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>DATE/TIME</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>AUTHENTICATOR</STRONG></TD>" +
		"  <TD class=\"type-whrite\"><STRONG>VALID</STRONG></TD>" +
		"</TR> " ;
		xQuery += "let $y := (for $b in collection('tig:///" + TLDatabaseName +"/APN')/APN/Pedigrees ";
		xQuery = xQuery + "where contains($b/Pedigree/DocumentId,'"+pedigreeDocID+"') order by $b/Pedigree/DateTime ";
		xQuery = xQuery + "return ";
		xQuery = xQuery + "<tr bgcolor='C8C8C8'>	 ";
		xQuery = xQuery + "<TD class='td-menu'>";
		xQuery = xQuery + "<A href='/ePharma/dist/epedigree/ViewPedigree.jsp?doc={data($b/Pedigree/DocumentId)}' target='_blank' class='type-red'>{data($b/Pedigree/DocumentId)}</A></TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/Pedigree/Manufacturer/Name)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{count($b/Pedigree/Products/Product)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/Pedigree/DateTime)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>{data($b/Pedigree/Custody/AuthenticatorName)}</TD> ";
		xQuery = xQuery + "<TD class='td-menu'>Valid</TD> ";
		xQuery = xQuery + "</tr>)" +
				"return  ($x, $y)";

		success = runQueryAndSetResult(request, xQuery);
		return success;
	}

	private boolean runQueryAndSetResult(HttpServletRequest request, String xQuery) {
		TLQueryRunner runner = new TLQueryRunner();
		boolean success = false;
		try {
			List inStreams = runner.executeQuery(xQuery);
			request.setAttribute("criteria", request.getParameter("criteria"));
			StringBuffer result = new StringBuffer();
			for(int i=0; i < inStreams.size(); i++) {
				InputStream is = (InputStream) inStreams.get(i);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while(is.available()> 0) {
					bos.write(is.read());
				}
				bos.flush();
				result.append(new String(bos.toByteArray()));
				//result.append("<br>");
			}
			request.setAttribute("SearchResult", result.toString());
			System.out.println("Resulting XML ============ " + result);
			success = true;
		} catch (PersistanceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

}

/*
 * Created on Oct 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

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

 package com.rdta.epharma.epedigree.action;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Arun Kumar
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ValidateSignature extends Action {

	private static final QueryRunner queryRunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	Helper helper = new Helper();

	private static Log log = LogFactory.getLog(ReceivingManagerAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		log.info("Inside Action ValidateSignature....... ");
		String path = request.getContextPath();
		String servIP = request.getServerName();
		String clientIP = request.getRemoteAddr();

		//	  	String sessionID = (String)session.getAttribute("sessionID");
		String pagenm = request.getParameter("pagenm");
		String tp_company_nm = request.getParameter("tp_company_nm");
		String sect = request.getParameter("part");
		String redirURL = "";
		String verifyEntry = "";
		String APNID = request.getParameter("pedid");
		String PEDID = request.getParameter("pedid");
		String partID = request.getParameter("partid");
		log.info("part =" + request.getParameter("part"));
		
		try{
							
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in Action :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        

			if ( !validateResult.equals("VALID")){
			   // return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
			
		String PedigreeOrder = (String) sess.getAttribute("PedigreeOrder");
		String SigExists = (String) sess.getAttribute("signstatus");
		if (SigExists == null)
			SigExists = "";
		else
			SigExists = "";
		String pedSign = (String) sess.getAttribute("pedsignstatus");
		if (pedSign == null)
			pedSign = "";
		String pedValid = (String) sess.getAttribute("pedvalid");
		if (pedValid == null)
			pedValid = "";
		String pedInvalid = (String) sess.getAttribute("pedInvalid");
		if (pedInvalid == null)
			pedInvalid = "";
		String xQuery = "";
		if (sect.equalsIgnoreCase("APN")) {
			log.info(" Inside Action before checking signature at APN level");
			xQuery = "declare namespace sig='http://www.w3.org/2000/09/xmldsig#'; ";
			xQuery = xQuery + "for $j in collection('tig:///ePharma/APN/') ";
			xQuery = xQuery + "where $j/APN/DocumentId = '" + APNID + "' ";
			xQuery = xQuery + "return fn:exists($j/APN/sig:Signature)";

			List list = (List) queryRunner.executeQuery(xQuery);
			if (list != null) {

				ByteArrayInputStream str = (ByteArrayInputStream) list.get(0);
				int c;
				SigExists = "";
				while ((c = str.read()) != -1) {
					SigExists = SigExists + (char) c;
				}
				log
						.info(" Inside Action before validating signature at APN level");

				if (SigExists.equalsIgnoreCase("true")) {
					xQuery = "tlsp:VerifyAPNSignature('" + APNID + "') ";
					log.info("query =" + xQuery);
					List list2 = (List) queryRunner.executeQuery(xQuery);
					ByteArrayInputStream apn = (ByteArrayInputStream) list2
							.get(0);
					int apnex;
					SigExists = "";
					while ((apnex = apn.read()) != -1) {
						SigExists = SigExists + (char) apnex;
					}
					verifyEntry = "<verifySig> ";
					verifyEntry = verifyEntry
							+ "<verifyLevel>APN</verifyLevel> ";
					verifyEntry = verifyEntry + "<verifyDocumentId>" + APNID
							+ "</verifyDocumentId> ";
					verifyEntry = verifyEntry + "<verifyPedigreeDoctId>"
							+ 12345 + "</verifyPedigreeDoctId>";
					verifyEntry = verifyEntry + "<verifyLevelUniqueId>" + 12345
							+ "</verifyLevelUniqueId>";
					verifyEntry = verifyEntry
							+ "<verifyDate>{current-dateTime()}</verifyDate> ";
					verifyEntry = verifyEntry + "<verifyBy>" + 12345
							+ "</verifyBy>";
					verifyEntry = verifyEntry + "<verifyStatus>" + SigExists
							+ "</verifyStatus> ";
					verifyEntry = verifyEntry + "</verifySig> ";

				} else {
					SigExists = "SIG EMPTY";
					verifyEntry = verifyEntry + "<verifySig> ";
					verifyEntry = verifyEntry
							+ "<verifyLevel>APN</verifyLevel> ";
					verifyEntry = verifyEntry + "<verifyDocumentId>" + APNID
							+ "</verifyDocumentId> ";
					verifyEntry = verifyEntry + "<verifyPedigreeDoctId>"
							+ 12345 + "</verifyPedigreeDoctId>";
					verifyEntry = verifyEntry + "<verifyLevelUniqueId>" + 12345
							+ "</verifyLevelUniqueId>";
					verifyEntry = verifyEntry
							+ "<verifyDate>{current-dateTime()}</verifyDate> ";
					verifyEntry = verifyEntry + "<verifyBy>" + 12345
							+ "</verifyBy>";
					verifyEntry = verifyEntry + "<verifyStatus>" + SigExists
							+ "</verifyStatus> ";
					verifyEntry = verifyEntry + "</verifySig> ";

					log.info("Signature is not there" + SigExists);
				}
				log.info(" Inside Action inserting  status to APNSignatureStatus signature at APN level");

				xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
				xQuery = xQuery
						+ "where $i/verifySig/verifyLevel='APN' and $i/verifySig/verifyDocumentId = '"
						+ APNID + "'";
				xQuery = xQuery
						+ "return tig:delete-document(document-uri( $i )), ";
				xQuery = xQuery
						+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/',"
						+ verifyEntry + ")";

				queryRunner.executeQuery(xQuery);
				verifyEntry = "";
			}

		} else if (sect.equalsIgnoreCase("Pedigree")) {
			log.info(" Inside Action before checking signature at Pedigree level");
			log.info(" Inside Action before validating signature at Pedigree level");
			xQuery = "tlsp:VerifyPedugreeSignature('" + APNID + "')";
			List list = (List) queryRunner.executeQuery(xQuery);
			Node pstatus = XMLUtil.parse((ByteArrayInputStream) list.get(0));

			Node exist = XMLUtil.getNode(pstatus, "exist");
			Node empty = XMLUtil.getNode(pstatus, "empty");
			Node Valid = XMLUtil.getNode(exist, "valid");
			Node Invalid = XMLUtil.getNode(exist, "invalid");

			pedSign = XMLUtil.getValue(empty, "count");
			pedValid = XMLUtil.getValue(Valid, "count");
			pedInvalid = XMLUtil.getValue(Invalid, "count");
			log.info("****************************pedSign:::::" + pedSign);
			if (pedSign != null) {
				if (pedSign.equals("0")) {
					//I have to reduce the code

				} else {
					pedSign = pedSign + "SIG EMPTY";
					log.info("going");
					List emptylist = (List) XMLUtil.getNodes(empty);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {
							log.info(" Inside Action inserting  status to APNSignatureStatus signature at APN level");

							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>PEDIGREE</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "SIG EMPTY" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ( $i/verifySig/verifyLevel='PEDIGREE' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("Inside Action  at PED level" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}

				}
			}
			if (pedValid != null) {
				if (!pedValid.equals("0")) {
					log.info(" Inside Action inserting VALID status to APNSignatureStatus signature at Pedigree level");

					List emptylist = (List) XMLUtil.getNodes(Valid);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {
							log.info("camkekekek");
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>PEDIGREE</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "VALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ( $i/verifySig/verifyLevel='PEDIGREE' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							queryRunner.executeQuery(xQuery);

						}
					}

					pedValid = pedSign + "Valid";
				}
			}
			if (pedInvalid != null) {
				if (!pedInvalid.equals("0")) {
					log.info(" Inside Action inserting InValid status to APNSignatureStatus signature at APN level");

					pedInvalid = pedInvalid + "InValid";

					log.info("going to Invalid element");
					List emptylist = (List) XMLUtil.getNodes(Invalid);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {

							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>PEDIGREE</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "INVALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ($i/verifySig/verifyLevel='PEDIGREE' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}
				}
			}

		} else if (sect.equalsIgnoreCase("Manufacturer")) {

			xQuery = "tlsp:VerifyManuFactureSignature('" + APNID + "')";
			List list = (List) queryRunner.executeQuery(xQuery);
			Node pstatus = XMLUtil.parse((ByteArrayInputStream) list.get(0));

			Node exist = XMLUtil.getNode(pstatus, "exist");
			Node empty = XMLUtil.getNode(pstatus, "empty");
			Node Valid = XMLUtil.getNode(exist, "valid");
			Node Invalid = XMLUtil.getNode(exist, "invalid");

			pedSign = XMLUtil.getValue(empty, "count");
			pedValid = XMLUtil.getValue(Valid, "count");
			pedInvalid = XMLUtil.getValue(Invalid, "count");
			log.info("****************************pedSign:::::" + pedSign);
			if (pedSign != null) {
				if (pedSign.equals("0")) {
					//I have to reduce the code

				} else {
					pedSign = pedSign + "SIG EMPTY";
					log.info("going");
					List emptylist = (List) XMLUtil.getNodes(empty);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {
							log.info("going to inser empty element");
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Manfacture</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "SIG EMPTY" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ( $i/verifySig/verifyLevel='Manfacture' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							queryRunner.executeQuery(xQuery);

						}
					}

				}
			}
			if (pedValid != null) {
				if (!pedValid.equals("0")) {

					List emptylist = (List) XMLUtil.getNodes(Valid);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {
							log.info("camkekekek");
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Manfacture</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "VALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ( $i/verifySig/verifyLevel='Manfacture' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}

					pedValid = pedSign + "Valid";
				}
			}
			if (pedInvalid != null) {
				if (!pedInvalid.equals("0")) {
					pedInvalid = pedInvalid + "InValid";

					log.info("going to Invalid element");
					List emptylist = (List) XMLUtil.getNodes(Invalid);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Manfacture</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "INVALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ($i/verifySig/verifyLevel='Manfacture' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}
				}
			}

		} else if (sect.equalsIgnoreCase("Product")) {
			//a

			xQuery = "tlsp:VerifyProductSignature('" + APNID + "','"
					+ PedigreeOrder + "')";
			List list = (List) queryRunner.executeQuery(xQuery);
			Node pstatus = XMLUtil.parse((ByteArrayInputStream) list.get(0));

			Node exist = XMLUtil.getNode(pstatus, "exist");
			Node empty = XMLUtil.getNode(pstatus, "empty");
			Node Valid = XMLUtil.getNode(exist, "valid");
			Node Invalid = XMLUtil.getNode(exist, "invalid");

			pedSign = XMLUtil.getValue(empty, "count");
			pedValid = XMLUtil.getValue(Valid, "count");
			pedInvalid = XMLUtil.getValue(Invalid, "count");
			if (pedSign != null) {
				if (pedSign.equals("0")) {
					//I have to reduce the code

				} else {
					pedSign = pedSign + "SIG EMPTY";

					List emptylist = (List) XMLUtil.getNodes(empty);
					int size = emptylist.size();
					boolean flag = true;
					for (int i = 0; i < size; i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);

						if (docId != "") {

							if (flag == true) {
								xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
								xQuery = xQuery
										+ "where ( $i/verifySig/verifyLevel='Product' and $i/verifySig/verifyDocumentId = '"
										+ APNID + "'";
								xQuery = xQuery
										+ " and $i/verifySig/verifyPedigreeDoctId = '"
										+ docId + "' ) ";
								xQuery = xQuery
										+ "return tig:delete-document(document-uri( $i ))";
								queryRunner.executeQuery(xQuery);
								flag = false;
							}
							log.info("going to inser empty element " + i);
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Product</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "SIG EMPTY" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							/*
							 * xQuery = xQuery + "where (
							 * $i/verifySig/verifyLevel='Product' and
							 * $i/verifySig/verifyDocumentId = '"+APNID+"'";
							 * xQuery = xQuery + " and
							 * $i/verifySig/verifyPedigreeDoctId = '"+docId+"'
							 * )"; xQuery = xQuery + "return "; xQuery = xQuery +
							 * "tig:insert-document(
							 * 'tig:///ePharma/APNSignatureStatus/',
							 * "+verifyEntry+" )";
							 */
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}

				}
			}
			if (pedValid != null) {
				if (!pedValid.equals("0")) {

					List emptylist = (List) XMLUtil.getNodes(Valid);
					boolean flag = true;
					int size = emptylist.size();
					for (int i = 0; i < size; i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						log.info("documentId " + docId);
						if (docId != "") {
							log.info("camkekekek");
							if (flag == true) {
								xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
								xQuery = xQuery
										+ "where ( $i/verifySig/verifyLevel='Product' and $i/verifySig/verifyDocumentId = '"
										+ APNID + "'";
								xQuery = xQuery
										+ " and $i/verifySig/verifyPedigreeDoctId = '"
										+ docId + "' ) ";
								xQuery = xQuery
										+ "return tig:delete-document(document-uri( $i ))";
								queryRunner.executeQuery(xQuery);
								flag = false;
							}
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Product</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "VALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							/*
							 * xQuery = "for $i in
							 * collection('tig:///ePharma/APNSignatureStatus')";
							 * xQuery = xQuery + "where (
							 * $i/verifySig/verifyLevel='Product' and
							 * $i/verifySig/verifyDocumentId = '"+APNID+"'";
							 * xQuery = xQuery + " and
							 * $i/verifySig/verifyPedigreeDoctId = '"+docId+"'
							 * )"; xQuery = xQuery + "return ";
							 */
							xQuery = "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}

					pedValid = pedSign + "Valid";
				}
			}
			if (pedInvalid != null) {
				if (!pedInvalid.equals("0")) {
					pedInvalid = pedInvalid + "InValid";
					boolean flag = true;
					log.info("going to Invalid element");
					List emptylist = (List) XMLUtil.getNodes(Invalid);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);

						if (docId != "") {
							if (flag == true) {
								xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
								xQuery = xQuery
										+ "where ( $i/verifySig/verifyLevel='Product' and $i/verifySig/verifyDocumentId = '"
										+ APNID + "'";
								xQuery = xQuery
										+ " and $i/verifySig/verifyPedigreeDoctId = '"
										+ docId + "' ) ";
								xQuery = xQuery
										+ "return tig:delete-document(document-uri( $i ))";
								queryRunner.executeQuery(xQuery);
								flag = false;
							}
							log.info("camkekekek");
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Product</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "INVALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ($i/verifySig/verifyLevel='Product' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery + "return  ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}
				}
			}

		} else if (sect.equalsIgnoreCase("Custody")) {

			xQuery = "tlsp:VerifyCustodySignature('" + APNID + "','"
					+ PedigreeOrder + "')";
			List list = (List) queryRunner.executeQuery(xQuery);
			Node pstatus = XMLUtil.parse((ByteArrayInputStream) list.get(0));

			Node exist = XMLUtil.getNode(pstatus, "exist");
			Node empty = XMLUtil.getNode(pstatus, "empty");
			Node Valid = XMLUtil.getNode(exist, "valid");
			Node Invalid = XMLUtil.getNode(exist, "invalid");

			pedSign = XMLUtil.getValue(empty, "count");
			pedValid = XMLUtil.getValue(Valid, "count");
			pedInvalid = XMLUtil.getValue(Invalid, "count");
			log.info("****************************pedSign:::::" + pedSign);
			if (pedSign != null) {
				if (pedSign.equals("0")) {
					//I have to reduce the code

				} else {
					pedSign = pedSign + "SIG EMPTY";
					log.info("going");
					List emptylist = (List) XMLUtil.getNodes(empty);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);

						if (docId != "") {
							log.info("going to inser empty element");
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Custody</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "SIG EMPTY" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ( $i/verifySig/verifyLevel='Custody' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}

				}
			}
			if (pedValid != null) {
				if (!pedValid.equals("0")) {

					List emptylist = (List) XMLUtil.getNodes(Valid);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {
							log.info("camkekekek");
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Custody</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "VALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ( $i/verifySig/verifyLevel='Custody' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}

					pedValid = pedSign + "Valid";
				}
			}
			if (pedInvalid != null) {
				if (!pedInvalid.equals("0")) {
					pedInvalid = pedInvalid + "InValid";

					log.info("going to Invalid element");
					List emptylist = (List) XMLUtil.getNodes(Invalid);
					for (int i = 0; i < emptylist.size(); i++) {
						Node node = (Node) emptylist.get(i);
						String docId = "";
						if (node.getNodeName().equals("DocumentId"))
							docId = XMLUtil.getValue(node);
						if (docId != "") {
							log.info("camkekekek");
							verifyEntry = "<verifySig> ";
							verifyEntry = verifyEntry
									+ "<verifyLevel>Custody</verifyLevel> ";
							verifyEntry = verifyEntry + "<verifyDocumentId>"
									+ APNID + "</verifyDocumentId> ";
							verifyEntry = verifyEntry
									+ "<verifyPedigreeDoctId>" + docId
									+ "</verifyPedigreeDoctId>";
							verifyEntry = verifyEntry + "<verifyLevelUniqueId>"
									+ 12345 + "</verifyLevelUniqueId>";
							verifyEntry = verifyEntry
									+ "<verifyDate>{current-dateTime()}</verifyDate> ";
							verifyEntry = verifyEntry + "<verifyBy>" + 12345
									+ "</verifyBy>";
							verifyEntry = verifyEntry + "<verifyStatus>"
									+ "INVALID" + "</verifyStatus>";
							verifyEntry = verifyEntry + "</verifySig> ";
							xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
							xQuery = xQuery
									+ "where ($i/verifySig/verifyLevel='Custody' and $i/verifySig/verifyDocumentId = '"
									+ APNID + "'";
							xQuery = xQuery
									+ " and $i/verifySig/verifyPedigreeDoctId = '"
									+ docId + "' )";
							xQuery = xQuery
									+ "return tig:delete-document(document-uri( $i )), ";
							xQuery = xQuery
									+ "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "
									+ verifyEntry + " )";
							log.info("************Query:::::" + xQuery);
							queryRunner.executeQuery(xQuery);

						}
					}
				}
			}

		} else {
			log.info("xquery is not formed");
		}
		//..
		sess.setAttribute("signstatus", SigExists);
		sess.setAttribute("pedsignstatus", pedSign);
		sess.setAttribute("pedsignvalid", pedValid);
		sess.setAttribute("pedsignInvalid", pedInvalid);
		request.setAttribute("pedid", APNID);
		log.info("Path :" + path);
		log.info("servIP :" + servIP);
		log.info("APNID :" + APNID);
		log.info("pagenm :" + pagenm);
		log.info("tp_company_nm :" + tp_company_nm);
		log.info("sect :" + sect);
		//log.info("pagenm :"+pagenm);

		return mapping.findForward("success");
	
	}catch(Exception ex){
		ex.printStackTrace();
		log.error("Error in ValidateSignature execute method........." +ex);
		return mapping.findForward("exception");
	}
		
		
		
	}

}

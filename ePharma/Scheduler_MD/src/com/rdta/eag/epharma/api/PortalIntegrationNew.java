/*
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
 */
package com.rdta.eag.epharma.api;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.apache.log4j.Logger;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.Log4jSetup;

import com.rdta.dhforms.CreatePDF;
import com.rdta.dhforms.SendDHForm;

public class PortalIntegrationNew {

	// private static final String ROLE = "com.rdta.eag.api.PortalIntegration";

	private static final QueryRunner queryRunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();

	static String getClassesFolderPath(Class clsObject) {
		String fullPath = PortalIntegrationNew.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();

		String className = clsObject.getName();
		String classNameChanged = replace(className, ".", "/");
		int pos;
		if ((pos = fullPath.indexOf(classNameChanged)) > 0) {
			fullPath = fullPath.substring(0, pos);
		}

		return new File(fullPath).getParentFile().getAbsolutePath();
	}

	static String replace(String org, String find, String repl) {
		StringBuffer sb = new StringBuffer(org);
		int pos;
		while ((pos = sb.indexOf(find)) >= 0) {
			sb.replace(pos, pos + find.length(), repl);
		}

		return sb.toString();
	}

	static {
		try {
			String path = getClassesFolderPath(PortalIntegrationNew.class);
			System.out.println("Set the DH Path as 124 " + path);
			System.setProperty("com.rdta.dhforms.path", path);
		} catch (Exception e) {
			System.out.println("Exception while setting the DH Path");
		}
	}

	/**
	 * 
	 * @param subscriberID
	 * @param poNumber
	 * @param deaNumber
	 * @param ndcNumber
	 * @param invoiceNumber
	 * @return
	 * @throws Exception
	 */
	public String getPedigreeForPONumberNDC(String subscriberID,
			String poNumber, String deaNumber, String ndcNumber,
			String invoiceNumber) throws Exception {

		if (checkForSubscriberID(subscriberID)) {
			List res = searchPedigrees(poNumber, deaNumber, ndcNumber,
					invoiceNumber);
			if (res.size() > 0) {

				String stringXML = createDHForms(res);

				System.out.println("\n " + stringXML);
				return stringXML;
			} else {

				System.out.println("Pedigree not found");
				return "Pedigree not found";
			}

		} else {
			System.out.println("Subscriber ID not found");
			return "Subscriber ID not found";
		}

	}

	/**
	 * 
	 * @param subscriberID
	 * @param poNumber
	 * @param deaNumber
	 * @param invoiceNumber
	 * @return
	 * @throws Exception
	 */
	public String getPedigreeForPONumber(String subscriberID, String poNumber,
			String deaNumber, String invoiceNumber) throws Exception {

		if (checkForSubscriberID(subscriberID)) {
			List res = searchPedigrees(poNumber, deaNumber, invoiceNumber);
			if (res.size() > 0) {
				String stringXML = createDHForms(res);
				System.out.println("\n " + stringXML);
				return stringXML;
			} else {
				System.out.println("Pedigree not found");
				return "Pedigree not found";
			}
		} else {
			System.out.println("Subscriber ID not found");
			return "Subscriber ID not found";
		}
	}

	/**
	 * 
	 * @param subscriberID
	 * @param invoiceID
	 * @param pedigreeEnvelopID
	 * @param pedigreeID
	 * @return
	 * @throws Exception
	 */
	public String getPedigreeDocument(String subscriberID, String invoiceID,
			String pedigreeEnvelopID, String pedigreeID) throws Exception {

		if (checkForSubscriberID(subscriberID)) {

			if (searchPedigree(pedigreeID, pedigreeEnvelopID, invoiceID)) {

				String stringXML = createDHForm(pedigreeID);

				System.out.println("\n " + stringXML);
				return stringXML;
			} else {

				System.out.println("Pedigree not found");
				return "Pedigree not found";
			}

		} else {
			System.out.println("Subscriber ID not found");
			return "Subscriber ID not found";
		}

	}

	private boolean searchPedigree(String pedigreeID, String pedigreeEnvelopID,
			String invoiceID) throws PersistanceException {

		StringBuffer buff = new StringBuffer(
				"tlsp:CheckForPedigreeExistsForPortal('" + pedigreeEnvelopID
						+ "','" + pedigreeID + "','" + invoiceID + "')");
		System.out.println(buff.toString());
		List result = queryRunner.executeQuery(buff.toString());
		if (result.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	private List searchPedigrees(String poNumber, String deaNumber,
			String ndcNumber, String invoiceNumber) throws PersistanceException {

		StringBuffer buff = new StringBuffer("tlsp:getPedigreeDocs('"
				+ poNumber + "','" + deaNumber + "','" + ndcNumber + "','"
				+ invoiceNumber + "')");
		System.out.println(buff.toString());
		List result = queryRunner.returnExecuteQueryStrings(buff.toString());
		if (result.size() > 0) {
			System.out.println("result");
			return result;
		} else {
			return result;
		}

	}

	private List searchPedigrees(String poNumber, String deaNumber,
			String invoiceNumber) throws PersistanceException {

		StringBuffer buff = new StringBuffer(
				"tlsp:getPedigreeDocsForPONumber('" + poNumber + "','"
						+ deaNumber + "','" + invoiceNumber + "')");
		System.out.println(buff.toString());
		List result = queryRunner.returnExecuteQueryStrings(buff.toString());
		if (result.size() > 0) {
			System.out.println("result");
			return result;
		} else {
			return result;
		}

	}

	private boolean checkForSubscriberID(String id) throws PersistanceException {
		StringBuffer buff = new StringBuffer(
				"tlsp:CheckForSubscriberForPortal('" + id + "')");
		System.out.println(buff.toString());
		List result = queryRunner.executeQuery(buff.toString());
		if (result.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String createDHForm(String pedigreeID) throws IOException,
			PersistanceException {

		// SendDHForm sdForm = new SendDHForm();
		CreatePDF sdForm = new CreatePDF();
		String result[] = sdForm.createPDF(pedigreeID);
		StringBuffer b = new StringBuffer("<pedigreeForm>");

		for (int i = 0; i < result.length; i++) {
			if (result[i] != null) {
				String data = convertTobinary(result[i]);
				b.append("<pedigree>" + data);
				b
						.append("<mimeType>document/pdf</mimeType><encoding>base-64</encoding></pedigree>");
			}
		}
		b.append("</pedigreeForm>");
		return b.toString();
	}

	public String createDHForms(List pedigreeIDs) throws IOException,
			PersistanceException {

		// SendDHForm sdForm = new SendDHForm();
		System.out.println("createDHForms: ");
		CreatePDF sdForm = new CreatePDF();
		try {
			System.out.println("try: ");
			sdForm.removeOldmergePDF();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuffer b = new StringBuffer("<pedigreeForm>");
		String previousfile = "";
		for (int ls = 0; ls < pedigreeIDs.size(); ls++) {
			System.out.println("List: " + pedigreeIDs.size());
			String pid = (String) pedigreeIDs.get(ls);
			String result[] = sdForm.createPDF(pid);
			if (ls == 0) {
				previousfile = result[0];
			} else {
				try {
					previousfile = sdForm.mergePDF(result[0], previousfile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println("previousfile: " + previousfile);
		if (previousfile != null) {
			String data = convertTobinary(previousfile);
			b.append("<pedigree>" + data);
			b
					.append("<mimeType>document/pdf</mimeType><encoding>base-64</encoding></pedigree>");
		}
		b.append("</pedigreeForm>");
		return b.toString();
	}

	private String convertTobinary(String stringFilepath) throws IOException,
			PersistanceException {

		StringBuffer buff = new StringBuffer(
				"tlsp:GetBinaryDataForFile('file:///" + stringFilepath + "')");
		System.out.println("GetBinary: " + buff.toString());

		String data = queryRunner.returnExecuteQueryStringsAsString(buff
				.toString());
		return data;
	}	
	
	public static void main(String args[]){
    	PortalIntegrationNew po= new PortalIntegrationNew();
    	try {
    		
    		String xml=po.getPedigreeForPONumber("757327325","20070306","RM0114481","20070306");
			System.out.println(xml);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

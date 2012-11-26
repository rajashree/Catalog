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

 
//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.rdta.epharma.epedigree.action.print;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 04-28-2006
 * 
 * XDoclet definition:
 * @struts.form name="pedigree2129Form"
 */
public class Pedigree2129Form extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	
	private String businessName;
	
	private String drugName;
	private String strength;
	private String dosageForm;
	private String containerSize;
	
	
	private String productCode;
	
	private List itemInfoList;
	
	private String identifier;
	private String identifierType;
	private String transactionDate;
	
	
	private String manufacturerName;
	private String manufacturerContactName;
	
	private List custodyInfoList;
	
	private String signerName;
	private String signerTitle;
	private String signatureId;
/*	private String initialBusinessName;
	
	private String initialBusinessAddressStreet1;
	private String initialBusinessAddressStreet2;
	private String initialBusinessAddressCity;
	private String initialBusinessAddressStateOrRegion;
	private String initialBusinessAddressPostalCode;
	private String initialBusinessAddressCountry;
	
	
	
	private String initialTransactionDate;
	private String initialIdentifierType;
	private String initialIdentifier;
	
	private String initialTransactionName;
	
	private String initialName;
	private String initialTelphone;
	private String initialEmail;*/
	

	
	
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getContainerSize() {
		return containerSize;
	}

	public void setContainerSize(String containerSize) {
		this.containerSize = containerSize;
	}

	public String getDosageForm() {
		return dosageForm;
	}

	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	/*public String getInitialBusinessAddressCity() {
		return initialBusinessAddressCity;
	}

	public void setInitialBusinessAddressCity(String initialBusinessAddressCity) {
		this.initialBusinessAddressCity = initialBusinessAddressCity;
	}

	public String getInitialBusinessAddressCountry() {
		return initialBusinessAddressCountry;
	}

	public void setInitialBusinessAddressCountry(
			String initialBusinessAddressCountry) {
		this.initialBusinessAddressCountry = initialBusinessAddressCountry;
	}

	public String getInitialBusinessAddressPostalCode() {
		return initialBusinessAddressPostalCode;
	}

	public void setInitialBusinessAddressPostalCode(
			String initialBusinessAddressPostalCode) {
		this.initialBusinessAddressPostalCode = initialBusinessAddressPostalCode;
	}

	public String getInitialBusinessAddressStateOrRegion() {
		return initialBusinessAddressStateOrRegion;
	}

	public void setInitialBusinessAddressStateOrRegion(
			String initialBusinessAddressStateOrRegion) {
		this.initialBusinessAddressStateOrRegion = initialBusinessAddressStateOrRegion;
	}

	public String getInitialBusinessAddressStreet1() {
		return initialBusinessAddressStreet1;
	}

	public void setInitialBusinessAddressStreet1(
			String initialBusinessAddressStreet1) {
		this.initialBusinessAddressStreet1 = initialBusinessAddressStreet1;
	}

	public String getInitialBusinessAddressStreet2() {
		return initialBusinessAddressStreet2;
	}

	public void setInitialBusinessAddressStreet2(
			String initialBusinessAddressStreet2) {
		this.initialBusinessAddressStreet2 = initialBusinessAddressStreet2;
	}

	public String getInitialBusinessName() {
		return initialBusinessName;
	}

	public void setInitialBusinessName(String initialBusinessName) {
		this.initialBusinessName = initialBusinessName;
	}

	public String getInitialEmail() {
		return initialEmail;
	}

	public void setInitialEmail(String initialEmail) {
		this.initialEmail = initialEmail;
	}

	public String getInitialIdentifier() {
		return initialIdentifier;
	}

	public void setInitialIdentifier(String initialIdentifier) {
		this.initialIdentifier = initialIdentifier;
	}

	public String getInitialIdentifierType() {
		return initialIdentifierType;
	}

	public void setInitialIdentifierType(String initialIdentifierType) {
		this.initialIdentifierType = initialIdentifierType;
	}

	public String getInitialName() {
		return initialName;
	}

	public void setInitialName(String initialName) {
		this.initialName = initialName;
	}

	public String getInitialTelphone() {
		return initialTelphone;
	}

	public void setInitialTelphone(String initialTelphone) {
		this.initialTelphone = initialTelphone;
	}

	public String getInitialTransactionDate() {
		return initialTransactionDate;
	}

	public void setInitialTransactionDate(String initialTransactionDate) {
		this.initialTransactionDate = initialTransactionDate;
	}

	public String getInitialTransactionName() {
		return initialTransactionName;
	}

	public void setInitialTransactionName(String initialTransactionName) {
		this.initialTransactionName = initialTransactionName;
	}*/

	public List getItemInfoList() {
		return itemInfoList;
	}

	public void setItemInfoList(List itemInfoList) {
		this.itemInfoList = itemInfoList;
	}

	public String getManufacturerContactName() {
		return manufacturerContactName;
	}

	public void setManufacturerContactName(String manufacturerContactName) {
		this.manufacturerContactName = manufacturerContactName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		// TODO Auto-generated method stub
	}

	public List getCustodyInfoList() {
		return custodyInfoList;
	}

	public void setCustodyInfoList(List custodyInfoList) {
		this.custodyInfoList = custodyInfoList;
	}

	public String getSignatureId() {
		return signatureId;
	}

	public void setSignatureId(String signatureId) {
		this.signatureId = signatureId;
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public String getSignerTitle() {
		return signerTitle;
	}

	public void setSignerTitle(String signerTitle) {
		this.signerTitle = signerTitle;
	}

}


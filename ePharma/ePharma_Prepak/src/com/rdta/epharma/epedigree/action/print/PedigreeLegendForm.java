
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

package com.rdta.epharma.epedigree.action.print;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class PedigreeLegendForm extends ActionForm {
	
	private String businessName;
	private String drugName;
	private String strength;
	private String dosageForm;
	private String containerSize;
	private String manufacturer;
	private String productCode;
	
	private String identifier;
	private String identifierType;
	private String transactionDate;
	
	
	private List repackagedLotList;
	
	private List previousInfoList;
	
	private List custodyInfoList;

	
	private String signerName;
	private String signerTitle;
	private String signatureId;
	
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

	public List getCustodyInfoList() {
		return custodyInfoList;
	}

	public void setCustodyInfoList(List custodyInfoList) {
		this.custodyInfoList = custodyInfoList;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List getPreviousInfoList() {
		return previousInfoList;
	}

	public void setPreviousInfoList(List previousInfoList) {
		this.previousInfoList = previousInfoList;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List getRepackagedLotList() {
		return repackagedLotList;
	}

	public void setRepackagedLotList(List repackagedLotList) {
		this.repackagedLotList = repackagedLotList;
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

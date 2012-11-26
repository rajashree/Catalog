
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

import org.apache.struts.action.ActionForm;

public class ProcessedEnteredPedigreesForm extends ActionForm{

	private String pedigreeId;
	private String invoiceNumber=null;
	private String vendorName=null;
	private String drugName=null;
	private String ndc=null;
	private String vendorLot=null;
	private String processedDate=null;
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public String getNdc() {
		return ndc;
	}
	public void setNdc(String ndc) {
		this.ndc = ndc;
	}
	public String getProcessedDate() {
		return processedDate;
	}
	public void setProcessedDate(String processedDate) {
		this.processedDate = processedDate;
	}
	public String getVendorLot() {
		return vendorLot;
	}
	public void setVendorLot(String vendorLot) {
		this.vendorLot = vendorLot;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getPedigreeId() {
		return pedigreeId;
	}
	public void setPedigreeId(String pedigreeId) {
		this.pedigreeId = pedigreeId;
	}
	
	

	
	
	
	
	


}

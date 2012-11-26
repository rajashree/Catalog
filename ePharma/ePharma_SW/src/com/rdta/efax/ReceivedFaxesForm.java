
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

 package com.rdta.efax;

import org.apache.struts.action.ActionForm;

public class ReceivedFaxesForm extends ActionForm{

	private String faxName = null;
	private String CSID = null;
	private String ANI = null;
	private String invoiceNumber = null;
	private String drugName = null;
	private String productCode = null;
	private String vendorName = null;
	private String dateReceived = null;
	private String processedDate = null;
	private String lots =null;
	
	
	public String getLots() {
		return lots;
	}
	public void setLots(String lots) {
		this.lots = lots;
	}
	public String getANI() {
		return ANI;
	}
	public void setANI(String ani) {
		ANI = ani;
	}
	public String getCSID() {
		return CSID;
	}
	public void setCSID(String csid) {
		CSID = csid;
	}
	public String getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getFaxName() {
		return faxName;
	}
	public void setFaxName(String faxName) {
		this.faxName = faxName;
	}
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
	public String getProcessedDate() {
		return processedDate;
	}
	public void setProcessedDate(String processedDate) {
		this.processedDate = processedDate;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	 
	
	
}

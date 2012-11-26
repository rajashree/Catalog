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

public class PedigreeDetailsForm extends ActionForm{
	
	private String rePack=null;
	private String drugName=null;
	private String productCode =null;
	private String codeType =null;
	private String manufacturer =null;
	private String quantity=null;
	private String dosageForm =null;
	private String strength =null;
	private String containerSize=null;
	private String custName=null;
	private String custAddress=null;
	private String custContact=null;
	private String custPhone=null;
	private String custEmail=null;
	private String datesInCustody=null;
	private String containersize=null;
	private String signatureInfoName=null;
	private String signatureInfoTitle=null;
	private String signatureInfoTelephone=null;
	private String signatureInfoEmail=null;
	private String signatureInfoUrl=null;
	private String signatureInfoDate=null;
	private String pedigreeId=null;
	private String transactionDate=null;
	private String transactionType=null;
	private String transactionNo=null;
	private String fromCompany=null;
	private String toCompany=null;
	private String fromShipAddress=null;
	private String fromBillAddress =null;
	private String fromContact =null;
	private String fromTitle =null;
	private String fromPhone=null;
	private String fromEmail =null;
	private String fromLicense =null;
	private String toShipAddress=null;
	private String toBillAddress=null;
	private String toContact=null;
	private String toTitle=null;
	private String toPhone=null;
	private String toEmail=null;
	private String toLicense=null;
	
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getContainersize() {
		return containersize;
	}
	public void setContainersize(String containersize) {
		this.containersize = containersize;
	}
	public String getContainerSize() {
		return containerSize;
	}
	public void setContainerSize(String containerSize) {
		this.containerSize = containerSize;
	}
	public String getCustAddress() {
		return custAddress;
	}
	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}
	public String getCustContact() {
		return custContact;
	}
	public void setCustContact(String custContact) {
		this.custContact = custContact;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustPhone() {
		return custPhone;
	}
	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}
	public String getDatesInCustody() {
		return datesInCustody;
	}
	public void setDatesInCustody(String datesInCustody) {
		this.datesInCustody = datesInCustody;
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
	public String getFromBillAddress() {
		return fromBillAddress;
	}
	public void setFromBillAddress(String fromBillAddress) {
		this.fromBillAddress = fromBillAddress;
	}
	public String getFromCompany() {
		return fromCompany;
	}
	public void setFromCompany(String fromCompany) {
		this.fromCompany = fromCompany;
	}
	public String getFromContact() {
		return fromContact;
	}
	public void setFromContact(String fromContact) {
		this.fromContact = fromContact;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getFromLicense() {
		return fromLicense;
	}
	public void setFromLicense(String fromLicense) {
		this.fromLicense = fromLicense;
	}
	public String getFromPhone() {
		return fromPhone;
	}
	public void setFromPhone(String fromPhone) {
		this.fromPhone = fromPhone;
	}
	public String getFromShipAddress() {
		return fromShipAddress;
	}
	public void setFromShipAddress(String fromShipAddress) {
		this.fromShipAddress = fromShipAddress;
	}
	public String getFromTitle() {
		return fromTitle;
	}
	public void setFromTitle(String fromTitle) {
		this.fromTitle = fromTitle;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getPedigreeId() {
		return pedigreeId;
	}
	public void setPedigreeId(String pedigreeId) {
		this.pedigreeId = pedigreeId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSignatureInfoDate() {
		return signatureInfoDate;
	}
	public void setSignatureInfoDate(String signatureInfoDate) {
		this.signatureInfoDate = signatureInfoDate;
	}
	public String getSignatureInfoEmail() {
		return signatureInfoEmail;
	}
	public void setSignatureInfoEmail(String signatureInfoEmail) {
		this.signatureInfoEmail = signatureInfoEmail;
	}
	public String getSignatureInfoName() {
		return signatureInfoName;
	}
	public void setSignatureInfoName(String signatureInfoName) {
		this.signatureInfoName = signatureInfoName;
	}
	public String getSignatureInfoTelephone() {
		return signatureInfoTelephone;
	}
	public void setSignatureInfoTelephone(String signatureInfoTelephone) {
		this.signatureInfoTelephone = signatureInfoTelephone;
	}
	public String getSignatureInfoTitle() {
		return signatureInfoTitle;
	}
	public void setSignatureInfoTitle(String signatureInfoTitle) {
		this.signatureInfoTitle = signatureInfoTitle;
	}
	public String getSignatureInfoUrl() {
		return signatureInfoUrl;
	}
	public void setSignatureInfoUrl(String signatureInfoUrl) {
		this.signatureInfoUrl = signatureInfoUrl;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getToBillAddress() {
		return toBillAddress;
	}
	public void setToBillAddress(String toBillAddress) {
		this.toBillAddress = toBillAddress;
	}
	public String getToCompany() {
		return toCompany;
	}
	public void setToCompany(String toCompany) {
		this.toCompany = toCompany;
	}
	public String getToContact() {
		return toContact;
	}
	public void setToContact(String toContact) {
		this.toContact = toContact;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getToLicense() {
		return toLicense;
	}
	public void setToLicense(String toLicense) {
		this.toLicense = toLicense;
	}
	public String getToPhone() {
		return toPhone;
	}
	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}
	public String getToShipAddress() {
		return toShipAddress;
	}
	public void setToShipAddress(String toShipAddress) {
		this.toShipAddress = toShipAddress;
	}
	public String getToTitle() {
		return toTitle;
	}
	public void setToTitle(String toTitle) {
		this.toTitle = toTitle;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getRePack() {
		return rePack;
	}
	public void setRePack(String rePack) {
		this.rePack = rePack;
	}
	
	
}

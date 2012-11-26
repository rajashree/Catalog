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
import org.apache.struts.upload.FormFile;

public class InitialPedigreeForm extends ActionForm {

	private String documentID; 
	private String name  ;
	private String Manufacturer  ;
	private String ProductCode  ;
	private String Strength  ;
	private String ContainerSize  ;
	private String LotNumber  ;
	private String Quantity  ;
	private String ContactName  ;
	private String DosageForm  ;
	private String ExpirationDate  ;
	private String TransactionId  ;
	private String TransactionDate  ;
	private String TransactionType=null  ;
	private FormFile Attachment  ;
	private String TpName  ;
	private String Address1  ;
	private String Address2  ;
	private String City  ;
	private String State  ;
	private String ZipCode  ;
	private String Country  ;
	private String itemSerialNumber ;
	 
	
	
	public String getDocumentID() {
		return documentID;
	}
	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}
	/**
	 * @return Returns the address1.
	 */
	public String getAddress1() {
		return Address1;
	}
	/**
	 * @param address1 The address1 to set.
	 */
	public void setAddress1(String address1) {
		Address1 = address1;
	}
	/**
	 * @return Returns the address2.
	 */
	public String getAddress2() {
		return Address2;
	}
	/**
	 * @param address2 The address2 to set.
	 */
	public void setAddress2(String address2) {
		Address2 = address2;
	}
	 
	 
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return City;
	}
	/**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		City = city;
	}
	/**
	 * @return Returns the contactName.
	 */
	public String getContactName() {
		return ContactName;
	}
	/**
	 * @param contactName The contactName to set.
	 */
	public void setContactName(String contactName) {
		ContactName = contactName;
	}
	/**
	 * @return Returns the containerSize.
	 */
	public String getContainerSize() {
		return ContainerSize;
	}
	/**
	 * @param containerSize The containerSize to set.
	 */
	public void setContainerSize(String containerSize) {
		ContainerSize = containerSize;
	}
	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return Country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		Country = country;
	}
	/**
	 * @return Returns the dosageForm.
	 */
	public String getDosageForm() {
		return DosageForm;
	}
	/**
	 * @param dosageForm The dosageForm to set.
	 */
	public void setDosageForm(String dosageForm) {
		DosageForm = dosageForm;
	}
	/**
	 * @return Returns the expirationDate.
	 */
	public String getExpirationDate() {
		return ExpirationDate;
	}
	/**
	 * @param expirationDate The expirationDate to set.
	 */
	public void setExpirationDate(String expirationDate) {
		ExpirationDate = expirationDate;
	}
	/**
	 * @return Returns the lotNumber.
	 */
	public String getLotNumber() {
		return LotNumber;
	}
	/**
	 * @param lotNumber The lotNumber to set.
	 */
	public void setLotNumber(String lotNumber) {
		LotNumber = lotNumber;
	}
	/**
	 * @return Returns the manufacturer.
	 */
	public String getManufacturer() {
		return Manufacturer;
	}
	/**
	 * @param manufacturer The manufacturer to set.
	 */
	public void setManufacturer(String manufacturer) {
		Manufacturer = manufacturer;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the productCode.
	 */
	public String getProductCode() {
		return ProductCode;
	}
	/**
	 * @param productCode The productCode to set.
	 */
	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}
	/**
	 * @return Returns the quantity.
	 */
	public String getQuantity() {
		return Quantity;
	}
	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}
	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return State;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		State = state;
	}
	/**
	 * @return Returns the strength.
	 */
	public String getStrength() {
		return Strength;
	}
	/**
	 * @param strength The strength to set.
	 */
	public void setStrength(String strength) {
		Strength = strength;
	}
	/**
	 * @return Returns the tpName.
	 */
	public String getTpName() {
		return TpName;
	}
	/**
	 * @param tpName The tpName to set.
	 */
	public void setTpName(String tpName) {
		TpName = tpName;
	}
	/**
	 * @return Returns the transactionDate.
	 */
	public String getTransactionDate() {
		return TransactionDate;
	}
	/**
	 * @param transactionDate The transactionDate to set.
	 */
	public void setTransactionDate(String transactionDate) {
		TransactionDate = transactionDate;
	}
	/**
	 * @return Returns the transactionId.
	 */
	public String getTransactionId() {
		return TransactionId;
	}
	/**
	 * @param transactionId The transactionId to set.
	 */
	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}
	/**
	 * @return Returns the transactionType.
	 */
	public String getTransactionType() {
		return TransactionType;
	}
	/**
	 * @param transactionType The transactionType to set.
	 */
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	/**
	 * @return Returns the zipCode.
	 */
	public String getZipCode() {
		return ZipCode;
	}
	/**
	 * @param zipCode The zipCode to set.
	 */
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}
	/**
	 * @return Returns the attachment.
	 */
	public FormFile getAttachment() {
		return Attachment;
	}
	/**
	 * @param attachment The attachment to set.
	 */
	public void setAttachment(FormFile attachment) {
		Attachment = attachment;
	}
	public String getItemSerialNumber() {
		return itemSerialNumber;
	}
	public void setItemSerialNumber(String itemSerialNumber) {
		this.itemSerialNumber = itemSerialNumber;
	}
}

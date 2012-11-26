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

public class PreviousInfo {
	
	
	private String lot; //Products/itemInfo
	private String quantity;
	private String manufacturer;
	private String contactInfoName;
	private String contactInfoTitle;
	private String contactInfoTelphone;
	private String contactInfoEmail;
	private String contactInfoURL;
	private String productCode;
	private String itemSerialNumber;
	
	
	public String getContactInfoEmail() {
		return contactInfoEmail;
	}
	public void setContactInfoEmail(String contactInfoEmail) {
		this.contactInfoEmail = contactInfoEmail;
	}
	public String getContactInfoName() {
		return contactInfoName;
	}
	public void setContactInfoName(String contactInfoName) {
		this.contactInfoName = contactInfoName;
	}
	public String getContactInfoTelphone() {
		return contactInfoTelphone;
	}
	public void setContactInfoTelphone(String contactInfoTelphone) {
		this.contactInfoTelphone = contactInfoTelphone;
	}
	public String getContactInfoTitle() {
		return contactInfoTitle;
	}
	public void setContactInfoTitle(String contactInfoTitle) {
		this.contactInfoTitle = contactInfoTitle;
	}
	public String getContactInfoURL() {
		return contactInfoURL;
	}
	public void setContactInfoURL(String contactInfoURL) {
		this.contactInfoURL = contactInfoURL;
	}
	public String getItemSerialNumber() {
		return itemSerialNumber;
	}
	public void setItemSerialNumber(String itemSerialNumber) {
		this.itemSerialNumber = itemSerialNumber;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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
	
	
	
	

}

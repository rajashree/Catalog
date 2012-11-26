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

public class InvoiceDetailsForm extends ActionForm{
	
	private String invoiceNumber=null;
	private String sellersID =null;
	private String invoiceDate =null;
	private String requestedDeliveryDate =null;
	private String buyerPartyName=null;
	private String buyerAddress =null;
	private String buyerContact =null;
	private String sellerPartyName=null;
	private String sellerAddress=null;
	private String itemIdentificationNumber =null;
	private String itemDescription =null;
	private String quantityOrdered=null;
	private String invoiceAmount=null;
	
	public String getBuyerAddress() {
		return buyerAddress;
	}
	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}
	public String getBuyerContact() {
		return buyerContact;
	}
	public void setBuyerContact(String buyerContact) {
		this.buyerContact = buyerContact;
	}
	public String getBuyerPartyName() {
		return buyerPartyName;
	}
	public void setBuyerPartyName(String buyerPartyName) {
		this.buyerPartyName = buyerPartyName;
	}
	public String getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getItemIdentificationNumber() {
		return itemIdentificationNumber;
	}
	public void setItemIdentificationNumber(String itemIdentificationNumber) {
		this.itemIdentificationNumber = itemIdentificationNumber;
	}
	public String getQuantityOrdered() {
		return quantityOrdered;
	}
	public void setQuantityOrdered(String quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	public String getRequestedDeliveryDate() {
		return requestedDeliveryDate;
	}
	public void setRequestedDeliveryDate(String requestedDeliveryDate) {
		this.requestedDeliveryDate = requestedDeliveryDate;
	}
	public String getSellerAddress() {
		return sellerAddress;
	}
	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
	public String getSellerPartyName() {
		return sellerPartyName;
	}
	public void setSellerPartyName(String sellerPartyName) {
		this.sellerPartyName = sellerPartyName;
	}
	public String getSellersID() {
		return sellersID;
	}
	public void setSellersID(String sellersID) {
		this.sellersID = sellersID;
	}


}

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

public class CustodyInfo {

	//Business Address from transaction data....
	
	private String pedigreeID;
	private String businessName;
	private String businessAddressStreet1;
	private String businessAddressStreet2;
	private String businessAddressCity;
	private String businessAddressStateOrRegion;
	private String businessAddressPostalCode;
	private String businessAddressCountry;
	
	
	
	private String transactionDate;
	private String identifierType;
	private String identifier;
	
	private String transactionName;
	
	private String name;
	private String telphone;
	private String email;
	
	private String recipientName;
	private String authenticatorName;
	//shipping from transaction data....
	
	private String shippingBusinessName;
	
	private String shippingBusinessAddressStreet1;
	private String shippingBusinessAddressStreet2;
	private String shippingBusinessAddressCity;
	private String shippingBusinessAddressStateOrRegion;
	private String shippingBusinessAddressPostalCode;
	private String shippingBusinessAddressCountry;
	
	
	
	private String shippingTransactionDate;
	private String shippingIdentifierType;
	private String shippingIdentifier;
	
	private String shippingTransactionName;
	
	private String shippingName;
	private String shippingTelphone;
	private String shippingEmail;
	
	private String shippingRecipientName;
	private String shippingAuthenticatorName;
	
	public String getBusinessAddressCity() {
		return businessAddressCity;
	}
	public void setBusinessAddressCity(String businessAddressCity) {
		this.businessAddressCity = businessAddressCity;
	}
	public String getBusinessAddressCountry() {
		return businessAddressCountry;
	}
	public void setBusinessAddressCountry(String businessAddressCountry) {
		this.businessAddressCountry = businessAddressCountry;
	}
	public String getBusinessAddressPostalCode() {
		return businessAddressPostalCode;
	}
	public void setBusinessAddressPostalCode(String businessAddressPostalCode) {
		this.businessAddressPostalCode = businessAddressPostalCode;
	}
	public String getBusinessAddressStateOrRegion() {
		return businessAddressStateOrRegion;
	}
	public void setBusinessAddressStateOrRegion(String businessAddressStateOrRegion) {
		this.businessAddressStateOrRegion = businessAddressStateOrRegion;
	}
	public String getBusinessAddressStreet1() {
		return businessAddressStreet1;
	}
	public void setBusinessAddressStreet1(String businessAddressStreet1) {
		this.businessAddressStreet1 = businessAddressStreet1;
	}
	public String getBusinessAddressStreet2() {
		return businessAddressStreet2;
	}
	public void setBusinessAddressStreet2(String businessAddressStreet2) {
		this.businessAddressStreet2 = businessAddressStreet2;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShippingBusinessAddressCity() {
		return shippingBusinessAddressCity;
	}
	public void setShippingBusinessAddressCity(String shippingBusinessAddressCity) {
		this.shippingBusinessAddressCity = shippingBusinessAddressCity;
	}
	public String getShippingBusinessAddressCountry() {
		return shippingBusinessAddressCountry;
	}
	public void setShippingBusinessAddressCountry(
			String shippingBusinessAddressCountry) {
		this.shippingBusinessAddressCountry = shippingBusinessAddressCountry;
	}
	public String getShippingBusinessAddressPostalCode() {
		return shippingBusinessAddressPostalCode;
	}
	public void setShippingBusinessAddressPostalCode(
			String shippingBusinessAddressPostalCode) {
		this.shippingBusinessAddressPostalCode = shippingBusinessAddressPostalCode;
	}
	public String getShippingBusinessAddressStateOrRegion() {
		return shippingBusinessAddressStateOrRegion;
	}
	public void setShippingBusinessAddressStateOrRegion(
			String shippingBusinessAddressStateOrRegion) {
		this.shippingBusinessAddressStateOrRegion = shippingBusinessAddressStateOrRegion;
	}
	public String getShippingBusinessAddressStreet1() {
		return shippingBusinessAddressStreet1;
	}
	public void setShippingBusinessAddressStreet1(
			String shippingBusinessAddressStreet1) {
		this.shippingBusinessAddressStreet1 = shippingBusinessAddressStreet1;
	}
	public String getShippingBusinessAddressStreet2() {
		return shippingBusinessAddressStreet2;
	}
	public void setShippingBusinessAddressStreet2(
			String shippingBusinessAddressStreet2) {
		this.shippingBusinessAddressStreet2 = shippingBusinessAddressStreet2;
	}
	public String getShippingBusinessName() {
		return shippingBusinessName;
	}
	public void setShippingBusinessName(String shippingBusinessName) {
		this.shippingBusinessName = shippingBusinessName;
	}
	public String getShippingEmail() {
		return shippingEmail;
	}
	public void setShippingEmail(String shippingEmail) {
		this.shippingEmail = shippingEmail;
	}
	public String getShippingIdentifier() {
		return shippingIdentifier;
	}
	public void setShippingIdentifier(String shippingIdentifier) {
		this.shippingIdentifier = shippingIdentifier;
	}
	public String getShippingIdentifierType() {
		return shippingIdentifierType;
	}
	public void setShippingIdentifierType(String shippingIdentifierType) {
		this.shippingIdentifierType = shippingIdentifierType;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getShippingTelphone() {
		return shippingTelphone;
	}
	public void setShippingTelphone(String shippingTelphone) {
		this.shippingTelphone = shippingTelphone;
	}
	public String getShippingTransactionDate() {
		return shippingTransactionDate;
	}
	public void setShippingTransactionDate(String shippingTransactionDate) {
		this.shippingTransactionDate = shippingTransactionDate;
	}
	public String getShippingTransactionName() {
		return shippingTransactionName;
	}
	public void setShippingTransactionName(String shippingTransactionName) {
		this.shippingTransactionName = shippingTransactionName;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	public String getPedigreeID() {
		return pedigreeID;
	}
	public void setPedigreeID(String pedigreeID) {
		this.pedigreeID = pedigreeID;
	}
	public String getAuthenticatorName() {
		return authenticatorName;
	}
	public void setAuthenticatorName(String authenticatorName) {
		this.authenticatorName = authenticatorName;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getShippingAuthenticatorName() {
		return shippingAuthenticatorName;
	}
	public void setShippingAuthenticatorName(String shippingAuthenticatorName) {
		this.shippingAuthenticatorName = shippingAuthenticatorName;
	}
	public String getShippingRecipientName() {
		return shippingRecipientName;
	}
	public void setShippingRecipientName(String shippingRecipientName) {
		this.shippingRecipientName = shippingRecipientName;
	}
	
	
	
	
	
}

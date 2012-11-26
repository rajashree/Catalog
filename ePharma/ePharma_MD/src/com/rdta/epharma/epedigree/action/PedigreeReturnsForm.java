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

public class PedigreeReturnsForm extends ActionForm{

	private String pedigreeid=null;
	private String envelopeID =null;
	private String date =null;
	private String source =null;
	private String destination=null;
	private String transactiontype =null;
    private String trnsaction =null;
    private String NDC = null;
    private String lotNumber = null;
    private String quantity = null;
    private String productName = null;
    
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getNDC() {
		return NDC;
	}
	public void setNDC(String ndc) {
		NDC = ndc;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getEnvelopeID() {
		return envelopeID;
	}
	public void setEnvelopeID(String envelopeID) {
		this.envelopeID = envelopeID;
	}
	public String getPedigreeid() {
		return pedigreeid;
	}
	public void setPedigreeid(String pedigreeid) {
		this.pedigreeid = pedigreeid;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTransactiontype() {
		return transactiontype;
	}
	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}
	public String getTrnsaction() {
		return trnsaction;
	}
	public void setTrnsaction(String trnsaction) {
		this.trnsaction = trnsaction;
	}
}

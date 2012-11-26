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

 
/*
 * Created on Sep 12, 2005
 *
 *Santosh Subrahmanya
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.epedigree.action;

import org.apache.struts.action.ActionForm;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReceivingManagerForm extends ActionForm{
	
	
	private String select = null;
	private String pagenm = null;
	private String pedigreeNum = null;
	private String trNum = null;
	private String dataRcvd = null;
	private String trdPrtnr = null;
	private String  trType= null;
	private String envolope = null;
	private String Source = null;
	private String Destination = null;
	private String Numofproducts = null;
	private String ProductsInShipment = null;
	private String NumOfPedigrees = null;
	private String status = null;
	private String createdBy = null;
	private String pedId = null;
	private String sessionId = null;
	private String screenEnteredDate = null;
	private String tp_company_nm = null;
	private String transactionType = null;
	private String containercode = null;
	private String prodNDC=null;
	private String lotNum = null;
	private String envelopeId = null;
	private String fromDT = null;
	private String toDT = null;
	protected String fromDate = null;
	
	public String getContainercode() {
		return containercode;
	}
	public void setContainercode(String containercode) {
		this.containercode = containercode;
	}
	public String getEnvelopeId() {
		return envelopeId;
	}
	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}
	public String getFromDT() {
		return fromDT;
	}
	public void setFromDT(String fromDT) {
		this.fromDT = fromDT;
	}
	public String getLotNum() {
		return lotNum;
	}
	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	public String getProdNDC() {
		return prodNDC;
	}
	public void setProdNDC(String prodNDC) {
		this.prodNDC = prodNDC;
	}
	public String getToDT() {
		return toDT;
	}
	public void setToDT(String toDT) {
		this.toDT = toDT;
	}
	/**
	 * @return Returns the transactionType.
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType The transactionType to set.
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	/**
	 * @return Returns the tp_company_nm.
	 */
	public String getTp_company_nm() {
		return tp_company_nm;
	}
	/**
	 * @param tp_company_nm The tp_company_nm to set.
	 */
	public void setTp_company_nm(String tp_company_nm) {
		this.tp_company_nm = tp_company_nm;
	}
	/**
	 * @return Returns the pagenm.
	 */


	
		/**
	 * @return Returns the trNum.
	 */
	
/**
	 * @return Returns the screenEnteredDate.
	 */
	public String getScreenEnteredDate() {
		return screenEnteredDate;
	}
	/**
	 * @param screenEnteredDate The screenEnteredDate to set.
	 */
	public void setScreenEnteredDate(String screenEnteredDate) {
		this.screenEnteredDate = screenEnteredDate;
	}
	/**
	 * @return Returns the createdBy.
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return Returns the dataRcvd.
	 */
	public String getDataRcvd() {
		return dataRcvd;
	}
	/**
	 * @param dataRcvd The dataRcvd to set.
	 */
	public void setDataRcvd(String dataRcvd) {
		this.dataRcvd = dataRcvd;
	}
	/**
	 * @return Returns the orderNum.
	 */
	public String gettrNum() {
		return trNum;
	}
	/**
	 * @param orderNum The orderNum to set.
	 */
	public void settrNum(String trNum) {
		this.trNum = trNum;
	}
	/**
	 * @return Returns the pedigreeNum.
	 */
	public String getPedigreeNum() {
		return pedigreeNum;
	}
	/**
	 * @param pedigreeNum The pedigreeNum to set.
	 */
	public void setPedigreeNum(String pedigreeNum) {
		this.pedigreeNum = pedigreeNum;
	}
	/**
	 * @return Returns the product.
	 */
	
	/**
	 * @return Returns the quantity.
	 */
	
	/**
	 * @return Returns the select.
	 */
	public String getSelect() {
		return select;
	}
	/**
	 * @param select The select to set.
	 */
	public void setSelect(String select) {
		this.select = select;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the trdPrtnr.
	 */
	public String getTrdPrtnr() {
		return trdPrtnr;
	}
	/**
	 * @param trdPrtnr The trdPrtnr to set.
	 */
	public void setTrdPrtnr(String trdPrtnr) {
		this.trdPrtnr = trdPrtnr;
	}
	/**
	 * @return Returns the pedId.
	 */
	public String getPedId() {
		return pedId;
	}
	/**
	 * @param pedId The pedId to set.
	 */
	public void setPedId(String pedId) {
		this.pedId = pedId;
		System.out.println("Inside Action Form Receiving Manager");
		System.out.println("The value of pedId is:"+pedId);
		
	}
	/**
	 * @return Returns the sessionId.
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId The sessionId to set.
	 */
	public void setSessionId(String sessionId) {
		System.out.println("Inside set SessionID******************");
		this.sessionId = sessionId;
	}

	/**
	 * @return Returns the numOfPedigrees.
	 */
	public String getNumOfPedigrees() {
		return NumOfPedigrees;
	}
	/**
	 * @param numOfPedigrees The numOfPedigrees to set.
	 */
	public void setNumOfPedigrees(String numOfPedigrees) {
		NumOfPedigrees = numOfPedigrees;
	}
	/**
	 * @return Returns the productsInShipment.
	 */
	public String getProductsInShipment() {
		return ProductsInShipment;
	}
	/**
	 * @param productsInShipment The productsInShipment to set.
	 */
	public void setProductsInShipment(String productsInShipment) {
		ProductsInShipment = productsInShipment;
	}
	/**
	 * @return Returns the pagenm.
	 */
	public String getPagenm() {
		return pagenm;
	}
	/**
	 * @param pagenm The pagenm to set.
	 */
	public void setPagenm(String pagenm) {
		this.pagenm = pagenm;
	}
	public String getTrNum() {
		return trNum;
	}
	public void setTrNum(String trNum) {
		this.trNum = trNum;
	}
	public String getTrType() {
		return trType;
	}
	public void setTrType(String trType) {
		this.trType = trType;
	}
	public String getDestination() {
		return Destination;
	}
	public void setDestination(String destination) {
		Destination = destination;
	}
	public String getEnvolope() {
		return envolope;
	}
	public void setEnvolope(String envolope) {
		this.envolope = envolope;
	}
	public String getNumofproducts() {
		return Numofproducts;
	}
	public void setNumofproducts(String numofproducts) {
		Numofproducts = numofproducts;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
}


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

public class POsSearchForm extends ActionForm{
	
	private String PONum = null;
	private String select = null;
	private String pagenm = null;
	private String pedigreeNum = null;
	private String orderNum = null;
	private String dataRcvd = null;
	private String trdPrtnr = null;
	private String product = null;
	private String quantity = null;
	private String status = null;
	private String createdBy = null;
	private String pedId = null;
	private String sessionId = null;
	private String screenEnteredDate = null;
	private String tp_company_nm = null;
	private String transactionType = null;
		
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
	public String getPagenm() {
		return pagenm;
	}
	/**
	 * @param pagenm The pagenm to set.
	 */
	public void setPagenm(String pagenm) {
		this.pagenm = pagenm;
	}
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
	public String getOrderNum() {
		return orderNum;
	}
	/**
	 * @param orderNum The orderNum to set.
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
	public String getProduct() {
		return product;
	}
	/**
	 * @param product The product to set.
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * @return Returns the quantity.
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
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
	public String getPONum() {
		return PONum;
	}
	public void setPONum(String num) {
		PONum = num;
	}

}


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

public class APNSearchForm extends ActionForm {
	
	private String select = null;
	private String pagenm = null;
	private String pedigreeId = null;
	private String envelopeId = null;
	private String dataRcvd = null;
	private String trdPrtnr = null;
	private String trNum = null;
	private String status = null;
	private String tp_company_nm = null;
	private String createdBy = null;
	private String pedstate = null;
	private String sessionId = null;
	private String screenEnteredDate = null;
		
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

	
	public String getEnvelopeId() {
		return envelopeId;
	}
	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}
	public String getPedigreeId() {
		return pedigreeId;
	}
	public void setPedigreeId(String pedigreeId) {
		this.pedigreeId = pedigreeId;
	}
	public String getTrNum() {
		return trNum;
	}
	public void setTrNum(String trNum) {
		this.trNum = trNum;
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
	public String getTp_company_nm() {
		return tp_company_nm;
	}
	public void setTp_company_nm(String tp_company_nm) {
		this.tp_company_nm = tp_company_nm;
	}
	public String getPedstate() {
		return pedstate;
	}
	public void setPedstate(String pedstate) {
		this.pedstate = pedstate;
	}	
}

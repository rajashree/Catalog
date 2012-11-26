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
 * Created on Oct 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.epedigree.action;

import org.apache.struts.action.ActionForm;

/**
 * @author 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShipingManagerForm extends ActionForm{
	
	private String select = null;
	private String trNum = null;
	//private String product = null;
	private String quantity = null;
	private String status = null;
	//private String createdBy = null;
	private String pedId = null;
	private String sessionId = null;
	private String dataRcvd = null;
	private String pedigreeNum = null;
	private String trdPrtnr = null;
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
	 * @return Returns the createdBy.
	 */
	
	/**
	 * @return Returns the trNum .
	 */
	public String gettrNum () {
		return trNum ;
	}
	/**
	 * @param trNum  The trNum  to set.
	 */
	public void settrNum (String trNum ) {
		this.trNum  = trNum ;
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
	}
	/**
	 * @return Returns the product.
	 */
	
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
	 * @return Returns the sessionId.
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId The sessionId to set.
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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
}


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

public class PedigreeStatusForm extends ActionForm{

	
	private String pedigreeid=null;
	private String envelopeID =null;
	private String date =null;
	private String source =null;
	private String destination=null;
	private String transactiontype =null;
    private String trnsaction =null;
    private String statusDate = null;
    private String statusValue = null;
    private String userId = null;
    FormFile attachFile;
    private String filePath = null;
    private String mimeType=null;
    
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public String getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public FormFile getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(FormFile attachFile) {
		this.attachFile = attachFile;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	
    
    
}

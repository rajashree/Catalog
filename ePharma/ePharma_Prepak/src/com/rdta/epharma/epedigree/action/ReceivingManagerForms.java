
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

public class ReceivingManagerForms extends ActionForm{

	private String name="";
	private String fromDt = null;
	private String toDt = null;
	private String containerCode = null;
	private String prodNDC = null;
	private String lotNum = null;
	private String trNum = null;
	private String pedId =null;
	private String envelopeId =null;
	private String status = null;
	private String tpName = null;
	private String dateReceived=null;
	private String source = null;
	private String destination = null;
	private String numOfPedigrees = null;
	public String getContainerCode() {
		return containerCode;
	}
	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}
	public String getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getEnvelopeId() {
		return envelopeId;
	}
	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}
	public String getFromDt() {
		return fromDt;
	}
	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}
	public String getLotNum() {
		return lotNum;
	}
	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumOfPedigrees() {
		return numOfPedigrees;
	}
	public void setNumOfPedigrees(String numOfPedigrees) {
		this.numOfPedigrees = numOfPedigrees;
	}
	public String getPedId() {
		return pedId;
	}
	public void setPedId(String pedId) {
		this.pedId = pedId;
	}
	public String getProdNDC() {
		return prodNDC;
	}
	public void setProdNDC(String prodNDC) {
		this.prodNDC = prodNDC;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToDt() {
		return toDt;
	}
	public void setToDt(String toDt) {
		this.toDt = toDt;
	}
	public String getTpName() {
		return tpName;
	}
	public void setTpName(String tpName) {
		this.tpName = tpName;
	}
	public String getTrNum() {
		return trNum;
	}
	public void setTrNum(String trNum) {
		this.trNum = trNum;
	}
	
}

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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

public class EnvelopeForm extends ActionForm{
	private String containercode = null;
	private String pedigreeID = null;
	private String quantity = null;
	private String lotnumber = null;
	private String pedigreetype = null;
	private String transactiondate = null;
	private String drugname = null;
	private String drugcode = null;
	private String source= null;
	private String destination = null;
	private String date= null;
	private String envelopeID = null;
	private String attachment = null;
	private String count = null;

	private String status = null;

	public String getContainercode() {
		return containercode;
	}
	public void setContainercode(String containercode) {
		if(StringUtils.isEmpty(containercode))
			this.containercode="N/A";
		else
		this.containercode = containercode;
	}
	public String getLotnumber() {
		return lotnumber;
	}
	public void setLotnumber(String lotnumber) {
		if(StringUtils.isEmpty(lotnumber))
			this.lotnumber="N/A";
		else
		this.lotnumber = lotnumber;
	}
	public String getPedigreeID() {
		return pedigreeID;
	}
	public void setPedigreeID(String pedigreeID) {
		if(StringUtils.isEmpty(pedigreeID))
			this.pedigreeID="N/A";
		else
		this.pedigreeID = pedigreeID;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		if(StringUtils.isEmpty(quantity))
			this.quantity="N/A";
		else
		this.quantity = quantity;
	}
	
	
	public String getDrugcode() {
		return drugcode;
	}
	public void setDrugcode(String drugcode) {
		this.drugcode = drugcode;
	}
	public String getDrugname() {
		return drugname;
	}
	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}
	public String getPedigreetype() {
		return pedigreetype;
	}
	public void setPedigreetype(String pedigreetype) {
		this.pedigreetype = pedigreetype;
	}
	public String getTransactiondate() {
		return transactiondate;
	}
	public void setTransactiondate(String transactiondate) {
		this.transactiondate = transactiondate;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		if(StringUtils.isEmpty(attachment))
			this.attachment="N/A";
		else
		this.attachment = attachment;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}

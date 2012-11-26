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


public class EpedigreeForm extends ActionForm{
	
	private String select = null;
	private String RelatedProcess = null;
	private String MessageID = null;
	private String CreatedDate = null;
	private String Status = null;
	private String SeverityLevel = null;
	private String  CreatedBy = null;
	private String MessageTitle = null;
	
	public String getMessageTitle() {
		return MessageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		MessageTitle = messageTitle;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public String getCreatedDate() {
		return CreatedDate;
	}
	public void setCreatedDate(String createdDate) {
		CreatedDate = createdDate;
	}
	public String getMessageID() {
		return MessageID;
	}
	public void setMessageID(String messageID) {
		MessageID = messageID;
	}
	public String getRelatedProcess() {
		return RelatedProcess;
	}
	public void setRelatedProcess(String relatedProcess) {
		RelatedProcess = relatedProcess;
	}
	public String getSelect() {
		return select;
	}
	public void setSelect(String select) {
		this.select = select;
	}
	public String getSeverityLevel() {
		return SeverityLevel;
	}
	public void setSeverityLevel(String severityLevel) {
		SeverityLevel = severityLevel;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}	
	
}

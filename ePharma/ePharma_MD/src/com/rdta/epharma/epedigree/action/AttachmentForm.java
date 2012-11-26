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

public class AttachmentForm extends ActionForm{
private String mimeType=null;
private String pedigreeId=null;
private String transactionDate=null;
private String transactionType=null;
private String transactionNo=null;



public String getPedigreeId() {
	return pedigreeId;
}

public void setPedigreeId(String pedigreeId) {
	this.pedigreeId = pedigreeId;
}

public String getTransactionDate() {
	return transactionDate;
}

public void setTransactionDate(String transactionDate) {
	this.transactionDate = transactionDate;
}

public String getTransactionNo() {
	return transactionNo;
}

public void setTransactionNo(String transactionNo) {
	this.transactionNo = transactionNo;
}

public String getTransactionType() {
	return transactionType;
}

public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
}

public String getMimeType() {
	return mimeType;
}

public void setMimeType(String mimeType) {
	this.mimeType = mimeType;
}
}


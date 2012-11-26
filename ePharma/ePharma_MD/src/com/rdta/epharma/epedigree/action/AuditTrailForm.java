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

public class AuditTrailForm extends ActionForm{

	private String pedigreeId=null;
	private String pedigreetype=null;
	private String trasactiondate=null;
	private String trnsactiontype=null;
	private String productqty=null;
	private String transactionNo=null;
	private String signature=null;

	public String getPedigreeId() {
		return pedigreeId;
	}

	public void setPedigreeId(String pedigreeId) {
		this.pedigreeId = pedigreeId;
	}

	
	public String getPedigreetype() {
		return pedigreetype;
	}

	public void setPedigreetype(String pedigreetype) {
		this.pedigreetype = pedigreetype;
	}

	public String getProductqty() {
		return productqty;
	}

	public void setProductqty(String productqty) {
		if(StringUtils.isEmpty(productqty))
			this.productqty="N/A";
		else
		this.productqty = productqty;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTrnsactiontype() {
		return trnsactiontype;
	}

	public void setTrnsactiontype(String trnsactiontype) {
		this.trnsactiontype = trnsactiontype;
	}

	public String getTrasactiondate() {
		return trasactiondate;
	}

	public void setTrasactiondate(String trasactiondate) {
		this.trasactiondate = trasactiondate;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
}

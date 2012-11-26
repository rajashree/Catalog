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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class OrderSearchForm extends ActionForm {
	
	private String orderNum = null;
	private String tradingPartner = null;
	private String address = null;
	private String issueDate = null;
	private String NumOfNDCs = null;
	private String NumOfLineItems = null;
	private String amount = null;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getNumOfLineItems() {
		return NumOfLineItems;
	}
	public void setNumOfLineItems(String numOfLineItems) {
		NumOfLineItems = numOfLineItems;
	}
	public String getNumOfNDCs() {
		return NumOfNDCs;
	}
	public void setNumOfNDCs(String numOfNDCs) {
		NumOfNDCs = numOfNDCs;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getTradingPartner() {
		return tradingPartner;
	}
	public void setTradingPartner(String tradingPartner) {
		this.tradingPartner = tradingPartner;
	}
	
	
}

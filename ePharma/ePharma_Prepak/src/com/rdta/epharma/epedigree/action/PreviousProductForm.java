
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

public class PreviousProductForm extends ActionForm {
	private String manuFacture=null;
	private String productCode=null;
	private String previousLot=null;
	private String previousExpDate=null;
	private String newNDC=null;
	private String quantity=null;
	private String strength=null;
	private String containerSize=null;
	public String getContainerSize() {
		return containerSize;
	}
	public void setContainerSize(String containerSize) {
		this.containerSize = containerSize;
	}
	public String getNewNDC() {
		return newNDC;
	}
	public void setNewNDC(String newNDC) {
		this.newNDC = newNDC;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getManuFacture() {
		return manuFacture;
	}
	public void setManuFacture(String manuFacture) {
		this.manuFacture = manuFacture;
	}
	public String getPreviousExpDate() {
		return previousExpDate;
	}
	public void setPreviousExpDate(String previousExpDate) {
		this.previousExpDate = previousExpDate;
	}
	public String getPreviousLot() {
		return previousLot;
	}
	public void setPreviousLot(String previousLot) {
		this.previousLot = previousLot;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}

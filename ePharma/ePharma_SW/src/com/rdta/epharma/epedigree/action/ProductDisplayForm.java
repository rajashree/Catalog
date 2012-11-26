
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

public class ProductDisplayForm extends ActionForm{
	
	private String epc = null;
	private String upc = null;
	private String ndc = null;
	private String mfrName = null;
	private String pbc = null;
	private String mfrDate = null;
	private String dosageForm = null;
	private String dosageStrength = null;
	private String prodName = null;
	public String getDosageForm() {
		return dosageForm;
	}
	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}
	public String getDosageStrength() {
		return dosageStrength;
	}
	public void setDosageStrength(String dosageStrength) {
		this.dosageStrength = dosageStrength;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getMfrDate() {
		return mfrDate;
	}
	public void setMfrDate(String mfrDate) {
		this.mfrDate = mfrDate;
	}
	public String getMfrName() {
		return mfrName;
	}
	public void setMfrName(String mfrName) {
		this.mfrName = mfrName;
	}
	public String getNdc() {
		return ndc;
	}
	public void setNdc(String ndc) {
		this.ndc = ndc;
	}
	public String getPbc() {
		return pbc;
	}
	public void setPbc(String pbc) {
		this.pbc = pbc;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	
	
	

}

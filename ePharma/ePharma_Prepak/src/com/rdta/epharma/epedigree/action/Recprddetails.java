
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

public class Recprddetails extends ActionForm{
	private String ndc =null;
	private String epc =null;
	private String upc =null;
	private String pbc =null;
	private String lot =null;
	private String mfgdate =null;
	private String pdcexpdate =null;
	private String lotexpdate =null;
	private String mfg =null;
	private String doseForm ="";
	private String dosestr =null;
	private String paksize =null;
	private String dec =null;
	private String makstatus =null;
	private String quantity =null;
	private String pdcId =null;
	private String drugName =null;
	private String threshold =null;
	
	public String getDec() {
		
		return dec;
	}
	public void setDec(String dec) {
		if(StringUtils.isEmpty(dec))
			this.dec="";
		else
		this.dec = dec;
	}
	
	public String getDoseForm() {
		return doseForm;
	}
	public void setDoseForm(String doseForm) {
		if(StringUtils.isEmpty(doseForm))
			this.doseForm="";
		else
			this.doseForm = doseForm;
	}
	public String getDosestr() {
		return dosestr;
	}
	public void setDosestr(String dosestr) {
		if(StringUtils.isEmpty(dosestr))
			this.dosestr="N/A";
		else
		this.dosestr = dosestr;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		if(StringUtils.isEmpty(epc))
			this.epc="N/A";
		else
		this.epc = epc;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		if(StringUtils.isEmpty(lot))
			this.lot="N/A";
		else
		this.lot = lot;
	}
	public String getLotexpdate() {
		return lotexpdate;
	}
	public void setLotexpdate(String lotexpdate) {
		if(StringUtils.isEmpty(lotexpdate))
			this.lotexpdate="N/A";
		else
		this.lotexpdate = lotexpdate;
	}
	public String getMakstatus() {
		return makstatus;
	}
	public void setMakstatus(String makstatus) {
		if(StringUtils.isEmpty(makstatus))
			this.makstatus="N/A";
		else
		this.makstatus = makstatus;
	}
	public String getMfg() {
		return mfg;
	}
	public void setMfg(String mfg) {
		if(StringUtils.isEmpty(mfg))
			this.mfg="N/A";
		else
		this.mfg = mfg;
	}
	public String getMfgdate() {
		return mfgdate;
	}
	public void setMfgdate(String mfgdate) {
		if(StringUtils.isEmpty(mfgdate))
			this.mfgdate="N/A";
		else
		this.mfgdate = mfgdate;
	}
	public String getNdc() {
		return ndc;
	}
	public void setNdc(String ndc) {
		if(StringUtils.isEmpty(ndc))
			this.ndc="N/A";
		else
		this.ndc = ndc;
	}
	public String getPaksize() {
		return paksize;
	}
	public void setPaksize(String paksize) {
		if(StringUtils.isEmpty(paksize))
			this.paksize="N/A";
		else
		this.paksize = paksize;
	}
	public String getPbc() {
		return pbc;
	}
	public void setPbc(String pbc) {
		if(StringUtils.isEmpty(pbc))
			this.pbc="N/A";
		else
		this.pbc = pbc;
	}
	public String getPdcexpdate() {
		return pdcexpdate;
	}
	public void setPdcexpdate(String pdcexpdate) {
		if(StringUtils.isEmpty(pdcexpdate))
			this.pdcexpdate="N/A";
		else
		this.pdcexpdate = pdcexpdate;
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
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		if(StringUtils.isEmpty(threshold))
			this.threshold="N/A";
		else
		this.threshold = threshold;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		if(StringUtils.isEmpty(upc))
			this.upc="N/A";
		else
		this.upc = upc;
	}
	public String getPdcId() {
		return pdcId;
	}
	public void setPdcId(String pdcId) {
		if(StringUtils.isEmpty(pdcId))
			this.pdcId="N/A";
		else
		this.pdcId = pdcId;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		if(StringUtils.isEmpty(drugName))
			this.drugName="N/A";
		else
		this.drugName = drugName;
	}

}

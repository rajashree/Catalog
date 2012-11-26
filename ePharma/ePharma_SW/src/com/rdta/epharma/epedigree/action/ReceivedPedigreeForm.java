
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

public class ReceivedPedigreeForm extends ActionForm{
	
	private String pedigreeid=null;
	private String daterecvd=null;
	private String lotnum=null;
	private String quantity=null;
	private String expirationdate=null;
	private String itemserialnumber=null;
	private String signame=null;
	private String title=null;
	private String sigemail=null;
	private String telephone=null;
	private String url=null;
	private String signaturedate=null;
	
	public String getExpirationdate() {
		return expirationdate;
	}
	public void setExpirationdate(String expirationdate) {
		this.expirationdate = expirationdate;
	}
	public String getItemserialnumber() {
		return itemserialnumber;
	}
	public void setItemserialnumber(String itemserialnumber) {
		this.itemserialnumber = itemserialnumber;
	}
	public String getLotnum() {
		return lotnum;
	}
	public void setLotnum(String lotnum) {
		this.lotnum = lotnum;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSigemail() {
		return sigemail;
	}
	public void setSigemail(String sigemail) {
		this.sigemail = sigemail;
	}
	public String getSigname() {
		return signame;
	}
	public void setSigname(String signame) {
		this.signame = signame;
	}
	public String getSignaturedate() {
		return signaturedate;
	}
	public void setSignaturedate(String signaturedate) {
		this.signaturedate = signaturedate;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDaterecvd() {
		return daterecvd;
	}
	public void setDaterecvd(String daterecvd) {
		this.daterecvd = daterecvd;
	}
	public String getPedigreeid() {
		return pedigreeid;
	}
	public void setPedigreeid(String pedigreeid) {
		this.pedigreeid = pedigreeid;
	}

}

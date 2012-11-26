
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

 package com.rdta.catalog.trading.action;

import org.apache.struts.action.ActionForm;

public class MapTradingPartnerForm extends ActionForm{
	
	public String TrdPName;
	public String TrdPId;
	public String catName;
	public String CatalogGenId;
	public String catalogName;
	public String CatalogGenID;
	
	
	
	
	public String getTrdPId() {
		return TrdPId;
	}
	public void setTrdPId(String trdPId) {
		TrdPId = trdPId;
	}
	public String getTrdPName() {
		return TrdPName;
	}
	public void setTrdPName(String trdPName) {
		TrdPName = trdPName;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getCatalogGenId() {
		return CatalogGenId;
	}
	public void setCatalogGenId(String catalogGenId) {
		CatalogGenId = catalogGenId;
	}
	public String getCatalogGenID() {
		return CatalogGenID;
	}
	public void setCatalogGenID(String catalogGenID) {
		CatalogGenID = catalogGenID;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	

	
	
}

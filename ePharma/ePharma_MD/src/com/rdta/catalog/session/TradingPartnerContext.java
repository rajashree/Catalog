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

 
package com.rdta.catalog.session;

import java.io.Serializable;


public class TradingPartnerContext implements Serializable {

	private String tpGenId;
	private String tpName;
	private CatalogContext catalogContext;
	
	public String getTpGenId() {
		return tpGenId;
	}
	
	public void setTpGenId(String tpGenId) {
		this.tpGenId = tpGenId;
	}
	
	
	public String getTpName() {
		return tpName;
	}
	
	public void setTpName(String tpName) {
		this.tpName = tpName;
	}
	
	public void setCatalogContext(CatalogContext context) {
		catalogContext = context;
	}
	
	public CatalogContext getCatalogContext() {
		return catalogContext;
	}
}
 
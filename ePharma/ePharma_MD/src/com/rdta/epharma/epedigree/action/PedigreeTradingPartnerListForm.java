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

public class PedigreeTradingPartnerListForm extends ActionForm{
	
	private String tradingPartnerName = null;
	private String deaNumber = null;
	
	public String getDeaNumber() {
		return deaNumber;
	}
	public void setDeaNumber(String deaNumber) {
		this.deaNumber = deaNumber;
	}
	public String getTradingPartnerName() {
		return tradingPartnerName;
	}
	public void setTradingPartnerName(String tradingPartnerName) {
		this.tradingPartnerName = tradingPartnerName;
	}
}

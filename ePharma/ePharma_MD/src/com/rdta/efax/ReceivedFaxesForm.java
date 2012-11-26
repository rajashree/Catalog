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

 
package com.rdta.efax;

import org.apache.struts.action.ActionForm;

public class ReceivedFaxesForm extends ActionForm{

	private String faxName = null;
	private String CSID = null;
	private String ANI = null;
	private String dateReceived = null;
	
	public String getANI() {
		return ANI;
	}
	public void setANI(String ani) {
		ANI = ani;
	}
	public String getCSID() {
		return CSID;
	}
	public void setCSID(String csid) {
		CSID = csid;
	}
	public String getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getFaxName() {
		return faxName;
	}
	public void setFaxName(String faxName) {
		this.faxName = faxName;
	}
	
	
}

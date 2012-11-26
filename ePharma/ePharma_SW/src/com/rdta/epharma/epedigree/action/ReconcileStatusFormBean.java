
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


public class ReconcileStatusFormBean  extends ActionForm{

	String    status1; 
	 
	 String    sessionID;
	 String APNID;
	 
	 
	 
	 
	 
		/**
	 * @return Returns the sessionID.
	 */
	public String getSessionID() {
		return sessionID;
	}
	/**
	 * @param sessionID The sessionID to set.
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	/**
	 * @return Returns the status1.
	 */
	public String getStatus1() {
		return status1;
	}
	/**
	 * @param status1 The status1 to set.
	 */
	public void setStatus1(String status1) {
		this.status1 = status1;
	}
	/**
	 * @return
	 */
	public String getAPNID() {
		// TODO Auto-generated method stub
		return APNID;
	}
}
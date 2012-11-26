
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


public class ShipStatusFormBean  extends ActionForm{

	String    Shipstatus1;  
	String    sessionID;
	String    APNID;
	 
	 
	 
	 
	 
		/**
	 * @return Returns the sessionID.
	 */
	
	    /**
     * @return Returns the aPNID.
     */
    public String getAPNID() {
        return APNID;
    }
    /**
     * @param apnid The aPNID to set.
     */
    public void setAPNID(String apnid) {
        APNID = apnid;
    }
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
     * @return Returns the shipstatus1.
     */
    public String getShipstatus1() {
        return Shipstatus1;
    }
    /**
     * @param shipstatus1 The shipstatus1 to set.
     */
    public void setShipstatus1(String shipstatus1) {
        Shipstatus1 = shipstatus1;
    }
}

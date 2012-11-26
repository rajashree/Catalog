/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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


import javax.servlet.http.HttpServletRequest;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;


/**
 * @author Rama Krishna
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TradingPartnerForm extends ActionForm {

	FormFile pictFile;
    
	
		
	
    /**
     * @return Returns the pictFile.
     */
    public FormFile getPictFile() {
        return pictFile;
    }
    /**
     * @param pictFile The pictFile to set.
     */
    public void setPictFile(FormFile pictFile) {
		
	//	System.out.println(" Inside here..... ");
        this.pictFile = pictFile;
    }
 
     public void reset(ActionMapping mapping,HttpServletRequest request) {

         pictFile=null;
              	
     }
}
    


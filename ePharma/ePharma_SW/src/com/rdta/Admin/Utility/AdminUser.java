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

 
package com.rdta.Admin.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.Admin.servlet.RepConstants;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

public class AdminUser extends Action{
	static final Log logger = LogFactory.getLog(AdminUser.class);
	
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,								
								HttpServletRequest request,
								HttpServletResponse response)throws Exception{		
		HttpSession ses=request.getSession();
		String sessionID = (String) ses.getAttribute("sessionID");
		String pagenm = (String) request.getParameter("pagenm");		
		String tp_company_nm = (String) request.getParameter("tp_company_nm");
		Helper helper = new Helper();   
		logger.debug("sessionID :"+sessionID);
		request.setAttribute("pagenm",pagenm);
    	request.setAttribute("tp_company_nm",tp_company_nm);
		try {
    		String valAccess = helper.validateAccess(sessionID,RepConstants.ADMIN_MODULE_ID,RepConstants.ACCESS_READ);
    		if(!valAccess.equalsIgnoreCase(RepConstants.ACCESS_VALID)){
    			return mapping.findForward("AccessDenied");    			
    		}    			
    	} catch (PersistanceException exp) {				
    		exp.printStackTrace();
    		return mapping.findForward("ServerBusy");
    	}
    	
		return mapping.findForward("AdminMenu");
	}
	
	
}

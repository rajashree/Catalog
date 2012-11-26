
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

package com.rdta.rules;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class RuleEngineAction extends Action
{
	//private static Log log=LogFactory.getLog(CreateRuleAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
								throws Exception
								{
		try {

			String rulesetID = request.getParameter("rulesetID");
			String payload = request.getParameter("payload");
			
		        	RuleEngine re = new RuleEngine();
		        	String instanceId = "";
		    		instanceId = re.initRuleSet(rulesetID,payload);
		    		re.executeRuleSet(instanceId,payload);





			return mapping.findForward("success");
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("success");
		}
		}


}














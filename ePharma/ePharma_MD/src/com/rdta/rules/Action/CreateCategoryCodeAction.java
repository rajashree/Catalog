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

 
/*
 * Created on Aug 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.rules.Action;

import com.rdta.rules.RuleCollection;
import com.rdta.rules.CategoryCollection;
import com.rdta.rules.RulesXMLStructure;
import com.rdta.rules.CategoryCode;
import com.rdta.rules.OperationType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateCategoryCodeAction extends Action
{
	private static Log log = LogFactory.getLog(CreateCategoryCodeAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
						HttpServletRequest request, HttpServletResponse response)
							throws Exception {
		
		String operation = request.getParameter("operationType");
		log.debug(" Cateogry code action class operation "+operation);
		
		CategoryCode catcode = null;
		
		if(operation != null)
		{
			if(operation.trim().equalsIgnoreCase(OperationType.ADD))
			{
				catcode = new CategoryCode(request);
				catcode.insert();
				
				// request.setAttribute("ruleDisplay",RulesXMLStructure.getRuleDisplay() );
			}else
			{

			}
		}	
		/*if (catcode != null)
		{
			request.setAttribute("categoryInfo",catcode.getNode());
		}*/
		loadParentCode(request);
		return mapping.findForward("success");
	}
	
	public void loadParentCode(HttpServletRequest request) throws Exception
	{
		CategoryCollection cc = new CategoryCollection();
		String ccList = cc.getParentCodeList();
		request.setAttribute("parentCodeList",ccList);
	}	
	
}

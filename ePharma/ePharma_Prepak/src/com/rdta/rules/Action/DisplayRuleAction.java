/*
 * Created on Aug 11, 2005
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


package com.rdta.rules.Action;

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

import org.w3c.dom.Node;
import java.util.List;
import com.rdta.commons.xml.XMLUtil;

/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DisplayRuleAction extends Action
{
	private static Log log = LogFactory.getLog(DisplayRuleAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
						HttpServletRequest request, HttpServletResponse response)
							throws Exception {
		
		String operation = request.getParameter("operationType");
		//String radioRuleID = request.getParameter("radioRuleID");
		
		if(operation != null)
		{
			if(operation.trim().equalsIgnoreCase(OperationType.FIND))
			{
				String cc = request.getParameter("ccList");
				String s = RulesXMLStructure.getRuleDisplay(cc);
				request.setAttribute("ruleDisplay",s );
			}else if (operation.trim().equalsIgnoreCase(OperationType.VIEW))
			{
				
			}

		}	
		loadCategoryCode(request);
		return mapping.findForward("success");
	}
	
	public void loadCategoryCode(HttpServletRequest request) throws Exception
	{	
		CategoryCollection cc = new CategoryCollection();
		String catList = cc.getListCategories();
		request.setAttribute("ccList",catList);
	}	
	
}

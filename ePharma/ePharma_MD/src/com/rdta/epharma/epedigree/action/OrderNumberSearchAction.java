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
import javax.servlet.http.HttpSession;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.commons.CommonUtil;
import org.w3c.dom.Node;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class OrderNumberSearchAction extends Action{
	
	private static Log logger = LogFactory.getLog(OrderNumberSearchAction.class);
	 public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		 logger.info("Inside OrderNumberSearchAction...............");
		 logger.info("Inside OrderNumberSearchAction execute method.......");
		 
		 OrderNumberSearchForm formbean = (OrderNumberSearchForm)form;
		 String OrderNum = request.getParameter("orderNum").trim();
		 //String OrderNum = formbean.getOrderNum();
		 
		 HttpSession sess = request.getSession();
		 String sessionID = (String)sess.getAttribute("sessionID");
		 
		 request.setAttribute("trNum",OrderNum);

		 return mapping.findForward("success");
	 }
}

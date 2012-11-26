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

import com.rdta.rules.RuleCollection;
import com.rdta.rules.CategoryCollection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.trading.model.TradingPartner;
import com.rdta.catalog.trading.model.ImageStore;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.rules.OperationType;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import java.util.List;
import com.rdta.commons.xml.XMLUtil;

public class CreateRuleAction extends Action
{
	private static Log log=LogFactory.getLog(CreateRuleAction.class);
	RuleCollection rc = new RuleCollection();	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response)
								throws Exception
								{
		try {
			String operation = request.getParameter("operationType");
			String rID = request.getParameter("ruleID"); 
			String radioRuleID = request.getParameter("radioRuleID");
			
			
			if(operation != null)
			{
				if(operation.trim().equalsIgnoreCase(OperationType.ADD))
				{
					getRulesParametersAndAdd(request);
				}
				else if(operation.trim().equalsIgnoreCase(OperationType.UPDATE))
				{
					getRulesParameterAndUpdate(request);// test update
				}
				if(operation.trim().equalsIgnoreCase(OperationType.VIEW))
				{
					getRulesAndDisplay(request,radioRuleID);// test update
				}
				else
				{
					log.warn(" Operation Type : " +operation +"not valid action");
				}
			}/* else if (radioRuleID != null )
			{
				log.debug("------------test radionRuleID -----"+radioRuleID);
				getRulesAndDisplay(request,rID);// test update
			} */
			loadCondition(request);
			loadCategoryCode(request);
			loadContext(request);
			loadProceduresList(request);

			return mapping.findForward("success");
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("success");
		}
		}
	
		public void loadCategoryCode(HttpServletRequest request) throws Exception
		{	
			CategoryCollection cc = new CategoryCollection();
			String catList = cc.getListCategories();
			request.setAttribute("categoryCode",catList);
		}
		
		public void loadContext(HttpServletRequest request) throws Exception
		{
			RuleCollection rc = new RuleCollection();
			String cxtList = rc.getContextList();
			request.setAttribute("contextSchema",cxtList);
		}
		public void loadCondition(HttpServletRequest request) throws Exception
		{
			RuleCollection rc = new RuleCollection();
			String tacList = rc.getProceduresList(); //used same as getContextList
			request.setAttribute("condition",tacList);
		}		
		
		public void loadProceduresList(HttpServletRequest request) throws Exception
		{
			RuleCollection rc = new RuleCollection();
			String tacList = rc.getProceduresList();
			request.setAttribute("trueAction",tacList);
			request.setAttribute("falseAction",tacList);
		}		
		
		public void getRulesParameterAndUpdate(HttpServletRequest request) throws Exception
		{
			
			String ruleID = getValue(request,"ruleID");
			String ruleName = getValue(request,"ruleName");
			String contextSchema = getValue(request,"contextSchema");
			String context = getValue(request,"context");
			String categoryCode = getValue(request,"categoryCode");
			String condition = getValue(request,"condition");
			
			String trueAction = getValue(request,"trueAction");
			String falseAction = getValue(request,"falseAction");
			String truetoLink = getValue(request,"truetoLink");
			String falsetoLink = getValue(request,"falsetoLink");
			String fromLink = getValue(request,"fromLink");
			String status = getValue(request,"status");

			String createdBy = getValue(request,"createdBy");
			String createdOn = getValue(request,"createdOn");
			String updatedBy = getValue(request,"updatedBy");
			String updatedOn = getValue(request,"updatedOn");			
			
			String id = rc.updateRule(
							ruleID,
							ruleName,
							contextSchema,
							context,
							categoryCode,
							condition,
							trueAction,
							falseAction,
							truetoLink,
							falsetoLink,
							fromLink,
							status,
							createdBy,
							createdOn,
							updatedBy,
							updatedOn					
							);
			
			getRulesAndDisplay(request,ruleID);
			
			log.debug("Rule record update");
		}	

		public void getRulesParametersAndAdd(HttpServletRequest request) throws Exception
		{
			
			String ruleID = getValue(request,"ruleID");
			String ruleName = getValue(request,"ruleName");
			String contextSchema = getValue(request,"contextSchema");
			String context = getValue(request,"context");
			String categoryCode = getValue(request,"categoryCode");
			String condition = getValue(request,"condition");
			String trueAction = getValue(request,"trueAction");
			String falseAction = getValue(request,"falseAction");
			String truetoLink = getValue(request,"truetoLink");
			String falsetoLink = getValue(request,"falsetoLink");
			String fromLink = getValue(request,"fromLink");
			String status = getValue(request,"status");

			String createdBy = getValue(request,"createdBy");
			String createdOn = getValue(request,"createdOn");
			String updatedBy = getValue(request,"updatedBy");
			String updatedOn = getValue(request,"updatedOn");			
			
			String id = rc.createRule(
							ruleID,
							ruleName,
							contextSchema,
							context,
							categoryCode,
							condition,
							trueAction,
							falseAction,
							truetoLink,
							falsetoLink,
							fromLink,
							status,
							createdBy,
							createdOn,
							updatedBy,
							updatedOn					
							);
			
			request.setAttribute("ruleID",id);
			request.setAttribute("ruleName",ruleName);
			//request.setAttribute("contextSchema",contextSchema);
			request.setAttribute("selcontextSchema",contextSchema);
			request.setAttribute("context",context);
			//request.setAttribute("categoryCode",categoryCode);
			request.setAttribute("selcategoryCode",categoryCode);
			//request.setAttribute("condition",condition);
			request.setAttribute("selcondition",condition);
			//request.setAttribute("trueAction",trueAction);
			request.setAttribute("seltrueAction",trueAction);
			//request.setAttribute("falseAction",falseAction);
			request.setAttribute("selfalseAction",falseAction);
			request.setAttribute("truetoLink",truetoLink);
			request.setAttribute("falsetoLink",falsetoLink);
			request.setAttribute("fromLink",fromLink);
			request.setAttribute("status",status);
			
			log.debug("Rule record created");
		}	
		
		public void getRulesAndDisplay(HttpServletRequest request,String rID) 
									throws Exception
		{	
			String ruleXML = rc.getRuleNode(rID);
			Node n = XMLUtil.parse(ruleXML);
			
			String ruleName = XMLUtil.getValue(n,"RuleName");
			String trueAction = XMLUtil.getValue(n,"Result/TrueAction");
			
			request.setAttribute("ruleID",rID);
			request.setAttribute("ruleName",XMLUtil.getValue(n,"RuleName"));
			request.setAttribute("selcontextSchema",XMLUtil.getValue(n,"ContextSchema"));
			request.setAttribute("context",XMLUtil.getValue(n,"Context"));
			request.setAttribute("selcategoryCode",XMLUtil.getValue(n,"CategoryCode"));
			//request.setAttribute("condition",XMLUtil.getValue(n,"Condition"));
			request.setAttribute("selcondition",XMLUtil.getValue(n,"Condition"));
			request.setAttribute("seltrueAction",XMLUtil.getValue(n,"Result/TrueAction"));
			request.setAttribute("selfalseAction",XMLUtil.getValue(n,"Result/FalseAction"));
			request.setAttribute("truetoLink",XMLUtil.getValue(n,"Links/TrueToLink"));
			request.setAttribute("falsetoLink",XMLUtil.getValue(n,"Links/FalseToLink"));
			request.setAttribute("fromLink",XMLUtil.getValue(n,"Links/FromLink"));
			request.setAttribute("status",XMLUtil.getValue(n,"RuleStatus"));			
		}
		
		
		public String getValue(HttpServletRequest request,String s)
									throws Exception
		{
			String value = request.getParameter(s);
			if (value == null)
			{
				value = "";
			}
			return value;
		}
}














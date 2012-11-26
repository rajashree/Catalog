
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
import com.rdta.rules.RuleSetCollection;

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
import com.rdta.commons.xml.XMLUtil;

public class CreateRuleSetAction extends Action
{
	private static Log log=LogFactory.getLog(CreateRuleSetAction.class);
	RuleCollection rc = new RuleCollection();
	RuleSetCollection rsc = new RuleSetCollection();
	
	String rsID = "";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response)
								throws Exception
								{
		try {
			String operation = request.getParameter("operationType");
			String deleteOp = request.getParameter("DEL");
			String radioRuleSetID = request.getParameter("radioRuleSetID");

			log.debug("------------rs-----------");
			log.debug("-----------"+radioRuleSetID);
			log.debug("-----------"+operation);
			
			if(operation != null)
			{
				if(operation.trim().equalsIgnoreCase(OperationType.ADD))
				{
					getRuleSetParametersAndAdd(request);
				} else if (operation.trim().equalsIgnoreCase(OperationType.DELETE))
				{
					String deleteRuleID = request.getParameter("radioRuleID");
					String ruleSetID = request.getParameter("ruleSetID");
					if (deleteRuleID != null)
					{
						rsc.deleteRuleFromRuleSet(deleteRuleID,ruleSetID);
					}					
					loadDetailsAfterDelete(request,ruleSetID);
				} else	if(operation.trim().equalsIgnoreCase(OperationType.VIEW))
				{
					log.debug("------------inside View-----------");
					getRuleSetAndDisplay(request,radioRuleSetID);// test update
				}				
				else
				{
					log.warn(" Operation Type : " +operation +"not valid action");
				}
			} 
			
			/*else 
			{
				if (deleteOp != null )
				{
						getRuleSetParametersAndDelete(request);
				}
			}		*/				

			loadContextSchema(request);
			loadContext(request);
			loadCategoryCode(request);
			loadRules(request);
			loadRuleSetXML(request);

			return mapping.findForward("success");
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("success");
		}
	}
	
	
	    public void getRuleSetAndDisplay(HttpServletRequest request,String radioRuleSetID) throws Exception
		{
	    	log.debug("------------rs-----------");
	    	log.debug("------------rs-----------"+radioRuleSetID);
			rsID = radioRuleSetID;
			String xmlfile = rsc.getRuleSetXML(rsID);
			log.debug("------------rs----xmlfile-------"+xmlfile);
			
			Node n = XMLUtil.parse(xmlfile);
			String contextSchema = XMLUtil.getValue(n,"ContextSchema");
			String context = XMLUtil.getValue(n,"Context");
			String ruleSetName = XMLUtil.getValue(n,"RuleSetName");
			String categoryCode = XMLUtil.getValue(n,"CategoryCode");	    	
	    	
			request.setAttribute("ruleSetID",rsID);
			request.setAttribute("selcontextSchema",contextSchema);
			request.setAttribute("selcontext",context);
			request.setAttribute("ruleSetName",ruleSetName);
			request.setAttribute("selcategoryCode",categoryCode);
			
			rsID = rsID;  //setting the ruleset id.	    	
	    	
		}
	
		/*public void loadRuleDetailsProc(HttpServletRequest request) throws Exception
		{	
			//RuleCollection rc = new CategoryCollection();
			String rList = rc.listRuleDetailsProc();
			request.setAttribute("rulesDisplay",rList);
			log.debug("--------------------start-------------------");
			log.debug("rulesDisplay"+rList);
		}	*/
		public void loadRules(HttpServletRequest request) throws Exception
		{	
			//RuleCollection rc = new CategoryCollection();
			String rList = rc.getListRulesProc();
			request.setAttribute("rules",rList);
		}	
		
		public void loadRuleSetXML(HttpServletRequest request) throws Exception
		{
			String ruleSetXML = rsc.getRuleSetXML(rsID);
			request.setAttribute("ruleSetXML",ruleSetXML);
			
			rsID="";
		}
		
		public void loadDetailsAfterDelete(HttpServletRequest request,String ruleSetID) throws Exception
		{
			rsID = ruleSetID;
			String xmlfile = rsc.getRuleSetXML(rsID);
			
			Node n = XMLUtil.parse(xmlfile);
			String contextSchema = XMLUtil.getValue(n,"ContextSchema");
			String context = XMLUtil.getValue(n,"Context");
			String ruleSetName = XMLUtil.getValue(n,"RuleSetName");
			String categoryCode = XMLUtil.getValue(n,"CategoryCode");

			request.setAttribute("ruleSetID",rsID);			
			request.setAttribute("contextSchema",contextSchema);
			request.setAttribute("context",context);
			request.setAttribute("ruleSetName",ruleSetName);
			request.setAttribute("categoryCode",categoryCode);			
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
			request.setAttribute("context",cxtList);
		}
		public void loadContextSchema(HttpServletRequest request) throws Exception
		{
			RuleCollection rc = new RuleCollection();
			String cxtList = rc.getContextList();
			request.setAttribute("contextSchema",cxtList);
		}		
		
		public void loadProceduresList(HttpServletRequest request) throws Exception
		{
			RuleCollection rc = new RuleCollection();
			String tacList = rc.getProceduresList();
			request.setAttribute("trueAction",tacList);
		}		

		public void getRuleSetParametersAndAdd(HttpServletRequest request) throws Exception
		{
			String ruleSetID = request.getParameter("ruleSetID");
			String contextSchema = request.getParameter("contextSchema");
			String context = request.getParameter("context");
			String ruleSetName = request.getParameter("ruleSetName");
			String categoryCode = request.getParameter("categoryCode");
			String ruleID = request.getParameter("rules");
			String trueToLink = request.getParameter("trueToLink");
			String falseToLink = request.getParameter("falseToLink");
			String fromLink = request.getParameter("fromLink");
			
			String createdBy = request.getParameter("createdBy");
			String createdOn = request.getParameter("createdOn");
			String updatedBy = request.getParameter("updatedBy");
			String updatedOn = request.getParameter("updatedOn");
			
			String id = "";
			
			if (ruleSetID != null & !(ruleSetID.equals("")) )
			{
				id=ruleSetID;
				rsc.updateRuleinRuleSet(
						ruleSetID,
						ruleID,
						trueToLink,
						falseToLink,
						fromLink,
						createdBy,
						createdOn,
						updatedBy,
						updatedOn
						);
				rsc.updateDetailsinRuleSet(
						ruleSetID,
						contextSchema,
						context,
						ruleSetName,
						categoryCode
						);
				
			}else
			{
				id = rsc.createRuleSet(
						ruleSetID,
						contextSchema,
						context,
						ruleSetName,
						categoryCode,
						ruleID,
						trueToLink,
						falseToLink,
						fromLink,
						createdBy,
						createdOn,
						updatedBy,
						updatedOn
						);				
			}

			log.debug("RuleSet record created");
			
			request.setAttribute("ruleSetID",id);
			request.setAttribute("selcontextSchema",contextSchema);
			request.setAttribute("selcontext",context);
			request.setAttribute("ruleSetName",ruleSetName);
			request.setAttribute("selcategoryCode",categoryCode);
			request.setAttribute("ruleID",ruleID);
			//request.setAttribute("trueToLink",trueToLink);
			//request.setAttribute("falseToLink",falseToLink);
			//request.setAttribute("fromLink",fromLink);
			request.setAttribute("createdBy",createdBy);
			request.setAttribute("createdOn",createdOn);
			request.setAttribute("updatedBy",updatedBy);
			request.setAttribute("updatedOn",updatedOn);
			
			rsID = id;  //setting the ruleset id.
			
			/*
			//display list of rules added
			String rList = rc.listRuleDetailsProc(id);
			request.setAttribute("rulesDisplay",rList);			
			*/
		}	
		

		public void getRuleSetParametersAndDelete(HttpServletRequest request) throws Exception
		{
			String ruleSetID = request.getParameter("ruleSetID");
			String contextSchema = request.getParameter("contextSchema");
			String context = request.getParameter("context");
			String ruleSetName = request.getParameter("ruleSetName");
			String categoryCode = request.getParameter("categoryCode");
			String ruleID = request.getParameter("rules");
			String trueToLink = request.getParameter("trueToLink");
			String falseToLink = request.getParameter("falseToLink");
			String fromLink = request.getParameter("fromLink");
			
			String createdBy = request.getParameter("createdBy");
			String createdOn = request.getParameter("createdOn");
			String updatedBy = request.getParameter("updatedBy");
			String updatedOn = request.getParameter("updatedOn");
			
			String id = "";
			
				id=ruleSetID;

			
			request.setAttribute("ruleSetID",id);
			request.setAttribute("selcontextSchema",contextSchema);
			request.setAttribute("selcontext",context);
			request.setAttribute("ruleSetName",ruleSetName);
			request.setAttribute("selcategoryCode",categoryCode);
			request.setAttribute("ruleID",ruleID);
			//request.setAttribute("trueToLink",trueToLink);
			//request.setAttribute("falseToLink",falseToLink);
			//request.setAttribute("fromLink",fromLink);
			request.setAttribute("createdBy",createdBy);
			request.setAttribute("createdOn",createdOn);
			request.setAttribute("updatedBy",updatedBy);
			request.setAttribute("updatedOn",updatedOn);
			
			//display list of rules added
			String rList = rc.listRuleDetailsProc(id);
			request.setAttribute("rulesDisplay",rList);			
			
		}		
}














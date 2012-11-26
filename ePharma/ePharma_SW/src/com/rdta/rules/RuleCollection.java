/*
 * Created on Aug 4, 2005
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

package com.rdta.rules;

import com.rdta.commons.persistence.QueryRunner ;
import com.rdta.commons.persistence.QueryRunnerFactory;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class RuleCollection {
	
	private static Log log=LogFactory.getLog(RuleCollection.class);
	
	QueryRunnerFactory qrunFac = QueryRunnerFactory.getInstance();
	QueryRunner qrun           = qrunFac.getDefaultQueryRunner();
	
	/**
	 * @param storedProc
	 * @return
	 * @throws Exception
	 */
	public String createRuleSP(String storedProc) throws Exception
	{
		int start = storedProc.indexOf("tig");
		int total = 29 + start;
		int close = storedProc.indexOf(",");

		String procName = storedProc.substring(total,close);

		System.out.println("index procName is-->"+procName);
		
		qrun.executeQuery(storedProc);
		
		return procName;
	}
	
	/**
	 * 
	 * @param ruleID
	 * @param ruleName
	 * @param contextSchema
	 * @param context
	 * @param categoryCode
	 * @param condition
	 * @param result_t
	 * @param result_f
	 * @param link_t
	 * @param link_f
	 * @param ruleStatus
	 * @param createdBy
	 * @param createdOn
	 * @param updatedBy
	 * @param updateOn
	 * @throws Exception
	 */
	public String createRule(String ruleID, 
										String ruleName, 
										String contextSchema, 
										String context, 
										String categoryCode, 
										String condition, 
										String result_t, 
										String result_f,
										String tlink_t,
										String flink_t, 
										String link_f, 
										String ruleStatus, 
										String createdBy, 
										String createdOn,
										String updatedBy, 
										String updatedOn	) throws Exception	
	
	{		
		
		String rid = "";
		List rlt = qrun.returnExecuteQueryStrings("tlsp:getRuleIDProc()");

		for (Iterator itr = rlt.iterator(); itr.hasNext();)
		{
			rid = (String)itr.next();
		}			
		
		ruleID = rid;
		
		String input = "'"+ ruleID + "','"+
						ruleName + "','"+
						contextSchema + "','"+
						context + "','"+
						categoryCode + "','"+
						condition + "','"+
						result_t + "','"+
						result_f + "','"+
						tlink_t + "','"+
						flink_t + "','"+
						link_f + "','"+
						ruleStatus + "','"+
						createdBy + "','"+
						createdOn + "','"+
						updatedBy + "','"+
						updatedOn +"'";
		
		List lt = qrun.executeQuery("tlsp:addRuleProc("+input+")");		
		
		return ruleID;
	}
	
		public String updateRule(String ruleID, 
				String ruleName, 
				String contextSchema, 
				String context, 
				String categoryCode, 
				String condition, 
				String result_t, 
				String result_f,
				String tlink_t,
				String flink_t, 
				String link_f, 
				String ruleStatus, 
				String createdBy, 
				String createdOn,
				String updatedBy, 
				String updatedOn	) throws Exception	
	
	{		
	
		String input = "'"+ ruleID + "','"+
		ruleName + "','"+
		contextSchema + "','"+
		context + "','"+
		categoryCode + "','"+
		condition + "','"+
		result_t + "','"+
		result_f + "','"+
		tlink_t + "','"+
		flink_t + "','"+
		link_f + "','"+
		ruleStatus + "','"+
		createdBy + "','"+
		createdOn + "','"+
		updatedBy + "','"+
		updatedOn +"'";
		
		List lt = qrun.executeQuery("tlsp:updateRuleProc("+input+")");		
		
		return ruleID;
	}	
	
	
	public String getRuleNode(String ruleID) throws Exception
	{
		String ret = "";
		List lt = qrun.returnExecuteQueryStrings("tlsp:getRuleProc('"+ruleID+"')");

		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			ret = (String)itr.next();
		}		

		return ret;
	}
	
	public String getContextList() throws Exception
	{
		String ret="";
		List lt = qrun.returnExecuteQueryStrings("tlsp:listContextProc()");
		String s ="";
		
		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			s = (String)itr.next();
			ret = ret + s;
		}
		return ret;
		
	}
	
	public String getListRulesProc() throws Exception
	{
		String ret="";
		//List lt = qrun.returnExecuteQueryStrings("tlsp:listRulesProc()");
		List lt = qrun.returnExecuteQueryStrings("tlsp:listRulesSelProc()");
		String s ="";
		
		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			s = (String)itr.next();
			ret = ret + s;
		}
		return ret;
	}	
	
	public String getProceduresList() throws Exception
	{
		String ret="";
		List lt = qrun.returnExecuteQueryStrings("tlsp:listProceduresProc()");
		String s ="";
		
		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			s = (String)itr.next();
			ret = ret + s;
		}
		return ret;
		
	}	
	
	public String listRuleDetailsProc(String ruleSetID) throws Exception
	{

		String ret="";
		List lt = qrun.returnExecuteQueryStrings("tlsp:listRuleDetailsProc('"+ruleSetID+"')");
		String s ="";
		
		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			s = (String)itr.next();
			ret = ret + s;
		}

		return ret;		
	}
	
	public String getRuleForDisplay(String categoryCode) throws Exception
	{
		
		String ret="";
		List lt = qrun.returnExecuteQueryStrings("tlsp:listRuleForDisplayProc('"+categoryCode+"')");
		String s ="";
		
		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			s = (String)itr.next();
			ret = ret + s;
		}
		return "<result>" +ret+ "</result>";			
		
	}
	
	//test
	public static void main(String args[]) throws Exception
	{
		RuleCollection rc = new RuleCollection();
		rc.createRule("r123","B2","C3","D4","E5","F6","G7","H8","I9","J","K","L","M","N","O","l");
		
		//String sp = " declare general-option 'experimental=true'; tig:create-stored-procedure(\"addRuleProc\", declare variable $ruleID as string external; declare variable $ruleID as string external; ";
		//sp.replaceAll("'","\"");

		//rc.createRuleSP(sp);
	}	
}

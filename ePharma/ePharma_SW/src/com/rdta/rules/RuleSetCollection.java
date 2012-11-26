/*
 * Created on Aug 5, 2005
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
import com.rdta.rules.Action.CreateRuleSetAction;

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
public class RuleSetCollection {
	
	private static Log log=LogFactory.getLog(RuleSetCollection.class);
	String AD     = "','"; 
	
	QueryRunnerFactory qrunFac = QueryRunnerFactory.getInstance();
	QueryRunner qrun           = qrunFac.getDefaultQueryRunner();	
	
	
	public String createRuleSet(
								String ruleSetID, 
								String contextSchema,
								String context,
								String ruleSetName, 
								String categoryCode, 
								String ruleID,
								String trueToLink, 
								String falseToLink, 
								String fromLink, 
								
								String createdBy, 
								String createdOn, 
								String updatedBy, 
								String updatedOn
							 ) throws Exception {
		
		
		String rid = "";
		List rlt = qrun.returnExecuteQueryStrings("tlsp:getRuleSetIDProc()");

		for (Iterator itr = rlt.iterator(); itr.hasNext();)
		{
			rid = (String)itr.next();
		}			
		
		ruleSetID = rid;
		
		String input = "'"+ ruleSetID + AD + 
		   contextSchema + AD +
		   context + AD +
		   ruleSetName +AD +
		   categoryCode + AD +
		   
		   ruleID + AD +
		   trueToLink + AD +
		   falseToLink + AD +
		   fromLink + AD +
		   
		   createdBy + AD +
		   createdOn + AD +
		   updatedBy + AD +
		   updatedOn + "'";

		List listcat = qrun.returnExecuteQueryStrings("tlsp:addRuleSetProc("+input+")");
		
		return ruleSetID;		
	}

	public String updateRuleinRuleSet(
			String ruleSetID, 
			String ruleID,
			String trueToLink,
			String falseToLink,
			String fromLink,
			
			String createdBy, 
			String createdOn, 
			String updatedBy, 
			String updatedOn
		 ) throws Exception {

		
		String input = "'"+ ruleSetID + AD + 
		ruleID + AD +
		trueToLink + AD +
		falseToLink + AD +
		fromLink + AD +

		createdBy + AD +
		createdOn + AD +
		updatedBy + AD +
		updatedOn + "'";
		
		log.debug("input"+input);
		
		List listcat = qrun.returnExecuteQueryStrings("tlsp:updateRulesInRuleSetProc("+input+")");
		
		return ruleSetID;
	}	

	public String updateDetailsinRuleSet(
			String ruleSetID,
			String contextSchema, 
			String context,
			String ruleSetName,
			String categoryCode
			
		 ) throws Exception {

		
		String input = "'"+ ruleSetID + AD + 
		contextSchema + AD +
		context + AD +
		ruleSetName + AD +		
		categoryCode + "'";
		
		List listcat = qrun.returnExecuteQueryStrings("tlsp:updateRuleSetDetails("+input+")");
		
		return ruleSetID;
	}	
	
	
	public String getRuleSetXML(String ruleSetID) throws Exception {
		
		String ret = "";
		List lt = qrun.returnExecuteQueryStrings("tlsp:getRuleSetProc('"+ruleSetID+"')");

		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			ret = (String)itr.next();
		}		
		return ret;		
	}
	
	public boolean deleteRuleFromRuleSet(String deleteRuleID,String ruleSetID) throws Exception
	{
		String input = "'"+ deleteRuleID + AD + ruleSetID +"'";
		qrun.returnExecuteQueryStrings("tlsp:deleteRuleFromRuleSetProc("+input+")");
		return true;
	}
	
	public String getRuleSetForDisplay(String categoryCode) throws Exception
	{
		
		String ret="";
		List lt = qrun.returnExecuteQueryStrings("tlsp:listRuleSetForDisplayProc('"+categoryCode+"')");
		String s ="";
		
		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			s = (String)itr.next();
			ret = ret + s;
		}
		return "<result>" +ret+ "</result>";			
		
	}	
	
	public String getRuleFromRuleSetXML(String ruleSetID, String ruleID)
				throws Exception {
		
		String ret = "";
		
		String input = "'"+ ruleSetID + AD + 
						ruleID + "'";
		
		List lt = qrun.returnExecuteQueryStrings("tlsp:getRuleFromRuleSet("+input+")");

		for (Iterator itr = lt.iterator(); itr.hasNext();)
		{
			ret = (String)itr.next();
		}		
		return ret;		
	}	
	
	// update rule in ruleSet collection
	public String updateRSRuleinRuleSet(
	        String ruleSetID,
	        String ruleID,
	        String ruleName,
	        String condition,
	        String contextSchema,
	        String categoryCode,
	        String trueAction,
	        String falseAction,
	        String truetoLink,
	        String falsetoLink,
	        String fromLink
		 ) throws Exception {

		
		String input = "'"+ ruleSetID + AD + 
		ruleID + AD +
		ruleName + AD +
		condition + AD +
		contextSchema + AD +

		categoryCode + AD +
		trueAction + AD +
		falseAction + AD +
		truetoLink + AD +
		falsetoLink + AD +		
		fromLink + "'";
		
		log.debug("--------input"+input);
		
		List listcat = qrun.returnExecuteQueryStrings("tlsp:updateRSRuleInRuleSetProc("+input+")");
		
		return ruleSetID;
	}	
	
/*	
	public void createRuleSet(String ruleSetID,
								String contextSchema,
								String context,
								String ruleSetName,
								String categoryCode,
								String ruleSetStatus,
								
								 String createdBy, 
								 String createdOn,
								 String updatedBy, 
								 String updatedOn ) throws Exception
		{
		String input = "'"+ ruleSetID + AD + 
					   contextSchema + AD +
					   context + AD +
					   ruleSetName +AD +
					   categoryCode + AD +
					   ruleSetStatus + AD +
					   createdBy + AD +
					   createdOn + AD +
					   updatedBy + AD +
					   updatedOn + "'";
		
		List listcat = qrun.returnExecuteQueryStrings("tlsp:addRuleSetProc("+input+")");
		
		}	
	
	public void addRuleInRuleSet(String ruleSetID, String ruleID ) throws Exception 
	{
		RuleCollection rc = new RuleCollection();
		String ruleNode = rc.getRuleNode(ruleID);
		System.out.println("---rulenode--"+ruleNode);
		updateRuleNode(ruleSetID, ruleNode );
		System.out.println("---rulenode updated--"+ruleNode);
	}	
	
	private void updateRuleNode(String ruleSetID, String ruleNode ) throws Exception
	{
		String input = "'"+ruleSetID +AD + ruleNode +"'";
		
		qrun.returnExecuteQueryStrings("tlsp:updateRuleNodeInRuleSetProc("+input+")");
	}	
	
	public static void main(String args[]) throws Exception 
	{
		RuleSetCollection rsc = new RuleSetCollection();
		rsc.createRuleSet("a123","b","c","d","e","f","g","h","i","j");
		System.out.println("--1--");
		rsc.addRuleInRuleSet("a123","r123");
		System.out.println("--2--");
	}
	*/
}

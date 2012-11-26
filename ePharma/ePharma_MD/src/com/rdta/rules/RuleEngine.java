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
 * Created on Aug 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.rules;
import  com.rdta.commons.persistence.*;
import  com.rdta.commons.xml.*;
import java.util.*;
import java.io.*;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/**
 * @author pparikh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RuleEngine { 
	 public static QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	 private String context = "";
	 private String payload =  context; 
	/**
	 * 
	 */
	 public void addContext(){
	 	this.payload =  this.context; 
	 }
	public RuleEngine() {
		super();
		// TODO Auto-generated constructor stub
	}

	
		
	public String initRuleSet(String ruleId, String context){
		String maxID = "0";
		try{
		this.context = context;
		addContext();
		String id = ruleId;
		String instanceuri = "";
		StringBuffer xQuery = new StringBuffer();
		ArrayList ruleList = new ArrayList();
		xQuery.append("tlsp:createInstance('" + id + "')");
		System.out.println("create insatance" +xQuery.toString() );
		
		ruleList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
		for(int i=0; i<ruleList.size(); i++){
		 	instanceuri = (String)ruleList.get(i);
		 	
		 }
		
		
		xQuery = new StringBuffer();
		xQuery.append("tlsp:getMax()");
		ruleList = new ArrayList();
		ruleList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
		for(int i=0; i<ruleList.size(); i++){
		 	 maxID = (String)ruleList.get(i);
		 	
		 }
		
		xQuery = new StringBuffer();
		
		xQuery.append("replace value of doc('" + instanceuri + "')/RuleSet/InstanceID with " + maxID);
		System.out.println("create insatance" +xQuery.toString() );
		
		ruleList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
		
		xQuery = new StringBuffer();
		xQuery.append("tlsp:attachPayload('" + this.payload + "','" + maxID  +"')");
		System.out.println("Attached Payload Query" +xQuery.toString() );
		ruleList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
		executeRuleSet(maxID,payload);
		}catch (Exception e){
			
			System.out.println("Exception" +e );
		}
		return maxID;
		}
	
	
	public void executeRuleSet(String ruleInstance, String context){
		String instanceID = ruleInstance;
		
		try{
			StringBuffer xQuery = new StringBuffer();
			ArrayList ruleList = new ArrayList();
			ArrayList resultList = new ArrayList();
			String firstRule = new String();
			String nextRule = new String();
			String nextLink = new String();
			xQuery.append("tlsp:GetAllRules('" + instanceID + "')");
			this.payload = context;
			boolean lastRule = false;
			ruleList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
			
			// Get First Rule
			xQuery = new StringBuffer();
			xQuery.append("tlsp:GetFirstRule('" + instanceID +"')"); 
			firstRule = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
			nextLink = executeRule(firstRule,instanceID,payload);
			
			while(!(("LastRule").equals(nextLink))){
				
				xQuery = new StringBuffer();
				xQuery.append("tlsp:GetNextRule('"+ instanceID + "','"+ nextLink +"')" );
				nextRule = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
				nextLink = executeRule(nextRule,instanceID,payload);
					
			}
				 
			
		}catch (Exception e){
			
			System.out.println("Exception" +e );
		}
	}
	
	
	public String executeRule(String rule,String ruleInstance, String context){
		
		String instanceID = ruleInstance;
		StringBuffer xQuery = new StringBuffer();
		ArrayList ruleList = new ArrayList();
		ArrayList resultList = new ArrayList();
		String firstRule = new String();
		String conditionResult = new String();
		String conditionProc = new String();
		String actionProc = new String();
		String actionResult = new String();
		String nextRule = new String();
		String nextLink = new String();
		String ruleID = "";
		//String payload = context;
		try{
			
			
			// Get Condition Proc Name
			xQuery = new StringBuffer();
			xQuery.append("declare variable $rule as node()* {" +rule +"};"); 
			xQuery.append("data($rule/Rule/Condition)"); 
			conditionProc = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
			System.out.println("Condition " + conditionProc);
			// Get Rule ID
			
			xQuery = new StringBuffer();
			xQuery.append("declare variable $rule as node()* {" +rule +"};"); 
			xQuery.append("data($rule/Rule/RuleID)"); 
			System.out.println("Rule Query" + xQuery.toString());
			ruleID = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
			
			
			
			// Execute Condition Proc and Get Result back, if not condtion than execute true action
			if ("true".equals(checkDocumentExists(conditionProc))){
				
			xQuery = new StringBuffer();
			xQuery.append("tlsp:" + conditionProc + "()");
			conditionResult = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
			}
			else{
			conditionResult = "true";}
			
			// Based on Condition Result get Action Proc Name
			xQuery = new StringBuffer();
			xQuery.append("declare variable $conditionResult as string {'" + conditionResult + "'};");
			xQuery.append("declare variable $rule as node()* {" + rule + "};");
			xQuery.append("if ($conditionResult = 'true') then ");
			xQuery.append(" data($rule/Rule/Result/TrueAction)");
			xQuery.append(" else ");
			xQuery.append("data($rule/Rule/Result/FalseAction)");
			System.out.println("Action Query" + xQuery.toString());
			if((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())!= null){
			actionProc = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
			}
			System.out.println("Action Proc" + actionProc);
			// Execute Action Proc
			if ("true".equals(checkDocumentExists(actionProc))){
			xQuery = new StringBuffer();
			xQuery.append("tlsp:" + actionProc + "('" + this.payload + "')");
			if((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())!= null){
				
			actionResult = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
			this.context = actionResult;
			}
			System.out.println("actionResult " + actionResult);
			
			addContext();
			}
			
			// Get Next link on Condition Result after
			
			
			xQuery = new StringBuffer();
			xQuery.append("declare variable $conditionResult as string {'" +conditionResult +"'};");
			xQuery.append("declare variable $rule as node()* {" + rule + "};");
			xQuery.append("if ($conditionResult = 'true') then ");
			xQuery.append("if(fn:string-length($rule/Rule/Links/TrueToLink) <= 0) then ");
			xQuery.append("'LastRule'");
			xQuery.append(" else ");
			xQuery.append("data($rule/Rule/Links/TrueToLink)");
			xQuery.append(" else ");
			xQuery.append("if(fn:string-length($rule/Rule/Links/FalseToLink) <= 0) then ");
			xQuery.append("'LastRule'");
			xQuery.append(" else ");
			xQuery.append("data($rule/Rule/Links/FalseToLink)");
						
			nextLink = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
						
			
		}catch (Exception e){
			
			System.out.println("Exception" +e );
		}
		return nextLink;
		
		
	}
	
	public String checkDocumentExists(String procName){
		String exists = "false";
		StringBuffer xQuery = new StringBuffer();
		try{
		xQuery.append("declare variable $docName as string {'" + procName + "'};");
		xQuery.append("document-exists(concat('tig:///admin/xquerystoredprocedure/',$docName))");
		exists = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
		}catch (Exception e){
		System.out.println("Exception" +e );
		}
		return  exists;
		
	}
		

	public static void main(String[] args) {
		RuleEngine re = new RuleEngine();
		String instanceId = "";
		instanceId = re.initRuleSet("2","11111");
		//re.executeRuleSet(instanceId,"<po><id>3</id><Status>Created</Status></po>");
	}
  
	}
/* All Stored Procedures
 
  
 */

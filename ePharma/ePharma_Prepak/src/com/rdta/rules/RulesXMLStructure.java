
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

import com.rdta.commons.xml.XMLUtil;
import com.rdta.rules.Action.CreateRuleAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RulesXMLStructure
{
	private static Log log=LogFactory.getLog(RulesXMLStructure.class);
	
	private static String categoryCode = " <CategoryDefn> <Code></Code><ParentCode></ParentCode>"
		  + "<Description></Description><Audit><CreatedBy></CreatedBy>"
		  +"<CreatedOn></CreatedOn><UpdatedBy></UpdatedBy><UpdatedOn></UpdatedOn>"
		  +"</Audit></CategoryDefn>";
	
	//private static String ruleDisplay = " <GET IT FROM QUERY > ";
	
	public static Node getCategoryCode() {
		return XMLUtil.parse(categoryCode);
	}	
	
//	public static Node getRuleDisplay() throws Exception {
	public static String getRuleDisplay(String cc) throws Exception {	
		RuleCollection rc = new RuleCollection();
		String ruleXML = rc.getRuleForDisplay(cc);
		log.debug("------debug rules XML "+ruleXML);
		return ruleXML;
//		return XMLUtil.parse(ruleXML);
	}	
	
//	public static Node getRuleDisplay() throws Exception {
	public static String getRuleSetDisplay(String cc) throws Exception {	
		RuleSetCollection rc = new RuleSetCollection();
		String ruleXML = rc.getRuleSetForDisplay(cc);
		log.debug("------debug rules XML "+ruleXML);
		return ruleXML;
//		return XMLUtil.parse(ruleXML);
	}	
	
}

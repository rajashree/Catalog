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

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import java.util.List;
import com.rdta.commons.xml.XMLUtil;

public class Test {
	
public static void main(String args[]) throws Exception
{
	String mainXML = "<result><ruleID></ruleID><ruleName></ruleName><rules></rules></result>";
	
	String appendXML = "<rule><cc></cc> <rule>";

	Node n = XMLUtil.parse(mainXML);
	
	XMLUtil.putValue(n,"ruleID","ID123");
	XMLUtil.putValue(n,"ruleName","Name123");
	
	/*
	Document rootDocument = targetNode.getOwnerDocument();
	Node newElement = rootDocument.createElement(elementName);
	Node parentNode = XMLUtil.getNode(targetNode,xpathBuff);
	return parentNode.appendChild(newElement);	
	*/
	System.out.println("----------start---------");
 
	String ruleID = XMLUtil.getValue(n,"ruleID");
	System.out.println("--ruleID---"+ruleID);
	String ruleName = XMLUtil.getValue(n,"ruleName");
	System.out.println("--ruleName---"+ruleName);
	
	Document rootDocument = n.getOwnerDocument();
	Node newElement = rootDocument.createElement("rule");
	Node nodecc = newElement.getOwnerDocument().createElement("cc");
	newElement.appendChild(nodecc);

	System.out.println("tostirng 0--- " + XMLUtil.convertToString(newElement));
	XMLUtil.putValue(newElement,"cc","cc123");
	
	Node parentNode = XMLUtil.getNode(n,"rules");
	parentNode.appendChild(newElement);

	
	System.out.println("tostirng 1--- " + XMLUtil.convertToString(n));
	//System.out.println("tostirng 2--- " + XMLUtil.convertToString(parentNode));
	
	
	
	
}

}

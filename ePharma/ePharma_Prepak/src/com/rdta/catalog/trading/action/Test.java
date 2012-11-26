/*
 * Created on Sep 12, 2005
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

 
package com.rdta.catalog.trading.action;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.commons.xml.XMLUtil;

/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test {
	
public static void main(String args[])
{
	String tpName = "";
	String tpGenId  = "";

	boolean isStandard = true;
	String leftcatalogGenId = "32248822880028878528147088736221";
	String rightcatalogGenId ="";
	
/*
	CatalogContext catalogContext = null;
	HttpSession session=null;
	
		catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);

	   TradingPartnerContext tpContext =(TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
		if(tpContext != null) {
			tpName = tpContext.getTpName();
			tpGenId = tpContext.getTpGenId();
		}
	*/CatalogContext catalogContext = null;
	

Node catalogNode = catalogContext.getCatalogNode(leftcatalogGenId);
	/*


String values = "";
String currSelectionXpath = "/" ;

String currSelectionNodeName = "";

//is it root element selection
boolean rootSelection = true;
Node node = XMLUtil.getNode(catalogNode, "schema/*");
if(node != null ) {
	if(currSelectionXpath == null ) {
		currSelectionXpath = node.getNodeName();
		currSelectionNodeName = node.getNodeName();
	} else {
		 StringBuffer buff = new StringBuffer(currSelectionXpath);
		 int index = buff.lastIndexOf("/");
		 if(index > 0) {
			rootSelection =	false;
			currSelectionNodeName = currSelectionXpath.substring(index+1, currSelectionXpath.length());
		 }
	}

} else {
	//if there is no schema under some element
	//then make it empty
	currSelectionXpath = "NEW TREE CREATION";
}


//get the values info
if(!rootSelection) {
	Node schemaNode = XMLUtil.getNode(catalogNode, "schema");
	if(schemaNode != null) {
		values = XMLUtil.getValue(schemaNode,currSelectionXpath+"/@values");
		if(values ==null)
			values ="";
	}
}

*/
}
}
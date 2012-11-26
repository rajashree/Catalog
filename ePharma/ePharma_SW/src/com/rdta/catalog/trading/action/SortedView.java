/*
 * Created on Oct 7, 2005
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

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.TreeSet;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/**
 * @author Arun Kumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SortedView {
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public static void main(String args[])throws Exception{
		
		StringBuffer buffer= new StringBuffer();
		
		buffer.append("for $i in collection ('tig:///CatalogManager/ProductMaster')//LegendDrugName");
		buffer.append(" where $i = 'kiran' return $i/../IncludeProducts ");
		
		Node node = XMLUtil.parse((ByteArrayInputStream)(queryRunner.executeQuery(buffer.toString()).get(0)));
		NodeList nodelist = node.getChildNodes();
		int length = nodelist.getLength();
		System.out.println(length);
		
		TreeSet set = new TreeSet();
		
		for( int i=1;i<length-1;i++){
				set.add(XMLUtil.getValue(nodelist.item(i),"Quantity"));
		}
		         
		
			
		
		
		
	}





}

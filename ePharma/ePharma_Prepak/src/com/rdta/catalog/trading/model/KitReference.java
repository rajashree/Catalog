
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

 package com.rdta.catalog.trading.model;


import java.util.List;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.w3c.dom.Node;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.PersistanceUtil;



import com.rdta.commons.persistence.PersistanceException;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

import com.rdta.commons.xml.XMLUtil;
import com.rdta.catalog.Constants;

import com.rdta.commons.CommonUtil;

/**
 * Trading Partner Form information collecting from the reques form.
 * 
 *  
 * 
 */

public class KitReference 
{
    private static Log log = LogFactory.getLog(KitReference.class);
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	private Node kitRefNode; 
	
	public KitReference(Node node){
		kitRefNode = node;
	}
	
	
	public Node getNode() {
		return kitRefNode;
	}
	
	public String getGenId() {
		return XMLUtil.getValue(kitRefNode,"genId");
	}
	
	public static KitReference find(String genId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/KitRef/genId='");
		buff.append( genId);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.KIT_REFERENCE_COLL,buff.toString());
		if(!list.isEmpty()) {
			System.out.println(" Before returning find : "  );
			return new KitReference(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	
	public void insert() throws PersistanceException {
		XMLUtil.putValue(kitRefNode,"genId", CommonUtil.getGUID());
		PersistanceUtil.insertDocument(kitRefNode,Constants.KIT_REFERENCE_COLL);
	}
	
	public void update() throws PersistanceException {
		StringBuffer buff = new StringBuffer("$a/KitRef/genId='");
		buff.append( XMLUtil.getValue(kitRefNode,"genId"));
		buff.append("'");
		PersistanceUtil.updateDocument(kitRefNode,Constants.KIT_REFERENCE_COLL,buff.toString());
	}
  
}

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


import java.util.Date;
import java.util.List;

import java.util.ArrayList;
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
 * Catalog information collecting from the reques form.
 * 
 *  
 * 
 */

public class MappingProducts
{
    private static Log log = LogFactory.getLog(MappingProducts.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private Node mappingProduct; 
	
		
	public MappingProducts(Node node){
		mappingProduct = node;
	}
	
	
	public Node getNode() {
		return mappingProduct;
	}
	
	public String getGenId() {
		
		return XMLUtil.getValue(mappingProduct,"genId");
	}
	
	public static MappingProducts find(String genId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/MappingProducts/genId='");
		buff.append( genId);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.MAPPING_PRODUCTS_COLL,buff.toString());
		if(!list.isEmpty()) {
			return new MappingProducts(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	
	
	public static MappingProducts find(String sourceGenId,String targetGenId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/MappingProducts/headerInfo/source/catalogId='");
		buff.append( sourceGenId);
		buff.append("' and ");
		buff.append("$a/MappingProducts/headerInfo/target/catalogId='");
		buff.append( targetGenId);
		buff.append("'");

		List list = PersistanceUtil.findDocument(Constants.MAPPING_PRODUCTS_COLL,buff.toString());
		if(!list.isEmpty()) {
			return new MappingProducts(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	
	
	public void insert(String sourceGenId,String targetGenId) throws PersistanceException {
		XMLUtil.putValue(mappingProduct,"genId", CommonUtil.getGUID());
		XMLUtil.putValue(mappingProduct,"headerInfo/source/catalogId", sourceGenId);
		XMLUtil.putValue(mappingProduct,"headerInfo/target/catalogId", targetGenId);
		PersistanceUtil.insertDocument(mappingProduct,Constants.MAPPING_PRODUCTS_COLL);
	}
	
	
	public void update() throws PersistanceException {
		StringBuffer buff = new StringBuffer("$a/MappingProducts/genId='");
		buff.append( XMLUtil.getValue(mappingProduct,"genId"));
		buff.append("'");
		PersistanceUtil.updateDocument(mappingProduct,Constants.MAPPING_PRODUCTS_COLL,buff.toString());
	}
	
	
		  
}
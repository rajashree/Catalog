
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

public class ImageStore 
{
    private static Log log = LogFactory.getLog(ImageStore.class);
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	private InputStream stream;
	
	public ImageStore(InputStream stream){
		this.stream = stream;
	}
	
	
	public InputStream getInputStrem() {
		return stream;
	}
	
	public static ImageStore find(String refColl, String refGenId) throws PersistanceException {
	
		StringBuffer xQuery = new StringBuffer();
		xQuery.append("for $a in collection( 'tig:///" + Constants.CATALOG_DB + "/" + Constants.IMAGES_COLL + "')");
		xQuery.append(" where  $a/Image/keyRef/collectionName='" + refColl + "'");
		xQuery.append(" and  $a/Image/keyRef/genId='" + refGenId + "'");
		xQuery.append(" return $a/Image/IMG/binary()");
		List list  = queryRunner.executeQuery(xQuery.toString());
		
		if(!list.isEmpty()) {
			System.out.println(" Before Creating Image Store");
			return new ImageStore((InputStream)list.get(0));
		} else {
			return null;
		}
	}
	
	public void insert(String refColl, String refGenId) throws PersistanceException {
		 			
		 	Node imageNode = XMLStructure.getImageNodeNode();
					
			XMLUtil.putValue(imageNode,"keyRef/collectionName",refColl);
			XMLUtil.putValue(imageNode,"keyRef/genId",refGenId);
			XMLUtil.putValue(imageNode,"IMG","{binary {$1}}");
						
		 	StringBuffer xQuery = new StringBuffer("declare binary-encoding none; ");
			xQuery.append("tig:insert-document( 'tig:///" + Constants.CATALOG_DB + "/" + Constants.IMAGES_COLL + "',");
			xQuery.append(XMLUtil.convertToString(imageNode,true) + ")");
					
			queryRunner.executeQueryWithStream(xQuery.toString(), stream);
	}
	
	 
	 public static void delete(String refColl, String refGenId) throws PersistanceException {
		 
		 	StringBuffer xQuery = new StringBuffer();
			xQuery.append("for $a in collection( 'tig:///" + Constants.CATALOG_DB + "/" + Constants.IMAGES_COLL + "')");
			xQuery.append(" where  $a/Image/keyRef/collectionName='" + refColl + "'");
			xQuery.append(" and  $a/Image/keyRef/genId='" + refGenId + "'");
			xQuery.append(" return tig:delete-document(document-uri( $a ))");
			queryRunner.executeQuery(xQuery.toString());
	}
	 
	 	
	 
	public String  saveAsFile() {
		
		return CommonUtil.saveImageFile(stream);
	}
			
  
}
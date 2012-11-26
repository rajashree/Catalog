
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
 * Location information collecting from the reques form.
 * 
 *  
 * 
 */

public class Location 
{
    private static Log log = LogFactory.getLog(Location.class);
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	private Node locationNode; 
	
	public Location(HttpServletRequest request){
		locationNode =createNodeFromRequest(request);
	}
	
	
	public Location(Node node){
		locationNode = node;
	}
	
	
	public Node getNode() {
		return locationNode;
	}
	
	public String getGenId() {
		
		return XMLUtil.getValue(locationNode,"genId");
	}
	
	public static Location find(String genId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/TradingPartnerLocation/genId='");
		buff.append( genId);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.LOCATION_COLL,buff.toString());
		if(!list.isEmpty()) {
			System.out.println(" Before returning find : "  );
			return new Location(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	
	public void insert(String refColl, String refGenId) throws PersistanceException {
		XMLUtil.putValue(locationNode,"genId", CommonUtil.getGUID());
		XMLUtil.putValue(locationNode,"keyRef/collectionName",refColl);
		XMLUtil.putValue(locationNode,"keyRef/genId",refGenId);
		
		PersistanceUtil.insertDocument(locationNode,Constants.LOCATION_COLL);
	}
	
	public void update(String refColl, String refGenId) throws PersistanceException {
		
		XMLUtil.putValue(locationNode,"keyRef/collectionName",refColl);
		XMLUtil.putValue(locationNode,"keyRef/genId",refGenId);
		
		StringBuffer buff = new StringBuffer("$a/TradingPartnerLocation/genId='");
		buff.append( XMLUtil.getValue(locationNode,"genId"));
		buff.append("'");
		PersistanceUtil.updateDocument(locationNode,Constants.LOCATION_COLL,buff.toString());
	}
	
	
	public static List  getList(String refColl, String refGenId) throws PersistanceException {
		
		if(CommonUtil.isNullOrEmpty(refGenId) || CommonUtil.isNullOrEmpty(refColl))
			return new ArrayList();
		
		StringBuffer buff = new StringBuffer();
		buff.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.LOCATION_COLL + "')");
		buff.append(" where  $a/TradingPartnerLocation/keyRef/collectionName='" + refColl + "'");
		buff.append(" and  $a/TradingPartnerLocation/keyRef/genId='" + refGenId + "'");
		buff.append(" return $a/* ");
				
		return queryRunner.executeQuery(buff.toString());
	}
	
	
    private  Node createNodeFromRequest(HttpServletRequest request) {
	   		
		Node tpLocationNode = XMLStructure.getLocationNode();
		
		XMLUtil.putValue(tpLocationNode,"genId",request.getParameter("locationGenId") );
		XMLUtil.putValue(tpLocationNode,"name",request.getParameter("name") );
		XMLUtil.putValue(tpLocationNode,"GLN",request.getParameter("GLN"));
		XMLUtil.putValue(tpLocationNode,"description",request.getParameter("description") );
		XMLUtil.putValue(tpLocationNode,"GIAI",request.getParameter("GIAI") );
		XMLUtil.putValue(tpLocationNode,"GPS",request.getParameter("GPS") );
		XMLUtil.putValue(tpLocationNode,"type",request.getParameter("type") );
		XMLUtil.putValue(tpLocationNode,"event",request.getParameter("event") );
		
		XMLUtil.putValue(tpLocationNode,"latitude",request.getParameter("latitude") );
		XMLUtil.putValue(tpLocationNode,"longitude",request.getParameter("longitude") );

		XMLUtil.putValue(tpLocationNode,"phone",request.getParameter("phone") );
		XMLUtil.putValue(tpLocationNode,"fax",request.getParameter("fax") );

		XMLUtil.putValue(tpLocationNode,"address/line1",request.getParameter("line1") );
		XMLUtil.putValue(tpLocationNode,"address/line2",request.getParameter("line2") );
		XMLUtil.putValue(tpLocationNode,"address/city",request.getParameter("city") );
		XMLUtil.putValue(tpLocationNode,"address/state",request.getParameter("state") );
		XMLUtil.putValue(tpLocationNode,"address/country",request.getParameter("country") );
		XMLUtil.putValue(tpLocationNode,"address/zip",request.getParameter("zip") );
		
		
		XMLUtil.putValue(tpLocationNode,"inFormat",request.getParameter("inFormat") );
		XMLUtil.putValue(tpLocationNode,"outFormat",request.getParameter("outFormat") );
		
		
		return tpLocationNode;
	}
	
	  
}

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

public class TradingPartner 
{
    private static Log log = LogFactory.getLog(TradingPartner.class);
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	private Node tradingNode; 
	
	public TradingPartner(HttpServletRequest request){
		tradingNode =createNodeFromRequest(request);
	}
	
	
	public TradingPartner(Node node){
		tradingNode = node;
	}
	
	
	public Node getNode() {
		return tradingNode;
	}
	
	public String getGenId() {
		
		return XMLUtil.getValue(tradingNode,"genId");
	}
	
	public static TradingPartner find(String genId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/TradingPartner/genId='");
		buff.append( genId);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.TRADING_PARTNER_COLL,buff.toString());
		if(!list.isEmpty()) {
			System.out.println(" Before returning find : "  );
			return new TradingPartner(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	
	public void insert() throws PersistanceException {
		XMLUtil.putValue(tradingNode,"genId", CommonUtil.getGUID());
		PersistanceUtil.insertDocument(tradingNode,Constants.TRADING_PARTNER_COLL);
	}
	
	public void update() throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/TradingPartner/genId='");
		buff.append( XMLUtil.getValue(tradingNode,"genId"));
		buff.append("'");
		PersistanceUtil.updateDocument(tradingNode,Constants.TRADING_PARTNER_COLL,buff.toString());
	}
	
	
	public static List  getTradingPartnerList() throws PersistanceException {
		
		StringBuffer buff = new StringBuffer();
		buff.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.TRADING_PARTNER_COLL + "') order by $a//name");
		buff.append(" return $a/* ");
				
		return queryRunner.executeQuery(buff.toString());
	}
	
	
    private  Node createNodeFromRequest(HttpServletRequest request) {
	   		
		Node tradingPartnerNode = XMLStructure.getTradingPartnerNode();
		
		XMLUtil.putValue(tradingPartnerNode,"genId",request.getParameter("tpGenId") );
		XMLUtil.putValue(tradingPartnerNode,"name",request.getParameter("name") );
		XMLUtil.putValue(tradingPartnerNode,"partnerType",request.getParameter("partnerType"));
		XMLUtil.putValue(tradingPartnerNode,"businessId",request.getParameter("businessId") );
		//XMLUtil.putValue(tradingPartnerNode,"idType",request.getParameter("idType") );
		XMLUtil.putValue(tradingPartnerNode,"status",request.getParameter("status") );
		XMLUtil.putValue(tradingPartnerNode,"webURL",request.getParameter("webURL") );
		XMLUtil.putValue(tradingPartnerNode,"description",request.getParameter("description") );
		
		XMLUtil.putValue(tradingPartnerNode,"contact",request.getParameter("contact") );
		XMLUtil.putValue(tradingPartnerNode,"title",request.getParameter("title") );
		XMLUtil.putValue(tradingPartnerNode,"deaNumber",request.getParameter("deaNumber") );

		XMLUtil.putValue(tradingPartnerNode,"phone",request.getParameter("phone") );
		XMLUtil.putValue(tradingPartnerNode,"fax",request.getParameter("fax") );

		XMLUtil.putValue(tradingPartnerNode,"email",request.getParameter("email") );
		XMLUtil.putValue(tradingPartnerNode,"notifyURI",request.getParameter("notifyURI") );

		XMLUtil.putValue(tradingPartnerNode,"address/line1",request.getParameter("line1") );
		XMLUtil.putValue(tradingPartnerNode,"address/line2",request.getParameter("line2") );
		XMLUtil.putValue(tradingPartnerNode,"address/city",request.getParameter("city") );
		XMLUtil.putValue(tradingPartnerNode,"address/state",request.getParameter("state") );
		XMLUtil.putValue(tradingPartnerNode,"address/country",request.getParameter("country") );
		XMLUtil.putValue(tradingPartnerNode,"address/zip",request.getParameter("zip") );
		
		return tradingPartnerNode;
	}
	
	  
}
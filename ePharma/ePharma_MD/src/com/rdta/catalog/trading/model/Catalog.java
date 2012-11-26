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
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.SchemaTree;
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

public class Catalog 
{
    private static Log log = LogFactory.getLog(Catalog.class);
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	private Node catalogNode; 
	
	public Catalog(HttpServletRequest request) throws PersistanceException{
		catalogNode =createNodeFromRequest(request);
	}
	
	
	public Catalog(Node node){
		catalogNode = node;
	}
	
	
	public Node getNode() {
		return catalogNode;
	}
	
	public SchemaTree getSchemaTree() {
		Node node = XMLUtil.getNode(catalogNode,"schema/*");
		return new SchemaTree(node);
	}
	
	public String getGenId() {
		
		return XMLUtil.getValue(catalogNode,"catalogID");
	}
	

/*	public   boolean search(String catalogName)throws PersistanceException{
		log.info("Catalog Search method");
		System.out.println("Catalog Search method");
		String query = "tlsp:SearchCatalogName('"+catalogName+"')";
		List list = ((List)queryRunner.executeQuery(query));
		ByteArrayInputStream stream = (ByteArrayInputStream) list.get(0);
		int k = stream.read();
		System.out.println("K contains this "+k);
		if (k==48){
				 return true;
		}else
			return false;
	}*/
	
	
	
/*	public static Catalog findByName(String Name) throws PersistanceException {
		
		
		StringBuffer buff = new StringBuffer("$a/Catalog/catalogName='");
		buff.append(Name);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.CATALOG_COLL,buff.toString());
		if(!list.isEmpty()) {
			System.out.println(" Before returning find : "  );
			return new Catalog(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	*/

	public   boolean search(String catalogName)throws PersistanceException{
		log.info("Catalog Search method");
	//	System.out.println("Catalog Search method");
		String query = "tlsp:SearchCatalogName('"+catalogName+"')";
		List list = ((List)queryRunner.executeQuery(query));
		ByteArrayInputStream stream = (ByteArrayInputStream) list.get(0);
		int k = stream.read();
	//	System.out.println("K contains this "+k);
		if (k==48){
				 return true;
		}else
			return false;
	}
/*	public   boolean search(String catalogName)throws PersistanceException{
		log.info("Catalog Search method");
		System.out.println("Catalog Search method");
		String query = "tlsp:SearchCatalogName('"+catalogName+"')";
		List list = ((List)queryRunner.executeQuery(query));
		ByteArrayInputStream stream = (ByteArrayInputStream) list.get(0);
		int k = stream.read();
		System.out.println("K contains this "+k);
		if (k==48){
				 return true;
		}else
			return false;
	}*/
	
	
	public static Catalog findByName(String Name) throws PersistanceException {
		
		
		StringBuffer buff = new StringBuffer("$a/Catalog/catalogName='");
		buff.append(Name);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.CATALOG_COLL,buff.toString());
		if(!list.isEmpty()) {
		//	System.out.println(" Before returning find : "  );
			return new Catalog(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	

	public static Catalog find(String genId) throws PersistanceException {
		
		
		StringBuffer buff = new StringBuffer("$a/Catalog/catalogID='");
		buff.append(genId);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.CATALOG_COLL,buff.toString());
		if(!list.isEmpty()) {
		//	System.out.println(" Before returning find : "  );
			return new Catalog(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	
	/*//Here ist the code to find master Catelogs
	public static Catalog findMasterCatalogs(String genId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/Catalog/genId='");
		buff.append( genId);
		buff.append("'");
		buff.append("and $a/Catalog/keyRef/collectionName=System");
		List list = PersistanceUtil.findDocument(Constants.CATALOG_COLL,buff.toString());
		if(!list.isEmpty()) {
			System.out.println(" Before returning find : "  );
			return new Catalog(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}*/
	
	
	public void insert(String refColl, String refGenId) throws PersistanceException {
		XMLUtil.putValue(catalogNode,"catalogID", CommonUtil.getGUID());
		XMLUtil.putValue(catalogNode,"keyRef/collectionName",refColl);
		XMLUtil.putValue(catalogNode,"keyRef/tradingPartnerID",refGenId);
		
		PersistanceUtil.insertDocument(catalogNode,Constants.CATALOG_COLL);
	}
	
	public void update(String refColl, String refGenId) throws PersistanceException {
		
		XMLUtil.putValue(catalogNode,"keyRef/collectionName",refColl);
		XMLUtil.putValue(catalogNode,"keyRef/tradingPartnerID",refGenId);
		XMLUtil.putValue(catalogNode,"updatedDate",CommonUtil.dateToTLString(new Date()));
	//	System.out.println("CATALOG "+XMLUtil.convertToString(catalogNode));
		StringBuffer buff = new StringBuffer("$a/Catalog/catalogID='");
		buff.append( XMLUtil.getValue(catalogNode,"catalogID"));
		buff.append("'");
		PersistanceUtil.updateDocument(catalogNode,Constants.CATALOG_COLL,buff.toString());
	}
	
	
	public static List  getList(String refColl, String refGenId) throws PersistanceException {
		
		if(CommonUtil.isNullOrEmpty(refGenId) || CommonUtil.isNullOrEmpty(refColl))
			return new ArrayList();
		
		StringBuffer buff = new StringBuffer();
		buff.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.CATALOG_COLL + "')");
		buff.append(" where  $a/Catalog/keyRef/collectionName='" + refColl + "'");
		buff.append(" and  $a/Catalog/keyRef/tradingPartnerID='" + refGenId + "'");
		buff.append(" return $a/* ");
				
		return queryRunner.executeQuery(buff.toString());
	}
	
	
	//Here Is the code to retrieve the Standard Catalog Data  
	public static List  getStandardCatalogList(String refColl,String refGenId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer();
		buff.append("for $i in collection('tig:///CatalogManager/Catalog')");
		buff.append(" where $i/Catalog/keyRef/collectionName = 'System'");
	//	buff.append(" and  $i/Catalog/keyRef/genId='" + refGenId + "'");
		buff.append(" return $i/*");
		/*buff.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.CATALOG_COLL + "')");
		buff.append(" where  $a/Catalog/keyRef/collectionName='" + refColl + "'");
		buff.append(" and  $a/Catalog/keyRef/genId='" + refGenId + "'");
		buff.append(" return $a/* ");*/
		return queryRunner.executeQuery(buff.toString());
	}
	
	public static List getStandardCatalogList(String refColl)throws PersistanceException{
		StringBuffer buff = new StringBuffer();
	/*	buff.append("for $i in collection('tig:///CatalogManager/Catalog')");
		buff.append(" where $i/Catalog/keyRef/collectionName = 'System'");
		buff.append(" return $i/*");*/
		
		buff.append("for $i in collection('tig:///CatalogManager/Catalog')");
		buff.append("where $i/Catalog/keyRef/collectionName= 'System' ");
		buff.append(" return $i/*");
	
		return queryRunner.executeQuery(buff.toString());
		
	}
	
	public static List  getList() throws PersistanceException {
				
		StringBuffer buff = new StringBuffer();
		buff.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.CATALOG_COLL + "')");
		buff.append(" return $a/* ");
				
		return queryRunner.executeQuery(buff.toString());
	}
	
/*	
	 private  Node createNodeFromRequest(HttpServletRequest request) {
   		
	Node tpCatalogNode = XMLStructure.getCatalogNode();
	
	System.out.println("catalogName ="+ request.getParameter("catalogName"));
	
	XMLUtil.putValue(tpCatalogNode,"catalogID",request.getParameter("catalogGenId") );
	XMLUtil.putValue(tpCatalogNode,"catalogName",request.getParameter("catalogName") );
	XMLUtil.putValue(tpCatalogNode,"description",request.getParameter("description") );
	
//	XMLUtil.putValue(tpCatalogNode,"createdDate",CommonUtil.dateToTLString(new Date()));
//	XMLUtil.putValue(tpCatalogNode,"updatedDate",CommonUtil.dateToTLString(new Date()));
		
	
	return tpCatalogNode;
}
	*/
	
	
	
    private  Node createNodeFromRequest(HttpServletRequest request) throws PersistanceException {
	   		
		Node tpCatalogNode = XMLStructure.getCatalogNode();
		Helper helper=new Helper();
		String str = request.getParameter("operationType");
		Node EAG=null;
		
		if(str == null)str="";
		String ets="";
		if(str.equalsIgnoreCase(OperationType.UPDATE)){
			
			String id = request.getParameter("catalogGenId");
		//	System.out.println("Going to Update "+id);
			ets=helper.updateePharmaEAGTimeStamp(id);
			EAG=XMLUtil.parse(ets);
			XMLUtil.putValue(tpCatalogNode,"catalogID",request.getParameter("catalogGenId") );
		}else 
		if(str.equalsIgnoreCase(OperationType.ADD)){
			
			ets=helper.getePharmaEAGTimeStamp();
			EAG=XMLUtil.parse(ets);
		
		}
		
	//	System.out.println("catalogName ="+ request.getParameter("catalogName"));
	//	System.out.println("description ="+ request.getParameter("description"));
	//	System.out.println("catalogID ="+ request.getParameter("catalogGenId"));
		
		
		XMLUtil.putValue(tpCatalogNode,"catalogName",request.getParameter("catalogName") );
		XMLUtil.putValue(tpCatalogNode,"description",request.getParameter("description") );
		
		XMLUtil.putNode(tpCatalogNode,"/Catalog",EAG);
	// 	XMLUtil.putValue(tpCatalogNode,"createdDate",CommonUtil.dateToTLString(new Date()));
	 //	XMLUtil.putValue(tpCatalogNode,"updatedDate",CommonUtil.dateToTLString(new Date()));
			
		
		return tpCatalogNode;
	}
	
	  
}
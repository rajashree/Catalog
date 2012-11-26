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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
 * Trading Partner Form information collecting from the request form.
 * 
 *  
 * 
 */

public class ProductMaster 
{
    private static Log log = LogFactory.getLog(ProductMaster.class);
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	private Node productMasterNode; 
	
	public ProductMaster(HttpServletRequest request){
		productMasterNode =createNodeFromRequest(request);
	}
	
	
	public ProductMaster(Node node){
		productMasterNode = node;
	}
	
	
	public Node getNode() {
		return productMasterNode;
	}
	
	public String getGenId() {
		return XMLUtil.getValue(productMasterNode,"genId");
	}
	
	public static ProductMaster find(String genId) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer("$a/Product/genId='");
		buff.append( genId);
		buff.append("'");
		List list = PersistanceUtil.findDocument(Constants.PRODUCT_MASTER_COLL,buff.toString());
		if(!list.isEmpty()) {
			System.out.println(" Before returning find : "  );
			return new ProductMaster(XMLUtil.parse((InputStream) list.get(0)) );
		} else {
			return null;
		}
	}
	
	public void insert() throws PersistanceException {
	//	XMLUtil.putValue(productMasterNode,"genId", CommonUtil.getGUID());
		
		PersistanceUtil.insertDocument(productMasterNode,Constants.PRODUCT_MASTER_COLL);
		System.out.println("I am also called");
	}
	
	public void insert(String cid) throws PersistanceException {
		XMLUtil.putValue(productMasterNode,"genId", CommonUtil.getGUID());
		XMLUtil.putValue(productMasterNode,"refKey/catalogID",cid);
		PersistanceUtil.insertDocument(productMasterNode,Constants.PRODUCT_MASTER_COLL);
		
	}
	
	
	public void update() throws PersistanceException {
		StringBuffer buff = new StringBuffer("$a/Product/genId='");
		buff.append( XMLUtil.getValue(productMasterNode,"genId"));
		buff.append("'");
		
		System.out.println("I am in the Update "+buff);
		PersistanceUtil.updateDocument(productMasterNode,Constants.PRODUCT_MASTER_COLL,buff.toString());
	}
	
	
	public static List  getProductList() throws PersistanceException {
		
		StringBuffer buff = new StringBuffer();
		buff.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.PRODUCT_MASTER_COLL + "')");
		buff.append(" return $a/* ");
				
		return queryRunner.executeQuery(buff.toString());
	}
	
	public static List  getProductList(String criteria) throws PersistanceException {
		
		StringBuffer buff = new StringBuffer();
		buff.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.PRODUCT_MASTER_COLL + "')");
		buff.append(" where " + criteria);
		buff.append(" return $a/* ");
				
		return queryRunner.executeQuery(buff.toString());
	}
	//Add by Arun
	// This method is used to get the data from the ProductMaster Collection
	// to include in the kit.
	public static List getAddList(String refProductId) throws PersistanceException{
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.PRODUCT_MASTER_COLL + "')/Product ");
		buffer.append(" where $a/genId = '" +refProductId+"'");
		buffer.append(" return ");
		buffer.append("<result>{ ");
		buffer.append("<ProductName>{data($a/ProductName)}</ProductName>,");
		buffer.append("<NDC>{data($a/NDC)}</NDC>,");
		buffer.append("<Dosage>{data($a/DosageForm)}</Dosage>, ");
		buffer.append("<Manufacturer>{data($a/ManufacturerName)}</Manufacturer>");
		buffer.append("}</result> ");
		System.out.println("Query we r apply ing is "+buffer);
		return queryRunner.executeQuery(buffer.toString());
	}
	
	public static List getKitProductList(String refProductId) throws PersistanceException{
		
		StringBuffer buffer= new StringBuffer();
		buffer.append("<result>{for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.PRODUCT_MASTER_COLL + "')/Product ");
		buffer.append(" where $a/genId = '" +refProductId+"'");
		buffer.append(" return  $a//IncludeProduct ");
		buffer.append("} </result> ");
		System.out.println("Query we r apply ing is "+buffer);
		
		
		return queryRunner.executeQuery(buffer.toString());
	}
	
	
	public static List removeProductEntry(String kitName,String ProductName)throws PersistanceException{
		
	
		
		
		
		return queryRunner.executeQuery("");
	}
	
	
	
	
	
	
	public void addKitInformation(HttpServletRequest request)
	{
		
		 	//This method adds Include Element Node Under IncludeElements in the Kit
		     // I am creating Product,Ndc,Dosage,Manufacturer,Quantiy elemnts 
		     // And searching for the existing IncludeProducts elements
		     //If the length =1 I am inserting the elements
		  
		List addedList =(List)request.getSession().getAttribute("showKitnow");
		if( addedList == null){
			System.out.println("addedList is null");
		}
		
		
		for(int s=0;s<addedList.size();s++){
			Node listNode = (Node) addedList.get(s);
			String quantity []=request.getParameterValues("text9");
			
			   Document rootDocument = productMasterNode.getOwnerDocument();
			   
			   Element ProductName = rootDocument.createElement("ProductName");
			   System.out.println("Node Value is this"+XMLUtil.getValue(listNode,"ProductName"));
			   Element NDC = rootDocument.createElement("NDC");
			   Element Dosage = rootDocument.createElement("Dosage");
			   Element Manufacturer = rootDocument.createElement("ManufacturerName");
			   Element quant = rootDocument.createElement("Quantity");
			   Element lotnumber = rootDocument.createElement("LotNumber");	
			   Node node = XMLUtil.getNode(productMasterNode,"IncludeProducts");
				if(node.hasChildNodes())
			{
				NodeList list = node.getChildNodes();
				int length = list.getLength();
				for(int i=0;i<length;i++){
					if(list.item(i).hasChildNodes())
					{	System.out.println("It has also child nodes");
						NodeList list1 =list.item(i).getChildNodes();
						if(list1.getLength()<2){
							Node node1= list.item(i);
							node1.appendChild(ProductName);
							XMLUtil.putValue(node1,"ProductName",XMLUtil.getValue(listNode,"ProductName"));
							node1.appendChild(NDC);
							XMLUtil.putValue(node1,"NDC",XMLUtil.getValue(listNode,"NDC"));
							node1.appendChild(Dosage);
							XMLUtil.putValue(node1,"Dosage",XMLUtil.getValue(listNode,"Dosage"));
							node1.appendChild(Manufacturer);
							XMLUtil.putValue(node1,"ManufacturerName",XMLUtil.getValue(listNode,"Manufacturer"));
							node1.appendChild(quant);
							XMLUtil.putValue(node1,"Quantity",quantity[s]);
							XMLUtil.putValue(node1,"LotNumber",XMLUtil.getValue(listNode,"LotNumber"));
							node1.appendChild(lotnumber);
							log.info("I am inserted node in the kit");
							
							break;
							
						}
					}
				}
			}
			}
			
			
			
			
		}
		
	
	public void  addProductRef(String refProductId) throws PersistanceException{
		
		System.out.println("I am called");
		Document rootDocument = productMasterNode.getOwnerDocument();
		Element refProduct = rootDocument.createElement("IncludeProduct");
		Element refProductGenId = rootDocument.createElement("genId");
		
		refProduct.appendChild(refProductGenId);
		XMLUtil.putValue(refProduct,"genId",refProductId);
		/*	Element ProductName = rootDocument.createElement("ProductName");
		Element NDC = rootDocument.createElement("NDC");
		Element Dosage = rootDocument.createElement("Dosage");
		Element Manufacturer = rootDocument.createElement("Manufacturer");
		refProduct.appendChild(ProductName);
		refProduct.appendChild(NDC);
		refProduct.appendChild(Dosage);
		refProduct.appendChild(Manufacturer);
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("for $a in collection('tig:///"+ Constants.CATALOG_DB  +"/" + Constants.PRODUCT_MASTER_COLL + "')/Product ");
		buffer.append(" where $a/genId = '" +refProductId+"'");
		buffer.append(" return ");
		buffer.append("<result>{ ");
		buffer.append("<ProductName>{data($a/LegendDrugName)}</ProductName>,");
		buffer.append("<NDC>{data($a/NDC)}</NDC>,");
		buffer.append("<Dosage>{data($a/DosageForm)}</Dosage>, ");
		buffer.append("<Manufacturer>{data($a/ManufacturerName)}</Manufacturer>");
		buffer.append("}</result> ");
		
		
		
		
		List list1 = queryRunner.executeQuery(buffer.toString());
		log.info("The query in ProductMaster contains the list object :"+list1);		
		
		XMLUtil.putValue(refProduct,"NDC",temp[1]);
		XMLUtil.putValue(refProduct,"Dosage",temp[2]);
		XMLUtil.putValue(refProduct,"Manufacturer",temp[3]);
		
		*/
		
		//get the parent node and append
		
		Node refProducts = XMLUtil.getNode(productMasterNode,"IncludeProducts");
		if(refProducts != null)
			refProducts.appendChild(refProduct);		
	
	
	}
	
/*	public void addQuantity(String quantity)
	{
		System.out.println("adding quantity");
		Document rootDocument = productMasterNode.getOwnerDocument();
		Element quant = rootDocument.createElement("Quantity");
	
		Node node = XMLUtil.getNode(productMasterNode,"refProducts");
		if(node.hasChildNodes())
		{
			NodeList list = node.getChildNodes();
			int length = list.getLength();
			System.out.println("It has child nodes");
			for(int i=0;i<length;i++){
				if(list.item(i).hasChildNodes())
				{	System.out.println("It has also child nodes");
					NodeList list1 =list.item(i).getChildNodes();
					if(list1.getLength()<2){
						Node node1= list.item(i);
						node1.appendChild(quant);
						XMLUtil.putValue(node1,"Quantity",quantity);
					System.out.println("I came till arun here"+quantity);
					}
				}
			}
		}*/
		/*if (node == null)
			System.out.println("Hi I am null");
		else{
			Node node1 = XMLUtil.getNode(node,"refProduct");
			node1.appendChild(quant);
			XMLUtil.putValue(node1,"Quantity",quantity);
			
		}
	}*/
	
	public void deleteProductRef(String refProductId) {
		
		List list = XMLUtil.executeQuery(productMasterNode,"refProducts/refProduct[genId='"+ refProductId +"']");
		
		for(int i=0; i< list.size(); i++) {
			Node removedNode = (Node) list.get(i);
			Node parentNode = removedNode.getParentNode();
			parentNode.removeChild(removedNode);
		}
	}
	
	public void upDateRequestInfo(HttpServletRequest request) {
		
				
		XMLUtil.putValue(productMasterNode,"genId",request.getParameter("productGenId") );
		XMLUtil.putValue(productMasterNode,"isKit",request.getParameter("isKit") == null ? "No":request.getParameter("isKit") );
		XMLUtil.putValue(productMasterNode,"EPC",request.getParameter("EPC") );
		XMLUtil.putValue(productMasterNode,"ParentEPC",request.getParameter("ParentEPC"));
		XMLUtil.putValue(productMasterNode,"GTIN",request.getParameter("gtin") );
		XMLUtil.putValue(productMasterNode,"NDC",request.getParameter("NDC") );
		XMLUtil.putValue(productMasterNode,"ProductName",request.getParameter("ProductName") );
		XMLUtil.putValue(productMasterNode,"DosageForm",request.getParameter("DosageForm") );
		XMLUtil.putValue(productMasterNode,"DosageStrength",request.getParameter("DosageStrength") );
		
		XMLUtil.putValue(productMasterNode,"ContainerSize",request.getParameter("ContainerSize") );
		XMLUtil.putValue(productMasterNode,"LotNumber",request.getParameter("LotNumber") );

		XMLUtil.putValue(productMasterNode,"LotExpireDate",request.getParameter("LotExpireDate") );
		XMLUtil.putValue(productMasterNode,"PackageUPC",request.getParameter("PackageUPC") );

		XMLUtil.putValue(productMasterNode,"MarketingStatus",request.getParameter("MarketingStatus") );
		XMLUtil.putValue(productMasterNode,"Description",request.getParameter("Description") );

		XMLUtil.putValue(productMasterNode,"Quantity",request.getParameter("Quantity") );
		XMLUtil.putValue(productMasterNode,"Quantity/@quantityUnitCode",request.getParameter("quantityUnitCode") );
		
		XMLUtil.putValue(productMasterNode,"ManufacturerName",request.getParameter("ManufacturerName") );
		XMLUtil.putValue(productMasterNode,"ManufacturerLicense",request.getParameter("ManufacturerLicense") );
		XMLUtil.putValue(productMasterNode,"CustodyLicenseNumber",request.getParameter("CustodyLicenseNumber") );
		XMLUtil.putValue(productMasterNode,"TagKillCode",request.getParameter("TagKillCode") );
		XMLUtil.putValue(productMasterNode,"EnvironmentalConstraints",request.getParameter("EnvironmentalConstraints") );
		
		XMLUtil.putValue(productMasterNode,"Overt/Packagings/Packaging/Info",request.getParameter("OvertPackagingInfo") );
		XMLUtil.putValue(productMasterNode,"Overt/ProductMarkings/ProductMarking/Info",request.getParameter("OvertProductMarkingInfo") );
	
		
	}
	
    private  Node createNodeFromRequest(HttpServletRequest request) {
	   		
		Node productMaster = XMLStructure.getProductNode();
	//	XMLUtil.putValue(productMaster,"genId",request.getParameter("productGenId") );
		XMLUtil.putValue(productMaster,"genId",CommonUtil.getGUID());
		XMLUtil.putValue(productMaster,"isKit",request.getParameter("isKit") == null ? "No":request.getParameter("isKit") );
		XMLUtil.putValue(productMaster,"EPC",request.getParameter("EPC") );
		XMLUtil.putValue(productMaster,"ParentEPC",request.getParameter("ParentEPC"));
		//I changed this for temporary purpos only
		if(request.getParameter("gtin")!=null){
			XMLUtil.putValue(productMaster,"GTIN",request.getParameter("gtin") );
			
		}else
		XMLUtil.putValue(productMaster,"GTIN",request.getParameter("gtin") );
		
		XMLUtil.putValue(productMaster,"NDC",request.getParameter("NDC") );
		//I changed this for temporary purpose only
		if ( request.getParameter("ProductName")!=null){
			XMLUtil.putValue(productMaster,"ProductName",request.getParameter("ProductName") );
			
		}else
		XMLUtil.putValue(productMaster,"ProductName",request.getParameter("ProductName") );
		
		XMLUtil.putValue(productMaster,"DosageForm",request.getParameter("DosageForm") );
		XMLUtil.putValue(productMaster,"DosageStrength",request.getParameter("DosageStrength") );
		
		XMLUtil.putValue(productMaster,"ContainerSize",request.getParameter("ContainerSize") );
		XMLUtil.putValue(productMaster,"LotNumber",request.getParameter("LotNumber") );

		XMLUtil.putValue(productMaster,"LotExpireDate",request.getParameter("LotExpireDate") );
		XMLUtil.putValue(productMaster,"PackageUPC",request.getParameter("PackageUPC") );

		XMLUtil.putValue(productMaster,"MarketingStatus",request.getParameter("MarketingStatus") );
		XMLUtil.putValue(productMaster,"Description",request.getParameter("Description") );

		XMLUtil.putValue(productMaster,"Quantity",request.getParameter("Quantity") );
		XMLUtil.putValue(productMaster,"Quantity/@quantityUnitCode",request.getParameter("quantityUnitCode") );
		
		XMLUtil.putValue(productMaster,"ManufacturerName",request.getParameter("ManufacturerName") );
		XMLUtil.putValue(productMaster,"ManufacturerLicense",request.getParameter("ManufacturerLicense") );
		XMLUtil.putValue(productMaster,"CustodyLicenseNumber",request.getParameter("CustodyLicenseNumber") );
		XMLUtil.putValue(productMaster,"TagKillCode",request.getParameter("TagKillCode") );
		XMLUtil.putValue(productMaster,"EnvironmentalConstraints",request.getParameter("EnvironmentalConstraints") );
	
		XMLUtil.putValue(productMaster,"Overt/Packagings/Packaging/Info",request.getParameter("OvertPackagingInfo") );
		XMLUtil.putValue(productMaster,"Overt/ProductMarkings/ProductMarking/Info",request.getParameter("OvertProductMarkingInfo") );
		System.out.println("******** created Kit Node ******************");
		System.out.println(XMLUtil.convertToString(productMaster));
		System.out.println("************************************************");
		
		return productMaster;
	}
	
	  
}
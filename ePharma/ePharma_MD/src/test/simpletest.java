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

 
/*
 * Created on Sep 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test;

import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.rdta.util.config.DomCompare;
import com.rdta.catalog.PersistanceUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.util.config.DomCompare;

/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class simpletest {
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

	public static void main(String[] args) throws Exception {
		 List list = queryRunner.executeQuery("for $I in collection('tig:///CatalogManager/Catalog') where $I/Catalog/genId =14824259512284240276843648253917" 
		 		+" return $I//schema ");
		 
		 System.out.println(" catalogGenId  : " +list.size());
		String catalogGenId = "14824259512284240276843648253917";
		
		System.out.println(" catalogGenId  : " + catalogGenId);
		//String fromModule = request.getParameter("fromModule");

	//	String targetUrl = request.getParameter("targetUrl");
	//	String display = request.getParameter("display");
        String standardCatalogGenId = "32248822880028878528147088736221";
		 
		/*	if(catalogGenId.startsWith("Standard")) {
				isStandard = true;
		}*/

         
	  
		
		//List catalogNodeList = PersistanceUtil.findDocument("Catalog","catalogGenId='14824259512284240276843648253917'");
		//List standardCatalogNode= PersistanceUtil.findDocument("Catalog","catalogGenId='32248822880028878528147088736221'");
		
       // System.out.println(" standardCatalogNode XML String  : "  + XMLUtil.convertToString(standardCatalogNode));
		//System.out.println(" catalogNode XML String  : "  + XMLUtil.convertToString(catalogNode) );
       
        //Node collectionName = XMLUtil.getNode(catalogNode, "//collectionName");
        
      
     /*   System.out.println("Before IF CONDITION ");
        if(XMLUtil.getValue(collectionName).equals("System"))
        {
        System.out.println(" IN THE IF CONDITION ");
        	isStandard = true;
        }
        
         System.out.println(" After IF CONDITION" );
      
		Node node = XMLUtil.getNode(catalogNode, "schema/*");
        Node standardNode= XMLUtil.getNode(standardCatalogNode,"schema/*");
        
        
        
        
		String jsString = "";

		if(node == null) {
			System.out.println(" Node schema is null");
			jsString = "[ Attributes Undefined ]";
		} else {
			System.out.println(" Node schema is not null");
			JavaScriptTree tree = null;

			if(targetUrl != null && !targetUrl.trim().equals("")) {
				//in case of mapping construct tree passing different target url
				targetUrl = targetUrl + "?display=" + display + "&";
				tree = new JavaScriptTree(node,catalogGenId,targetUrl);
			} else {
			  //schema edit functionality
			  tree = new JavaScriptTree(node,catalogGenId,"SchemaElementEdit.do?");
			}

			jsString = tree.toJSString();
		}

	System.out.println(" JS TREE --- " + jsString);
*/
	}
}

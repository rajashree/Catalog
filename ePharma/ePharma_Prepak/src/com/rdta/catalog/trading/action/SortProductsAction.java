
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;


import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.commons.xml.XMLUtil;

public class SortProductsAction extends Action {
	
private static Log log=LogFactory.getLog(ProductMasterSearchAction.class);
private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();	
    
public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	
    	String operationType = request.getParameter("operationType");
    	String productName = request.getParameter("ProductName");
    	    	
    	log.info("Operation Type is "+operationType);
    	log.info("Operation Type is :"+operationType);
    	log.info("ProductName Type is "+productName);
    	
    	StringBuffer Query = new StringBuffer();
    	
    	Query.append("<IncludeProducts> {");
    	Query.append("for $i in collection ('tig:///CatalogManager/ProductMaster')/Product/IncludeProducts/IncludeProduct");
		Query.append(" where $i/../../ProductName = '"+productName+"'");
		
    	if(operationType.equals("Quantity"))
    	{
    		Query.append(" order by xs:integer($i/"+operationType+")");	
    	}
    	else
    	{
    		Query.append(" order by ($i/"+operationType+")");
    	}
    	
    	Query.append(" return "); 
		Query.append(" <IncludeProduct> "); 
		Query.append(" <ProductName>{data($i/ProductName)}</ProductName> ");
		Query.append(" <NDC>{data($i/NDC)}</NDC> ");
		Query.append(" <Dosage>{data($i/Dosage)}</Dosage> ");
		Query.append(" <Manufacturer>{data($i/Manufacturer)}</Manufacturer> ");
		Query.append(" <Quantity>{data($i/Quantity)}</Quantity> ");
		Query.append(" </IncludeProduct> } ");
		Query.append("</IncludeProducts>");
    	
    	log.info("The Query is "+Query);
    	List result = queryRunner.executeQuery(Query.toString());
    	
    	log.info("The result of Query is "+result);
    	
    	for(int i=0;i<result.size();i++)
    	{
    		log.info("The Contents of List are "+result.get(i));
    	}
    	
    	Node node = XMLUtil.parse((ByteArrayInputStream)result.get(0));
    	request.setAttribute("sortedProducts",node);
    	log.info("The Contents of Node are "+node);
    	
    	return mapping.findForward("success");
    	
    }
}

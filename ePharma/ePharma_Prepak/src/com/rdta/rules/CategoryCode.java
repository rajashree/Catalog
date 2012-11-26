
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


package com.rdta.rules;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.rdta.rules.Util.PersistanceUtil;
import com.rdta.commons.xml.XMLUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rdta.commons.CommonUtil;

/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CategoryCode 
{

	private static Log log = LogFactory.getLog(CategoryCode.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String code = "";
	String parentCode = "";
	String description  = ""; 
	
	Node categoryCodeNode;
	
	public CategoryCode(Node node){
		categoryCodeNode=node;
	}
	
	public Node getNode(){
		return categoryCodeNode;
	}
	
	public CategoryCode(HttpServletRequest request){
		categoryCodeNode=createCategoryCodeFromRequest(request);
	}

	private Node createCategoryCodeFromRequest(HttpServletRequest request){
		
		Node ccNode = RulesXMLStructure.getCategoryCode();
		XMLUtil.putValue(ccNode,"Code",request.getParameter("code"));
		XMLUtil.putValue(ccNode,"ParentCode",request.getParameter("parentCode"));
		XMLUtil.putValue(ccNode,"Description",request.getParameter("description"));
		
		return ccNode;
	}
	
	public void insert() throws Exception {
		PersistanceUtil.insertDocument(categoryCodeNode,"Category");
	}
	
	public void update(String cd, String pcd) throws Exception {
		String condition = "$a/CategoryDefn/Code='"+cd+"' and $a/CategoryDefn/ParentCode='"+pcd+"'"  ;
		PersistanceUtil.updateDocument(categoryCodeNode,"CategoryCode",condition);
	}	

}

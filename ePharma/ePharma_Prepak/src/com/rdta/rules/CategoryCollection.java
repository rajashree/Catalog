/*
 * Created on Aug 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

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

/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CategoryCollection 
{
	
	String parentValue = "";
	String append = "-";
	String AD     = "','"; 
	
	QueryRunnerFactory qrunFac = QueryRunnerFactory.getInstance();
	QueryRunner qrun           = qrunFac.getDefaultQueryRunner();	
	
	public void createCategory(String parent, 
								 String child, 
								 String desc,
								 String createdBy, 
								 String createdOn,
								 String updatedBy, 
								 String updatedOn ) throws Exception									 
	{
		String input = "'" + parent + AD + child + AD +  desc + 
						AD + createdBy + AD + createdOn + AD + updatedBy +
						AD + updatedOn + "'";  
		List lt = qrun.executeQuery("tlsp:addCategoryProc("+input+")");
	}
	

	public String getListCategories() throws Exception
	{
		String ret="";
		//ArrayList al = (ArrayList)qrun.returnExecuteQueryStrings("tlsp:listCategoriesProc()");
		List al = qrun.returnExecuteQueryStrings("tlsp:listCategoriesProc()");
		
		for (Iterator itr = al.iterator(); itr.hasNext();)
		{
			String s1 = (String)itr.next();
			ret = ret + s1;
		}
		
		return ret;
	}
	
	public String getParentCodeList() throws Exception
	{
		String ret="";

		List al = qrun.returnExecuteQueryStrings("tlsp:listCategoriesProc()");
		
		for (Iterator itr = al.iterator(); itr.hasNext();)
		{
			String s1 = (String)itr.next();
			ret = ret + s1;
		}
		
		return ret;
	}
	
	//test
	public static void main(String args[]) throws Exception 
	{
		CategoryCollection catcl = new CategoryCollection();
		catcl.createCategory("a1","b1","c","d","e","f","g");

		System.out.println("Value of list is-> "+ catcl.getListCategories());
	}
}

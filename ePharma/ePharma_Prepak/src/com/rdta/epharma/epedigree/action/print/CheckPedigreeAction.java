//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

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


package com.rdta.epharma.epedigree.action.print;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/** 
 * MyEclipse Struts
 * Creation date: 05-05-2006
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 * @struts.action-forward name="repackage" path="/repackagePDF"
 * @struts.action-forward name="initial" path="/pedigree2129"
 */
public class CheckPedigreeAction extends Action {

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static final Log log = LogFactory.getLog(CheckPedigreeAction.class);
	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)throws Exception {

		log.info("Inside method execute");
		String pedigreeID = request.getParameter("pedigreeID"); 
		
		String query="tlsp:getRepackagedInfo('"+pedigreeID+"')";
		
		
		try {
			log.info("Query"+query);
			String str=queryRunner.returnExecuteQueryStringsAsString(query);
			log.info("QueryResult"+str);
			
			if(str !=null){
				Node n1=XMLUtil.parse(str);
				Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
				Node n3 = XMLUtil.getNode(n2,"descendant::repackagedPedigree");
				
				if(n3 != null){
					return mapping.findForward("repackage");
				}
			}
		} catch (PersistanceException e) {
			log.error("Error in execute()" + e);
			throw new PersistanceException(e);	
		}
		return mapping.findForward("initial");
	}

}


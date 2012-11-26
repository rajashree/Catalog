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

 
package com.rdta.epharma.epedigree.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.commons.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class ChangeStatusAction  extends Action
{
	
	String CreatedDate = null;
	String CreatedBy = null;
	String MessageID = null;
	String RelatedProcess = null;
	String Status = null;
	String xml = null;
	String userID = null;
	
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		
		
		
		String Status = null;
		
		String query = "";
		
		
		
		
		
		/*
		StringBuffer buff = new StringBuffer();
		
		String query ="let $i := (for $x in collection('tig:///EAGRFID/UserSessions')/session ";
			   query = query + " where $x/sessionid = '"+sessionID+"'";
			   query = query + " return $x/userid) ";
			   query = query + "for $s in collection('tig:///EAGRFID/SysUsers')/user ";
			   query = query + "where $s/id = $i ";
			   query = query + "return <root>{ concat(data($s/id),\",\",$s/username)}</root>";
			  			  		
			   List list1 = queryRunner.executeQuery(query);
			   Node node = XMLUtil.parse((ByteArrayInputStream)list1.get(0));
		       String temp= XMLUtil.getValue(node);
			   int k = 0;
			   userID = temp.substring(0,(k=temp.indexOf(",")));
			   CreatedBy = temp.substring(k+1,temp.length());
			   System.out.println("User Id"+userID+"CreatedBy"+CreatedBy);
			   			   
			  query = "for $x in collection('tig:///ePharma/APN')//APN ";
			  query = query + "where $x/DocumentId = '"+DocID+"' ";
			  query = query + "return <root> { data($x/Pedigrees/Pedigree/PedigreeStatus/Status/Status) } </root>";
			  
			  List list4 = queryRunner.executeQuery(query);
			  Node node2 = XMLUtil.parse((ByteArrayInputStream)list4.get(0));
			  String Status = XMLUtil.getValue(node2);
			   
			  String query1 = "for $x in collection('tig:///EAGRFID/SysVocabulary')/SysVocabulary ";
				     query1 = query1 + "where $x/Category/CategoryName='Pedigree Exception' ";
				     query1 = query1 + "return <root> { concat(data($x/Category/Terms/Status),\",\",$x/Category/Terms/RelatedEvents/EventName)}</root>";
					
			List list2 = queryRunner.executeQuery(query1);
			Node node1 = XMLUtil.parse((ByteArrayInputStream)list2.get(0));
		       String temp1= XMLUtil.getValue(node1);
			   int m=0;
			   String sd = temp1.substring(0,(m=temp1.indexOf(",")));
			   RelatedProcess=temp1.substring(m+1,temp1.length());
			   
			System.out.println("Status: "+Status+"Related Process: "+RelatedProcess);
			
		buff.append("tlsp:CreateMessage('"+CreatedDate+"','"+CreatedBy+"','"+MessageID+"','"+Roles+"'," +
				"'"+userID+"','"+RelatedProcess+"','"+Title+"','"+DocType+"','"+DocID+"','"+Level+"','"+RequiredAction+"','"+Comments+"','"+Status+"') ");
		
		System.out.println("query for insert message: "+buff);
		List list3 = queryRunner.executeQuery(buff.toString());
				*/
		return mapping.findForward("success");
   }
	
}
	
	

		



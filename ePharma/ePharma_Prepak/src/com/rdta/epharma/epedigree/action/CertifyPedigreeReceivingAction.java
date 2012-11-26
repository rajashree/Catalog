
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class CertifyPedigreeReceivingAction extends Action {
	
	private static Log log = LogFactory.getLog(CertifyPedigreeReceivingAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String apnId;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
		
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String CertifyType = "Certify Pedigree";			
		try{	
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			System.out.println("validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			System.out.println("sessionID in Action :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if (!validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
		
				
		//getting userid, username
		String query = "for $i in collection('tig:///EAGRFID/SysSessions') where $i/session/sessionid='"+sessionID+"' " ; 
		query = query + " return  <root> { concat($i/session/userid,',',$i/session/username) } </root>";
		System.out.println("The query for user id and name is "+query);
		List list1 = queryRunner.executeQuery(query.toString());
		Node node = XMLUtil.parse((ByteArrayInputStream)list1.get(0));
		System.out.println("The result of query for user id and name is "+list1.get(0));
		String temp1= XMLUtil.getValue(node);
		int m=0;
		String userid = temp1.substring(0,(m=temp1.indexOf(",")));
		String username = temp1.substring(m+1,temp1.length());
		
		//apnid
		apnId = request.getParameter("check");
		System.out.println("The APNID from form in Certifying is "+apnId);
		
		//For user Values
		String  query1 = " for $j in collection('tig:///EAGRFID/SysUsers') ";
		query1 = query1 + " where $j/User/UserID='"+userid+"' ";
		query1 = query1 + " return <root> { concat($j/User/UserRole,',',$j/User/Phone,',',$j/User/Email) } </root> ";
		
		List list2 = queryRunner.executeQuery(query1);
		Node node1 = XMLUtil.parse((ByteArrayInputStream)list2.get(0));
		String temp2= XMLUtil.getValue(node1);
		int m1=0;
		int n1=0;
		System.out.println("The temp1 value "+temp2);
		String title = temp2.substring(0,(m1=temp2.indexOf(",")));
		String phone = temp2.substring(m1+1,(n1=temp2.lastIndexOf(",")));
		String email = temp2.substring(n1+1,temp2.length());
		
		System.out.println("The Title(UserRole) of user is "+title);
		System.out.println("The phone of user is "+phone);
		System.out.println("The email of user is "+email);
		
		
		//For Access Priveleges.
			
		List accessList1 = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','8.0','Insert')");
		String insertStatus8 = accessList1.get(0).toString();
		System.out.println("The insertStatus is "+insertStatus8);
			
		if(insertStatus8.equalsIgnoreCase("false")){
			return mapping.findForward("failure");
		}
			
			
		//for date
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyy-MM-dd");
		String tmDate = df.format(new java.util.Date());
		df.applyPattern("hh:mm:ss");
		String tmTime = df.format(new java.util.Date());
		String CreatedDate = tmDate + "T" + tmTime;
		System.out.println("The Generated date time from Certify Pedigree is "+CreatedDate);
				
		//EAG-TimeStamp
		String buff = "tlsp:CreateEAG-TimeStampType()";
		List list3 = queryRunner.returnExecuteQueryStrings(buff.toString());
		String timestamp = list3.get(0).toString();
		System.out.println("The EAG TIMESTAMP from ACTION is "+timestamp);
		
		
		if(apnId != null){
		
			String CertifyPedigree = "<CertifyPedigree>";
			CertifyPedigree = CertifyPedigree + "<Authenticator>";
			CertifyPedigree = CertifyPedigree +"<Name>"+username+"</Name>";
			CertifyPedigree = CertifyPedigree +"<Date>"+CreatedDate+"</Date>";
			CertifyPedigree = CertifyPedigree +"<Title>"+title+"</Title>";
			CertifyPedigree = CertifyPedigree +"<TelephoneNumber>"+phone+"</TelephoneNumber>";
			CertifyPedigree = CertifyPedigree +"<Email>"+email+"</Email>";
			CertifyPedigree = CertifyPedigree +"</Authenticator>";
			CertifyPedigree = CertifyPedigree +"<APNDocumentId>"+apnId+"</APNDocumentId>";
			CertifyPedigree = CertifyPedigree +"<CertifyType>"+CertifyType+"</CertifyType>";
			CertifyPedigree = CertifyPedigree + timestamp;
			CertifyPedigree = CertifyPedigree +"</CertifyPedigree>";
			
			System.out.println("The EAG SESSION XML from ACTION is "+CertifyPedigree);
			
			String insertQuery = "tig:insert-document( 'tig:///ePharma/CertifyPedigree/', "+CertifyPedigree+")";
			System.out.println("The insert query is "+insertQuery);
			
			queryRunner.executeQuery(insertQuery);
			
		}
		
		}catch(Exception ex){
    		ex.printStackTrace();
    		log.error("Error in OrderSearchAction execute method........." +ex);
    		return mapping.findForward("exception");
		}
		return mapping.findForward("success");
	}
	
}

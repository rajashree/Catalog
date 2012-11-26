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

 
package com.rdta.security;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

public class SecurityServiceImpl implements SecurityService {
	
	private static Log log = LogFactory.getLog("SecurityServiceImpl");
	
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

	/**
	 * a real implementation would retrieve this data from a persistent store such as a file system, relational database, or LDAP server.
	 */
	public SecurityServiceImpl() {
	} 

	private User getUser(String username, HttpServletRequest request)throws PersistanceException{				
		log.info("In getUser method");
		String query="tlsp:loginUserInfo_MD('"+username+"')";
		log.info("Query:"+query);
		try {
			String userData=queryRunner.returnExecuteQueryStringsAsString(query);
			if(userData!=null&&!(userData.trim().equals(""))){
				Node user=XMLUtil.parse(userData);				
				String userID = XMLUtil.getValue(user,"/User/UserID");
				String password = XMLUtil.getValue(user,"/User/Password");
				String firstName = XMLUtil.getValue(user,"/User/FirstName");
	            String lastName = XMLUtil.getValue(user,"/User/LastName");
	            String tp_company_nm = XMLUtil.getValue(user,"/User/BelongsToCompany");
	            boolean disable = Boolean.valueOf((XMLUtil.getValue(user,"/User/Disable").trim())).booleanValue();
	            String tp_company_id = "";
	            if(tp_company_nm != null && !tp_company_nm.trim().equals("")){
	                tp_company_id = XMLUtil.getValue(user,"/User/businessId");
	            }else{
	                tp_company_nm = "";  
	            }	            
	            return new User(userID,username,password,firstName,lastName,request.getRemoteAddr(),tp_company_id,tp_company_nm,disable);
			}
			
		} catch (PersistanceException e) {
			log.error("Error in getUser .."+e);
			throw new PersistanceException(e);					
		}	
		return null;
	}

	
	public String replaceString(String s, String one, String another) {		
		log.info("In replaceString method");		
        if (s.equals("")) return "";
        String res = "";
        int i = s.indexOf(one,0);
        int lastpos = 0;
        while (i != -1) {
          res += s.substring(lastpos,i) + another;
          lastpos = i + one.length();
          i = s.indexOf(one,lastpos);
        }
        res += s.substring(lastpos);  
        return res;
        }

	public void insertSessionDoc(User user, HttpServletRequest request)throws PersistanceException {
		
		log.info("In insertSessionDoc method");		
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyyMMdd");
		String idDate = df.format(new java.util.Date());
		df.applyPattern("hhmmss");
		String idTime = df.format(new java.util.Date());
		String sessionID = replaceString(request.getRemoteAddr(), ".", "")+idDate+idTime;
 	
		df.applyPattern("yyyy-MM-dd");
		String screenEnteredDate = df.format(new java.util.Date());
		df.applyPattern("HH:mm:ss");
		String screenEnteredTime = df.format(new java.util.Date());
		String screenEnteredDT = screenEnteredDate+"T"+screenEnteredTime;
    	
		StringBuffer sessXML =  new StringBuffer("<session>");
		sessXML.append("<sessionid>"+sessionID+"</sessionid>");
		sessXML.append("<userid>"+user.getUserid()+"</userid>");
		sessXML.append("<username>"+user.getFirstname()+" "+user.getLastname()+"</username>");
		sessXML.append("<accesslevel>epedigree</accesslevel>");
		sessXML.append("<sessionstart>"+screenEnteredDT+"</sessionstart>");
		sessXML.append("<userip>"+request.getRemoteAddr()+"</userip>");
		sessXML.append("<lastuse>"+screenEnteredDT+"</lastuse>");
		sessXML.append("<tp_company_nm>"+user.getTp_company_nm()+"</tp_company_nm>");
		sessXML.append("<tp_company_id>"+user.getTp_company_id()+"</tp_company_id>");
		sessXML.append("</session>");
		
		String sessQuery = "tig:insert-document( 'tig:///EAGRFID/SysSessions/', "+sessXML.toString()+")";
		log.info("Query:"+sessQuery);
		try {
			queryRunner.executeUpdate(sessQuery);
		} catch (PersistanceException e) {
			log.error("error:"+e);			
			throw new PersistanceException(e);					
		}
		request.getSession(false).setAttribute("sessionID",sessionID);
	}

	public User authenticate(String username, String password, HttpServletRequest request) throws AuthenticationException,PersistanceException {

		log.info("In authenticate method");
		
		//		We need to get data related to user from database...
	    User userobj = (User)getUser(username,request);
	    
	    
	    if (userobj == null) 
	      throw new AuthenticationException("Unknown user");
	    
	    //Check whether password entered is correct 
	    boolean passwordIsValid =userobj.passwordMatch(password);
	    if (!passwordIsValid)
	      throw new AuthenticationException("Invalid password");
	    
	    //Check if user authentication is enabled
	    
	    if(userobj.isDisable()){
	    	
	      throw new AuthenticationException("User login is disabled");
	    }
	    
	    return userobj;
	}
	
}
	
	


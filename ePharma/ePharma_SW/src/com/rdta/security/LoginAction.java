
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

import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;


public final class LoginAction extends Action {
	
	private static Log log = LogFactory.getLog("LoginAction");
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	  public ActionForward execute(ActionMapping mapping,
			  					ActionForm form,
			  					HttpServletRequest request,
			  					HttpServletResponse response)throws Exception{
		log.info("In execute method");
		HttpSession session = request.getSession();	    
	    String username = request.getParameter("uname");
        String password = request.getParameter("password");        
	    SecurityService service = new SecurityServiceImpl();	   
	    //Here is the code to authenticate user
	    User user = service.authenticate(username, password,request);
	    
	    //Check if user has already logged In from different system...
	    
	    ServletContext context=servlet.getServletContext();
	    Hashtable userTable = (Hashtable)context.getAttribute("currentUsers");	   
	    Iterator it = userTable.values().iterator();
	    while(it.hasNext()){
	    	User userobj=(User)it.next();
	    	if(userobj.getUsername().equals(username)){
	    		if(!userobj.getClientIP().equals(request.getRemoteAddr())){
	    			//If user has logged in from the different system
	    			throw new AuthenticationException("Already Logged in");
	    		}else{
	    			//delete the session object ....
	    			try{
	    				String  sessQuery="tlsp:deleteInvlidUserByID('"+user.getUserid()+"')";
	    				log.info("Query:"+sessQuery);
	    				queryRunner.executeUpdate(sessQuery);
	    			}catch(PersistanceException p){
	    				log.error("Error in execute ->LoginAction :"+p);
	    				throw new PersistanceException(p);
	    			}
	    		}
	    	}	    	
	    }
	    	    
	    //storing the user object to session 
	   	session.setAttribute("web_user", user);
	    
	    //Inserting the session document
	    if(user!=null){
	    	service.insertSessionDoc(user,request);
	    }
	    
	    /*//Setting session time out
	    String str=SessionTimeInfo.getSessionTimeOut();
    	session.setAttribute("sessionTime",str);
    	double sessionTime=Double.parseDouble(str);
	    session.setMaxInactiveInterval(((int)sessionTime*60 ));
	    */  
	    
	    // Forward control to this Action's success forward
	    return mapping.findForward("success");
	  }
	}

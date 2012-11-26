
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


package com.rdta.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.MessageResources;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.error.TLException;
import com.rdta.eag.epharma.util.SendDHFormEmail;

/**
 *
 *	@author     <a href="mailto:developer_email">Johnson Joseph</a>
 *
 *	Date:		Apr 21, 2006 3:32:43 PM
 *     
 *	@version    $Id :$
 *
 *	@since   	REL1.0
 */


/**
 * ActionExceptionHandler represents an implementation to Exception handling.
 */

public class ActionExceptionHandler  extends ExceptionHandler{

	/** The commons <code>logger</code> instance for this class. */
	private final static Log logger = LogFactory.getLog(ActionExceptionHandler.class);
		
		/**
		 * Class Stores the exception message details into the error log file.
		 * 
		 * @param ex 
		 * 				Exception
		 * @param exConfig
		 * 				ExceptionConfig
		 * @param mapping
		 * 				ActionMapping
		 * @param formInstance
		 * 				ActionForm
		 * @param request
		 * 				HttpServletRequest	
		 * @param response
		 * 				HttpServletResponse
		 * @throws ServletException
		 */
	
		public ActionForward execute( Exception ex, 
            					ExceptionConfig exConfig,
            					ActionMapping mapping,
            					ActionForm formInstance,
            					HttpServletRequest request,
            					HttpServletResponse response) throws ServletException {
			
			logger.info("Deatils of Error with TL messages..");	
			ActionForward forward=null;
			try {
				String strorigKey = exConfig.getKey(); 
				String strMessage=null;
				String strErrCode="";
				ActionMessage actMessage = null; 
				String strproperty = null;			
				if(ex instanceof IOException)
					logger.info("Exception :"+ex.getMessage());			
				else if(ex instanceof PersistanceException){								
					strErrCode =((TLException)ex).getCode();
					strMessage=((TLException)ex).getMessage();
					actMessage = new ActionMessage(strErrCode);
					logger.info("Error Code :"+strErrCode);
					logger.info("Error Message :"+((TLException)ex).getMessage());
					logger.info("Full Message :"+((TLException)ex).getTLException());							
				}
				else if(ex instanceof Exception){
					logger.info("Error Message :"+ex.getMessage());
					logger.info("Stack Trace :"+ex.getClass());
				}else
					logger.info("Error Message :"+ex.getMessage());
								
				forward = super.execute(
						new Exception(),exConfig,mapping,formInstance,request,response);
				if(forward == null) 
			    forward = mapping.findForward("error");											
				logger.info("Definition :"+actMessage);			
				if (actMessage==null)
					actMessage = new ActionMessage("errors.none");
				strproperty = actMessage.getKey();
				storeException(request, strproperty, actMessage);
			
				actMessage = new ActionMessage("errors.resp");
				strproperty = actMessage.getKey();
				storeException(request, strproperty, actMessage);						
				MessageResources messages = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);				
		
				SendDHFormEmail.sendMailToSupport(messages.getMessage("eMail.Support.From"),messages.getMessage("eMail.Support.To"),messages.getMessage("eMail.Support.SMTP"),messages.getMessage("eMail.Support.Subject"),ex.getMessage(),messages.getMessage("eMail.Support.Username"),messages.getMessage("eMail.Support.Password"));								
			} catch (Exception e) {						
				logger.info("Error in ActionExceptionHandler :"+e);
			}
			return forward;			
		}

		protected void storeException(HttpServletRequest request,
									String strproperty,
									ActionMessage message) {
			ActionMessages messages =(ActionMessages) request.getAttribute(Globals.ERROR_KEY);		
			messages.add(strproperty, message);
			request.setAttribute(Globals.ERROR_KEY, messages);
		}
    
	



}

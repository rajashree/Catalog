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



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.trading.model.Location;


import org.w3c.dom.Node;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.Constants;
import com.rdta.commons.persistence.PersistanceException;

/**
 * 
 * 
 * 
 */
public class LocationListAction extends Action
{
    private static Log log=LogFactory.getLog(LocationListAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 
		try{
		log.info(" Inside LocationListAction Action, tpGenId: " + request.getParameter("tpGenId"));
		
		List list = Location.getList(Constants.TRADING_PARTNER_COLL,request.getParameter("tpGenId"));

		//set the result here
		request.setAttribute("LocationListInfo",list);
		}catch(PersistanceException e){
			log.error("Error in LocationListAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in LocationListAction execute method........." +ex);
    		throw new Exception(ex);
		}
		 
		return mapping.findForward("success");
   }
	

   
  
}
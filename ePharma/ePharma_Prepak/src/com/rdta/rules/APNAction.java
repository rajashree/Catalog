
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


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import  com.rdta.commons.persistence.*;

import  com.rdta.commons.xml.*;
import com.rdta.eag.signature.CreateSig;
import com.rdta.eag.signature.verify.VerifySig;

import java.util.*;
import java.io.*;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class APNAction extends Action
{


        public ActionForward execute(ActionMapping mapping, ActionForm form,
                                                                 HttpServletRequest request,
                                                                 HttpServletResponse response)
                                                                throws Exception
                                                                {

				try{

					CreateAPNAction cp = new CreateAPNAction();
					String docType = (String) request.getParameter("docType");
					String id = (String) request.getParameter("id");
						cp.execute(docType,id);



					return mapping.findForward("success");
				} catch (Exception e) {
					e.printStackTrace();
					return mapping.findForward("success");
				}
				}


}














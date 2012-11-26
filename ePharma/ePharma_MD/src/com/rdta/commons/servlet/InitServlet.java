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

 
package com.rdta.commons.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import com.rdta.commons.CommonUtil;

/**
 * @author asangha
 * 
 * This servlet gets initialized when the application starts. The configuration
 * to start this is in web.xml file. This performs the EAG initialization.
 *  
 */
public class InitServlet extends HttpServlet {

    public void init() {
        String prefix = getServletContext().getRealPath("."); 
		System.out.println("value of prefix " + prefix);
        String file = getInitParameter("log4j-init-file"); // if the log4j-init-file is not set, then no point in trying 
        if(file != null) { 
            CommonUtil.initLog4J(prefix,file);
        }  
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }
}

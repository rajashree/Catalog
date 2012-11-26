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

 
package com.rdta.Admin.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import com.rdta.Admin.servlet.RepConstants;
import com.rdta.commons.CommonUtil;
import com.rdta.tlapi.boa.Session;

public class LogServlet extends HttpServlet {
	private static Logger logger = null;
	private	String realPath = null;
    private String webInfDir = null;

    public void init() {
        String prefix = getServletContext().getRealPath("."); 
		System.out.println("value of prefix " + prefix);
        String file = getInitParameter("log4j-init-file"); // if the log4j-init-file is not set, then no point in trying 
        if(file != null) { 
            CommonUtil.initLog4J(prefix,file);
        }  
    }

   

    public void init(ServletConfig config) throws ServletException {
		super.init(config);		
		
		logger = Logger.getLogger(com.rdta.Admin.servlet.LogServlet.class);
		realPath = config.getServletContext().getRealPath("/");
        webInfDir = realPath + "WEB-INF" + File.separator;
		System.out.println("Real Path ******: "+realPath);
		System.out.println("Web Inf Path******** : "+webInfDir);
		RepConstants.APPL_PATH=realPath;
		System.out.println("Real Path RepConstants******** : "+RepConstants.APPL_PATH);
        try {
			getServletContext().setAttribute("AppsRealPath",realPath);
			getServletContext().setAttribute("AppsWebInfPath",webInfDir);
			System.out.println("Calling method - initLogging");
			initLogging(config);
        }
        catch(Exception ex)
        {
            System.out.println("Error initilizing - LogServlet " + ex);
            throw new ServletException(ex.getMessage());
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        // do nothing.
    }

    /* This method reads data from log4j.xml file, and initializes
	* the logging part for the application.
    **/
	public void initLogging(ServletConfig config) throws ServletException {		
        String logPath = config.getInitParameter("log4j-log-path");
        logPath = realPath + (logPath == null ? "log" : logPath);
        File logPathDir = new File(logPath);
        if(!logPathDir.exists() && !logPathDir.mkdir())
            throw new ServletException("Couldn't find nor create log directory [" + logPath + "]");
        log("Setting system property [log.home] to [" + logPath + "]");
        System.setProperty("log.home", logPath);
        String log4jConfFile = config.getInitParameter("log4j-configuration");
        log4jConfFile = webInfDir + log4jConfFile;
        if(!(new File(log4jConfFile)).exists())
        {
            log4jConfFile = webInfDir + "log4j.properties";
            if(!(new File(log4jConfFile)).exists())
                log4jConfFile = null;
        }
        if(log4jConfFile == null)
        {
            log("Webapp: log4j basic config");
            BasicConfigurator.configure();
        } else
        if(log4jConfFile.endsWith("xml"))
        {
            log("Webapp: log4j XML config with file [" + log4jConfFile + "]");
            DOMConfigurator.configureAndWatch(log4jConfFile);
        } else
        if(log4jConfFile.endsWith("properties"))
        {
            log("Webapp: log4j properties-file config with file [" + log4jConfFile + "]");
            PropertyConfigurator.configureAndWatch(log4jConfFile);

        }
	}

}
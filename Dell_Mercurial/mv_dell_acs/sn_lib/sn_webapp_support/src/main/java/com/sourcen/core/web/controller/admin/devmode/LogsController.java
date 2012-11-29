/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.controller.admin.devmode;

import com.sourcen.core.util.WebUtils;
import com.sourcen.core.web.controller.BaseController;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2949 $, $Date:: 2012-06-06 09:59:49#$
 */
@Controller
public class LogsController extends BaseController {

    @RequestMapping(value = "/admin/devmode/logs.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView caches_list(HttpServletRequest request) {
        String selectedFile = null;
        Long showLines = WebUtils.getLongParameter(request, "lines", 100L);
        ModelAndView mv = new ModelAndView();
        Enumeration appenders = Logger.getRootLogger().getAllAppenders();
        Collection<String> availableLogFiles = new HashSet<String>();
        while (appenders.hasMoreElements()) {
            Object appenderObj = appenders.nextElement();
            if (appenderObj instanceof FileAppender) {
                FileAppender fileAppender = (FileAppender) appenderObj;
                availableLogFiles.add(fileAppender.getFile());
            }
        }
        if (selectedFile == null && !availableLogFiles.isEmpty()) {
            selectedFile = availableLogFiles.iterator().next();
        }
        // read the logs
        if (selectedFile != null) {
            File file = new File(selectedFile);
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                mv.addObject("errorMessage", e.getMessage());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                mv.addObject("errorMessage", e.getMessage());
            }
        }
        return mv;
    }


}
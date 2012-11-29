/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.controller.admin.devmode;

import com.sourcen.core.util.ArrayUtils;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.web.controller.BaseController;
import net.sf.json.JSONSerializer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: navinr $
 @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$ */
@Controller
public class SystemInfoController extends BaseController {

    private static final Logger logger = LogManager.getLogger(SystemInfoController.class);


    @RequestMapping(value = "/admin/devmode/system-info.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView sys_info(Model model, HttpServletRequest request) {
        Assert.notNull(configurationService);
        ModelAndView mv = new ModelAndView();

        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        Map<Long, Object[]> threadList = new HashMap<Long, Object[]>();

        ThreadInfo[] infos = threadBean.dumpAllThreads(true, true);
        boolean enableThreadFiltering = WebUtils.getBooleanParameter(request, "enableThreadFiltering", true);
        String threadFilterString = WebUtils.getParameter(request, "threadFilterString", "quartzTaskExecutor");

        for (ThreadInfo threadInfo : infos) {
            if ((enableThreadFiltering && !threadInfo.getThreadName().contains(threadFilterString))) {
                continue;
            }
            Object[] data = new Object[4];
            data[0] = threadInfo;
            data[1] = ((threadBean.getThreadCpuTime(threadInfo.getThreadId()) / 1000000000));

            StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
            String[] stackTrace = new String[stackTraceElements.length];
            for (int i = stackTraceElements.length - 1; i > -1; i--) {
                stackTrace[i] = stackTraceElements[i].toString();
            }
            ArrayUtils.reverse(stackTrace);
            data[2] = stackTrace;
            data[3] = JSONSerializer.toJSON(stackTrace).toString();
            logger.info("threadInfo" + data[3]);
            threadList.put(threadInfo.getThreadId(), data);
        }
        model.addAttribute("quartzThreads", threadList);


        /*Map<String, Object> memoryUseage = new HashMap<String, Object>();*/
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        model.addAttribute("heapUsage_committed", FileUtils.byteCountToDisplaySize(heapUsage.getCommitted()));
        model.addAttribute("heapUsage_init", FileUtils.byteCountToDisplaySize(heapUsage.getInit()));
        model.addAttribute("heapUsage_max", FileUtils.byteCountToDisplaySize(heapUsage.getMax()));
        model.addAttribute("heapUsage_used", FileUtils.byteCountToDisplaySize(heapUsage.getUsed()));

        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();
        model.addAttribute("nonHeapUsage_committed", FileUtils.byteCountToDisplaySize(nonHeapUsage.getCommitted()));
        model.addAttribute("nonHeapUsage_init", FileUtils.byteCountToDisplaySize(nonHeapUsage.getInit()));
        model.addAttribute("nonHeapUsage_max", FileUtils.byteCountToDisplaySize(nonHeapUsage.getMax()));
        model.addAttribute("nonHeapUsage_used", FileUtils.byteCountToDisplaySize(nonHeapUsage.getUsed()));


        return mv;
    }

}
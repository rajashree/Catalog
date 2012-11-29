/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

@Controller
public class SystemStatusController extends BaseDellController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConfigurationService configurationService;

    @RequestMapping(value = "/system-status.do", method = RequestMethod.GET)
    public ModelAndView status(Model model, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        int last = request.getRequestURL().lastIndexOf(request.getContextPath());
        // Quick fix - Fetch URL
        model.addAttribute("serverUrl", StringUtils.substring(request.getRequestURL().toString(), 0, last).toString()
                .concat(request.getContextPath()));


        model.addAttribute("serverStatus", request.getContextPath());

        boolean dbServerActive = false;
        Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            if (connection != null) {
                try {
                    stmt = connection.createStatement();
                    dbServerActive = true;
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        model.addAttribute("dbStatus", dbServerActive);
        model.addAttribute("dbUrl", ((ComboPooledDataSource) dataSource).getJdbcUrl());
        String dbUser = ((ComboPooledDataSource) dataSource).getUser();
        String dbPwd = ((ComboPooledDataSource) dataSource).getPassword();
        try {
            if(connection != null && dbServerActive){
                model.addAttribute("dbName", connection.getCatalog());
                model.addAttribute("dbServer", connection.getMetaData().getDatabaseProductName());
                model.addAttribute("dbAutoCommit", connection.getAutoCommit());
//                model.addAttribute("maxConnections", connection.getMetaData().getMaxConnections());
            }
            Long upTimeMillis = ((ComboPooledDataSource) dataSource).getUpTimeMillis(dbUser, dbPwd);
            String upTimeString = String.format("%h:%d:%d ( hh:mm:ss )",
                    TimeUnit.MILLISECONDS.toHours(upTimeMillis),
                    TimeUnit.MILLISECONDS.toMinutes(upTimeMillis),
                    TimeUnit.MILLISECONDS.toSeconds(upTimeMillis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(upTimeMillis))
            );
            model.addAttribute("dbServerUPTime", upTimeString);
        } catch (SQLException e) {
            model.addAttribute("dbName", 0);
            model.addAttribute("dbServer", 0);
            model.addAttribute("dbAutoCommit", 0);
            model.addAttribute("maxConnections", 0);
            model.addAttribute("dbServerUPTime", 0);
            e.printStackTrace();
        }
        // <job_name>, <enabled/disabled>
        Map<String, Object> jobs = new ConcurrentHashMap<String, Object>();
        String key = "";
        for(Map.Entry<String, Object> entry : configurationService.getProperties("com.dell.acs.jobs.").entrySet()){
            // Beautifying jobs name
            key = entry.getKey();
            key = key.replace("com.dell.acs.jobs.", "");
            key = key.replace(".enabled", "");
            // Download Data Files Job .ficstar will be Download Data Files Job - ficstar
            key = key.replace(".", " - ");

            jobs.put(StringUtils.splitCamelCase(key), entry.getValue());
        }
        model.addAttribute("jobs", jobs);
        return mv;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

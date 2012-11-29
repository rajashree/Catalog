/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.spring.servlet;

import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;
import net.sf.json.processors.JsDateJsonValueProcessor;
import net.sf.json.spring.web.servlet.view.JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: nrajkumar $
 * @version $Revision: 0 $, $Date:: 2000-00-00 00:00:01#$
 * A View that renders its model as a JSON object.
 */
public class CustomJsonView extends JsonView {


    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        getJsonConfig().registerJsonValueProcessor(Date.class, new DateValueProcessor());
        getJsonConfig().registerDefaultValueProcessor(Long.class, new DefaultNullValueProcessor());

        if (request.getAttribute("jsonConfig.booleanNullValue") != null
                && request.getAttribute("jsonConfig.booleanNullValue").toString().equalsIgnoreCase("null")) {
            getJsonConfig().registerDefaultValueProcessor(Boolean.class, new DefaultNullValueProcessor());
        }

        super.renderMergedOutputModel(model, request, response);
    }

    private static class DateValueProcessor extends JsDateJsonValueProcessor {

        private DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

        public DateValueProcessor() {
            super();
        }

        @Override
        public Object processArrayValue(Object value, JsonConfig jsonConfig) {
            return super.processArrayValue(value, jsonConfig);
        }

        @Override
        public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
            if (value instanceof Date) {
                return dateFormatter.format((Date) value);
            } else {
                return value == null ? null : value.toString();
            }
        }
    }

    private static class DefaultNullValueProcessor extends DefaultDefaultValueProcessor {
        @Override
        public Object getDefaultValue(Class type) {
            return null;
        }
    }
}

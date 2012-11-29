/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views;

import com.sourcen.core.util.StringUtils;
import com.sourcen.core.web.ws.views.transformer.JsonOutputTransformer;
import com.sourcen.core.web.ws.views.transformer.OutputTransformerFactory;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class JsonView extends net.sf.json.spring.web.servlet.view.JsonView {

    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);

    public JsonView() {
        super();
        setContentType("application/json");
    }

    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        response.setCharacterEncoding("UTF-8");
        if (model.containsKey("webservice_result")) {
            Object returnValue = model.get("webservice_result");
            model.clear();
            JsonOutputTransformer transformer = OutputTransformerFactory.getJsonOutputTransformer(request);
            transformer.setJsonConfig(getJsonConfig());
            Object output = transformer.transform(returnValue);
            String jsonCallback = request.getParameter("jsonp");
            if (jsonCallback == null) {
                jsonCallback = request.getParameter("callback");
            }
            if (jsonCallback != null) {
                jsonCallback = StringUtils.getSimpleString(jsonCallback);
                if (jsonCallback.isEmpty()) {
                    jsonCallback = "parseResponse";
                }
                Writer writer = response.getWriter();
                writer.write(jsonCallback + "(");
                writer.write(output.toString());
                writer.write(");");
            } else {
                if(request.getRequestURI().indexOf("/v2") != -1) {
                    //Adding wrapper for Success
                    JSONObject jsonSuccess = new JSONObject();
                    jsonSuccess.accumulate("status", true);
                    jsonSuccess.accumulate("data", output.toString());
                    response.getWriter().write(jsonSuccess.toString(2));
                } else {
                    response.getWriter().write(output.toString());
                }

            }
        } else if (model.containsKey("error")) {
            //Adding wrapper for Error
            JSONObject jsonError = new JSONObject();
            jsonError.accumulate("status", false);
            jsonError.accumulate("message", model.get("error"));
            response.getWriter().write(jsonError.toString(2));
        } else {
            super.renderMergedOutputModel(model, request, response);
        }
    }

}

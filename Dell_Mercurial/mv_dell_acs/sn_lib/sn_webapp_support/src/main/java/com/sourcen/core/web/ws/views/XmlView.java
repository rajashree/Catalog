/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views;

import com.sourcen.core.web.ws.views.transformer.OutputTransformerFactory;
import com.sourcen.core.web.ws.views.transformer.XmlOutputTransformer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class XmlView extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);

    protected static XStream xstream;

    public XmlView() {
        super();
        setContentType("text/xml");
        if (xstream == null) {
            xstream = new XStream(new Xpp3Driver());
            xstream.setMode(XStream.NO_REFERENCES);
            xstream.autodetectAnnotations(true);
            xstream.aliasPackage("", "com.sourcen.web");
        }
    }

    @Override
    protected void renderMergedOutputModel(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(getContentType());
        // write model to Xml
        Writer writer = response.getWriter();
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<result>\n");
        if (!model.isEmpty()) {
            if (model.containsKey("error")) {
                String systemError = "false";
                if (model.containsKey("systemError")) {
                    systemError = model.get("systemError").toString();
                }
                String errorClass = model.get("errorClass").toString();
                writer.write("<error systemError=\"" + systemError + "\" errorClass=\"" + errorClass + "\">" + model.get("error").toString() + "</error>");
            } else {

                try {

                    String output = "";
                    Object returnValue = model;
                    if (model.containsKey("webservice_result")) {
                        //just process the one model item, ignore rest.
                        returnValue = model.get("webservice_result");
                        model.clear();
                        if (returnValue instanceof Collection && ((Collection) returnValue).isEmpty()) {
                            output = "<list/>";
                        } else {
                            XmlOutputTransformer transformer = OutputTransformerFactory.getXmlOutputTransformer(request);
                            output = transformer.transform(returnValue).toString();
                        }
                    } else {
                        output = xstream.toXML(model);
                    }
                    writer.write(output);
                } catch (Exception e) {
                    writer.write("<error renderingError=\"true\">" + e.getMessage() + "</error>");
                }

            }
        }
        writer.write("\n</result>");
    }

}

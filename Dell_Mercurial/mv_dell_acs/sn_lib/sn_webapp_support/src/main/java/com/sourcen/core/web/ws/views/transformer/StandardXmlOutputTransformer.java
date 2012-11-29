/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views.transformer;

import com.google.common.base.Joiner;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class StandardXmlOutputTransformer extends OutputTransformerAdapter implements XmlOutputTransformer {

    protected static XStream xstream;

    public StandardXmlOutputTransformer() {
        if (xstream == null) {
            xstream = new XStream(new Xpp3Driver());
            xstream.setMode(XStream.NO_REFERENCES);
            xstream.autodetectAnnotations(true);
            xstream.aliasPackage("", "com.sourcen.web");
        }
    }


    @Override
    protected Object transformObject(final Object object) {
        return xstream.toXML(object);
    }

    @Override
    protected Object transformResultCollection(final Collection<Object> collection) {
        return "<list>" + Joiner.on("\n").join(collection) + "</list>";
    }
}

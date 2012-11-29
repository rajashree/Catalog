/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.beans;

import com.sourcen.core.web.ws.beans.base.WSBeanModel;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSProperty extends WSBeanModel {

    private String name;
    private String value;

    public WSProperty(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "WSProperty{"
                + "name='" + name + '\''
                + ", value='" + value + '\''
                + '}';
    }
}

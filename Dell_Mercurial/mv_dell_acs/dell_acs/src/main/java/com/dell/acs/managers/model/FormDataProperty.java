/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.model;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: navinr $
 @version $Revision: 2803 $, $Date:: 2012-06-01 08:33:25#$ */

public class FormDataProperty implements FormData {

    private String name;

    private String value;

    public FormDataProperty(final String name, final String value) {
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
        return "FormBeanProperty{"
                + "name='" + name + '\''
                + ", value='" + value + '\''
                + '}';
    }
}

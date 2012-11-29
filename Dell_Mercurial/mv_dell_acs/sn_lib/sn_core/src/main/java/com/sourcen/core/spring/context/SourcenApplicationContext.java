/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2757 $, $Date:: 2012-05-29 20:44:49#$
 * @since 1.0
 */
public class SourcenApplicationContext extends XmlWebApplicationContext {

    public SourcenApplicationContext() {
    }

    /**
     * we dont want this anyone to start execution until the upgrade is done.
     */
    @Override
    public void refresh() throws BeansException, IllegalStateException {
        super.refresh();
        instance = this;
    }

    private static SourcenApplicationContext instance = null;

    public static SourcenApplicationContext getInstance() {
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException("SourcenApplicationContext has not yet been initialized.");
    }

}

/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2757 $, $Date:: 2012-05-29 20:44:49#$
 */
public class SourcenContextImpl extends XmlWebApplicationContext implements SourcenContext {

    private static final Logger log = LoggerFactory.getLogger(SourcenContextImpl.class);

    @Override
    public void initialize() {
        super.afterPropertiesSet();
    }
}

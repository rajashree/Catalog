/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.spring.context;


import com.sourcen.core.services.DefaultImplementation;
import org.springframework.context.ApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2757 $, $Date:: 2012-05-29 20:44:49#$
 */

@DefaultImplementation(SourcenContextImpl.class)
public interface SourcenContext extends ApplicationContext {

    public void initialize();

    public void destroy();

}

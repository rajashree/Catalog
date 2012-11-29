/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2757 $, $Date:: 2012-05-29 20:44:49#$
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface DefaultImplementation {

    Class value() default Object.class;

    String className() default "";

}

/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectMap {
	/**
	 * If a name is specified, the ObjectMap will 
	 * only be used when referenced by name.
	 * Only one name can exist for a map, if more than
	 * one mapping method has the same name, the last
	 * one found will be utilized. 
	 */
	String name() default "";
}

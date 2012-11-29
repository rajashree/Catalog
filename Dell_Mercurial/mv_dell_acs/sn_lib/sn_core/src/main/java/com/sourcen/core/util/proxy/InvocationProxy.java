/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.proxy;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.InvocationHandler;

public interface InvocationProxy<T> extends InvocationHandler, MethodBeforeAdvice, AfterReturningAdvice {

    void init(Class<?>[] unproxiedObjectInterface, T unproxiedObject);

    T getUnproxiedObject();


}

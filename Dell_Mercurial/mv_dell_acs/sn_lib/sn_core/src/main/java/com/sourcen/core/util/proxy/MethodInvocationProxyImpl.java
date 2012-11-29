/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MethodInvocationProxyImpl<T> implements InvocationProxy<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected T unproxiedObject;

    protected Class<?>[] proxyInterfaces;

    public MethodInvocationProxyImpl() {
    }

    @Override
    public T getUnproxiedObject() {
        return this.unproxiedObject;
    }

    @Override
    public void init(final Class<?>[] proxyInterfaces, final T unproxiedObject) {
        this.proxyInterfaces = proxyInterfaces;
        this.unproxiedObject = unproxiedObject;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        Object returnValue = null;
        try {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Pre-Invocation on " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + ", args:" + args);
            }
            this.before(method, args, proxy);
            returnValue = method.invoke(this.unproxiedObject, args);
            afterReturning(returnValue, method, args, this.unproxiedObject);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Post-Invocation on " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + ", args:" + args);
            }
            afterReturning(returnValue, method, args, this.unproxiedObject);
            return returnValue;
        } catch (final InvocationTargetException e) {
            throw e.getTargetException();
        } catch (final SourcenProxyException e) {
            throw e;
        } catch (final Exception e) {
            throw new InvocationTargetException(e);
        }
    }

    @Override
    public void before(final Method method, final Object[] args, final Object target) throws Throwable {
    }

    @Override
    public void afterReturning(final Object returnValue, final Method method, final Object[] args, final Object target) throws Throwable {
    }

}

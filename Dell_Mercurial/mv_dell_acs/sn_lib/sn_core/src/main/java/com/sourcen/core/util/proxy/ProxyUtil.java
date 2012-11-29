/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.proxy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Navin Raj kumar
 * @see {@code http://cglib.sourceforge.net/xref/samples/JdkCompatibleProxy.html}
 */
public class ProxyUtil {

    // FIXME convert this to use a [BeanName]Proxy as a delegate if thats available.

    private static final Logger log = LoggerFactory.getLogger(ProxyUtil.class);

    public static <T> T createProxy(final Class<?>[] interfaces, final T unproxiedObject) {
        return ProxyUtil.createProxy(ProxyUtil.resolveProxyClass(interfaces, MethodInvocationProxyImpl.class), interfaces, unproxiedObject);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> T createProxy(final Class<? extends InvocationProxy> proxyInvocationHandler, final Class<?>[] unproxiedObjectInterface, final T unproxiedObject) throws SourcenProxyException {

        try {
            final InvocationProxy<T> handler = proxyInvocationHandler.newInstance();
            handler.init(unproxiedObjectInterface, unproxiedObject);
            return (T) Proxy.newProxyInstance(proxyInvocationHandler.getClassLoader(), unproxiedObjectInterface, (InvocationHandler) handler);
        } catch (final InstantiationException e) {
            ProxyUtil.log.warn(e.getMessage(), e);
        } catch (final IllegalAccessException e) {
            ProxyUtil.log.warn(e.getMessage(), e);
        }
        throw new SourcenProxyException("Unable to create proxiedObject using " + proxyInvocationHandler + ", fromSrc :" + unproxiedObject);
    }

    public static Class<? extends InvocationProxy> resolveProxyClass(final Class<?>[] interfaces, final Class<? extends InvocationProxy> defaultProxyHandlerClass) {
        Class<? extends InvocationProxy> resolvedProxyClass = null;
        SourcenProxy annotation = null;
        for (final Class<?> interfaceClass : interfaces) {
            annotation = interfaceClass.getAnnotation(SourcenProxy.class);
            if (annotation != null) {
                break;
            }
        }

        if (annotation != null) {
            final Class<?> proxyClass = annotation.proxyClass();
            if (proxyClass.equals(void.class)) {
                return defaultProxyHandlerClass;
            } else if (InvocationProxy.class.isAssignableFrom(proxyClass)) {
                return (Class<? extends InvocationProxy<?>>) proxyClass;
            }
            throw new SourcenProxyException(proxyClass + " must be a subclass of " + InvocationProxy.class);
        }

        // this is pretty much the base interface object.
        final String proxyClassName = interfaces[0].getName().concat("Proxy");
        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName(proxyClassName);
            resolvedProxyClass = (Class<? extends InvocationProxy<?>>) proxyClass;
        } catch (final ClassNotFoundException e) {
            throw new SourcenProxyException(e);
        } catch (final ClassCastException e) {
            throw new SourcenProxyException(resolvedProxyClass + " must be a subclass of " + InvocationProxy.class, e);
        }

        if (resolvedProxyClass == null) {
            return defaultProxyHandlerClass;
        }
        return resolvedProxyClass;
    }

}

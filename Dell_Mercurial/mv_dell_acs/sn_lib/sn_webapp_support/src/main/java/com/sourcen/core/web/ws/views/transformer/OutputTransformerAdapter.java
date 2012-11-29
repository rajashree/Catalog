/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views.transformer;


import com.sourcen.core.util.StringUtils;
import com.sourcen.core.web.ws.beans.base.WSBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2806 $, $Date:: 2012-06-01 10:40:50#$
 */
public abstract class OutputTransformerAdapter implements OutputTransformer {

    protected abstract Object transformObject(final Object object);

    @Override
    public Object transform(final Object object) {
        if (isWSBean(object)) {
            return transformBean((WSBean) object);
        }
        if (isWSCollection(object)) {
            return transformResultCollection(transformCollection((Collection<WSBean>) object));
        }
        if (isWSMap(object)) {
            return transformResultMap(transformMap((Map) object));
        }
        return transformObject(object);
    }

    private Map transformMap(final Map map) {
        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        for (Object key : map.entrySet()) {
            result.put(transform(key), transform(map.get(key)));
        }
        return result;
    }

    protected static boolean isWSObject(Object object) {
        return isWSBean(object) || isWSCollection(object) || isWSMap(object);
    }

    private static boolean isWSMap(final Object object) {
        if (object instanceof Map) {
            Map map = ((Map) object);
            if (!map.isEmpty()) {
                Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
                return (isWSObject(entry.getKey()) || isWSObject(entry.getValue()));
            }
        }
        return false;
    }

    protected static boolean isWSCollection(Object object) {
        // is Collection?
        if (object instanceof Collection) {
            Collection collection = (Collection) object;
            if (!collection.isEmpty()) {
                if (collection.iterator().next() instanceof WSBean) {
                    return true;
                }
            }
        }
        return false;

    }

    protected static boolean isWSBean(Object object) {
        return (object instanceof WSBean);
    }

    protected Object transformResultCollection(final Collection<Object> collection) {
        return transformObject(collection);
    }

    protected Object transformResultMap(final Map collection) {
        return transformObject(collection);
    }

    protected Collection<Object> transformCollection(final Collection<? extends WSBean> collection) {
        Collection<Object> result = new LinkedList<Object>();
        for (WSBean item : collection) {
            result.add(transformBean(item));
        }
        return result;
    }

    protected Object transformBean(final WSBean bean) {
        return transformObject(bean);
    }

    protected HttpServletRequest request;

    @Override
    public void setHttpServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    protected String determineScope(HttpServletRequest request) {
        String scope = null;
        if (request != null) {
            if (!StringUtils.isEmpty(request.getParameter("scope"))) {
                scope = request.getParameter("scope");
            } else if (request.getAttribute("scope") != null && request.getAttribute("scope") instanceof String) {
                scope = request.getAttribute("scope").toString();
            }
        }
        if(scope == null){
            if(System.getProperty("defaultRequestScope") != null){
                scope = System.getProperty("defaultRequestScope");
            } else{
                scope = "default";
            }
        }
        return scope;
    }
}


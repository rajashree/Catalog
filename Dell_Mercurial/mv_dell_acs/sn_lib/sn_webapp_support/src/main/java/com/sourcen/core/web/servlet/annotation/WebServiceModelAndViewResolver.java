/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.servlet.annotation;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;

import java.lang.reflect.Method;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */

public class WebServiceModelAndViewResolver implements ModelAndViewResolver {

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    private String packageName = "com.sourcen.web.ws";

    @Override
    public ModelAndView resolveModelAndView(Method handlerMethod, Class handlerType, Object returnValue,
                                            ExtendedModelMap implicitModel, NativeWebRequest webRequest) {
        if (handlerType.getCanonicalName().startsWith(packageName)) {
            ModelAndView mav = new ModelAndView();
            mav.getModelMap().put("webservice_result", returnValue);
            return mav;
        }
        return ModelAndViewResolver.UNRESOLVED;
    }
}

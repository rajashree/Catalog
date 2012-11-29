/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.servlet.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2809 $, $Date:: 2012-06-01 11:03:47#$
 */

public class AnnotationMethodHandlerAdapter
        extends org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationMethodHandlerAdapter.class);


    @Override
    protected ModelAndView invokeHandlerMethod(final HttpServletRequest request, final HttpServletResponse response,
                                               final Object handler) throws
            Exception {
        try {
            return super.invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            if (e instanceof MissingServletRequestParameterException) {
                logger.error("handler :=" + handler + ", message:=" + e.getMessage());
            } else {
                logger.error(e.getMessage(), e);
            }
            ModelAndView mv = new ModelAndView();
            Locale locale = localeResolver.resolveLocale(request);
            try {
                mv.addObject("error", messageSource.getMessage(e.getMessage(), null, locale));
            } catch (Exception msgException) {
                mv.addObject("error", e.getMessage());
            }
            mv.addObject("systemError", true);
            mv.addObject("errorClass", e.getClass().getSimpleName());
            return mv;
        }
    }

    private MessageSource messageSource;

    private LocaleResolver localeResolver;

    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocaleResolver(final LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
}

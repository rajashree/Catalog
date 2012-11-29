/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.i18n.spring;

import com.sourcen.core.i18n.I18NService;
import com.sourcen.core.texttemplate.providers.TextTemplateNotFoundException;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class I18NTemplateProviderMessageSourceImpl implements I18NTemplateProviderMessageSource {

    private I18NService _I18NService;


    public I18NService getI18NService() {
        return _I18NService;
    }

    public void setI18NService(I18NService i18NService) {
        _I18NService = i18NService;
    }

    @Override
    public void setParentMessageSource(MessageSource parent) {
        throw new UnsupportedOperationException("we use the i18nService to resolve messages. hence you cannot set a custom parent.");
    }

    @Override
    public MessageSource getParentMessageSource() {
        throw new UnsupportedOperationException("we use the i18nService to resolve messages. hence you cannot set a custom parent.");
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws TextTemplateNotFoundException {
        return getI18NService().get(code, locale, args);
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return getI18NService().get(code, locale, defaultMessage, args);
    }


    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws TextTemplateNotFoundException {
        if (resolvable.getCodes() != null) {
            for (String code : resolvable.getCodes()) {
                try {
                    return getI18NService().get(code, locale, resolvable.getArguments());
                } catch (TextTemplateNotFoundException tnfe) {
                    // ignore and keep trying.
                }
            }
        }
        return StringUtils.formatMessage(resolvable.getDefaultMessage(), locale, resolvable.getArguments());
    }

    @Override
    public void destroy() {
    }

    @Override
    public void initialize() {
        Assert.notNull(getI18NService(), "TemplateProvider cannot be null for I18NTemplateProviderMessageSource.");
    }
}

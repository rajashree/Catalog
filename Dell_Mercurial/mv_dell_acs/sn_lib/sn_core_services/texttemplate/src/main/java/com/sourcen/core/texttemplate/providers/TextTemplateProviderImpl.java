/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate.providers;

import com.sourcen.core.App;
import com.sourcen.core.locale.LocaleService;
import com.sourcen.core.util.StringUtils;

import java.util.Locale;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: ashish $
 @version $Revision: 2866 $, $Date:: 2012-06-04 07:56:37#$ */
public abstract class TextTemplateProviderImpl implements TextTemplateProvider {


    private final LocaleService localeService = App.getService(LocaleService.class);

    private TextTemplateProvider parentDelegate;


    public TextTemplateProviderImpl() {
    }

    @Override
    public String get(String templateId) throws TextTemplateNotFoundException {
        return get(templateId, localeService.getLocale());
    }

    @Override
    public String get(String templateId, Locale locale) throws TextTemplateNotFoundException {
        String message = getMessage(templateId, locale);
        if (message == null) {
            throw new TextTemplateNotFoundException(templateId, locale);
        }
        return message;
    }

    @Override
    public String get(String templateId, Locale locale, Object[] args) throws TextTemplateNotFoundException {
        return StringUtils.formatMessage(get(templateId, locale), locale, args);
    }

    @Override
    public String get(String templateId, Object[] args) throws TextTemplateNotFoundException {
        return StringUtils.formatMessage(get(templateId, localeService.getLocale()), localeService.getLocale(), args);
    }


    @Override
    public String get(String templateId, String defaultMessage) {
        return get(templateId, localeService.getLocale(), defaultMessage);
    }

    @Override
    public String get(String templateId, Locale locale, String defaultMessage) throws TextTemplateNotFoundException {
        return getMessage(templateId, locale, defaultMessage);
    }

    @Override
    public String get(String templateId, Locale locale, String defaultMessage, Object[] args) {
        String result = get(templateId, locale, defaultMessage);
        return StringUtils.formatMessage(result, locale, args);
    }

    @Override
    public String get(String templateId, String defaultMessage, Object[] args) {
        return StringUtils
                .formatMessage(get(templateId, localeService.getLocale(), defaultMessage), localeService.getLocale(),
                        args);
    }

    @Override
    public TextTemplateProvider getParent() {
        return parentDelegate;
    }

    @Override
    public void setParent(TextTemplateProvider provider) {
        this.parentDelegate = provider;
    }

    //
    //
    //

    private String parentName;

    private Integer providerType;

    public Integer getProviderType() {
        if (providerType == null) {
            throw new UnsupportedOperationException("Provider Type has not been set for:=" + this);
        }
        return providerType;
    }

    public void setProviderType(Integer providerType) {
        this.providerType = providerType;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }


    //
    // helpers
    //

    protected String getMessage(String messageId, Locale locale, String defaultMessage) {
        try {
            String result = getMessage(messageId, locale);
            if (result == null) {
                return defaultMessage;
            }
            return result;
        } catch (TextTemplateNotFoundException tnfe) {
            return defaultMessage;
        }
    }

    protected String getMessage(String messageId, Locale locale) {
        String message = null;
        try {
            message = get(getParentName(), messageId, locale);

        } catch (Exception e) {
            // ignore and check with parent.
        }
        if (message == null) {
            if (getParent() != null) {
                return getParent().get(messageId, locale);
            }
        }
        return message;
    }

    protected String formatMessage(String text, Locale locale, Object... replacements) {
        return StringUtils.formatMessage(text, locale, replacements);
    }


    @Override
    public void refresh() {
    }

}

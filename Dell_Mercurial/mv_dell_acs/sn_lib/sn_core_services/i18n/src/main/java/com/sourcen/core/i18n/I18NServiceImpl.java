/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.i18n;

import com.sourcen.core.App;
import com.sourcen.core.i18n.texttemplate.providers.I18NTextTemplateProvider;
import com.sourcen.core.services.ServiceImpl;
import com.sourcen.core.texttemplate.TextTemplateService;
import com.sourcen.core.texttemplate.providers.TextTemplateNotFoundException;
import com.sourcen.core.texttemplate.providers.TextTemplateProvider;

import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2730 $, $Date:: 2012-05-29 12:07:23#$
 */
public class I18NServiceImpl extends ServiceImpl implements I18NService {

    public I18NServiceImpl() {
    }

    @Override
    public String get(String templateId) throws TextTemplateNotFoundException {
        return getProvider().get(templateId);
    }

    @Override
    public String get(String templateId, String defaultMessage) {
        return getProvider().get(templateId, defaultMessage);
    }

    @Override
    public String get(String templateId, Locale locale) throws TextTemplateNotFoundException {
        return getProvider().get(templateId, locale);
    }

    @Override
    public String get(String templateId, Locale locale, String defaultMessage) throws TextTemplateNotFoundException {
        return getProvider().get(templateId, locale, defaultMessage);
    }

    @Override
    public String get(String templateId, Locale locale, Object[] args) throws TextTemplateNotFoundException {
        return getProvider().get(templateId, locale, args);
    }

    @Override
    public String get(String templateId, Locale locale, String defaultMessage, Object[] args) {
        return getProvider().get(templateId, locale, defaultMessage, args);
    }

    @Override
    public String get(String templateId, Object[] args) throws TextTemplateNotFoundException {
        return getProvider().get(templateId, args);
    }

    @Override
    public String get(String templateId, String defaultMessage, Object[] args) {
        return getProvider().get(templateId, defaultMessage, args);
    }

    @Override
    public String get(String parentName, String messageId, Locale locale) {
        return getProvider().get(parentName, messageId, locale);
    }

    @Override
    public Integer getProviderType() {
        return I18NTextTemplateProvider.PROVIDER_TYPE;
    }

    @Override
    public TextTemplateProvider getParent() {
        throw new UnsupportedOperationException("Cannot set a parent provider for the i18NService.");
    }

    @Override
    public void setParent(TextTemplateProvider provider) {
        throw new UnsupportedOperationException("Cannot set a parent provider for the i18NService.");
    }

    protected TextTemplateService _textTemplateService;

    protected TextTemplateProvider getProvider() {
        return getTextTemplateService().getTemplateProvider(I18NTextTemplateProvider.PROVIDER_TYPE);
    }


    public TextTemplateService getTextTemplateService() {
        if (_textTemplateService == null) {
            _textTemplateService = App.getService(TextTemplateService.class);
        }
        return _textTemplateService;
    }

    public void setTextTemplateService(TextTemplateService textTemplateService) {
        this._textTemplateService = textTemplateService;
    }
}

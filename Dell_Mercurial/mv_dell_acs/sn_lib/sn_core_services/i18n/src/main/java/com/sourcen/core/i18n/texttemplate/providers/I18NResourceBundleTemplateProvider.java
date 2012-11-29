/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.i18n.texttemplate.providers;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.texttemplate.providers.TextTemplateNotFoundException;
import com.sourcen.core.texttemplate.providers.TextTemplateProviderImpl;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: ashish $
 * @version $Revision: 2866 $, $Date:: 2012-06-04 07:56:37#$
 */
public class I18NResourceBundleTemplateProvider extends TextTemplateProviderImpl implements I18NTextTemplateProvider {

    private final ResourceBundle.Control bundleControl = new MessageResourceBundleControl();
    private static final String BUNDLE_EXPIRATION_TIME_KEY = "app.i18n.refreshTime";

    private MessageSource _fallbackMessageSource;

    public MessageSource getFallbackMessageSource() {
        return _fallbackMessageSource;
    }

    public void setFallbackMessageSource(MessageSource _messageSource) {
        this._fallbackMessageSource = _messageSource;
    }

    public I18NResourceBundleTemplateProvider() {
    }

    @Override
    public String get(String parentName, String messageId, Locale locale) throws TextTemplateNotFoundException {
        // parent-name is the bundle name
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(parentName, locale,
                    Thread.currentThread().getContextClassLoader(),
                    bundleControl);
            return bundle.getString(messageId);
        } catch (MissingResourceException t) {
            throw new TextTemplateNotFoundException(messageId,locale);
        }
    }

    @Override
    public void refresh() {
        ResourceBundle.clearCache();
    }


    private static final class MessageResourceBundleControl extends ResourceBundle.Control {

        private final ConfigurationService configurationService = App.getService(ConfigurationService.class);

        private MessageResourceBundleControl() {
        }

        // just dont cache anything in devMode.
        @Override
        public long getTimeToLive(String baseName, Locale locale) {
            if (configurationService.isDevMode()) {
                return TTL_DONT_CACHE;
            }
            return configurationService.getLongProperty(BUNDLE_EXPIRATION_TIME_KEY, 1800L) * 1000;
        }
    }

    @Override
    public Integer getProviderType() {
        return PROVIDER_TYPE;
    }
}

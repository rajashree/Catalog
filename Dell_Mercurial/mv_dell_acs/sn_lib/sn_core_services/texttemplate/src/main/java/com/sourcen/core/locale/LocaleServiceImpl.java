/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.locale;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.services.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class LocaleServiceImpl extends ServiceImpl implements LocaleService {

    private static final Logger log = LoggerFactory.getLogger(LocaleServiceImpl.class);
    private static final ThreadLocal<Locale> threadLocale = new ThreadLocal<Locale>();
    private static ConfigurationService configurationService = App.getService(ConfigurationService.class);
    public static final String APPLICATION_LOCALE_KEY = "app.locale";
    public static final Locale DEFAULT_APPLICATION_LOCALE = Locale.ENGLISH;


    private static final LocaleServiceImpl instance = new LocaleServiceImpl();

    public static LocaleServiceImpl getInstance() {
        return instance;
    }

    private LocaleServiceImpl() {
    }

    @Override
    public Locale getLocale() {
        Locale locale = threadLocale.get();
        if (locale == null) {
            String localeStr = configurationService.getProperty(APPLICATION_LOCALE_KEY);
            if (localeStr != null) {
                locale = parseLocale(localeStr);
            } else {
                locale = DEFAULT_APPLICATION_LOCALE;
            }
            threadLocale.set(locale);
        }
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        threadLocale.set(locale);
        configurationService.setProperty(APPLICATION_LOCALE_KEY, locale.toString());
    }

    @Override
    public Locale parseLocale(String localeStr) {
        Locale locale = null;
        try {
            String[] parts = localeStr.split("_");
            if (parts.length == 1) {
                locale = new Locale(parts[0]);
            } else if (parts.length == 2) {
                locale = new Locale(parts[0], parts[1]);
            } else if (parts.length == 3) {
                locale = new Locale(parts[0], parts[1], parts[2]);
            }
        } catch (Exception e) {
            log.warn("Unable to parse locale from localeString:=" + localeStr + ", using default locale as English.");
            locale = Locale.ENGLISH;
        }
        return locale;
    }

    private static final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return acceptHeaderLocaleResolver.resolveLocale(request);
    }

    @Override
    public void initialize() {
        getLocale();// initialize the default locale.
    }

}

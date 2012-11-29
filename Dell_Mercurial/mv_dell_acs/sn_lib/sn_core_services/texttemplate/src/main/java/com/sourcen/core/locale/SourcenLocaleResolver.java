/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.locale;

import com.sourcen.core.App;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class SourcenLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return getLocaleService().resolveLocale(request);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        getLocaleService().setLocale(locale);
    }

    private LocaleService _localeService;

    public LocaleService getLocaleService() {
        if (_localeService == null) {
            this._localeService = App.getService(LocaleService.class);
        }
        return _localeService;
    }

    public void setLocaleService(LocaleService localeService) {
        this._localeService = localeService;
    }
}

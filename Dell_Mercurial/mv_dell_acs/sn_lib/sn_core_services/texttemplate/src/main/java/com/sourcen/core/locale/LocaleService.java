/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.locale;

import com.sourcen.core.services.DefaultImplementation;
import com.sourcen.core.services.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@DefaultImplementation(LocaleServiceImpl.class)
public interface LocaleService extends Service {

    Locale getLocale();

    void setLocale(Locale locale);

    Locale parseLocale(String localeStr);

    Locale resolveLocale(HttpServletRequest request);

}

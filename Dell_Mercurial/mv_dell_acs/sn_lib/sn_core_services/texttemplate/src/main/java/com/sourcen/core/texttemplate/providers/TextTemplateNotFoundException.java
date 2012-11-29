/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate.providers;

import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class TextTemplateNotFoundException extends RuntimeException {


    public TextTemplateNotFoundException(String message, String code, Locale locale) {
        super(message + " messageId:=" + code + ", locale:=" + locale);
    }

    public TextTemplateNotFoundException(String code, Locale locale) {
        super("unable to find messageId:=" + code + ", locale:=" + locale);
    }

    public TextTemplateNotFoundException(String message, String code, Locale locale, Throwable cause) {
        super(message + " messageId:=" + code + ", locale:=" + locale, cause);
    }
}

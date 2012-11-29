/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate;

import com.sourcen.core.SourcenRuntimeException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class TextTemplateException extends SourcenRuntimeException {

    public TextTemplateException() {
    }

    public TextTemplateException(String message) {
        super(message);
    }

    public TextTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextTemplateException(Throwable cause) {
        super(cause);
    }
}

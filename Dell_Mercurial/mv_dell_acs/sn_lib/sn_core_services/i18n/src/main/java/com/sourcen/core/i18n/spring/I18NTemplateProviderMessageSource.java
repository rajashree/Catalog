/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.i18n.spring;

import com.sourcen.core.services.Lifecycle;
import com.sourcen.core.texttemplate.providers.TextTemplateNotFoundException;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSourceResolvable;

import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface I18NTemplateProviderMessageSource extends HierarchicalMessageSource, Lifecycle {


    @Override
    String getMessage(String code, Object[] args, Locale locale) throws TextTemplateNotFoundException;

    @Override
    String getMessage(MessageSourceResolvable resolvable, Locale locale) throws TextTemplateNotFoundException;


}

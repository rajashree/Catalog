/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.i18n;

import com.sourcen.core.services.DefaultImplementation;
import com.sourcen.core.services.Service;
import com.sourcen.core.texttemplate.providers.TextTemplateProvider;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@DefaultImplementation(I18NServiceImpl.class)
public interface I18NService extends Service, TextTemplateProvider {

}

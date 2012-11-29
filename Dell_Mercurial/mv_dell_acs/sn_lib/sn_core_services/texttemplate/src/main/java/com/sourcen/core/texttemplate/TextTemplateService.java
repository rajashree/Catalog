/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate;

import com.sourcen.core.services.DefaultImplementation;
import com.sourcen.core.services.Service;
import com.sourcen.core.services.ServiceProvider;
import com.sourcen.core.texttemplate.providers.TextTemplateProvider;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@DefaultImplementation(TextTemplateServiceImpl.class)
public interface TextTemplateService extends Service, ServiceProvider<TextTemplateProvider> {

    <T extends TextTemplateProvider> T getTemplateProvider(Integer providerType);

}

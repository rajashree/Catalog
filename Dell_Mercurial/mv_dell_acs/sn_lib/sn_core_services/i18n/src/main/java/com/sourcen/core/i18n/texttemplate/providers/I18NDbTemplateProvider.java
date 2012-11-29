/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.i18n.texttemplate.providers;

import com.sourcen.core.texttemplate.providers.PersistenceTextTemplateProviderImpl;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class I18NDbTemplateProvider extends PersistenceTextTemplateProviderImpl implements I18NTextTemplateProvider {


    public I18NDbTemplateProvider() {
    }

    @Override
    public Integer getProviderType() {
        return I18NTextTemplateProvider.PROVIDER_TYPE;
    }

}

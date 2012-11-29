/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate.providers;

import com.sourcen.core.services.Refreshable;

import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: ashish $
 * @version $Revision: 2866 $, $Date:: 2012-06-04 07:56:37#$
 */
public interface TextTemplateProvider extends Refreshable {

    String get(String templateId) throws TextTemplateNotFoundException;

    String get(String templateId, String defaultMessage);

    String get(String templateId, Locale locale) throws TextTemplateNotFoundException;

    String get(String templateId, Locale locale, String defaultMessage) throws TextTemplateNotFoundException;

    String get(String templateId, Locale locale, Object[] args) throws TextTemplateNotFoundException;

    String get(String templateId, Locale locale, String defaultMessage, Object[] args);

    String get(String templateId, Object[] args) throws TextTemplateNotFoundException;

    String get(String templateId, String defaultMessage, Object[] args);

    String get(String parentName, String messageId, Locale locale) throws TextTemplateNotFoundException;

    //
    // parent is used when we want to merge multiple providers in a hierarchy.
    //
    TextTemplateProvider getParent();

    void setParent(TextTemplateProvider provider);

    Integer getProviderType();

}

/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate.providers;

import com.sourcen.core.persistence.domain.TextTemplate;
import com.sourcen.core.persistence.repository.TextTemplateRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2766 $, $Date:: 2012-05-30 02:19:10#$
 */
public abstract class PersistenceTextTemplateProviderImpl extends TextTemplateProviderImpl {

    public PersistenceTextTemplateProviderImpl() {
    }

    @Override
    public String get(String parentName, String messageId, Locale locale) {
        TextTemplate template = textTemplateRepository.get(getProviderType(), parentName, locale.toString(), messageId);
        if (template != null) {
            return template.getMessage();
        }
        return null;
    }

    //
    // injections
    //

    protected TextTemplateRepository textTemplateRepository;

    public TextTemplateRepository getTextTemplateRepository() {
        return textTemplateRepository;
    }

    @Transactional
    public void setTextTemplateRepository(TextTemplateRepository textTemplateRepository) {
        this.textTemplateRepository = textTemplateRepository;
    }
}

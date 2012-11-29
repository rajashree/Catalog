/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.persistence.repository;


import com.sourcen.core.persistence.domain.TextTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TextTemplateRepository extends Repository<TextTemplate> {

    @Transactional(readOnly = true)
    TextTemplate get(Integer type, String locale, String code);

    @Transactional(readOnly = true)
    TextTemplate get(Integer type, String parent, String locale, String code);

    @Transactional(readOnly = true)
    List<TextTemplate> getList(Integer type, String parent, String locale, String code);

}

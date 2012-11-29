/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.persistence.repository.impl.hibernate;

import com.sourcen.core.App;
import com.sourcen.core.locale.LocaleService;
import com.sourcen.core.persistence.domain.TextTemplate;
import com.sourcen.core.persistence.domain.impl.hibernate.TextTemplateModel;
import com.sourcen.core.persistence.repository.TextTemplateRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class TextTemplateRepositoryImpl extends RepositoryImpl<TextTemplate> implements TextTemplateRepository {

    private final LocaleService localeService = App.getService(LocaleService.class);

    public TextTemplateRepositoryImpl() {
        super(TextTemplateModel.class);
    }

    //
    // main queries
    //

    @Override
    public TextTemplate get(final Integer type, final String locale, final String code) {
        return get(type, null, locale, code);
    }

    @Override
    public TextTemplate get(final Integer type, final String parent, final String locale, final String code) {

        Criteria criteria = getSession().createCriteria(TextTemplateModel.class);
        if (type != null) {
            criteria.add(Restrictions.eq("type.type", type));
        }
        if (parent != null) {
            criteria.add(Restrictions.eq("parent", parent));
        }
        if (locale != null) {
            int localeSplitIndex = locale.indexOf("_");
            if (localeSplitIndex > -1) {
                criteria.add(Restrictions.or(
                        Restrictions.eq("locale", locale),
                        Restrictions.eq("locale", locale.substring(0, localeSplitIndex))));
                criteria.addOrder(Order.desc("locale"));
            } else {
                criteria.add(Restrictions.eq("locale", locale));
            }
        }
        if (code != null) {
            criteria.add(Restrictions.eq("name.title", code));
        }
        List<TextTemplate> list = criteria.setMaxResults(1).list();
        if (list.isEmpty()) {
            return null;
        }
        return list.iterator().next();
    }

    @Override
    public List<TextTemplate> getList(final Integer type, final String parent, final String locale, final String code) {

        TextTemplateModel example = new TextTemplateModel();
        if (type != null) {
            example.setType(type);
        }
        if (parent != null) {
            example.setParent(parent);
        }
        if (locale != null) {
            example.setLocale(locale);
        }
        if (code != null) {
            example.setTitle(code);
        }

        return super.getByExample((TextTemplate) example);
    }

}

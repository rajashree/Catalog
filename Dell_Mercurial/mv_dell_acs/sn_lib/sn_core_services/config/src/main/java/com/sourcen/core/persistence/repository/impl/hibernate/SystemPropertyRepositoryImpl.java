/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.persistence.repository.impl.hibernate;

import com.sourcen.core.persistence.domain.SystemProperty;
import com.sourcen.core.persistence.domain.impl.hibernate.SystemPropertyModel;
import com.sourcen.core.persistence.repository.SystemPropertyRepository;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class SystemPropertyRepositoryImpl extends IdentifiableEntityRepositoryImpl<String, SystemProperty> implements SystemPropertyRepository {

    public SystemPropertyRepositoryImpl() {
        super(SystemPropertyModel.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SystemProperty> getProperties(final String name) {
        return onFindForList(
                getSession().
                        createCriteria(SystemProperty.class)
                        .add(Restrictions.like("id", name + "%"))
                        .list());
    }

    @Override
    public void removeProperties(final String name) {
        super.executeQuery("DELETE FROM " + this.entityClassName + " WHERE id LIKE ?", name.concat("%"));
    }

    @Override
    public void removeProperty(final String name) {
        final SystemPropertyModel template = new SystemPropertyModel();
        template.setId(name);
        super.remove(template);
    }

    @Override
    public void setProperty(final String name, final String value) {
        SystemProperty prop = null;
        try {
            prop = super.get(name);
        } catch (Exception e) {
            prop = null;
        }
        SystemPropertyModel template;
        if (prop == null) {
            template = new SystemPropertyModel();
            template.setId(name);
        } else {
            template = (SystemPropertyModel) prop;
        }
        template.setValue(value);
        super.insert(template);
    }

}

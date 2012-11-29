/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.persistence.domain.impl.hibernate;

import com.sourcen.core.persistence.domain.TextTemplate;
import com.sourcen.core.persistence.domain.constructs.jpa.Dated;
import com.sourcen.core.persistence.domain.constructs.jpa.Named;
import com.sourcen.core.persistence.domain.constructs.jpa.ParentAware;
import com.sourcen.core.persistence.domain.constructs.jpa.Typed;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

/**
 * A simple database table to store key-value pairs that is accessible to store all general settings.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 * @since 1.01
 */
@javax.persistence.Entity
//sfisk - CS-380
@Table(name = "t_text_template", uniqueConstraints = {@UniqueConstraint(columnNames = {"parent", "title", "locale", "type"})})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TextTemplateModel extends IdentifiableEntityModel<Long> implements TextTemplate {

    private static final long serialVersionUID = -6635099084187453850L;

    @Embedded
    private Named name = new Named();

    @Embedded
    private Dated date = new Dated();

    @Embedded
    private Typed<Integer> type = new Typed<Integer>();

    @Embedded
    private ParentAware<String> parent = new ParentAware<String>();


    @Column(unique = false, nullable = false, length = 32)
    private String locale = "en";

    @Column(unique = false, nullable = true, length = 4000)
    private String message = "";


    public TextTemplateModel() {
        super();
    }

    @Override
    public String getTitle() {
        return name.getTitle();
    }

    @Override
    public void setTitle(String title) {
        name.setTitle(title);
    }

    @Override
    public String getDescription() {
        return name.getDescription();
    }

    @Override
    public void setDescription(String description) {
        name.setDescription(description);
    }

    @Override
    public Date getDateCreated() {
        return date.getDateCreated();
    }

    @Override
    public void setDateCreated(Date date) {
        this.date.setDateCreated(date);
    }

    @Override
    public Date getDateModified() {
        return date.getDateModified();
    }

    @Override
    public void setDateModified(Date date) {
        this.date.setDateModified(date);
    }

    @Override
    public String getParent() {
        return parent.getParent();
    }

    @Override
    public void setParent(String parent) {
        this.parent.setParent(parent);
    }

    @Override
    public Integer getType() {
        return type.getType();
    }

    @Override
    public void setType(Integer type) {
        this.type.setType(type);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

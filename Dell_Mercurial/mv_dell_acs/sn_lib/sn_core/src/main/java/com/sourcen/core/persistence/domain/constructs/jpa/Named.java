/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs.jpa;

import com.sourcen.core.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@Embeddable
public class Named implements com.sourcen.core.persistence.domain.constructs.Named {

    @Column
    private String title;

    @Column
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NamedModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    //
    // Object
    //

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(title, description);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(com.sourcen.core.persistence.domain.constructs.Named.class) && obj.hashCode() == this.hashCode();
    }
}

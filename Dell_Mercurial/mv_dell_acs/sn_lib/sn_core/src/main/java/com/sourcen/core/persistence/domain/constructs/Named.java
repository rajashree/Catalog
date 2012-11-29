/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs;

/**
 * Provides named identifiers and description to a entity.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface Named {

    /**
     * @return title of this entity.
     */
    String getTitle();

    /**
     * @param title to be set for this entity.
     */
    void setTitle(String title);

    /**
     * @return description of this entity.
     */
    String getDescription();

    /**
     * @param description to be set for this entity.
     */
    void setDescription(String description);

}

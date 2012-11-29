/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs;

import java.util.Date;

/**
 * Some times its necessary to store the history as of when a entity was created and modified.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface Dated {

    /**
     * @return the date when the entity was created.
     */
    Date getDateCreated();

    /**
     * @param date to set when the entity was created.
     */
    void setDateCreated(Date date);

    /**
     * @return the date when the entity was modified.
     */
    Date getDateModified();

    /**
     * @param date to set when the entity was modified.
     */
    void setDateModified(Date date);

}

/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository;


import com.sourcen.core.persistence.domain.HierarchicalPropertiesAwareEntity;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface HierarchicalPropertiesAwareRepository<R extends HierarchicalPropertiesAwareEntity>
        extends HierarchyAwareRepository<R> {

}

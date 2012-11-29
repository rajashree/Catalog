/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain;

/**
 * A entity that provides Hiearchy nested sets along with custom properties extension.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface HierarchicalPropertiesAwareEntity extends HierarchyAwareEntity, PropertiesAwareEntity<Long> {
}

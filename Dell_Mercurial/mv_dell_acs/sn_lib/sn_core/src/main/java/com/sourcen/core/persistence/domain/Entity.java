/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain;

import com.sourcen.core.util.beans.ScopeAware;

import java.io.Serializable;

/**
 * All models by default should be an entity, this will allow us to effectively use the caching layer, without the
 * overhead of determining the cache keys to be generated.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 * @since 1.0
 */
public interface Entity extends Serializable, ScopeAware {

}

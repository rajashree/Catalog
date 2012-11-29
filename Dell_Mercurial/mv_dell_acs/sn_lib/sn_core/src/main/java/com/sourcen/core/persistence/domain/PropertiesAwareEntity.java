/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain;


import com.sourcen.core.persistence.domain.constructs.PropertiesAware;

import java.io.Serializable;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 */
public interface PropertiesAwareEntity<K extends Serializable> extends IdentifiableEntity<K>, PropertiesAware {


}

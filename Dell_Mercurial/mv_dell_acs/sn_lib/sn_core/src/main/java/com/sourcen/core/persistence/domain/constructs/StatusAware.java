/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs;

/**
 * marker interface
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 */
public interface StatusAware<S> {

    public void setStatus(S status);

    public S getStatus();


}

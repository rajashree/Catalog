/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views.transformer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2806 $, $Date:: 2012-06-01 10:40:50#$
 */
public interface OutputTransformer {

    public Object transform(Object bean);

    void setHttpServletRequest(HttpServletRequest request);
}

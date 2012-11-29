/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.spring.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public class SavedRequestAwareAuthenticationSuccessHandler extends org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler {

    private boolean useRequestAttribute = true;

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        if (useRequestAttribute && request.getAttribute("redirectUrl") != null) {
            return request.getAttribute("redirectUrl").toString();
        }
        return super.determineTargetUrl(request, response);
    }

    public void setUseRequestAttribute(boolean useRequestAttribute) {
        this.useRequestAttribute = useRequestAttribute;
    }

    public boolean getUseRequestAttribute() {
        return useRequestAttribute;
    }
}

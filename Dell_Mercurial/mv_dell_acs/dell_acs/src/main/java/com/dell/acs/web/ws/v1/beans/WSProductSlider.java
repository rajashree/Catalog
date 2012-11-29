/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.ScopeAware;
import com.sourcen.core.web.ws.beans.base.WSBeanModel;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSProductSlider extends WSBeanModel implements ScopeAware {

    private String title;

    @Scope(name = "WSProductSlider.targetProduct")
    private WSProduct targetProduct;

    private String targetURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTargetURL() {
        return targetURL;
    }

    public void setTargetURL(final String targetURL) {
        this.targetURL = targetURL;
    }

    public WSProduct getTargetProduct() {
        return targetProduct;
    }

    public void setTargetProduct(final WSProduct targetProduct) {
        this.targetProduct = targetProduct;
    }


    @Override
    public String toString() {
        return "WSProductSlider{"
                + "targetProduct=" + targetProduct
                + ", targetURL='" + targetURL + '\''
                + ", title='" + title + '\''
                + '}';
    }
}

/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.ScopeAware;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.web.ws.beans.base.WSBeanModel;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2789 $, $Date:: 2012-05-30 12:36:21#$
 */
@Scopes({
        @Scope(name = "minimal", fields = {"imageURL"}),
        @Scope(name = "search", fields = {"imageURL"})
})
public class WSProductImage extends WSBeanModel implements ScopeAware {

    //private WSProduct product;
    private String imageType;
    private String imageName;
    private String imageURL;
    private String srcImageURL;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        String cdnPrefix = ConfigurationServiceImpl.getInstance().getProperty("filesystem.cdnPrefix", "");
        if (imageURL.startsWith("http"))
            this.imageURL = imageURL;
        else
            this.imageURL = cdnPrefix + imageURL;

    }

    public String getSrcImageURL() {
        return srcImageURL;
    }

    public void setSrcImageURL(String srcImageURL) {
        this.srcImageURL = srcImageURL;
    }

    @Override
    public String toString() {
        return "WSProductImage{" +
                "imageType='" + imageType + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", srcImageURL='" + srcImageURL + '\'' +
                '}';
    }
}

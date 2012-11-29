/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1;

import com.dell.acs.web.ws.v1.beans.WSAdPublisher;
import com.sourcen.core.web.ws.WebService;
import com.sourcen.core.web.ws.beans.WSProperty;

import java.util.Collection;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface AdPublisherService extends WebService {

    /**
     * retrieve the WebService AdPublisher user object.
     *
     * @param apiKey , Store the api Key .
     *
     * @return WSAdPublisher Object
     */
    WSAdPublisher getPublisherByWebsite(String apiKey);

    /**
     * retrieve the Properties for AdPublisher User on the basis of Publisher Id .
     *
     * @param adPublisherId, Store the Publisher Id.
     *
     * @return Properties Collection  of WebService Property for WebService AdPublisher User.
     */
    Collection<WSProperty> getProperties(Long adPublisherId);

    /**
     * retrieve the Map of AdTags which store the image details in custom Ad Page.
     *
     * @return map
     */
    Map<String, String> getAdTags();

    /**
     * return the api key on the basis of website.
     *
     * @param websiteName
     *
     * @return String
     */
    String getApiKey(String websiteName);

}

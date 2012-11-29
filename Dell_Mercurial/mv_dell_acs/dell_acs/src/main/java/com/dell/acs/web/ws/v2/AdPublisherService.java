package com.dell.acs.web.ws.v2;

import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.web.ws.v1.beans.WSAdPublisher;
import com.sourcen.core.web.ws.beans.WSProperty;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: 7/20/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AdPublisherService {

    /**
     * retrieve the WebService AdPublisher user object.
     *
     * @param facebookAPIKey , Store the api Key .
     *
     * @return WSAdPublisher Object
     */
    AdPublisher getPublisherByWebsite(String facebookAPIKey);

    /**
     * retrieve the Map of AdTags which store the image details in custom Ad Page.
     *
     * @return map
     */
    Map<String, String> getAdTags();

    /**
     * return the api key on the basis of website.
     *
     * @param adPublisherWebsite
     *
     * @return String
     */
    String getFacebookAPIKey(String adPublisherWebsite);
}

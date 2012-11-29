/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 * User: Ashish.
 * Date: 3/1/12
 */
public interface AdPublisherManager extends Manager {

    /**
     * Create the adPublisher user with the api key.
     *
     * @param user    , keep the information for facebook user
     * @param website , keep the website domain for facebook user
     *
     * @return return the created ad_publisher object
     */
    AdPublisher createAdPublisher(User user, String website);

    /**
     * return the collection of adPublisher  user list.
     * whose records are present in users table.
     *
     * @param user , store the information of user
     *
     * @return return the collection of adPublisher  user list.
     */
    Collection<AdPublisher> getAdPublishers(User user);

    /**
     * pass the user information and website domain ,
     * to get(User user,String website) for retrive the adpublisher object  .
     *
     * @param user    , store the user information
     * @param website , store the website domain information
     *
     * @return return the adpublisher object on the basis of facebook user website domain
     */
    AdPublisher getAdPublisher(User user, String website);

    /**
      return AdPublisher User on the basis of Publisher Id.
     @param adPublisherId  , store the publisher Id.
     @return  AdPublisher User  Object
     */
    AdPublisher getAdPublisher(Long adPublisherId);

    /**
     return the AdPublisher User on the basis of API Key of FacebookUser.
     @param apiKey, Store the api key value.
     @return return the adPublisher Object.
     */
    AdPublisher getAdPublisher(String apiKey);

    /**
     Update the AdPublisher User with updated wordpress json text.
     @param record, store the AdPublisher Object which store wordpress json text
                    for the user
     */
    void updateAdPublisher(AdPublisher record);

}

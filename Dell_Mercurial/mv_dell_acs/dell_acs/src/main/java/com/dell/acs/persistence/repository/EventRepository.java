/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Event;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;
import java.util.Map;

/**
 * @author Ashish
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date: 2012-04-27
 */

public interface EventRepository extends IdentifiableEntityRepository<Long, Event> {

    List<Event> getAllEvent(Long retailerSiteId);

    /**
     retrieve the event on the basis of event id and retailer site id.
     @param eventId, store the event id.
     @param retailerSiteID , store the retailer site id.
     @return event belong to particular retail;er site.
     */
    Event getEvent(Long eventId,Long retailerSiteID);

    /**
     * List all event , which belong to same retailer.
     *
     * @param siteId , store the retailer site id.
     *
     * @return list of all events.
     */
    List<Event> getEventsBySiteId(Long siteId);

    /**
     Retrieved the filter event on the basis map parameter.
     @param params, store the parameter as map object.
     @return list of events.
     */
    List<Event> filterEvents(Map<String, Object> params);

    /**
     Check  the name availability for preventing the duplicate entry.
     @param name , store the event name.
     @return   boolean status.
     */
     boolean checkEventNameAvailability(String name);

    /**
     Retrieve the optional image map, which contain the key as optionalimage_key and value as optionalimage_value.
     @param id
     @return
     */
    Map<String,String> getNumbersOfOptionalImage(Long eventId);

    /**
     Retrieve the list of optional key created for particular event.
     @param eventPropertyId ,that is id equivalent to event id.
     @return
     */
    String [] getAllOptionalImageKey(Long eventPropertyId);

    /**
     retrieve the event name on the basis of event id.
     @param eventID
     @return
     */
    String getEventNameById(Long eventID);

    /**
     set the status value for deleted events.
     @param eventId
     @return
     */
    Event deleteEventById(Long eventId);

}

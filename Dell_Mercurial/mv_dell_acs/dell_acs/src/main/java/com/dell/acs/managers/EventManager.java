/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.EventAlreadyExistException;
import com.dell.acs.EventNotFoundException;
import com.dell.acs.persistence.domain.Event;
import com.sourcen.core.managers.Manager;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Map;

/**
 @author Samee K.S
 @author sameeks: svnName $
 @version : 0 $, :: 2012-03-07 09:56:40#$ */

public interface EventManager extends Manager {

    /**
     Event Related Properties.
     */
    public static final String EVENT_NUMBER_OF_DAYS_PROPERTIES = "dell.event.numberOfDays";

    public static final String EVENT_INFO_LINK_PROPERTIES = "dell.event.infoLink";
    public static final String EVENT_BUY_LINK_PROPERTIES = "dell.event.buyLink";

    public static final String EVENT_PRICE_PROPERTIES = "dell.event.price";
    public static final String EVENT_LIST_PRICE_PROPERTIES = "dell.event.listPrice";

    public static final String EVENT_PER_N_PEOPLE_PROPERTIES = "dell.event.perNpeople";
    public static final String EVENT_MINIMUM_PEOPLE_PROPERTIES = "dell.event.minimumPeople";

    public static final String EVENT_CLASS_OF_SERVICE_PROPERTIES = "dell.event.classOfService";
    public static final String EVENT_REVIE_LINK_PROPERTIES = "dell.event.reviewLink";

    public static final String EVENT_FACEBOOK_LIKES_PROPERTIES = "dell.event.facebookLikes";
    public static final String EVENT_TWITTER_PROPERTIES = "dell.event.twitter";

    public static final String EVENT_GOOGLE_PLUSES_PROPERTIES = "dell.event.googlePluses";
    public static final String EVENT_RATINGS_PROPERTIES = "dell.event.ratings";

    public static final String EVENT_STARS_PROPERTIES = "dell.event.stars";
    public static final String EVENT_THUMBNAIL_PROPERTIES = "dell.event.thumbnail";


    /**
     Save the event detail.

     @param event , store the event data.
     @return inserted event.
     @throws EventAlreadyExistException, handle the exception for duplicate entry.
     */
    /* throws EntityExistsException is removed from the functionality , may be added later*/
    Event saveEvent(Event event);

    /**
     delete the particular event.

     @param eventID , store the id of event, which has to be deleted.
     */
    void deleteEvent(Long eventID) throws EventNotFoundException;

    /**
     Retrieve the event on the basis of event_id and retailer_site Id .

     @param eventID, store the event id.
     @param retailerSiteID , store the retailer site id.
     @return event instance.
     */
    Event getEvent(Long eventID,Long retailerSiteID);

    /**
     List of all events using belong to same retailers.

     @param siteId , store the retailer site id belong to particular events.
     @return list os all events.
     */
    List<Event> getEventsByRetailerSiteId(Long siteId);

    /**
      Create the cdn directory structure for particular events.
     @param event , store the events.
     @return event.
     */
    String getBaseCDNPathForEvent(Event event);

    /**
     Retrieved the filter event on the basis map parameter.
     @param paramsMap , store the parameter as map object.
     @return list of Map.
     */
    List<Map<String, String>> getFilteredEvents(Map<String, Object> paramsMap);

    /**
     Check  the name availability for preventing the duplicate entry.

     @param name , store the event name.
     @return , boolean status.
     */
    boolean checkNameAvailability(String name);

    /**
     Retrieve the optional image map, which contain the key as optional image_key and value as optional image_value.

     @param eventId, store the event id.
     @return  map contain the optional image key(like dell.event.optional.image<number>) and it's value.
     */
    Map<String, String> getNumberOfOptionalImage(Long eventId);

    /**
      Retrieve the list of optional key created for particular event.
     @param eventPropertyID , that is id equivalent to event id.
     @return return the optional key list.
     */
    String[] getListAllOptionalKeyFromEventProperty(Long eventPropertyID);

    /**
     retrieve the event name on the basis of event id.
     @param ID,store the event id.
     @return
     */
    String getEventNameByID(Long eventID);


    /**
     List of all events.

     @return list os all events.
     */
    @Deprecated
    List<Event> getEvents(Long retailerSiteId);


    /**
     Update the edited event.

     @param event, store the updated event instance.
     @return , return the updated event instance.
     */
    @Deprecated
    Event updateEvent(Event event);

    /**
     * Return an Event
     * @param eventID - Long eventID
     * @return an Event if found, else and exception is thrown
     */
    Event getEvent(Long eventID);


}

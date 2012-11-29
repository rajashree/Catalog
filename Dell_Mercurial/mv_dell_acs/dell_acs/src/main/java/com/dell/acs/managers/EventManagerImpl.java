/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;


import com.dell.acs.EventAlreadyExistException;
import com.dell.acs.EventNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Event;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.EventRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 @author Samee K.S
 @author : sameeks $
 @version : 0 $, :: 2012-03-07 09:56:40#$ */
@Service
public class EventManagerImpl implements EventManager {

    private static final Logger LOG = Logger.getLogger(EventManagerImpl.class);

    /**
     @inheritdoc
     */
    @Override
    @Transactional
    /* throws EntityExistsException  and "rollbackFor = EntityExistsException.class, attribute of Transactional"  are removed from functionality */
    public Event saveEvent(final Event event) {


        if (event.getId() != null) {
            event.setModifiedDate(new Date());
            eventRepository.update(event);
            LOG.debug("Update event - " + event.getId() + " , " + event.getName());
        } else {
            event.setCreationDate(new Date());
            event.setModifiedDate(new Date());
            event.setStatus(EntityConstants.Status.PUBLISHED.getId());
            eventRepository.insert(event);
            LOG.debug("Insert event - " + event.getId() + " , " + event.getName());
        }

        return event;
    }

    /**
     @inheritdoc
     */
    @Override
    @Transactional(rollbackFor = EventNotFoundException.class)
    public void deleteEvent(final Long eventID) throws EventNotFoundException {
        eventRepository.deleteEventById(eventID);
    }

    /**
     @inheritdoc
     */
    @Override
    @Transactional
    @Deprecated
    public List<Event> getEvents(Long retailerSiteId) {
        List<Event> eventList = eventRepository.getAllEvent(retailerSiteId);
        return eventList;
    }

    /**
     @inheritdoc
     */
    @Override
    @Transactional(readOnly = false)
    public Event updateEvent(final Event event) {
        event.setModifiedDate(new Date());
        eventRepository.update(event);
        return event;
    }

    /**
     @inheritdoc
     */
    @Override
    @Transactional
    public Event getEvent(final Long eventID, Long retailerSiteID) {
        return eventRepository.getEvent(eventID, retailerSiteID);
    }

    /**
     @inheritdoc
     */
    @Override
    @Transactional
    public List<Event> getEventsByRetailerSiteId(final Long siteId) {
        List<Event> eventList = eventRepository.getEventsBySiteId(siteId);
        return eventList;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public String getBaseCDNPathForEvent(final Event event) {

        RetailerSite retailerSite = retailerSiteRepository.get(event.getRetailerSite().getId());
        Retailer retailer = retailerSite.getRetailer();

        String campaignCDNBasePath = StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "cdn"
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + retailer.getId()
                + "_" + retailer.getName()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + retailerSite.getId() + "_" + retailerSite.getSiteName()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "event"
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + event.getId()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR;
        return campaignCDNBasePath;

    }


    @Override
    public List<Map<String, String>> getFilteredEvents(Map<String, Object> paramsMap) {
        List<Map<String, String>> filteredEvents = new ArrayList<Map<String, String>>();
        List<Event> events = eventRepository.filterEvents(paramsMap);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        // Prepare the list of event maps
        PropertiesProvider pros = null;
        String stars = "", reviews = "", ratings = "", price = "", listPrice = "";
        for (Event event : events) {
            pros = event.getProperties();
            Map<String, String> eventData = new HashMap<String, String>();
            eventData.put("id", String.valueOf(event.getId()));
            eventData.put("title", event.getName());
            eventData.put("location", event.getLocation());

            // it's possible that dates being passed in are null
            Date date = event.getStartDate();
            if (date != null) {
                eventData.put("startDate", formatter.format(date));
            }
            date = event.getEndDate();
            if (date != null) {
                eventData.put("endDate", formatter.format(date));
            }

            eventData.put("site", event.getRetailerSite().getSiteName());
            if (pros.hasProperty(EventManager.EVENT_STARS_PROPERTIES)) {
                stars = pros.getProperty(EventManager.EVENT_STARS_PROPERTIES);
            }
            eventData.put("stars", stars);

            if (pros.hasProperty(EventManager.EVENT_RATINGS_PROPERTIES)) {
                ratings = pros.getProperty(EventManager.EVENT_RATINGS_PROPERTIES);
            }
            eventData.put("ratings", ratings);

            if (pros.hasProperty(EventManager.EVENT_LIST_PRICE_PROPERTIES)) {
                listPrice = pros.getProperty(EventManager.EVENT_LIST_PRICE_PROPERTIES);
            }
            eventData.put("listPrice", listPrice);

            if (pros.hasProperty(EventManager.EVENT_PRICE_PROPERTIES)) {
                price = pros.getProperty(EventManager.EVENT_PRICE_PROPERTIES);
            }
            eventData.put("price", price);

            // Dummy field
            eventData.put("reviews", "");

            filteredEvents.add(eventData);
        }
        return filteredEvents;
    }

    @Override
    @Transactional
    public boolean checkNameAvailability(final String name) {
        boolean nameExistenceStatus = eventRepository.checkEventNameAvailability(name);
        return nameExistenceStatus;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getNumberOfOptionalImage(final Long id) {
        return eventRepository.getNumbersOfOptionalImage(id);
    }

    @Override
    @Transactional
    public String[] getListAllOptionalKeyFromEventProperty(Long eventPropertyId) {
        return eventRepository.getAllOptionalImageKey(eventPropertyId);
    }

    @Override
    @Transactional
    public String getEventNameByID(final Long eventID) {
        return eventRepository.getEventNameById(eventID);
    }

    /**
     RetailerSiteRepository bean.
     */
    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    /**
     EventRepository bean.
     */
    @Autowired
    private EventRepository eventRepository;

    /**
     EventRepository bean injection.

     @param eventRepository, store the eventRepository instance.
     */
    public void setEventRepository(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    /**
     RetailerSiteRepository bean injection.

     @param retailerSiteRepository, store the eventRepository instance.
     */
    public void setEventRepository(final RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }

    @Override
    public Event getEvent(Long eventID) {
        return this.eventRepository.get(eventID);
    }
}

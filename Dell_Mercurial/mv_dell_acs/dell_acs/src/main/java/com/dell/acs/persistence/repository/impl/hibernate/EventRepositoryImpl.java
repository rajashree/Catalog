/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Event;
import com.dell.acs.persistence.domain.EventProperty;
import com.dell.acs.persistence.repository.EventRepository;
import com.sourcen.core.ObjectNotFoundException;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.Assert;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 @author Samee K.S
 @author sameeks: svnName $
 @version : 0 $, :: 2012-03-07 09:56:40#$ */
@Repository
public class EventRepositoryImpl extends PropertiesAwareRepositoryImpl<Event>
        implements EventRepository {

    private Logger logger = LoggerFactory.getLogger(EventRepositoryImpl.class);

    /**
     Default constructor.
     */
    public EventRepositoryImpl() {
        super(Event.class, EventProperty.class);
    }

    /**
     @return , list of all events.
     @inheritdoc
     */
    @Override
    public List<Event> getAllEvent(Long retailerSiteId) {

        Criteria criteria = getSession().createCriteria(Event.class);
        criteria.add(Restrictions.eq("retailerSite.id", retailerSiteId))
                .add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
        List<Event> eventList = criteria.list();
        if (eventList == null) {
            logger.error("Event details not found for particular retailer site");
        }
        return eventList;
    }

    /**
     retrieve the event on the basis of event id and retailer site id.

     @param eventId        ,       store the event id.
     @param retailerSiteID , store the retailer site id.
     @return list of event belong to particular retail;er site.
     */
    @Override
    @Transactional
    public Event getEvent(final Long eventId, final Long retailerSiteID) {
        Criteria criteria = getSession().createCriteria(Event.class);
        criteria.add(Restrictions.eq("id", eventId)).add(Restrictions.eq("retailerSite.id", retailerSiteID))
                .add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));

        Event event = null;
        Object object = criteria.uniqueResult();
        if (object != null) {
            event = (Event) object;
            event = onFindForObject(event);
        } else {
            logger.error("Unable to get the event details for particular site");
        }
        return event;

    }

    /**
     @return , list of all events based on retailersite id.
     @inheritdoc
     */
    @Override
    public List<Event> getEventsBySiteId(final Long siteId) {
        List<Event> eventList = null;
        try {
            eventList = (List<Event>) getSession().createCriteria(Event.class).add(Restrictions
                    .eq("retailerSite.id", siteId))
                    .add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId())).list();
            throw new ObjectNotFoundException("Event object not found for retailerSite_id==>" + siteId);
        } catch (ObjectNotFoundException e) {
            logger.debug("Event Object Not Found for RetailerSite id: " + siteId);
        }
        return eventList;
    }


    @Override
    public List<Event> filterEvents(Map<String, Object> params) {
        Criteria eventCriteria = getSession().createCriteria(Event.class);
        if (params.containsKey("keyword")) {
            eventCriteria.add(
                    Restrictions.or(
                            Restrictions.like("name", params.get("keyword").toString(), MatchMode.ANYWHERE),
                            Restrictions.like("description", params.get("keyword").toString(), MatchMode.ANYWHERE)
                    )
            );
        }
        if (params.containsKey("startDate")) {
            Date startDate = (Date) params.get("startDate");
            eventCriteria.add(Restrictions.ge("startDate", new Date(startDate.getTime())));
        }
        if (params.containsKey("endDate")) {
            Date endDate = (Date) params.get("endDate");
            eventCriteria.add(Restrictions.le("endDate", new Date(endDate.getTime())));
        }
        if (params.containsKey("location")) {
            eventCriteria.add(Restrictions.like("location", params.get("location").toString()));
        }

        Long siteID = Long.valueOf(params.get("siteID").toString());
        eventCriteria.add(Restrictions.eq("retailerSite.id", siteID));

        return onFindForList(eventCriteria.list());
    }

    @Override
    public boolean checkEventNameAvailability(final String name) {
        Criteria criteria = getSession().createCriteria(Event.class);
        criteria.add(Restrictions.eq("name", name));

        Object obj = criteria.uniqueResult();
        if (obj != null) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Map<String, String> getNumbersOfOptionalImage(Long id) {
        Criteria criteria = getSession().createCriteria(EventProperty.class);
        criteria.add(Restrictions.eq("pk.id", id)).add(Restrictions.like("pk.name", "dell.event.optional.image%"));
        List keyNameList = criteria.list();
        Map<String, String> eventPropertyValuePairMap = new HashMap<String, String>();
        Iterator iterator = keyNameList.iterator();
        while (iterator.hasNext()) {
            EventProperty eventProperty = (EventProperty) iterator.next();
            eventPropertyValuePairMap.put(eventProperty.getName(), eventProperty.getValue());
        }

        return eventPropertyValuePairMap;
    }

    @Override
    public String[] getAllOptionalImageKey(Long eventPropertyId) {
        Criteria criteria = getSession().createCriteria(EventProperty.class);
        criteria.setProjection(Projections.projectionList().add(Projections.property("pk.name"))).add(
                Restrictions.like("pk.name", "dell.event.optional.image%"))
                .add(Restrictions.eq("pk.id", eventPropertyId));
        List<String> allOptionalImageKeyList = criteria.list();
        String[] allOptionalImageKeyArray = new String[allOptionalImageKeyList.size()];
        int i = 0;
        if (allOptionalImageKeyList.size() > 0) {
            for (String key : allOptionalImageKeyList) {
                allOptionalImageKeyArray[i++] = key;
            }
        }
        return allOptionalImageKeyArray;
    }

    @Override
    public String getEventNameById(final Long eventID) {
        String eventName = null;
        Criteria criteria = getSession().createCriteria(Event.class);
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("name"));
        criteria.setProjection(proList);
        criteria.add(Restrictions.eq("id", eventID)).add(
                Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
        Object object = criteria.uniqueResult();
        if (object != null) {
            eventName = (String) object;
            logger.info("Event name :==>" + eventName);
        }
        return eventName;
    }

    @Override
    @Transactional
    public Event deleteEventById(final Long eventId) {
        Event event = get(eventId);
        if (event != null) {
            event.setStatus(EntityConstants.Status.DELETED.getId());
            update(event);
        }
        return event;
    }
}

/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Tag;
import com.dell.acs.persistence.domain.TagMapping;
import com.dell.acs.persistence.repository.TagRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.codehaus.plexus.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */
@Repository
public class TagRepositoryImpl  extends IdentifiableEntityRepositoryImpl<Long, Tag> implements TagRepository{

    public TagRepositoryImpl() {
        super(Tag.class);
    }

    @Override
    public Tag getTag(String name) throws EntityNotFoundException {
        Criteria criteria = getSession().createCriteria(Tag.class);
        criteria.add(Restrictions.eq("name", name));
        Tag tag = (Tag)criteria.uniqueResult();
        if(tag == null){
            throw new EntityNotFoundException("Tag not found - " + name);
        }
        return tag;
    }

    @Override
    public void saveTags(Set<Tag> tags, int entityType, Long entityID) {
        // entityType, entityID, tagID, creationDate
        int removed = flushEntityTagAssociation(entityType, entityID);
        logger.debug("Remove the old TAG association from TagMapping table - " + removed);
        // Create new mapping for the entity
        for(Tag tag: tags){
            TagMapping.TagMappingPK pk = new TagMapping.TagMappingPK(tag.getId(),entityType, entityID );
            TagMapping model = new TagMapping(pk);
            getSession().save(model);
        }
    }

    private int flushEntityTagAssociation(int entityType, Long entityID){
        // Remove all already associated tags and then insert fresh mapping for the entity
        Query query = getSession().createQuery("DELETE FROM TagMapping tm " +
                "WHERE tm.entityType=:entityType AND tm.entityID=:entityID");
        query.setParameter("entityType", entityType);
        query.setParameter("entityID", entityID);
        return query.executeUpdate();
    }

    @Override
    public Collection<Tag> getTags(String searchTerm, boolean retailerSiteSpecific, ServiceFilterBean filter){
        Criteria criteria = getSession().createCriteria(Tag.class);

        if(StringUtils.isNotEmpty(searchTerm)){
            // Need a better way to add the wildcard for searchTerm
            criteria.add(Restrictions.like("name", searchTerm, MatchMode.ANYWHERE));
        }

        // Apply the generic criteria
        applyGenericCriteria(criteria, filter);

        if(retailerSiteSpecific){
            // Load the retailer siteID from the context
           // criteria.add(Restrictions.eq("retailerSite.id", context.retailerSiteID));
        }
        return criteria.list();
    }

    @Override
    public Tag getTag(Object tagObj) throws EntityNotFoundException{
        // Fetch the tag by ID or String
        Tag tag = null;
        if(StringUtils.isNumeric(tagObj.toString())){
            tag = get(Long.valueOf(tagObj.toString()));
        }else{
            tag = getTag(tagObj.toString());
        }
        if(tag == null){
            throw new EntityNotFoundException("Tag -" + tagObj.toString() + "not found.");
        }
        return tag;
    }



        @Override
    public Collection<Tag> getTags(Long retailerSiteID, ServiceFilterBean filter){
        Criteria criteria = getSession().createCriteria(Tag.class);
        criteria.add(Restrictions.eq("retailerSite.id", retailerSiteID));

        // Apply the generic criteria
        applyGenericCriteria(criteria, filter);
        return criteria.list();
    }



    @Override
    public Collection<Tag> getRetailerSiteTags(String searchTerm, Long retailerSiteID, ServiceFilterBean filter) {
        Criteria criteria = getSession().createCriteria(Tag.class);
        // Apply like ONLY if there is some valid search text
        if(StringUtils.isNotEmpty(searchTerm)){
            criteria.add(Restrictions.like("name", searchTerm));
        }
        criteria.add(Restrictions.eq("retailerSite.id", retailerSiteID));

        // Apply the generic criteria
        applyGenericCriteria(criteria, filter);
        return criteria.list();
    }

    @Override
    public Collection<Tag> getTags(int entityType, Long entityID, ServiceFilterBean filter) {
        Query query = getSession().createQuery("SELECT tagID FROM TagMapping t WHERE t.entityType=:entityType " +
                "AND t.entityID=:entityID ");
        query.setParameter("entityType", entityType);
        query.setParameter("entityID", entityID);
        List<Long> tagIDs = query.list();
        Criteria criteria = getSession().createCriteria(Tag.class);
        // Apply the generic criteria - If null then don't apply the criteria
        if(filter != null){
            applyGenericCriteria(criteria, filter);
        }
        if(tagIDs.size() > 0){
            criteria.add(Restrictions.in("id", tagIDs));
            return criteria.list();
        }
        return Collections.emptyList();
    }
}

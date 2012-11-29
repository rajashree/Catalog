/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.Tag;
import com.dell.acs.persistence.repository.TagRepository;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

@Service
public class TagManagerImpl implements TagManager {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private RetailerManager retailerManager;


    @Override
    @Transactional
    public Tag createTag(String name) {
        Tag tag = new Tag(name);
        return saveTag(tag);
    }

    private Tag saveTag(Tag entity){
        try {
            return getTag(entity.getName());
        } catch (EntityNotFoundException e) {
            entity.setCreationDate(new Date());
            tagRepository.insert(entity);
            return entity;
        }
    }

    @Override
    @Transactional
    public Tag createTag(String name, Object site) {
        Tag tag = new Tag(name);
        tag.setRetailerSite(retailerManager.getRetailerSite(site));
        return saveTag(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getTag(String name) throws EntityNotFoundException {
        return tagRepository.getTag(name);
    }

    @Override
    public Collection<Object> getContent(String tag) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Tag> getRetailerSiteTags(String searchTerm, Object site, ServiceFilterBean filter) {
        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        Collection<Tag> entities = tagRepository.getRetailerSiteTags(searchTerm, retailerSite.getId(), filter);
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getTag(Long id) {
        return tagRepository.get(id);
    }

//    @Override
//    @Transactional
//    public void saveTags(Collection<Object> tags, int entityType, Long entityID) {
//        Set<Tag> existingTags = new LinkedHashSet<Tag>();
//        Tag tag = null;
//        if(tags.size() > 0){
//            // If comma separated tags are sent the we
//            if (tags.get(0) instanceof String){
//                String trimmedTagStr = StringUtils.replace(tags.toString(), ",", " ");
//                Set<String> tagList = new HashSet<String>(tags);
//                for(String tagName: tagList){
//                    try {
//                        tag = tagRepository.getTag(tagName);
//                    } catch (EntityNotFoundException e) {
//                        tag = BeanUtils.instantiate(Tag.class);
//                        tag.setName(tagName);
//                        tag.setCount(0);
//                        tag.setCreationDate(new Date());
//                        // TODO: Set retailerSite from context
//                        tagRepository.insert(tag);
//                    }
//                    existingTags.add(tag);
//                }
//            }else if (tags.get(0) instanceof Long){
//                for(Long tagName: tags){
//
//
//                }
//            }
//        }
//    }

    @Override
    @Transactional
    public void saveTags(String tags, int entityType, Long entityID) {
        String trimmedTagStr = StringUtils.replace(tags, ",", " ");
        Set<String> tagList = new HashSet<String>(StringUtils.asCollection(StringUtils.split(trimmedTagStr, " "), true));
        Set<Tag> existingTags = new LinkedHashSet<Tag>();
        Tag tag = null;
        for(String tagName: tagList){
            try {
                tag = tagRepository.getTag(tagName);
            } catch (EntityNotFoundException e) {
                tag = BeanUtils.instantiate(Tag.class);
                tag.setName(tagName);
                tag.setCreationDate(new Date());
                // TODO: Set retailerSite from context
                tagRepository.insert(tag);
            }
            existingTags.add(tag);
        }
        tagRepository.saveTags(existingTags, entityType, entityID );
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<Tag> getTags(ServiceFilterBean filter) {
        // Get all tags irrespective of retailerSite
        Collection<Tag> entities = tagRepository.getTags(null, false, filter);
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Tag> getTags(Object site, ServiceFilterBean filter) {
        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        Collection<Tag> tags = tagRepository.getRetailerSiteTags(null, retailerSite.getId(), null);
        return tags;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Tag> findTags(String searchTerm, ServiceFilterBean filter) {
        Collection<Tag> entities = tagRepository.getTags(searchTerm, false, filter);
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Tag> getTags(int entityType, Long entityID, ServiceFilterBean filter) {
        Collection<Tag> entities = tagRepository.getTags(entityType, entityID, filter);
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public String getTagsAsString(int entityType, Long entityID) {
        Collection<Tag> entityTags = tagRepository.getTags(entityType, entityID, null);
        StringBuffer sbf = new StringBuffer("");
        for(Tag tag : entityTags){
            sbf.append(tag.getName()).append(" ");
        }
        return sbf.toString();
    }
}

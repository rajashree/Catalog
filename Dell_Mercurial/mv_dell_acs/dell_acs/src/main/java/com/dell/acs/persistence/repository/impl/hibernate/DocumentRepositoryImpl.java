/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.DocumentProperty;
import com.dell.acs.persistence.repository.DocumentRepository;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Samee K.S
 * @author sameeks: svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */
@Repository
public class DocumentRepositoryImpl extends PropertiesAwareRepositoryImpl<Document>
        implements DocumentRepository {

    public DocumentRepositoryImpl() {
        super(Document.class, DocumentProperty.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> getDocumentByRetailerSiteID(final Long retailerSiteID) {
        try {

            List<Document> documentList = onFindForList(
                    getSession().createCriteria(Document.class).add(Restrictions.eq("retailerSite.id", retailerSiteID))
                            .list());
            return documentList;
        } catch (Exception e) {
            logger.warn(" DocumentRepositoryImpl " + e.getMessage());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document updateProperty(Long documentId, String name, String value) {
        Document item = get(documentId);
        if (item == null) {
            throw new RuntimeException("Document item not found.");
        } else {
            item.getProperties().setProperty(name, value);
            update(item);
        }
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String getDocumentNameByID(final Long documentId, Integer type) {
        String documentName = null;
        Criteria criteria = getSession().createCriteria(Document.class);
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("name"));
        criteria.setProjection(proList);
        criteria.add(Restrictions.eq("id", documentId)).add(Restrictions.eq("type", type)).add(Restrictions.eq("status",EntityConstants.Status.PUBLISHED.getId()));

        Object object = criteria.uniqueResult();
        if (object != null) {
            documentName = (String) object;
            logger.info("Document name :==>" + documentName);
        }
        return documentName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean checkNameAvailability(final String name) {
        Criteria criteria = getSession().createCriteria(Document.class);
        criteria.add(Restrictions.eq("name", name));
        Object object = criteria.uniqueResult();
        if (object != null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> filterDocuments(Map<String, Object> params) {
        Criteria documentCriteria = getSession().createCriteria(Document.class);

        // To switch between the different types of documents ( image, link, video )
        // TODO: Need to include article as well in the items list ?
        if(params.containsKey("itemType")){
            int type = EntityConstants.Entities.getByValue(params.get("itemType").toString()).getId();
            documentCriteria.add(Restrictions.eq("type", type));
        }

        if (params.containsKey("keyword")) {
            documentCriteria.add(
                    Restrictions.or(
                            Restrictions.like("name", params.get("keyword").toString(), MatchMode.ANYWHERE),
                            Restrictions.like("description", params.get("keyword").toString(), MatchMode.ANYWHERE)
                    )
            );
        }
        if (params.containsKey("startDate")) {
            Date startDate = (Date) params.get("startDate");
            documentCriteria.add(Restrictions.ge("startDate", new Date(startDate.getTime())));
        }

        if (params.containsKey("endDate")) {
            Date endDate = (Date) params.get("endDate");
            documentCriteria.add(Restrictions.le("endDate", new Date(endDate.getTime())));
        }

        documentCriteria.add(Restrictions.eq("retailerSite.id", Long.valueOf(params.get("siteID").toString())));
        return onFindForList(documentCriteria.list());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> getLatestDocuments(Long siteID) {
        Criteria latest = getSession().createCriteria(Document.class);
        int max = ConfigurationServiceImpl.getInstance().getIntegerProperty("app.library.latest.maxItems");
        max = (max > 0) ? max : 10;

        latest.add(Restrictions.eq("retailerSite.id", siteID));
        latest.setMaxResults(max);
        latest.addOrder(Order.desc("modifiedDate"));
        return latest.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> getDocuments(final Long retailerSiteID, final Integer type, final ServiceFilterBean filterBean) {
        Criteria criteria = getSession().createCriteria(Document.class);
        //Apply generic criteria
        applyGenericCriteria(criteria, filterBean);
        criteria.add(Restrictions.eq("retailerSite.id", retailerSiteID))
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
        return onFindForList(criteria.list());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Document getDocument(Long documentID, Integer type) {
        Criteria criteria = getSession().createCriteria(Document.class);
        criteria.add(Restrictions.eq("id", documentID));
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
        return onFindForObject((Document) criteria.uniqueResult());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document deleteDocument(Long documentID) {
        Document document = get(documentID);
        if (document != null) {
            document.setStatus(EntityConstants.Status.DELETED.getId());
            update(document);
        }
        return document;
    }
}

package com.dell.acs.persistence.repository.impl.hibernate;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-18 4:21 PM#$
 */

import com.dell.acs.CurationContentException;
import com.dell.acs.CurationContentNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationContent;
import com.dell.acs.persistence.domain.CurationContentProperty;
import com.dell.acs.persistence.repository.CurationContentRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * CurationContentRepositoryImpl Class provide the implementation for
 * CurationContentRepository interface which have
 * Curation Content Management Functionality Declaration
 */

@Repository
public class CurationContentRepositoryImpl
        extends PropertiesAwareRepositoryImpl<CurationContent> implements CurationContentRepository {

    private static final Logger logger = LoggerFactory.getLogger(CurationContentRepositoryImpl.class);

    public CurationContentRepositoryImpl() {
        super(CurationContent.class, CurationContentProperty.class);
        logger.debug("CurationContentRepositoryImpl instantiate ");
    }

    @Override
    public CurationContent createContent(CurationContent curationContent) throws EntityExistsException {

        // One time default ordering. This is the case when we already have the data in CurationContent table.
        // Since the default value is '0' for all records, this will fail 'updateContentPosition'
        // Hence we need define a default order for all items for a category. This method 'getPosition0Elements()'
        // will be executed ONLY if there are more than one items with '0' as the position value
        Collection<CurationContent> items = getZeroPositionedItems(curationContent);
        if(items.size() > 0){
            checkAndApplyDefaultOrdering(items);
        }


        // Get all id whose position is >= curationContent.position to update the position with +1 after insert
        Collection<Long> ids = getContentIDList(curationContent, null);
        insert(curationContent);
        // Updating the positions ONLY if the item is added in between some items
        if (ids.size() > 0) {
            adjustPositionColumn(ids, true);
        }
        return curationContent;
    }

    @Override
    public void updateContentPosition(Long contentID, Integer position) {
        CurationContent curationContent = get(contentID);
        // One time default ordering. This is the case when we already have the data in CurationContent table.
        // Since the default value is '0' for all records, this will fail 'updateContentPosition'
        // Hence we need define a default order for all items for a category. This method 'getPosition0Elements()'
        // will be executed ONLY if there are more than one items with '0' as the position value
        Collection<CurationContent> items = getZeroPositionedItems(curationContent);
        if(items.size() > 0){
            checkAndApplyDefaultOrdering(items);
        }

        //  ( currentContentPosition - newPosition ) < 0 then decrement
        //  ( currentContentPosition - newPosition ) > 0 then increment
        if (curationContent.getPosition() == position) {
            logger.debug("Item is already in that position. No need to update the position");
        } else {
            int maxAllowed = getMaxItemPosition(curationContent);
            if (position > (maxAllowed + 1)) {
                throw new IllegalArgumentException("Object cannot be placed at this position. Current MAX position is " + maxAllowed + "");
            } else {
                boolean incrementFlag = ((curationContent.getPosition() - position) < 0) ? false : true;
                // Fetch all record-ids which needs an update on the position values relative to the contentID
                Collection<Long> ids = getContentIDList(curationContent, position);
                // Update the content with new position
                curationContent.setPosition(position);
                update(curationContent);
                if (ids.size() > 0) {
                    adjustPositionColumn(ids, incrementFlag);
                }
            }
        }
    }

    private void checkAndApplyDefaultOrdering(Collection<CurationContent> items){
        int pos = 0;
        for(CurationContent content : items){
            content.setPosition(pos++);
            update(content);
        }
    }

    private Collection<CurationContent> getZeroPositionedItems(CurationContent curationContent){
        Criteria listCriteria = getSession().createCriteria(CurationContent.class);
        listCriteria.add(Restrictions.eq("curation.id", curationContent.getCuration().getId()));
        listCriteria.add(Restrictions.eq("categoryID", curationContent.getCategoryID()));
        listCriteria.add(Restrictions.eq("position", 0));
        return listCriteria.list();
    }

    private int getMaxItemPosition(CurationContent curationContent) {
        Criteria criteria = getSession().createCriteria(CurationContent.class);
        Projection maxPosition = Projections.max("position");
        criteria.setProjection(Projections.projectionList()
                .add(maxPosition)
        );
        criteria.add(Restrictions.eq("curation.id", curationContent.getCuration().getId()));
        criteria.add(Restrictions.eq("categoryID", curationContent.getCategoryID()));
        return (Integer) criteria.uniqueResult();
    }

    /**
     * Helper method to fetch the content record-ids which will be used to update the position column value.
     * These ids will be relative to the current position of the content
     *
     * @param curationContent - Content object to which new position is been set.
     * @return List of content record ids.
     */
    private Collection<Long> getContentIDList(CurationContent curationContent, Integer position) {
        Criteria criteria = getSession().createCriteria(CurationContent.class);
        Projection id_Projection = Projections.property("id").as("id");
        criteria.setProjection(Projections.projectionList()
                .add(id_Projection)
        );
        criteria.add(Restrictions.eq("curation.id", curationContent.getCuration().getId()));
        criteria.add(Restrictions.eq("categoryID", curationContent.getCategoryID()));

        // Since position is already set in the content so there wont be any range.
        // It will be always an increment for all position <= to the new position
        if (position == null) {
            criteria.add(Restrictions.ge("position", curationContent.getPosition()));
        } else {
            // To fetch the range of ids for which the position update is required.
            /*
             *  [100, 200, 300, 400, 500, 600] - consider the array index as the position.
             *
             *  Moving item 200 to index-5 then [ affected items in range 1-5 ie. 200, 300, 400, 500 ]
             *  1 - 5 = -4 results in a negative value. So items within the range 1-5 needs -1 decrement in their position
             *
             *  Moving item 400 moved to index-1 then [ affected items in range 3-1 i.e (200, 300) ]
             *  3 - 1 = 2 which is a positive. So items within the range 1-3 needs +1 increment in their position
             *
             */

            Criterion gtPosition = null;
            Criterion lePosition = null;

            if (curationContent.getPosition() > position) {
                gtPosition = Restrictions.lt("position", curationContent.getPosition());
                lePosition = Restrictions.ge("position", position);
            } else {
                gtPosition = Restrictions.gt("position", curationContent.getPosition());
                lePosition = Restrictions.le("position", position);
            }

            criteria.add(Restrictions.and(gtPosition, lePosition));
        }
        return criteria.list();
    }

    /**
     * Helper method to update the content positions.
     *
     * @param ids - List of content record ids.
     */
    private void adjustPositionColumn(Collection<Long> ids, boolean increment) {
        StringBuffer sbf = new StringBuffer("");
        sbf = sbf.append(" UPDATE CurationContent ");
        if (increment) {
            // Increment the position values
            logger.debug("Increment the position values");
            sbf = sbf.append(" SET position = position + 1 ");
        } else {
            // Decrement the position values
            logger.debug("Decrement the position values");
            sbf = sbf.append(" SET position = position - 1 ");
        }
        sbf = sbf.append(" WHERE id IN (:ids) ");
        Query query = getSession().createQuery(sbf.toString());
        query.setParameterList("ids", ids);
        int updated = query.executeUpdate();
        logger.debug(
                (increment ? "Incremented" : "Decremented") +
                        " the " + updated + " rows position " + ids.toString());
    }


    @Override
    public CurationContent updateContent(CurationContent curationContent) throws CurationContentException {
        logger.debug("Request for updateCurationContent()");
        try {
            update(curationContent);
        } catch (Exception e) {
            throw new CurationContentException("Unable to Update Curation Content for Id" + curationContent.getId() + " " + e.getMessage());
        }
        return curationContent;
    }

    @Override
    public boolean contentExist(CurationContent curationContent)
            throws CurationContentNotFoundException, CurationContentException {
        logger.debug("Request for curationContentExist()");
        CurationContent curationContent1 = get(curationContent.getId());
        if (curationContent1 == null) {
            throw new CurationContentNotFoundException("Unable to find Curation Content for id" + curationContent.getId());
        } else if (curationContent1.getStatus() == EntityConstants.Status.DELETED.getId()) {
            throw new CurationContentException("Curation Content is having delete state for Id " + curationContent.getId());
        } else if (curationContent1.getStatus() == EntityConstants.Status.PUBLISHED.getId()) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<CurationContent> getContents(final ServiceFilterBean filter)
            throws CurationContentException, CurationContentNotFoundException {
        logger.debug("Fetching the paginated results for curation content.");
        Collection<CurationContent> categoryContents = null;
        try {
            Criteria criteria = getSession().createCriteria(CurationContent.class);
            // Apply the generic criteria
            applyGenericCriteria(criteria, filter);
            criteria.add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
            categoryContents = criteria.list();
        } catch (Exception e) {
            throw new CurationContentException(e.getMessage());
        }
        if (categoryContents != null && categoryContents.size() > 0) {
            return categoryContents;
        }
        logger.debug("No curation contents found.");
        return categoryContents;
    }

    @Override
    public Collection<CurationContent> getCategoryContents(final ServiceFilterBean filter, final Long curationID, final Long categoryID) {
        Collection<CurationContent> categoryContents = null;
        try {
            final Criteria criteria = getSession().createCriteria(CurationContent.class);

            //Apply generic criteria
            applyGenericCriteria(criteria, filter);

            criteria.add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
            criteria.add(Restrictions.eq("curation.id", curationID));
            criteria.add(Restrictions.eq("categoryID", categoryID));
            categoryContents = criteria.list();
            if (!categoryContents.isEmpty()) {
                return categoryContents;
            }
            logger.info("No content found for category '" + categoryID + "' and curation - " + curationID);
            return Collections.emptyList();
        } catch (Exception e) {
            logger.info("Error while fetching the content for category " + categoryID + " and curation  " + curationID);
            return Collections.emptyList();
        }
    }


    @Override
    public Collection<CurationContent> getContents(final ServiceFilterBean filter, String columnName, Object columnValue)
            throws CurationContentException, CurationContentNotFoundException {
        logger.debug("Fetching the category content where " + columnName + " == " + columnValue);
        Collection<CurationContent> categoryContents = null;
        try {
            final Criteria criteria = getSession().createCriteria(CurationContent.class);
            //Apply generic criteria
            applyGenericCriteria(criteria, filter);

            criteria.add(Restrictions.eq(columnName, columnValue));
            categoryContents = criteria.list();
        } catch (Exception e) {
            throw new CurationContentException("Unable to fetch the content for a curation where " + columnName + " == " + columnValue);
        }
        if (categoryContents != null && categoryContents.size() > 0) {
            return categoryContents;
        }
        logger.debug("No content found for a curation where " + columnName + " == " + columnValue);
        return Collections.emptyList();
    }

    @Override
    public Collection<CurationContent> getContents(final ServiceFilterBean filter, Long curationID, Long categoryID)
            throws CurationContentException, CurationContentNotFoundException {
        logger.debug("Fetching the category content for Curation '" + curationID + "' and category '" + categoryID + "'");
        Collection<CurationContent> categoryContents = null;
        try {
            final Criteria criteria = getSession().createCriteria(CurationContent.class);
            //Apply generic criteria
            applyGenericCriteria(criteria, filter);

            criteria.add(Restrictions.eq("curation.id", curationID));
            criteria.add(Restrictions.eq("categoryID", categoryID));
            categoryContents = criteria.list();
        } catch (Exception e) {
            throw new CurationContentException("Unable to fetch the content for for Curation '" + curationID +
                    "' and category '" + categoryID + "'.\n\n" + e.getMessage());
        }
        if (categoryContents.size() > 0) {
            return categoryContents;
        }
        logger.debug("No content found for the specified Curation '" + curationID + "' and category '" + categoryID + "'");
        return Collections.emptyList();
    }

    @Override
    public Collection<CurationContent> getContents(final ServiceFilterBean filter, Long categoryID) {
        logger.debug("Request for getCurationContents()");
        List list = null;
        try {
            final Criteria criteria = getSession().createCriteria(CurationContent.class);
            //Apply generic criteria
            applyGenericCriteria(criteria, filter);

            criteria.add(Restrictions.eq("categoryID", categoryID));
            list = criteria.list();
        } catch (Exception e) {
            throw new CurationContentException(e.getMessage());
        }
        if (list == null) {
            throw new CurationContentNotFoundException("Curation Content isn't found ");
        }
        return list;
    }

    @Override
    public void deleteContent(Long curationID, Long contentID) {
        Query query = getSession().createQuery("DELETE FROM CurationContent cc WHERE cc.curation.id=:curationID AND " +
                "cc.cacheContent.id=:contentID");
        query.setParameter("curationID", curationID);
        query.setParameter("contentID", contentID);
        query.executeUpdate();
    }

    @Override
    public void updateFavouriteStatus(Long contentID, boolean status) {
        CurationContent content = get(contentID);
        content.setFavorite(status);
        update(content);
    }

    public void updateStickyStatus(Long contentID, boolean status) {
        CurationContent content = get(contentID);
        content.setSticky(status);
        update(content);
    }
}

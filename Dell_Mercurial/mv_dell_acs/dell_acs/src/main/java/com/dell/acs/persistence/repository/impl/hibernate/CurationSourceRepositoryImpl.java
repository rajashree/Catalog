package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.CurationSourceMapping;
import com.dell.acs.persistence.domain.CurationSourceProperty;
import com.dell.acs.persistence.repository.CurationRepository;
import com.dell.acs.persistence.repository.CurationSourceRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.DateUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mahalakshmi
 * @author $LastChangedBy: mmahalaxmi $
 * @version $Revision: 1595 $, $Date:: 7/19/12  3:01 PM#$
 */
@Repository
public class CurationSourceRepositoryImpl extends PropertiesAwareRepositoryImpl<CurationSource>
        implements CurationSourceRepository {


    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(CurationSourceRepositoryImpl.class);


    @Autowired
    private CurationRepository curationRepository;

    /**
     * default constructor.
     */
    public CurationSourceRepositoryImpl() {
        super(CurationSource.class, CurationSourceProperty.class);
    }

    @Override
    @Transactional
    public CurationSource getByHashCode(final Integer hashCode) {


        Collection<CurationSource> list =
                getSession().createCriteria(CurationSource.class).add(Restrictions.eq("hashCode", hashCode))
                        .setMaxResults(1).list();
        if (list != null && list.isEmpty()) {
            return null;
        }
        return onFindForObject(list.iterator().next());
    }

    @Override
    @Transactional
    public void removeSourceMapping(final long curationID, final long curationSourceID) {
        Integer status = EntityConstants.Status.DELETED.getId();
        //TODO  query
        Criteria criteria = getSession().createCriteria(CurationSourceMapping.class);
        CurationSourceMapping curationSourceMapping = (CurationSourceMapping) criteria
                .add(Restrictions.eq("pk.curation.id", curationID))
                .add(Restrictions.eq("pk.curationSource.id", curationSourceID)).uniqueResult();
        curationSourceMapping.setStatus(status);
        getSession().saveOrUpdate(curationSourceMapping);
    }

    @Override
    @Transactional
    public CurationSourceMapping getSourceMapping(final long curationID, final long curationSourceID) {

        Integer status = EntityConstants.Status.PUBLISHED.getId();
        Criteria criteria = getSession().createCriteria(CurationSourceMapping.class);
        CurationSourceMapping curationSourceMapping = (CurationSourceMapping) criteria
                .add(Restrictions.eq("pk.curation.id", curationID))
                .add(Restrictions.eq("pk.curationSource.id", curationSourceID)).uniqueResult();

        if (curationSourceMapping != null) {
            onFindForObject(curationSourceMapping.getCurationSource());
        }
        return curationSourceMapping;
    }

    @Override
    @Transactional(readOnly = true)
    public CurationSource getSourceToUpdateCache() {
        Integer status = EntityConstants.Status.PUBLISHED.getId();

        Criteria criteria = getSession().createCriteria((CurationSourceMapping.class));
        Collection ids = criteria.add(Restrictions.eq("status", status))
                .setProjection(Projections.property("pk.curationSource.id"))
                .list();
        // no active ones found?
        if (ids == null || ids.isEmpty()) {
            return null;
        }

        Criterion inQueueRestriction = Restrictions.eq("executionStatus", EntityConstants.ExecutionStatus.IN_QUEUE.getId());
        Criterion completedRestriction = Restrictions.eq("executionStatus", EntityConstants.ExecutionStatus.DONE.getId());

        /**
         *Order on
         * ExecutionStatus - to get In queue source first
         * LastUpdatedTime - to get last updated source
         */
        Collection items = getSession().createCriteria(CurationSource.class).add(Restrictions.in("id", ids))
                .add(Restrictions.or(inQueueRestriction,completedRestriction)).addOrder(Order.asc("executionStatus")).addOrder(Order.asc("lastUpdatedTime")).list();

        //check if items are not found
        if (items == null || items.isEmpty()) {
            return null;
        }
        Iterator itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            //Get curationSource
            CurationSource curationSource = (CurationSource) itemIterator.next();
            //Exection Status
            Integer executionStatus = curationSource.getExecutionStatus();
            //If execution status is 0(InQueue) return the curation source
            if(executionStatus== EntityConstants.ExecutionStatus.IN_QUEUE.getId()){
                return curationSource;
            }else{
                //Execution status is 200(Done)- hence check for the lastupdated time is less than 1 hr
                //Get last updated time
                Date lastUpdatedTime = curationSource.getLastUpdatedTime();
                //Convert current Date to string
                String dateString = DateUtils.to24formatString(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(dateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //setting behind hour to 1 hour
                c.roll(Calendar.HOUR_OF_DAY, -1);
                dateString = sdf.format(c.getTime());
                //Date computed by setting behind 1 hour
                Date comparedDate = null;
                try {
                    comparedDate = DateUtils.getDate(dateString, "yyyy-MM-dd HH:mm:ss");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long timeDifference  = (new Date().getTime()) - lastUpdatedTime.getTime();
                int hours   = (int) ((timeDifference / (1000*60*60)) % 24);

                if ((lastUpdatedTime.compareTo(comparedDate) < 0 && hours>0)){
                    return curationSource;
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void addSourceMapping(final long curationID, final long curationSourceID) {

        CurationSourceMapping sourceMapping = getSourceMapping(curationID, curationSourceID);
        if (sourceMapping == null) {
            CurationSourceMapping curationSourceMapping = new CurationSourceMapping();
//saving the sourceMapping
            curationSourceMapping.getPk().setCuration(curationRepository.getCurationById(curationID));
            curationSourceMapping.getPk().setCurationSource(get(curationSourceID));

            curationSourceMapping.setStatus(EntityConstants.Status.PUBLISHED.getId());
            getSession().save(curationSourceMapping);

// On delete of a source ,Status of source  is set to DELETED{0} in mapping
// when same source is added again it is Enabled by setting the status of source to PUBLISHED{1}

        } else if (sourceMapping.getStatus() == 0) {
            sourceMapping.setStatus(EntityConstants.Status.PUBLISHED.getId());
            getSession().update(sourceMapping);
        }


    }

    @Override
    @Transactional
    public Collection<CurationSource> getSources(final long curationID) {

        Integer status = EntityConstants.Status.PUBLISHED.getId();

//to retrive  the list of Active curationSource by curationID
        Criteria criteria = getSession().createCriteria(CurationSourceMapping.class);
        criteria.add(Restrictions.eq("pk.curation.id", curationID)).add(Restrictions.eq("status", status));
        List<CurationSourceMapping> curationSourceMappingList = criteria.list();

//   onFindForList(curationSourceMappingList);
        List<CurationSource> curationSourceList = new ArrayList<CurationSource>();
        Iterator iterator = curationSourceMappingList.iterator();
        while (iterator.hasNext()) {
            CurationSourceMapping curationSourceMapping = (CurationSourceMapping) iterator.next();
            curationSourceList.add(curationSourceMapping.getCurationSource());
        }
        return onFindForList(curationSourceList);
    }

    @Override
    public Collection<CurationSourceMapping> getSourceMapping(final Long curationID) {
        Integer status = EntityConstants.Status.PUBLISHED.getId();
        Criteria criteria = getSession().createCriteria(CurationSourceMapping.class);
        criteria.add(Restrictions.eq("pk.curation.id", curationID)).add(Restrictions.eq("status", status));
        List<CurationSourceMapping> items = criteria.list();
        List<CurationSource> curationSourceList = new ArrayList<CurationSource>();
        Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            CurationSourceMapping curationSourceMapping = (CurationSourceMapping) iterator.next();
            curationSourceList.add(curationSourceMapping.getCurationSource());
        }
        onFindForList(curationSourceList);
        return items;
    }

    public Collection<CurationSource> getSources(ServiceFilterBean filter, long curationID) {
        Integer status = EntityConstants.Status.PUBLISHED.getId();
//to retrive  the list of Active curationSource by curationID
        Criteria criteria = getSession().createCriteria(CurationSourceMapping.class);
        applyGenericCriteria(criteria, filter);
        criteria.add(Restrictions.eq("pk.curation.id", curationID)).add(Restrictions.eq("status", status));
        List<CurationSourceMapping> curationSourceMappingList = criteria.list();

        List<CurationSource> curationSourceList = new ArrayList<CurationSource>();
        Iterator iterator = curationSourceMappingList.iterator();
        while (iterator.hasNext()) {
            CurationSourceMapping curationSourceMapping = (CurationSourceMapping) iterator.next();
            curationSourceList.add(curationSourceMapping.getCurationSource());
        }
        return onFindForList(curationSourceList);
    }

    @Override
    @Transactional
    public Collection<Curation> getCurations(final Long cuartionSourceID) {

        Integer status = EntityConstants.Status.PUBLISHED.getId();

//to retrive  the list of Active Curation by cuartionSourceID
        Criteria criteria = getSession().createCriteria(CurationSourceMapping.class);
        criteria.add(Restrictions.eq("pk.curationSource.id", cuartionSourceID)).add(Restrictions.eq("status", status));
        List<CurationSourceMapping> curationSourceMappingList = criteria.list();

        List<Curation> curationList = new ArrayList<Curation>();
        Iterator iterator = curationSourceMappingList.iterator();
        while (iterator.hasNext()) {
            CurationSourceMapping curationSourceMapping = (CurationSourceMapping) iterator.next();
            curationList.add(curationSourceMapping.getPk().getCuration());
        }

        return curationList;
    }

    @Override
    public Collection<CurationSource> getSource() {
        Integer status = EntityConstants.Status.PUBLISHED.getId();

        Criteria criteria = getSession().createCriteria((CurationSourceMapping.class));
        List<CurationSourceMapping> curationSourceMappingList = criteria.add(Restrictions.eq("status", status)).list();

        List<CurationSource> sourceList = new ArrayList<CurationSource>();
        Iterator iterator = curationSourceMappingList.iterator();
        while (iterator.hasNext()) {
            CurationSourceMapping curationSourceMapping = (CurationSourceMapping) iterator.next();
            sourceList.add(curationSourceMapping.getPk().getCurationSource());
        }

        return onFindForList(sourceList);

    }

    @Override
    public Collection<Curation> getCurations() {
        Integer status = EntityConstants.Status.PUBLISHED.getId();

        Criteria criteria = getSession().createCriteria((CurationSourceMapping.class));
        List<CurationSourceMapping> curationSourceMappingList = criteria.add(Restrictions.eq("status", status)).list();

        List<Curation> curationList = new ArrayList<Curation>();
        Iterator iterator = curationSourceMappingList.iterator();
        while (iterator.hasNext()) {
            CurationSourceMapping curationSourceMapping = (CurationSourceMapping) iterator.next();
            curationList.add(curationSourceMapping.getPk().getCuration());
        }

        return curationList;

    }

    /**
     * Give the status of Source
     *
     * @param curationID
     * @param curationSourceID
     * @return
     */
    @Override
    public CurationSourceMapping checkStatus(final long curationID, final long curationSourceID) {

        Criteria criteria = getSession().createCriteria(CurationSourceMapping.class);
        CurationSourceMapping sourceMapping = (CurationSourceMapping) criteria
                .add(Restrictions.eq("pk.curation.id", curationID))
                .add(Restrictions.eq("pk.curationSource.id", curationSourceID)).uniqueResult();
        return sourceMapping;
    }

}

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationProperty;
import com.dell.acs.persistence.repository.CurationRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.Assert;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/19/12 1:57 PM#$ */
@Repository
public class CurationRepositoryImpl extends PropertiesAwareRepositoryImpl<Curation>
        implements CurationRepository {

    private Logger logger = LoggerFactory.getLogger(CurationRepositoryImpl.class);

    public CurationRepositoryImpl() {
        super(Curation.class, CurationProperty.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Curation> getAllCurationByRetailerSite(final Long retailerSiteId) {
        Criteria criteria = getSession().createCriteria(Curation.class);
        criteria.add(Restrictions.eq("retailerSite.id", retailerSiteId));
        Collection<Curation> curations= criteria.list();
        return curations;
    }

    @Override
    @Transactional(readOnly = true)
    public Curation getCurationByExample(final Curation curation){
        Criteria criteria = getSession().createCriteria(Curation.class);
        criteria.add(Restrictions.eq("name", curation.getName()));
        criteria.add(Restrictions.eq("retailerSite.id", curation.getRetailerSite().getId()));
        return (Curation)criteria.uniqueResult();
    }

    @Override
    @Transactional
    public void deleteCuration(final Long curationId) {
        Curation curation = getCurationById(curationId);
        Assert.notNull(curation, "Unable to delete the record, record not exist");
        //TODO: Asish - Add a dependency check with curation content ,then remove the record,if there is no dependency exist.
        // remove(curation);
        curation.setActive(false);
        // Also remove the associated Taxonomies and its related categories
        logger.info("Record successfully deleted...");
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = EntityNotFoundException.class)
    public Curation getCurationById(Long id) throws  EntityNotFoundException {
        Curation curation = get(id);
        if(curation == null){
            throw new EntityNotFoundException("Curation with id - " + id + " not found.");
        }
        return curation;
    }

    @Override
    public boolean checkCurationExistence(final String curationName) {
        return false;
    }
}

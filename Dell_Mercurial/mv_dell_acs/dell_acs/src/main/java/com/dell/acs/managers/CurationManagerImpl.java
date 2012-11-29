package com.dell.acs.managers;

import com.dell.acs.CurationNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.Taxonomy;
import com.dell.acs.persistence.domain.TaxonomyCategory;
import com.dell.acs.persistence.repository.CurationRepository;
import com.dell.acs.persistence.repository.TaxonomyRepository;
import com.sourcen.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Date;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/19/12 1:56 PM#$ */
@Service
public class CurationManagerImpl implements CurationManager {

    private static Logger logger = LoggerFactory.getLogger(CurationManagerImpl.class);

    @Autowired
    private TaxonomyManager taxonomyManager;

    @Autowired
    private CurationRepository curationRepository;

    @Autowired
    private TaxonomyRepository taxonomyRepository;

    @Override
    @Transactional(noRollbackFor = EntityNotFoundException.class )
    public Curation saveCuration(final Curation curation) {
        Assert.notNull(curation, "Curation object can't be null.");
        Curation curationObj = getCurationByName(curation);
         if (curationObj != null) {
            logger.info("Curation by name '" + curation.getName() + "' already exists for the retailer '"
                    + curationObj.getRetailerSite().getSiteName());
            return curationObj;
        }

        if(curation.getId() == null){
            curationRepository.insert(curation);
            // For every new curation we will have new taxonomy created
            // Taxonomy name will be in the format - curation.<curation_id>
            createTaxonomyForCuration(curation);
        }else{
            updateCuration(curation);
        }
        return curation;
    }

    /**
     Helper method to create the Taxonomy specific to the new Curation

     @param name - Name of Taxonomy to be created. This name will be same as the Curation name
     */
    private void createTaxonomyForCuration(Curation curation) {
        // skammar - CS-403
        String taxonomyName = "curation.".concat(curation.getId().toString());
        // Check if taxonomy for a curation already exists
        Taxonomy taxonomy =  taxonomyManager.getTaxonomyByName(taxonomyName, EntityConstants.Entities.CURATION.getId());
        if(taxonomy == null){
            logger.info("There is no taxonomy associated to curation - " + curation.getId() +  ". So creating a new.");
            taxonomy = new Taxonomy();
            taxonomy.setName(taxonomyName);
            taxonomy.setRetailerSite(curation.getRetailerSite());
            taxonomy.setType(EntityConstants.Entities.CURATION.getId());
            taxonomyRepository.insert(taxonomy);
        }else{
            logger.info("Taxonomy for this curation - " + curation.getId() +  " is already created.");
        }
    }


    private Curation getCurationByName(Curation curation) {
        Curation curationExampleObj = new Curation();
        curationExampleObj.setName(curation.getName());
        curationExampleObj.setRetailerSite(curation.getRetailerSite());
        return curationRepository.getCurationByExample(curationExampleObj);
    }

    @Override
    @Transactional
    public void updateCuration(Curation curation) {
        RetailerSite retailerSite = retailerManager.getRetailerSite(curation.getRetailerSite().getId());

        Assert.notNull(retailerSite, "RetailerSite Object Not Found");
        logger.info("RetailerSite Object Found successfully");

        Assert.notNull(curation, "Curation object can't be null.");
        logger.info("Curation Object Found successfully");

        curation.setRetailerSite(retailerSite);
        curation.setModifiedDate(new Date());
        curationRepository.update(curation);

        logger.debug("Updated document - " + curation.getId() + " :: " + curation.getName());
    }


    @Override
    public void deleteCuration(final Long curationID) {
        // Delete Categories
        for(TaxonomyCategory cat: taxonomyManager.getCategories(curationID, false)){
            taxonomyManager.deleteCurationCategory(curationID, cat.getId());
        }
        // Delete Taxonomy
        Taxonomy tx = taxonomyManager.getCurationTaxonomy(curationID);
        taxonomyRepository.remove(tx);
        // Delete Curation
        curationRepository.deleteCuration(curationID);
    }


    @Override
    @Transactional
    public Curation getCuration(final Long id) throws CurationNotFoundException {
        Curation curation = curationRepository.getCurationById(id);
        Assert.notNull(curation, "No curation object found for id: "+id);
        return curation;
    }

    @Override
    public Collection<Curation> getAllCurationByRetailerSite(final Long retailerSiteId) {
        return curationRepository.getAllCurationByRetailerSite(retailerSiteId);
    }


    @Autowired
    private RetailerManager retailerManager;

    public void setRetailerManager(final RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

    public void setCurationRepository(final CurationRepository curationRepository) {
        this.curationRepository = curationRepository;
    }
}

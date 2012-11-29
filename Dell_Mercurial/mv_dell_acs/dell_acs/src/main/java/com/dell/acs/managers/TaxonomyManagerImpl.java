/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.TaxonomyCategoryRepository;
import com.dell.acs.persistence.repository.TaxonomyRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

/**
 * User: Chethan
 */
@Service
public class TaxonomyManagerImpl implements TaxonomyManager {

    private static Logger logger = Logger.getLogger(TaxonomyManagerImpl.class);

    @Autowired
    private TaxonomyCategoryRepository taxonomyCategoryRepository;

    @Autowired
    private TaxonomyRepository taxonomyRepository;

    @Autowired
    private CurationContentManager curationContentManager;

    @Autowired
    private CurationManager curationManager;



    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<TaxonomyCategory> getTree(TaxonomyCategory category) {
        return taxonomyCategoryRepository.getTree(category);
    }

    @Override
    @Transactional
    public void createTaxonomy(final RetailerSite retailerSite, final String name, final Integer type) {
        Taxonomy taxonomy = new Taxonomy(retailerSite, name, type);
        taxonomyRepository.insert(taxonomy);
        TaxonomyCategory category = new TaxonomyCategory("ROOT");
        category.setTaxonomy(taxonomy);
        category.setDepth(0);
        taxonomyCategoryRepository.insert(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Taxonomy getTaxonomy(final RetailerSite retailerSite, final String name)  throws EntityNotFoundException {
        Taxonomy taxonomy = taxonomyRepository.getTaxonomy(retailerSite, name);
        if(taxonomy == null){
            throw new EntityNotFoundException("Taxonomy by name '" + name + "' for rertailer site '" + retailerSite.getSiteName() + "' not found.");
        }
        return taxonomy;
    }

    @Override
    @Transactional(readOnly =  true)
    public Collection<Long> getRecursiveSubCategories(Long categoryID, Collection<Long> categories) {
        TaxonomyCategory category = taxonomyCategoryRepository.get(categoryID);
        return taxonomyCategoryRepository.getRecursiveSubCategories(category, categories);
    }


    @Override
    @Transactional(readOnly = true)
    public TaxonomyCategory getCurationCategory(Long curationID, Long categoryID) throws EntityNotFoundException{
        Taxonomy taxonomy = getTaxonomyByName("curation."+curationID, EntityConstants.Entities.CURATION.getId());
        if(taxonomy == null){
            throw new EntityNotFoundException("Taxonomy for the curation '" + curationID + "' not found.");
        }
        TaxonomyCategory category = taxonomyCategoryRepository.getCategory(taxonomy.getId(), categoryID);
        if(category == null){
            throw new EntityNotFoundException("Taxonomy category by id '" + categoryID + "' not found.");
        }
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public TaxonomyCategory getTaxonomyCategory(Long id) throws EntityNotFoundException {
        TaxonomyCategory category = taxonomyCategoryRepository.get(id);
        if(category == null){
            throw new EntityNotFoundException("Taxonomy category by id '" + id + "' not found.");
        }
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public Taxonomy getTaxonomyByName(String name, Integer type)  throws EntityNotFoundException {
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setName(name);
        if(type == null){
            taxonomy.setType(EntityConstants.Entities.PRODUCT.getId());
        }else {
            taxonomy.setType(type);
        }
        taxonomy.setName(name);
        taxonomy = taxonomyRepository.getUniqueByExample(taxonomy);

        if(taxonomy == null){
            //throw new EntityNotFoundException("Taxonomy for curation with id - " + name + " not found");
            logger.info("Taxonomy for curation with id - " + name + " not found");
        }
        return taxonomy;
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteCurationCategory(Long curationID, Long categoryID) {
        // Remove the associated content for the category and then remove the category
        Collection<CurationContent> categoryItems = curationContentManager.getContents(new ServiceFilterBean(), curationID, categoryID);
        for(CurationContent content: categoryItems){
            curationContentManager.deleteContent(content);
        }
        Taxonomy taxonomy = getCurationTaxonomy(curationID);
        taxonomyCategoryRepository.deleteCategory(taxonomy.getId(), categoryID);
    }

    /**
     * Method to create the Curation related to TaxonomyCategories
     */
    @Override
    @Transactional
    public TaxonomyCategory createCategory(Taxonomy taxonomy, String name, Integer position, Long parentID) {
        TaxonomyCategory category = new TaxonomyCategory();
        if(parentID != null && parentID > 0){
            TaxonomyCategory parentCategory = taxonomyCategoryRepository.get(parentID);
            category.setParent(parentCategory);
        }

        // If no position is passed then save it at the last
        if(position == null){
            position = taxonomyCategoryRepository.getLastCategoryPositionForTaxonomy(taxonomy.getId(), parentID);
        }

        category.setName(name);
        category.setPosition(position);

        category.setTaxonomy(taxonomy);
        category.setDepth(0);
        // Confirm with navin - Are we really using these fields for constructing the tree ?
        category.setLeftNode(0);
        category.setRightNode(0);
        taxonomyCategoryRepository.insert(category);
        return category;
    }

    @Override
    @Transactional
    public TaxonomyCategory updateCategory(Long categoryID, String name, Long parentID, Integer position){
        TaxonomyCategory category = taxonomyCategoryRepository.get(categoryID);
        category.setName(name);
        if(position != null){
            category.setPosition(position);
        }
        if(parentID != null && parentID > 0 ){
            category.setParent(taxonomyCategoryRepository.get(parentID));
        }
        taxonomyCategoryRepository.update(category);
        return category;
    }

    @Override
    @Transactional(readOnly =  true)
    public Collection<TaxonomyCategory> getCategories(Long parentCategoryID) {
        return taxonomyCategoryRepository.getCategories(parentCategoryID);
    }

    @Transactional(readOnly = true)
    public Collection<TaxonomyCategory> getCategories(final Long curationID, boolean topLevelOnly) throws EntityNotFoundException {
        Taxonomy taxonomy = getCurationTaxonomy(curationID);
        return taxonomyCategoryRepository.getCategories(taxonomy.getId(), topLevelOnly);
    }

    public Taxonomy getCurationTaxonomy(Long curationID){
        // Curation taxonomies will have curation in the prefix
        // ex: curation.<id>
        String curationTaxonomyName = "curation.".concat(curationID.toString());
        Taxonomy  taxonomy = getTaxonomyByName(curationTaxonomyName, EntityConstants.Entities.CURATION.getId());
        if(taxonomy == null){
            //throw new ObjectNotFoundException("Taxonomy by name - " + curationTaxonomyName + " NOT found.");
            logger.info("Taxonomy by name - " + curationTaxonomyName + " NOT found. So creating new one.");
            Curation curation =  curationManager.getCuration(curationID);
            taxonomy = new Taxonomy();
            taxonomy.setName(curationTaxonomyName);
            taxonomy.setRetailerSite(curation.getRetailerSite());
            taxonomy.setType(EntityConstants.Entities.CURATION.getId());
            taxonomyRepository.insert(taxonomy);
        }
        return taxonomy;
    }


}

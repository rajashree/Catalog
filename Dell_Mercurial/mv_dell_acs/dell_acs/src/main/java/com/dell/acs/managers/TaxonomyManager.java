/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.Taxonomy;
import com.dell.acs.persistence.domain.TaxonomyCategory;
import com.sourcen.core.managers.Manager;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

/**
 * User: Chethan
 * Date: 3/28/12
 * Time: 1:36 AM
 */
public interface TaxonomyManager extends Manager {

    void createTaxonomy(RetailerSite retailerSite, String name, Integer type);

    TaxonomyCategory createCategory(Taxonomy taxonomy, String name, Integer position, Long parentID);

    TaxonomyCategory updateCategory(Long categoryID, String name,Long parentID, Integer position);

    Taxonomy getTaxonomy(RetailerSite retailerSite, String name) throws EntityNotFoundException;

    List<TaxonomyCategory> getTree(TaxonomyCategory category);

    /**
     * Method used in campaign - manage-items.jsp page to fetch all sub-categories id's
     */
    Collection<Long> getRecursiveSubCategories(Long categoryID, Collection<Long> categories);


    TaxonomyCategory getTaxonomyCategory(Long id) throws EntityNotFoundException;

    TaxonomyCategory getCurationCategory(Long curationID, Long categoryID)  throws EntityNotFoundException;

    void deleteCurationCategory(Long curationID, Long categoryID);

    Taxonomy getCurationTaxonomy(Long curationID);

    Collection<TaxonomyCategory> getCategories(Long parentCategoryID);

    Collection<TaxonomyCategory> getCategories(final Long curationID, boolean topLevelOnly);

    Taxonomy getTaxonomyByName(String name, Integer type);
}

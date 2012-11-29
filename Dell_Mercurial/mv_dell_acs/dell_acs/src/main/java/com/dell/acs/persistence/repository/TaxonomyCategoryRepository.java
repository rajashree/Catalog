/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */
package com.dell.acs.persistence.repository;


import com.dell.acs.persistence.domain.Taxonomy;
import com.dell.acs.persistence.domain.TaxonomyCategory;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author Adarsh.
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 2801 $, $Date:: 2012-06-01 07:55:14#$
 */

public interface TaxonomyCategoryRepository extends IdentifiableEntityRepository<Long, TaxonomyCategory> {

    /**
     * getRootCategory() is for fetching the category
     * by providing the category name
     *
     * @return Category bean object
     */
    TaxonomyCategory getRootCategory(Taxonomy taxonomy);

    void deleteCategory(Long taxonomyID, Long categoryID);

    /**
     * getCategory() this will return the category object on the
     * basic of Category name and parent category Id
     *
     * @param categoryName name of the category
     * @param parent       parent Category Id
     *
     * @return return the Category
     */
    public TaxonomyCategory getCategory(Taxonomy taxonomy, TaxonomyCategory parent, String categoryName);


    /**
     * Method to return the MAX + 1 position for a new taxonomy category
     * @param taxonomyID -
     * @return
     */
    Integer getLastCategoryPositionForTaxonomy(final Long taxonomyID, Long parentID);

    /**
     * getTree() will fetch the category tree on the
     * basic of the provided category
     *
     * @param category
     *
     * @return
     */
    List<TaxonomyCategory> getTree(TaxonomyCategory category);

    Collection<Long> getRecursiveSubCategories(TaxonomyCategory category, Collection<Long> categories);

    TaxonomyCategory getCategory(final Long taxonomyID,final Long categoryID);

    Collection<TaxonomyCategory> getCategories(final Long taxonomyID, boolean topLevelOnly);

    Collection<TaxonomyCategory> getCategories(final Long parentID);
}

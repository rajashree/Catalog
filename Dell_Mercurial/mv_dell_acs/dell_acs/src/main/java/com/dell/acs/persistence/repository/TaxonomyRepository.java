package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.Taxonomy;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 Created by IntelliJ IDEA. User: Adarsh Date: 3/27/12 Time: 1:29 PM To change this template use File | Settings | File
 Templates.
 */
public interface TaxonomyRepository extends IdentifiableEntityRepository<Long, Taxonomy> {

    /**
     saveOrUpdateTaxomony()

     @param taxonomy
     */
    public void saveOrUpdateTaxomony(Taxonomy taxonomy);

    /**
     getTaxonomy() for getting the Taxonomy

     @param retailerSite object for filter
     @param name         of the Taxonomy
     @return Taxomony object
     */
    public Taxonomy getTaxonomy(RetailerSite retailerSite, String name);

    Taxonomy getTaxonomy(Long retailerSiteID, String name);
}

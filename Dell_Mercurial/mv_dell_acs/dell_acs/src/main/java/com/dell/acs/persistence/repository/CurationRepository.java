package com.dell.acs.persistence.repository;

import com.dell.acs.CurationNotFoundException;
import com.dell.acs.persistence.domain.Curation;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $,
 $Date:: 7/18/12 4:34 PM#$
 */

public interface CurationRepository extends IdentifiableEntityRepository<Long,Curation> {

    Curation getCurationByExample(final Curation curation);

    /**
     get all curation by name.
     @param curationName
     @return
     */
    Collection<Curation> getAllCurationByRetailerSite(Long retailerSiteId);

    /**
     delete the curation , if not associated with CurationContent.
     @param curation
     @param curationContent
     */
    public void deleteCuration(Long curationId);

    /**
      check whether the curation already exist or not.
     @param curationName
     @return
     */
    public boolean checkCurationExistence(String curationName);

    public Curation getCurationById(Long id) throws EntityNotFoundException;



}

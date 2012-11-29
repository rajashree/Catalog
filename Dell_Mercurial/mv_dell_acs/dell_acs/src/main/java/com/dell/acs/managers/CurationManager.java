package com.dell.acs.managers;

import com.dell.acs.CurationNotFoundException;
import com.dell.acs.persistence.domain.Curation;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $,
 $Date:: 7/19/12 11:18 AM#$ */

public interface CurationManager extends Manager {

    /**
     get all curation by name.
     @param curationName
     @return
     */
    public abstract Collection<Curation> getAllCurationByRetailerSite(Long retailerSiteId);

    /**
     Save the curation.
     @param curation
     */
    public abstract Curation saveCuration(Curation curation);

    /**
     update the curation.
     @param curation
     */
    public abstract void updateCuration(Curation curation);

    /**
     delete the curation , if not associated with CurationContent.
     @param curation
     @param curationContent
     */
    public abstract void deleteCuration(Long curationId);


    Curation getCuration(final Long id) throws CurationNotFoundException;

}

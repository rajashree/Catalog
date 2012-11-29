package com.dell.acs.persistence.repository;


import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.CurationSourceMapping;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;


/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 7/18/12  4:41 PM#$ */
public interface CurationSourceRepository extends IdentifiableEntityRepository<Long, CurationSource> {


    /**
     Method to retrive the Curation Source by unique hasCode generated .

     @param hash - Integer - The computed HashCode values for a CuartionSource
     @return Curation Source Entity
     */
    CurationSource getByHashCode(Integer hash);


    CurationSource getSourceToUpdateCache();

    /**
     Updates the Status of sourcemapping to Deleted

     @param curationSourceID
     @param curationID        */
    void removeSourceMapping( long curationID,long curationSourceID);


    /**
     retrives the sourcemapping for given curation and curationSource

     @param curationSourceID
     @param curationID
     @return
     */
    CurationSourceMapping getSourceMapping(long curationID,long curationSourceID);

    /**
     saves the sourcemapping for curation and curationSource if not present

     @param curationSourceID
     @param curationID        */
    void addSourceMapping(long curationID,long curationSourceID);

    /**
     status of source mapping

     @param curationSourceID
     @param curationID
     @return
     */
    CurationSourceMapping checkStatus(long curationID,long curationSourceID);

    /**
     Returns the Collection of CuartionSource Associated to a Curation

     @param curationID
     @return Collection<CurationSource>
     */
    Collection<CurationSource> getSources(long curationID);

    /**
     Given the CourationSource,retrives all the cuartion to which it is associated

     @param cuartionSourceID
     @return Collection<Curation>
     */
    Collection<Curation> getCurations(Long cuartionSourceID);

       Collection<CurationSource> getSource();

       Collection<Curation> getCurations();

    Collection<CurationSourceMapping> getSourceMapping(Long curationID);
}

package com.dell.acs.managers;


import com.dell.acs.ObjectLockedException;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.HandlerNotFoundException;
import com.dell.acs.persistence.domain.CurationSourceMapping;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

import com.dell.acs.persistence.domain.CurationSourceMapping;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

import com.dell.acs.persistence.domain.CurationSourceMapping;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 7/19/12  3:01 PM#$ */
public interface CurationSourceManager {

    /**
     Creates Cuartion Source for a Cuartion

     @param curationSource holds the data which needs to be saved for CurationSource
     @param curationID
     @return
     @throws EntityExistsException when curationSource already exist for that curation
     */


    CurationSource createSource(long curationID, CurationSource curationSource) throws EntityExistsException;

    /**
     Updates the CurationSource Entity

     @param curationSource CurationSource -The values which needs to be updated .
     @return CurationSource
     @throws EntityExistsException
     */
    CurationSource updateSource(CurationSource curationSource) throws
            EntityExistsException;


    /**
     retrives a cuartionSource associated to a cuartionID

     @param cuartionID
     @param curationSourceID
     @return
     @throws EntityNotFoundException when we dont find the mapping between cuartion and  curationSource
     */
    CurationSource getSource(long cuartionID, long curationSourceID) throws EntityNotFoundException;


    /**
     returns the collection of curationSource associated to a cuartion

     @param cuartionID
     @return
     @throws EntityNotFoundException
     */
    Collection<CurationSource> getSources(long cuartionID) throws EntityNotFoundException;

    /**
     retrives the CuartionSource  by  ID

     @param curationSourceID
     @return
     @throws EntityNotFoundException
     */
    CurationSource getSource(Long curationSourceID) throws EntityNotFoundException;

    /**
     retrives all the cuartion associated to the curationSource

     @param cuartionSourceID
     @return
     @throws EntityNotFoundException
     */
    Collection<Curation> getCurations(Long cuartionSourceID) throws EntityNotFoundException;


    /**
     retrives the sourcemapping for given curation and curationSource

     @param cuartionSourceID
     @param curationID
     @return
     @throws EntityNotFoundException
     */
    CurationSourceMapping getSourceMapping(long curationID, long cuartionSourceID) throws EntityNotFoundException;

    /**
     saves the sourcemapping for curation and curationSource if not present

     @param cuartionSourceID
     @param curationID        */
    void addSourceMapping(long curationID, long cuartionSourceID);

    void updateSourceMapping(CurationSourceMapping curationSourceMapping);

    /**
     Updates the Status of sourcemapping to Deleted

     @param curationSourceID
     @param curatoinID
     @throws EntityNotFoundException
     */
    void removeSourceMapping(final long curatoinID, final long curationSourceID) throws EntityNotFoundException;

    /**
     return a curationSource who's cache needs to be updated.

     @return CurationSource entity or null if none is found.
     */
    CurationSource getSourceToUpdateCache();

    /**
     invoke a CurationSourceDataHandler for a specific CurationSource.

     @param curationSource entity whos' cache will be updated.
     @throws com.dell.acs.ObjectLockedException
     if we are not able to acquire lock on this object.
     @throws com.sourcen.core.HandlerNotFoundException
     when we are not able to find a CurationSourceDataHandler for this source.
     */
    void updateCache(CurationSource curationSource) throws ObjectLockedException, HandlerNotFoundException;

    /**
     retrives the Collection of active sources

     @return Collection of Active <CurationSource>
     */

    Collection<CurationSource> getSource();

    /**
     retrives the Collection of active Curations

     @return Collection of Active <Curation>
     */
    Collection<Curation> getCurations();


}

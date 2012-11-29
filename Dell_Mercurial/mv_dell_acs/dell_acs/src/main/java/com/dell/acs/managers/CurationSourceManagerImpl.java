package com.dell.acs.managers;

import com.dell.acs.CurationCacheAlreadyExistsException;
import com.dell.acs.CurationCacheException;
import com.dell.acs.ObjectLockedException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.curation.CurationSourceDataHandler;
import com.dell.acs.curation.CurationSourceHandlerStrategy;
import com.dell.acs.curation.ExecutorException;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.CurationSourceMapping;
import com.dell.acs.persistence.repository.CurationSourceRepository;
import com.sourcen.core.HandlerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 7/19/12  3:01 PM#$ */
@Service
public class CurationSourceManagerImpl implements CurationSourceManager {

    /**
     logger instance.
     */
    private static final Logger logger = LoggerFactory.getLogger(CurationSourceManagerImpl.class);

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false,
            rollbackFor = EntityExistsException.class)
    public CurationSource createSource(final long curationID, CurationSource curationSource)
            throws EntityExistsException {

        EntityConstants.CurationSourceType sourceType =
                EntityConstants.CurationSourceType.getById(curationSource.getSourceType());
        for (String key : sourceType.getProperties()) {
            if (!curationSource.getProperties().hasProperty(key)) {
                throw new IllegalArgumentException(
                        "sourceType:" + sourceType.name() + " requires property :=" + key + " but not found.");
            }
        }

        int computedHashCode = computeHashCode(curationSource);

        CurationSource curationSourceForHash = curationSourceRepository.getByHashCode(computedHashCode);
        if (curationSourceForHash != null) {
            long sourceID = curationSourceForHash.getId();
            CurationSourceMapping sourceMapping = curationSourceRepository
                    .getSourceMapping(curationID, sourceID); //fix for adding same source for a different curation app
            if (sourceMapping != null) {
                if (sourceMapping.getStatus() == 1) {
                    logger.debug(
                            "Source mapping already exist for CurationID : " + curationID + " and for source ID: " +
                                    sourceID);
                    throw new EntityExistsException("The Curation already has the CurationSource defined.");
                } else {//Enable the deleted source
                    addSourceMapping(curationID, sourceID);
                    logger.debug("Successfully added curation source for curation: " + curationID);
                    curationSource = curationSourceForHash;
                }
            } else {//Associate the existing CurationSource for a new Curation i.e. create an entry in the mapping table
                addSourceMapping(curationID, sourceID);
            }
            curationSource = curationSourceForHash;
        } else {
            curationSource.setHashCode(computedHashCode);
            curationSourceRepository.insert(curationSource);
            long sourceID = curationSource.getId();

            logger.debug("CurationSource created successfully " + curationSource.getName());

            addSourceMapping(curationID, sourceID);
            logger.debug("Successfully added source for curation: " + curationID);

        }
        return curationSource;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CurationSource getSource(final long cuartionID, final long curationSourceID) throws EntityNotFoundException {
        Collection<CurationSourceMapping> items = curationSourceRepository.getSourceMapping(cuartionID);
        CurationSource source = null;
        if (items.size() == 0) {
            throw new EntityNotFoundException("Currently there is no curation source mapping for curation.");
        } else {
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                CurationSourceMapping sourceMapping = (CurationSourceMapping) iterator.next();
                if (sourceMapping.getCurationSource().getId().equals(curationSourceID)) {
                    source = sourceMapping.getCurationSource();
                }
            }

            if (source == null) {
                throw new EntityNotFoundException(
                        "Content Source id: " + curationSourceID + " is not mapped with the curation id: " +
                                cuartionID);
            }
            return source;
        }
    }

    @Override
    public CurationSource updateSource(final CurationSource curationSource) throws EntityExistsException {
        return null;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<CurationSource> getSources(long cuartionID) throws EntityNotFoundException {
        Collection<CurationSource> sources = curationSourceRepository.getSources(cuartionID);
        if (sources.size() == 0) {
            sources = Collections.emptyList();
        }
        return sources;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CurationSource getSource(final Long curationSourceID) throws EntityNotFoundException {
        return curationSourceRepository.get(curationSourceID);
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<Curation> getCurations(final Long cuartionSourceID) throws EntityNotFoundException {
        return curationSourceRepository.getCurations(cuartionSourceID);
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = EntityNotFoundException.class)
    public CurationSourceMapping getSourceMapping(final long curationID, final long cuartionSourceID)
            throws EntityNotFoundException {
        CurationSourceMapping sourceMapping = curationSourceRepository.getSourceMapping(curationID, cuartionSourceID);
        if (sourceMapping == null) {
            throw new EntityNotFoundException("Content Source not found");
        } else {
            return sourceMapping;
        }

    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false,
            rollbackFor = EntityExistsException.class)
    public void addSourceMapping(final long curationID, final long cuartionSourceID) throws EntityExistsException {


        /*CurationSourceMapping sourceMapping = getSourceMapping(curationID, cuartionSourceID);
        if (sourceMapping != null && sourceMapping.getStatus()==1) {
            throw new EntityExistsException(
                    "SourceMapping already exist for courationSourceId :" + cuartionSourceID + " for curationID :" +
                            curationID);
        } else if (sourceMapping.getStatus()== 0) {*/
        curationSourceRepository.addSourceMapping(curationID, cuartionSourceID);
        //}

    }

    @Override
    @Transactional
    public void updateSourceMapping(final CurationSourceMapping curationSourceMapping) {
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false,
            rollbackFor = EntityNotFoundException.class)
    public void removeSourceMapping(final long curationID, final long curationSourceID) throws EntityNotFoundException {
        int status = EntityConstants.Status.DELETED.getId();

        //Trying to delete the source whose status is already DELETED or Source entity is not present
        if (curationSourceRepository.checkStatus(curationID, curationSourceID) == null) {
            throw new EntityNotFoundException("Source not Found");
        }
        curationSourceRepository.removeSourceMapping(curationID, curationSourceID);

    }

    /**
     Computes  the unquie hashCode for a cuartionSource based on its SourceType and its values

     @param curationSource
     @return hashcode
     */

    public static int computeHashCode(CurationSource curationSource) {
        int hashcode = curationSource.getSourceType() * 31;
        String value = "";
        for (String key : curationSource.getProperties().keySet()) {
            value = curationSource.getProperties().getProperty(key).toLowerCase();
            hashcode = hashcode * key.hashCode() * value.hashCode();
        }
        return hashcode;
    }

    public static String computeName(int sourceType) {

        return null;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public CurationSource getSourceToUpdateCache() {
        CurationSource curationSource = curationSourceRepository.getSourceToUpdateCache();
        if (curationSource != null) {
            //Changed to acquireLock for status(0 and 200)
            CurationSource lockedCurationSource = curationSourceRepository
                    .acquireLock(curationSource, "executionStatus", curationSource.getExecutionStatus(),
                            EntityConstants.ExecutionStatus.PROCESSING.getId());

            // acquire lock.
            if (lockedCurationSource == null) {
                logger.info("Unable to lock object from IN_QUEUE TO PROCESSING  objID:=" + curationSource +
                        " as it was locked by " + curationSource.getLockedThread());
                return null;
            }
            return lockedCurationSource;
        }
        return null;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void updateCache(CurationSource curationSource) throws ObjectLockedException, HandlerNotFoundException {

        if (EntityConstants.ExecutionStatus.PROCESSING.getId() != curationSource.getExecutionStatus()) {
            throw new ObjectLockedException(
                    "curationSource with id:=" + curationSource.getId() + " has not beeen locked for processing.");
        }
        curationSource.setExecutionStartTime(new Date());
        curationSourceRepository.update(curationSource);
        curationSourceRepository.refresh(curationSource);

        EntityConstants.ExecutionStatus finalStatus = EntityConstants.ExecutionStatus.DONE;
        // ok now get the strategy for extraction
        try {
            CurationSourceDataHandler handler = curationSourceHandlerStrategy.determineExecutor(curationSource);

            try {
                Collection<CurationCache> cacheItems = handler.getItems(curationSource);
                if (cacheItems != null && !cacheItems.isEmpty()) {
                    for (CurationCache item : cacheItems) {
                        try {
                            curationCacheManager.saveCache(item);
                        } catch (CurationCacheException e) {
                            logger.warn(e.getMessage(), e);
                        } catch (EntityExistsException e) {
                            // ignore
                            logger.debug("skipping insert for item as it already exists sourceId:={} guid:={}",
                                    curationSource.getId(),
                                    item.getGuid());
                        }
                    }
                }
            } catch (ExecutorException e) {
                logger.error(e.getMessage(), e);
                finalStatus = EntityConstants.ExecutionStatus.ERROR_PARSING;
            }
        } catch (HandlerNotFoundException e) {
            logger.error(e.getMessage(), e);
            finalStatus = EntityConstants.ExecutionStatus.ERROR_NO_HANDLER;
        }
        curationSource.setLastUpdatedTime(new Date());
        curationSourceRepository.update(curationSource);
        curationSourceRepository.refresh(curationSource);
        CurationSource lockedCurationSource = curationSourceRepository
                .acquireLock(curationSource, "executionStatus",
                        EntityConstants.ExecutionStatus.PROCESSING.getId(), finalStatus.getId());
        if (lockedCurationSource == null) {
            // wo! someone else finished this?
            logger.error("Unable to finalize updating source with id := " + curationSource.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CurationSource> getSource() {
        return curationSourceRepository.getSource();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Curation> getCurations() {
        return curationSourceRepository.getCurations();
    }

    /**
     IoC reference
     */
    @Autowired
    private CurationSourceHandlerStrategy curationSourceHandlerStrategy;

    /**
     IoC reference
     */

    @Autowired
    private CurationCacheManager curationCacheManager;

    /**
     IoC reference
     */

    @Autowired
    private CurationSourceRepository curationSourceRepository;

    /**
     IoC reference
     */
    public void setCurationSourceHandlerStrategy(final CurationSourceHandlerStrategy curationSourceHandlerStrategy) {
        this.curationSourceHandlerStrategy = curationSourceHandlerStrategy;
    }

    /**
     IoC reference
     */
    public void setCurationSourceRepository(final CurationSourceRepository curationSourceRepository) {
        this.curationSourceRepository = curationSourceRepository;
    }

    /**
     IoC reference
     */
    public void setCurationCacheManager(final CurationCacheManager curationCacheManager) {
        this.curationCacheManager = curationCacheManager;
    }

}

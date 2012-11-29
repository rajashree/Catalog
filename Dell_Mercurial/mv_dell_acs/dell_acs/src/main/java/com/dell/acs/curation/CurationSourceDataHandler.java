package com.dell.acs.curation;

import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationSource;

import java.util.Collection;

/**
 * Base interface that all curation source extractors will have to implement.
 *
 * @author Navin Raj Kumar G.S.
 */
public interface CurationSourceDataHandler {

    /**
     * executes a update for a specific curationSource and ensures the cache is up to date.
     *
     * @param curationSource to be used to update.
     * @return Collection of CurationCache items extracted from the source.
     */
    public Collection<CurationCache> getItems(CurationSource curationSource) throws ExecutorException;

    /**
     * determine if the executor can handle a specific curation source
     *
     * @param curationSource to be used to update.
     * @return true if this executor can handle updates.
     */
    public boolean canHandle(CurationSource curationSource);

}

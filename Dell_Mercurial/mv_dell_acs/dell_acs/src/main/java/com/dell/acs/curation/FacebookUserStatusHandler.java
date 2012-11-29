package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationSource;

import java.util.Collection;

/**
 * Extracts data from a facebook user's statuses and returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class FacebookUserStatusHandler extends AbstractCurationSourceDataHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.FACEBOOK_USERNAME;
    }

    @Override
    public Collection<CurationCache> getItems(final CurationSource curationSource) throws ExecutorException {
        throw new UnsupportedOperationException("not-implemented.");
    }

}

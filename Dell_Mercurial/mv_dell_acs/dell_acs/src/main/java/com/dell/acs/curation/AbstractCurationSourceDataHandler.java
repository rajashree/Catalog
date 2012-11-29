package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for the CurationSourceDataHandler including helper methods and base implementation.
 *
 * @author Navin Raj Kumar G.S.
 */
public abstract class AbstractCurationSourceDataHandler implements CurationSourceDataHandler {

    protected final Logger logger = LoggerFactory.getLogger(AbstractCurationSourceDataHandler.class);

    /**
     * return a Map of properties from the curationSource based on the type.
     *
     * @param curationSource entity to use to search for the properties.
     * @return
     */
    protected Map<String, String> getSourceProperties(final CurationSource curationSource) {
        Map<String, String> properties = new HashMap<String, String>();
        for (String propKey : getHandlerType().getProperties()) {
            properties.put(propKey, curationSource.getProperties().getProperty(propKey));
        }
        Assert.notEmpty(properties);
        return properties;
    }

    /** {@inheritDoc} */
    @Override
    public boolean canHandle(final CurationSource curationSource) {
        Assert.notNull(curationSource);
        return getHandlerType().getId() == curationSource.getSourceType();
    }

    /** @return the Enum constant for this handler. */
    protected abstract EntityConstants.CurationSourceType getHandlerType();

}

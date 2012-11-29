package com.dell.acs.curation;

import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.HandlerNotFoundException;
import com.sourcen.core.ObjectNotFoundException;
import com.sourcen.core.util.Assert;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * strategy class to determine which CurationSourceDataHandler to execute for different types of curation sources.
 * provides a registry as well.
 *
 * @author Navin Raj Kumar G.S.
 */
public class CurationSourceHandlerStrategy implements ApplicationContextAware {

    /** a list of CurationSourceDataHandler to use to determine the correct executor for a source. */
    Map<String, CurationSourceDataHandler> curationSourceDataHandlers;

    /**
     * set the collection of executors to use.
     *
     * @param curationSourceDataHandlers to use.
     */
    public void setCurationSourceDataHandlers(final Map<String, CurationSourceDataHandler> curationSourceDataHandlers) {
        this.curationSourceDataHandlers = curationSourceDataHandlers;
    }

    /**
     * return a CurationSourceDataHandler based on the curationSource or null if none found.
     *
     * @param curationSource to find a CurationSourceDataHandler for.
     * @return CurationSourceDataHandler
     * @throws ObjectNotFoundException if no executors are found.
     */
    public CurationSourceDataHandler determineExecutor(CurationSource curationSource) throws HandlerNotFoundException {
        Assert.notNull(curationSource);
        Assert.notNull(applicationContext);

        for (Map.Entry<String, CurationSourceDataHandler> entry : curationSourceDataHandlers.entrySet()) {
            CurationSourceDataHandler curationSourceDataHandler = entry.getValue();
            if (curationSourceDataHandler.canHandle(curationSource)) {
                // ok return a new instance of this as this could be multi-threaded.
                // this also helps in wrapping any proxies if required.
                return applicationContext.getBean(entry.getKey(), curationSourceDataHandler.getClass());
            }
        }
        throw new HandlerNotFoundException(
                "Unable to find any executors for sourceType:=" + curationSource.getSourceType());

    }

    /** reference to the ApplicationContext. */
    private ApplicationContext applicationContext;

    /** {@inheritDoc} */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * base testcases for all CurationSourceHandlers.
 *
 * @author Navin Raj Kumar G.S.
 */
@RunWith(Parameterized.class)
public abstract class CurationSourceHandlerTest {

    /** logger instance. */
    protected static final Logger logger = LoggerFactory.getLogger(CurationSourceHandlerTest.class);

    /** reference to the curationSource we are testing. */
    protected CurationSource curationSource;

    /**
     * default constructor for this test.
     *
     * @param curationSource to use for execution.
     */
    protected CurationSourceHandlerTest(final CurationSource curationSource) {
        this.curationSource = curationSource;
    }

    /** check if the CurationSourceDataHandler is capable handling the curationSource entity. */
    @Test
    public void testCanHandle() {
        CurationSourceDataHandler handler = getHandler(curationSource);
        Assert.notNull(handler);
        Assert.isTrue(handler.canHandle(curationSource), handler + " is unable to handle " + curationSource);
    }

    /**
     * test if the handler is capable of returning items for the curationSourceItem.
     * More test methods can be written for specific failure methods.
     *
     * @throws ExecutorException
     */
    @Test
    public void testGetItems() throws ExecutorException {
        Collection<CurationCache> items = getHandler(curationSource).getItems(curationSource);
        Assert.notEmpty(items);
        for (CurationCache item : items) {
            Assert.notNull(item.getCurationSource(),
                    "getCurationSource() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getGuid(),
                    "getGuid() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getTitle(),
                    "getTitle() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getLink(),
                    "getLink() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getBody(),
                    "getBody() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getStatus(),
                    "getStatus() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getSource(),
                    "getSource() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getPublishedDate(),
                    "getPublishedDate() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getImportedDate(),
                    "getImportedDate() cannot be null in in item:" + item + " for source:=" + curationSource);
            Assert.notNull(item.getUpdatedDate(),
                    "getUpdatedDate() cannot be null in in item:" + item + " for source:=" + curationSource);
        }
    }

    /**
     * must return an instance of the CurationSourceDataHandler for the concrete test case.
     *
     * @return implementation of CurationSourceDataHandler.
     */
    protected abstract CurationSourceDataHandler getHandler(CurationSource source);

    /**
     * helper method to create a curationSource based on the type and passing the properties.
     *
     * @param type       of the curationSource EntityConstants.CurationSourceType
     * @param properties to apply to the source.this must be in key-value pairs.
     * @return new instance of CurationSource
     */
    protected static CurationSource createDummySource(EntityConstants.CurationSourceType type, Object... properties) {
        CurationSource source = new CurationSource();
        source.setSourceType(type.getId());
        source.setName("dummy-" + type.name().toLowerCase() + "-source");
        if (properties != null && properties.length > 1) {
            if (properties.length % 2 != 0) {
                throw new IllegalArgumentException("properties passed must be in key-value pairs.");
            }

            for (int i = 0; i < properties.length; i += 2) {
                source.getProperties().setProperty(String.valueOf(properties[i]), properties[i + 1]);
            }
        }
        return source;

    }

}

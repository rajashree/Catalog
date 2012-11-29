/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.errors;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2651 $, $Date:: 2012-05-28 08:27:49#$
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class DataExceptionHandlerAdapter implements DataExceptionHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTableDefinitionException(Exception e) {
        onTableDefinitionException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTableDefinitionException(Exception e, boolean bubble) {
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTableDefinitionException(RuntimeException e) {
        onTableDefinitionException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTableDefinitionException(RuntimeException e, boolean bubble) {
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }


    /* Reader Related Method */

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataReaderException(Exception e) {
        onDataReaderException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataReaderException(Exception e, boolean bubble) {
        readerFailedCount++;
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataReaderException(RuntimeException e) {
        onDataReaderException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataReaderException(RuntimeException e, boolean bubble) {
        readerFailedCount++;
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataReaderException(DataReaderException e) {
        onDataReaderException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataReaderException(DataReaderException e, boolean bubble) {
        readerFailedCount++;
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }


    /*Writer Realted Method */

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(Exception e) {
        onDataWriterException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(Exception e, boolean bubble) {
        writerFailedCount++;
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(RuntimeException e) {
        onDataWriterException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(RuntimeException e, boolean bubble) {
        writerFailedCount++;
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(DataWriterException e) {
        onDataWriterException(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(DataWriterException e, boolean bubble) {
        writerFailedCount++;
        if (bubble) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } else {
            logger.error(e.getMessage());
        }
    }


    public void setLogger(final Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    protected Integer readerFailedCount = 0;
    protected Integer writerFailedCount = 0;

    public Integer getReaderFailedCount() {
        return readerFailedCount;
    }

    public Integer getWriterFailedCount() {
        return writerFailedCount;
    }
}

/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 10:26:09#$
 */

/**
 * DuplicateKeyException is the custom Excpetion class
 * and throws when duplicates keys are find while insertion
 */
public class DuplicateKeyException extends DataImportException {

    /**
     * DuplicateKeyException constructors
     */
    public DuplicateKeyException() {
    }

    public DuplicateKeyException(String message) {
        super(message);
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateKeyException(Throwable cause) {
        super(cause);
    }
}

package com.dell.dw;

import com.sourcen.core.SourcenException;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/17/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateKeyException extends SourcenException{
    public DuplicateKeyException() {
        super();
    }

    public DuplicateKeyException(String msg) {
        super(msg);
    }

    public DuplicateKeyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public DuplicateKeyException(Throwable throwable) {
        super(throwable);
    }
}

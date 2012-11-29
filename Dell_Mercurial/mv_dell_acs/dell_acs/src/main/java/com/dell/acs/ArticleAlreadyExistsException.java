package com.dell.acs;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@Deprecated
public class ArticleAlreadyExistsException extends ACSException {

    public ArticleAlreadyExistsException() {
        super();
    }

    public ArticleAlreadyExistsException(String msg) {
        super(msg);
    }

    public ArticleAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ArticleAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}

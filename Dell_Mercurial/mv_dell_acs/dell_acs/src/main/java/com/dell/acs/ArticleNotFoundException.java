package com.dell.acs;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 * @version $Revision: 3707 $, $Date:: 2012-07-13 3:26 PM#$
 */
@Deprecated
public class ArticleNotFoundException extends ACSException {
    public ArticleNotFoundException() {
        super();
    }

    public ArticleNotFoundException(String msg) {
        super(msg);
    }

    public ArticleNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ArticleNotFoundException(Throwable throwable) {
        super(throwable);
    }
}

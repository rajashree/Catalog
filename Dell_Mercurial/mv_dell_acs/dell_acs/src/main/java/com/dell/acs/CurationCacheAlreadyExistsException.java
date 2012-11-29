package com.dell.acs;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$
 */
public class CurationCacheAlreadyExistsException extends ACSException {

    public CurationCacheAlreadyExistsException() {
        super();
    }

    public CurationCacheAlreadyExistsException(String msg) {
        super(msg);
    }

    public CurationCacheAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurationCacheAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}

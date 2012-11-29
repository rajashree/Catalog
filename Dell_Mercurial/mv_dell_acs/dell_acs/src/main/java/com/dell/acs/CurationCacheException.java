package com.dell.acs;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$
 */
public class CurationCacheException extends RuntimeException {

    public CurationCacheException() {
    }

    public CurationCacheException(String message) {
        super(message);
    }

    public CurationCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurationCacheException(Throwable cause) {
        super(cause);
    }
}

package com.dell.acs;

import com.sourcen.core.ObjectNotFoundException;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$
 */
public class CurationCacheNotFoundException extends ObjectNotFoundException {


    public CurationCacheNotFoundException() {
        super();
    }

    public CurationCacheNotFoundException(String msg) {
        super(msg);
    }

    public CurationCacheNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurationCacheNotFoundException(Throwable throwable) {
        super(throwable);
    }

}

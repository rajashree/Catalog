package com.dell.acs;

import com.sourcen.core.ObjectNotFoundException;
/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$
 */
public class CurationContentNotFoundException extends ObjectNotFoundException {


    public CurationContentNotFoundException() {
        super();
    }

    public CurationContentNotFoundException(String msg) {
        super(msg);
    }

    public CurationContentNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurationContentNotFoundException(Throwable throwable) {
        super(throwable);
    }

}

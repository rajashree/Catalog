package com.dell.acs;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$
 */

public class CurationContentAlreadyExistsException extends ACSException {


    public CurationContentAlreadyExistsException() {
        super();
    }

    public CurationContentAlreadyExistsException(String msg) {
        super(msg);
    }

    public CurationContentAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurationContentAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}
package com.dell.acs;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$
 */
public class CurationContentException extends RuntimeException {

    public CurationContentException() {
        super();
    }

    public CurationContentException(String message) {
        super(message);
    }

    public CurationContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurationContentException(Throwable cause) {
        super(cause);
    }
}

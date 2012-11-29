package com.dell.acs;

/**
 * User: vivek
 * Date: 6/18/12
 * Time: 12:57 PM
 * Custom Exception class for all the PixelTracking related exceptions
 */
public class PixelTrackingException extends ACSException {

    public PixelTrackingException() {
        super();
    }

    public PixelTrackingException(String message) {
        super(message);
    }

    public PixelTrackingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public PixelTrackingException(Throwable throwable) {
        super(throwable);
    }

}

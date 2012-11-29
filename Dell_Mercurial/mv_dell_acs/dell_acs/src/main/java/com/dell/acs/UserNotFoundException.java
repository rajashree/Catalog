package com.dell.acs;

/**
 * The class <code>UserException</code> is a subclass of <code>ACSException</code> are a form of
 * <code>Throwable</code>.
 *
 * This exception is used when the User object isn't found in the content server
 * @author : Vivek Kondur
 * @version : 1.0
 */
public class UserNotFoundException extends ACSException {


    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(Throwable throwable){
        super(throwable);
    }

    public UserNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }

}

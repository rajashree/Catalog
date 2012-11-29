package com.dell.acs;

/**
 * The class <code>UserAlreadyExitsException</code> is a subclass of <code>ACSException</code> are a form of
 * <code>Throwable</code>.
 *
 * This exception is used when there is an existing User in the content server
 * User: Raghavendra
 */
public class UserAlreadyExitsException extends ACSException {

    public UserAlreadyExitsException()
    {
        super();
    }

    public UserAlreadyExitsException(String msg)
    {
        super(msg);
    }
    public UserAlreadyExitsException(String msg, Throwable throwable)
    {
        super(msg,throwable);
    }
    public UserAlreadyExitsException(Throwable throwable)
    {
        super(throwable);
    }



}

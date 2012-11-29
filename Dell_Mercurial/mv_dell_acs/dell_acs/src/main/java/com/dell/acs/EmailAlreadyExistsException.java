package com.dell.acs;

/**
 * The class <code>EmailAlreadyExistsException</code> is a subclass of <code>ACSException</code> are a form of
 * <code>Throwable</code>.
 *
 * This exception is used when there is an existing User with smae <code>emailAddress</code>in the content server
 * User: Raghavendra
 */
public class EmailAlreadyExistsException extends ACSException {

    public EmailAlreadyExistsException()
    {
        super();
    }
    public EmailAlreadyExistsException(String msg)
    {
        super(msg);
    }
    public EmailAlreadyExistsException(String msg,Throwable throwable)
    {
        super(msg,throwable);
    }
    public EmailAlreadyExistsException(Throwable throwable)
    {
        super(throwable);
    }
}

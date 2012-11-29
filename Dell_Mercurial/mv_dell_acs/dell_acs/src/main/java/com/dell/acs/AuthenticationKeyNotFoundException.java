package com.dell.acs;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: 7/2/12
 * Time: 1:05 PM
 *
 */
public class AuthenticationKeyNotFoundException extends ACSException {

    public AuthenticationKeyNotFoundException() {
        super();
    }

    public AuthenticationKeyNotFoundException(String message){
        super(message);
    }

    public AuthenticationKeyNotFoundException(String message, Throwable throwable){
        super(message,throwable);
    }

    public AuthenticationKeyNotFoundException(Throwable throwable){
        super(throwable);
    }

}

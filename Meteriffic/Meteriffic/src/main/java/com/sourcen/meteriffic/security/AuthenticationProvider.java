package com.sourcen.meteriffic.security;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;

public interface AuthenticationProvider {

	//To use it in action classes.
	
	/**
     * Accesses the current authentication associated with the scope of the current
     * operation.
     * @return Implementation of Acegi's Authentication interface.
     */
    Authentication getAuthentication();
    
    /**
     * Returns the raw Acegi security context for the given scope. This should
     * generally be avoided and is only needed for changing the authentication
     * of the current scope - something that acegi should manage for us.
     * 
     * @return The SecurityContext for the current request.
     */
    SecurityContext getSecurityContext();
}

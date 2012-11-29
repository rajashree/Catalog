package com.sourcen.microsite.security;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;

import com.sourcen.microsite.model.User;



public interface AuthenticationProvider {

	
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

    /**
     * Returns the jive user associated with the current call context.
     * The original Jive User is also available via this object.
     *
     * @return
     */
    User getJiveUser();

}

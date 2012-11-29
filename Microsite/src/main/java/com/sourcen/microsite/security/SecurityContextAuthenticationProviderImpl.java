package com.sourcen.microsite.security;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.log4j.Logger;

import com.sourcen.microsite.model.User;




public class SecurityContextAuthenticationProviderImpl implements AuthenticationProvider {

	  private static final Logger log = Logger.getLogger(SecurityContextAuthenticationProviderImpl.class);

	public JiveAuthentication getAuthentication() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (null == auth) {
            log.debug("No authentication associated with current context. Defaulting to anonymous");
           // return new AnonymousAuthentication();
        }

        //this should universally be true if the interceptor stack is configured properly
        return (JiveAuthentication) auth;
    }

    public SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

  
    /**
     * Returns the JiveUser associated with this request.
     *
     * @return the JiveUser associated with this request.
     */
    public User getJiveUser() {
    	 User user = getAuthentication().getUser();
        return user;
    }

	

	
}

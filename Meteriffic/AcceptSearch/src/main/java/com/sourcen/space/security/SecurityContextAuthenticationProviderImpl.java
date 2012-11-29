package com.sourcen.space.security;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

public class SecurityContextAuthenticationProviderImpl implements AuthenticationProvider {

	//To use it in action classes.
    public Authentication getAuthentication() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (null == auth) {
               //  return new AnonymousAuthentication();
        }
      
        return  auth;
    }

    public SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
  }

package com.sourcen.microsite.security;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;

public class AnonymousAuthentication implements Authentication {

	public GrantedAuthority[] getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}

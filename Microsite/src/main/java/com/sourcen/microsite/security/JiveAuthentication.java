package com.sourcen.microsite.security;

import org.acegisecurity.Authentication;

import com.sourcen.microsite.model.User;

public interface JiveAuthentication extends Authentication{

	  public boolean isAnonymous();

	    public long getUserID();

	    public User getUser();
}

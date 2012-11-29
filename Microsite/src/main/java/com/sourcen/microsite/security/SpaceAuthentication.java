package com.sourcen.microsite.security;
import org.acegisecurity.Authentication;

import com.sourcen.microsite.model.User;

public interface SpaceAuthentication extends Authentication{
	
	    public boolean isAnonymous();

	    public long getUserID();

	    public User getUser();
	}
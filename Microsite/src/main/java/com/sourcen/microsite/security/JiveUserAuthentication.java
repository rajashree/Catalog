package com.sourcen.microsite.security;

import org.acegisecurity.GrantedAuthority;

import com.sourcen.microsite.model.User;

public class JiveUserAuthentication implements JiveAuthentication {

	private User snUser;
	private GrantedAuthority[] grantedAuthorities;

	public JiveUserAuthentication(User user, GrantedAuthority[] grantedAuthorities) {
		if (user == null) {
			snUser = new User();
		} else {
			snUser = user;
		}
		this.grantedAuthorities=grantedAuthorities;
	}

	public User getUser() {
		
		return snUser;
	}

	public long getUserID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isAnonymous() {
		// TODO Auto-generated method stub
		return false;
	}

	public GrantedAuthority[] getAuthorities() {
		return  grantedAuthorities;
	}

	public Object getCredentials() {
		return null;
	}

	public Object getDetails() {
		return snUser;
	}

	public Object getPrincipal() {
		return snUser;
	}

	public boolean isAuthenticated() {
		return true;
	}

	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	public String getName() {
		// TODO Auto-generated method stub
		return snUser.getUsername();
	}

	public String toString() {
		if (null == snUser) {
			return "[NULL]";
		} else {
			return "[" + snUser.getId() + ":" + snUser.getUsername() + "] "
					+ snUser.getUsername();
		}
	}

}

package com.sourcen.space.security;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;



import com.sourcen.space.model.Role;
import com.sourcen.space.model.User;

public class SpaceUserDetail implements UserDetails {

	//UserDetail Bean
	private static final long serialVersionUID = 1L;
	private User user = null;
    private List<Role> roles =null;
	public SpaceUserDetail(User user) {
		this.user = user;

	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;

	}

	public GrantedAuthority[] getAuthorities() {
	 GrantedAuthority[] arrayAuths =null;
       if(roles== null){
    	    arrayAuths = new GrantedAuthority[1];
    	    arrayAuths[0] = new GrantedAuthorityImpl("ROLE_USER");
    	    return arrayAuths;
       }
       arrayAuths = new GrantedAuthority[roles.size()];
       Iterator<Role> it= roles.iterator();
      for( int i=0;i<roles.size();i++){
    	  Role role=(Role)it.next();
    	  arrayAuths[i] = new GrantedAuthorityImpl(role.getRole());
      }
      		
		return arrayAuths;

	
	}

	public String getPassword() {
		return user.getPassword();
	}

	public String getUsername() {

		return user.getUsername();
	}

	public boolean isAccountNonExpired() {
		return user.isEnabled();
	}

	public boolean isAccountNonLocked() {
		return user.isEnabled();
	}

	public boolean isCredentialsNonExpired() {

		return user.isEnabled();
	}

	public boolean isEnabled() {
		return user.isEnabled();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

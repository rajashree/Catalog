package com.sourcen.microsite.security;


import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.sourcen.microsite.model.User;
import com.sourcen.microsite.service.UserManager;


public class SpaceUserDetailsService implements UserDetailsService {

	private UserManager userManager;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		try {
	           User jiveUser = userManager.getUser(username);
	              
	           SpaceUserDetail userDetails= new SpaceUserDetail(jiveUser);
	          

	           
	           userDetails.setRoles(	 userManager.getRoles(username));
	           return userDetails;
	        }
	        catch(UsernameNotFoundException unfe) {
	         
	        	System.out.println(unfe.getMessage());
	        	throw unfe;
	        }
	        catch(Exception unf) {
	        	 unf.printStackTrace();
	        	final String message = "User manager failed while loading user for name '" + username + "'.";
	            System.out.println(message+unf.getMessage());  
	            throw new UsernameNotFoundException(message, unf);
	        }
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}

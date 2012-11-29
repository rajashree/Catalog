package com.sourcen.meteriffic.security;

import org.acegisecurity.Authentication;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationInterceptor implements Interceptor{
	private AuthenticationProvider authProvider;

	public AuthenticationProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		Authentication auth = null;
		try{
			auth = authProvider.getAuthentication();
		}catch(Exception ex){
			final String message = "Failed to authenticate. Unable to check for authorization.";
			ex.printStackTrace();
			throw new SecurityException(message);
		}
		boolean isAuthenticated = (null!=auth) && auth.isAuthenticated();
		
		if(!isAuthenticated){
			return Action.LOGIN;
		}else{
			return actionInvocation.invoke();
		}
	}
	
}
/*public class AuthenticationInterceptor implements Interceptor {

	

	//To Check if the current user is authenticated, else redirect him to the login page.
	public String intercept(ActionInvocation actionInvocation) throws Exception {

	           	       
	        boolean isAuthenticated = (null!=auth) && auth.isAuthenticated();

	        if (!isAuthenticated) {
	        	  	return Action.LOGIN;            
	        }else {
	            return actionInvocation.invoke();
	        }

	    }

		public AuthenticationProvider getAuthProvider() {
			return authProvider;
		}

		public void setAuthProvider(AuthenticationProvider authProvider) {
			this.authProvider = authProvider;
		}
		
}
*/
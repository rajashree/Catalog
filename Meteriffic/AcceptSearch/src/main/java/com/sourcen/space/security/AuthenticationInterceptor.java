package com.sourcen.space.security;



import org.acegisecurity.Authentication;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;


public class AuthenticationInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;
	private AuthenticationProvider authProvider;
	
	public void destroy () {}

	public void init() {}

	//To Check if the current user is authenticated, else redirect him to the login page.
	public String intercept(ActionInvocation actionInvocation) throws Exception {

	     	  Authentication auth = null;
	          try {
	              auth = authProvider.getAuthentication();
	          }
	          catch (Exception ex) {
	              final String message = "Failed to extract  authentication. Unable to check authoirzation.";
	              ex.printStackTrace(); 
	              throw new SecurityException(message);
	          }
	          
	       	       
	        boolean isAuthenticated = (null!=auth) && auth.isAuthenticated();

	        if (!isAuthenticated) {
	        	  	return Action.LOGIN;            
	        }
	        else {
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

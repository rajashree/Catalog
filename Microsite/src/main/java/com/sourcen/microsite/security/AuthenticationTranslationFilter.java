package com.sourcen.microsite.security;

import java.io.IOException;
import java.util.Date;

import javassist.NotFoundException;

import javax.servlet.*;


import org.acegisecurity.Authentication;
import org.acegisecurity.InsufficientAuthenticationException;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.sourcen.microsite.model.User;
import com.sourcen.microsite.service.UserManager;

public class AuthenticationTranslationFilter implements Filter,  ApplicationEventPublisherAware{

    private static final Logger log = Logger.getLogger(AuthenticationTranslationFilter.class);
	protected UserManager userManager = null;

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	 public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
     throws IOException, ServletException{
		
	        //extract the existing authentication
	        final SecurityContext context = SecurityContextHolder.getContext();

	        if (null == context) {
	            throw new ServletException("Invalid filter configuration: Must be located after security context "
	                    + "has been established.");
	        }

	        Authentication auth = context.getAuthentication();

	      

	         JiveAuthentication jiveAuth = null;
	        if (auth != null && !(auth instanceof JiveAuthentication)) {
	            //try to load the user from the existing authentication
	            User user = extractJiveUser(auth);

	            if (null == user) {
	                log.warn("No jive user found in authentication.");
	                throw new InsufficientAuthenticationException("Jive User not located in authentication.");
	            }

	            //replace the existing authentication with a jive authentication and continue down the filter
	            jiveAuth = new JiveUserAuthentication(user,auth.getAuthorities());
	            
	            //set the current authentication to the jive auth for the life of the user's session
	            context.setAuthentication(jiveAuth);
	        }
	        else {
	        	jiveAuth = (JiveAuthentication) auth;
	        }

	        
	        
	      	     
	        try {
	            filterChain.doFilter(servletRequest, servletResponse);
	        }
	        catch (ServletException se) {
	            throw se;
	        }
	        catch (IOException ioe) {
	            throw ioe;
	        }
	        finally {

	            final Authentication postRequestAuth = context.getAuthentication();

	            
	        }
	    
		
	}

	 protected User extractJiveUser(Authentication auth) {
	        User user = resolveUser(auth.getPrincipal());

	        if (null != user) {
	        	//auth.getAuthorities().
	        	
	        	user.setAdmin(true);
	        	return user;
	        }

	        //if we get here, we couldn't convert the details, try and convert the
	        //principal
	        Object principal = auth.getPrincipal();
	        return resolveUser(principal);
	    }
	 protected User resolveUser(Object candidate) {
	        if (null == candidate) {
	            return null;
	        }

	        User user = null;
	        if (candidate instanceof SpaceUserDetail) {
	        	SpaceUserDetail jud = (SpaceUserDetail) candidate;
	        	 
	            user = jud.getUser();
	            
	        }
	        if (candidate instanceof String) {
	            try {
	                user = userManager.getUser((String) candidate);
	            }
	            catch (NotFoundException unfe) {
	                //no-op
	            }
	        }
	        if (candidate instanceof User) {
	            user = (User) candidate;
	        }

	        //ensure LastLoggedIn date is > 0
	        return setLastLoggedInDate(user);

	    }
	 protected User setLastLoggedInDate(User user) {
	        if (user == null) {
	            return user;
	        }
	        if (user.getLastLoggedIn() == null || user.getLastLoggedIn().equals(new Date(0))) {
	            
	            user.setLastLoggedIn(new Date());
	            return user;
	        }
	        else {
	            return user;
	        }
	    }
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}

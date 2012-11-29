package com.sourcen.space.action;

import javax.servlet.http.Cookie;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class LoginAction extends SpaceActionSupport {

	private static final long serialVersionUID = 1L;
	protected boolean authnFailed = false;

	public String execute() {

		if (authnFailed) {
				addFieldError("login.error", getText("error.login"));			
			return INPUT;
		}
		if(authProvider.getAuthentication() != null && authProvider.getAuthentication().isAuthenticated())
			return SUCCESS;
		
		String targetURL ="http://demo.serendio.com/usvoomsrv/login?category%5Fid=10&password=915e7ea8a469faa99910adde6a53942a&login=&user%5Fname=e4e";
		
		String response = null;
		
		GetMethod userPost = new GetMethod(targetURL);
		try {
			HttpClient client = this.applicationManager.getClient();
			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				
				org.apache.commons.httpclient.Cookie[] cookies = client
				.getState().getCookies();
				for (int i = 0; i < cookies.length; i++) {
					System.out.println("Name = Value ::  "
							+ cookies[i].getName() + " = "
							+ cookies[i].getValue());
					Cookie cookie = new Cookie(cookies[i].getName(),cookies[i].getValue());
					cookie.setDomain(".serendio.com");
					cookie.setPath("/");
					this.response.addCookie(cookie);
				}
			} else {
				System.out.println("SOME ERROR");
			}
			
		}catch(ConnectTimeoutException timeout){
			System.err.println("connection time out==> get new connection");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		
		return INPUT;
	}

	public boolean isAuthnFailed() {
		
		return authnFailed;
	}

	public void setAuthnFailed(boolean authnFailed) {
		this.authnFailed = authnFailed;
	}

}

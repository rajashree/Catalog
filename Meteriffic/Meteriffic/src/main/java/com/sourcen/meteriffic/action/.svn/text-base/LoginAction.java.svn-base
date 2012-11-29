package com.sourcen.meteriffic.action;

import javax.servlet.http.Cookie;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.sourcen.meteriffic.action.SpaceActionSupport;

public class LoginAction extends SpaceActionSupport{
	
	protected boolean authnFailed=false;
	
	public String execute(){
		if(authnFailed){
			addFieldError("login.error",getText("error.login"));
			return INPUT;
		}
		if(authProvider.getAuthentication() != null && authProvider.getAuthentication().isAuthenticated())
		{
			String targetURL ="http://demo.serendio.com/usvoomsrv/login?password=ad0234829205b9033196ba818f7a872b&category%5Fid=14&login=&user%5Fname=test2";
			String response = null;
			GetMethod userPost = new GetMethod(targetURL);
			try {
				HttpClient client = this.applicationManager.getClient();
				int status = client.executeMethod(userPost);
				if (status == HttpStatus.SC_OK) {
					org.apache.commons.httpclient.Cookie[] cookies = client.getState().getCookies();
					for (int i = 0; i < cookies.length; i++) {
						System.out.println("Name = Value ::  "+ cookies[i].getName() + " = "+cookies[i].getValue());
						Cookie cookie = new Cookie(cookies[i].getName(),cookies[i].getValue());
						cookie.setDomain(".serendio.com");
						cookie.setPath("/");
						this.response.addCookie(cookie);
					}
				} else {
					return ERROR;
				}
			}catch(ConnectTimeoutException timeout){
				return ERROR;
			} catch (Exception ex) {
				return ERROR;
			}
			return SUCCESS;
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

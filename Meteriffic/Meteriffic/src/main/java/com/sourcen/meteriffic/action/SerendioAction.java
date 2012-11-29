package com.sourcen.meteriffic.action;

import javassist.NotFoundException;

import javax.servlet.http.Cookie;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class SerendioAction extends SpaceActionSupport{
	public String execute(){
		tabIndex=2;
		
		
		/*String targetURL = this.getApplicationManager().getServerUrl()+"/login?password=318775fb44e6f2e6388b719352e93ee8&category%5Fid="+this.applicationManager.getCategoryId()+"&login=1&user%5Fname=sourcen";
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
		}*/
		return SUCCESS;
	}
}

package com.sourcen.space.action;

public class MailSettingsAction extends SpaceActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int port;
	
	private String user;
	private String password;
	private String host;
	public String execute() {
		tabIndex=4;
	  return SUCCESS;
	}
	
	public String input() {
		tabIndex=4;
		return INPUT;

	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public void validate() {
		
	}
	

}

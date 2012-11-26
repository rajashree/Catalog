package com.snipl.ice.security;


/**
* @Author Kamalakar Challa
*   
*/

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class LoginForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String id_username;
	String id_pass;
	boolean rememberMe;
	
	public void setId_username(String id_username) {
		this.id_username = id_username;
	}

	public String getId_username() {
		return id_username;
	}

	public void setId_pass(String id_pass) {
		this.id_pass = id_pass;
	}

	public String getId_pass() {
		return id_pass;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public boolean getRememberMe() {
		return rememberMe;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.id_username = "";
		this.id_pass = "";
		this.rememberMe=false;
	}

}

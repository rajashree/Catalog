package com.snipl.ice.security;


/**
* @Author Chethan jayaraj 
*   
*/
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ForgotPasswordForm extends ActionForm {
	private static final long serialVersionUID = 1L;

	private String emailid;

	public void setEmailid(String id) {
		this.emailid = id;
	}

	public String getEmailid() {
		return this.emailid;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.emailid = null;
	}
}

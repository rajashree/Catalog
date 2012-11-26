package com.snipl.ice.settings;

/**
* @Author Kamalakar Challa
*   
*/


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class PasswordManagerForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id_cur_pass;
	String id_new_pass;
	public void setId_cur_pass(String id_cur_pass) {
		this.id_cur_pass = id_cur_pass;
	}
	public String getId_cur_pass() {
		return id_cur_pass;
	}
	public void setId_new_pass(String id_new_pass) {
		this.id_new_pass = id_new_pass;
	}
	public String getId_new_pass() {
		return id_new_pass;
	}
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
			this.id_cur_pass = "";
			this.id_new_pass="";
	}

}

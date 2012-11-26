package com.snipl.ice.registration;

/**
* @Author Sankara Rao & Kamalakar Challa
*   
*/

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class Register_ConformForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id_code=null;
	public void setId_code(String id_code) {
		this.id_code = id_code;
	}
	public String getId_code() {
		return id_code;
	}
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
			this.id_code = "";
	}
}

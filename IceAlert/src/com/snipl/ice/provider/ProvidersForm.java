package com.snipl.ice.provider;


/**
* @Author Sankara Rao
*   
*/

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ProvidersForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String country;

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.country = "";
	}
}

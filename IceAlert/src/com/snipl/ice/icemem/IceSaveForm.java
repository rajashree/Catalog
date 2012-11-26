package com.snipl.ice.icemem;


/**
* @Author Sankara Rao
*   
*/

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class IceSaveForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id_ice_name=null;
	private String m_Number=null;
	private String div_combo6=null;
	private String div_provider=null;
	private String email=null;
	private String ice_email=null;
	private String ice_no=null;
	public void setId_ice_name(String id_ice_name) {
		this.id_ice_name = id_ice_name;
	}
	public String getId_ice_name() {
		return id_ice_name;
	}
	public void setM_Number(String m_Number) {
		this.m_Number = m_Number;
	}
	public String getM_Number() {
		return m_Number;
	}
	public void setIce_email(String ice_email) {
		this.ice_email = ice_email;
	}
	public String getIce_email() {
		return ice_email;
	}
	
	public void setIce_no(String ice_no) {
		this.ice_no = ice_no;
	}
	public String getIce_no() {
		return ice_no;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	
	public void setDiv_combo6(String div_combo6) {
		this.div_combo6 = div_combo6;
	}
	public String getDiv_combo6() {
		return div_combo6;
	}
	public void setDiv_provider(String div_provider) {
		this.div_provider = div_provider;
	}
	public String getDiv_provider() {
		return div_provider;
	}
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
			this.id_ice_name = "";
			this.m_Number = "";
			this.div_combo6 = "";
			this.div_provider="";
			this.email="";
			this.ice_email="";
			this.ice_no="";
	}
}

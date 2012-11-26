package com.snipl.ice.community;

/**
* @Author Kamalakar Challa 
*   
*/
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class InviteCommunityForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String email_data = null;
	String comid = null;
	String commname = null;
	String id_inv_msg = null;
	String id_email = null;
	
	public void setEmail_data(String email_data) {
		this.email_data = email_data;
	}	
	public String getEmail_data() {
		return email_data;
	}
	
	public void setComid(String comid) {
		this.comid = comid;
	}	
	public String getComid() {
		return comid;
	}
	
	public void setCommname(String commname) {
		this.commname = commname;
	}	
	public String getCommname() {
		return commname;
	}
	
	public void setId_inv_msg(String id_inv_msg) {
		this.id_inv_msg = id_inv_msg;
	}	
	public String getId_inv_msg() {
		return id_inv_msg;
	}
	
	public void setId_email(String id_email) {
		this.id_email = id_email;
	}	
	public String getId_email() {
		return id_email;
	}
	
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
			this.email_data = "";
			this.comid="";
			this.commname="";
			this.id_email="";
			this.id_inv_msg="";
	}

}

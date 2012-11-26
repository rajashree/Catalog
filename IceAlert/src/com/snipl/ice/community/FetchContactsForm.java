package com.snipl.ice.community;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class FetchContactsForm extends ActionForm {
	String id_uname=null;
	String mail_providers=null;
	String id_passwd=null;
	String prev_contact=null;
	
	public void setId_uname(String id_uname) {
		this.id_uname = id_uname;
	}
	public String getId_uname() {
		return id_uname;
	}
	
	public void setMail_providers(String mail_providers) {
		this.mail_providers = mail_providers;
	}
	public String getMail_providers() {
		return mail_providers;
	}
	
	public void setId_passwd(String id_passwd) {
		this.id_passwd = id_passwd;
	}
	public String getId_passwd() {
		return id_passwd;
	}
	
	public void setPrev_contact(String prev_contact) {
		this.prev_contact = prev_contact;
	}
	public String getPrev_contact() {
		return prev_contact;
	}
	
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
		this.id_uname="";
		this.mail_providers="";
		this.id_passwd="";
		this.prev_contact="";
	}
}

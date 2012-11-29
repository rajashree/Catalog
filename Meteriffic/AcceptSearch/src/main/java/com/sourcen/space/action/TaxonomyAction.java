package com.sourcen.space.action;

import java.io.File;

import com.opensymphony.xwork2.Preparable;
import com.sourcen.space.service.SerendioManager;

public class TaxonomyAction extends SpaceActionSupport implements Preparable{

	/**
	 * 
	 */

	private File upload;

	private static final long serialVersionUID = 1L;
	private SerendioManager serendioManager=null;
	private boolean product=true;
	
	public boolean isProduct() {
		return product;
	}
	public void setProduct(boolean product) {
		this.product = product;
	}
	public SerendioManager getSerendioManager() {
		return serendioManager;
	}
	public void setSerendioManager(SerendioManager serendioManager) {
		this.serendioManager = serendioManager;
	}
  public String input(){
				
		return INPUT;
	}
	
	public String execute(){
		
		if(product)
			serendioManager.saveProductXML(upload);
		else
			serendioManager.saveFeatureXML(upload);
		if(product)
		 this.addActionMessage(getText("product.taxonomy.udpate.success"));
		else
			 this.addActionMessage(getText("feature.taxonomy.udpate.success"));
		
		return SUCCESS;
	}
	
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	
	public void prepare() throws Exception {
		tabIndex=4;
		
	}
	public void validate() {
	
		
		//addFieldError("username", getText("error.username.required"));
		/*if (getContentType() == null
				|| getContentType().e) {
			addFieldError("username", getText("error.username.required"));

		}*/
		/*if (getEmail() == null
				|| getEmail().length() < registrationManager
						.getDefaultMinUsernameLength()) {
			addFieldError("email", getText("error.email.required"));

		}
		if (getLastname() == null
				|| getLastname().length() < registrationManager
						.getDefaultMinUsernameLength()) {
			addFieldError("lastname", getText("error.username.required"));

		}
		if (getPassword() == null
				|| getPassword().length() < registrationManager
						.getDefaultMinUsernameLength()) {
			addFieldError("password", getText("error.password.required"));

		}
		if (!getPassword().equals(getConformPassword())) {
			 addFieldError("password", getText("error.password.match"));

		}
		String tempKeycode = null;

		if (registrationManager.isHumanValidationEnabled()
				&& (this.getSession()
						.containsKey(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY))) {
			tempKeycode = (String) this.getSession().get(
					nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY);
			if (tempKeycode == null || !tempKeycode.equals(keycode))
				addFieldError("keycode", getText("error.human.validation"));
		}

		if (!isAgree()) {
			addFieldError("isAgree", getText("error.aggrement.required"));

		}*/
		super.validate();
	}


}

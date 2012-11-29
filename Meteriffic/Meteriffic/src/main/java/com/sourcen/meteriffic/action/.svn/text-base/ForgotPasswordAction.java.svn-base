package com.sourcen.meteriffic.action;

import javassist.NotFoundException;

import com.sourcen.meteriffic.model.EmailMessage;
import com.sourcen.meteriffic.model.Property;
import com.sourcen.meteriffic.model.User;
import com.sourcen.meteriffic.model.EmailMessage.EmailAddress;
import com.sourcen.meteriffic.service.EmailManager;
import com.sourcen.meteriffic.service.RegistrationManager;

public class ForgotPasswordAction extends SpaceActionSupport{

	private String username;
	private String keycode;
	private RegistrationManager registrationManager = null;
	private EmailManager emailManager = null;
	
	
	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}
	public EmailManager getEmailManager() {
		return emailManager;
	}
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}
	
	public String input(){
		if(!registrationManager.isPasswordResetEnabled())
			return "unavailable";
		return INPUT;
	}
	
	public String execute(){
		if(!registrationManager.isPasswordResetEnabled())
			return "unavailable";
		try{
			User user = this.userManager.getUser(username);
			EmailMessage msg = new EmailMessage();
			msg.addRecipient(new EmailAddress(user.getUserName(), user.getEmail()));
			msg.setSubject("Password Reset");
			String token=null;
			try{
				token = this.applicationManager.getProperty(username).getValue();
			}catch(NotFoundException e){
				token = applicationManager.getStringToken();				
			}
			this.getApplicationManager().saveProperty(new Property(username,token));
			msg.setHtmlBody("Hi "+user.getUserName()+",<br/><br/>Please <a href=\""+getBaseUrl()+"/reset.password!input.htm?username="
					+ username +"&keycode="+token+"\"\">click here</a> to reset you password.<br/><br/>If you need any further assistance, please email us at "+this.applicationManager.getFeedbackMailFromAddress()+".<br/><br/>Sincerely,<br/>The Meteriffic Product Team");
			msg.setSender(new EmailAddress("Support",this.applicationManager.getFeedbackMailFromAddress()));
			emailManager.send(msg);
			
		}catch(NotFoundException e){
			addFieldError("username",getText("error.username.not.found"));
			return INPUT;
			
		}
		this.addActionMessage(getText("forgot.password.success"));
		return SUCCESS;
	}

	public void validate() {

		if (getUsername() == null) {
			addFieldError("username", getText("error.username.required"));
		}
		/*String tempKeycode=null;
		if (registrationManager.isHumanValidationEnabled() &&(this.getSession().containsKey(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY)))	{ 
			tempKeycode = (String) this.getSession().get(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY);
			if(tempKeycode == null  || !tempKeycode.equals(keycode))				
				addFieldError("keycode",getText("error.human.validation"));
		}	
		*/
		super.validate();
	}

}

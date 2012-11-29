package com.sourcen.meteriffic.action;

import javassist.NotFoundException;

import com.sourcen.meteriffic.model.EmailMessage;
import com.sourcen.meteriffic.model.Property;
import com.sourcen.meteriffic.model.User;
import com.sourcen.meteriffic.model.EmailMessage.EmailAddress;
import com.sourcen.meteriffic.service.RegistrationManager;
import com.sourcen.meteriffic.service.UserAlreadyExistsException;

public class SignUpAction extends SpaceActionSupport{

	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private String confirmPassword;
	private String email;
	private boolean agree;
	private String keycode;
	
	
	public String input() {
		if (!getRegistrationManager().isEnabled())
			return "unavailable";
		return INPUT;
	}
	
	public String execute(){
		if(hasErrors()){
			return INPUT;
		}
		if(getRegistrationManager().isEnabled())
			try{
				User user = new User();
				user.setUserName(username);
				user.setPassword(password);
				user.setEmail(email);
				user.setFirstName(firstname);
				user.setLastName(lastname);
				user.setCreated(this.getApplicationManager().getApplicationTime().toString());
				user.setModified(this.getApplicationManager().getApplicationTime().toString());
				user.setEnabled(!this.getRegistrationManager().isEmailValidationEnabled());
				userManager.createUser(user);
				EmailMessage msg = new EmailMessage();
				msg.addRecipient(new EmailAddress(user.getUserName(), user
						.getEmail()));
				msg.addBccRecipient(new EmailAddress("Admin",this.getApplicationManager().getFeedbackMailFromAddress()));
			//	msg.addBccRecipient(new EmailAddress("Admin","rajashree.meganathan@sourcen.com"));
				msg.setSubject("Welcome to Meteriffic!");
				String body;
				if (getRegistrationManager().isEmailValidationEnabled()) {
				String token=applicationManager.getStringToken();
					body = "Dear "+user.getUserName()+",<br/><br/>Thank you for registering with Meteriffic, a groundbreaking new service for gaining meaningful product insight from online consumer reviews, forums and blogs.<br/><br/>Here are your registration details:<br/><br/>Username:  "+user.getUserName()+"<br/>First Name: "+user.getFirstName()+"<br/>Last Name:"+user.getLastName()+"<br/>Email: "+user.getEmail()+"<br/><br/>Please <a href=\""+ getBaseUrl() + "/index!verifyEmail.htm?username="+ username + "&keycode=" + token+ "\"  \" >click here </a> to activate your account<br/><br/>If you need help or have comments or suggestions, please email us at "+this.applicationManager.getFeedbackMailFromAddress()+". We're always interested in feedback, so don't hesitate to send us your thoughts.<br/><br/>Sincerely,<br/>The Meteriffic Product Team"; 
					this.getApplicationManager().saveProperty(
							new Property(username, token));
				}else
					body ="Dear "+user.getUserName()+",<br/><br/>Thank you for registering with Meteriffic, a groundbreaking new service for gaining meaningful product insight from online consumer reviews, forums and blogs.<br/><br/>Here are your registration details:<br/><br/>Username:  "+user.getUserName()+"<br/>First Name: "+user.getFirstName()+"<br/>Last Name:"+user.getLastName()+"<br/>Email: "+user.getEmail()+"<br/><br/>Please click on the following link to return to the Meteriffic login page:<br/>"+ getBaseUrl() + "/login.htm<br/><br/>If you need help or have comments or suggestions, please email us at "+this.applicationManager.getFeedbackMailFromAddress()+". We're always interested in feedback, so don't hesitate to send us your thoughts.<br/><br/>Sincerely,<br/>The Meteriffic Product Team";
				
				msg.setHtmlBody(body);
				msg.setSender(new EmailAddress("Admin",this.getApplicationManager().getFeedbackMailFromAddress()));

				emailManager.send(msg);
			}catch(UnsupportedOperationException e){
				return "unavailable";
			} catch (NotFoundException e) {
				return "unavailable";
			} catch (UserAlreadyExistsException e) {
				return INPUT;
			}
			if(getRegistrationManager().isEmailValidationEnabled())
				this.addActionMessage(getText("create.account.activation.success", new String[]{this.getBaseUrl()}));
			else
				this.addActionMessage(getText("create.account.success", new String[]{this.getBaseUrl()}));
			return SUCCESS;
	}
	
	public void validate() {
		if (getUsername() == null)
			addFieldError("username", getText("error.username.required"));
		if(getUsername().length() < getRegistrationManager().getDefaultMinUsernameLength()){
			addFieldError("username", getText("error.username.minlength"));
		} else
			try {
				if(getUsername().equals(this.getUserManager().getUser(getUsername()).getUserName())){
									addFieldError("username", getText("error.username.unique"));
				}
			} catch (NotFoundException e) {
				
			}
		if (getEmail() == null) {
			addFieldError("email", getText("error.email.required"));
		}
		if (getLastname() == null) {
			addFieldError("lastname", getText("error.username.required"));
		}
		if (getPassword() == null) {
			addFieldError("password", getText("error.password.required"));
		}
		if (!getPassword().equals(getConfirmPassword())) {
			 addFieldError("password", getText("error.password.match"));
		}
		if (!isAgree()) {
			addFieldError("isAgree", getText("error.aggrement.required"));
		}
		super.validate();
	}

	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isAgree() {
		return agree;
	}
	public void setAgree(boolean agree) {
		this.agree = agree;
	}
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}
}

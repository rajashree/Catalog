package com.sourcen.space.action;

import javassist.NotFoundException;

import com.sourcen.space.model.EmailMessage;
import com.sourcen.space.model.Property;
import com.sourcen.space.model.User;
import com.sourcen.space.model.EmailMessage.EmailAddress;
import com.sourcen.space.service.RegistrationManager;
import com.sourcen.space.service.UserAlreadyExistsException;

public class SignUpAction extends SpaceActionSupport {

	RegistrationManager registrationManager = null;

	private static final long serialVersionUID = 1L;

	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private String conformPassword;
	private String email;
	private boolean agree;
	private String keycode;

	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	public String execute() {

		if (hasErrors()) {
			return INPUT;
		}
		if (registrationManager.isEnabled())
			try {
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				user.setEmail(email);
				user.setFirstName(firstname);
				user.setLastName(lastname);
				user.setCreated(this.getApplicationManager()
						.getApplicationTime().toString());
				user.setModified(this.getApplicationManager()
						.getApplicationTime().toString());
				user.setEnabled(!this.registrationManager
						.isEmailValidationEnabled());
				userManager.createUser(user);
				EmailMessage msg = new EmailMessage();
				msg.addRecipient(new EmailAddress(user.getUsername(), user
						.getEmail()));
				msg.addBccRecipient(new EmailAddress("Admin",this.registrationManager.getFeedbackMailFromAddress()));
			//	msg.addBccRecipient(new EmailAddress("Admin","rajashree.meganathan@sourcen.com"));
				msg.setSubject("Welcome to Accept Search!");

				
				String body ="Dear "+user.getUsername()+",<br/><br/>Thank you for registering with Accept Search, a groundbreaking new service for gaining meaningful product insight from online consumer reviews, forums and blogs.<br/><br/>Here are your registration details:<br/><br/>Username:  "+user.getUsername()+"<br/>First Name: "+user.getFirstName()+"<br/>Last Name:"+user.getLastName()+"<br/>Email: "+user.getEmail()+"<br/><br/>Please click on the following link to return to the Accept Search login page:<br/>"+ getBaseUrl() + "/login.htm<br/><br/>If you need help or have comments or suggestions, please email us at "+this.registrationManager.getFeedbackMailFromAddress()+". We're always interested in feedback, so don't hesitate to send us your thoughts.<br/><br/>Sincerely,<br/>The Accept Search Product Team";
				if (this.registrationManager.isEmailValidationEnabled()) {
				String token=applicationManager.getStringToken();
					body = body + "Click here to activate your account <a href=\""
					+ getBaseUrl() + "/index!varifyEmail.htm?username="
					+ username + "&keycode=" + token
					+ "\"  \" >Click here </a>";
					
					this.getApplicationManager().saveProperty(
							new Property(username, token));

				}
				msg.setHtmlBody(body);
				msg.setSender(new EmailAddress("Admin",this.registrationManager.getFeedbackMailFromAddress()));

				emailManager.send(msg);

			} catch (UnsupportedOperationException e) {

				return "unavailable";
			} catch (UserAlreadyExistsException e) {
				return INPUT;
			} catch (NotFoundException e) {
				return "unavailable";
			}
		this.addActionMessage(getText("create.account.success"));

		return SUCCESS;
	}

	public String input() {
		if (!registrationManager.isEnabled())
			return "unavailable";

		
		return INPUT;

	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	public void validate() {
		if (getUsername() == null
				|| getUsername().length() < registrationManager
						.getDefaultMinUsernameLength()) {
			addFieldError("username", getText("error.username.required"));

		}
		if (getEmail() == null
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

	public String getConformPassword() {
		return conformPassword;
	}

	public void setConformPassword(String conformPassword) {
		this.conformPassword = conformPassword;
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

	public void setAgree(boolean isAgree) {
		
	
		this.agree = isAgree;
	}

}

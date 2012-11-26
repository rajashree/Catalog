/*
 * Created on Sep 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 

package com.rdta.Admin.User;

import java.io.File;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author Ajay Reddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserForm extends ActionForm {
	protected String sessionID;
	 protected String userID;
	 protected String firstName;
	 protected String lastName;
	 protected String userName;
	 protected String email;
	 protected String password;
	 protected boolean disabled;
	 protected String phone;
	 protected String fax;
	 protected String privateKey;
	 protected String publicKey;
	// protected String accessLevel;
	 protected String userRole;
	 protected String belongsToCompany; //here
	 protected String bgLocation;
	 protected  FormFile signatureFile;
	 protected boolean signer;
		File picture = null;
	 
	 
	/**
	 * @return Returns the sessionID.
	 */
	public String getSessionID() {
		return sessionID;
	}
	/**
	 * @param sessionID The sessionID to set.
	 */
	public void setSessionID(String sessionID) {
		System.out.println("In setSession Id of form Bean"+sessionID);
		this.sessionID = sessionID;
	}
	/**
	 * @return Returns the bgLocation.
	 */
	public String getBgLocation() {
		return bgLocation;
	}
	/**
	 * @param bgLocation The bgLocation to set.
	 */
	public void setBgLocation(String bgLocation) {
		this.bgLocation = bgLocation;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	/*public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}*/
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the userID.
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @param userID The userID to set.
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return Returns the userRole.
	 */
	public String getUserRole() {
		return userRole;
	}
	/**
	 * @param userRole The userRole to set.
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	//here
	/**
	 * @return Returns the belongsToCompany.
	 */
	public String getBelongsToCompany() {
		return belongsToCompany;
	}
	/**
	 * @param belongsToCompany The belongsToCompany to set.
	 */
	public void setBelongsToCompany(String belongsToCompany) {
		this.belongsToCompany = belongsToCompany;
	}	
	
    /**
     * @return Returns the signatureFile.
     */
    public FormFile getSignatureFile() {
        return signatureFile;
    }
    /**
     * @param signatureFile The signatureFile to set.
     */
    public void setSignatureFile(FormFile signatureFile) {
        this.signatureFile = signatureFile;
    }
    
	public void reset(){
		
		 userID=null;
		 firstName=null;
		 lastName=null;
		 userName=null;
		 email=null;
		 password=null;
		 disabled=false;
		//  accessLevel;
		 userRole=null;
		 belongsToCompany=null; //here		 
		 bgLocation=null;
		
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public boolean isSigner() {
		return signer;
	}
	public void setSigner(boolean signer) {
		this.signer = signer;
	}
	
}

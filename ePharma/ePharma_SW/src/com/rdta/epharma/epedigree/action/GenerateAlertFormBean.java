
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

 package com.rdta.epharma.epedigree.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


public class GenerateAlertFormBean extends ActionForm{
	
	private String APNID;
	private String Title;
	private String sessionID;
	private String action;
	private String comments;
	private String severitylevel;
	private String systemroles;
	private String exceptions;
	private String pagenm;
	private String tp_company_nm;
	private String CreatedBy;
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAPNID() {
		return APNID;
	}
	public void setAPNID(String apnid) {
		APNID = apnid;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getExceptions() {
		return exceptions;
	}
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}
	public String getSeveritylevel() {
		return severitylevel;
	}
	public void setSeveritylevel(String severitylevel) {
		this.severitylevel = severitylevel;
	}
	public String getSystemroles() {
		return systemroles;
	}
	public void setSystemroles(String systemroles) {
		this.systemroles = systemroles;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getPagenm() {
		return pagenm;
	}
	public void setPagenm(String pagenm) {
		this.pagenm = pagenm;
	}
	public String getTp_company_nm() {
		return tp_company_nm;
	}
	public void setTp_company_nm(String tp_company_nm) {
		this.tp_company_nm = tp_company_nm;
	}
	
}

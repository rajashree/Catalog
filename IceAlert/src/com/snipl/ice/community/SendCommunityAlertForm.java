package com.snipl.ice.community;
/**
* @Author Kamalakar Challa 
*   
*/

import org.apache.struts.action.ActionForm;

public class SendCommunityAlertForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subject=null;
	private String description=null;
	private String commid=null;
	
	public void setSubject(String subject) {
		this.subject=subject;
	}
	public String getSubject()
	{
		return subject;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}
	public String getDescription()
	{
		return description;
	}
	
	public void setCommid(String commid) {
		this.commid=commid;
	}
	public String getCommid()
	{
		return commid;
	}
}

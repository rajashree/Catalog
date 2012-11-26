package com.snipl.ice.community;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class BackToInviteForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String backToInvite=null;
	
	public void setBackToInvite(String backToInvite) {
		this.backToInvite=backToInvite;
	}
	public String getBackToInvite()
	{
		return backToInvite;
	}
	
	public void reset(ActionMapping mapping,HttpServletRequest request){

		   this.backToInvite="";
	}
}

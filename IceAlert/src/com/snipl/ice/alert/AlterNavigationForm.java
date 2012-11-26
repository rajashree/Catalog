package com.snipl.ice.alert;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class AlterNavigationForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String alert_pre=null;
	private String alert_next=null;
	
	
	public void setAlert_pre(String alert_pre) {
		this.alert_pre=alert_pre;
	}
	public String getAlert_pre()
	{
		return alert_pre;
	}
	
	public void setAlert_next(String alert_next) {
		this.alert_next=alert_next;
	}
	public String getAlert_next()
	{
		return alert_next;
	}
	
	 public void reset(ActionMapping mapping,HttpServletRequest request){

		   this.alert_pre="";
		   this.alert_next="";
	 }

}

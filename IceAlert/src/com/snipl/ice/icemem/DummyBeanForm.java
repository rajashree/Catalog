package com.snipl.ice.icemem;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class DummyBeanForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean check=false;
	
	public void setCheck(boolean check) {
		this.check=check;
	}
	public boolean getCheck()
	{
		return check;
	}
	
	 public void reset(ActionMapping mapping,HttpServletRequest request){
		 this.check=false;
	 }
}

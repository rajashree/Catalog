package com.snipl.ice.community;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SelectedContactsForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String queary=null;
	
	public void setQueary(String queary){
	   this.queary=queary;
   }
   public String getQueary(){
	   return this.queary;
   }
   
   public void reset(ActionMapping mapping,HttpServletRequest request){

	   this.queary=null;
   }
}

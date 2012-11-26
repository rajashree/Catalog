package com.snipl.ice.community;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class AllCommunitiesForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String community_next=null;
	private String community_pre=null;
	private String searchkey=null;
	private String alert="false";
	
	
	public void setCommunity_next(String community_next) {
		this.community_next=community_next;
	}
	public String getCommunity_next()
	{
		return community_next;
	}
	
	public void setCommunity_pre(String community_pre) {
		this.community_pre=community_pre;
	}
	public String getCommunity_pre()
	{
		return community_pre;
	}
	
	public void setSearchkey(String searchkey) {
		this.searchkey=searchkey;
	}
	public String getSearchkey()
	{
		return searchkey;
	}
	
	public void setAlert(String alert) {
		this.alert=alert;
	}
	public String getAlert()
	{
		return alert;
	}
	
	 public void reset(ActionMapping mapping,HttpServletRequest request){

		   this.community_next="";
		   this.community_pre="";
		   this.searchkey="";
		   this.alert="false";
	 }

}

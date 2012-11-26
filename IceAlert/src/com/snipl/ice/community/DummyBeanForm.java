package com.snipl.ice.community;


import org.apache.struts.action.ActionForm;

public class DummyBeanForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name=null;
	private String id=null;
	
	public void setName(String name) {
		this.name=name;
	}
	public String getName()
	{
		return name;
	}
	
	public void setId(String id) {
		this.id=id;
	}
	public String getId()
	{
		return id;
	}
}

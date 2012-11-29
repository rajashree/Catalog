package com.sourcen.meteriffic.action;



import java.util.List;

import javassist.NotFoundException;

import org.acegisecurity.context.SecurityContextHolder;

public class SettingsAction extends SpaceActionSupport{
	public String execute(){
		List list = this.getUserManager().getRoles(SecurityContextHolder.getContext().getAuthentication().getName());
		if(list.size() >0)
			System.out.println(list.size());
		else
			System.out.println(list.size());
	
	return SUCCESS;
	}
}

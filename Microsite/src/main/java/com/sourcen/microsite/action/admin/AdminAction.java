package com.sourcen.microsite.action.admin;

/*
 * Revision: 1.0
 * Date: October 25, 2008
 *
 * Copyright (C) 2005 - 2008 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 *
 * By : Chandra Shekher
 *
 */
import com.opensymphony.xwork2.Preparable;
import com.sourcen.microsite.action.SourcenActionSupport;

public class AdminAction extends SourcenActionSupport implements Preparable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public String execute() {
		this.actionIndex = 0;

		return SUCCESS;

	}

	public void prepare() throws Exception {
		this.menuIndex = 4;
	}

}

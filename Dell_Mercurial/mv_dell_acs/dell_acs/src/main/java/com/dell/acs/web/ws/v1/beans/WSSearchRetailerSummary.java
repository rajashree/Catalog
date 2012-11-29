/*
 * Copyright (c) Dell Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.web.ws.beans.base.WSBeanModel;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public class WSSearchRetailerSummary extends WSBeanModel {
	/**
	 * Generated serial id required by java serialization. 
	 */
	private static final long serialVersionUID = -826286442690311748L;
	private String siteName;
	private int totalCount;
	
	/**
	 * 
	 */
	public WSSearchRetailerSummary() {
		// TODO Auto-generated constructor stub
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String pSiteName) {
		this.siteName = pSiteName;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int pTotalCount) {
		this.totalCount = pTotalCount;
	}

}

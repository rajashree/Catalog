/*
 * Copyright (c) Dell Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import java.util.Collection;

import com.sourcen.core.web.ws.beans.base.WSBeanModel;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public class WSSearchHeader extends WSBeanModel {
	/**
	 * Generated serial id required by java serialization. 
	 */
	private static final long serialVersionUID = 4751425924613210831L;
	
	int totalCount;
	Collection<WSSearchRetailerSummary> retailers;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int pTotalCount) {
        this.totalCount = pTotalCount;
    }

    public Collection<WSSearchRetailerSummary> getRetailers() {
        return retailers;
    }

    public void setRetailers(Collection<WSSearchRetailerSummary> pRetailers) {
        this.retailers = pRetailers;
    }

    @Override
    public String toString() {
        return "WSSerachHeader{" +
                "totalCount=" + totalCount +
                ", retailers=" + retailers +
                '}';
    }
}

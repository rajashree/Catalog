/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 
package com.rdta.catalog.trading.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class StartUploadProcessFormBean extends ActionForm {
	
	String catalogGenId;
	String standardCatalogId;
	String operationType;
	
	public String getCatalogGenId() {
		return catalogGenId;
	}
	public void setCatalogGenId(String catalogGenId) {
		this.catalogGenId = catalogGenId;
	}
	public String getStandardCatalogId() {
		return standardCatalogId;
	}
	public void setStandardCatalogId(String standardCatalogId) {
		this.standardCatalogId = standardCatalogId;
	}	
	public String getoperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
}

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

 
/*
 * Created on Aug 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.reports.form;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author mgambhir
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PedigreeReportForm extends ActionForm {

	/*
	 * 
	 private String pedigreeId ;
	private Date dateIssued;
	private Date dateReceived;
	private String licenseNumber;
	private String shippingTraderPartner;
	private String receivingTraderPartner;
	*/
	private String cubeName;
	private String[] filterList;
	private String[] outputFieldList;
	
	
	
	/**
	 * @return Returns the cubeName.
	 */
	public String getCubeName() {
		return cubeName;
	}
	/**
	 * @param cubeName The cubeName to set.
	 */
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
	/**
	 * @return Returns the filterList.
	 */
	public String[] getFilterList() {
		return filterList;
	}
	/**
	 * @param filterList The filterList to set.
	 */
	public void setFilterList(String[] filterList) {
		this.filterList = filterList;
	}
	/**
	 * @return Returns the outputFieldList.
	 */
	public String[] getOutputFieldList() {
		return outputFieldList;
	}
	/**
	 * @param outputFieldList The outputFieldList to set.
	 */
	public void setOutputFieldList(String[] outputFieldList) {
		this.outputFieldList = outputFieldList;
	}
	}

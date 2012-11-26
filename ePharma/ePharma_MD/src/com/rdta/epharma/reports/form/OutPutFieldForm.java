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

 
package com.rdta.epharma.reports.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

public class OutPutFieldForm extends ActionForm {
	
	private String cubeName=null;
	private String key=null;
	private String name=null;
	private String join=null;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
	public String getCubeName() {
		return cubeName;
	}
	public String getJoin() {
		return join;
	}
	public void setJoin(String join) {
		this.join = join;
	}
	
	
	
}

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

 
package com.rdta.catalog.session;

import java.util.List;
import java.io.Serializable;



public class ReconcilableData implements Serializable {
	
	
	 private String targetElementName;
	 private String sourceElementName;
	 
	 //needs to be resloved
	 private int totalNumOfValues; 
	 private List valuesList;
	 
	 //it in integer value
	 private String columnIndex;
	 
	 public ReconcilableData(){
		 
	 }

	public String getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(String columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getSourceElementName() {
		return sourceElementName;
	}

	public void setSourceElementName(String sourceElementName) {
		this.sourceElementName = sourceElementName;
	}

	public String getTargetElementName() {
		return targetElementName;
	}

	public void setTargetElementName(String targetElementName) {
		this.targetElementName = targetElementName;
	}

	public int getTotalNumOfValues() {
		return totalNumOfValues;
	}

	public void setTotalNumOfValues(int totalNumOfValues) {
		this.totalNumOfValues = totalNumOfValues;
	}

	public List getValuesList() {
		return valuesList;
	}

	public void setValuesList(List valuesList) {
		this.valuesList = valuesList;
	} 
		
}
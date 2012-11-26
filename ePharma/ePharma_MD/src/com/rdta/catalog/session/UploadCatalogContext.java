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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;

import com.rdta.catalog.MappingNodeObject;


public class UploadCatalogContext implements Serializable {
	
	private String filePath;
	private MappingNodeObject mappingNodeObj;
	private List reconcilableList;
	private List notMatchSourceColumns;
	private int noOfRecordsToLoad;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public MappingNodeObject getMappingNodeObj() {
		return mappingNodeObj;
	}

	public void setMappingNodeObj(MappingNodeObject mappingNodeObj) {
		this.mappingNodeObj = mappingNodeObj;
	}

	public List getReconcilableList() {
		return reconcilableList;
	}

	public void setReconcilableList(List reconcilableList) {
		this.reconcilableList = reconcilableList;
	}

	public List getNotMatchSourceColumns() {
		return notMatchSourceColumns;
	}

	public void setNotMatchSourceColumns(List notMatchSourceColumns) {
		this.notMatchSourceColumns = notMatchSourceColumns;
	}

	public int getNoOfRecordsToLoad() {
		return noOfRecordsToLoad;
	}

	public void setNoOfRecordsToLoad(int noOfRecordsToLoad) {
		this.noOfRecordsToLoad = noOfRecordsToLoad;
	} 
	
	
}
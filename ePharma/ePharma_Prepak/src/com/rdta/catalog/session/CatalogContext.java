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

import org.w3c.dom.Node;


public class CatalogContext implements Serializable {
	
	private HashMap map = new HashMap();
	private String catalogGenId; 
	
	
	public void setCatalogNode(String catalogGenId, Node node) {
		System.out.println("catalogGenId*********************"+catalogGenId);
		System.out.println("node*********************"+node);
		map.put(catalogGenId,node);
	}
	
	
	public Node getCatalogNode(String catalogGenId) {
		return (Node)map.get(catalogGenId);
	}
	
	public void setCatalogGenId(String catalogId) {
		catalogGenId = catalogId;
	}
		
	public String getCatalogGenId() {
		System.out.println("catalogGenID^^^^^^^^^^^^"+catalogGenId);
		return catalogGenId;
	}
	
}
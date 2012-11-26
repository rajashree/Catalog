
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

import java.util.HashMap;
import java.util.Map;

public class OutPutFieldPath {
	
	public static HashMap pathMap=null;
	
	public static HashMap getShippedPath(){
		pathMap=new HashMap();
		pathMap.put("EnvelopeID","envolopeId");
		pathMap.put("DateIssued","transactionInfo/transactionDate");
		pathMap.put("ContainerCode","contanier/containerCode");
		pathMap.put("PedigreeID","pedigreeId");
		pathMap.put("Manufacturer","productInfo/manufacturer");
		pathMap.put("DrugName","productInfo/drugName");
		pathMap.put("ProductCode","productInfo/productCode");
		pathMap.put("dosageForm","productInfo/dosageForm");
		pathMap.put("strength","productInfo/strength");
		pathMap.put("containerSize","productInfo/containerSize");
		pathMap.put("ExpirationDate","itemInfo/expirationDate");
		pathMap.put("LotNumber","itemInfo/lot");
		pathMap.put("LicenseNumber","transactionInfo/recipientInfo/licenseNumber");
		pathMap.put("DateReceived","receivingInfo/dateReceived");
		pathMap.put("ShippingTradingPartner","transactionInfo/senderInfo/businessAddress/businessName");
		pathMap.put("RecevingTradingPartner","transactionInfo/recipientInfo/businessAddress/businessName");
		pathMap.put("PackageType","productInfo/containerSize");
		pathMap.put("ManufacturingAddress","productInfo/manfactureAddress");
		pathMap.put("quantity","itemInfo/quantity");
		
		
		
		
		return pathMap;
	}
	

}

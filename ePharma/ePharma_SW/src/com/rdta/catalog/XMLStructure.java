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

 

package com.rdta.catalog;


import com.rdta.commons.xml.XMLUtil;
import org.w3c.dom.Node;

/**
 * Operation Type information.
 *
 */
public class XMLStructure {
    
	private static String tradingPartner = "<TradingPartner><genId></genId><name></name><partnerType></partnerType>" 
		+"<businessId></businessId><description> </description> <status></status><webURL></webURL>"
		+"<contact> </contact><title></title><deaNumber></deaNumber><phone></phone><fax></fax> <email></email> <notifyURI></notifyURI>"
		+"<address> <line1></line1> <line2></line2> <city></city>"
		+"<state></state><country></country><zip></zip></address></TradingPartner>";
	

	private static String imageNode ="<Image> <keyRef>"
		+"<collectionName></collectionName> <genId></genId></keyRef> <IMG></IMG></Image>";
		
		
	private static String location ="<TradingPartnerLocation> <keyRef>"
		+"<collectionName></collectionName> <genId></genId></keyRef>"
		+"<genId></genId> <name></name> <GLN></GLN> <description></description><GIAI></GIAI>"
		+"<GPS></GPS><type></type><event></event><latitude></latitude><longitude></longitude>"
		+"<phone></phone><fax></fax><address> <line1></line1> <line2></line2> <city></city>"
		+"<state></state><country></country><zip></zip></address> <inFormat></inFormat> <outFormat></outFormat> </TradingPartnerLocation>";

	
	
	private static String catalog ="<Catalog>"
		+"<catalogID></catalogID><catalogName></catalogName><description></description>"
        +"<keyRef>"
		+"<collectionName></collectionName><tradingPartnerName> </tradingPartnerName><tradingPartnerID></tradingPartnerID></keyRef>"
		//+"<createdDate> </createdDate> <updatedDate></updatedDate>"
		
 +"<schema> </schema> "
 +"</Catalog>";

	
	private static String mappingCatalogs = "<MappingCatalogs><name></name><genId></genId><headerInfo>"
		+"<source><catalogId></catalogId><name></name></source>"
		+"<target><catalogId></catalogId><name></name></target>"
		+"</headerInfo><dataList></dataList></MappingCatalogs>";
	
	/*
	 * Added for MappingProducts
	 * 
	 * */
	
	private static String mappingProducts = "<MappingProducts><name></name><genId></genId><headerInfo>"
		+"<source><catalogId></catalogId><name></name></source>"
		+"<target><catalogId></catalogId><name></name></target>"
		+"</headerInfo><dataList></dataList></MappingProducts>";
	
	
	
	
	private static String product = "<Product><genId></genId><refKey><catalogID></catalogID>"
		+"</refKey><isKit></isKit> <EPC></EPC>   <ParentEPC></ParentEPC>  <GTIN></GTIN>"
		+"<NDC></NDC> <ProductName></ProductName> <DosageForm></DosageForm>"
		+"<DosageStrength></DosageStrength> <ContainerSize></ContainerSize>  <LotNumber></LotNumber>"
		+"<LotExpireDate></LotExpireDate> <PackageUPC></PackageUPC> <MarketingStatus></MarketingStatus>"
		+"<Description></Description>"
		+"<ManufacturerName></ManufacturerName><ManufacturerLicense></ManufacturerLicense> <CustodyLicenseNumber></CustodyLicenseNumber>"
		+"<TagKillCode></TagKillCode> <EnvironmentalConstraints/>"
		+"<Overt> <Packagings> <Packaging> <Info/> <ImageUrl/> </Packaging> </Packagings>"
		+"<ProductMarkings> <ProductMarking> <Info/> <ImageUrl/> </ProductMarking> </ProductMarkings> </Overt>"
		+"<Covert> <Measure1/> <Measure2/> </Covert> <IncludeProducts></IncludeProducts> </Product>";
	
	
	//<refProduct> <name></name><genId></genId>
	private static String kitRef = "<KitRef> <genId></genId><keyRef> <genId></genId></keyRef>"
		+"<refProducts>  </refProduct>"
		+"</refProducts></KitRef>";
	
	public static Node getTradingPartnerNode() {
		
		return XMLUtil.parse(tradingPartner);
	}
	
	public static Node getLocationNode() {
		
		return XMLUtil.parse(location);
	}

	
	public static Node getImageNodeNode() {
		return XMLUtil.parse(imageNode);
	}
	
	
	public static Node getCatalogNode() {
		return XMLUtil.parse(catalog);
	}
	
	public static Node getMappingCatalogs() {
		return XMLUtil.parse(mappingCatalogs);
	}
	
	public static Node getMappingProducts() {
		return XMLUtil.parse(mappingProducts);
	}
	
	public static Node getProductNode() {
		return XMLUtil.parse(product);
	}
	
	public static Node getKitRefNode() {
		return XMLUtil.parse(kitRef);
	}
	
}

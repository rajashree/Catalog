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


/**
 * Constants
 *
 */
public interface Constants {
    
	public static final String CATALOG_DB  = "CatalogManager";
	public static final String EPHARMA_DB  = "ePharma";
	
	public static final String TRADING_PARTNER_COLL  = "TradingPartner";
	public static final String STD_TRADING_PARTNER_COLL  = "System";
	//public static final String STD_TRADING_PARTNER_COLL  = "StdTradingPartner";
	public static final String LOCATION_COLL  = "TradingPartnerLocation";
	public static final String IMAGES_COLL = "Images";
	
	public static final String CATALOG_COLL = "Catalog";
	public static final String MAPPING_CATALOGS_COLL = "MappingCatalogs";
	
	public static final String CATALOG_SCHMA_COLL = "CatalogSchema";
	
	public static final String PRODUCT_MASTER_COLL = "ProductMaster";
	
	public static final String KIT_REFERENCE_COLL = "KitReference";
	
	public static final String DESPATH_ADVICE_COLL  = "DespatchAdvice";
	public static final String ORDER_COLL  = "Orders";
	public static final String MASTER_CATALOG_NAMES = "ShowMasterCatalogsForm";

	public static final String MAPPING_PRODUCTS_COLL = "MappingProducts";
	
	
	//session infor
	
	public static final String SESSION_TP_CONTEXT = "TradingPartnerContext";
	public static final String SESSION_CATALOG_CONTEXT = "CatalogContext";
	
	public static final String SESSION_CATALOG_MAPPING_CONTEXT ="CatalogMappingContext";
	public static final String SESSION_CATALOG_UPLOAD_CONTEXT ="CatalogUploadContext";
	
	public static final String SESSION_KITREF_CONTEXT ="KitRefContext";
	//These are the changes for mapping between two trading partners
	public static final String MAP_TRD_PTNR_KEY = "MapTradingPartnerForm";
	public static final String MAP_TRD_CAT_KEY = "MapTradingPartnerForm";
	
	public static final String STD_CAT_TREE="StandardTree";
	public static final String RCVNG_MNGR_DETAILS = "ReceivingManagerForm";
	public static final String SEARCH_INVOICES_DETAIL = "SearchInvoicesForm";
	public static final String MANDATORY_ELEMENTS = "Mandatory";
	public static final String ALIAS_ELEMENTS = "Alias";
	public static final String Ship_MNGR_DETAILS = "ShipingManagerForm";
	public static final String ALERT_MSG_DETAILS = "EpedigreeForm";
	public static final String ORDER_DETAILS = "OrderSearchForm";
	public static final String ASN_DETAILS = "ASNSearchForm";
	//public static final String INVOICE_DETAILS = "ASNSearchForm";
	public static final String APN_DETAILS = "APNSearchForm";
	public static final String PEDIGREE_DETAILS = "ReceivedPedigreeSearchFormBean";

	public static final String ENVELOPE_DETAILS = "EpedigreeForm";
	public static final String RECEIVEDPEDIGREE_DETAILS = "ReceivedPedigreeForm";
	public static final String SHIPPEDPEDIGREE_DETAILS = "PedigreeDetailsForm";
	public static final String SHIPPED_DETAILS = "PedigreeStatusForm";
	public static final String RETURNS_DETAILS = "PedigreeReturnsForm";
	public static final String AUDITTRAIL_DETAILS = "AuditTrailForm";
	public static final String ATTACHMENT_DETAILS = "AttachmentForm";
	public static final String PRODUCTDISPLAY_DETAILS = "ProductDisplayForm";
	public static final String INVOICE_DETAILS = "InvoiceDetailsForm";
	public static final String MESSAGE_DETAILS = "ViewMessageForm";
	public static final String FAX_DETAILS = "ReceivedFaxesForm";
	public static final String ENTER_PEDIGREE_DETAILS = "ProcessedEnteredPedigreesForm";
	
	public static final String TP_DETAILS = "TradingPartnerListForm";

	public static final String NO_OF_RECORDS = "20";
	
	

}

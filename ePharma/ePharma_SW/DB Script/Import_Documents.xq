(: --------------- ePharma Documents ------------------------:)

for $i in collection( 'file:///C:/temp/importCollections/ePharma/Alerts' ) return insert document $i into 'tig:///ePharma/Alerts',
for $i in collection( 'file:///C:/temp/importCollections/ePharma/DespatchAdvice' ) return insert document $i into 'tig:///ePharma/DespatchAdvice',
for $i in collection( 'file:///C:/temp/importCollections/ePharma/Invoices' ) return insert document $i into 'tig:///ePharma/Invoices',
for $i in collection( 'file:///C:/temp/importCollections/ePharma/Orders' ) return insert document $i into 'tig:///ePharma/Orders',
for $i in collection( 'file:///C:/temp/importCollections/ePharma/PaperPedigree' ) return insert document $i into 'tig:///ePharma/PaperPedigree',  
for $i in collection( 'file:///C:/temp/importCollections/ePharma/PedigreeBank' ) return insert document $i into 'tig:///ePharma/PedigreeBank' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/PedigreeStatus' ) return insert document $i into 'tig:///ePharma/PedigreeStatus' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/ReceivedPedigree' ) return insert document $i into 'tig:///ePharma/ReceivedPedigree' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/ReceivedFax' ) return insert document $i into 'tig:///ePharma/ReceivedFax' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/ReportCubes' ) return insert document $i into 'tig:///ePharma/ReportCubes' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/ReportFilterMapping' ) return insert document $i into 'tig:///ePharma/ReportFilterMapping' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/ReportOutputFieldMapping' ) return insert document $i into 'tig:///ePharma/ReportOutputFieldMapping' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/ShippedPedigree' ) return insert document $i into 'tig:///ePharma/ShippedPedigree' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/SQLShip' ) return insert document $i into 'tig:///ePharma/SQLShip' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/SQLPedRcv') return insert document $i into 'tig:///ePharma/SQLPedRcv' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/SQLPedIn' ) return insert document $i into 'tig:///ePharma/SQLPedIn',
for $i in collection( 'file:///C:/temp/importCollections/ePharma/SQLPedBadRcv' ) return insert document $i into 'tig:///ePharma/SQLPedBadRcv' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma/SubscriberKeys' ) return insert document $i into 'tig:///ePharma/SubscriberKeys' 


,


(: ----------------------------CATALOG_MANAGER documents ----------------------------- :)  
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/ProductMaster' ) return insert document $i into 'tig:///CatalogManager/ProductMaster',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/TradingPartner' ) return insert document $i into 'tig:///CatalogManager/TradingPartner',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/Catalog' ) return insert document $i into 'tig:///CatalogManager/Catalog',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/CatalogSchema' ) return insert document $i into 'tig:///CatalogManager/CatalogSchema',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/Images' ) return insert document $i into 'tig:///CatalogManager/Images',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/KitReference' ) return insert document $i into 'tig:///CatalogManager/KitReference',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/MappingCatalogs' ) return insert document $i into 'tig:///CatalogManager/MappingCatalogs',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/TradingPartnerLocation' ) return insert document $i into 'tig:///CatalogManager/TradingPartnerLocation',
  

(: -----------------------------EAGRFID Documents --------------------------------------:)

(:for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Devices' ) return insert document $i into 'tig:///EAGRFID/Devices',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Events' ) return insert document $i into 'tig:///EAGRFID/Events',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/FCDetailsInfo' ) return insert document $i into 'tig:///EAGRFID/FCDetailsInfo', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/FilteredEvents' ) return insert document $i into 'tig:///EAGRFID/FilteredEvents', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Filters' ) return insert document $i into 'tig:///EAGRFID/Filters',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/LocationBuildGroup' ) return insert document $i into 'tig:///EAGRFID/LocationBuildGroup',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/LocationDefinitions' ) return insert document $i into 'tig:///EAGRFID/LocationDefinitions',  
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/LocationImage' ) return insert document $i into 'tig:///EAGRFID/LocationImage',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/ProductImage' ) return insert document $i into 'tig:///EAGRFID/ProductImage',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/ProductInventory' ) return insert document $i into 'tig:///EAGRFID/ProductInventory',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysSetup' ) return insert document $i into 'tig:///EAGRFID/SysSetup', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SystemVocabulary' ) return insert document $i into 'tig:///EAGRFID/SystemVocabulary', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysTransformation' ) return insert document $i into 'tig:///EAGRFID/SysTransformation',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Products' ) return insert document $i into 'tig:///EAGRFID/Products',:)

for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/GroupAccess' ) return insert document $i into 'tig:///EAGRFID/GroupAccess', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Repository' ) return insert document $i into 'tig:///EAGRFID/Repository',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysGroups' ) return insert document $i into 'tig:///EAGRFID/SysGroups',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysSessions' ) return insert document $i into 'tig:///EAGRFID/SysSessions', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysUsers' ) return insert document $i into 'tig:///EAGRFID/SysUsers', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/UserSign' ) return insert document $i into 'tig:///EAGRFID/UserSign'

  
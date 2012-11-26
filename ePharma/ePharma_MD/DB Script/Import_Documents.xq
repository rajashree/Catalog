(: --------------- ePharma Documents ------------------------:)

for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/Alerts' ) return insert document $i into 'tig:///ePharma_MD/Alerts',
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/DespatchAdvice' ) return insert document $i into 'tig:///ePharma_MD/DespatchAdvice',
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/Invoices' ) return insert document $i into 'tig:///ePharma_MD/Invoices',
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/Orders' ) return insert document $i into 'tig:///ePharma_MD/Orders',
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/PedigreeBank' ) return insert document $i into 'tig:///ePharma_MD/PedigreeBank' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/PedigreeStatus' ) return insert document $i into 'tig:///ePharma_MD/PedigreeStatus' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/ReceivedPedigree' ) return insert document $i into 'tig:///ePharma_MD/ReceivedPedigree' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/ReportCubes' ) return insert document $i into 'tig:///ePharma_MD/ReportCubes' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/ReportOutputFieldMapping' ) return insert document $i into 'tig:///ePharma_MD/ReportOutputFieldMapping' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/ReportFilterMapping' ) return insert document $i into 'tig:///ePharma_MD/ReportFilterMapping' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/ShippedPedigree' ) return insert document $i into 'tig:///ePharma_MD/ShippedPedigree' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/SQLShip' ) return insert document $i into 'tig:///ePharma_MD/SQLShip' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/SQLPedRcv') return insert document $i into 'tig:///ePharma_MD/SQLPedRcv' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/SQLPedIn' ) return insert document $i into 'tig:///ePharma_MD/SQLPedIn',
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/SQLPedBadRcv' ) return insert document $i into 'tig:///ePharma_MD/SQLPedBadRcv' ,
for $i in collection( 'file:///C:/temp/importCollections/ePharma_MD/SubscriberKeys' ) return insert document $i into 'tig:///ePharma_MD/SubscriberKeys' ,


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
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Devices' ) return insert document $i into 'tig:///EAGRFID/Devices',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Events' ) return insert document $i into 'tig:///EAGRFID/Events',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/FCDetailsInfo' ) return insert document $i into 'tig:///EAGRFID/FCDetailsInfo', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/FilteredEvents' ) return insert document $i into 'tig:///EAGRFID/FilteredEvents', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Filters' ) return insert document $i into 'tig:///EAGRFID/Filters',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/GroupAccess' ) return insert document $i into 'tig:///EAGRFID/GroupAccess', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/LocationDefinitions' ) return insert document $i into 'tig:///EAGRFID/LocationDefinitions',  
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/LocationImage' ) return insert document $i into 'tig:///EAGRFID/LocationImage',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/ProductImage' ) return insert document $i into 'tig:///EAGRFID/ProductImage',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/ProductInventory' ) return insert document $i into 'tig:///EAGRFID/ProductInventory',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Products' ) return insert document $i into 'tig:///EAGRFID/Products',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/Repository' ) return insert document $i into 'tig:///EAGRFID/Repository',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysGroups' ) return insert document $i into 'tig:///EAGRFID/SysGroups',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysSessions' ) return insert document $i into 'tig:///EAGRFID/SysSessions', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysSetup' ) return insert document $i into 'tig:///EAGRFID/SysSetup', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SystemVocabulary' ) return insert document $i into 'tig:///EAGRFID/SystemVocabulary', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysTransformation' ) return insert document $i into 'tig:///EAGRFID/SysTransformation',
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/SysUsers' ) return insert document $i into 'tig:///EAGRFID/SysUsers', 
for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/UserSign' ) return insert document $i into 'tig:///EAGRFID/UserSign'

(: for $i in collection( 'file:///C:/temp/importCollections/EAGRFID/LocationBuildGroup' ) return insert document $i into 'tig:///EAGRFID/LocationBuildGroup',:)
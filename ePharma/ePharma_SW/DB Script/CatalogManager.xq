for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/ProductMaster' ) return insert document $i into 'tig:///CatalogManager/ProductMaster',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/TradingPartner' ) return insert document $i into 'tig:///CatalogManager/TradingPartner',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/Catalog' ) return insert document $i into 'tig:///CatalogManager/Catalog',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/Images' ) return insert document $i into 'tig:///CatalogManager/Images',

for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/MappingCatalogs' ) return insert document $i into 'tig:///CatalogManager/MappingCatalogs',
for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/TradingPartnerLocation' ) return insert document $i into 'tig:///CatalogManager/TradingPartnerLocation'
 
(:for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/CatalogSchema' ) return insert document $i into 'tig:///CatalogManager/CatalogSchema',

for $i in collection( 'file:///C:/temp/importCollections/CatalogManager/KitReference' ) return insert document $i into 'tig:///CatalogManager/KitReference',
:)

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Catalog","CatalogManager",
			"To store all the Catalogs that are created.", $config),



let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","CatalogSchema","CatalogManager",
			"To store all Catalog Schema .", $config),



let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Images","CatalogManager",
			"To store all Catalog Schema .", $config),



let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","KitReference","CatalogManager",
			"To store all Kits info data .", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","MappingCatalogs","CatalogManager",
			"To store all Catalog mappings .", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ProductMaster","CatalogManager",
			"To store all Product Master Schema .", $config),


let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","TradingPartnerLocation","CatalogManager",
			"To store locations of Trading Partners .", $config),


(:let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Trading","CatalogManager",
			"To store all Catalog Schema .", $config),:)

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","TradingPartner","CatalogManager",
			"To store all Catalog Schema .", $config)
 
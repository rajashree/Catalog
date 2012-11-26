let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","GroupAccess","EAGRFID",
			"To store all the Catalogs that are created.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SysGroups","EAGRFID",
			"To store all the Catalogs that are created.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SysSessions","EAGRFID",
			"To store all the Catalogs that are created.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SysUsers","EAGRFID",
			"To store all the Catalogs that are created.", $config),
let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","UserSign","EAGRFID",
			"To store all the Catalogs that are created.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Repository","EAGRFID",
			"To store all the Catalogs that are created.", $config),




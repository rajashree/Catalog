let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Alerts","ePharma","Collection to store all the alert messages.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","DespatchAdvice","ePharma","To store all the Dispatch Advice that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Invoices","ePharma","To store all the invoices that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Orders","ePharma","To store all the orders that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","PaperPedigree","ePharma","Collection to store PaperPedigree data", $config),


let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","PedigreeBank","ePharma","Collection to store Diaggregation data", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","PedigreeStatus","ePharma","Collection to store Pedigree status data", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReceivedPedigree","ePharma","Collection to store received pedigree  data", $config),


let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReceivedFax","ePharma","Collection to store upstream trading partner faxes", $config),


let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReportFilterMapping","ePharma","Collection for reports", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReportCubes","ePharma","To store all the subsciber information that are coming in.", $config),	

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReportOutputFieldMapping","ePharma","To store all the subsciber information that are coming in.", $config),	

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ShippedPedigree","ePharma","Collection to store shipped pedigree  data", $config),	
	
let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return 
tig:create-collection( "root","SQLShip","ePharma","To store all information of SQLShip.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData> 
return
tig:create-collection( "root","SQLPedRcv","ePharma", "To store all the SQLPedRcv", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SQLPedIn","ePharma", "Collection to store SQLPedIn", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SQLPedBadRcv","ePharma", "Collection to store SQLPedBadRcv", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SubscriberKeys","ePharma", "Collection to store SubscriberKeys", $config)




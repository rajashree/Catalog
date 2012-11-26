let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Alerts","ePharma_MD","Collection to store all the alert messages.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","DespatchAdvice","ePharma_MD","To store all the Despatch Advice that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Orders","ePharma_MD","To store all the orders that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","Invoices","ePharma_MD","To store all the invoices that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","OrderResponse","ePharma_MD","To store all the responses to the orders that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","OrderCancellations","ePharma_MD","To store all the cancellations of the orders that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","PedigreeBank","ePharma_MD","Collection to store Diaggregation data", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","PedigreeStatus","ePharma_MD","Collection to store Pedigree status data", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReceivedPedigree","ePharma_MD","Collection to store received pedigree  data", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReportCubes","ePharma_MD","To store all the subsciber information that are coming in.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReportFilterMapping","ePharma_MD","Collection for reports", $config),
let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ReportOutputFieldMapping","ePharma_MD","Collection for reports", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","ShippedPedigree","ePharma_MD","Collection to store shipped pedigree  data", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return 
tig:create-collection( "root","SQLShip","ePharma_MD","To store all information of SQLShip.", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData> 
return
tig:create-collection( "root","SQLPedRcv","ePharma_MD", "To store all the SQLPedRcv", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SQLPedIn","ePharma_MD", "Collection to store SQLPedIn", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SQLPedBadRcv","ePharma_MD", "Collection to store SQLPedBadRcv", $config),

let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData>
return
tig:create-collection( "root","SubscriberKeys","ePharma_MD","Collection to store SubscriberKeys", $config)

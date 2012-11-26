tig:create-stored-procedure("GetBinaryDataForFile","
declare binary-encoding none;
declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $filePath as xs:string external ;
let $binData := binary{doc($filePath)}
return 
<data>{bin:base64-encode($binData)}</data>
"),
tig:create-stored-procedure("CheckForPedigreeExistsForPortal","
declare variable $pedEnv as string external;
declare variable $pediD as string external;
declare variable $invoice as string external;
for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber=$pedEnv and *:pedigree//*:shippedPedigree/*:documentInfo/*:serialNumber=$pediD]
where $i/*:pedigree//*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier=$invoice
return
$i"),
tig:create-stored-procedure("CheckForSubscriberForPortal","
declare variable $subid as string external;
for $i in collection('tig:///ePharma/SubscriptionInfo') 
where $i/SubscriberAgreement/SubscriberID=$subid
return
$i "),


tig:create-stored-procedure("GetPedigreeIDFromPE","
declare variable $pedEnv as string external;

for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber=$pedEnv]
return

data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)
"),



tig:create-stored-procedure("allPedigrees","
 for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/descendant::*:*
 where $i/name() = 'pedigree'
 return $i


"),

tig:create-stored-procedure("getRepackagedInfo","
declare variable $serialNumber as string external;
let $allPedigrees := tlsp:allPedigrees()
for $ped in $allPedigrees[*:shippedPedigree/*:documentInfo/*:serialNumber = $serialNumber]
return $ped/*:shippedPedigree
"),




tig:create-stored-procedure("getPedigreeDocs_Invoice","

declare variable $deaNumber as string external;
declare variable $invoiceNumber as string external;

let $values := tlsp:TPCustomization($deaNumber,'manualusecase') 
return (
let $pe := (for $x in collection('tig:///ePharma/ShippedPedigree')
	return $x/*:pedigreeEnvelope)
for  $p in $pe/*:pedigree where 
$p/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber=$deaNumber
and $p/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier=$invoiceNumber
(:and $p/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType='InvoiceNumber':)
return data($p/*:shippedPedigree/*:documentInfo/*:serialNumber)



)
 "),
 


tig:create-stored-procedure("getPedigreeDocs_InvoiceAndNDC","

declare variable $deaNumber as string external;
declare variable $ndcNumber as string external;
declare variable $invoiceNumber as string external;

let $values := tlsp:TPCustomization($deaNumber,'manualusecase') 
return (
let $pe := (for $x in collection('tig:///ePharma/ShippedPedigree')
	return $x/*:pedigreeEnvelope)
for  $p in $pe/*:pedigree where 
$p/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber=$deaNumber
and   $p//*:initialPedigree/*:productInfo/*:productCode=$ndcNumber  or $p//*:repackagedPedigree/*:productInfo/*:productCode=$ndcNumber 
and $p/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier=$invoiceNumber
return data($p/*:shippedPedigree/*:documentInfo/*:serialNumber)



)
 "),
 
 
tig:create-stored-procedure("TPCustomization","
declare variable $deaNumber as string external;
declare variable $usecase as string external;
let $config := <root>{(for $i in collection('tig:///ePharma/PedigreeTradingPartner')/PedigreeTradingPartner
where $i/deaNumber = $deaNumber
return
if( $usecase = 'manualusecase') then $i/configurationElements/manualusecase/config 
else if ( $usecase = 'dropshipusecase')then $i/configurationElements/dropshipusecase/config 
else if ( $usecase = 'automatedusecase')then $i/configurationElements/automatedusecase/config 
else ()
)}</root>

let $ccode := for $i in $config/config where $i/element = 'containerCode' return data($i/value)
let $shipment := for $i in $config/config where $i/element = 'shipmentHandle' return data($i/value)
return 
<result>
{
<containerCode>{$ccode}</containerCode>,
<shipmentHandle>{$shipment}</shipmentHandle>
}
</result>
")

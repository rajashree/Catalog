(:tig:drop-stored-procedure("getPedigreeDocs"),:)
tig:create-stored-procedure("getPedigreeDocs","
declare variable $poNumber as string external;
declare variable $deaNumber as string external;
declare variable $ndcNumber as string external;
declare variable $invoiceNumber as string external;


for  $p in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree where 

 $p/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber=$deaNumber

and $p/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier=$poNumber

and $p/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType='PurchaseOrderNumber'

and $p/*:shippedPedigree/*:initialPedigree/*:productInfo/*:productCode=$ndcNumber

return data($p/*:shippedPedigree/*:documentInfo/*:serialNumber)


")

,
tig:create-stored-procedure("getRepackagedInfo_MD","
declare variable $serialNumber as string external;
let $allPedigrees := 
 (
 for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/descendant::*:*
 where $i/name() = 'pedigree'
 return $i
 )

 for $ped in $allPedigrees[*:shippedPedigree/*:documentInfo/*:serialNumber = $serialNumber]
 return $ped/*:shippedPedigree
"),
tig:create-stored-procedure("CheckForPedigreeExistsForPortal","
declare variable $pedEnv as string external;
declare variable $pediD as string external;
declare variable $invoice as string external;
for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber=$pedEnv and *:pedigree//*:shippedPedigree/*:documentInfo/*:serialNumber=$pediD]
where $i/*:pedigree//*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier=$invoice
return
$i"),

tig:create-stored-procedure("CheckForSubscriberForPortal","
declare variable $subid as string external;
for $i in collection('tig:///ePharma_MD/SubscriptionInfo') 
where $i/SubscriberAgreement/SubscriberID=$subid
return
$i "),
tig:create-stored-procedure("GetBinaryDataForFile","
declare binary-encoding none;
declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $filePath as xs:string external ;
let $binData := binary{doc($filePath)}
return 
<data>{bin:base64-encode($binData)}</data>
"),

tig:create-stored-procedure("GetBinaryImageForServlet","
declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support'; 
declare binary-encoding none;

declare variable $node as binary() external;
declare variable $node-name as xs:string external; 

declare function local:extract($str as xs:string,$node-name as xs:string) as xs:string {
  let $end := concat( '</', $node-name, '>' )
  let $start := concat( '<', $node-name, '>' )
  return  substring-before( substring-after($str,$start) , $end ) 
};

    bin:base64-decode(  local:extract( bin:as-string($node,'utf-8') , $node-name )  )

")

,
(:tig:drop-stored-procedure("getPedigreeDocsForPONumber"),:)
tig:create-stored-procedure("getPedigreeDocsForPONumber","
declare variable $poNumber as string external;
declare variable $deaNumber as string external;
 declare variable $invoiceNumber as string external;

for  $p in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree where 
 $p/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber=$deaNumber
and $p/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier=$poNumber
and $p/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType='PurchaseOrderNumber'
return data($p/*:shippedPedigree/*:documentInfo/*:serialNumber)
 ")




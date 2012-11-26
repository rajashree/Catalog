
tig:drop-stored-procedure("ShippingPedigreeDetails_MD"),
tig:create-stored-procedure("ShippingPedigreeDetails_MD","

declare variable $serialNumber as string external;

declare function local:getItemInfo($node as node()){
if( exists($node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:quantity)) then 
$node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:quantity 
else if( exists($node/*:shippedPedigree/*:itemInfo/*:quantity)) then 
$node/*:shippedPedigree/*:itemInfo/*:quantity
else if (exists($node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo/*:quantity))then 
( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo/*:quantity )
else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:quantity) )then
( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:quantity) 
else if(exists($node/*:shippedPedigree/*:pedigree))then  
local:getItemInfo($node/*:shippedPedigree/*:pedigree) 
else local:getItemInfo($node/*:receivedPedigree/*:pedigree)

};
declare function local:getProductInfo($e as node()){
let $item := local:getItemInfo($e)
return
if(exists($e/*:shippedPedigree/*:repackagedPedigree)) then(
<drugName>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:drugName)}</drugName>,
<productCode>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:manufacturer)}</manufacturer>,



<quantity>{data(local:getItemInfo($e))}</quantity>,
<dosageForm>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:containerSize)}</containerSize>
)
else(
<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data(local:getItemInfo($e))}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>
)
};


for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree
where $e/*:shippedPedigree/*:documentInfo/*:serialNumber = $serialNumber

return <output>{
<repackage>{exists($e/*:shippedPedigree/*:repackagedPedigree)}</repackage>,
local:getProductInfo($e),
<custName>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</custName>,
<custAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</custAddress>,
<custContact>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</custContact>,
<custPhone>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</custPhone>,
<custEmail>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/*:shippedPedigree/*:signatureInfo/*:signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/*:shippedPedigree/*:documentInfo/*:serialNumber)}</pedigreeId>,
<transactionDate>{data($e/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</transactionDate>, 
<transactionType>{data($e/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</transactionType>,
<transactionNo>{data($e/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)}</transactionNo>,
<fromCompany>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</fromCompany>,
<toCompany>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:country))}</fromBillAddress>,
<fromContact>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</fromContact>,
<fromTitle>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:title)} </fromTitle>,
<fromPhone>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</fromPhone>,
<fromEmail>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</fromEmail>,
<fromLicense>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:country))}</toShipAddress>,
<toBillAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:country))}</toBillAddress>,
<toContact>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:name)}</toContact>,
<toTitle>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:title)}</toTitle>,
<toPhone>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:telephone)}</toPhone>,
<toEmail>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:email)}</toEmail>,
<toLicense>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber)}</toLicense>


}</output>
")
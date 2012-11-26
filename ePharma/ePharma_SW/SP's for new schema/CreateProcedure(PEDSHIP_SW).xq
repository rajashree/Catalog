tig:create-stored-procedure("UpDateLotQuantityforShippingNew", "
declare variable $lot_info as node()* external;

update
let $qty := xs:integer(data($lot_info/LotInfo/quantity))
let $swLotNum := data($lot_info/LotInfo/lot)
for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[SWLotNum = $swLotNum ]
replace value of $i/Quantity with $i/Quantity - $qty

"),

tig:create-stored-procedure("UpdatePedigreeBankQuantityNew","

declare variable $lotNum as string external;
declare variable $totQty as integer external;

update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $lotNum
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory - $totQty
"),

tig:create-stored-procedure("updatePedigreebankAfterShipmentNew", "

declare variable $product as node()* external;

let $swLotNum := data($product/ProductInfo/LotInfo[1]/lot)
let $qty := sum(for $lot_info in $product/ProductInfo/LotInfo return xs:integer(data($lot_info/quantity)))
let $containerSize  := xs:integer(data($product/ProductInfo/containerSize))
let $tolQty := $qty * $containerSize
let $prod  := <root>{$product/ProductInfo/node()}</root>
let $res := ( for $i in $prod/LotInfo return
	     tlsp:UpDateLotQuantityforShippingNew(<root>{$i}</root>))
let $r := tlsp:UpdatePedigreeBankQuantityNew($swLotNum,$tolQty)
return $res

"),

tig:create-stored-procedure("CreatedRepackagedElementNew","
declare variable $pedId as string external;
declare variable $singerId as string external;
declare variable $xmlString as node()* external;

declare function local:getItemInfo($node as node()) as node()+{
if( exists($node/*:receivedPedigree/*:receivingInfo/*:itemInfo)) then 
$node/*:receivedPedigree/*:receivingInfo/*:itemInfo 
else if( exists($node/*:shippedPedigree/*:itemInfo)) then 
$node/*:shippedPedigree/*:itemInfo
else if (exists($node/*:shippedPedigree/*:repackagedPedigree ))then 
( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo )
else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo) )then
( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo) else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getItemInfo($node/*:shippedPedigree/*:pedigree) else local:getItemInfo($node/*:receivedPedigree/*:pedigree)

};

let $productNode := $xmlString
let $itemNode := $xmlString/LotInfo
let $pedigree := tlsp:GetRPORInitialPedigreeDetails($pedId)
let $pedigreeType := $pedigree/PedigreeType

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $singerId
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>
return
if($pedigreeType = 'Received') then
let $RP := $pedigree/Pedigree/*:pedigree
let $product := (<productInfo>
		{$productNode/drugName}{$productNode/manufacturer}<productCode type='NDC'>{data($productNode/productCode)}</productCode>
		{$productNode/dosageForm}{$productNode/strength}{$productNode/containerSize}
		</productInfo>)
return

<repackagedPedigree>
	<previousProducts>
		<previousProductInfo>
			<manufacturer>{data($RP//*:initialPedigree/*:productInfo/*:manufacturer)}</manufacturer>
			<productCode type='NDC'>{data($RP//*:initialPedigree/*:productInfo/*:productCode)}</productCode>
		</previousProductInfo>
		{local:getItemInfo($RP)}
		<contactInfo>
			<name>{data($RP/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:name)}</name>
			<title>{data($RP/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:title)}</title>
			<telephone>{data($RP/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:telephone)}</telephone>
			<email>{data($RP/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:email)}</email>
			<url>{data($RP/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:url)}</url>
		</contactInfo>
	</previousProducts>

	<previousPedigrees>{$RP}</previousPedigrees>
	<productInfo>{$product//*}</productInfo>
	{
	for $lot_info in $productNode/LotInfo return
	<itemInfo>	
		{$lot_info/lot}<expirationDate>{data($lot_info/ExpirationDate)}</expirationDate>{$lot_info/quantity}
	 </itemInfo>}
</repackagedPedigree>
else 
let $initial := $pedigree/Pedigree/*:initialPedigree
let $product := (<productInfo>
		{$productNode/drugName}{$productNode/manufacturer}<productCode type='NDC'>{data($productNode/productCode)}</productCode>
		{$productNode/dosageForm}{$productNode/strength}{$productNode/containerSize}
		</productInfo>)
return <repackagedPedigree>
	<previousProducts>
		<previousProductInfo>
			<manufacturer>{data($initial/*:productInfo/*:manufacturer)}</manufacturer>
			<productCode type='NDC'>{data($initial/*:productInfo/*:productCode)}</productCode>
		</previousProductInfo>
		<itemInfo>
			<lot>{$initial/*:itemInfo/*:lot/text()}</lot>
			<expirationDate>{$initial/*:itemInfo/*:expirationDate/text()}</expirationDate>
			<quantity>{$initial/*:itemInfo/*:quantity/text()}</quantity>
			<itemSerialNumber>{$initial/*:itemInfo/*:itemSerialNumber/text()}</itemSerialNumber>
		</itemInfo>
		<contactInfo>
			<name>{data($signerInfo/name)}</name>
			<title>{data($signerInfo/title)}</title>
			<telephone>{data($signerInfo/telephone)}</telephone>
			<email>{data($signerInfo/email)}</email>
			<url>{data($signerInfo/url)}</url>
		</contactInfo>
	</previousProducts>
	<previousPedigrees>{$initial}</previousPedigrees>
	<productInfo>{$product//*}</productInfo>
	{
	for $lot_info in $productNode/LotInfo return
	<itemInfo>	
		{$lot_info/lot}<expirationDate>{data($lot_info/ExpirationDate)}</expirationDate>{$lot_info/quantity}
	 </itemInfo>}
</repackagedPedigree> 
"),

tig:create-stored-procedure("CreateShippedPedigreeRepackedNew","

declare variable $flag as string external;
declare variable $xmlString as string external;

import module namespace util = 'xquery:modules:util';

declare function local:getPartnerInfo( $name as string) as node()*
{
for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
where $i/name = $name
return $i
	
};

let $pedNode := tlsp:StringToNode($xmlString)/*:*
let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $signerId := $pedNode/signerId/text()
let $transDate := substring-before(data($pedNode/TransactionInfo/TransactionDate) cast as string,'T')

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $signerId
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>

let $sellerInfo := local:getPartnerInfo('Southwood Pharmaceuticals')
let $buyerInfo :=  $pedNode/TransactionInfo/Address

let $senderinfo := (<senderInfo><businessAddress><businessName>{data($sellerInfo/name)}</businessName>
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>
	<city>{data($sellerInfo/address/city)}</city><stateOrRegion>{data($sellerInfo/address/state)}</stateOrRegion>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>	</businessAddress>	
	<shippingAddress><businessName>{data($sellerInfo/name)}</businessName>	
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>	
	<city>{data($sellerInfo/address/city)}</city><stateOrRegion>{data($sellerInfo/address/state)}</stateOrRegion>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>	</shippingAddress>		<licenseNumber>{data($sellerInfo/businessId)}</licenseNumber>
	</senderInfo>)
let $recipientinfo := (<recipientInfo><businessAddress><businessName>{data($buyerInfo/businessName)}</businessName>	
		<street1>{data($buyerInfo/street1)}</street1><street2>{data($buyerInfo/street2)}</street2>	
		<city>{data($buyerInfo/city)}</city><stateOrRegion>{data($buyerInfo/stateOrRegion)}</stateOrRegion>
		<postalCode>{data($buyerInfo/postalCode)}</postalCode><country>{data($buyerInfo/country)}</country>
		</businessAddress>	
		<shippingAddress><businessName>{data($buyerInfo/businessName)}</businessName>
		<street1>{data($buyerInfo/street1)}</street1><street2>{data($buyerInfo/street2)}</street2>	
		<city>{data($buyerInfo/city)}</city><stateOrRegion>{data($buyerInfo/stateOrRegion)}</stateOrRegion>	
		<postalCode>{data($buyerInfo/postalCode)}</postalCode><country>{data($buyerInfo/country)}</country>	
		</shippingAddress>
		<licenseNumber>{data($buyerInfo/CustomerDEA)}</licenseNumber>
	</recipientInfo>)
let $envid := util:create-uuid()

let $pdenv := (
<pedigreeEnvelope>
<version>1</version>
<serialNumber>{$envid}</serialNumber>
<date>{$date}</date>
<sourceRoutingCode>Southwood Pharmaceuticals</sourceRoutingCode><destinationRoutingCode>{data($pedNode/TransactionInfo/Address/businessName)}</destinationRoutingCode>

{
for $s in (
<x> {for $product in $pedNode/ProductInfo
let $swLotNum := data($product/LotInfo[1]/lot)
let $qty := sum(for $lot_info in $product/LotInfo return xs:integer(data($lot_info/quantity)))
let $containerSize as xs:integer := xs:integer(data($product/containerSize))
let $tolQty := $qty * $containerSize

let $rcvPedId := xs:string(for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[SWLotNum = $swLotNum]
		return data($i/ReceivedPedigreeID) )
return if(count($rcvPedId) = 0) then false() else 
let $pedbank := tlsp:updatePedigreebankAfterShipmentNew(<root>{$product}</root>)
let $ped := tlsp:GetRPORInitialPedigreeDetails(xs:string($rcvPedId) ) 
return 
if( count($ped) = 0 ) then false()
 else 

let $quantity := data($product/LotInfo/quantity) 
let $lotexp := data($product/LotInfo/ExpirationDate)
let $sscc :=  ''
let $lotno := data($product/LotInfo/lot) 


let $docid := util:create-uuid()
let $shipid := util:create-uuid()
let $contid := util:create-uuid()

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber></itemSerialNumber><productCode type='NDC'>{data($product/productCode)}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>
</pedigreeHandle></container>) 

return ($container,
  
<pedigree>
  <shippedPedigree id='{$shipid}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>1</version>
	</documentInfo>
{if($flag = '1')then	
	''
else 
if($flag = '2') then <unsignedReceivedPedigree/>
else
if($flag = '3') then  tlsp:CreatedRepackagedElementNew(xs:string($rcvPedId),$signerId,$product)
else tlsp:GetRPDetails($rcvPedId)
  
}
	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{data($pedNode/TransactionInfo/TransactionId)}</identifier><identifierType>{data($pedNode/TransactionInfo/TransactionType)}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{$transDate}</transactionDate>
	</transactionInfo>
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{$dateTime}</signatureDate>
	  <signatureMeaning>Certified</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
<Signature>
	<SignedInfo>
		<CanonicalizationMethod Algorithm=''/>
		<SignatureMethod Algorithm=''/>
		<Reference>
			<DigestMethod Algorithm=''/>
			<DigestValue/>
		</Reference>
	</SignedInfo>
	<SignatureValue/>
</Signature>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</pedigreeEnvelope>
)
let $res := tig:insert-document('tig:///ePharma/ShippedPedigree',$pdenv)
return $envid


"),

tig:create-stored-procedure("ShipGoodsNew","

declare variable $xmlString as string external;
declare namespace bin= 'http://www.rainingdata.com/TigerLogic/binary-support';

let $pedNode := tlsp:StringToNode($xmlString)/*:*
for $pedship in $pedNode/PEDSHIP
	let $envId :=  tlsp:CreateShippedPedigreeRepackedNew('3', bin:as-string(   binary{ $pedship } , 'UTF-8' ))
  (:	let $k :=  tlsp:UpdatePedigreeBankQuantity($swLotNum,$totQty)
	let $ndc :=  tlsp:UpdatePedigreeBankNDC($swLotNum,$swNDC) :)
	return $envId 
  
")
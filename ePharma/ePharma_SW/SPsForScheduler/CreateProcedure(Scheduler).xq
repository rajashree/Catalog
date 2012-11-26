(:  FOR PEDSHIP :)
tig:create-stored-procedure("StringToNode","

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $ped as string external;

let $b := bin:parse(  binary{$ped} , 'text/xml' )
return $b
"),

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

tig:create-stored-procedure("GetPedigreeDetailsNew","
declare variable $rcvPedId as string external;
declare variable $productNode as node()* external;
let $pedigreeData := tlsp:GetRPORInitialPedigreeDetails($rcvPedId)
let $pedigreeType := $pedigreeData/PedigreeType
let $item_info := (for $lot_info in $productNode/LotInfo return
	<itemInfo>	
		{$lot_info/lot}<expirationDate>{substring-before(data($lot_info/ExpirationDate),'T')}</expirationDate>{$lot_info/quantity}
	 </itemInfo>)
return
if($pedigreeType = 'Received') then
( $pedigreeData/Pedigree/*:pedigree,$item_info)
else ($pedigreeData/Pedigree/*:initialPedigree, $item_info)
(:return ($pedigreeData/Pedigree/*:pedigree ,$item_info):)
"),

tig:create-stored-procedure("GetRPORInitialPedigreeDetails","

declare variable $pedID as string external;

let $ped := (for $j in collection('tig:///ePharma/ShippedPedigree')/*:pedigree
where $j/*:receivedPedigree/*:documentInfo/*:serialNumber = $pedID
return $j)
return  if(count($ped)= 0) then 
  <output><PedigreeType>Initial</PedigreeType>
  <Pedigree>{for $s in collection('tig:///ePharma/PaperPedigree')/initialPedigree
  where $s/DocumentInfo/serialNumber = $pedID
  return <initialPedigree>
	  <productInfo>
		{$s/productInfo/drugName}{$s/productInfo/manufacturer}<productCode type='NDC'>{data($s/productInfo/productCode)}</productCode>
		{$s/productInfo/dosageForm}{$s/productInfo/strength}{$s/productInfo/containerSize}
	  </productInfo>
	{$s/itemInfo}{$s/altPedigree}{$s/transactionInfo}
	 </initialPedigree>}
  </Pedigree>
  </output>
else 
  <output><PedigreeType>Received</PedigreeType>
	<Pedigree>{$ped}</Pedigree>
  </output>
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
		{$productNode/drugName}<manufacturer>{data($productNode/Manufacturer)}</manufacturer><productCode type='NDC'>{data($productNode/productCode)}</productCode>
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
		{$lot_info/lot}<expirationDate>{substring-before(data($lot_info/ExpirationDate),'T')}</expirationDate>{$lot_info/quantity}
	 </itemInfo>}
</repackagedPedigree>
else 
let $initial := $pedigree/Pedigree/*:initialPedigree
let $product := (<productInfo>
		{$productNode/drugName}<manufacturer>{data($productNode/Manufacturer)}</manufacturer><productCode type='NDC'>{data($productNode/productCode)}</productCode>
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
			<name>{data($initial/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</name>
			<title>{data($initial/*:transactionInfo/*:senderInfo/*:contactInfo/*:title)}</title>
			<telephone>{data($initial/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</telephone>
			<email>{data($initial/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</email>
			<url>{data($initial/*:transactionInfo/*:senderInfo/*:contactInfo/*:url)}</url>
		</contactInfo>

	</previousProducts>
	<previousPedigrees>{$initial}</previousPedigrees>
	<productInfo>{$product//*}</productInfo>
	{
	for $lot_info in $productNode/LotInfo return
	<itemInfo>	
		{$lot_info/lot}<expirationDate>{substring-before(data($lot_info/ExpirationDate),'T')}</expirationDate>{$lot_info/quantity}
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

let $sellerInfo := local:getPartnerInfo('SOUTHWOOD PHARMACEUTICALS, Inc.')
let $buyerInfo :=  $pedNode/TransactionInfo/Address

let $senderinfo := (<senderInfo><businessAddress><businessName>{data($sellerInfo/name)}</businessName>
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>
	<city>{data($sellerInfo/address/city)}</city><stateOrRegion>{data($sellerInfo/address/state)}</stateOrRegion>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>	</businessAddress>	
	<shippingAddress><businessName>{data($sellerInfo/name)}</businessName>	
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>	
	<city>{data($sellerInfo/address/city)}</city><stateOrRegion>{data($sellerInfo/address/state)}</stateOrRegion>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>	</shippingAddress>	
	<licenseNumber>{data($sellerInfo/DEANumber)}</licenseNumber>
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
<version>20060331</version>
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
let $useVendorLot := data($product/UseVendorLot)

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
	  <version>20060331</version>
	</documentInfo>
{ 
if(fn:upper-case($useVendorLot) = 'N') then  tlsp:CreatedRepackagedElementNew(xs:string($rcvPedId),$signerId,$product)
else tlsp:GetPedigreeDetailsNew(xs:string($rcvPedId),$product)
  
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
  
"),

tig:create-stored-procedure("GetRPID","
declare variable $xmlString as string external;
let $pedNode := tlsp:StringToNode($xmlString)/*:*
let $pedshipNode := for $pedship in $pedNode/PEDSHIP return $pedship
for $product in $pedshipNode/ProductInfo
let $swLotNum := data($product/LotInfo[1]/lot)
let $pedID := for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[SWLotNum = $swLotNum]
		return data($i/ReceivedPedigreeID)
let $ped := (for $j in collection('tig:///ePharma/ShippedPedigree')/*:pedigree
	    where $j/*:receivedPedigree/*:documentInfo/*:serialNumber = $pedID
	    return $j)
return 
if(exists($ped)) then $ped else 'false'
"),

tig:create-stored-procedure("GetEmailId","
declare variable $envId as string external;
declare variable $xmlString as string external;

let $invNum := (for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envId]
	       return data($i/*:pedigree[1]/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier))

let $pedNode := tlsp:StringToNode($xmlString)/*:*
let $pedshipNode := (for $pedship in $pedNode/PEDSHIP return $pedship)
for $ship in $pedshipNode/TransactionInfo[TransactionId = $invNum] 
return data($ship/Address/email)
"),

(:  FOR PEDRCV :)

tig:create-stored-procedure("ndcExists","
 declare variable $ndc as string external;
 
 for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand 
 where upper-case($i/NDC) = upper-case($ndc)
 return true()
"),

tig:create-stored-procedure("UpDateTotalInventory", "
declare variable $NDC as string external;
declare variable $value as integer external;
update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory+$value
"),

tig:create-stored-procedure("CreateRepackageInstrDocForInitial","
declare variable $ndc as string external;
declare variable $pedId as string external;
declare variable $pedRcv as node()* external;
import module namespace util = 'xquery:modules:util';

let $lot := (for $j in $pedRcv//iteminfo
let $itemInfo := (for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber= $pedId]
	        return $i/itemInfo)

let $quantity := xs:integer( for $k in $itemInfo
		 where fn:upper-case(data($k/lot)) = fn:upper-case(data($j/Vendor_Lot_Number))
		 return data($k/quantity))
return <root>
	  <prevQuantity>{$quantity}</prevQuantity>
	  <LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
	   <LotInfo>
		<Quantity/>
		<LotNumber>{data($j/SWP_Lot_Number)}</LotNumber>
		<SerialNumber/>
		<PreviousLotNumber>{data($j/Vendor_Lot_Number)}</PreviousLotNumber>
	    </LotInfo>
	</root>
)

let $docid := util:create-uuid()

let $repackNode := 
<RepackageInstructions>
	<DocumentId>{$docid}</DocumentId>
	<NewProduct>
	    <NDC>{data($pedRcv/SWP_Stock_Code)}</NDC>
	   <TransactionType/>
	  {$lot/LotInfo}		
	    <PreviousProduct>
		<NDC>{$ndc}</NDC>
		{$lot/LotNumber}
		<Quantity>{sum( ( for $i in $lot return xs:integer(data($i/prevQuantity )) ))}</Quantity>
		<SerialNumber/>
	   </PreviousProduct>
	</NewProduct>
</RepackageInstructions>

let $repack := insert document $repackNode into 'tig:///ePharma/RepackageInstructions' 
return 
<ID>{$docid}</ID>
"),

tig:create-stored-procedure("InsertLot" ,"
declare variable $NDC as string external;
declare variable $LotInfo as node()* external;


update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
insert
$LotInfo/LotInfo as last into $i/InventoryOnHand 
"),

tig:create-stored-procedure("insertLotinfoNodeNew","

declare variable $NDC as string external;
declare variable $LotInfo as node()* external;

for $lots in $LotInfo/LotInfo
for $root in collection('tig:///ePharma/PedigreeBank')
let $doc_uri as xs:string := document-uri( $root )

for $i in $root//PedigreeBank/InventoryOnHand [NDC = $NDC]
return 

if(data($i/LotInfo/SWLotNum) = data($lots/SWLotNum)) then 
(
tlsp:replaceQty($doc_uri , <root>{$lots}</root>,data($lots/Quantity) ) 
)
else( tlsp:InsertLot($NDC, <root>{$lots}</root>))

"),

tig:create-stored-procedure("replaceQty","
declare variable $docuri as string external;
declare variable $lotinfo as node()* external;
declare variable $quantity as string external;

	
update
let $lot_Num := xs:string($lotinfo/SWLotNum)
let $qty :=  $quantity cast as xs:integer 

for $i in doc($docuri)/PedigreeBank/InventoryOnHand/LotInfo
where $i/SWLotNum = $lotinfo/LotInfo/SWLotNum
 replace value of $i/Quantity with 
    $i/Quantity+$qty

"),

tig:create-stored-procedure("UpDatePedigreeBankDocForInitial", "
declare variable $NDC as string external;
declare variable $pedId as string external;
declare variable $pedRcv as node()* external;

let $lot := (for $j in $pedRcv//iteminfo
let $itemInfo := (for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber= $pedId]
	        return $i/itemInfo)

let $quantity := xs:integer( for $i in $itemInfo
		 where $i/lot = $j/Vendor_Lot_Number 
		 return data($i/quantity))
let $res := tlsp:UpDateTotalInventory($NDC, $quantity cast as integer )
return (

<LotInfo>
<LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
<SWLotNum>{data($j/SWP_Lot_Number)}</SWLotNum>
<ShippingInfo/>
<Quantity>{$quantity}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$pedId}</ReceivedPedigreeID>
</LotInfo>))
return 
(
tlsp:insertLotinfoNodeNew($NDC, <root>{$lot}</root> )

)
"),



tig:create-stored-procedure("UpDatePedigreeBankDocNew", "
declare variable $NDC as string external;
declare variable $recId as string external;
declare variable $pedRcv as node()* external;

declare function local:getItemInfo($node as node()){
if( exists($node/*:receivedPedigree/*:receivingInfo/*:itemInfo)) then 
$node/*:receivedPedigree/*:receivingInfo/*:itemInfo 
else if( exists($node/*:shippedPedigree/*:itemInfo)) then 
$node/*:shippedPedigree/*:itemInfo
else if (exists($node/*:shippedPedigree/*:repackagedPedigree ))then 
( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo )
else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo) )then
( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo) else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getItemInfo($node/*:shippedPedigree/*:pedigree) else local:getItemInfo($node/*:receivedPedigree/*:pedigree)

};

let $lot := (for $j in $pedRcv//LotInfo
let $itemInfo := (for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$i}</pedigree>))

let $quantity := xs:integer( for $i in $itemInfo
		 where $i/lot = $j/Vendor_Lot_Number 
		 return data($i/quantity))
let $res := tlsp:UpDateTotalInventory($NDC, $quantity cast as integer )
return (

<LotInfo>
<LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
<SWLotNum>{data($j/SWP_Lot_Number)}</SWLotNum>
<ShippingInfo/>
<Quantity>{$quantity}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
</LotInfo>))
return 
(
tlsp:insertLotinfoNodeNew($NDC, <root>{$lot}</root> )

)

"),

tig:create-stored-procedure("createNewPedBankDocForInitial", "
declare variable $NDC as string external;
declare variable $pedId as string external;
declare variable $pedRcv as node()* external;

let $Insertrepack := tlsp:CreateRepackageInstrDocForInitial($NDC,$pedId,$pedRcv)
let $swp_ndc := $pedRcv/SWP_Stock_Code
let $lot := (for $j in $pedRcv//iteminfo 
let $itemInfo := (for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber= $pedId]
	        return $i/itemInfo)	

let $quantity :=  xs:integer( for $i in $itemInfo
		 			   		  where fn:upper-case(data($i/lot)) = fn:upper-case(data($j/Vendor_Lot_Number))
					  		  return data($i/quantity))
let $res := tlsp:UpDateTotalInventory($NDC, $quantity cast as integer )
return 
<LotInfo>
<LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
<SWLotNum>{data($j/SWP_Lot_Number)}</SWLotNum>
<ShippingInfo/>
<Quantity>{$quantity}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$pedId}</ReceivedPedigreeID>
</LotInfo>)

let $PedigreeBank :=
(
<PedigreeBank>
<RepackagedInstructions>{$Insertrepack}</RepackagedInstructions>
<InventoryOnHand>
<NDC>{$NDC}</NDC>
<SWNDC>{data($swp_ndc)}</SWNDC>
<TotalInventory>{sum( ( for $i in $lot return xs:integer(data($i/Quantity )) ))}</TotalInventory>
{$lot}
</InventoryOnHand>
</PedigreeBank>
)
return insert document $PedigreeBank into  'tig:///ePharma/PedigreeBank' 

"),

tig:create-stored-procedure("UpdatePedigreeBankForInitial","

declare variable $pedId as string external;
declare variable $pedRcv as node()* external;

for $k in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber= $pedId]
let $ndc := data($k/productInfo/productCode)
return
if(tlsp:ndcExists($ndc) ) then
tlsp:UpDatePedigreeBankDocForInitial($ndc,$pedId,$pedRcv )
else 
tlsp:createNewPedBankDocForInitial($ndc,$pedId,$pedRcv)


"),

tig:create-stored-procedure("CreateRepackageInstrDoc","
declare variable $ndc as string external;
declare variable $recId as string external;
declare variable $pedRcv as node()* external;
import module namespace util = 'xquery:modules:util';


declare function local:getItemInfo($node as node()){
if( exists($node/receivedPedigree/receivingInfo/itemInfo)) then 
$node/receivedPedigree/receivingInfo/itemInfo 
else if( exists($node/shippedPedigree/itemInfo)) then 
$node/shippedPedigree/itemInfo
else if (exists($node/shippedPedigree/repackagedPedigree ))then 
( $node/shippedPedigree/repackagedPedigree/itemInfo )
else if( exists($node/shippedPedigree/initialPedigree/itemInfo) )then
( $node/shippedPedigree/initialPedigree/itemInfo) else if(exists($node/shippedPedigree/pedigree))then  local:getItemInfo($node/shippedPedigree/pedigree) else local:getItemInfo($node/receivedPedigree/pedigree)

};

let $lot := (for $j in $pedRcv//iteminfo
let $itemInfo := (for $m in collection('tig:///ePharma/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$m}</pedigree>))

let $quantity := xs:integer( for $k in $itemInfo
		 where $k/lot = $j/Vendor_Lot_Number 
		 return data($k/quantity))
return <root>
	  <prevQuantity>{$quantity}</prevQuantity>
	  <LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
	   <LotInfo>
		<Quantity/>
		<LotNumber>{data($j/SWP_Lot_Number)}</LotNumber>
		<SerialNumber/>
		<PreviousLotNumber>{data($j/Vendor_Lot_Number)}</PreviousLotNumber>
	    </LotInfo>
	</root>
)

let $docid := util:create-uuid()

let $repackNode := 
<RepackageInstructions>
	<DocumentId>{$docid}</DocumentId>
	<NewProduct>
	    <NDC>{data($pedRcv/SWP_Stock_Code)}</NDC>
	   <TransactionType/>
	  {$lot/LotInfo}		
	    <PreviousProduct>
		<NDC>{$ndc}</NDC>
		{$lot/LotNumber}
		<Quantity>{sum( ( for $i in $lot return xs:integer(data($i/prevQuantity )) ))}</Quantity>
		<SerialNumber/>
	   </PreviousProduct>
	</NewProduct>
</RepackageInstructions>

let $repack := insert document $repackNode into 'tig:///ePharma/RepackageInstructions' 
return 
<ID>{$docid}</ID>
"),

tig:create-stored-procedure("createNewPedBankDocNewschema", "
declare variable $NDC as string external;
declare variable $recId as string external;
declare variable $pedRcv as node()* external;

declare function local:getItemInfo($node as node()){
if( exists($node/*:receivedPedigree/*:receivingInfo/*:itemInfo)) then 
$node/*:receivedPedigree/*:receivingInfo/*:itemInfo 
else if( exists($node/*:shippedPedigree/*:itemInfo)) then 
$node/*:shippedPedigree/*:itemInfo
else if (exists($node/*:shippedPedigree/*:repackagedPedigree ))then 
( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo )
else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo) )then
( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo) else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getItemInfo($node/*:shippedPedigree/*:pedigree) else local:getItemInfo($node/*:receivedPedigree/*:pedigree)

};

let $Insertrepack := tlsp:CreateRepackageInstrDoc($NDC,$recId,$pedRcv)
let $swp_ndc := $pedRcv/SWP_Stock_Code
let $lot := (for $j in $pedRcv//iteminfo
let $itemInfo := (for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$i}</pedigree>))

let $quantity :=  xs:integer( for $i in $itemInfo
		 where $i/lot = $j/Vendor_Lot_Number 
		 return data($i/quantity))
let $res := tlsp:UpDateTotalInventory($NDC, $quantity cast as integer )
return 
<LotInfo>
<LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
<SWLotNum>{data($j/SWP_Lot_Number)}</SWLotNum>
<ShippingInfo/>
<Quantity>{$quantity}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
</LotInfo>)

let $PedigreeBank :=
(
for $s in collection('tig:///ePharma/ShippedPedigree')/*:pedigree/*:receivedPedigree 
where $s/*:documentInfo/*:serialNumber=$recId
return
<PedigreeBank>
<RepackagedInstructions>{$Insertrepack}</RepackagedInstructions>
<InventoryOnHand>
<NDC>{$NDC}</NDC>
<SWNDC>{data($swp_ndc)}</SWNDC>
<TotalInventory>{sum( ( for $i in $lot return xs:integer(data($i/Quantity )) ))}</TotalInventory>
{$lot}
</InventoryOnHand>
</PedigreeBank>
)
return insert document $PedigreeBank into  'tig:///ePharma/PedigreeBank' 


"),

tig:create-stored-procedure("CreateSignToReceivedPedigree","
declare character-encoding 'UTF-8';
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

declare variable $pedId as string external;

declare variable $keyFile as string {'C:/security/keys/RDTA_keystore'};
declare variable $keyPwd as string  {'jasmine23'};
declare variable $keyAlias as string {'RDTAClient'};

declare function local:to-string($node as node()* ) as string
{
  bin:as-string(binary{$node},'UTF-8')
};

declare function local:to-node($str as xs:string ) as node()* 
{ bin:parse( binary{$str},'text/xml') treat as node()*
}; 

declare function local:createSignature($doc as node(),$local_key_file as xs:string,
	$local_keyPwd as xs:string, $local_keyAlias as xs:string ) as node()* {
 let $xmlStr := local:to-string($doc) 
 let $signedDoc := local:signXMLDocument($xmlStr, $local_key_file, $local_keyPwd , $local_keyAlias)
 return local:to-node( substring($signedDoc,39) ) treat as node()*
};
 
 update
 for $pedigree in collection('tig:///ePharma/ShippedPedigree')[*:pedigree/*:receivedPedigree/*:documentInfo/*:serialNumber = $pedId]/*:pedigree

 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
 where not( exists($pedigree/*:Signature) )
 replace  $pedigree  with  $pedigree_sign/pedigree
 
"),

tig:create-stored-procedure("CreateReceivedPedigreeForPedigrees1","
import module namespace util = 'xquery:modules:util';
declare variable $pedRcv as node()* external;
declare variable $signerId as string external;
declare variable $pedigreeId as string external;

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

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $signerId
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')

for $k in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree

let $docid := util:create-uuid()
let $recvid := util:create-uuid()
let $ndc := $k//*:initialPedigree/*:productInfo/*:productCode
where $k/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedigreeId
return
if(tlsp:ndcExists($ndc) ) then
(
tig:insert-document('tig:///ePharma/ShippedPedigree',
<pedigree>
  	<receivedPedigree id='{$docid}'>
	 	<documentInfo>
	  		<serialNumber>{$recvid}</serialNumber>
	  		<version>1</version>
		</documentInfo>
		{$k}
		<receivingInfo>
			<dateReceived>{$date}</dateReceived>
			 {
                           let $all_item_info as node()+ := local:getItemInfo($k) 
			for $item_info in $all_item_info
			let $lot_number as xs:string := xs:string( $item_info/*:lot )
			return <itemInfo>
				<lot>{$lot_number}</lot>
				<expirationDate>{xs:string( $item_info/*:expirationDate )}</expirationDate>
				<quantity>{xs:string( $item_info/*:quantity )}</quantity>
				<itemSerialNumber>{xs:string( $item_info/*:itemSerialNumber )}</itemSerialNumber>
				</itemInfo>
			 
			}
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{$dateTime}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
 ,tlsp:UpDatePedigreeBankDocNew($ndc,$recvid,$pedRcv ),tlsp:CreateSignToReceivedPedigree($recvid))

else
 (
tig:insert-document('tig:///ePharma/ShippedPedigree',
<pedigree>
  	<receivedPedigree id='{$docid}'>
	 	<documentInfo>
	  		<serialNumber>{$recvid}</serialNumber>
	  		<version>1</version>
		</documentInfo>
		{$k}
		<receivingInfo>
			<dateReceived>{$date}</dateReceived>
			 {
                           let $all_item_info as node()+ := local:getItemInfo($k) 
			for $item_info in $all_item_info
			let $lot_number as xs:string := xs:string( $item_info/*:lot )
			return <itemInfo>
				<lot>{$lot_number}</lot>
				<expirationDate>{xs:string( $item_info/*:expirationDate )}</expirationDate>
				<quantity>{xs:string( $item_info/*:quantity )}</quantity>
				<itemSerialNumber>{xs:string( $item_info/*:itemSerialNumber )}</itemSerialNumber>
				</itemInfo>
			 
			}
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{$dateTime}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
,tlsp:createNewPedBankDocNewschema($ndc,$recvid,$pedRcv),tlsp:CreateSignToReceivedPedigree($recvid)) 
"),

tig:create-stored-procedure("InsertAndChangePedigreeStatus1","

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $userid as string external;
declare variable $pedId as string external;
declare variable $status as string external;


let $time := fn:current-dateTime()

let $data := ( for $i in collection ('tig:///ePharma/PedigreeStatus') 
		where $i/PedigreeStatus/PedigreeID = $pedId
		return $i )
return if (empty($data))
	then
	    tig:insert-document('tig:///ePharma/PedigreeStatus', 
		<PedigreeStatus><PedigreeID>{$pedId}</PedigreeID>
		   <Status>
  		   	<StatusChangedOn>{$time}</StatusChangedOn>
			<StatusChangedTo>{$status}</StatusChangedTo>
			<UserId>{$userid}</UserId>
		   </Status>
		   <TimeStamp>{$time}</TimeStamp>
		</PedigreeStatus>
)
       else (tlsp:insertNode(<x> <Status>
  			     	 <StatusChangedOn>{$time}</StatusChangedOn>
   			     	 <StatusChangedTo>{$status}</StatusChangedTo>
  			      	<UserId>{$userid}</UserId>
			      </Status>
			      <TimeStamp>{$time}</TimeStamp></x>,document-uri($data)))
"),

tig:create-stored-procedure("ReconcileGoods","

declare variable $xmlString as string external; 
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

let $pedNode := tlsp:StringToNode($xmlString)
let $pedRcv := $pedNode//PEDRCV
for $i in $pedRcv
let $res := (for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber= data($i/PedigreeID)]
	     return $i)
return
if($res) then tlsp:UpdatePedigreeBankForInitial(data($i/PedigreeID),$i)
else 
(
	 tlsp:CreateReceivedPedigreeForPedigrees1($i,data($i/SignerID) cast as string,data($i/PedigreeID) cast as string), 	 
	 tlsp:InsertAndChangePedigreeStatus1(data($i/SignerID) cast as string,data($i/PedigreeID) cast as string,'Received And Authenticated')
)
"),

tig:create-stored-procedure("UpdateQuantity","
declare variable $swlot as string external;
update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $swlot
replace value of $i/InventoryOnHand/TotalInventory with 0
"),

tig:create-stored-procedure("CloseCallNew","
declare variable $message as string external;

let $pedNode := tlsp:StringToNode($message)/*:*
let $pedclose := (for $pedclose in $pedNode/PEDCLOSE/SWPLot return $pedclose)
for $j in $pedclose
return
tlsp:UpdateQuantity($j)
")




tig:create-stored-procedure("ClearLotQtyforShipping_MD", "
declare variable $ndc as string external;
declare variable $binNum as string external;
declare variable $lot_info as node()* external;

update
for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $ndc]/BinInfo[BinNumber = $binNum]/LotInfo
where $i/LotNumber = data($lot_info/LotInfo/LotNumber) and $i/BankTime =  data($lot_info/LotInfo/BankTime) and $i/TransactionInfo/TransactionId=data($lot_info/LotInfo/TransactionInfo/TransactionId)
replace value of $i/Quantity with  0

"),

tig:create-stored-procedure("UpDateLotQtyforShipping_MD", "
declare variable $ndc as string external;
declare variable $binNum as string external;
declare variable $lot_info as node()* external;
declare variable $qty as integer external;


update
for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $ndc]/BinInfo[BinNumber = $binNum]/LotInfo
where $i/LotNumber = data($lot_info/LotInfo/LotNumber) and $i/Quantity > 0 and $i/BankTime =  data($lot_info/LotInfo/BankTime) and $i/TransactionInfo/TransactionId=data($lot_info/LotInfo/TransactionInfo/TransactionId)
replace value of $i/Quantity with  $i/Quantity - $qty 

"),

tig:create-stored-procedure("UpdatePedigreeBankTotQtyNew","

declare variable $NDC as string external;
declare variable $totQty as integer external;

update
for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $NDC]
replace value of $i/TotalInventory with $i/TotalInventory - $totQty
"),

tig:create-stored-procedure("updatePedigreebankAfterShipmentNew_MD", "
declare variable $ndc as string external;
declare variable $qtyPulled as string external;
declare variable $binNum as string external;
declare variable $product as node()* external;

let $qty as xs:integer := xs:integer($qtyPulled)
let $containerSize  := xs:integer('1')
let $tolQty := $qty * $containerSize
let $prod  := <root>{$product/ProductInfo/node()}</root>

for $l in $product/LotInfo 
return
if($qty < $l/Quantity) then  
tlsp:UpDateLotQtyforShipping_MD($ndc,$binNum,$l,$qty)
 else tlsp:ClearLotQtyforShipping_MD($ndc,$binNum,$l)


"),

tig:create-stored-procedure("GetLotInfo_MD","
declare variable $quantity as string external;
declare variable $ndc as string external;
declare variable $binNum as string external;

let $qty as xs:integer := xs:integer($quantity)
let $lot := (for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $ndc]/BinInfo[BinNumber = $binNum]/LotInfo
		order by $i/BankTime ascending
		return $i)

let $index as xs:integer? := min(
		for $i in 1 to count($lot)
		(:let $all := ( for $q in ($lot/Quantity)[position() <= $i and not ( xs:integer(.) <= 0 )] return $q ):)
		let $all := ($lot/Quantity)[position() <= $i]
		let $ints  := ( for $s in $all  return  xs:integer(data($s)) )
		where sum($ints) >= $qty 
		return   $i
		)
for $q in $lot[position() <= $index]
return if( $q/Quantity >0 ) then <root><LotInfo>{$q/*}</LotInfo><qty>{$qty}</qty></root> else ()

"),

tig:create-stored-procedure("GetLotDetails_MD","

declare variable $NDC as string external;
declare variable $xmlString as node()* external;

let $shipNode := $xmlString/pedship
let $lotDetails := (for $ship_node in $shipNode
let $binNum := data($ship_node/BinLocation)
let $individual_qty := data($ship_node/QtyPulled)
let $k := tlsp:GetLotInfo_MD($individual_qty,$NDC,$binNum)
(:let $pedBankresult := tlsp:updatePedigreebankAfterShipmentNew_MD($NDC,$individual_qty,$binNum,<root>{$k}</root>):)
let $count :=  count ($k)
let $quantity as xs:integer :=xs:integer(sum($k/Quantity) )
let $result := (for $i at $x in $k
return  if( $x = $count )
then (
let $final_qty := xs:integer(($i/Quantity )-( xs:integer(sum($k/Quantity)) - $individual_qty))
let $pbresult :=  tlsp:UpDateLotQtyforShipping_MD($NDC,$binNum,<root>{$i}</root>,$final_qty)
return <itemInfo><lot>{data($i/LotNumber)}</lot><expirationDate>{data($i/ExpirationDate)}</expirationDate><quantity>{$final_qty}</quantity></itemInfo>
)
else (
let $res := tlsp:ClearLotQtyforShipping_MD($NDC,$binNum,<root>{$i}</root>)
return <itemInfo><lot>{data($i/LotNumber)}</lot><expirationDate>{data($i/ExpirationDate)}</expirationDate><quantity>{data($i/Quantity)}</quantity></itemInfo>))
return $result
)
let $lots := <root>{$lotDetails}</root>
let $lotNo := (for $l in distinct-values($lots/itemInfo/lot) return $l)
let $item_info := (for $m in $lotNo
		let $qty := (for $i in $lots/itemInfo[lot = $m]  return xs:integer($i/quantity))
	         return <itemInfo><lot>{$m}</lot><expirationDate>{distinct-values(for $i in $lots/itemInfo[lot=$m] return data($i/expirationDate))}</expirationDate><quantity>{sum($qty)}</quantity></itemInfo>)
return $item_info
"),
 
tig:create-stored-procedure("CreateShippedPedigree_MDNew","
import module namespace util = 'xquery:modules:util';
declare variable $xmlString as node()* external;
declare variable $signerId as string external;
declare variable $deaNumber as string external;
declare variable $sourceRoutingCode as string external;

let $pedshipdata := $xmlString/pedshipData/ndc
let $ind_pedship := (for $i in $pedshipdata return $i/pedship)[1]
let $values := tlsp:TPCustomization($ind_pedship/CustomerDea)

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $md := (for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner where $i/deaNumber = $deaNumber return $i)
let $envid := concat('urn:uuid:',util:create-uuid())
let $pedigree_data := <Result>{(for $pedship_ndc in $pedshipdata
let $initial_lot := (for $l in $pedship_ndc/lots/LotInfo return <itemInfo><lot>{data($l/LotNumber)}</lot><expirationDate>{data($l/ExpirationDate)}</expirationDate><quantity>{data($l/InitialQuantity)}</quantity> 
		    </itemInfo>)

let $lotNo := (for $l in distinct-values($pedship_ndc/lots/itemInfo/lot) return $l)
let $shipped_lot := (for $m in $lotNo
		let $qty := (for $i in $pedship_ndc/lots/itemInfo[lot = $m ]  return xs:integer($i/quantity))
	         return <itemInfo><lot>{$m}</lot><expirationDate>{distinct-values(for $i in $pedship_ndc/lots/itemInfo[lot=$m] return data($i/expirationDate))}</expirationDate><quantity>{($qty)[1]}</quantity></itemInfo>)


let $shipNode := $pedship_ndc/pedship

let $tot_qty := sum(for $lot_info in $pedship_ndc/lots/itemInfo return xs:integer(data($lot_info/quantity)))
let $NDC := data($shipNode[1]/NDC)
let $binNum := data($shipNode/BinLocation)

let $productInfo := (for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $NDC]
		   return $i/ProductInfo)
let $productNode := (<productInfo><drugName>{data($productInfo/drugName)}</drugName><manufacturer>{data($productInfo/Manufacturer)}</manufacturer>
	   	   <productCode type='NDC'>{$NDC}</productCode><dosageForm>{data($productInfo/dosageForm)}</dosageForm>
    	            <strength>{data($productInfo/strength)}</strength><containerSize>{data($productInfo/containerSize)}</containerSize></productInfo> )
let $transInfo := (for $l in $pedship_ndc/lots/LotInfo return $l/TransactionInfo)[1]
let $recDate :=  (for $l in $pedship_ndc/lots/LotInfo return $l/ReceivingInfo)[1]
let $mfgrInfo := (<senderInfo><businessAddress><businessName>{data($transInfo/Address/businessName)}</businessName>
		<street1>{data($transInfo/Address/street1)}</street1><street2>{data($transInfo/Address/street2)}</street2><city>{data($transInfo/Address/city)}</city><stateOrRegion>{data($transInfo/Address/stateOrRegion)}</stateOrRegion>
		<postalCode>{data($transInfo/Address/postalCode)}</postalCode><country>{data($transInfo/Address/country)}</country></businessAddress>
		<licenseNumber>{data($transInfo/Address/licenseNumber)}</licenseNumber>
		<contactInfo><name>{data($transInfo/Address/contactName)}</name><telephone>{data($transInfo/Address/telephone)}</telephone><email>{data($transInfo/Address/email)}</email></contactInfo>
		</senderInfo> )
let $totInven := tlsp:UpdatePedigreeBankTotQtyNew($NDC,$tot_qty)
(:let $lotDetails := tlsp:GetLotDetails_MD($NDC,<root>{$shipNode}</root>):)
let $senderinfo := (
	<senderInfo><businessAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/businessAddress/line1)}</street1><street2>{data($md/businessAddress/line2)}</street2><city>{data($md/businessAddress/city)}</city><stateOrRegion>{data($md/businessAddress/state)}</stateOrRegion>
	<postalCode>{data($md/businessAddress/zip)}</postalCode><country>{data($md/businessAddress/country)}</country></businessAddress>
	<shippingAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/shippingAddress/line1)}</street1><street2>{data($md/shippingAddress/line2)}</street2><city>{data($md/shippingAddress/city)}</city><stateOrRegion>{data($md/shippingAddress/state)}</stateOrRegion>
	<postalCode>{data($md/shippingAddress/zip)}</postalCode><country>{data($md/shippingAddress/country)}</country></shippingAddress>
	<licenseNumber>{data($md/deaNumber)}</licenseNumber>
	<contactInfo><name>{data($md/contact)}</name><telephone>{data($md/phone)}</telephone><email>{data($md/email)}</email>
	</contactInfo>
	</senderInfo>
)
let $recipientinfo := (
	<recipientInfo><businessAddress><businessName>{distinct-values(data($shipNode/CustomerName))}</businessName>
	<street1>{distinct-values(data($shipNode/CustomerAddressLine1))}</street1><street2>{distinct-values(data($shipNode/CustomerAddressLine2))}</street2>
	<city>{distinct-values(data($shipNode/CustomerCity))}</city><stateOrRegion>{distinct-values(data($shipNode/State))}</stateOrRegion>
	<postalCode>{distinct-values(data($shipNode/CustomerZip))}</postalCode><country>{distinct-values(data($shipNode/Country))}</country>
	</businessAddress>
	<shippingAddress><businessName>{distinct-values(data($shipNode/ShipToCustomerName))}</businessName>
	<street1>{distinct-values(data($shipNode/ShipToCustomerAddressLine1))}</street1><street2>{distinct-values(data($shipNode/ShipToCustomerAddressLine2))}</street2>
	<city>{distinct-values(data($shipNode/ShipToCustomerCity))}</city><stateOrRegion>{distinct-values(data($shipNode/ShipToState))}</stateOrRegion>
	<postalCode>{distinct-values(data($shipNode/ShipToCustomerZip))}</postalCode><country>{distinct-values(data($shipNode/ShipToCountry))}</country>
	</shippingAddress>
	<licenseNumber>{distinct-values(data($shipNode/CustomerDea))}</licenseNumber>
	</recipientInfo>)

let $signerInfo := (for $y in collection('tig:///EAGRFID/SysUsers')/User where $y/UserID = $signerId
		 return <signerInfo><name>{concat(data($y/FirstName),data($y/LastName))}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
			<email>{data($y/Email)}</email></signerInfo>)

let $docid := concat('urn:uuid:',util:create-uuid())
let $shipid := concat('_',util:create-uuid())
let $contid :=  distinct-values(data($shipNode/UniqueBoxID))

let $container as node()+ := (<pedigreeHandle><serialNumber>{$docid}</serialNumber></pedigreeHandle>)
let $TPStatus := (for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner where $i/deaNumber = data($shipNode/CustomerDea) return $i)
return if(exists($TPStatus)) then
(
<pedigree>
  <shippedPedigree id='{$shipid}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>20060331</version>
	</documentInfo>
	<initialPedigree>
	   {$productNode}
	   {$initial_lot}
	    <transactionInfo>
	   	{$mfgrInfo}
	         <recipientInfo>{$senderinfo/child::*}
		</recipientInfo>
	   	<transactionIdentifier>
			<identifier>{data($transInfo/TransactionId)}</identifier><identifierType>{data($transInfo/TransactionType)}</identifierType>
	         </transactionIdentifier>
		<transactionType>Sale</transactionType>
	   	<transactionDate>{data($transInfo/TransactionDate)}</transactionDate>
	    </transactionInfo>
	    <receivingInfo><dateReceived>{data($recDate/ReceivedDate)}</dateReceived></receivingInfo>
	 </initialPedigree>
	{$shipped_lot}
 	<transactionInfo>
	<senderInfo><businessAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/businessAddress/line1)}</street1><street2>{data($md/businessAddress/line2)}</street2><city>{data($md/businessAddress/city)}</city><stateOrRegion>LA</stateOrRegion>
	<postalCode>{data($md/businessAddress/zip)}</postalCode><country>{data($md/businessAddress/country)}</country></businessAddress>
	<shippingAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/shippingAddress/line1)}</street1><street2>{data($md/shippingAddress/line2)}</street2><city>{data($md/shippingAddress/city)}</city><stateOrRegion>{data($md/shippingAddress/state)}</stateOrRegion>
	<postalCode>{data($md/shippingAddress/zip)}</postalCode><country>{data($md/shippingAddress/country)}</country></shippingAddress>
	<licenseNumber>{data($md/deaNumber)}</licenseNumber>
	<contactInfo><name>{data($md/contact)}</name><telephone>{data($md/phone)}</telephone><email>{data($md/email)}</email></contactInfo>
	</senderInfo>
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{distinct-values(data($shipNode/PONumber))}</identifier><identifierType>PurchaseOrderNumber</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{distinct-values(data($shipNode/InvoiceDate))}</transactionDate>
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
</pedigree>,$container )else ())}</Result>
return if(exists($pedigree_data/pedigree)) then
let $pedEnv := (<pedigreeEnvelope>
<version>20060331</version>
<serialNumber>{$envid}</serialNumber>
<date>{$date}</date>
<sourceRoutingCode>{$sourceRoutingCode}</sourceRoutingCode><destinationRoutingCode>{data($md/destinationRoutingCode)}</destinationRoutingCode>
<container><containerCode>{if($values/containerCode = 'PONumber') then distinct-values(data($ind_pedship/PONumber)) 
else if($values/shipmentHandle = 'InvoiceNumber') then distinct-values(data($ind_pedship/InvoiceNo))
else 'NA'}</containerCode>
<shipmentHandle>{if($values/shipmentHandle = 'PONumber') then distinct-values(data($ind_pedship/PONumber)) 
else if($values/shipmentHandle = 'InvoiceNumber') then distinct-values(data($ind_pedship/InvoiceNo))
else 'NA'}</shipmentHandle>
{$pedigree_data/pedigreeHandle}
</container>
{$pedigree_data/pedigree}
</pedigreeEnvelope> 
) 
(:let $res := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pedEnv)
return $envid :) return $pedEnv
else ()

"),
 
tig:create-stored-procedure("PEDSHIP_MD","
declare variable $shipNode as node()* external;
declare variable $signerId as string external;
declare variable $deaNumber as string external;
declare variable $sourceRoutingCode as string external;

(:<pedigreeEnvelope xmlns=""urn:epcGlobal:PedigreeEnvelope:xsd:1"">{ $shipNode/pedshipData}</pedigreeEnvelope> :)
for $ship_data in $shipNode/*:pedshipData
let $final_shipdata := tlsp:ReformatingPedShipData($ship_data)
let $ship := tlsp:ReformatingPedShipDataNew(<root>{$final_shipdata}</root>)

let $result := tlsp:CreateShippedPedigree_MDNew(<root>{$ship}</root>,$signerId,$deaNumber,$sourceRoutingCode)
return (for $i in $ship return if($i/name() ='status') then concat('Insufficient Quantity',',',$i/NDC/text(),',',$i/bin/text()) else (),$result)
 
"),
tig:create-stored-procedure("ReformatingPedShipData", "
declare variable $pedshipdata as node()* external;

let $i := $pedshipdata
 
return
<pedshipData>
 {
 for $j in distinct-values($i/pedship/NDC)
 return
 <ndc>{
  for $ped in $i/pedship [NDC = $j]
  return $ped
  }
 </ndc>
}
</pedshipData>"),

tig:create-stored-procedure("ReformatingPedShipDataNew", "
declare variable $xmlString as node()* external;

let $pedshipdata := $xmlString/pedshipData/ndc
for $pedship_ndc in $pedshipdata
let $shipNode := $pedship_ndc/pedship
let $NDC := data($shipNode[1]/NDC)
let $binN := distinct-values(data($shipNode/BinLocation))
for $bin_nums  in $binN
let $binNum  := xs:string($bin_nums)
let $tot_qty := sum(for $lot_info in $shipNode  where $lot_info/BinLocation = $binNum return xs:integer(data($lot_info/QtyPulled)))
let $lots1 :=  (tlsp:GetLotInfo_MD(xs:string($tot_qty),$NDC,$binNum))
let $count := count($lots1/LotInfo)
let $lots := for $j in (1 to $count) return $lots1/LotInfo[$j]

return
if(exists($lots)) then 

let $count :=  count ($lots)
let $quantity as xs:integer :=xs:integer(sum($lots/Quantity) )
let $result := (for $i at $x in $lots
	       return  if( $x = $count )
		      then (
			let $final_qty := xs:integer(($i/Quantity )-( xs:integer(sum($lots/Quantity)) - $tot_qty))
			let $pbresult :=  (tlsp:UpDateLotQtyforShipping_MD($NDC,$binNum,<root>{$i}</root>,$final_qty))
			return <itemInfo><lot>{data($i/LotNumber)}</lot><expirationDate>{data($i/ExpirationDate)}</expirationDate><quantity>{$final_qty}</quantity><transactionId>{data($i/TransactionInfo/TransactionId)}</transactionId></itemInfo>
			)
		       else (	
			let $res := tlsp:ClearLotQtyforShipping_MD($NDC,$binNum,<root>{$i}</root>)
			return <itemInfo><lot>{data($i/LotNumber)}</lot><expirationDate>{data($i/ExpirationDate)}</expirationDate><quantity>{data($i/Quantity)}</quantity><transactionId>{data($i/TransactionInfo/TransactionId)}</transactionId></itemInfo>))
let $lots1 := <root>{$result}</root>
let $lotNo := (for $l in distinct-values($lots1/itemInfo/lot)  return $l )
let $transNo := (for $l in distinct-values($lots1/itemInfo/transactionId)  return $l)
let $item_info := (
		for $m in $lotNo
		for $t in $transNo
		let $qty := (for $i in $lots1/itemInfo[lot = $m and transactionId = $t]  return xs:integer($i/quantity))
	         return <itemInfo><lot>{$m}</lot><expirationDate>{distinct-values(for $i in $lots1/itemInfo[lot=$m] return data($i/expirationDate))}</expirationDate><quantity>{sum($qty)}</quantity>
<transactionId>{distinct-values(for $i in $lots1/itemInfo[lot=$m and transactionId = $t] return data($i/transactionId))}</transactionId></itemInfo>)

return
<pedshipData>
 {
 for $j in distinct-values($lots/TransactionInfo/TransactionId)
 return
 <ndc>
  {$pedship_ndc/pedship}{
   for $l in $lots[TransactionInfo/TransactionId = $j]
  return<lots>{$l} {for $i in $item_info where $i/lot = $l/LotNumber and $i/transactionId = $l/TransactionInfo/TransactionId return $i}</lots>
  }
 </ndc>
}
</pedshipData>
else (<status><NDC>{$NDC}</NDC><bin>{$binNum}</bin></status>)

"),

tig:create-stored-procedure("InsertShippedPedigree","
declare variable $envNode as node()* external;
declare variable $fileName as string external;

let $envId := data($envNode/*:serialNumber)
let $result := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$envNode)
let $sign := tlsp:CreateSignatureToEnvelope_MD($envId)
return $envId
"),

tig:create-stored-procedure("InsertProcessedEnvelope","
declare variable $envIds as node()* external;
declare variable $fileName as string external;

tig:insert-document('tig:///ePharma_MD/ProcessedEnvelope',
			<ProcessedEnvelope>
				<fileName>{$fileName}</fileName>
				{$envIds/envelopeId}
			</ProcessedEnvelope>)
"),

tig:drop-stored-procedure("CreatePedigreeNodesForDropShip"),
tig:create-stored-procedure("CreatePedigreeNodesForDropShip","

import module namespace util = 'xquery:modules:util';
declare variable $xmlNode as node()* external;
declare variable $signerid as string external;
declare variable $deanumber as string external; 


let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $signerid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email>
</signerInfo>

let $md := (for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner where $i/deaNumber = $deanumber return $i)


let $all_peds := 
  (
  <Result>{
  for $shipNode in $xmlNode/DS 
    let $docid := concat('urn:uuid:',util:create-uuid()) 
    let $shipId := concat('_',util:create-uuid())	
    let $all_containers := (
		
	let $container := (<container><containerCode>{data($shipNode/PONumber)}</containerCode><shipmentHandle>{data($shipNode/PONumber)}</shipmentHandle><pedigreeHandle><serialNumber>{$docid}</serialNumber>
	</pedigreeHandle></container>) 
	return $container
    )

  let $quantity := data($shipNode/Quantity)
  let $NDC := data($shipNode/ProductCode)

  let $productNode := (<productInfo><drugName>{data($shipNode/DrugName)}</drugName><manufacturer>{data($shipNode/ManufacturerName)}</manufacturer>
	    	   <productCode type='NDC'>{$NDC}</productCode><dosageForm>{data($shipNode/Dosage)}</dosageForm>
    	            <strength>{data($shipNode/Strength)}</strength><containerSize>{data($shipNode/ContainerSize)}</containerSize></productInfo> )


let $mfgrInfo := (<senderInfo><businessAddress><businessName>{data($shipNode/ManufacturerName)}</businessName>
		 <street1>{data($shipNode/ManuAdd1)}</street1><street2>{data($shipNode/ManuAdd2)}</street2><city>{data($shipNode/ManuCity)}</city><stateOrRegion>{data($shipNode/ManuState)}</stateOrRegion>
		<postalCode>{data($shipNode/ManuZip)}</postalCode><country>{data($shipNode/ManuCountry)}</country></businessAddress>
		<licenseNumber>{data($shipNode/*:ManuDEA)}</licenseNumber>
		<contactInfo><name>{data($shipNode/ManContactName)}</name><telephone>{data($shipNode/ManContactPhone)}</telephone>
		<email>{data($shipNode/ManContactEmail)}</email>
		</contactInfo>
		</senderInfo> )

let $senderinfo := (
	<senderInfo><businessAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/businessAddress/line1)}</street1><street2>{data($md/businessAddress/line2)}</street2><city>{data($md/businessAddress/city)}</city><stateOrRegion>LA</stateOrRegion>
	<postalCode>{data($md/businessAddress/zip)}</postalCode><country>{data($md/businessAddress/country)}</country></businessAddress>
	<shippingAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/shippingAddress/line1)}</street1><street2>{data($md/shippingAddress/line2)}</street2><city>{data($md/shippingAddress/city)}</city><stateOrRegion>LA</stateOrRegion>
	<postalCode>{data($md/shippingAddress/zip)}</postalCode><country>{data($md/shippingAddress/country)}</country></shippingAddress>
	<licenseNumber>{data($md/deaNumber)}</licenseNumber>
	<contactInfo>
		<name>{data($md/contact)}</name>
		<telephone>{data($md/phone)}</telephone>
		<email>{data($md/email)}</email>
	</contactInfo>
	</senderInfo>
)
let $recipientinfo := (
	<recipientInfo><businessAddress><businessName>{data($shipNode/Custname)}</businessName>
	<street1>{data($shipNode/CustBillAdd1)}</street1>
	<street2>{data($shipNode/CustBillAdd2)}</street2>
	<city>{data($shipNode/CustBillAddCity)}</city><stateOrRegion>{data($shipNode/CustBillState)}</stateOrRegion>
	<postalCode>{data($shipNode/CustBillZip)}</postalCode><country>{data($shipNode/CustBillCountry)}</country>
	</businessAddress><shippingAddress><businessName>{data($shipNode/Custname)}</businessName>
	<street1>{data($shipNode/CustshipAdd1)}</street1>
	<street2>{data($shipNode/CustshipAdd2)}</street2>
	<city>{data($shipNode/CustShiAddCity)}</city><stateOrRegion>{data($shipNode/CustShipAddState)}</stateOrRegion>
	<postalCode>{data($shipNode/CustShipzip)}</postalCode><country>{data($shipNode/CustShipcountry)}</country>
	</shippingAddress>
	<licenseNumber>{data($shipNode/CustDEA)}</licenseNumber>
	</recipientInfo>)

let $ped := (
<pedigree>
  <shippedPedigree id='{$shipId}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>20060331</version>
	</documentInfo>
	<initialPedigree>
	   <productInfo>
		   <drugName>{data($shipNode/DrugName)}</drugName><manufacturer>{data($shipNode/ManufacturerName)}</manufacturer>
	   	   <productCode type='NDC'>{$NDC}</productCode><dosageForm>{data($shipNode/Dosage)}</dosageForm>
    	            <strength>{data($shipNode/Strength)}</strength><containerSize>{data($shipNode/ContainerSize)}</containerSize>
	   </productInfo> 
	   <itemInfo>
	     <lot>{data($shipNode/LotNo)}</lot> 
	     <expirationDate>{data($shipNode/ExpDate)}</expirationDate> 
	     <quantity>{data($shipNode/Quantity)}</quantity> 
	    </itemInfo>
	    <transactionInfo>
	   	{$mfgrInfo}
	         <recipientInfo><businessAddress>{$senderinfo/businessAddress//*}</businessAddress><shippingAddress>{$senderinfo/shippingAddress//*}</shippingAddress></recipientInfo>
	   	<transactionIdentifier>
			<identifier>{data($shipNode/PONumber)}</identifier><identifierType>PurchaseOrderNumber</identifierType>
	         </transactionIdentifier>
		<transactionType>Sale</transactionType>
	   	<transactionDate>{data($shipNode/PODate)}</transactionDate>
	    </transactionInfo>	   
	 </initialPedigree>
	<itemInfo>			
		<lot>{data($shipNode/LotNo)}</lot>
		<expirationDate>{data($shipNode/ExpDate)}</expirationDate>
		<quantity>{$quantity}</quantity>
	</itemInfo>
 	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{data($shipNode/CustPONumber)}</identifier><identifierType>PurchaseOrderNumber</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{data($shipNode/CustPODate)}</transactionDate>
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
</pedigree> )
 return ($ped,$all_containers)
 }</Result>
  )
let $final_containers := <container>{($all_peds/container/containerCode)[1]}{($all_peds/container/shipmentHandle)[1]}{$all_peds/container/pedigreeHandle}</container>
return ( $final_containers ,$all_peds/pedigree )
 "),

tig:drop-stored-procedure("CreateShippedPedigreeForDropShip_MD1"),
tig:create-stored-procedure("CreateShippedPedigreeForDropShip_MD1","
import module namespace util = 'xquery:modules:util';
declare variable $xmlNode as node()* external;
declare variable $signerid as string external;
declare variable $deanumber as string external;
declare variable $sourceRoutingCode as string external;


let $ind_pedship := (for $i in $xmlNode return $i/DS)[1]
let $drc := (for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner
where $i/deaNumber = $ind_pedship/CustDEA
return data($i/destinationRoutingCode))

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $envid := concat('urn:uuid:',util:create-uuid())

let $shipid := concat('_',util:create-uuid())

let $pedEnv  := (<pedigreeEnvelope>
<version>20060331</version>
<serialNumber>{$envid}</serialNumber>
<date>{$date}</date>

<sourceRoutingCode>{$sourceRoutingCode}</sourceRoutingCode><destinationRoutingCode>{$drc}</destinationRoutingCode>
{tlsp:CreatePedigreeNodesForDropShip($xmlNode, $signerid, $deanumber)}
</pedigreeEnvelope>
)

return $pedEnv
(:let $res := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pedEnv)
 for $envId in doc($res) return data($envId/*:pedigreeEnvelope/*:serialNumber) :)

"),

tig:create-stored-procedure("RetrievePedTradingPartnerInfo", "

declare variable $deaNumber as string external;

<root>{
for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner where $i/deaNumber = $deaNumber
return ($i/notificationDescription,
       $i/notificationInfo,
       $i/localFolder)          
}</root>")

 
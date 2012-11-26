
tig:drop-stored-procedure("CreateShippedPedigreeRepacked_MD"),
tig:create-stored-procedure("CreateShippedPedigreeRepacked_MD","

declare variable $despatchAdviseNo as string external;
declare variable $transactionType as string external;
declare variable $flag as string external;
declare variable $pedigree as string external; 
declare variable $xmlString as string external;

import module namespace util = 'xquery:modules:util';

declare function local:getPartnerInfo( $name as string) as node()*
{
let $k := (for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
where $i/name = $name
return 
<root>{$i/partnerType}{$i/businessId}{$i/address}
</root>
)	
return
$k	
};
let $pedNode := tlsp:StringToNode_MD($xmlString)
let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $pedNode//signerId/text()
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>


let $envid := util:create-uuid()
let $pdenv := (for $k in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice
where $k/ID = $despatchAdviseNo
return <pedigreeEnvelope>
<version>1</version>
<serialNumber>{$envid}</serialNumber>
<date>{$date}</date>
<sourceRoutingCode>{data($k/SellerParty/Party/PartyName/Name)}</sourceRoutingCode><destinationRoutingCode>{data($k/BuyerParty/Party/PartyName/Name)}</destinationRoutingCode>

{
for $s in (
<x> {for $NDC in data($k/DespatchLine/Item/SellersItemIdentification/ID)

let $quantity1 :=(for $x in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice
	        where $x/ID = $despatchAdviseNo 
	        and $x/DespatchLine/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DespatchLine/DeliveredQuantity)
	)
let $lotexp1 :=(  for $i in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where $i/Description = 'LOTEXP'
		and $i/../../../../ID = $despatchAdviseNo
		return 
		data( $i/../ID)
	     )
let $sscc1 := (for $i in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification
where $i/PhysicalAttribute/Description = 'SSCC'
and  $i/../../../ID = $despatchAdviseNo
return
data( $i/ID)
)

let $sellerInfo := local:getPartnerInfo(data($k/SellerParty/Party/PartyName/Name))
let $buyerInfo := local:getPartnerInfo(data($k/BuyerParty/Party/PartyName/Name))

let $senderinfo := (
	for $k in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice
	where $k/ID = $despatchAdviseNo
	return <senderInfo><businessAddress><businessName>{data($k/SellerParty/Party/PartyName)}</businessName>
	<street1>{data($k/SellerParty/Party/Address/StreetName)}</street1><street2>{data($k/SellerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/SellerParty/Party/Address/CityName)}</city><stateOrRegion>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</stateOrRegion>
	<postalCode>{data($k/SellerParty/Party/Address/PostalZone)}</postalCode><country>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</country>
	</businessAddress>
	<shippingAddress><businessName>{data($k/SellerParty/ShippingContact/Name)}</businessName>
	<street1>{data($k/SellerParty/Party/Address/StreetName)}</street1><street2>{data($k/SellerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/SellerParty/Party/Address/CityName)}</city><stateOrRegion>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</stateOrRegion>
	<postalCode>{data($k/SellerParty/Party/Address/PostalZone)}</postalCode>
	<country>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</country></shippingAddress>
	<licenseNumber state='{data($sellerInfo/address/state)}' agency=''>{data($sellerInfo/businessId)}</licenseNumber>
	<contactInfo><name>{data($k/SellerParty/ShippingContact/Name)}</name><title>{data($k/SellerParty/ShippingContact/Title)}</title>
	<telephone>{data($k/SellerParty/ShippingContact/Telephone)}</telephone><email>{data($k/SellerParty/ShippingContact/E-Mail)}</email>
	<url>{data($k/SellerParty/ShippingContact/E-Mail)}</url></contactInfo>
	</senderInfo>
)

let $recipientinfo := (
	for $k in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice
	where $k/ID = $despatchAdviseNo
	return <recipientInfo><businessAddress><businessName>{data($k/BuyerParty/Party/PartyName)}</businessName>
	<street1>{data($k/BuyerParty/Party/Address/StreetName)}</street1><street2>{data($k/BuyerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/BuyerParty/Party/Address/CityName)}</city><stateOrRegion>{data($k/BuyerParty/Party/Address/CountrySubentityCode)}</stateOrRegion>
	<postalCode>{data($k/BuyerParty/Party/Address/PostalZone)}</postalCode><country>{data($k/BuyerParty/Party/Address/CountrySubentityCode)}</country>
	</businessAddress>
	<shippingAddress><businessName>{data($k/BuyerParty/BuyerContact/Name)}</businessName>
	<street1>{data($k/BuyerParty/Party/Address/StreetName)}</street1><street2>{data($k/BuyerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/BuyerParty/Party/Address/CityName)}</city><stateOrRegion>{data($k/BuyerParty/Party/Address/CountrySubentityCode)}</stateOrRegion>
	<postalCode>{data($k/BuyerParty/Party/Address/PostalZone)}</postalCode><country>{data($k/BuyerParty/Party/Address/CountrySubentityCode)}</country></shippingAddress>
	<licenseNumber state='{data($buyerInfo/address/state)}' agency=''>{data($buyerInfo/businessId)}</licenseNumber>
	<contactInfo><name>{data($k/BuyerParty/BuyerContact/Name)}</name><title>{data($k/BuyerParty/BuyerContact/Title)}</title>
	<telephone>{data($k/BuyerParty/BuyerContact/Telephone)}</telephone><email>{data($k/BuyerParty/BuyerContact/E-Mail)}</email>
	<url>{data($k/BuyerParty/BuyerContact/E-Mail)}</url></contactInfo>
	</recipientInfo>
)
let $lotno1 :=( for $i in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where( $i/Description = 'LOTNO'
		and $i/../../../../ID =  $despatchAdviseNo  )
		return 
		data ($i/../ID)
	     )
let $lotno := $lotno1
let $sscc := $sscc1
let $lotexp := $lotexp1
let $quantity := $quantity1
let $docid := util:create-uuid()
let $shipid := util:create-uuid()
let $contid := util:create-uuid()
let $transDate := for $n in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice[ID = $despatchAdviseNo]
		return data($n/IssueDate)

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber></itemSerialNumber><productCode>{$NDC}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>

</pedigreeHandle></container>)

return ($container,
  
<pedigree>
  <shippedPedigree id='{$shipid}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>1</version>
	</documentInfo>
{if($flag = '1')then	
	$pedigree
else 
if($flag = '2') then <unsignedReceivedPedigree/>
else
if($flag = '3') then tlsp:CreatedRepackagedElement_MD($pedigree,$xmlString)
else tlsp:GetRPDetails_MD($pedigree)
  
}
		<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{$despatchAdviseNo}</identifier><identifierType>{$transactionType}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{$date}</transactionDate>
	</transactionInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</pedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment_MD($envid) :)
return $envid

")


(: To Display Alert Message :)
tig:create-stored-procedure("DisplayMessage","

declare variable $sessionid as string external;

let $i := for $x in collection('tig:///EAGRFID/SysSessions')/session
 where $x/sessionid = $sessionid
return $x  
return 
(
let $j := 
for $y in collection('tig:///EAGRFID/SysUsers')/User 
where $y/UserID = $i/userid 
return $y 
return 
( 
for $l in collection('tig:///ePharma/Alerts')/AlertMessage 
where $l/TargetUserId = $j/UserID 
order by $l/RelatedProcess 
return  <output> 
 <MessageTitle>{data($l/Message/MessageTitle)}</MessageTitle> 
 <MessageID>{data($l/MessageID)}</MessageID> 
 <CreatedDate>{data($l/CreatedDate)}</CreatedDate> 
 <Status>{data($l/Status)}</Status> 
 <SeverityLevel>{data($l/Message/SeverityLevel)}</SeverityLevel> 
 <CreatedBy>{data($l/CreatedBy)}</CreatedBy> 
 <RelatedProcess>{data($l/RelatedProcess)}</RelatedProcess>
 </output> 
)
)
"),

(: To Delete Alert Message :)
tig:create-stored-procedure("DeleteMessage",
"
declare variable $MessageID as string external;
for $i in collection('tig:///ePharma/Alerts') 
where $i/AlertMessage/MessageID = $MessageID 
return tig:delete-document(document-uri($i))
"),

(: To Create an Alert Message :)
tig:create-stored-procedure("CreateMessage",
"
declare variable $Date as string external;
declare variable $CreatedBy as string external;
declare variable $MessageID as string external;
declare variable $GroupName as string external;
declare variable $UserId as string external;
declare variable $Process as string external;
declare variable $Title as string external;
declare variable $DocType as string external;
declare variable $DocId as string external;
declare variable $SLevel as string external;
declare variable $Action as string external;
declare variable $Comments as string external;
declare variable $Status as string external;

tig:insert-document('tig:///ePharma/Alerts', <AlertMessage><CreatedDate>{$Date}</CreatedDate><CreatedBy>{$CreatedBy}</CreatedBy><MessageID>{$MessageID}</MessageID><GroupName>{$GroupName}</GroupName><TargetUserId>{$UserId}</TargetUserId><RelatedProcess>{$Process}</RelatedProcess><Message><MessageTitle>{$Title}</MessageTitle><DocType>{$DocType}</DocType><DocId>{$DocId}</DocId><SeverityLevel>{$SLevel}</SeverityLevel><RequiredAction>{$Action}</RequiredAction><Comments>{$Comments}</Comments></Message><Status>{$Status}</Status></AlertMessage>)
" ),

tig:create-stored-procedure("BuyersID",
"
declare variable $ASNNum as string external;
for $l in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
where $l/ID = $ASNNum 
return data($l/OrderReference/BuyersID) 
")
,

tig:create-stored-procedure("TPEmailID",
"
declare variable $documentID as string external;
distinct-values(
for $i in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where $i/serialNumber = $documentID
return 
let $num := data($i/pedigree/shippedPedigree/transactionInfo/recipientInfo/licenseNumber) 
	for $j in collection('tig:///CatalogManager/TradingPartner')/TradingPartner 
	where data($i/pedigree/shippedPedigree/transactionInfo/recipientInfo/licenseNumber)  = data($j/businessId) 
	return 
		if(fn:exists(data($j/notifyURI))) 
		then  (data($j/notifyURI)) else (data($j/email)))
"),

(: For Validate Access Privileges :)
tig:create-stored-procedure("validateAccess",
"

declare variable $sessId as string external;
declare variable $acLevel as string external;
declare variable $type as string external;

if( contains(
<op>{
for $t in
(for $i in collection('tig:///EAGRFID/SysSessions')/session[sessionid=$sessId]
	return 
	   for $j in collection('tig:///EAGRFID/SysUsers')/User[UserID=$i/userid]
		return $j/AccessLevel/Access)
return 

for $g in collection('tig:///EAGRFID/SysGroups')/Group[GroupID=$t]
return 

for $t in $g/Permissions/Permission
return 

if(($t/ModuleID)=$acLevel) then

	if ($type = 'Insert') then
	      data($t/Insert)
	else if ($type='Update') then
	      data($t/Update)
	else if ($type='Delete') then
	      data($t/Delete)
	else if ($type='Read') then
	      data($t/Read)
	else 'false'


else 'false'


}</op>, 'true')) then

'true'
else
'false'

"),

tig:create-stored-procedure('updateLastUse',"
declare variable $sesId as string external;

update 
for $d in collection('tig:///EAGRFID/SysSessions')/session[sessionid=$sesId]
replace value of $d/lastuse with fn:current-dateTime()
"),

(: For Validating Session :)
tig:create-stored-procedure('validateSession',"

declare variable $sesId as string external;

let $D :=
( for $s in collection('tig://root/EAGRFID/SysSessions/') 
where $s/session/sessionid = $sesId
return fn:get-days-from-dayTimeDuration
(xs:dateTime(fn:current-dateTime())- xs:dateTime($s/session/lastuse)) 
)

let $H :=
( for $s1 in collection('tig://root/EAGRFID/SysSessions/') 
where $s1/session/sessionid = $sesId
return fn:get-hours-from-dayTimeDuration 
(xs:dateTime(fn:current-dateTime())- xs:dateTime($s1/session/lastuse)) 
)

let $M :=
( for $s2 in collection('tig://root/EAGRFID/SysSessions/') 
where $s2/session/sessionid = $sesId
return fn:get-minutes-from-dayTimeDuration
(xs:dateTime(fn:current-dateTime())- xs:dateTime($s2/session/lastuse)) 
)

return 

if ($D=0 and $H=0 and $M<15) then
(
tlsp:updateLastUse($sesId)
)
else
(
'false'
)

"),

tig:create-stored-procedure('ValidateStatus',"

declare variable $sessionId as string external;
declare variable $acclevel as string external;
declare variable $type as string external;

<ValidateStatus>

<SessionStatus> {
   	if (tlsp:validateSession($sessionId)='false') then
	'false'
	else
	'true'
   }
</SessionStatus>


<AccessStatus> {tlsp:validateAccess($sessionId,$acclevel,$type)} </AccessStatus>

</ValidateStatus>

"),

(: EAG Time Stamp :)
tig:create-stored-procedure("CreateEAG-TimeStampType","
let $id := for $i in collection('tig:///EAGRFID/Repository')/Repository[IPAddress=local:ServerIP()]
return data($i/RepositoryID)
return
<EAG-TimeStamp>
<origin-serverID>{$id}</origin-serverID>
<updated-timestamp>2005-10-05T06:02:25</updated-timestamp>
<updated-serverID>{$id}</updated-serverID>
<reported>false</reported>
</EAG-TimeStamp>
"),

tig:create-stored-procedure("GetEAG-TimeStampTypeForRepository","
declare variable $orgid as string external;
let $id := for $i in collection('tig:///EAGRFID/Repository')/Repository[IPAddress=local:ServerIP()]
return data($i/RepositoryID)

return
<EAG-TimeStamp>
<origin-serverID>{$orgid}</origin-serverID>
<updated-timestamp>2005-10-05T06:02:25</updated-timestamp>
<updated-serverID>{$id}</updated-serverID>
<reported>false</reported>
</EAG-TimeStamp>
"),


(: View Pedigree Details :)
tig:create-stored-procedure("PedigreeDetails","
declare variable $TransactionNumber as string external ;
let $all :=
for $b in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where $b/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier = $TransactionNumber
order by $b/serialNumber descending
return $b
let $recent := $all[1]
let $peid := data($recent/serialNumber)
let $status := (for $i in collection ('tig:///ePharma/PedigreeStatus')/PedigreeStatus
where  $i/PedigreeID = $peid and $i/Status/StatusChangedOn = $i/TimeStamp
return data($i/Status/StatusChangedTo)
)
return <output>{
         <DocumentId>{data($recent/serialNumber)}</DocumentId> ,
         <TransactionNumber>{data($recent/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</TransactionNumber> ,
         <DateTime>{data($recent/date)}</DateTime>,
	<Name>{data($recent/source)}</Name>,
	<Count>{count($recent/pedigree)}</Count>, 
	<Status>{$status}</Status> 
}</output>
"),

(: Insert Pedigree Status Document :)
tig:create-stored-procedure("InsertPedigreeStatus","

declare variable $pedigreeId as string external;
declare variable $status as string external;
declare variable $sessionId as string external;

let $userid := (for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
)
return
tig:insert-document('tig:///ePharma/PedigreeStatus', 
<PedigreeStatus><PedigreeID>{$pedigreeId}</PedigreeID>
		<Status>
		   <StatusChangedOn>{fn:current-dateTime()}</StatusChangedOn>
		   <StatusChangedTo>{$status}</StatusChangedTo>
		   <UserId>{$userid}</UserId>
		</Status>
		<TimeStamp>{fn:current-dateTime()}</TimeStamp>
</PedigreeStatus>
 )
"),

(: Checking Pedigree Bank for PedigreeID :)
tig:create-stored-procedure("PedigreeBankCheck","

declare variable $despatchAdviceNo as string external;

let $NDC := (for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
where $k/ID = $despatchAdviceNo 
return data($k/DespatchLine/Item/SellersItemIdentification/ID)
)
let $lotno :=( for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where $i/Description = 'LOTNO'
		and $i/../../../../ID = $despatchAdviceNo
		return 
		data( $i/../ID)
	     )
for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand
where $i/NDC = $NDC and $i/LotInfo/LotNumber = $lotno
return data($i/LotInfo/ReceivedPedigreeID)
"),

(: Get Received Pedigree Details :)
tig:create-stored-procedure("GetRPDetails","
declare variable $pedID as string external;

for $j in collection('tig:///ePharma/ShippedPedigree')/pedigree
where $j/receivedPedigree/documentInfo/serialNumber = $pedID
return $j
"),

tig:create-stored-procedure("ReturnsDetails","

declare variable $pedId as string external;
let $recvdId := (for $i in collection('tig:///ePharma/ShippedPedigree')/pedigree
where $i/receivedPedigree/pedigree/shippedPedigree/documentInfo/serialNumber = $pedId
return data($i/receivedPedigree/documentInfo/serialNumber) )

let $lotInfo := (for $k in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[ReceivedPedigreeID = $recvdId and Returned = 'true']
return <root><NDC>{data($k/../NDC)}</NDC><LotNumber>{data($k/LotNumber)}</LotNumber>
<Quantity>{data($k/Quantity)}</Quantity></root>
)

let $product := (for $k in collection('tig:///CatalogManager/ProductMaster')/Product
where $k/NDC = data($lotInfo/root/NDC)
return <ProductName>{data($k/ProductName)}</ProductName>
)

return <output>{$lotInfo, $product}
</output>
"),

(: To create New Pedigree Bank Document :)
tig:create-stored-procedure("createNewPedigreeBankDoc", "
declare variable $NDC as string external;
declare variable $recId as string external;
let $PedigreeBank :=
(
for $s in collection('tig:///ePharma/ShippedPedigree')/pedigree/receivedPedigree 
where $s/documentInfo/serialNumber=$recId
return
(
<PedigreeBank>
<InventoryOnHand>
<NDC>{$NDC}</NDC>
<TotalInventory>{data($s//initialPedigree/itemInfo/quantity)}</TotalInventory>
<LotInfo>
<LotNumber>{data($s//initialPedigree/itemInfo/lot)}</LotNumber>
<ShippingInfo/>
<Quantity>{data($s//initialPedigree/itemInfo/quantity)}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
</LotInfo>
</InventoryOnHand>
</PedigreeBank>
)
)
return insert document $PedigreeBank into  'tig:///ePharma/PedigreeBank'
"),

(: To Insert LotInfo Node:)
tig:create-stored-procedure("insertLotinfoNode", "
declare variable $NDC as string external;
declare variable $LotInfo as node()* external;
update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
insert
$LotInfo as last into $i/InventoryOnHand"),

(:To Update Total Inventory:)
tig:create-stored-procedure("UpDateTotalInventory", "
declare variable $NDC as string external;
declare variable $value as integer external;
update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory+$value
"),

(:To update PedigreeBank for receipt of goods:)
tig:create-stored-procedure("UpDatePedigreeBank", "
declare variable $NDC as string external;
declare variable $recId as string external;
let $LotInfo as node()? :=
(<LotInfo>{
for $s in collection('tig:///ePharma/ShippedPedigree')/pedigree/receivedPedigree 
where $s/documentInfo/serialNumber=$recId
return 
(
<LotNumber>{data($s//initialPedigree/itemInfo/lot)}</LotNumber>,
<Quantity>{data($s//initialPedigree/itemInfo/quantity)}</Quantity>,
<LocationID></LocationID>,
<SerialNumber></SerialNumber>,
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
)
}</LotInfo>
)
return 
(
tlsp:insertLotinfoNode($NDC, $LotInfo ),
tlsp:UpDateTotalInventory($NDC,  xs:integer( $LotInfo/Quantity )  )
)

 "),
(:To insert shipping info node to pedigreebank :)
tig:create-stored-procedure("insertShippingInfoNodetoPedigreeBank", "
declare variable $NDC as string external;
declare variable $lot as string external;
declare variable $shippingInfo as node()* external;

update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank/InventoryOnHand[NDC=$NDC]/LotInfo
where $i/LotNumber=$lot
insert
$shippingInfo after $i/LotNumber"),

(:To Update TotalInventory for shipping:)
tig:create-stored-procedure("UpDateTotalInventoryforShipping", "
declare variable $NDC as string external;
declare variable $value as integer external;
update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory - $value
"),
(:To update Lot Quantity for Shipping:)
tig:create-stored-procedure("UpDateLotQuantityforShipping", "
declare variable $NDC as string external;
declare variable $lot as string external;
declare variable $quantity as integer external;

update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank/InventoryOnHand[NDC=$NDC]/LotInfo
where $i/LotNumber=$lot
replace value of $i/Quantity with $i/Quantity - $quantity
"),

(:To update pedigree after shipment:)
tig:create-stored-procedure("updatePedigreebankAfterShipment", "
declare variable $serialNumber as string external;
for $s in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber=$serialNumber]/pedigree 
let $NDC := data($s/shippedPedigree//initialPedigree/productInfo/productCode) cast as string
let $lot := data($s/shippedPedigree//initialPedigree/itemInfo/lot) cast as string
let $quantity := data($s/shippedPedigree//initialPedigree/itemInfo/quantity) cast as integer
let $ShippingInfo :=
(<ShippingInfo>
                <Quantity>{$quantity}</Quantity>
                <ShippedPedigreeID>{data($s/shippedPedigree/documentInfo/serialNumber)}</ShippedPedigreeID>
 </ShippingInfo>
)

return 
(
tlsp:insertShippingInfoNodetoPedigreeBank($NDC,$lot,$ShippingInfo),
tlsp:UpDateTotalInventoryforShipping($NDC,$quantity),
tlsp:UpDateLotQuantityforShipping($NDC,$lot,$quantity)
)
"),
(:to Check whether the NDC exists in the Pedigree Bank or not:)
tig:create-stored-procedure("ndcExists","
 declare variable $ndc as string external;
 let $all_booleans as xs:boolean :=
 (
 for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand 
 where upper-case($i/NDC) = upper-case($ndc) 
 return true()
 )
 return $all_booleans = true()
 
"),


(: For Create shipped pedigree :)
tig:create-stored-procedure("CreateShippedPedigree","

declare variable $despatchAdviseNo as string external;
declare variable $transactionType as string external;
declare variable $flag as string external;
declare variable $pedID as string external; 
declare variable $sessionId as string external;
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

let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>
let $envid := util:create-unique-id('PE')
let $pdenv := (for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
where $k/ID = $despatchAdviseNo
return <PedigreeEnvelope>
<version>1</version>
<serialNumber>{$envid}</serialNumber>
<date>{fn:current-dateTime()}</date>
<source>{data($k/SellerParty/Party/PartyName/Name)}</source><destination>{data($k/BuyerParty/Party/PartyName/Name)}</destination>

{
for $s in (
<x> {for $NDC in data($k/DespatchLine/Item/SellersItemIdentification/ID)

let $productNode :=(
for $j in collection('tig:///CatalogManager/ProductMaster')/Product
	where $j/NDC = $NDC
	return <productInfo><drugName>{data($j/ProductName)}</drugName><manufacturer>{data($j/ManufacturerName)}</manufacturer>
	<productCode type='NDC'>{$NDC}</productCode><dosageForm>{data($j/DosageForm)}</dosageForm>
	<strength>{data($j/DosageStrength)}</strength><containerSize>{data($j/ContainerSize)}</containerSize> </productInfo> 
 )
let $itemserial1 := (
for $x in collection('tig:///CatalogManager/ProductMaster')/Product
	where $x/NDC = $NDC
	return data($x/EPC)
)
let $quantity1 :=(for $x in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	        where $x/ID = $despatchAdviseNo 
	        and $x/DespatchLine/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DespatchLine/DeliveredQuantity)
	)
let $lotexp1 :=(  for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where $i/Description = 'LOTEXP'
		and $i/../../../../ID = $despatchAdviseNo
		return 
		data( $i/../ID)
	     )
let $sscc1 := (for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification
where $i/PhysicalAttribute/Description = 'SSCC'
and  $i/../../../ID = $despatchAdviseNo
return
data( $i/ID)
)

let $sellerInfo := local:getPartnerInfo(data($k/SellerParty/Party/PartyName/Name))
let $buyerInfo := local:getPartnerInfo(data($k/BuyerParty/Party/PartyName/Name))

let $senderinfo := (
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	where $k/ID = $despatchAdviseNo
	return <senderInfo><businessAddress><businessName>{data($k/SellerParty/Party/PartyName)}</businessName>
	<street1>{data($k/SellerParty/Party/Address/StreetName)}</street1><street2>{data($k/SellerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/SellerParty/Party/Address/CityName)}</city>
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
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
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
let $lotno1 :=( for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where( $i/Description = 'LOTNO'
		and $i/../../../../ID =  $despatchAdviseNo  )
		return 
		data ($i/../ID)
	     )
let $lotno := $lotno1
let $sscc := $sscc1
let $lotexp := $lotexp1
let $quantity := $quantity1
let $itemserial := $itemserial1
let $docid := util:create-unique-id('S')
let $shipid := util:create-unique-id('ID')
let $contid := util:create-unique-id('C')
let $res := tlsp:GetRPDetails($pedID)

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber>{$itemserial}</itemSerialNumber><productCode>{data($productNode/productCode)}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>

</pedigreeHandle></container>)

return ($container,
  
<pedigree>
  <shippedPedigree id='{$shipid}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>1</version>
	</documentInfo>
{if($flag = '1')then	
	<initialPedigree>
	   {$productNode}
	   <itemInfo>
	     <lot>{$lotno}</lot> 
	     <expirationDate>{$lotexp}</expirationDate> 
	     <quantity>{$quantity}</quantity> 
	     <itemSerialNumber>{$itemserial}</itemSerialNumber>
	   </itemInfo>
	   <altPedigree/>
	</initialPedigree>
else 
if($flag = '2') then <unsignedReceivedPedigree/>
else
if($flag = '3') then <repackagedPedigree/>
else $res 
  
}
	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{$despatchAdviseNo}</identifier><identifierType>{$transactionType}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{fn:current-dateTime()}</transactionDate>
	</transactionInfo>
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{fn:current-dateTime()}</signatureDate>
	  <signatureMeaning>Created Unsigned</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</PedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment($envid) :)
return $ret 

"),
(: For Creatin and Updating Status :)
tig:create-stored-procedure("insertNode","declare variable $n1 as node() external;
declare variable $uri as string external;
update for $b in doc($uri)//TimeStamp
replace $b with 
         $n1/* "),
tig:create-stored-procedure("InsertAndChangeStatus","

declare variable $pedId as string external;
declare variable $status as string external;
declare variable $sessionId as string external;

let $time := fn:current-dateTime()

let $userid := (for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
)
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
(: For Shipping Manager Pedigree Detail Screen :)
tig:create-stored-procedure("ShipEnvelopeDetails","
declare function local:getPedigreeType($envelopId as xs:string){
  for $i in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber = $envelopId]/pedigree
  return 
   if( exists( $i/shippedPedigree/pedigree )  ) then 'Shipped-Received' else if( exists( $i/shippedPedigree/initialPedigree) ) then 'Shipped-Initial'  else 'Unknown'
};

declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where $e/serialNumber = $envelopeid
return <output>{
<ContainerCode>{data($e/container/containerCode)}</ContainerCode>,
<PedigreeID>{data($e/pedigree/shippedPedigree/documentInfo/serialNumber)}</PedigreeID>, 
<Quantity>{data($e/container/pedigreeHandle/quantity )}</Quantity>,
<LotNum>{data($e/container/pedigreeHandle/lot)}</LotNum>,
<PedigreeType>{local:getPedigreeType($envelopeid)}</PedigreeType>,
<TransactionDate>{data($e/pedigree/shippedPedigree/transactionInfo/transactionDate)}</TransactionDate>,
<DrugName>{data($e//initialPedigree/productInfo/drugName )}</DrugName>,
<DrugCode>{data($e//initialPedigree/productInfo/productCode )}</DrugCode>,
<Attachement>EDI ASN DATA</Attachement>,
<Envelope>{data($e/serialNumber)}</Envelope>,
<Date>{data($e/date)}</Date>, 
<source>{data($e/source)}</source>,
<destination>{data($e/destination)}</destination>,
<count>{data(count($e/pedigree/shippedPedigree))}</count>

}</output>
"),





(: For Receiving Manager :)
(:Quick find options queries sp:)
tig:create-stored-procedure("getSearchQueries2",
"
 declare variable $root_node as xs:string {'$root'};
 declare variable $common_str as xs:string {""
 
 declare function local:getStatus($pedID as xs:string?) as xs:string {

 for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedID] 
 for $status in $ps/Status 
 where $status/StatusChangedOn  = $ps/TimeStamp[1]
 return $status/StatusChangedTo/text()
   
};

 ""};
 declare variable $recvd_local_fn_1 as xs:string {""declare function local:returnPedigrees($root as document-node()? ) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/PedigreeEnvelope/pedigree
 ""};

 declare variable $recvd_local_fn_3 as xs:string {"" 
 return 
 <Record>
 {
  <pedigreeID>{data($ped/shippedPedigree/documentInfo/serialNumber)}</pedigreeID>,
  <envelopID>{data($root/PedigreeEnvelope/serialNumber)}</envelopID>,
    <dateRecieved>{$ped/shippedPedigree/transactionInfo/transactionDate/text()}</dateRecieved>,
  <tradingPartner>{$ped/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/shippedPedigree/transactionInfo/transactionIdentifier/identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/shippedPedigree/documentInfo/serialNumber )
  }</status>,
  <createdBy>System</createdBy>,
  <docURI>{$docURI}</docURI>
 }
 </Record>	
 };
 ""};


 declare variable $shipped_local_fn_1 as xs:string {""declare function local:returnPedigrees($root as document-node()?) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/pedigree/receivedPedigree
 ""};
 
 declare variable $shipped_local_fn_3 as xs:string {"" 
 return <Record>
 {
  <pedigreeID>{$ped/documentInfo/serialNumber/text()}</pedigreeID>,
  <envelopID>N/A</envelopID>,
   <dateRecieved>{$ped/receivingInfo/dateReceived/text()}</dateRecieved>,
  <tradingPartner>{$ped/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/documentInfo/serialNumber )
  }</status>,
 <createdBy>System</createdBy>,
 <docURI>{$docURI}</docURI>
 }
 </Record>	
 };
 ""};
 
 declare variable $g_search_elts as node()
		{<Search>
		   <Scenarios>	
			<Scenario>	
				<Collections>
					<Collection>tig:///ePharma/ReceivedPedigree</Collection>	
					<Collection>tig:///ePharma/ShippedPedigree</Collection>	
				</Collections>
		   		<Key>
					<Name>FromDate</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>PedigreeEnvelope/date</Path>
						<Operator>&gt;=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>
		   		<Key>
					<Name>ToDate</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>PedigreeEnvelope/date</Path>
						<Operator><![CDATA[<=]]></Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	
				<Key>
					<Name>ContainerCode</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>PedigreeEnvelope/container/containercode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>No</PedPrefix>			
						<Path><![CDATA[<Dummy>{data($ped/shippedPedigree/@id)}</Dummy>]]></Path>
						<Operator>=</Operator>
						<Value>$root/PedigreeEnvelope/container[containercode = '$UserVal']/pedigreeHandle/serialNumber</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>NDC</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>PedigreeEnvelope/pedigree/shippedPedigree/initialPedigree/productInfo/productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>shippedPedigree/initialPedigree/productInfo/productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>LotNumber</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>				
						<Path>PedigreeEnvelope/container/pedigreeHandle/lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
						
		   		</Key>	
				<Key>
					<Name>TransNo</Name>
					<RootSelection>
					    <Query>	
						<RootPrefix>Yes</RootPrefix>		
						<Path>PedigreeEnvelope/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>PedID</Name>
					<RootSelection>
					    <Query>			
						<RootPrefix>Yes</RootPrefix>
						<Path>PedigreeEnvelope/pedigree/shippedPedigree/documentInfo/serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>shippedPedigree/documentInfo/serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>TPName</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>PedigreeEnvelope/source</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	                               
				<Key>
					<Name>Status</Name>
					<RootSelection>
					    <Query>			
						<RootPrefix>No</RootPrefix>
						<Path>local:getStatus( $root/PedigreeEnvelope/pedigree/shippedPedigree/documentInfo/serialNumber/text() )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/shippedPedigree/documentInfo/serialNumber )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				
				<Key>
					<Name>EnvelopeID</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>PedigreeEnvelope/serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	     			
			</Scenario>
			
			<Scenario>	
				<Collections>
					<Collection>tig:///ePharma/ShippedPedigree</Collection>	
				</Collections>
		   		<Key>
					<Name>FromDate</Name>
					<RootSelection>
					    <Query>		
						<RootPrefix>Yes</RootPrefix>	
						<Path>pedigree/receivedPedigree/receivingInfo/dateReceived</Path>
						<Operator>&gt;=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>
		   		<Key>
					<Name>ToDate</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>pedigree/receivedPedigree/receivingInfo/dateReceived</Path>
						<Operator><![CDATA[<=]]></Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	
				<Key>
					<Name>ContainerCode</Name>
					<RootSelection>
					</RootSelection>
		   		</Key>	
				<Key>
					<Name>NDC</Name>
					<RootSelection>
					    <Query>		
						<RootPrefix>Yes</RootPrefix>	
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/initialPedigree/productInfo/productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>pedigree/shippedPedigree/initialPedigree/productInfo/productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>LotNumber</Name>
					<RootSelection>
					    <Query>			
						<RootPrefix>Yes</RootPrefix>	
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/initialPedigree/itemInfo/lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>		
						<PedPrefix>Yes</PedPrefix>		
						<Path>pedigree/shippedPedigree/initialPedigree/itemInfo/lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>TransNo</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>PedID</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>pedigree/receivedPedigree/documentInfo/serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>documentInfo/serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>

		   		</Key>	
				<Key>
					<Name>TPName</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>Status</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>No</RootPrefix>			
						<Path>local:getStatus($root/pedigree/receivedPedigree/documentInfo/serialNumber/text())</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/documentInfo/serialNumber )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				

				<Key>
					<Name>EnvelopeID</Name>
					<RootSelection>
					</RootSelection>
		   		</Key>	     		
			</Scenario>
		   </Scenarios>		
		 </Search>	 
		};

    declare function local:getRootWhereClause($root as node()?,$no as xs:integer,$keys as xs:string*,$values as xs:string*) as xs:string?{
	string-join( 
	(	
		
		for $index in 1 to count($keys)
		let $key_val as xs:string := ( $keys[$index] ) cast as xs:string
		let $user_val as xs:string? := ( $values[$index] ) cast as xs:string
		return 
		for $query in $root//Scenario[$no]/Key[Name = $key_val]/RootSelection/Query
		let $val as xs:string := string( $query/Value )
		let $prefx_root as xs:string  :=   string($query/RootPrefix)
		let $root_node_name as xs:string := if(  lower-case($prefx_root)  = 'yes' ) then  '$root/' else ''
		let $val_query as xs:string :=  if( $val = '$UserVal' ) then concat( '''', $user_val ,'''') else $val
		return string-join( ( concat( $root_node_name, string($query/Path)  ), string($query/Operator) , $val_query) , '  ' )
	),' and ' )
  };
      
     declare function local:getUserValReplaced($src as xs:string,$search4 as xs:string,$rep as xs:string) as xs:string {
  	(: Replaces only the first occurrance :)
  	let $str1 as xs:string? := substring-before( $src , $search4 )
  	let $str2 as xs:string? := substring-after(  $src , $search4 )
  	return if( string-length($str1) = 0 and string-length($str2) = 0 ) then  $src  else string-join( ($str1,$rep,$str2),'' ) 
     };
	
      declare function local:getPedWhereClause($root as node()?,$no as xs:integer,$keys as xs:string*,$values as xs:string*) as xs:string?{
		
         string-join( 
	(
	for $index in 1 to count($keys)
	let $key_val as xs:string := ( $keys[$index] ) cast as xs:string
	let $user_val as xs:string := ( $values[$index] ) cast as xs:string
	return 
	for $query in $root//Scenario[$no]/Key[Name = $key_val]/PedigreeSelection/Query
	let $prefx_ped as xs:string := string( $query/PedPrefix  )
	let $val as xs:string := string( $query/Value )
	let $ped_node_name as xs:string := if( lower-case($prefx_ped) eq 'yes' ) then  '$ped/' else ''
	let $val_query as xs:string :=   if( $val = '$UserVal' ) then concat( '''', $user_val ,'''') else local:getUserValReplaced($val,'$UserVal', $user_val )
	return  string-join( ( concat( $ped_node_name,$query/Path/string() ), $query/Operator/string() , $val_query ) , '  ' )
	
	),' and ' )
	
  };

  declare function local:getAllKeys($root as node()?,$no as xs:integer,$keys as xs:string*){
	for $key in $root//Scenario[$no]/Key[Name = $keys]
	return $key
  };

  declare function local:getQuery( $coluri as xs:string, $root_where_clause as xs:string?,
		$ped_where_clause as xs:string? ,$root_node_name as xs:string,
		$scenario_3 as xs:boolean,
		$common_local_str as xs:string,
		$recvd_start as xs:string,$recvd_end as xs:string,
		$shipped_start as xs:string,$shipped_end as xs:string,
		$count_elements as xs:integer) as xs:string {
  
 let $first_local_fn as xs:string :=  xs:string($common_local_str ) 
 let $second_local_fn_start as xs:string := xs:string ( if( $scenario_3 ) then $shipped_start else $recvd_start )
 let $ped_full_clause as xs:string := xs:string( if( not(empty($ped_where_clause)) and string-length($ped_where_clause) > 0 ) then concat(' where ', $ped_where_clause ) else '' ) 
 let $second_local_fn_end as xs:string := xs:string ( if( $scenario_3 ) then $shipped_end else $recvd_end ) 
 let $for_clause as xs:string := xs:string (  concat( 'for ' , $root_node_name , ' in ' , ' collection(''',  $coluri , ''') ' )   )
 let $root_full_clause as xs:string := xs:string( if( not(empty($root_where_clause)) and string-length($root_where_clause) > 0 ) then concat('where ',$root_where_clause) else if( $count_elements > 0  ) then ' where false() ' else ' ' )
 let $return_clause as xs:string := xs:string( concat( ' return  local:returnPedigrees(' , $root_node_name , ')' ) )
 return concat(  $first_local_fn , ' ' ,
		$second_local_fn_start , ' ',
		$ped_full_clause , ' ' , $second_local_fn_end, ' ' ,
		$for_clause , ' ' , $root_full_clause , ' ',
		$return_clause
	   )

 };

  declare function local:validateElements( $root as node()? ,  $elts as xs:string* )  {

	  for $elt_name in $elts
	  for $sc in $root//Scenario
	  let $key_names  := $sc//Key/Name/text()
	  return if( $elt_name = $key_names ) then fn:true() else error( concat( $elt_name , ' not found in all the scenarios ') )

  };	

  declare variable $search_elt_names_ext as xs:string* external;
  declare variable $search_elt_values_ext as xs:string* external;  
   
  let $search_elt_names := $search_elt_names_ext
  let $search_elt_values := $search_elt_values_ext
  let $validate := local:validateElements( $g_search_elts ,  $search_elt_names )
  let $count_elts as xs:integer := count( $search_elt_names )
  for $index in 1 to count($g_search_elts//Scenario)
  let $root_where_clause as xs:string? := local:getRootWhereClause($g_search_elts,$index,$search_elt_names,$search_elt_values)		
  let $ped_where_clause as xs:string? := local:getPedWhereClause($g_search_elts,$index,$search_elt_names,$search_elt_values)	
  for $col_uri in $g_search_elts//Scenario[$index]/Collections/Collection
  return local:getQuery($col_uri/text(), $root_where_clause ,$ped_where_clause,$root_node, $index = 2 ,$common_str,$recvd_local_fn_1 ,$recvd_local_fn_3 , $shipped_local_fn_1 , $shipped_local_fn_3,$count_elts )
  
"),
tig:create-stored-procedure("getSearchResults2","

 declare variable $search_elt_names_ext as xs:string* external;
 declare variable $search_elt_values_ext as xs:string* external;   
 
 let $queries  as xs:string+ :=  tlsp:getSearchQueries2( $search_elt_names_ext , $search_elt_values_ext )
 return <SearchInfo>{ 
 for $query in $queries
 return evaluate( $query )
 }
 </SearchInfo> 
"),
(:Quick find options Envelope display sp:)
tig:create-stored-procedure("envelopeSearch","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma/ReceivedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope
where $envelope/serialNumber = $distinct_eids
return <output> 
{
<envelopeId>{data($envelope/serialNumber)}</envelopeId>,
<dateRecieved>{data($envelope/date)}</dateRecieved>,
<source>{data($envelope/source)}</source>,
<destination>{data($envelope/destination)}</destination>,
<count>{count($envelope/pedigree)}</count>,
<createdBy>System</createdBy>
}

</output> 
"),
(:Quick find options pedigree sp status as authenticate:)
tig:create-stored-procedure("PedigreeSearch"," 
declare variable $search_elt_names_ext as xs:string* external; 
declare variable $search_elt_values_ext as xs:string* external; 
let $all_record := tlsp:getSearchResults2( $search_elt_names_ext , $search_elt_values_ext )//Record[ starts-with(docURI,'tig:///ePharma/ShippedPedigree/') ] 
return $all_record 
"), 
(: Envelope Details display sp:)
tig:create-stored-procedure("Envelope","
declare function local:getPedigreeType($envelopId as xs:string){
  for $i in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $envelopId]/pedigree
  return 
   if( exists( $i/shippedPedigree/pedigree )  ) then 'Shipped-Received' else if( exists( $i/shippedPedigree/initialPedigree) ) then 'Shipped-Initial'  else 'Unknown'
};

declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope
where $e/serialNumber = $envelopeid
return <output>{
<ContainerCode>{data($e/container/containerCode)}</ContainerCode>,
<PedigreeID>{data($e/pedigree/shippedPedigree/documentInfo/serialNumber)}</PedigreeID>, 
<Quantity>{data($e/container/pedigreeHandle/quantity )}</Quantity>,
<LotNum>{data($e/container/pedigreeHandle/lot)}</LotNum>,
<PedigreeType>{local:getPedigreeType($envelopeid)}</PedigreeType>,
<TransactionDate>{data($e/pedigree/shippedPedigree/transactionInfo/transactionDate)}</TransactionDate>,
<DrugName>{data($e//initialPedigree/productInfo/drugName )}</DrugName>,
<DrugCode>{data($e//initialPedigree/productInfo/productCode )}</DrugCode>,
<Attachement>{data($e//initialPedigree/attachment/mimeType)}</Attachement>,
<Envelope>{data($e/serialNumber)}</Envelope>,
<Date>{data($e/date)}</Date>, 
<source>{data($e/source)}</source>,
<destination>{data($e/destination)}</destination>,
<count>{data(count($e/pedigree/shippedPedigree))}</count>

}</output>
"),

(: AuditTrail Details display sp:)
tig:create-stored-procedure("getAuditTrailDetails","
declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree/shippedPedigree/documentInfo[ serialNumber = $serualId]
return $i/parent::*)
return ($k,$k//descendant::*[exists(@id)])
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree/shippedPedigree/documentInfo[ serialNumber = $serualId]
return $i/parent::*)
return ($k,$k//descendant::*[exists(@id)])
else ('No collection found')
"),

(: Attachment Details display sp:)
tig:create-stored-procedure("getAttachmentDetails","
declare variable $catalogName as xs:string external;
declare variable $serialId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree//documentInfo[ serialNumber = $serialId]
return $i/parent::*)
return <data>{ 

<true>{exists($k//initialPedigree/attachment)}</true>,
<mimeType>{data($k//attachment/mimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</TransactionType>,
<Date>{data($k//shippedPedigree/transactionInfo/transactionDate)}</Date>,
<TransactionNo>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</TransactionNo>
}
</data>
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree//documentInfo[ serialNumber = $serialId]
return $i/parent::*)
return <data>{ 

<true>{exists($k//initialPedigree/attachment)}</true>,
<mimeType>{data($k//attachment/mimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</TransactionType>,
<Date>{data($k//shippedPedigree/transactionInfo/transactionDate)}</Date>,
<TransactionNo>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</TransactionNo>
}
</data>
else ('No collection found')
"),

tig:create-stored-procedure("Shippedpedigreedetails","

declare variable $serialNumber as string external;

declare function local:getProductInfo($e as node()){
if(exists($e/shippedPedigree/repackagedPedigree)) then(
<drugName>{data($e/shippedPedigree/repackagedPedigree/productInfo/drugName)}</drugName>,
<productCode>{data($e/shippedPedigree/repackagedPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e/shippedPedigree/repackagedPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e/shippedPedigree/repackagedPedigree/productInfo/manufacturer)}</manufacturer>,
<quantity>{data($e/shippedPedigree/repackagedPedigree/itemInfo/quantity )}</quantity>,
<dosageForm>{data($e/shippedPedigree/repackagedPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e/shippedPedigree/repackagedPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e/shippedPedigree/repackagedPedigree/productInfo/containerSize)}</containerSize>
)
else(
<drugName>{data($e//initialPedigree/productInfo/drugName )}</drugName>,
<productCode>{data($e//initialPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e//initialPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e//initialPedigree/productInfo/manufacturer )}</manufacturer>,
<quantity>{data($e//initialPedigree/itemInfo/quantity)}</quantity>,
<dosageForm>{data($e//initialPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e//initialPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e//initialPedigree/productInfo/containerSize)}</containerSize>
)
};

for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope//pedigree
where $e/shippedPedigree/documentInfo/serialNumber=$serialNumber

return <output>{
<repackage>{exists($e/shippedPedigree/repackagedPedigree)}</repackage>,
local:getProductInfo($e),
<custName>{data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</custName>,
<custAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</custAddress>,
<custContact>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</custContact>,
<custPhone>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</custPhone>,
<custEmail>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/shippedPedigree/signatureInfo/signerInfo/name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/shippedPedigree/signatureInfo/signerInfo/title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/shippedPedigree/signatureInfo/signerInfo/telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/shippedPedigree/signatureInfo/signerInfo/email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/shippedPedigree/signatureInfo/signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/shippedPedigree/signatureInfo/signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/shippedPedigree/documentInfo/serialNumber)}</pedigreeId>,
<transactionDate>{data($e/shippedPedigree/transactionInfo/transactionDate)}</transactionDate>, 
<transactionType>{data($e/shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</transactionType>,
<transactionNo>{data($e/shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</transactionNo>,
<fromCompany>{data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</fromCompany>,
<toCompany>{data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/country))}</fromBillAddress>,
<fromContact>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</fromContact>,
<fromTitle>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/title)} </fromTitle>,
<fromPhone>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</fromPhone>,
<fromEmail>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</fromEmail>,
<fromLicense>{data($e/shippedPedigree/transactionInfo/senderInfo/licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/country))}</toShipAddress>,
<toBillAddress>{concat(data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street1),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street2),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/city),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/country))}</toBillAddress>,
<toContact>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/name)}</toContact>,
<toTitle>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/title)}</toTitle>,
<toPhone>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/telephone)}</toPhone>,
<toEmail>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/email)}</toEmail>,
<toLicense>{data($e/shippedPedigree/transactionInfo/recipientInfo/licenseNumber)}</toLicense>

}</output>
"),


tig:create-stored-procedure("ReceivedPedigreeDetails","
declare variable $pedigreeId as string external;

let $recvId := ( for $i in collection('tig:///ePharma/ShippedPedigree')/pedigree
where $i/receivedPedigree/pedigree/shippedPedigree/documentInfo/serialNumber = $pedigreeId
return data($i/receivedPedigree/documentInfo/serialNumber)
)

for $i in collection('tig:///ePharma/ShippedPedigree')/pedigree
where $i/receivedPedigree/documentInfo/serialNumber = $recvId
return
<result>
<pedigreeid>{$recvId}</pedigreeid>
<daterecvd>{data($i/receivedPedigree/receivingInfo/dateReceived)}</daterecvd>
<lotnum>{data($i//initialPedigree/itemInfo/lot)}</lotnum>
<quantity>{data($i//initialPedigree/itemInfo/quantity)}</quantity>
<expirationdate>{data($i//initialPedigree/itemInfo/expirationDate)}</expirationdate>
<itemserialnumber>{data($i//initialPedigree/itemInfo/itemSerialNumber)}</itemserialnumber>
<signame>{data($i/receivedPedigree/signatureInfo/signerInfo/name)}</signame>
<title>{data($i/receivedPedigree/signatureInfo/signerInfo/title)}</title>
<telephone>{data($i/receivedPedigree/signatureInfo/signerInfo/telephone)}</telephone>
<sigemail>{data($i/receivedPedigree/signatureInfo/signerInfo/email)}</sigemail>
<url>{data($i/receivedPedigree/signatureInfo/signerInfo/url)}</url>
<signaturedate>{data($i/receivedPedigree/signatureInfo/signatureDate)}</signaturedate>
</result>
"),

tig:create-stored-procedure("CreateReceivedPedigreeForEnvelopes","

declare variable $envelopeId as string external;
declare variable $sessionId as string external;
import module namespace util = 'xquery:modules:util';

let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>

for $k in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $envelopeId]/pedigree
let $docid := util:create-unique-id('ID')
let $recvid := util:create-unique-id('R')

for $ndc in data($k//initialPedigree/productInfo/productCode)
return
if( tlsp:ndcExists($ndc) ) then
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
			<dateReceived>{fn:current-dateTime()}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//initialPedigree/itemInfo/lot)}</lot>
                    		<expirationDate>{data($k//initialPedigree/itemInfo/expirationDate)}</expirationDate> 
                    		<quantity>{data($k//initialPedigree/itemInfo/quantity)}</quantity>
                    		<itemSerialNumber>{data($k//initialPedigree/itemInfo/itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{fn:current-dateTime()}</signatureDate>
			<signatureMeaning>Received</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
,tlsp:UpDatePedigreeBank($ndc,$recvid ) )

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
			<dateReceived>{fn:current-dateTime()}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//initialPedigree/itemInfo/lot)}</lot>
                    		<expirationDate>{data($k//initialPedigree/itemInfo/expirationDate)}</expirationDate> 
                    		<quantity>{data($k//initialPedigree/itemInfo/quantity)}</quantity>
                    		<itemSerialNumber>{data($k//initialPedigree/itemInfo/itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{fn:current-dateTime()}</signatureDate>
			<signatureMeaning>Received</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
,tlsp:createNewPedigreeBankDoc($ndc,$recvid)) 

"),

tig:create-stored-procedure("CreateReceivedPedigreeForPedigrees","

declare variable $pedigreeId as string external;
declare variable $sessionId as string external;
import module namespace util = 'xquery:modules:util';

let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>

for $k in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree

let $docid := util:create-unique-id('ID')
let $recvid := util:create-unique-id('R')
let $ndc := $k//initialPedigree/productInfo/productCode
where $k/shippedPedigree/documentInfo/serialNumber = $pedigreeId

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
			<dateReceived>{fn:current-dateTime()}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//initialPedigree/itemInfo/lot)}</lot>
                    		<expirationDate>{data($k//initialPedigree/itemInfo/expirationDate)}</expirationDate> 
                    		<quantity>{data($k//initialPedigree/itemInfo/quantity)}</quantity>
                    		<itemSerialNumber>{data($k//initialPedigree/itemInfo/itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{fn:current-dateTime()}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
 ,tlsp:UpDatePedigreeBank($ndc,$recvid ))

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
			<dateReceived>{fn:current-dateTime()}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//initialPedigree/itemInfo/lot)}</lot>
                    		<expirationDate>{data($k//initialPedigree/itemInfo/expirationDate)}</expirationDate> 
                    		<quantity>{data($k//initialPedigree/itemInfo/quantity)}</quantity>
                    		<itemSerialNumber>{data($k//initialPedigree/itemInfo/itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{fn:current-dateTime()}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
,tlsp:createNewPedigreeBankDoc($ndc,$recvid)) 

"),

tig:create-stored-procedure("ShippingEnvelopeSearch","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma/ShippedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where $envelope/serialNumber = $distinct_eids
return <output> 
{
<DocumentId>{data($envelope/serialNumber)}</DocumentId>,
<TransactionNumber>{data($envelope/pedigree[1]/shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</TransactionNumber>,
<DateTime>{data($envelope/date)}</DateTime>,
<Name>{data($envelope/source)}</Name>,
<destination>{data($envelope/destination)}</destination>,
<count>{count($envelope/pedigree)}</count>,

<Status> 
  {	
   distinct-values(
   for $i in $all_record
   where $i/envelopID = $envelope/serialNumber
   return $i/envelopeStatus/string()
   )
  }</Status>
}

</output> 
"),

tig:create-stored-procedure("pedigreeLevelSignature","
declare character-encoding 'UTF-8';
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

declare variable $EnvelopeId as string external;
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
 for $pedigree in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber =  $EnvelopeId 
	and pedigree/shippedPedigree/documentInfo/serialNumber = $pedId]/pedigree

 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
 where not( exists($pedigree/*:Signature) )
 replace  $pedigree  with  $pedigree_sign/pedigree
 
"),

tig:create-stored-procedure("EnvelopeD","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/serialNumber)}</EnvelopeID>,
<Date>{data($e/date)}</Date>, 
<source>{data($e/source)}</source>,
<destination>{data($e/destination)}</destination>
}</output>
"),
tig:create-stored-procedure("StatusElement","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber= $envelopeid]
(:let $pedId :=data($e/pedigree/shippedPedigree/documentInfo/serialNumber):)
let $status := (for $i in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'Received'
"),
tig:create-stored-procedure("StatE","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber =  $envelopeid]
return 
for $ped in $e/pedigree
let $pedID as xs:string := string($ped/shippedPedigree/documentInfo/serialNumber)
return <pedigree>{
       <pedigreeid>{ $pedID }</pedigreeid>,
       <DrugName>{data( $ped//initialPedigree/productInfo/drugName )}</DrugName>,
      <DrugCode>{data($ped//initialPedigree/productInfo/productCode )}</DrugCode>,
<TransactionDate>{data($ped/shippedPedigree/transactionInfo/transactionDate)}</TransactionDate>,

     (:<Date>{data($e/date)}</Date>, 
     <source>{data($e/source)}</source>,
     <destination>{data($e/destination)}</destination>,:)
       <count>{data(count($e/pedigree))}</count>,
 <status>{ tlsp:StatusElement($envelopeid, $pedID ) }</status>,
<containerCode>{
   data( $e/container[pedigreeHandle/serialNumber = $pedID ]/containerCode )
  }</containerCode>,
<Quantity>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/quantity)}</Quantity>,
<LotNum>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/lot)}</LotNum>
}</pedigree>
"),
tig:create-stored-procedure("getStatusOfPedigree","

declare variable $pedId as string external;
for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedId] 
for $status in $ps/Status 
where $status/StatusChangedOn  = $ps/TimeStamp[1]
return $status/StatusChangedTo/text()
"),
tig:create-stored-procedure("getStatusOfPedigrees","

declare variable $envelopId as string external;

for $ped in collection ('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $envelopId]
let $pedid := data($ped/pedigree/shippedPedigree/documentInfo/serialNumber)
for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedid] 
for $status in $ps/Status 
where $status/StatusChangedOn  = $ps/TimeStamp[1]
return $status/StatusChangedTo/text()

"),



tig:create-stored-procedure("GetProductId","

declare variable $pedId as string external;
let $ndc := (for $i in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where  $i/pedigree/shippedPedigree/documentInfo/serialNumber = $pedId
return data($i/pedigree/shippedPedigree/initialPedigree/productInfo/productCode) )

for $j in collection('tig:///EAGRFID/Products')/Product
where $j/NDC = $ndc
return data($j/ProductID)
"),

tig:create-stored-procedure("GetProductDetails","
declare variable $envId as string external;
declare variable $pedId as string external;
declare variable $ndc as string external;

<output>{
for $j in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where $j/pedigree/shippedPedigree/documentInfo/serialNumber = $pedId
return $j//initialPedigree/productInfo
}
<item>{
for $item in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where $item/pedigree/shippedPedigree/documentInfo/serialNumber = $pedId
return $item//initialPedigree/itemInfo
}
</item>
<root>{
let $a := (for $n in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
           where $n/pedigree/shippedPedigree/documentInfo/serialNumber = $pedId 
           return data($n//initialPedigree/productInfo/productCode))
for $i in collection('tig:///CatalogManager/ProductMaster')/Product 
where $i/NDC = $a  return $i
}</root>
<products>{
let $a := (for $n in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
           where $n/pedigree/shippedPedigree/documentInfo/serialNumber = $pedId 
           return data($n//initialPedigree/productInfo/productCode))
for $k in collection('tig:///EAGRFID/Products')/Product 
where $k/NDC = $a return $k
}
</products></output>
"),
tig:create-stored-procedure("GetBinaryImage","
declare binary-encoding none;
declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support'; 

declare variable $node as binary() external;
declare variable $node-name as xs:string external; (: When I used the $node/name it is showing error-Class Loader failure :)

declare function local:extract($str as xs:string,$node-name as xs:string) as xs:string {
  let $end := concat( ']]>','</', $node-name, '>' )
  return  substring-before( substring-after($str,'CDATA[') , $end ) 
};

    bin:base64-decode(  local:extract( bin:as-string($node,'utf-8') , $node-name )  )

"),

tig:create-stored-procedure("ShippingManagerPedigreeDetails","

declare variable $serialNumber as string external;
for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope/pedigree
where $e/shippedPedigree/documentInfo/serialNumber=$serialNumber

return <output>{

<drugName>{data($e//initialPedigree/productInfo/drugName )}</drugName>,
<productCode>{data($e//initialPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e//initialPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e//initialPedigree/productInfo/manufacturer )}</manufacturer>,
<quantity>{data($e//initialPedigree/itemInfo/quantity )}</quantity>,
<dosageForm>{data($e//initialPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e//initialPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e//initialPedigree/productInfo/containerSize)}</containerSize>,
<custName>{data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</custName>,
<custAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</custAddress>,
<custContact>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</custContact>,
<custPhone>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</custPhone>,
<custEmail>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/shippedPedigree/signatureInfo/signerInfo/name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/shippedPedigree/signatureInfo/signerInfo/title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/shippedPedigree/signatureInfo/signerInfo/telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/shippedPedigree/signatureInfo/signerInfo/email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/shippedPedigree/signatureInfo/signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/shippedPedigree/signatureInfo/signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/shippedPedigree/documentInfo/serialNumber)}</pedigreeId>,
<transactionDate>{data($e/shippedPedigree/transactionInfo/transactionDate)}</transactionDate>, 
<transactionType>{data($e/shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</transactionType>,
<transactionNo>{data($e/shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</transactionNo>,
<fromCompany>{data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</fromCompany>,
<toCompany>{data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/country))}</fromBillAddress>,
<fromContact>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</fromContact>,
<fromTitle>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/title)} </fromTitle>,
<fromPhone>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</fromPhone>,
<fromEmail>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</fromEmail>,
<fromLicense>{data($e/shippedPedigree/transactionInfo/senderInfo/licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/country))}</toShipAddress>,
<toBillAddress>{concat(data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street1),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street2),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/city),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/country))}</toBillAddress>,
<toContact>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/name)}</toContact>,
<toTitle>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/title)}</toTitle>,
<toPhone>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/telephone)}</toPhone>,
<toEmail>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/email)}</toEmail>,
<toLicense>{data($e/shippedPedigree/transactionInfo/recipientInfo/licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("ManufacturerDetails","

declare variable $pedId as string external;
let $mfrName :=( for $n in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree[documentInfo/serialNumber= $pedId]
                  return data($n//initialPedigree/productInfo/manufacturer))
for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
where $i/name = $mfrName and $i/partnerType = 'Manufacturer'
return <output>
<Name>{data($i/name)}</Name><Address><Line1>{data($i/address/line1)}</Line1><Line2>{data($i/address/line2)}</Line2>
<City>{data($i/address/city)}</City><State>{data($i/address/state)}</State><Country>{data($i/address/country)}</Country>
<Zip>{data($i/address/zip)}</Zip>
</Address>
<Contact>{data($i/contact)}</Contact>
<Phone>{data($i/phone)}</Phone><Email>{data($i/email)}</Email><License>{data($i/businessId)}</License>
</output>
"),
tig:create-stored-procedure("ReceivingManufacturerDetails","

declare variable $pedId as string external;
let $mfrName :=( for $n in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope
		where $n/pedigree/shippedPedigree/documentInfo/serialNumber = $pedId
                  return data($n//initialPedigree/productInfo/manufacturer))
for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
where $i/name = $mfrName and $i/partnerType = 'Manufacturer'
return <output>
<Name>{data($i/name)}</Name><Address><Line1>{data($i/address/line1)}</Line1><Line2>{data($i/address/line2)}</Line2>
<City>{data($i/address/city)}</City><State>{data($i/address/state)}</State><Country>{data($i/address/country)}</Country>
<Zip>{data($i/address/zip)}</Zip>
</Address>
<Contact>{data($i/contact)}</Contact>
<Phone>{data($i/phone)}</Phone><Email>{data($i/email)}</Email><License>{data($i/businessId)}</License>
</output>
"),
tig:create-stored-procedure("getBuyersId","

declare variable $pedId as string external;
(:let $trNo :=  data(tlsp:getSearchResults2(('PedID'),('S1138115791625B'))/Record/transactionNumber):)
let $trNo :=  data(tlsp:getSearchResults2(('PedID'),($pedId))/Record/transactionNumber)
for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice 
where $k/ID = $trNo
return data($k/OrderReference/BuyersID)
"),
tig:create-stored-procedure("getInvoiceId","

declare variable $pedId as string external;
(:let $trNo :=  data(tlsp:getSearchResults2(('PedID'),('S1138115791625B'))/Record/transactionNumber):)
let $buyersId :=  tlsp:getBuyersId($pedId)
for $k in collection('tig:///ePharma/Invoices')/Invoice 
where $k/OrderReference/BuyersID = $buyersId
return data($k/ID)
"),

tig:create-stored-procedure("RecEnvelopeSearch","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma/ReceivedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope
where $envelope/serialNumber = $distinct_eids
return <output> 
{
<envelopeId>{data($envelope/serialNumber)}</envelopeId>,
<dateRecieved>{data($envelope/date)}</dateRecieved>,
<source>{data($envelope/source)}</source>,
<destination>{data($envelope/destination)}</destination>,
<count>{count($envelope/pedigree)}</count>,
<createdBy>System</createdBy>
}

</output> 
"),
tig:create-stored-procedure("RecPedStatusElement","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber= $envelopeid]
let $status := (for $i in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'Received'
"),
tig:create-stored-procedure("RecEnvelopesDisplay","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber =  $envelopeid]
return 
 for $ped in $e/pedigree
 let $pedID as xs:string := string($ped/shippedPedigree/documentInfo/serialNumber)
 return <pedigree>{
       <pedigreeid>{ $pedID }</pedigreeid>,
       <DrugName>{data( $ped//initialPedigree/productInfo/drugName )}</DrugName>,
       <DrugCode>{data($ped//initialPedigree/productInfo/productCode )}</DrugCode>,
       <TransactionDate>{data($ped/shippedPedigree/transactionInfo/transactionDate)}</TransactionDate>,
       <count>{data(count($e/pedigree))}</count>,
       <status>{ tlsp:RecPedStatusElement($envelopeid, $pedID ) }</status>,
      <containerCode>{
       data( $e/container[pedigreeHandle/serialNumber = $pedID ]/containerCode )
      }</containerCode>,
     <Quantity>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/quantity)}</Quantity>,
     <LotNum>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/lot)}</LotNum>, 
     <Attachement>{data($ped//initialPedigree/attachment/mimeType)}</Attachement>
}</pedigree>
"),
tig:create-stored-procedure("RecEnvelopeDetails","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/serialNumber)}</EnvelopeID>,
<Date>{data($e/date)}</Date>, 
<source>{data($e/source)}</source>,
<destination>{data($e/destination)}</destination>
}</output>
"),
tig:create-stored-procedure("RecGetProductId","
declare variable $pedId as string external;
let $ndc := (for $i in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
where  $i/documentInfo/serialNumber = $pedId
return data($i/pedigree/shippedPedigree/initialPedigree/productInfo/productCode) )

for $j in collection('tig:///EAGRFID/Products')/Product
where $j/NDC = $ndc
return data($j/ProductID)
"),
tig:create-stored-procedure("RecProductDetails","
(:declare variable $envId as string external;:)
declare variable $pedId as string external;
(:declare variable $ndc as string external;:)

<output>{
for $j in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
where $j/documentInfo/serialNumber = $pedId
return $j//initialPedigree/productInfo
}
<item>{
for $item in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
where $item/documentInfo/serialNumber = $pedId
return $item/itemInfo
}
</item>
<root>{
let $a := (for $n in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
           where $n/documentInfo/serialNumber = $pedId 
           return data($n//initialPedigree/productInfo/productCode))
for $i in collection('tig:///CatalogManager/ProductMaster')/Product 
where $i/NDC = $a  return $i
}</root>
<products>{
let $a := (for $n in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
           where $n/documentInfo/serialNumber = $pedId 
           return data($n//initialPedigree/productInfo/productCode))
for $k in collection('tig:///EAGRFID/Products')/Product 
where $k/NDC = $a return $k
}
</products></output>
"),
tig:create-stored-procedure("RecgetAuditTrailDetails","
declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree/shippedPedigree/documentInfo[ serialNumber = $serualId]
return $i/parent::*)
return ($k,$k//descendant::*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree/shippedPedigree/documentInfo[ serialNumber = $serualId]
return $i/parent::*)
return ($k,$k//descendant::*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else ('No collection found')
"),

(: Attachment Details display sp:)
tig:create-stored-procedure("RecgetAttachmentDetails","
declare variable $catalogName as xs:string external;
declare variable $serialId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in 
collection(concat('tig:///ePharma/',$catalogName))//pedigree//documentInfo[ serialNumber = $serialId]
return $i/parent::*)
return <data>{ 
<true>{exists($k//initialPedigree/attachment)}</true>,
<mimeType>{data($k//initialPedigree/attachment/mimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</TransactionType>,
<Date>{data($k//shippedPedigree/transactionInfo/transactionDate)}</Date>,
<TransactionNo>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</TransactionNo>
}
</data>
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in 
collection(concat('tig:///ePharma/',$catalogName))/PedigreeEnvelope/pedigree/shippedPedigree/documentInfo[ serialNumber 
= $serialId]
return $i/parent::*)
return <data>{ 
<true>{exists($k//initialPedigree/attachment)}</true>,
<mimeType>{data($k//initialPedigree/attachmentmimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k/transactionInfo/transactionIdentifier/identifierType)}</TransactionType>,
<Date>{data($k/transactionInfo/transactionDate)}</Date>,
<TransactionNo>{data($k/transactionInfo/transactionIdentifier/identifier)}</TransactionNo>
}
</data>
else ('No collection found')
"),

(:Create Pedigree for Orders :)
tig:create-stored-procedure("CreateShippedPedigreeForOrders","
declare variable $despatchAdviseNo as string external;
declare variable $orderNo as string external;
declare variable $transactionType as string external;
declare variable $flag as string external;
declare variable $pedID as string external; 
declare variable $sessionId as string external;
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

let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>
let $envid := util:create-unique-id('PE')
let $pdenv := (for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
where $k/ID = $despatchAdviseNo
return <PedigreeEnvelope>
<version>1</version>
<serialNumber>{$envid}</serialNumber>
<date>{fn:current-dateTime()}</date>
<source>{data($k/SellerParty/Party/PartyName/Name)}</source><destination>{data($k/BuyerParty/Party/PartyName/Name)}</destination>

{
for $s in (
<x> {for $NDC in data($k/DespatchLine/Item/SellersItemIdentification/ID)

let $productNode :=(
for $j in collection('tig:///CatalogManager/ProductMaster')/Product
	where $j/NDC = $NDC
	return <productInfo><drugName>{data($j/ProductName)}</drugName><manufacturer>{data($j/ManufacturerName)}</manufacturer>
	<productCode type='NDC'>{$NDC}</productCode><dosageForm>{data($j/DosageForm)}</dosageForm>
	<strength>{data($j/DosageStrength)}</strength><containerSize>{data($j/ContainerSize)}</containerSize> </productInfo> 
 )
let $itemserial1 := (
for $x in collection('tig:///CatalogManager/ProductMaster')/Product
	where $x/NDC = $NDC
	return data($x/EPC)
)
let $quantity1 :=(for $x in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	        where $x/ID = $despatchAdviseNo 
	        and $x/DespatchLine/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DespatchLine/DeliveredQuantity)
	)
let $lotexp1 :=(  for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where $i/Description = 'LOTEXP'
		and $i/../../../../ID = $despatchAdviseNo
		return 
		data( $i/../ID)
	     )
let $sscc1 := (for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification
where $i/PhysicalAttribute/Description = 'SSCC'
and  $i/../../../ID = $despatchAdviseNo
return
data( $i/ID)
)

let $sellerInfo := local:getPartnerInfo(data($k/SellerParty/Party/PartyName/Name))
let $buyerInfo := local:getPartnerInfo(data($k/BuyerParty/Party/PartyName/Name))

let $senderinfo := (
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	where $k/ID = $despatchAdviseNo
	return <senderInfo><businessAddress><businessName>{data($k/SellerParty/Party/PartyName)}</businessName>
	<street1>{data($k/SellerParty/Party/Address/StreetName)}</street1><street2>{data($k/SellerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/SellerParty/Party/Address/CityName)}</city>
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
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
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
let $lotno1 :=( for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where( $i/Description = 'LOTNO'
		and $i/../../../../ID =  $despatchAdviseNo  )
		return 
		data ($i/../ID)
	     )
let $lotno := $lotno1
let $sscc := $sscc1
let $lotexp := $lotexp1
let $quantity := $quantity1
let $itemserial := $itemserial1
let $docid := util:create-unique-id('S')
let $shipid := util:create-unique-id('ID')
let $contid := util:create-unique-id('C')
let $res := tlsp:GetRPDetails($pedID)

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber>{$itemserial}</itemSerialNumber><productCode>{data($productNode/productCode)}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>

</pedigreeHandle></container>)

return ($container,
  
<pedigree>
  <shippedPedigree id='{$shipid}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>1</version>
	</documentInfo>
{if($flag = '1')then	
	<initialPedigree>
	   {$productNode}
	   <itemInfo>
	     <lot>{$lotno}</lot> 
	     <expirationDate>{$lotexp}</expirationDate> 
	     <quantity>{$quantity}</quantity> 
	     <itemSerialNumber>{$itemserial}</itemSerialNumber>
	   </itemInfo>
	</initialPedigree>
else 
if($flag = '2') then <unsignedReceivedPedigree/>
else
if($flag = '3') then <repackagedPedigree/>
else $res 
  
}
	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{$orderNo}</identifier><identifierType>{$transactionType}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{fn:current-dateTime()}</transactionDate>
	</transactionInfo>
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{fn:current-dateTime()}</signatureDate>
	  <signatureMeaning>Created Unsigned</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</PedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment($envid) :)
return $ret 

"),
tig:create-stored-procedure("ShippingMgnrEnvelopesDisplay","
declare variable $envelopeid as string external;
declare function local:getProductInfo($e as node()){
if(exists($e/shippedPedigree/repackagedPedigree)) then(
<DrugName>{data($e/shippedPedigree/repackagedPedigree/productInfo/drugName)}</DrugName>,
<DrugCode>{data($e/shippedPedigree/repackagedPedigree/productInfo/productCode )}</DrugCode>

)
else(
<DrugName>{data($e//initialPedigree/productInfo/drugName)}</DrugName>,
<DrugCode>{data($e/initialPedigree//productInfo/productCode )}</DrugCode>

)
};

for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber =  $envelopeid]
return 
 for $ped in $e/pedigree
 let $pedID as xs:string := string($ped/shippedPedigree/documentInfo/serialNumber)
 return <pedigree>{
       <pedigreeid>{ $pedID }</pedigreeid>,
       local:getProductInfo($ped),
       <TransactionDate>{data($ped/shippedPedigree/transactionInfo/transactionDate)}</TransactionDate>,
       <count>{data(count($e/pedigree))}</count>,
       <status>{ tlsp:ShippingStatusElement($envelopeid, $pedID ) }</status>,
      <containerCode>{
       data( $e/container[pedigreeHandle/serialNumber = $pedID ]/containerCode )
      }</containerCode>,
     <Quantity>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/quantity)}</Quantity>,
     <LotNum>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/lot)}</LotNum>, 
     <Attachement>{data($ped//initialPedigree/attachment/mimeType)}</Attachement>
}</pedigree>
"),
tig:create-stored-procedure("ShippingMgnrEnvelopeDetails","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/serialNumber)}</EnvelopeID>,
<Date>{data($e/date)}</Date>, 
<source>{data($e/source)}</source>,
<destination>{data($e/destination)}</destination>
}</output>
"),
tig:create-stored-procedure("GetUserInfo","
declare variable $sessionId as string external;
let $userId := (for $x in collection('tig:///EAGRFID/SysSessions')/session
where $x/sessionid = $sessionId
return data($x/userid) )

for $i in collection('tig:///EAGRFID/SysUsers')/User 
where $i/UserID = $userId
return data($i/AccessLevel/Access)
"),
tig:create-stored-procedure("ShippingStatusElement","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber= $envelopeid]
(:let $pedId :=data($e/pedigree/shippedPedigree/documentInfo/serialNumber):)
let $status := (for $i in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'Received'
"),

tig:create-stored-procedure("ShippedORReceivedPedigreeDetails","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope//shippedPedigree[documentInfo/serialNumber = $shipId]
//receivedPedigree[documentInfo/serialNumber = $serualId]

return <output>{

<drugName>{data($e//initialPedigree/productInfo/drugName )}</drugName>,
<productCode>{data($e//initialPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e//initialPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e//initialPedigree/productInfo/manufacturer )}</manufacturer>,
<quantity>{data($e//initialPedigree/itemInfo/quantity )}</quantity>,
<dosageForm>{data($e//initialPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e//initialPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e//initialPedigree/productInfo/containerSize)}</containerSize>,
<custName>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</custName>,
<custAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</custAddress>,
<custContact>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</custContact>,
<custPhone>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</custPhone>,
<custEmail>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/pedigree/shippedPedigree/signatureInfo/signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/pedigree/shippedPedigree/documentInfo/serialNumber)}</pedigreeId>,
<transactionDate>{data($e/pedigree/shippedPedigree/transactionInfo/transactionDate)}</transactionDate>, 
<transactionType>{data($e/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</transactionType>,
<transactionNo>{data($e/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</transactionNo>,
<fromCompany>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</fromCompany>,
<toCompany>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/country))}</fromBillAddress>,
<fromContact>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</fromContact>,
<fromTitle>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/title)} </fromTitle>,
<fromPhone>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</fromPhone>,
<fromEmail>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</fromEmail>,
<fromLicense>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/country))}</toShipAddress>,
<toBillAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/country))}</toBillAddress>,
<toContact>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/name)}</toContact>,
<toTitle>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/title)}</toTitle>,
<toPhone>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/telephone)}</toPhone>,
<toEmail>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/email)}</toEmail>,
<toLicense>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/licenseNumber)}</toLicense>

}</output>

"),

tig:create-stored-procedure("InitialPedigreeDetails","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber = $shipId]
//pedigree/shippedPedigree[documentInfo/serialNumber = $serualId ]

return <output>{

<drugName>{data($e//initialPedigree/productInfo/drugName )}</drugName>,
<productCode>{data($e//initialPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e//initialPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e//initialPedigree/productInfo/manufacturer )}</manufacturer>,
<quantity>{data($e//initialPedigree/itemInfo/quantity )}</quantity>,
<dosageForm>{data($e//initialPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e//initialPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e//initialPedigree/productInfo/containerSize)}</containerSize>,
<custName>{data($e/transactionInfo/senderInfo/businessAddress/businessName)}</custName>,
<custAddress>{concat(data($e/transactionInfo/senderInfo/businessAddress/street1),',',data($e/transactionInfo/senderInfo/businessAddress/street2),',',data($e/transactionInfo/senderInfo/businessAddress/city),',',data($e/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/transactionInfo/senderInfo/businessAddress/country))}</custAddress>,
<custContact>{data($e/transactionInfo/senderInfo/contactInfo/name)}</custContact>,
<custPhone>{data($e/transactionInfo/senderInfo/contactInfo/telephone)}</custPhone>,
<custEmail>{data($e/transactionInfo/senderInfo/contactInfo/email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/signatureInfo/signerInfo/name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/signatureInfo/signerInfo/title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/signatureInfo/signerInfo/telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/shippedPedigree/signatureInfo/signerInfo/email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/signatureInfo/signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/signatureInfo/signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/documentInfo/serialNumber)}</pedigreeId>,
<transactionDate>{data($e/transactionInfo/transactionDate)}</transactionDate>, 
<transactionType>{data($e/transactionInfo/transactionIdentifier/identifierType)}</transactionType>,
<transactionNo>{data($e/transactionInfo/transactionIdentifier/identifier)}</transactionNo>,
<fromCompany>{data($e/transactionInfo/senderInfo/businessAddress/businessName)}</fromCompany>,
<toCompany>{data($e/transactionInfo/recipientInfo/businessAddress/businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/transactionInfo/senderInfo/businessAddress/street1),',',data($e/transactionInfo/senderInfo/businessAddress/street2),',',data($e/transactionInfo/senderInfo/businessAddress/city),',',data($e/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/transactionInfo/senderInfo/businessAddress/country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/transactionInfo/senderInfo/shippingAddress/street1),',',data($e/transactionInfo/senderInfo/shippingAddress/street2),',',data($e/transactionInfo/senderInfo/shippingAddress/city),',',data($e/transactionInfo/senderInfo/shippingAddress/stateOrRegion),',',data($e/transactionInfo/senderInfo/shippingAddress/postalCode),',',data($e/transactionInfo/senderInfo/shippingAddress/country))}</fromBillAddress>,
<fromContact>{data($e/transactionInfo/senderInfo/contactInfo/name)}</fromContact>,
<fromTitle>{data($e/transactionInfo/senderInfo/contactInfo/title)} </fromTitle>,
<fromPhone>{data($e/transactionInfo/senderInfo/contactInfo/telephone)}</fromPhone>,
<fromEmail>{data($e/transactionInfo/senderInfo/contactInfo/email)}</fromEmail>,
<fromLicense>{data($e/transactionInfo/senderInfo/licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/transactionInfo/recipientInfo/businessAddress/street1),',',data($e/transactionInfo/recipientInfo/businessAddress/street2),',',data($e/transactionInfo/recipientInfo/businessAddress/city),',',data($e/transactionInfo/recipientInfo/businessAddress/stateOrRegion),',',data($e/transactionInfo/recipientInfo/businessAddress/postalCode),',',data($e/transactionInfo/recipientInfo/businessAddress/country))}</toShipAddress>,
<toBillAddress>{concat(data($e/transactionInfo/recipientInfo/shippingAddress/street1),',',data($e/transactionInfo/recipientInfo/shippingAddress/street2),',',data($e/transactionInfo/recipientInfo/shippingAddress/city),',',data($e/transactionInfo/recipientInfo/shippingAddress/stateOrRegion),',',data($e/transactionInfo/recipientInfo/shippingAddress/postalCode),',',data($e/transactionInfo/recipientInfo/shippingAddress/country))}</toBillAddress>,
<toContact>{data($e/transactionInfo/recipientInfo/contactInfo/name)}</toContact>,
<toTitle>{data($e/transactionInfo/recipientInfo/contactInfo/title)}</toTitle>,
<toPhone>{data($e/transactionInfo/recipientInfo/contactInfo/telephone)}</toPhone>,
<toEmail>{data($e/transactionInfo/recipientInfo/contactInfo/email)}</toEmail>,
<toLicense>{data($e/transactionInfo/recipientInfo/licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("GetStatusDetails","
declare variable $pedId as string external;
for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedId] 
for $status in $ps/Status   where $status/StatusChangedOn  = $ps/TimeStamp[1] return $status 
"),

tig:create-stored-procedure("InvoiceDetails","
declare variable $InvoiceId as string external; 
for $b in collection('tig:///ePharma/Invoices')/Invoice 
where $b/ID = $InvoiceId
return 
<output>{
<InvoiceNumber>{data($b/ID)}</InvoiceNumber>,
<SellersID>{data($b/OrderReference/SellersID)}</SellersID>,
<InvoiceDate>{concat(fn:get-month-from-dateTime($b/IssueDate) cast as string,'/',fn:get-day-from-dateTime($b/IssueDate)  cast as string,'/',fn:get-year-from-dateTime($b/IssueDate)  cast as string)}</InvoiceDate>,
<RequestedDeliveryDate>{concat(fn:get-month-from-dateTime($b/Delivery/RequestedDeliveryDateTime) cast as string,'/',fn:get-day-from-dateTime($b/Delivery/RequestedDeliveryDateTime)  cast as string,'/',fn:get-year-from-dateTime($b/Delivery/RequestedDeliveryDateTime)  cast as string)}</RequestedDeliveryDate>, 
<BuyerPartyName>{data($b/BuyerParty/Party/PartyName)}</BuyerPartyName>,
<BuyerAddress>{concat($b/BuyerParty/Party/Address/StreetName,' ',$b/BuyerParty/Party/Address/CityName,' ',$b/BuyerParty/Party/Address/CountrySubentityCode, ' ', $b/BuyerParty/Party/Address/PostalZone)}</BuyerAddress>, 
<BuyerContact>{data($b/$b/BuyerParty/Party/BuyerContact/Name)}</BuyerContact>,
<SellerPartyName>{data($b/SellerParty/Party/PartyName)}</SellerPartyName>,
<SellerAddress>{concat($b/SellerParty/Party/Address/StreetName,' ',$b/SellerParty/Party/Address/CityName,' ',$b/SellerParty/Party/Address/CountrySubentityCode, ' ', $b/SellerParty/Party/Address/PostalZone)}</SellerAddress>,
<ItemIdentificationNumber>{data($b/InvoiceLine/Item/SellersItemIdentification/ID)}</ItemIdentificationNumber>,
<ItemDescription>{data($b/InvoiceLine/Item/Description)}</ItemDescription>,
<QuantityOrdered>{data($b/InvoiceLine/InvoicedQuantity)}</QuantityOrdered>,
<InvoiceAmount>{data($b/InvoiceLine/LineExtensionAmount)}</InvoiceAmount>
}</output>
"),

tig:create-stored-procedure("MessageDetails","
declare variable $MessageID as string external; 
let $pedID := for $i in collection('tig:///ePharma/Alerts')/AlertMessage[MessageID = $MessageID]
	     return data($i/Message/DocId)
let $ped := for $b in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $pedID] 
	   return $b
	     
for $b in collection('tig:///ePharma/Alerts')/AlertMessage
where $b/MessageID = $MessageID
return 
<output>
{
<CreatedBy>{data($b/CreatedBy)}</CreatedBy>,
<MessageTitle>{data($b/Message/MessageTitle)}</MessageTitle>,
<RequiredAction>{data($b/Message/RequiredAction)}</RequiredAction>,
<Comments>{data($b/Message/Comments)}</Comments>,
<PriorityLevel>{data($b/Message/SeverityLevel)}</PriorityLevel>,
<DocId>{data($b/Message/DocId)}</DocId>,
if(count($ped) = 0) then  <Flag>PedLevel</Flag>
 else <Flag>EnvLevel</Flag>
}
</output>
"),
tig:create-stored-procedure("StringToNode","

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $ped as string external;

let $b := bin:parse(  binary{$ped} , 'text/xml' )
return $b
"),


tig:create-stored-procedure("UpdatePedigreeBankClearQuantity","
declare variable $swpLotNum as string external;
update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $swpLotNum
replace value of $i/InventoryOnHand/TotalInventory with 0
"),

tig:create-stored-procedure("CreateShippedPedigreeForClose","

declare variable $swpLotNum as string external;
declare variable $sessionId as string external;
declare variable $pedigree as item()* external; 
import module namespace util = 'xquery:modules:util';

declare function local:getPartnerInfo( $name as string) as node()*
{
let $k := (for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
where $i/name = $name
return 
$i
)	
return
$k	
};

let $NDC := for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank
	   where $i//SWLotNum = $swpLotNum
	   return $i/InventoryOnHand/SWNDC

let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>


let $sellerInfo := local:getPartnerInfo('Amgen Inc')
let $senderinfo := (
	<senderInfo>
	<businessAddress><businessName>SouthWood</businessName>
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>
	<city>{data($sellerInfo/address/city)}</city>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>
	</businessAddress>
	<shippingAddress><businessName>SouthWood</businessName>
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>
	<city>{data($sellerInfo/address/city)}</city>
	<stateOrRegion>{data($sellerInfo/address/state)}</stateOrRegion>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>
	</shippingAddress>
	<licenseNumber state='{data($sellerInfo/address/state)}' agency=''>{data($sellerInfo/businessId)}</licenseNumber>
	<contactInfo><name>{data($sellerInfo/contact)}</name><title>Receiving Manager</title>
	<telephone>{data($sellerInfo/phone)}</telephone><email>{data($sellerInfo/email)}</email>
	<url>{data($sellerInfo/notifyURI)}</url></contactInfo>
	</senderInfo>
)

let $recipientinfo := (
	<recipientInfo>
	<businessAddress><businessName>SouthWood</businessName>
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>
	<city>{data($sellerInfo/address/city)}</city>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>
	</businessAddress>
	<shippingAddress><businessName>SouthWood</businessName>
	<street1>{data($sellerInfo/address/line1)}</street1><street2>{data($sellerInfo/address/line2)}</street2>
	<city>{data($sellerInfo/address/city)}</city>
	<stateOrRegion>{data($sellerInfo/address/state)}</stateOrRegion>
	<postalCode>{data($sellerInfo/address/zip)}</postalCode><country>{data($sellerInfo/address/country)}</country>
	</shippingAddress>
	<licenseNumber state='{data($sellerInfo/address/state)}' agency=''>{data($sellerInfo/businessId)}</licenseNumber>
	<contactInfo><name>{data($sellerInfo/contact)}</name><title>Receiving Manager</title>
	<telephone>{data($sellerInfo/phone)}</telephone><email>{data($sellerInfo/email)}</email>
	<url>{data($sellerInfo/notifyURI)}</url></contactInfo>
	</recipientInfo>
)
let $envid := util:create-unique-id('PE')
let $docid := util:create-unique-id('S')
let $shipid := util:create-unique-id('ID')
let $pedenv := <PedigreeEnvelope>
		<documentInfo>
		  <serialNumber>{$envid}</serialNumber>
		  <version>1</version>
		</documentInfo>
		<date>{fn:current-dateTime()}</date>
		<source>SouthWood</source><destination>SouthWood</destination>
		<pedigree>
		   <shippedPedigree id='{$shipid}'>
			<documentInfo>
			   <serialNumber>{$docid}</serialNumber>
		  	   <version>1</version>
			</documentInfo>
			{$pedigree}
			<transactionInfo>
	   		  {$senderinfo}
			  {$recipientinfo}
		           <transactionIdentifier>
				<identifier></identifier><identifierType></identifierType>
			   </transactionIdentifier>
	   		   <transactionType>Sale</transactionType>
			   <transactionDate>{fn:current-dateTime()}</transactionDate>
			</transactionInfo>
			<signatureInfo>
			  {$signerInfo}
	 		  <signatureDate>{fn:current-dateTime()}</signatureDate>
	 		  <signatureMeaning>Created Unsigned</signatureMeaning>
			</signatureInfo>
		   </shippedPedigree>
		</pedigree>
	      </PedigreeEnvelope>
return tig:insert-document('tig:///ePharma/ShippedPedigree',$pedenv)
"),

tig:create-stored-procedure("CloseCall","

declare variable $swpLotNum as string external;
declare variable $sessionId as string external;
declare variable $pedigree as item()* external; 

let $qty := tlsp:UpdatePedigreeBankClearQuantity($swpLotNum)
let $res := tlsp:CreateShippedPedigreeForClose($swpLotNum,$sessionId,$pedigree)
return $res

"),


tig:create-stored-procedure("InsertSubscriberKeys","
declare variable $subscribeID as string external;
declare variable $PIN as string external;
declare variable $key as string external;
declare variable $keyType as string external;

tig:insert-document('tig:///ePharma/SubscriberKeys',
	<SubscriberKeys>
	   <SubscriberID>{$subscribeID}</SubscriberID>
	   <PIN>{$PIN}</PIN>
	   <Key>{$key}</Key>
	   <KeyType>{$keyType}</KeyType>
	</SubscriberKeys>)
"),

tig:create-stored-procedure("GetRPORInitialPedigreeDetails","

declare variable $pedID as string external;

let $ped := (for $j in collection('tig:///ePharma/ShippedPedigree')/pedigree
where $j/receivedPedigree/documentInfo/serialNumber = $pedID
return $j)
return  if(count($ped)= 0) then 
  <output><PedigreeType>Initial</PedigreeType>
  <Pedigree>{for $s in collection('tig:///ePharma/PaperPedigree')/PaperPedigree
  where $s/DocumentId = $pedID
  return $s/initialPedigree}
  </Pedigree>
  </output>
else 
  <output><PedigreeType>Received</PedigreeType>
	<Pedigree>{$ped}</Pedigree>
  </output>
"),

tig:create-stored-procedure("CreateShippedPedigreeForInvoice","

declare variable $despatchAdviseNo as string external;
declare variable $transactionType as string external;
declare variable $flag as string external;
declare variable $pedigree as item()* external; 
declare variable $sessionId as string external;
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

let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>
let $envid := util:create-unique-id('PE')
let $pdenv := (for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
where $k/ID = $despatchAdviseNo
return <PedigreeEnvelope>
<version>1</version>
<serialNumber>{$envid}</serialNumber>
<date>{fn:current-dateTime()}</date>
<source>{data($k/SellerParty/Party/PartyName/Name)}</source><destination>{data($k/BuyerParty/Party/PartyName/Name)}</destination>

{
for $s in (
<x> {for $NDC in data($k/DespatchLine/Item/SellersItemIdentification/ID)

let $quantity1 :=(for $x in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	        where $x/ID = $despatchAdviseNo 
	        and $x/DespatchLine/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DespatchLine/DeliveredQuantity)
	)
let $lotexp1 :=(  for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where $i/Description = 'LOTEXP'
		and $i/../../../../ID = $despatchAdviseNo
		return 
		data( $i/../ID)
	     )
let $sscc1 := (for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification
where $i/PhysicalAttribute/Description = 'SSCC'
and  $i/../../../ID = $despatchAdviseNo
return
data( $i/ID)
)

let $sellerInfo := local:getPartnerInfo(data($k/SellerParty/Party/PartyName/Name))
let $buyerInfo := local:getPartnerInfo(data($k/BuyerParty/Party/PartyName/Name))

let $senderinfo := (
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	where $k/ID = $despatchAdviseNo
	return <senderInfo><businessAddress><businessName>{data($k/SellerParty/Party/PartyName)}</businessName>
	<street1>{data($k/SellerParty/Party/Address/StreetName)}</street1><street2>{data($k/SellerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/SellerParty/Party/Address/CityName)}</city>
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
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
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
let $lotno1 :=( for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where( $i/Description = 'LOTNO'
		and $i/../../../../ID =  $despatchAdviseNo  )
		return 
		data ($i/../ID)
	     )
let $lotno := $lotno1
let $sscc := $sscc1
let $lotexp := $lotexp1
let $quantity := $quantity1
let $docid := util:create-unique-id('S')
let $shipid := util:create-unique-id('ID')
let $contid := util:create-unique-id('C')

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
if($flag = '3') then <repackagedPedigree/>
else $pedigree
  
}
	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{$despatchAdviseNo}</identifier><identifierType>{$transactionType}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{fn:current-dateTime()}</transactionDate>
	</transactionInfo>
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{fn:current-dateTime()}</signatureDate>
	  <signatureMeaning>Created Unsigned</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</PedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment($envid) :)
return $envid

"),

tig:create-stored-procedure("UpdatePedigreeBankQuantity","

declare variable $lotNum as string external;
declare variable $totQty as integer external;

update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $lotNum
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory - $totQty
"),

tig:create-stored-procedure("UpdatePedigreeBankNDC","
declare variable $swLotNum as string external;
declare variable $ndc as string external;

update
for $i in collection('tig:///ePharma/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $swLotNum
replace value of $i/InventoryOnHand/SWNDC with $ndc
"),



(:Usage:- tlsp:CreateInvoiceDoc('Inv12345', '2006-07-11', 'CVS Pharmacy', 'Spring St.', '413', 'Orlando', 'OL', '60123', '20', 'Viagra', '40 mg', 'oral', 
'50', 'NDC124-56') :)

tig:create-stored-procedure("CreateInvoiceDoc","

declare variable $SWPRefNum as string external;
declare variable $SWPRefDate as string external;
declare variable $CustomerName as string external;
declare variable $CustomerAddr1 as string external;
declare variable $CustomerAddr2 as string external;
declare variable $City as string external;
declare variable $State as string external;
declare variable $Zip as string external;
declare variable $Qty as string external;
declare variable $DrugName as string external;
declare variable $DrugForm as string external;
declare variable $PackSize as string external;
declare variable $DrugStrength as string external;
declare variable $NDC as string external;

let $inv := <Invoice>
		<ID>{$SWPRefNum}</ID>
		<IssueDate>{$SWPRefDate}</IssueDate>
		<OrderReference>
        			<BuyersID></BuyersID>
        			<SellersID></SellersID>
        			<IssueDate></IssueDate>
		</OrderReference>
		<BuyerParty>
		        <Party>
		            <PartyName>
                			<Name>{$CustomerName}</Name>
		            </PartyName>
		            <Address>
              			  <StreetName>{$CustomerAddr1}</StreetName>
                			  <BuildingNumber>{$CustomerAddr2}</BuildingNumber>
		                    <CityName>{$City}</CityName>
				  <StateName>{$State}</StateName> 	
			           <PostalZone>{$Zip}</PostalZone>		 	           
		            </Address>
       		        </Party>
		</BuyerParty>
		<SellerParty>
       			<Party>
		            <PartyName>
                			<Name>Southwood</Name>
		            </PartyName>
		            <Address>
		                <StreetName></StreetName>
                		       <BuildingNumber></BuildingNumber>
		                <CityName></CityName>
		                <PostalZone></PostalZone>		                
		            </Address>
        			</Party>        
		</SellerParty>
		<InvoiceDetails>        
		        <InvoicedQuantity quantityUnitCode=''>{$Qty}</InvoicedQuantity>
		        <Item>
  	        	        	       <Drug>{$DrugName}</Drug>
			       <DrugStrength>{$DrugStrength}</DrugStrength>
			       <DrugForm>{$DrugForm}</DrugForm>
			       <PackSize>{$PackSize}</PackSize>		
			       <NDC>{$NDC}</NDC>		                    		
        		       </Item>
    		</InvoiceDetails>
	   </Invoice> 

return tig:insert-document('tig:///ePharma/Invoices',$inv)
"),

tig:create-stored-procedure("GetSessionID","
declare variable $uID as string external;
for $s in collection('tig:///EAGRFID/SysSessions')/session
where $s/userid = $uID
return data($s/sessionid)
"),

tig:create-stored-procedure("InsertAndChangePedigreeStatus","

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $xmlString as string external;
declare variable $status as string external;

let $pedNode := tlsp:StringToNode($xmlString)
let $signerID := $pedNode/SQLXMLData/PedigreeData/signerID/text()
let $pedId := $pedNode/SQLXMLData/PedigreeData/pedigreeID/text()

let $sessionId := tlsp:GetSessionID($signerID)
let $time := fn:current-dateTime()

let $userid := (for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
)
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

tig:create-stored-procedure("InsertDocuments","

declare variable $xmlString as string external;
declare variable $collnName as string external;
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

let $pedNode := tlsp:StringToNode($xmlString)/node()
let $signerID := $pedNode//PedigreeData/signerID/text()

return
if($collnName = 'SQLPedRcv') then
	insert document $pedNode into  'tig:///ePharma/SQLPedRcv'
else if($collnName = 'SQLPedIn') then insert document $pedNode into  'tig:///ePharma/SQLPedRcv'
else insert document $pedNode into  'tig:///ePharma/SQLPedBadRcv'

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
 for $pedigree in collection('tig:///ePharma/ShippedPedigree')[pedigree//shippedPedigree/documentInfo/serialNumber = $pedId]/pedigree

 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
 where not( exists($pedigree/*:Signature) )
 replace  $pedigree  with  $pedigree_sign/pedigree
 
"),

tig:create-stored-procedure("InsertPedigreeInRP","

declare variable $p as node() external;

let $ped := (for $i in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope
	    return $i )
return 
insert document $p into  'tig:///ePharma/ReceivedPedigree'
"),


tig:create-stored-procedure("CreatedRepackagedElement","
declare variable $pedId as string external;
declare variable $xmlString as string external;
let $pedNode := tlsp:StringToNode($xmlString)
let $productNode := $pedNode//PedigreeData/ProductInfo
let $itemNode := $pedNode//PedigreeData/ItemInfo
let $pedigree := tlsp:GetRPORInitialPedigreeDetails($pedId)
let $pedigreeType := $pedigree/PedigreeType
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $pedNode//PedigreeData/signerId/text()
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>
return
if($pedigreeType = 'Received') then
let $RP := $pedigree/Pedigree/pedigree
return
<repackagedPedigree>
	<previousProducts>
		<previousProductInfo>
			<manufacturer>{data($RP//initialPedigree/productInfo/manufacturer)}</manufacturer>
			<productCode>{data($RP//initialPedigree/productInfo/productCode)}</productCode>
		</previousProductInfo>
		<itemInfo>
			<lot>{$RP/receivedPedigree/pedigree/shippedPedigree/itemInfo/lot/text()}</lot>
			<expirationDate>{$RP/receivedPedigree/pedigree/shippedPedigree/itemInfo/expirationDate/text()}</expirationDate>
			<quantity>{$RP/receivedPedigree/pedigree/shippedPedigree/itemInfo/quantity/text()}</quantity>
			<itemSerialNumber>{$RP/receivedPedigree/pedigree/shippedPedigree/itemInfo/itemSerialNumber/text()}</itemSerialNumber>
		</itemInfo>
		<contactInfo>
			<name>{data($RP/receivedPedigree/signatureInfo/signerInfo/name)}</name>
			<title>{data($RP/receivedPedigree/signatureInfo/signerInfo/title)}</title>
			<telephone>{data($RP/receivedPedigree/signatureInfo/signerInfo/telephone)}</telephone>
			<email>{data($RP/receivedPedigree/signatureInfo/signerInfo/email)}</email>
			<url>{data($RP/receivedPedigree/signatureInfo/signerInfo/url)}</url>
		</contactInfo>
	</previousProducts>
	<previousPedigree>{$RP}</previousPedigree>
	<productInfo>{$productNode//*}</productInfo>
	<itemInfo>{$itemNode//*}</itemInfo>
</repackagedPedigree>
else 
let $initial := $pedigree/Pedigree/initialPedigree
return <repackagedPedigree>
	<previousProducts>
		<previousProductInfo>
			<manufacturer>{data($initial/productInfo/manufacturer)}</manufacturer>
			<productCode>{data($initial/productInfo/productCode)}</productCode>
		</previousProductInfo>
		<itemInfo>
			<lot>{$initial/itemInfo/lot/text()}</lot>
			<expirationDate>{$initial/itemInfo/expirationDate/text()}</expirationDate>
			<quantity>{$initial/itemInfo/quantity/text()}</quantity>
			<itemSerialNumber>{$initial/itemInfo/itemSerialNumber/text()}</itemSerialNumber>
		</itemInfo>
		<contactInfo>
			<name>{data($signerInfo/name)}</name>
			<title>{data($signerInfo/title)}</title>
			<telephone>{data($signerInfo/telephone)}</telephone>
			<email>{data($signerInfo/email)}</email>
			<url>{data($signerInfo/url)}</url>
		</contactInfo>
	</previousProducts>
	<previousPedigree>{$initial}</previousPedigree>
	<productInfo>{$productNode//*}</productInfo>
	<itemInfo>{$itemNode//*}</itemInfo>
</repackagedPedigree>
"),

tig:create-stored-procedure("CreateShippedPedigreeRepacked","

declare variable $despatchAdviseNo as string external;
declare variable $transactionType as string external;
declare variable $flag as string external;
declare variable $pedigree as string external; 
declare variable $sessionId as string external;
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

let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>


let $envid := util:create-unique-id('PE')
let $pdenv := (for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
where $k/ID = $despatchAdviseNo
return <PedigreeEnvelope>
<version>1</version>
<serialNumber>{$envid}</serialNumber>
<date>{fn:current-dateTime()}</date>
<source>{data($k/SellerParty/Party/PartyName/Name)}</source><destination>{data($k/BuyerParty/Party/PartyName/Name)}</destination>

{
for $s in (
<x> {for $NDC in data($k/DespatchLine/Item/SellersItemIdentification/ID)

let $quantity1 :=(for $x in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	        where $x/ID = $despatchAdviseNo 
	        and $x/DespatchLine/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DespatchLine/DeliveredQuantity)
	)
let $lotexp1 :=(  for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where $i/Description = 'LOTEXP'
		and $i/../../../../ID = $despatchAdviseNo
		return 
		data( $i/../ID)
	     )
let $sscc1 := (for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification
where $i/PhysicalAttribute/Description = 'SSCC'
and  $i/../../../ID = $despatchAdviseNo
return
data( $i/ID)
)

let $sellerInfo := local:getPartnerInfo(data($k/SellerParty/Party/PartyName/Name))
let $buyerInfo := local:getPartnerInfo(data($k/BuyerParty/Party/PartyName/Name))

let $senderinfo := (
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
	where $k/ID = $despatchAdviseNo
	return <senderInfo><businessAddress><businessName>{data($k/SellerParty/Party/PartyName)}</businessName>
	<street1>{data($k/SellerParty/Party/Address/StreetName)}</street1><street2>{data($k/SellerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/SellerParty/Party/Address/CityName)}</city>
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
	for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
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
let $lotno1 :=( for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where( $i/Description = 'LOTNO'
		and $i/../../../../ID =  $despatchAdviseNo  )
		return 
		data ($i/../ID)
	     )
let $lotno := $lotno1
let $sscc := $sscc1
let $lotexp := $lotexp1
let $quantity := $quantity1
let $docid := util:create-unique-id('S')
let $shipid := util:create-unique-id('ID')
let $contid := util:create-unique-id('C')
let $transDate := for $n in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice[ID = $despatchAdviseNo]
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
if($flag = '3') then tlsp:CreatedRepackagedElement($pedigree,$xmlString)
else tlsp:GetRPDetails($pedigree)
  
}
	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{$despatchAdviseNo}</identifier><identifierType>{$transactionType}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{$transDate}</transactionDate>
	</transactionInfo>
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{fn:current-dateTime()}</signatureDate>
	  <signatureMeaning>Created Unsigned</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</PedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment($envid) :)
return $envid

"),

tig:create-stored-procedure("ShipGoods","

declare variable $invNum as string external;
declare variable $swLotNum as string external;
declare variable $swNDC as string external;
declare variable $xmlString as string external;

let $pedNode := tlsp:StringToNode($xmlString)
let $qty as xs:integer := xs:integer($pedNode//PedigreeData/ItemInfo/quantity)
let $containerSize as xs:integer := xs:integer($pedNode//PedigreeData/ProductInfo/containerSize)
let $signerId := $pedNode//PedigreeData/signerId/text()

let $sessionId := tlsp:GetSessionID($signerId)
let $rcvPedId := (for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[SWLotNum = $swLotNum]
		return data($i/ReceivedPedigreeID) )
return if(count($rcvPedId) = 0) then false() else 
let $pedigree := tlsp:GetRPORInitialPedigreeDetails($rcvPedId)

return 
if( count($pedigree) = 0 ) then false()
 else 
	let $totQty := $qty * $containerSize
	let $refNum := (for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice[OrderReference/BuyersID = $invNum]
			 return data($i/ID))
	let $envId :=  tlsp:CreateShippedPedigreeRepacked($refNum,'Invoice','3',$rcvPedId,$sessionId,$xmlString)
  	let $k :=  tlsp:UpdatePedigreeBankQuantity($swLotNum,$totQty)
	let $ndc :=  tlsp:UpdatePedigreeBankNDC($swLotNum,$swNDC)
(:	let $sign := tlsp:pedigreeSignature($envId)
:)
(:       let $sendPed :=  tlsp:SendPedigree($envId,'venu.gopal@sourcen.com','testepharma@sourcen.com', 'sniplpass')
:)
	return true()
      
"),

tig:create-stored-procedure("getRepackagedInfo","
declare variable $serialNumber as string external;
let $allPedigrees := 
 (
 for $i in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope/descendant::*:*
 where $i/name() = 'pedigree'
 return $i
 )

 for $ped in $allPedigrees[*:shippedPedigree/*:documentInfo/*:serialNumber = $serialNumber]
 return $ped/*:shippedPedigree
"),



tig:create-stored-procedure("getPreviousProducts","
declare variable $serialNumber as string external;
for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope/pedigree
where $e/shippedPedigree/documentInfo/serialNumber=$serialNumber
return 
<out><previousProducts>{$e/shippedPedigree[1]/repackagedPedigree/previousProducts}</previousProducts>
<productInfo>{$e/shippedPedigree[1]/repackagedPedigree/productInfo}</productInfo>
<itemInfo>{$e/shippedPedigree[1]/repackagedPedigree/itemInfo/*}</itemInfo>
</out>"),


tig:create-stored-procedure("FaxDetails","
declare variable $requestType as string external;
for $i in collection('tig:///ePharma/ReceivedFax')/InboundPostRequest 
where $i/FaxControl/Status = $requestType
return
<output>
{
<FaxName>{data($i/FaxControl/FaxName)}</FaxName>,
<CSID>{data($i/FaxControl/CSID)}</CSID>,
<ANI>{data($i/FaxControl/ANI)}</ANI>,
<DateReceived>{data($i/FaxControl/DateReceived)}</DateReceived>
}
</output>"),

tig:create-stored-procedure("createNewPedBankDocNewschema", "
declare variable $NDC as string external;
declare variable $recId as string external;
declare variable $pedRcv as node()* external;

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

let $swp_ndc := $pedRcv/SWP_Stock_Code
let $lot := (for $j in $pedRcv//LotInfo
let $itemInfo := (for $i in collection('tig:///ePharma/ShippedPedigree')/pedigree/receivedPedigree[documentInfo/serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$i}</pedigree>))

let $quantity := ( for $i in $itemInfo
		 where $i/lot = $j/Vendor_Lot_No 
		 return data($i/quantity))
return 
<LotInfo>
<LotNumber>{data($j/Vendor_Lot_No)}</LotNumber>
<SWLotNum>{data($j/SWP_Lot_Number)}</SWLotNum>
<ShippingInfo/>
<Quantity>{$quantity}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
</LotInfo>)

let $PedigreeBank :=
(
for $s in collection('tig:///ePharma/ShippedPedigree')/pedigree/receivedPedigree 
where $s/documentInfo/serialNumber=$recId
return
<PedigreeBank>
<InventoryOnHand>
<NDC>{$NDC}</NDC>
<SWNDC>{data($swp_ndc)}</SWNDC>
<TotalInventory>{data($s//initialPedigree/itemInfo/quantity)}</TotalInventory>
{$lot}
</InventoryOnHand>
</PedigreeBank>
)
return insert document $PedigreeBank into  'tig:///ePharma/PedigreeBank'

"),

tig:create-stored-procedure("UpDatePedigreeBankDocNew", "
declare variable $NDC as string external;
declare variable $recId as string external;
declare variable $pedRcv as node()* external;

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

let $lot := $pedRcv//LotInfo
let $lot := (for $j in $pedRcv//LotInfo
let $itemInfo := (for $i in collection('tig:///ePharma/ShippedPedigree')/pedigree/receivedPedigree[documentInfo/serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$i}</pedigree>))

let $quantity := ( for $i in $itemInfo
		 where $i/lot = $j/Vendor_Lot_No 
		 return data($i/quantity))
return (
<LotInfo>
<LotNumber>{data($j/Vendor_Lot_No)}</LotNumber>
<SWLotNum>{data($j/SWP_Lot_Number)}</SWLotNum>
<ShippingInfo/>
<Quantity>{$quantity}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
</LotInfo>))

return 
(
	
tlsp:insertLotinfoNode($NDC, $lot )
(:tlsp:UpDateTotalInventory($NDC,  xs:integer($quantity) :)

)


"),

tig:create-stored-procedure("CreateReceivedPedigreeForPedigrees1","
import module namespace util = 'xquery:modules:util';
declare variable $pedRcv as node()* external;
declare variable $signerId as string external;
declare variable $pedigreeId as string external;

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $signerId
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>

for $k in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree

let $docid := util:create-unique-id('ID')
let $recvid := util:create-unique-id('R')
let $ndc := $k//initialPedigree/productInfo/productCode
where $k/shippedPedigree/documentInfo/serialNumber = $pedigreeId
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
			<dateReceived>{fn:current-dateTime()}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//initialPedigree/itemInfo/lot)}</lot>
                    		<expirationDate>{data($k//initialPedigree/itemInfo/expirationDate)}</expirationDate> 
                    		<quantity>{data($k//initialPedigree/itemInfo/quantity)}</quantity>
                    		<itemSerialNumber>{data($k//initialPedigree/itemInfo/itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{fn:current-dateTime()}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
 ,tlsp:UpDatePedigreeBankDocNew($ndc,$recvid,$pedRcv ))

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
			<dateReceived>{fn:current-dateTime()}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//initialPedigree/itemInfo/lot)}</lot>
                    		<expirationDate>{data($k//initialPedigree/itemInfo/expirationDate)}</expirationDate> 
                    		<quantity>{data($k//initialPedigree/itemInfo/quantity)}</quantity>
                    		<itemSerialNumber>{data($k//initialPedigree/itemInfo/itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{fn:current-dateTime()}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
,tlsp:createNewPedBankDocNewschema($ndc,$recvid,$pedRcv)) 
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
	
return (
	 tlsp:CreateReceivedPedigreeForPedigrees1($i,data($i/SignerID) cast as string,data($i/PedigreeID) cast as string), 
	 tlsp:CreateSignToReceivedPedigree(data($i/PedigreeID) cast as string),
	 tlsp:InsertAndChangePedigreeStatus1(data($i/SignerID) cast as string,data($i/PedigreeID) cast as string,'Received And Authenticated')
)
"),


tig:create-stored-procedure("ShippingPedigreeDetails","

declare variable $serialNumber as string external;

declare function local:getProductInfo($e as node()){
if(exists($e/shippedPedigree/repackagedPedigree)) then(
<drugName>{data($e/shippedPedigree/repackagedPedigree/productInfo/drugName)}</drugName>,
<productCode>{data($e/shippedPedigree/repackagedPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e/shippedPedigree/repackagedPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e/shippedPedigree/repackagedPedigree/productInfo/manufacturer)}</manufacturer>,
<quantity>{data($e/shippedPedigree/repackagedPedigree/itemInfo/quantity )}</quantity>,
<dosageForm>{data($e/shippedPedigree/repackagedPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e/shippedPedigree/repackagedPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e/shippedPedigree/repackagedPedigree/productInfo/containerSize)}</containerSize>
)
else(
<drugName>{data($e//initialPedigree/productInfo/drugName )}</drugName>,
<productCode>{data($e//initialPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e//initialPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e//initialPedigree/productInfo/manufacturer )}</manufacturer>,
<quantity>{data($e//initialPedigree/itemInfo/quantity)}</quantity>,
<dosageForm>{data($e//initialPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e//initialPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e//initialPedigree/productInfo/containerSize)}</containerSize>
)
};

for $e in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope//pedigree
where $e/shippedPedigree/documentInfo/serialNumber=$serialNumber

return <output>{
<repackage>{exists($e/shippedPedigree/repackagedPedigree)}</repackage>,
local:getProductInfo($e),
<custName>{data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</custName>,
<custAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</custAddress>,
<custContact>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</custContact>,
<custPhone>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</custPhone>,
<custEmail>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/shippedPedigree/signatureInfo/signerInfo/name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/shippedPedigree/signatureInfo/signerInfo/title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/shippedPedigree/signatureInfo/signerInfo/telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/shippedPedigree/signatureInfo/signerInfo/email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/shippedPedigree/signatureInfo/signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/shippedPedigree/signatureInfo/signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/shippedPedigree/documentInfo/serialNumber)}</pedigreeId>,
<transactionDate>{data($e/shippedPedigree/transactionInfo/transactionDate)}</transactionDate>, 
<transactionType>{data($e/shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</transactionType>,
<transactionNo>{data($e/shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</transactionNo>,
<fromCompany>{data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</fromCompany>,
<toCompany>{data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street1),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street2),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/city),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/senderInfo/shippingAddress/country))}</fromBillAddress>,
<fromContact>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</fromContact>,
<fromTitle>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/title)} </fromTitle>,
<fromPhone>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</fromPhone>,
<fromEmail>{data($e/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</fromEmail>,
<fromLicense>{data($e/shippedPedigree/transactionInfo/senderInfo/licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street1),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street2),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/city),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/recipientInfo/businessAddress/country))}</toShipAddress>,
<toBillAddress>{concat(data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street1),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street2),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/city),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/stateOrRegion),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/postalCode),',',data($e/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/country))}</toBillAddress>,
<toContact>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/name)}</toContact>,
<toTitle>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/title)}</toTitle>,
<toPhone>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/telephone)}</toPhone>,
<toEmail>{data($e/shippedPedigree/transactionInfo/recipientInfo/contactInfo/email)}</toEmail>,
<toLicense>{data($e/shippedPedigree/transactionInfo/recipientInfo/licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("GetBinaryImageForServlet","
declare binary-encoding none;
declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support'; 

declare variable $node as binary() external;
declare variable $node-name as xs:string external; 

declare function local:extract($str as xs:string,$node-name as xs:string) as xs:string {
  let $end := concat( '</', $node-name, '>' )
  let $start := concat( '<', $node-name, '>' )
  return  substring-before( substring-after($str,$start) , $end ) 
};

    bin:base64-decode(  local:extract( bin:as-string($node,'utf-8') , $node-name )  )

"),

tig:create-stored-procedure("InitialPedigreeDetailsRP","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $shipId]
//pedigree/shippedPedigree[documentInfo/serialNumber = $serualId ]

return <output>{

<drugName>{data($e//initialPedigree/productInfo/drugName )}</drugName>,
<productCode>{data($e//initialPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e//initialPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e//initialPedigree/productInfo/manufacturer )}</manufacturer>,
<quantity>{data($e//initialPedigree/itemInfo/quantity )}</quantity>,
<dosageForm>{data($e//initialPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e//initialPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e//initialPedigree/productInfo/containerSize)}</containerSize>,
<custName>{data($e/transactionInfo/senderInfo/businessAddress/businessName)}</custName>,
<custAddress>{concat(data($e/transactionInfo/senderInfo/businessAddress/street1),',',data($e/transactionInfo/senderInfo/businessAddress/street2),',',data($e/transactionInfo/senderInfo/businessAddress/city),',',data($e/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/transactionInfo/senderInfo/businessAddress/country))}</custAddress>,
<custContact>{data($e/transactionInfo/senderInfo/contactInfo/name)}</custContact>,
<custPhone>{data($e/transactionInfo/senderInfo/contactInfo/telephone)}</custPhone>,
<custEmail>{data($e/transactionInfo/senderInfo/contactInfo/email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/signatureInfo/signerInfo/name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/signatureInfo/signerInfo/title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/signatureInfo/signerInfo/telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/shippedPedigree/signatureInfo/signerInfo/email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/signatureInfo/signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/signatureInfo/signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/documentInfo/serialNumber)}</pedigreeId>,
<transactionDate>{data($e/transactionInfo/transactionDate)}</transactionDate>, 
<transactionType>{data($e/transactionInfo/transactionIdentifier/identifierType)}</transactionType>,
<transactionNo>{data($e/transactionInfo/transactionIdentifier/identifier)}</transactionNo>,
<fromCompany>{data($e/transactionInfo/senderInfo/businessAddress/businessName)}</fromCompany>,
<toCompany>{data($e/transactionInfo/recipientInfo/businessAddress/businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/transactionInfo/senderInfo/businessAddress/street1),',',data($e/transactionInfo/senderInfo/businessAddress/street2),',',data($e/transactionInfo/senderInfo/businessAddress/city),',',data($e/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/transactionInfo/senderInfo/businessAddress/country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/transactionInfo/senderInfo/shippingAddress/street1),',',data($e/transactionInfo/senderInfo/shippingAddress/street2),',',data($e/transactionInfo/senderInfo/shippingAddress/city),',',data($e/transactionInfo/senderInfo/shippingAddress/stateOrRegion),',',data($e/transactionInfo/senderInfo/shippingAddress/postalCode),',',data($e/transactionInfo/senderInfo/shippingAddress/country))}</fromBillAddress>,
<fromContact>{data($e/transactionInfo/senderInfo/contactInfo/name)}</fromContact>,
<fromTitle>{data($e/transactionInfo/senderInfo/contactInfo/title)} </fromTitle>,
<fromPhone>{data($e/transactionInfo/senderInfo/contactInfo/telephone)}</fromPhone>,
<fromEmail>{data($e/transactionInfo/senderInfo/contactInfo/email)}</fromEmail>,
<fromLicense>{data($e/transactionInfo/senderInfo/licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/transactionInfo/recipientInfo/businessAddress/street1),',',data($e/transactionInfo/recipientInfo/businessAddress/street2),',',data($e/transactionInfo/recipientInfo/businessAddress/city),',',data($e/transactionInfo/recipientInfo/businessAddress/stateOrRegion),',',data($e/transactionInfo/recipientInfo/businessAddress/postalCode),',',data($e/transactionInfo/recipientInfo/businessAddress/country))}</toShipAddress>,
<toBillAddress>{concat(data($e/transactionInfo/recipientInfo/shippingAddress/street1),',',data($e/transactionInfo/recipientInfo/shippingAddress/street2),',',data($e/transactionInfo/recipientInfo/shippingAddress/city),',',data($e/transactionInfo/recipientInfo/shippingAddress/stateOrRegion),',',data($e/transactionInfo/recipientInfo/shippingAddress/postalCode),',',data($e/transactionInfo/recipientInfo/shippingAddress/country))}</toBillAddress>,
<toContact>{data($e/transactionInfo/recipientInfo/contactInfo/name)}</toContact>,
<toTitle>{data($e/transactionInfo/recipientInfo/contactInfo/title)}</toTitle>,
<toPhone>{data($e/transactionInfo/recipientInfo/contactInfo/telephone)}</toPhone>,
<toEmail>{data($e/transactionInfo/recipientInfo/contactInfo/email)}</toEmail>,
<toLicense>{data($e/transactionInfo/recipientInfo/licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("ShippedORReceivedPedigreeDetailsRP","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

let $e := (for $i in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope//shippedPedigree[documentInfo/serialNumber = $shipId]
//receivedPedigree[documentInfo/serialNumber = $serualId]
return $i)
(:
let $e := ( for $i in collection(concat('tig:///ePharma/',$catalogName))/PedigreeEnvelope//documentInfo[ serialNumber = $serualId]
	   return $i/parent::*):)
return <output>{

<drugName>{data($e//initialPedigree/productInfo/drugName )}</drugName>,
<productCode>{data($e//initialPedigree/productInfo/productCode )}</productCode>,
<codeType>{data($e//initialPedigree/productInfo/productCode/@type )}</codeType>,
<manufacturer>{data($e//initialPedigree/productInfo/manufacturer )}</manufacturer>,
<quantity>{data($e//initialPedigree/itemInfo/quantity )}</quantity>,
<dosageForm>{data($e//initialPedigree/productInfo/dosageForm )}</dosageForm>,
<strength>{data($e//initialPedigree/productInfo/strength )}</strength>,
<containerSize>{data($e//initialPedigree/productInfo/containerSize)}</containerSize>,
<custName>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</custName>,
<custAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</custAddress>,
<custContact>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</custContact>,
<custPhone>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</custPhone>,
<custEmail>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/pedigree/shippedPedigree/signatureInfo/signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/pedigree/shippedPedigree/signatureInfo/signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/pedigree/shippedPedigree/documentInfo/serialNumber)}</pedigreeId>,
<transactionDate>{data($e/pedigree/shippedPedigree/transactionInfo/transactionDate)}</transactionDate>, 
<transactionType>{data($e/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</transactionType>,
<transactionNo>{data($e/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</transactionNo>,
<fromCompany>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</fromCompany>,
<toCompany>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/shippingAddress/country))}</fromBillAddress>,
<fromContact>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/name)}</fromContact>,
<fromTitle>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/title)} </fromTitle>,
<fromPhone>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/telephone)}</fromPhone>,
<fromEmail>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/contactInfo/email)}</fromEmail>,
<fromLicense>{data($e/pedigree/shippedPedigree/transactionInfo/senderInfo/licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/businessAddress/country))}</toShipAddress>,
<toBillAddress>{concat(data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street1),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/street2),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/city),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/stateOrRegion),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/postalCode),',',data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/shippingAddress/country))}</toBillAddress>,
<toContact>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/name)}</toContact>,
<toTitle>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/title)}</toTitle>,
<toPhone>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/telephone)}</toPhone>,
<toEmail>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/contactInfo/email)}</toEmail>,
<toLicense>{data($e/pedigree/shippedPedigree/transactionInfo/recipientInfo/licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("getAuthPedStatus","
declare variable $pedId as string external; 
for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID =$pedId]
for $status in $ps/Status where $status/StatusChangedOn = $ps/TimeStamp
return <output><status>{$status/StatusChangedTo/text()}</status><time>{substring-before($status/StatusChangedOn/text(),'T')}</time>
<date>{substring-before(substring-after($status/StatusChangedOn/text(),'T'),'.')}</date></output>") ,


tig:drop-stored-procedure("repnode"),
tig:create-stored-procedure("repnode","
declare binary-encoding none;
 
declare variable $n as xs:string external ;
declare variable $docid as xs:string external ;
replace collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber = $docid]/altPedigree
with <altPedigree><mimeType>application/pdf</mimeType><encoding>base64binary</encoding><data>{$n}</data></altPedigree>"),

tig:create-stored-procedure("InsertPedIdInefax", "
declare variable $pedId as string external;
declare variable $docId as string external;
insert <InitialPedigreeId>{$pedId}</InitialPedigreeId> as first into doc($docId)/*:InboundPostRequest"),


tig:create-stored-procedure("ChangeFaxStatus", "
declare variable $pid as string external;
update
for $i in collection('tig:///ePharma/ReceivedFax')/InboundPostRequest[InitialPedigreeId = $pid]
replace value of $i/FaxControl/Status with 1
"),

tig:drop-stored-procedure("repEncoding"),
tig:create-stored-procedure("repEncoding","
declare binary-encoding none;
declare variable $docid as xs:string external ;
replace collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber = $docid]/altPedigree/data
with    doc('file:///c:/Temp/abc.xml')/data  ")



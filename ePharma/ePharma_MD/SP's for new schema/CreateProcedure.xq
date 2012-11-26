
(: To Display Alert Message :)
tig:create-stored-procedure("DisplayMessage_MD","

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
for $l in collection('tig:///ePharma_MD/Alerts')/AlertMessage 
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
tig:create-stored-procedure("DeleteMessage_MD",
"
declare variable $MessageID as string external;
for $i in collection('tig:///ePharma_MD/Alerts') 
where $i/AlertMessage/MessageID = $MessageID 
return tig:delete-document(document-uri($i))
"),

(: To Create an Alert Message :)
tig:create-stored-procedure("CreateMessage_MD",
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

tig:insert-document('tig:///ePharma_MD/Alerts', <AlertMessage><CreatedDate>{$Date}</CreatedDate><CreatedBy>{$CreatedBy}</CreatedBy><MessageID>{$MessageID}</MessageID><GroupName>{$GroupName}</GroupName><TargetUserId>{$UserId}</TargetUserId><RelatedProcess>{$Process}</RelatedProcess><Message><MessageTitle>{$Title}</MessageTitle><DocType>{$DocType}</DocType><DocId>{$DocId}</DocId><SeverityLevel>{$SLevel}</SeverityLevel><RequiredAction>{$Action}</RequiredAction><Comments>{$Comments}</Comments></Message><Status>{$Status}</Status></AlertMessage>)
" ),

tig:create-stored-procedure("BuyersID_MD",
"
declare variable $ASNNum as string external;
for $l in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice
where $l/ID = $ASNNum 
return data($l/OrderReference/BuyersID) 
")
,

tig:create-stored-procedure("TPEmailID_MD",
"
declare variable $documentID as string external;
distinct-values(
for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
where $i/*:serialNumber = $documentID
return 
let $num := data($i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber) 
	for $j in collection('tig:///CatalogManager/TradingPartner')/TradingPartner 
	where data($i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber)  = data($j/businessId) 
	return 
		if(fn:exists(data($j/notifyURI))) 
		then  (data($j/notifyURI)) else (data($j/email)))
"),

(: For Validate Access Privileges :)
tig:create-stored-procedure("validateAccess_MD",
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

tig:create-stored-procedure('updateLastUse_MD',"
declare variable $sesId as string external;

update 
for $d in collection('tig:///EAGRFID/SysSessions')/session[sessionid=$sesId]
replace value of $d/lastuse with fn:current-dateTime()
"),

(: For Validating Session :)
tig:create-stored-procedure('validateSession_MD',"

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
tlsp:updateLastUse_MD($sesId)
)
else
(
'false'
)

"),

tig:create-stored-procedure('ValidateStatus_MD',"

declare variable $sessionId as string external;
declare variable $acclevel as string external;
declare variable $type as string external;

<ValidateStatus>

<SessionStatus> {
   	if (tlsp:validateSession_MD($sessionId)='false') then
	'false'
	else
	'true'
   }
</SessionStatus>


<AccessStatus> {tlsp:validateAccess_MD($sessionId,$acclevel,$type)} </AccessStatus>

</ValidateStatus>

"),

(: EAG Time Stamp :)
tig:create-stored-procedure("CreateEAG-TimeStampType_MD","
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

tig:create-stored-procedure("GetEAG-TimeStampTypeForRepository_MD","
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
tig:create-stored-procedure("PedigreeDetails_MD","
declare variable $TransactionNumber as string external ;
let $all :=
for $b in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
where $b/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier = $TransactionNumber
order by $b/*:serialNumber descending
return $b
let $recent := $all[1]
let $peid := data($recent/*:serialNumber)
let $transNo := data($recent/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)
return <output>{
         <DocumentId>{data($recent/*:serialNumber)}</DocumentId> ,
         <TransactionNumber>{$transNo[1]}</TransactionNumber> ,
         <DateTime>{data($recent/*:date)}</DateTime>,
	<Name>{data($recent/*:sourceRoutingCode)}</Name>,
	<Count>{count($recent/*:pedigree)}</Count> 	
}</output>
"),

(: Insert Pedigree Status Document :)
tig:create-stored-procedure("InsertPedigreeStatus_MD","

declare variable $pedigreeId as string external;
declare variable $status as string external;
declare variable $sessionId as string external;

let $userid := (for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
)
return
tig:insert-document('tig:///ePharma_MD/PedigreeStatus', 
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
tig:create-stored-procedure("PedigreeBankCheck_MD","

declare variable $despatchAdviceNo as string external;

let $NDC := (for $k in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice
where $k/ID = $despatchAdviceNo 
return data($k/DespatchLine/Item/SellersItemIdentification/ID)
)
let $lotno :=( for $i in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute
		where $i/Description = 'LOTNO'
		and $i/../../../../ID = $despatchAdviceNo
		return 
		data( $i/../ID)
	     )
for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand
where $i/NDC = $NDC and $i/LotInfo/LotNumber = $lotno
return data($i/LotInfo/ReceivedPedigreeID)
"),

(: Get Received Pedigree Details :)
tig:create-stored-procedure("GetRPDetails_MD","
declare variable $pedID as string external;

for $j in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree
where $j/*:receivedPedigree/*:documentInfo/*:serialNumber = $pedID
return $j
"),

tig:create-stored-procedure("ReturnsDetails_MD","

declare variable $pedId as string external;
let $recvdId := (for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree
where $i/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId
return data($i/*:receivedPedigree/*:documentInfo/*:serialNumber) )

let $lotInfo := (for $k in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[ReceivedPedigreeID = $recvdId and Returned = 'true']
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
tig:create-stored-procedure("createNewPedigreeBankDoc_MD", "
declare variable $NDC as string external;
declare variable $recId as string external;
let $PedigreeBank :=
(
for $s in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree 
where $s/*:documentInfo/*:serialNumber=$recId
return
(
<PedigreeBank>
<InventoryOnHand>
<NDC>{$NDC}</NDC>
<TotalInventory>{data($s//*:initialPedigree/*:itemInfo/*:quantity)}</TotalInventory>
<LotInfo>
<LotNumber>{data($s//*:initialPedigree/*:itemInfo/*:lot)}</LotNumber>
<ShippingInfo/>
<Quantity>{data($s//*:initialPedigree/*:itemInfo/*:quantity)}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
</LotInfo>
</InventoryOnHand>
</PedigreeBank>
)
)
return insert document $PedigreeBank into  'tig:///ePharma_MD/PedigreeBank'
"),

(: To Insert LotInfo Node:)
tig:create-stored-procedure("insertLotinfoNode_MD", "
declare variable $NDC as string external;
declare variable $LotInfo as node()* external;
update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
insert
$LotInfo as last into $i/InventoryOnHand"),

(:To Update Total Inventory:)
tig:create-stored-procedure("UpDateTotalInventory_MD", "
declare variable $NDC as string external;
declare variable $value as integer external;
update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory+$value
"),

(:To update PedigreeBank for receipt of goods:)
tig:create-stored-procedure("UpDatePedigreeBank_MD", "
declare variable $NDC as string external;
declare variable $recId as string external;
let $LotInfo as node()? :=
(<LotInfo>{
for $s in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree 
where $s/*:documentInfo/*:serialNumber=$recId
return 
(
<LotNumber>{data($s//*:initialPedigree/*:itemInfo/*:lot)}</LotNumber>,
<Quantity>{data($s//*:initialPedigree/*:itemInfo/*:quantity)}</Quantity>,
<LocationID></LocationID>,
<SerialNumber></SerialNumber>,
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
)
}</LotInfo>
)
return 
(
tlsp:insertLotinfoNode_MD($NDC, $LotInfo ),
tlsp:UpDateTotalInventory_MD($NDC,  xs:integer( $LotInfo/*:Quantity )  )
)

 "),
(:To insert shipping info node to pedigreebank :)
tig:create-stored-procedure("insertShippingInfoNodetoPedigreeBank_MD", "
declare variable $NDC as string external;
declare variable $lot as string external;
declare variable $shippingInfo as node()* external;

update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank/InventoryOnHand[NDC=$NDC]/LotInfo
where $i/LotNumber=$lot
insert
$shippingInfo after $i/LotNumber"),

(:To Update TotalInventory for shipping:)
tig:create-stored-procedure("UpDateTotalInventoryforShipping_MD", "
declare variable $NDC as string external;
declare variable $value as integer external;
update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory - $value
"),
(:To update Lot Quantity for Shipping:)
tig:create-stored-procedure("UpDateLotQuantityforShipping_MD", "
declare variable $NDC as string external;
declare variable $lot as string external;
declare variable $quantity as integer external;

update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank/InventoryOnHand[NDC=$NDC]/LotInfo
where $i/LotNumber=$lot
replace value of $i/Quantity with $i/Quantity - $quantity
"),

(:To update pedigree after shipment:)
tig:create-stored-procedure("updatePedigreebankAfterShipment_MD", "
declare variable $serialNumber as string external;
for $s in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber=$serialNumber]/*:pedigree 
let $NDC := data($s/*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode) cast as string
let $lot := data($s/*:shippedPedigree//*:initialPedigree/*:itemInfo/*:lot) cast as string
let $quantity := data($s/*:shippedPedigree//*:initialPedigree/*:itemInfo/*:quantity) cast as integer
let $ShippingInfo :=
(<ShippingInfo>
                <Quantity>{$quantity}</Quantity>
                <ShippedPedigreeID>{data($s/*:shippedPedigree/*:documentInfo/*:serialNumber)}</ShippedPedigreeID>
 </ShippingInfo>
)

return 
(
tlsp:insertShippingInfoNodetoPedigreeBank_MD($NDC,$lot,$ShippingInfo),
tlsp:UpDateTotalInventoryforShipping_MD($NDC,$quantity),
tlsp:UpDateLotQuantityforShipping_MD($NDC,$lot,$quantity)
)
"),
(:to Check whether the NDC exists in the Pedigree Bank or not:)
tig:create-stored-procedure("ndcExists_MD","
 declare variable $ndc as string external;
 
 for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand 
 where upper-case($i/NDC) = upper-case($ndc)
 return true()
"),

(: For Create shipped pedigree :)
tig:create-stored-procedure("CreateShippedPedigree_MD","

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

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
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
let $itemserial := $itemserial1
let $docid := util:create-uuid()
let $shipid := util:create-uuid()
let $contid := util:create-uuid()
let $res := tlsp:GetRPDetails_MD($pedID)

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber>{$itemserial}</itemSerialNumber><productCode type='NDC'>{data($productNode/productCode)}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>

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
		<identifier>{$despatchAdviseNo}</identifier><identifierType>{$transactionType}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{$date}</transactionDate>
	</transactionInfo>
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{$dateTime}</signatureDate>
	  <signatureMeaning>Certified</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</pedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment_MD($envid) :)
(:let $sig := tlsp:pedigreeSignature_MD($envid):)
return $ret 

"),
(: For Creatin and Updating Status :)
tig:create-stored-procedure("insertNode_MD","
declare variable $n1 as node() external;
declare variable $uri as string external;
update for $b in doc($uri)//TimeStamp
replace $b with 
         $n1/* "),
tig:create-stored-procedure("InsertAndChangeStatus_MD","

declare variable $pedId as string external;
declare variable $status as string external;
declare variable $sessionId as string external;

let $time := fn:current-dateTime()

let $userid := (for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
)
let $data := ( for $i in collection ('tig:///ePharma_MD/PedigreeStatus') 
		where $i/PedigreeStatus/PedigreeID = $pedId
		return $i )
return if (empty($data))
	then
	    tig:insert-document('tig:///ePharma_MD/PedigreeStatus', 
		<PedigreeStatus><PedigreeID>{$pedId}</PedigreeID>
		   <Status>
  		   	<StatusChangedOn>{$time}</StatusChangedOn>
			<StatusChangedTo>{$status}</StatusChangedTo>
			<UserId>{$userid}</UserId>
		   </Status>
		   <TimeStamp>{$time}</TimeStamp>
		</PedigreeStatus>
)
       else (tlsp:insertNode_MD(<x> <Status>
  			     	 <StatusChangedOn>{$time}</StatusChangedOn>
   			     	 <StatusChangedTo>{$status}</StatusChangedTo>
  			      	<UserId>{$userid}</UserId>
			      </Status>
			      <TimeStamp>{$time}</TimeStamp></x>,document-uri($data)))
"),
(: For Shipping Manager Pedigree Detail Screen :)

tig:create-stored-procedure("ShipEnvelopeDetails_MD","
declare function local:getPedigreeType($envelopId as xs:string){
  for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envelopId]/*:pedigree
  return 
   if( exists( $i/*:shippedPedigree/*:pedigree )  ) then 'Shipped-Received' else if( exists( $i/*:shippedPedigree/*:initialPedigree) ) then 'Shipped-Initial'  else 'Unknown'
};

declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
where $e/*:serialNumber = $envelopeid
return <output>{
<ContainerCode>{data($e/*:container/*:containerCode)}</ContainerCode>,
<PedigreeID>{data($e/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)}</PedigreeID>, 
<Quantity>{data($e/*:container/*:pedigreeHandle/*:quantity )}</Quantity>,
<LotNum>{data($e/*:ontainer/*:pedigreeHandle/*:lot)}</LotNum>,
<PedigreeType>{local:getPedigreeType($envelopeid)}</PedigreeType>,
<TransactionDate>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</TransactionDate>,
<DrugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</DrugName>,
<DrugCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</DrugCode>,
<Attachement>EDI ASN DATA</Attachement>,
<Envelope>{data($e/*:serialNumber)}</Envelope>,
<Date>{data($e/*:date)}</Date>, 
<source>{data($e/*:source)}</source>,
<destination>{data($e/*:destination)}</destination>,
<count>{data(count($e/*:pedigree/*:shippedPedigree))}</count>
}</output>
"),

(: For Receiving Manager :)
tig:create-stored-procedure("getSearchQueries2_MD",
"
 declare variable $root_node as xs:string {'$root'};
 declare variable $common_str as xs:string {""
 
 declare function local:getStatus($pedID as xs:string?) as xs:string {

 for $ps in collection('tig:///ePharma_MD/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedID] 
 for $status in $ps/Status 
 where $status/StatusChangedOn  = $ps/TimeStamp[1]
 return $status/StatusChangedTo/text()
   
};

 ""};
 declare variable $recvd_local_fn_1 as xs:string {""declare function local:returnPedigrees($root as document-node()? ) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/*:pedigreeEnvelope/*:pedigree

 ""};

 declare variable $recvd_local_fn_3 as xs:string {"" 
 return 
 <Record>
 {
  <pedigreeID>{$ped/*:shippedPedigree/*:documentInfo/*:serialNumber/text()}</pedigreeID>,
  <envelopID>{$root/*:pedigreeEnvelope/*:serialNumber/text()}</envelopID>,
  <dateRecieved>{data($ped/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</dateRecieved>,
  <tradingPartner>{$ped/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/*:shippedPedigree/*:documentInfo/*:serialNumber )
  }</status>,
  <createdBy>System</createdBy>,
  <docURI>{$docURI}</docURI>
 }
 </Record>	
 };
 ""};


 declare variable $shipped_local_fn_1 as xs:string {""declare function local:returnPedigrees($root as document-node()?) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/*:pedigree/*:receivedPedigree
 ""};
 
 declare variable $shipped_local_fn_3 as xs:string {"" 
 return <Record>
 {
  <pedigreeID>{$ped/*:documentInfo/*:serialNumber/text()}</pedigreeID>,
  <envelopID>N/A</envelopID>,
   <dateRecieved>{data($ped/*:receivingInfo/*:dateReceived)}</dateRecieved>,
  <tradingPartner>{$ped/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/*:documentInfo/*:serialNumber )
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
					<Collection>tig:///ePharma_MD/ReceivedPedigree</Collection>	
					<Collection>tig:///ePharma_MD/ShippedPedigree</Collection>	
				</Collections>
		   		<Key>
					<Name>FromDate</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigreeEnvelope/*:date/text()</Path>
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
						<Path>*:pedigreeEnvelope/*:date/text()</Path>
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
						<Path>*:pedigreeEnvelope/*:container/*:containerCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>No</PedPrefix>			
						<Path><![CDATA[<Dummy>{data($ped/*:shippedPedigree/*:documentInfo/*:serialNumber)}</Dummy>]]></Path>
						<Operator>=</Operator>
						<Value>$root/*:pedigreeEnvelope/*:container[*:containerCode = '$UserVal']/*:pedigreeHandle/*:serialNumber</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>NDC</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigreeEnvelope/*:pedigree/*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
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
						<Path>*:pedigreeEnvelope/*:container/*:pedigreeHandle/*:lot</Path>
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
						<Path>*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
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
						<Path>*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:shippedPedigree/*:documentInfo/*:serialNumber</Path>
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
						<Path>*:pedigreeEnvelope/*:sourceRoutingCode</Path>
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
						<Path>local:getStatus( $root/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber/text() )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/*:shippedPedigree/*:documentInfo/*:serialNumber )</Path>
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
						<Path>*:pedigreeEnvelope/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	     			
			</Scenario>
			
			<Scenario>	
				<Collections>
					<Collection>tig:///ePharma_MD/ShippedPedigree</Collection>	
				</Collections>
		   		<Key>
					<Name>FromDate</Name>
					<RootSelection>
					    <Query>		
						<RootPrefix>Yes</RootPrefix>	
						<Path>*:pedigree/*:receivedPedigree/*:receivingInfo/*:dateReceived/text()</Path>
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
						<Path>*:pedigree/*:receivedPedigree/*:receivingInfo/*:dateReceived/text()</Path>
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
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:pedigree/*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
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
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree//*:initialPedigree/*:itemInfo/*:lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>		
						<PedPrefix>Yes</PedPrefix>		
						<Path>*:pedigree/*:shippedPedigree//*:initialPedigree/*:itemInfo/lot</Path>
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
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
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
						<Path>*:pedigree/*:receivedPedigree/*:documentInfo/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:documentInfo/*:serialNumber</Path>
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
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName</Path>
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
						<Path>local:getStatus($root/*:pedigree/*:receivedPedigree/*:documentInfo/*:serialNumber/text())</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/*:documentInfo/*:serialNumber )</Path>
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

tig:create-stored-procedure("getSearchResults2_MD","

 declare variable $search_elt_names_ext as xs:string* external;
 declare variable $search_elt_values_ext as xs:string* external;   
 
 let $queries  as xs:string+ :=  tlsp:getSearchQueries2_MD( $search_elt_names_ext , $search_elt_values_ext )
 return <SearchInfo>{ 
 for $query in $queries
 return evaluate( $query )
 }
 </SearchInfo> 
"),
tig:create-stored-procedure("EnvelopeSearch_MD","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2_MD( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma_MD/ReceivedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope
where $envelope/*:serialNumber = $distinct_eids
return <output> 
{
<envelopeId>{data($envelope/*:serialNumber)}</envelopeId>,
<dateRecieved>{data($envelope/*:date)}</dateRecieved>,
<source>{data($envelope/*:sourceRoutingCode)}</source>,
<destination>{data($envelope/*:destinationRoutingCode)}</destination>,
<count>{count($envelope/*:pedigree)}</count>,
<createdBy>System</createdBy>
}

</output> 
"),
tig:create-stored-procedure("EnvelopeDisplay_MD","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/*:serialNumber)}</EnvelopeID>,
<Date>{data($e/*:date)}</Date>, 
<source>{data($e/*:sourceRoutingCode)}</source>,
<destination>{data($e/*:destinationRoutingCode)}</destination>
}</output>
"),
tig:create-stored-procedure("PedStatusElement_MD","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber= $envelopeid]
let $status := (for $i in collection('tig:///ePharma_MD/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'Received'
"),
tig:create-stored-procedure("EnvelopeDetails_MD","
declare variable $envelopeid as string external;
declare function local:getProductInfo($e as node()){
if(exists($e/*:shippedPedigree/*:repackagedPedigree)) then(
<DrugName>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:drugName)}</DrugName>,
<DrugCode>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode )}</DrugCode>

)
else(
<DrugName>{data($e//*:initialPedigree/*:productInfo/*:drugName)}</DrugName>,
<DrugCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</DrugCode>

)
};
for $e in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber=$envelopeid]
return
   for $ped in $e/*:pedigree
   let $pedID as xs:string := string($ped/*:shippedPedigree/*:documentInfo/*:serialNumber)
return <output> {
	<pedigreeid>{ $pedID }</pedigreeid>,
	 local:getProductInfo($ped),
	<TransactionDate>{data($ped/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</TransactionDate>,
	<Attachement>{data($ped//*:initialPedigree/*:attachment/*:mimeType)}</Attachement>,
	<containerCode>{
	 data( $e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:containerCode )
	 }</containerCode>,
	<Quantity>{data($e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:pedigreeHandle/*:quantity)}</Quantity>,
	<LotNum>{data($e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:pedigreeHandle/*:lot)}</LotNum>,
	<status>{ tlsp:PedStatusElement_MD($envelopeid, $pedID ) }</status>,
	<count>{data(count($e/*:pedigree))}</count>

}</output>
"),

 tig:create-stored-procedure("ShippedPedigreedetails_MD","
declare variable $serialNumber as string external;

declare function local:getProductInfo($e as node()){
if(exists($e/*:shippedPedigree/*:repackagedPedigree)) then(
<drugName>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:drugName)}</drugName>,
<productCode>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/productCode )}</productCode>,
<codeType>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:manufacturer)}</manufacturer>,
<quantity>{data($e/*:shippedPedigree/*:repackagedPedigree/*:itemInfo/*:quantity )}</quantity>,
<dosageForm>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:containerSize)}</containerSize>
)
else(
<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data($e//*:initialPedigree/*:itemInfo/*:quantity)}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>
)
};

for $e in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree
where $e/*:shippedPedigree/*:documentInfo/*:serialNumber=$serialNumber

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
"),
tig:create-stored-procedure("getManufacturerDetails_MD","
declare variable $catalogName as xs:string external;
declare variable $pedId as string external;
let $mfrName :=( for $n in collection(concat('tig:///ePharma_MD/',$catalogName))/*:pedigreeEnvelope
		where $n/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId
                  return data($n//*:initialPedigree/*:productInfo/*:manufacturer))
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
tig:create-stored-procedure("getAuditTrailDetails_MD","
declare variable $catalogName as xs:string external;
declare variable $serialId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in collection(concat('tig:///ePharma_MD/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
return $i/parent::*:*)
return ($k,$k//descendant::*:*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in collection(concat('tig:///ePharma_MD/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
return $i/parent::*:*)
return ($k,$k//descendant::*:*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else ('No collection found')
"),

tig:create-stored-procedure("getAttachmentDetails_MD","
declare variable $catalogName as xs:string external;
declare variable $serialId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in 
collection(concat('tig:///ePharma_MD/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
return $i/parent::*:*)
return <data>{ 
<true>{exists($k//*:initialPedigree/*:attachment)}</true>,
<mimeType>{data($k//*:initialPedigree/*:attachment/*:mimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k//*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</TransactionType>,
<Date>{data($k//*:shippedPedigree/*:transactionInfo/*:transactionDate)}</Date>,
<TransactionNo>{data($k//*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)}</TransactionNo>
}
</data>
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in 
collection(concat('tig:///ePharma_MD/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
return $i/parent::*:*)
return <data>{ 
<true>{exists($k//*:initialPedigree/*:attachment)}</true>,
<mimeType>{data($k//*:initialPedigree/*:attachment/*:mimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</TransactionType>,
<Date>{data($k/*:transactionInfo/*:transactionDate)}</Date>,
<TransactionNo>{data($k/*:transactionInfo/*:transactionIdentifier/*:identifier)}</TransactionNo>
}
</data>
else ('No collection found')
"),

tig:create-stored-procedure("ProductDetails_MD","

declare variable $pedId as string external;

<output>{
for $j in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree
where $j/*:documentInfo/*:serialNumber = $pedId
return $j//*:initialPedigree/*:productInfo
}
<item>{
for $item in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree
where $item/*:documentInfo/*:serialNumber = $pedId
return $item/*:itemInfo
}
</item>
<root>{
let $a := (for $n in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree
           where $n/*:documentInfo/*:serialNumber = $pedId 
           return data($n//*:initialPedigree/*:productInfo/*:productCode))
for $i in collection('tig:///CatalogManager/ProductMaster')/Product 
where $i/NDC = $a  return $i
}</root>
<products>{
let $a := (for $n in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree
           where $n/*:documentInfo/*:serialNumber = $pedId 
           return data($n//*:initialPedigree/*:productInfo/*:productCode))
for $k in collection('tig:///EAGRFID/Products')/Product 
where $k/NDC = $a return $k
}
</products></output>
"),

tig:create-stored-procedure("RecGetProductId_MD","
declare variable $pedId as string external;
let $ndc := (for $i in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree
where  $i/*:documentInfo/*:serialNumber = $pedId
return data($i/*:initialPedigree/*:productInfo/*:productCode) )

for $j in collection('tig:///EAGRFID/Products')/Product
where $j/NDC = $ndc
return data($j/ProductID)
"),

tig:create-stored-procedure("getStatusOfPedigree_MD","

declare variable $pedId as string external;
for $ps in collection('tig:///ePharma_MD/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedId] 
for $status in $ps/Status 
where $status/StatusChangedOn  = $ps/TimeStamp[1]
return $status/StatusChangedTo/text()
"),

tig:create-stored-procedure("getStatusOfPedigrees_MD","

declare variable $envelopId as string external;

for $ped in collection ('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envelopId]
let $pedid := data($ped/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)
for $ps in collection('tig:///ePharma_MD/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedid] 
for $status in $ps/Status 
where $status/StatusChangedOn  = $ps/TimeStamp[1]
return $status/StatusChangedTo/text()

"),

tig:create-stored-procedure("getBuyersId_MD","

declare variable $pedId as string external;
(:let $trNo :=  data(tlsp:getSearchResults2_MD(('PedID'),('S1138115791625B'))/Record/transactionNumber):)
let $trNo :=  data(tlsp:getSearchResults2_MD(('PedID'),($pedId))/Record/transactionNumber)
for $k in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice 
where $k/ID = $trNo
return data($k/OrderReference/BuyersID)
"),
tig:create-stored-procedure("getInvoiceId_MD","

declare variable $pedId as string external;
(:let $trNo :=  data(tlsp:getSearchResults2_MD(('PedID'),('S1138115791625B'))/Record/transactionNumber):)
let $buyersId :=  tlsp:getBuyersId_MD($pedId)
for $k in collection('tig:///ePharma_MD/Invoices')/Invoice 
where $k/OrderReference/BuyersID = $buyersId
return data($k/ID)
"),

tig:create-stored-procedure("InvoiceDetails_MD","
declare variable $InvoiceId as string external; 
for $b in collection('tig:///ePharma_MD/Invoices')/Invoice 
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

tig:create-stored-procedure("MessageDetails_MD","
declare variable $MessageID as string external; 
let $pedID := for $i in collection('tig:///ePharma_MD/Alerts')/AlertMessage[MessageID = $MessageID]
	     return data($i/Message/DocId)
let $ped := for $b in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $pedID] 
	   return $b
	     
for $b in collection('tig:///ePharma_MD/Alerts')/AlertMessage
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

tig:create-stored-procedure("CreateReceivedPedigreeForPedigrees_MD","

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

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')

for $k in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree

let $docid := util:create-uuid()
let $recvid := util:create-uuid()
let $ndc := $k//*:initialPedigree/*:productInfo/*:productCode
where $k/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedigreeId

return
if(tlsp:ndcExists_MD($ndc) ) then
(
tig:insert-document('tig:///ePharma_MD/ShippedPedigree',
<pedigree>
  	<receivedPedigree id='{$docid}'>
	 	<documentInfo>
	  		<serialNumber>{$recvid}</serialNumber>
	  		<version>1</version>
		</documentInfo>
		{$k}
		<receivingInfo>
			<dateReceived>{$date}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//*:initialPedigree/*:itemInfo/*:lot)}</lot>
                    		<expirationDate>{data($k//*:initialPedigree/*:itemInfo/*:expirationDate)}</expirationDate> 
                    		<quantity>{data($k//*:initialPedigree/*:itemInfo/*:quantity)}</quantity>
                    		<itemSerialNumber>{data($k//*:initialPedigree/*:itemInfo/*:itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{$dateTime}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
 ,tlsp:UpDatePedigreeBank_MD($ndc,$recvid ))

else
(
tig:insert-document('tig:///ePharma_MD/ShippedPedigree',
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
                    		<lot>{data($k//*:initialPedigree/*:itemInfo/*:lot)}</lot>
                    		<expirationDate>{data($k//*:initialPedigree/*:itemInfo/*:expirationDate)}</expirationDate> 
                    		<quantity>{data($k//*:initialPedigree/*:itemInfo/*:quantity)}</quantity>
                    		<itemSerialNumber>{data($k//*:initialPedigree/*:itemInfo/*:itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{fn:current-dateTime()}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
</pedigree> )
,tlsp:createNewPedigreeBankDoc_MD($ndc,$recvid)) 

"),

	

tig:create-stored-procedure("ReceivedPedigreeDetails_MD","
declare variable $pedigreeId as string external;

let $recvId := ( for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree
where $i/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedigreeId
return data($i/*:receivedPedigree/*:documentInfo/*:serialNumber)
)

for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree
where $i/*:receivedPedigree/*:documentInfo/*:serialNumber = $recvId
return
<result>
<pedigreeid>{$recvId}</pedigreeid>
<daterecvd>{data($i/*:receivedPedigree/*:receivingInfo/*:dateReceived)}</daterecvd>
<lotnum>{data($i//*:initialPedigree/*:itemInfo/*:lot)}</lotnum>
<quantity>{data($i//*:initialPedigree/*:itemInfo/*:quantity)}</quantity>
<expirationdate>{data($i//*:initialPedigree/*:itemInfo/*:expirationDate)}</expirationdate>
<itemserialnumber>{data($i//*:initialPedigree/*:itemInfo/*:itemSerialNumber)}</itemserialnumber>
<signame>{data($i/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:name)}</signame>
<title>{data($i/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:title)}</title>
<telephone>{data($i/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:telephone)}</telephone>
<sigemail>{data($i/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:email)}</sigemail>
<url>{data($i/*:receivedPedigree/*:signatureInfo/*:signerInfo/*:url)}</url>
<signaturedate>{data($i/*:receivedPedigree/*:signatureInfo/*:signatureDate)}</signaturedate>
</result>
"),

tig:create-stored-procedure("ShippingEnvelopeSearch_MD","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2_MD( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma_MD/ShippedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
where $envelope/*:serialNumber = $distinct_eids
return <output> 
{
<DocumentId>{data($envelope/*:serialNumber)}</DocumentId>,
<TransactionNumber>{data($envelope/*:pedigree[1]/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)}</TransactionNumber>,
<DateTime>{data($envelope/*:date)}</DateTime>,
<Name>{data($envelope/*:sourceRoutingCode)}</Name>,
<destination>{data($envelope/*:destinationRoutingCode)}</destination>,
<count>{count($envelope/*:pedigree)}</count>,

<Status> 
  {	
   distinct-values(
   for $i in $all_record
   where $i/envelopID = $envelope/*:serialNumber
   return $i/envelopeStatus/string()
   )
  }</Status>
}

</output> 
"),

tig:create-stored-procedure("pedigreeLevelSignature_MD","
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
 for $pedigree in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber =  $EnvelopeId 
	and *:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId]/*:pedigree

 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
(: where not( exists($pedigree/*:Signature) ):)
replace  $pedigree/*:Signature  with  $pedigree_sign/*:pedigree/*:Signature[2]

 
"),


tig:create-stored-procedure("GetProductId_MD","

declare variable $pedId as string external;
let $ndc := (for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
where  $i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId
return data($i/*:pedigree/*:shippedPedigree/*:initialPedigree/*:productInfo/*:productCode) )

for $j in collection('tig:///EAGRFID/Products')/Product
where $j/NDC = $ndc
return data($j/ProductID)
"),

tig:create-stored-procedure("GetProductDetails_MD","
declare variable $envId as string external;
declare variable $pedId as string external;
declare variable $ndc as string external;

<output>{
for $j in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
where $j/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId
return $j//*:initialPedigree/*:productInfo
}
<item>{
for $item in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
where $item/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId
return $item//*:initialPedigree/*:itemInfo
}
</item>
<root>{
let $a := (for $n in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
           where $n/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId 
           return data($n//*:initialPedigree/*:productInfo/*:productCode))
for $i in collection('tig:///CatalogManager/ProductMaster')/Product 
where $i/NDC = $a  return $i
}</root>
<products>{
let $a := (for $n in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope
           where $n/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId 
           return data($n//*:initialPedigree/*:productInfo/*:productCode))
for $k in collection('tig:///EAGRFID/Products')/Product 
where $k/NDC = $a return $k
}
</products></output>
"),
tig:create-stored-procedure("GetBinaryImage_MD","
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

tig:create-stored-procedure("ShippingManagerPedigreeDetails_MD","

declare variable $serialNumber as string external;
for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree
where $e/*:shippedPedigree/*:documentInfo/*:serialNumber=$serialNumber

return <output>{

<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data($e//*:initialPedigree/*:itemInfo/*:quantity )}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>,
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
"),

tig:create-stored-procedure("ManufacturerDetails_MD","

declare variable $pedId as string external;
let $mfrName :=( for $n in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber= $pedId]
                  return data($n//*:initialPedigree/*:productInfo/*:manufacturer))
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


(:Create Pedigree for Orders :)
tig:create-stored-procedure("CreateShippedPedigreeForOrders_MD","
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

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
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
let $quantity1 :=(for $x in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice[ID = $despatchAdviseNo]/DespatchLine
	        where $x/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DeliveredQuantity))
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
let $itemserial := $itemserial1
let $docid := util:create-uuid()
let $shipid := util:create-uuid()
let $contid := util:create-uuid()
let $res := tlsp:GetRPDetails_MD($pedID)

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber>{$itemserial}</itemSerialNumber><productCode type='NDC'>{data($productNode/productCode)}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>

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
	   <transactionDate>{$date}</transactionDate>
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
(:let $ret := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment_MD($envid) :)
return $ret :)
return $pdenv
"),

tig:create-stored-procedure("ShippingMgnrEnvelopesDisplay_MD","
declare variable $envelopeid as string external;
declare function local:getProductInfo($e as node()){
if(exists($e/*:shippedPedigree/*:repackagedPedigree)) then(
<DrugName>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:drugName)}</DrugName>,
<DrugCode>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode )}</DrugCode>

)
else(
<DrugName>{data($e//*:initialPedigree/*:productInfo/*:drugName)}</DrugName>,
<DrugCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</DrugCode>

)
};

for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber =  $envelopeid]
return 
 for $ped in $e/*:pedigree
 let $pedID as xs:string := string($ped/*:shippedPedigree/*:documentInfo/*:serialNumber)
 return <pedigree>{
       <pedigreeid>{ $pedID }</pedigreeid>,
       local:getProductInfo($ped),
       <TransactionDate>{data($ped/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</TransactionDate>,
       <count>{data(count($e/*:pedigree))}</count>,
       <status>{ tlsp:ShippingStatusElement_MD($envelopeid, $pedID ) }</status>,
      <containerCode>{
       data( $e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:containerCode )
      }</containerCode>,
     <Quantity>{data($e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:pedigreeHandle/*:quantity)}</Quantity>,
     <LotNum>{data($e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:pedigreeHandle/*:lot)}</LotNum>, 
     <Attachement>{data($ped//*:initialPedigree/*:attachment/*:mimeType)}</Attachement>
}</pedigree>
"),

tig:create-stored-procedure("ShippingMgnrEnvelopeDetails_MD","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/*:serialNumber)}</EnvelopeID>,
<Date>{data($e/*:date)}</Date>, 
<source>{data($e/*:sourceRoutingCode)}</source>,
<destination>{data($e/*:destinationRoutingCode)}</destination>
}</output>
"),
tig:create-stored-procedure("GetUserInfo_MD","
declare variable $sessionId as string external;
let $userId := (for $x in collection('tig:///EAGRFID/SysSessions')/session
where $x/sessionid = $sessionId
return data($x/userid) )

for $i in collection('tig:///EAGRFID/SysUsers')/User 
where $i/UserID = $userId
return data($i/AccessLevel/Access)
"),
tig:create-stored-procedure("ShippingStatusElement_MD","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber= $envelopeid]
(:let $pedId :=data($e/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber):)
let $status := (for $i in collection('tig:///ePharma_MD/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'NA'
"),

tig:create-stored-procedure("ShippedORReceivedPedigreeDetails_MD","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope//*:shippedPedigree[*:documentInfo/*:serialNumber = $shipId]
//*:receivedPedigree[*:documentInfo/*:serialNumber = $serualId]

return <output>{

<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data($e//*:initialPedigree/*:itemInfo/*:quantity )}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>,
<custName>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</custName>,
<custAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</custAddress>,
<custContact>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</custContact>,
<custPhone>{data($e/*:pedigree/*:shippedPedigree/vtransactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</custPhone>,
<custEmail>{data($e/*:pedigree/*:shippedPedigree/vtransactionInfo/*:senderInfo/*:contactInfo/*:email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)}</pedigreeId>,
<transactionDate>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</transactionDate>, 
<transactionType>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</transactionType>,
<transactionNo>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)}</transactionNo>,
<fromCompany>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</fromCompany>,
<toCompany>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street2),',',data($e/pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:country))}</fromBillAddress>,
<fromContact>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</fromContact>,
<fromTitle>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:title)} </fromTitle>,
<fromPhone>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</fromPhone>,
<fromEmail>{data($e/*:pedigree/shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</fromEmail>,
<fromLicense>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:country))}</toShipAddress>,
<toBillAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:country))}</toBillAddress>,
<toContact>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:name)}</toContact>,
<toTitle>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:title)}</toTitle>,
<toPhone>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:telephone)}</toPhone>,
<toEmail>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:email)}</toEmail>,
<toLicense>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:ecipientInfo/*:licenseNumber)}</toLicense>

}</output>

"),

tig:create-stored-procedure("InitialPedigreeDetails_MD","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $shipId]
//*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = $serualId ]

return <output>{

<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data($e//*:initialPedigree/*:itemInfo/*:quantity )}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>,
<custName>{data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</custName>,
<custAddress>{concat(data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</custAddress>,
<custContact>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</custContact>,
<custPhone>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</custPhone>,
<custEmail>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/*:signatureInfo/*:signerInfo/*:name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/*:signatureInfo/*:signerInfo/*:title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/*:signatureInfo/*:signerInfo/*:telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/*:signatureInfo/*:signerInfo/*:url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/*:signatureInfo/*:signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/*:documentInfo/*:serialNumber)}</pedigreeId>,
<transactionDate>{data($e/*:transactionInfo/*:transactionDate)}</transactionDate>, 
<transactionType>{data($e/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</transactionType>,
<transactionNo>{data($e/*:transactionInfo/*:transactionIdentifier/*:identifier)}</transactionNo>,
<fromCompany>{data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</fromCompany>,
<toCompany>{data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street1),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street2),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:city),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:postalCode),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:country))}</fromBillAddress>,
<fromContact>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</fromContact>,
<fromTitle>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:title)} </fromTitle>,
<fromPhone>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</fromPhone>,
<fromEmail>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</fromEmail>,
<fromLicense>{data($e/*:transactionInfo/*:senderInfo/*:licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street1),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street2),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:city),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:postalCode),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:country))}</toShipAddress>,
<toBillAddress>{concat(data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street1),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street2),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:city),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:postalCode),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:country))}</toBillAddress>,
<toContact>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:name)}</toContact>,
<toTitle>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:title)}</toTitle>,
<toPhone>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:telephone)}</toPhone>,
<toEmail>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:email)}</toEmail>,
<toLicense>{data($e/*:transactionInfo/*:recipientInfo/*:licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("GetStatusDetails_MD","
declare variable $pedId as string external;
for $ps in collection('tig:///ePharma_MD/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedId] 
for $status in $ps/Status   where $status/StatusChangedOn  = $ps/TimeStamp[1] return $status 
"),



tig:create-stored-procedure("StringToNode_MD","

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $ped as string external;

let $b := bin:parse(  binary{$ped} , 'text/xml' )
return $b
"),


tig:create-stored-procedure("UpdatePedigreeBankClearQuantity_MD","
declare variable $swpLotNum as string external;
update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $swpLotNum
replace value of $i/InventoryOnHand/TotalInventory with 0
"),

tig:create-stored-procedure("CreateShippedPedigreeForClose_MD","

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

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $NDC := for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank
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


let $sellerInfo := local:getPartnerInfo('Southwood Pharmaceuticals')
let $senderinfo := (
	<senderInfo>
	<businessAddress><businessName>Southwood Pharmaceuticals</businessName>
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
	<businessAddress><businessName>Southwood Pharmaceuticals</businessName>
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
let $envid := util:create-uuid()
let $docid := util:create-uuid()
let $shipid := util:create-uuid()
let $pedenv := <pedigreeEnvelope>
		<documentInfo>
		  <serialNumber>{$envid}</serialNumber>
		  <version>1</version>
		</documentInfo>
		<date>{$date}</date>
		<sourceRoutingCode>SouthWood</sourceRoutingCode><destinationRoutingCode>SouthWood</destinationRoutingCode>
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
			   <transactionDate>{$date}</transactionDate>
			</transactionInfo>
			<signatureInfo>
			  {$signerInfo}
	 		  <signatureDate>{$dateTime}</signatureDate>
	 		  <signatureMeaning>Created Unsigned</signatureMeaning>
			</signatureInfo>
		   </shippedPedigree>
		</pedigree>
	      </pedigreeEnvelope>
return tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pedenv)
"),

tig:create-stored-procedure("CloseCall_MD","

declare variable $swpLotNum as string external;
declare variable $sessionId as string external;
declare variable $pedigree as item()* external; 

let $qty := tlsp:UpdatePedigreeBankClearQuantity_MD($swpLotNum)
let $res := tlsp:CreateShippedPedigreeForClose_MD($swpLotNum,$sessionId,$pedigree)
return $res

"),


tig:create-stored-procedure("InsertSubscriberKeys_MD","
declare variable $subscribeID as string external;
declare variable $PIN as string external;
declare variable $key as string external;
declare variable $keyType as string external;

tig:insert-document('tig:///ePharma_MD/SubscriberKeys',
	<SubscriberKeys>
	   <SubscriberID>{$subscribeID}</SubscriberID>
	   <PIN>{$PIN}</PIN>
	   <Key>{$key}</Key>
	   <KeyType>{$keyType}</KeyType>
	</SubscriberKeys>)
"),

tig:create-stored-procedure("GetRPORInitialPedigreeDetails_MD","

declare variable $pedID as string external;

let $ped := (for $j in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree
where $j/*:receivedPedigree/*:documentInfo/*:serialNumber = $pedID
return $j)
return  if(count($ped)= 0) then 
  <output><PedigreeType>Initial</PedigreeType>
  <Pedigree>{for $s in collection('tig:///ePharma_MD/PaperPedigree')/initialPedigree
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

tig:create-stored-procedure("CreateShippedPedigreeForInvoice_MD","

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

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
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

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber></itemSerialNumber><productCode type='NDC'>{$NDC}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>

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
	   <transactionDate>{$date}</transactionDate>
	</transactionInfo>
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{$dateTime}</signatureDate>
	  <signatureMeaning>Certified</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</pedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment_MD($envid) :)
return $envid

"),

tig:create-stored-procedure("UpdatePedigreeBankQuantity_MD","

declare variable $lotNum as string external;
declare variable $totQty as integer external;

update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $lotNum
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory - $totQty
"),

tig:create-stored-procedure("UpdatePedigreeBankNDC_MD","
declare variable $swLotNum as string external;
declare variable $ndc as string external;

update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank
where $i//SWLotNum = $swLotNum
replace value of $i/InventoryOnHand/SWNDC with $ndc
"),



(:Usage:- tlsp:CreateInvoiceDoc_MD('Inv12345', '2006-07-11', 'CVS Pharmacy', 'Spring St.', '413', 'Orlando', 'OL', '60123', '20', 'Viagra', '40 mg', 'oral', 
'50', 'NDC124-56') :)

tig:create-stored-procedure("CreateInvoiceDoc_MD","

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

return tig:insert-document('tig:///ePharma_MD/Invoices',$inv)
"),

tig:create-stored-procedure("GetSessionID_MD","
declare variable $uID as string external;
for $s in collection('tig:///EAGRFID/SysSessions')/session
where $s/userid = $uID
return data($s/sessionid)
"),

tig:create-stored-procedure("InsertAndChangePedigreeStatus_MD","

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $xmlString as string external;
declare variable $status as string external;

let $pedNode := tlsp:StringToNode_MD($xmlString)
let $signerID := $pedNode/SQLXMLData/PedigreeData/signerID/text()
let $pedId := $pedNode/SQLXMLData/PedigreeData/pedigreeID/text()

let $sessionId := tlsp:GetSessionID_MD($signerID)
let $time := fn:current-dateTime()

let $userid := (for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
)
let $data := ( for $i in collection ('tig:///ePharma_MD/PedigreeStatus') 
		where $i/PedigreeStatus/PedigreeID = $pedId
		return $i )
return if (empty($data))
	then
	    tig:insert-document('tig:///ePharma_MD/PedigreeStatus', 
		<PedigreeStatus><PedigreeID>{$pedId}</PedigreeID>
		   <Status>
  		   	<StatusChangedOn>{$time}</StatusChangedOn>
			<StatusChangedTo>{$status}</StatusChangedTo>
			<UserId>{$userid}</UserId>
		   </Status>
		   <TimeStamp>{$time}</TimeStamp>
		</PedigreeStatus>
)
       else (tlsp:insertNode_MD(<x> <Status>
  			     	 <StatusChangedOn>{$time}</StatusChangedOn>
   			     	 <StatusChangedTo>{$status}</StatusChangedTo>
  			      	<UserId>{$userid}</UserId>
			      </Status>
			      <TimeStamp>{$time}</TimeStamp></x>,document-uri($data)))
"),

tig:create-stored-procedure("InsertDocuments_MD","

declare variable $xmlString as string external;
declare variable $collnName as string external;
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

let $pedNode := tlsp:StringToNode_MD($xmlString)/node()
let $signerID := $pedNode//PedigreeData/signerID/text()

return
if($collnName = 'SQLPedRcv') then
	insert document $pedNode into  'tig:///ePharma_MD/SQLPedRcv'
else if($collnName = 'SQLPedIn') then insert document $pedNode into  'tig:///ePharma_MD/SQLPedRcv'
else insert document $pedNode into  'tig:///ePharma_MD/SQLPedBadRcv'

"),

tig:create-stored-procedure("CreateSignToReceivedPedigree_MD","
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
 for $pedigree in collection('tig:///ePharma_MD/ShippedPedigree')[*:pedigree/*:receivedPedigree/*:documentInfo/*:serialNumber = $pedId]/*:pedigree

 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
 where not( exists($pedigree/*:Signature) )
 replace  $pedigree  with  $pedigree_sign/pedigree
 
"),

tig:create-stored-procedure("InsertPedigreeInRP_MD","

declare variable $p as node() external;

let $ped := (for $i in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope
	    return $i )
return 
insert document $p into  'tig:///ePharma_MD/ReceivedPedigree'
"),


tig:create-stored-procedure("CreatedRepackagedElement_MD","
declare variable $pedId as string external;
declare variable $xmlString as string external;
let $pedNode := tlsp:StringToNode_MD($xmlString)
let $productNode := $pedNode//PedigreeData/ProductInfo
let $itemNode := $pedNode//PedigreeData/ItemInfo
let $pedigree := tlsp:GetRPORInitialPedigreeDetails_MD($pedId)
let $pedigreeType := $pedigree/PedigreeType
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User


where $y/UserID = $pedNode//PedigreeData/signerId/text()
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
let $item := (<itemInfo>
		{$itemNode/lot}{$itemNode/expirationDate}{$itemNode/quantity}{$itemNode/itemSerialNumber}
	     </itemInfo>)
return
<repackagedPedigree>
	<previousProducts>
		<previousProductInfo>
			<manufacturer>{data($RP//*:initialPedigree/*:productInfo/*:manufacturer)}</manufacturer>
			<productCode type='NDC'>{data($RP//*:initialPedigree/*:productInfo/*:productCode)}</productCode>
		</previousProductInfo>
		<itemInfo>
			<lot>{$RP/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:itemInfo/*:lot/text()}</lot>
			<expirationDate>{$RP/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:itemInfo/*:expirationDate/text()}</expirationDate>
			<quantity>{$RP/*:receivedPedigree/pedigree/*:shippedPedigree/*:itemInfo/*:quantity/text()}</quantity>
			<itemSerialNumber>{$RP/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:itemInfo/*:itemSerialNumber/text()}</itemSerialNumber>
		</itemInfo>
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
	<itemInfo>{$item//*}</itemInfo>
</repackagedPedigree>
else 
let $initial := $pedigree/Pedigree/*:initialPedigree
let $product := (<productInfo>
		{$productNode/drugName}{$productNode/manufacturer}<productCode type='NDC'>{data($productNode/productCode)}</productCode>
		{$productNode/dosageForm}{$productNode/strength}{$productNode/containerSize}
		</productInfo>)
let $item := (<itemInfo>
		{$itemNode/lot}{$itemNode/expirationDate}{$itemNode/quantity}{$itemNode/itemSerialNumber}
	     </itemInfo>)
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
	<itemInfo>{$item//*}</itemInfo>
</repackagedPedigree>
"),

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
where $y/UserID = $pedNode//PedigreeData/signerId/text()
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
	<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{$dateTime}</signatureDate>
	  <signatureMeaning>Certified</signatureMeaning>
	</signatureInfo>
  </shippedPedigree>
</pedigree> )}</x> )/child::*
order by $s/name()
return $s }
</pedigreeEnvelope>
)
let $ret := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment_MD($envid) :)
return $envid

"),

tig:create-stored-procedure("ShipGoods_MD","

declare variable $invNum as string external;
declare variable $swLotNum as string external;
declare variable $swNDC as string external;
declare variable $xmlString as string external;

let $pedNode := tlsp:StringToNode_MD($xmlString)
let $qty as xs:integer := xs:integer($pedNode//PedigreeData/ItemInfo/quantity)
let $containerSize as xs:integer := xs:integer($pedNode//PedigreeData/ProductInfo/containerSize)
let $signerId := $pedNode//PedigreeData/signerId/text()

let $sessionId := tlsp:GetSessionID_MD($signerId)
let $rcvPedId := (for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[SWLotNum = $swLotNum]
		return data($i/ReceivedPedigreeID) )
return if(count($rcvPedId) = 0) then false() else 
let $pedigree := tlsp:GetRPORInitialPedigreeDetails_MD($rcvPedId)

return 
if( count($pedigree) = 0 ) then false()
 else 
	let $totQty := $qty * $containerSize
	let $refNum := (for $i in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice[OrderReference/BuyersID = $invNum]
			 return data($i/ID))
	let $envId :=  tlsp:CreateShippedPedigreeRepacked_MD($refNum,'Invoice','3',$rcvPedId,$xmlString)
  	let $k :=  tlsp:UpdatePedigreeBankQuantity_MD($swLotNum,$totQty)
	let $ndc :=  tlsp:UpdatePedigreeBankNDC_MD($swLotNum,$swNDC)
(:	let $sign := tlsp:pedigreeSignature_MD($envId)
    let $sendPed :=  tlsp:SendPedigree_MD($envId,'venu.gopal@sourcen.com','testePharma_MD@sourcen.com', 'sniplpass')
:)
	return true()
      
"),

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



tig:create-stored-procedure("getPreviousProducts_MD","
declare variable $catalogName as string external; 
declare variable $serialNumber as string external; 
for $e in collection(concat('tig:///ePharma_MD/',$catalogName))/*:pedigreeEnvelope/*:pedigree 
where $e/*:shippedPedigree/*:documentInfo/*:serialNumber=$serialNumber 
return 
<out>
<previousProducts>{$e/*:shippedPedigree[1]/*:repackagedPedigree/*:previousProducts}</previousProducts>
<productInfo>{$e/*:shippedPedigree[1]/*:repackagedPedigree/*:productInfo}</productInfo> 
<itemInfo>{$e/*:shippedPedigree[1]/*:repackagedPedigree/*:itemInfo}</itemInfo> 
</out>
"),

tig:create-stored-procedure("FaxDetails_MD","
declare variable $requestType as string external;
for $i in collection('tig:///ePharma_MD/ReceivedFax')/InboundPostRequest 
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

tig:create-stored-procedure("ndcExistsNew_MD","
 declare variable $ndc as string external;
 
 for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand 
 where upper-case($i/NDC) = upper-case($ndc) 
 return true()
 
 
"),

tig:create-stored-procedure("CreateRepackageInstrDoc_MD","
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

let $lot := (for $j in $pedRcv//LotInfo
let $itemInfo := (for $m in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
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

let $repack := insert document $repackNode into 'tig:///ePharma_MD/RepackageInstructions' 
return 
<ID>{$docid}</ID>
"),

tig:create-stored-procedure("UpDatePedigreeBankDocNew_MD", "
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
let $itemInfo := (for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$i}</pedigree>))

let $quantity := xs:integer( for $i in $itemInfo
		 where $i/lot = $j/Vendor_Lot_Number 
		 return data($i/quantity))
let $res := tlsp:UpDateTotalInventory_MD($NDC, $quantity cast as integer )
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
tlsp:insertLotinfoNode_MD($NDC, $lot )
)

"),

tig:create-stored-procedure("createNewPedBankDocNewschema_MD", "
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

let $Insertrepack := tlsp:CreateRepackageInstrDoc_MD($NDC,$recId,$pedRcv)
let $swp_ndc := $pedRcv/SWP_Stock_Code
let $lot := (for $j in $pedRcv//LotInfo
let $itemInfo := (for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$i}</pedigree>))

let $quantity :=  xs:integer( for $i in $itemInfo
		 where $i/lot = $j/Vendor_Lot_Number 
		 return data($i/quantity))
let $res := tlsp:UpDateTotalInventory_MD($NDC, $quantity cast as integer )
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
for $s in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree 
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
return insert document $PedigreeBank into  'tig:///ePharma_MD/PedigreeBank' 


"),

tig:create-stored-procedure("CreateReceivedPedigreeForPedigrees1_MD","
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

for $k in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree

let $docid := util:create-uuid()
let $recvid := util:create-uuid()
let $ndc := $k//*:initialPedigree/*:productInfo/*:productCode
where $k/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedigreeId
return
if(tlsp:ndcExists_MD($ndc) ) then
(
tig:insert-document('tig:///ePharma_MD/ShippedPedigree',
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
 ,tlsp:UpDatePedigreeBankDocNew_MD($ndc,$recvid,$pedRcv ),tlsp:CreateSignToReceivedPedigree_MD($recvid))

else
 (
tig:insert-document('tig:///ePharma_MD/ShippedPedigree',
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
,tlsp:createNewPedBankDocNewschema_MD($ndc,$recvid,$pedRcv),tlsp:CreateSignToReceivedPedigree_MD($recvid)) 
")
,

tig:create-stored-procedure("InsertAndChangePedigreeStatus1_MD","

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $userid as string external;
declare variable $pedId as string external;
declare variable $status as string external;


let $time := fn:current-dateTime()

let $data := ( for $i in collection ('tig:///ePharma_MD/PedigreeStatus') 
		where $i/PedigreeStatus/PedigreeID = $pedId
		return $i )
return if (empty($data))
	then
	    tig:insert-document('tig:///ePharma_MD/PedigreeStatus', 
		<PedigreeStatus><PedigreeID>{$pedId}</PedigreeID>
		   <Status>
  		   	<StatusChangedOn>{$time}</StatusChangedOn>
			<StatusChangedTo>{$status}</StatusChangedTo>
			<UserId>{$userid}</UserId>
		   </Status>
		   <TimeStamp>{$time}</TimeStamp>
		</PedigreeStatus>
)
       else (tlsp:insertNode_MD(<x> <Status>
  			     	 <StatusChangedOn>{$time}</StatusChangedOn>
   			     	 <StatusChangedTo>{$status}</StatusChangedTo>
  			      	<UserId>{$userid}</UserId>
			      </Status>
			      <TimeStamp>{$time}</TimeStamp></x>,document-uri($data)))
"),

tig:create-stored-procedure("ReconcileGoods_MD","

declare variable $xmlString as string external; 
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

let $pedNode := tlsp:StringToNode_MD($xmlString)
let $pedRcv := $pedNode//PEDRCV
for $i in $pedRcv
	
return (
	 tlsp:CreateReceivedPedigreeForPedigrees1_MD($i,data($i/SignerID) cast as string,data($i/PedigreeID) cast as string), 	 
	 tlsp:InsertAndChangePedigreeStatus1_MD(data($i/SignerID) cast as string,data($i/PedigreeID) cast as string,'Received And Authenticated')
)
"),


tig:create-stored-procedure("ShippingPedigreeDetails_MD","

declare variable $serialNumber as string external;

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
declare function local:getProductInfo($e as node()){
let $item := local:getItemInfo($e)
return
if(exists($e/*:shippedPedigree/*:repackagedPedigree)) then(
<drugName>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:drugName)}</drugName>,
<productCode>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:manufacturer)}</manufacturer>,
<quantity>{data($item/*:quantity)}</quantity>,
<dosageForm>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:containerSize)}</containerSize>
)
else(
<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data($item/*:quantity)}</quantity>,
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
"),

tig:create-stored-procedure("GetBinaryImageForServlet_MD","
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

"),

tig:create-stored-procedure("InitialPedigreeDetailsRP_MD","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $shipId]
//*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = $serualId ]

return <output>{

<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data($e//*:initialPedigree/*:itemInfo/*:quantity )}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>,
<custName>{data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</custName>,
<custAddress>{concat(data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</custAddress>,
<custContact>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</custContact>,
<custPhone>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</custPhone>,
<custEmail>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/*:signatureInfo/*:signerInfo/*:name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/*:signatureInfo/*:signerInfo/*:title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/*:signatureInfo/*:signerInfo/*:telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/*:signatureInfo/*:signerInfo/*:url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/*:signatureInfo/*:signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/*:documentInfo/*:serialNumber)}</pedigreeId>,
<transactionDate>{data($e/*:transactionInfo/*:transactionDate)}</transactionDate>, 
<transactionType>{data($e/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</transactionType>,
<transactionNo>{data($e/*:transactionInfo/*:transactionIdentifier/*:identifier)}</transactionNo>,
<fromCompany>{data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</fromCompany>,
<toCompany>{data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street1),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street2),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:city),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:postalCode),',',data($e/*:transactionInfo/*:senderInfo/*:shippingAddress/*:country))}</fromBillAddress>,
<fromContact>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</fromContact>,
<fromTitle>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:title)} </fromTitle>,
<fromPhone>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</fromPhone>,
<fromEmail>{data($e/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</fromEmail>,
<fromLicense>{data($e/*:transactionInfo/*:senderInfo/*:licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street1),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street2),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:city),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:postalCode),',',data($e/*:transactionInfo/*:recipientInfo/*:businessAddress/*:country))}</toShipAddress>,
<toBillAddress>{concat(data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street1),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street2),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:city),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:postalCode),',',data($e/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:country))}</toBillAddress>,
<toContact>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:name)}</toContact>,
<toTitle>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:title)}</toTitle>,
<toPhone>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:telephone)}</toPhone>,
<toEmail>{data($e/*:transactionInfo/*:recipientInfo/*:contactInfo/*:email)}</toEmail>,
<toLicense>{data($e/*:transactionInfo/*:recipientInfo/*:licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("ShippedORReceivedPedigreeDetailsRP_MD","


declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = $shipId ]
//*:receivedPedigree[*:documentInfo/*:serialNumber = $serualId]

return <output>{

<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,
<quantity>{data($e//*:initialPedigree/*:itemInfo/*:quantity )}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>,
<custName>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</custName>,
<custAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</custAddress>,
<custContact>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</custContact>,
<custPhone>{data($e/*:pedigree/*:shippedPedigree/vtransactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</custPhone>,
<custEmail>{data($e/*:pedigree/*:shippedPedigree/vtransactionInfo/*:senderInfo/*:contactInfo/*:email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signerInfo/url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/*:pedigree/*:shippedPedigree/*:signatureInfo/*:signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)}</pedigreeId>,
<transactionDate>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</transactionDate>, 
<transactionType>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</transactionType>,
<transactionNo>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)}</transactionNo>,
<fromCompany>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</fromCompany>,
<toCompany>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street2),',',data($e/pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:country))}</fromBillAddress>,
<fromContact>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</fromContact>,
<fromTitle>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:title)} </fromTitle>,
<fromPhone>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</fromPhone>,
<fromEmail>{data($e/*:pedigree/shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</fromEmail>,
<fromLicense>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:country))}</toShipAddress>,
<toBillAddress>{concat(data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street1),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street2),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:city),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:postalCode),',',data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:country))}</toBillAddress>,
<toContact>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:name)}</toContact>,
<toTitle>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:title)}</toTitle>,
<toPhone>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:telephone)}</toPhone>,
<toEmail>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:email)}</toEmail>,
<toLicense>{data($e/*:pedigree/*:shippedPedigree/*:transactionInfo/*:ecipientInfo/*:licenseNumber)}</toLicense>

}</output>
"),

tig:create-stored-procedure("getAuthPedStatus_MD","
declare variable $pedId as string external; 
for $ps in collection('tig:///ePharma_MD/PedigreeStatus')/PedigreeStatus[PedigreeID =$pedId]
for $status in $ps/Status where $status/StatusChangedOn = $ps/TimeStamp
return <output><status>{$status/StatusChangedTo/text()}</status><time>{substring-before($status/StatusChangedOn/text(),'T')}</time>
<date>{substring-before(substring-after($status/StatusChangedOn/text(),'T'),'.')}</date></output>") ,


tig:create-stored-procedure("repnode_MD","
declare binary-encoding none;
 
declare variable $n as xs:string external ;
declare variable $docid as xs:string external ;
replace collection('tig:///ePharma_MD/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber = $docid]/altPedigree
with <altPedigree><mimeType>application/pdf</mimeType><encoding>base64binary</encoding><data>{$n}</data></altPedigree>"),

tig:create-stored-procedure("InsertPedIdInefax_MD", "
declare variable $pedId as string external;
declare variable $docId as string external;
insert <InitialPedigreeId>{$pedId}</InitialPedigreeId> as first into doc($docId)/*:InboundPostRequest"),


tig:create-stored-procedure("ChangeFaxStatus_MD", "
declare variable $pid as string external;
update
for $i in collection('tig:///ePharma_MD/ReceivedFax')/InboundPostRequest[InitialPedigreeId = $pid]
replace value of $i/FaxControl/Status with 1
"),

tig:create-stored-procedure("repEncoding_MD","
declare binary-encoding none;
declare variable $docid as xs:string external ;
replace collection('tig:///ePharma_MD/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber = $docid]/altPedigree/data
with    doc('file:///c:/Temp/abc.xml')/data  "),


tig:create-stored-procedure("CreateShippedPedigreenew_MD","
import module namespace util = 'xquery:modules:util';
declare variable $despatchAdviseNo as string external;
declare variable $transactionType as string external;
declare variable $flag as string external;
declare variable $pedID as string external; 
declare variable $sessionId as string external;

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

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $userid := for $x in collection('tig:///EAGRFID/SysSessions')/session 
where $x/sessionid = $sessionId 
return data($x/userid)
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $userid
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
let $quantity1 :=(for $x in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice[ID = $despatchAdviseNo]/DespatchLine
	        where $x/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DeliveredQuantity)
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
let $itemserial := $itemserial1
let $docid := util:create-uuid()
(:let $shipid := util:create-unique-id('ID'):)
let $shipid := util:create-uuid()
let $contid := util:create-uuid()
let $res := tlsp:GetRPDetails_MD($pedID)

let $container := (<container><containerCode>{$sscc}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber>{$itemserial}</itemSerialNumber><productCode type='NDC'>{data($productNode/productCode)}</productCode><quantity>{$quantity}</quantity><lot>{$lotno}</lot>

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
		<identifier>{$despatchAdviseNo}</identifier><identifierType>{$transactionType}</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{$date}</transactionDate>
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
(:let $ret := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment_MD($envid) :)
(:let $sig := tlsp:pedigreeSignature_MD($envid):)
return $ret :)
return $pdenv
"),

tig:create-stored-procedure("CreateSignToReceivedPedigreeNew_MD","
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
 

 for $pedigree in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree
  where $pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId
return
 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
(: where not( exists($pedigree/*:Signature) ):)
return $pedigree_sign/*:pedigree/*:Signature[2]
 
"),

tig:create-stored-procedure("CreateReceivedPedigreeForPedigreesNew_MD","

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

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')

for $k in collection('tig:///ePharma_MD/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree

let $docid := util:create-uuid()
let $recvid := util:create-uuid()
let $ndc := $k//*:initialPedigree/*:productInfo/*:productCode
where $k/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedigreeId
return

let $j :=

<pedigree>
  	<receivedPedigree id='{$docid}'>
	 	<documentInfo>
	  		<serialNumber>{$recvid}</serialNumber>
	  		<version>1</version>
		</documentInfo>
		{$k}
		<receivingInfo>
			<dateReceived>{$date}</dateReceived>
			<itemInfo>
                    		<lot>{data($k//*:initialPedigree/*:itemInfo/*:lot)}</lot>
                    		<expirationDate>{data($k//*:initialPedigree/*:itemInfo/*:expirationDate)}</expirationDate> 
                    		<quantity>{data($k//*:initialPedigree/*:itemInfo/*:quantity)}</quantity>
                    		<itemSerialNumber>{data($k//*:initialPedigree/*:itemInfo/*:itemSerialNumber)}</itemSerialNumber>
                		</itemInfo>
		</receivingInfo>
		<signatureInfo>
			{$signerInfo}
			<signatureDate>{$dateTime}</signatureDate>
			<signatureMeaning>Certified</signatureMeaning>
		</signatureInfo>
	</receivedPedigree>
{tlsp:CreateSignToReceivedPedigreeNew_MD($pedigreeId)}
</pedigree> 

return $j

"),

tig:create-stored-procedure("PedigreeBankUpdateAfterReceivedPedigree_MD","
declare variable $pedigree as string external;

let $ped := tlsp:StringToNode_MD($pedigree)/node()
let $doc := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$ped)
let $ndc := ( for $i in $ped return data($i//*:productCode) )
let $recvid := ( for $i in $ped return data($i/*:receivedPedigree/*:documentInfo/*:serialNumber) )
let $ndcexists := ( if(tlsp:ndcExists_MD($ndc)) then tlsp:UpDatePedigreeBank_MD($ndc,$recvid ) else tlsp:createNewPedigreeBankDoc_MD($ndc,$recvid) )
return $ndcexists
"),

tig:create-stored-procedure("PedIdForFax_MD","
import module namespace util = 'xquery:modules:util';
let $pedid := util:create-uuid()
return $pedid"),

tig:create-stored-procedure("ClearLotQtyforShipping_MD", "
declare variable $ndc as string external;
declare variable $binNum as string external;
declare variable $lot_info as node()* external;

update
for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $ndc]/BinInfo[BinNumber = $binNum]/LotInfo
where $i/LotNumber = data($lot_info/LotNumber)
replace value of $i/Quantity with  0

"),

tig:create-stored-procedure("UpDateLotQtyforShipping_MD", "
declare variable $ndc as string external;
declare variable $binNum as string external;
declare variable $lot_info as node()* external;
declare variable $qty as integer external;


update
for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $ndc]/BinInfo[BinNumber = $binNum]/LotInfo
where $i/LotNumber = data($lot_info/LotNumber)
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
		order by $i/BankTime
		return $i)
let $index as xs:integer? := min(
		for $i in 1 to count($lot)
		let $all := ($lot/Quantity)[position() <= $i]
		let $ints  := ( for $s in $all return  xs:integer(data($s)) )
		where sum($ints) >= $qty
		return   $i
		)
for $i in $lot[position() <= $index]
return $i

"),

tig:create-stored-procedure("CreateShippedPedigree_MDNew","
import module namespace util = 'xquery:modules:util';
declare variable $xmlString as string external;

let $shipNode := tlsp:StringToNode($xmlString)/*:*

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $envid := util:create-uuid()
let $quantity := data($shipNode/QtyPulled)
let $NDC := data($shipNode/NDC)
let $binNum := data($shipNode/BinLocation)

let $productInfo := (for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $NDC]
		return $i/ProductInfo)
let $productNode := (<productInfo><drugName>{data($productInfo/drugName)}</drugName><manufacturer>{data($productInfo/Manufacturer)}</manufacturer>
	   	   <productCode type='NDC'>{$NDC}</productCode><dosageForm>{data($productInfo/dosageForm)}</dosageForm>
    	            <strength>{data($productInfo/strength)}</strength><containerSize>{data($productInfo/containerSize)}</containerSize></productInfo> )
let $transInfo := (for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $NDC]/BinInfo[BinNumber = $binNum]/LotInfo[1]
		return $i/TransactionInfo)
let $mfgrInfo := (<senderInfo><businessAddress><businessName>{data($transInfo/Address/businessName)}</businessName>
		<street1>{data($transInfo/Address/street1)}</street1><street2>{data($transInfo/Address/street2)}</street2><city>{data($transInfo/Address/city)}</city><stateOrRegion>{data($transInfo/Address/stateOrRegion)}</stateOrRegion>
		<postalCode>{data($transInfo/Address/postalCode)}</postalCode><country>{data($transInfo/Address/country)}</country></businessAddress>
		<licenseNumber state='{data($transInfo/Address/stateOrRegion)}' agency=''>{data($transInfo/Address/licenseNumber)}</licenseNumber>
		</senderInfo> )
let $lot_Info := (for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC = $NDC]/BinInfo[BinNumber = $binNum]/LotInfo[1]
		return $i)
let $lot := tlsp:GetLotInfo_MD($quantity,$NDC,$binNum)
let $totInven := tlsp:UpdatePedigreeBankTotQtyNew($NDC,$quantity)
let $pedBankresult := tlsp:updatePedigreebankAfterShipmentNew_MD($NDC,$quantity,$binNum,<root>{$lot}</root>)
let $lotDetails := (for $i in $lot return <itemInfo><lot>{data($i/LotNumber)}</lot><quantity>{$quantity}</quantity></itemInfo>)

let $senderinfo := (
	<senderInfo><businessAddress><businessName>Morris And Dickson Co., LLC</businessName>
	<street1>410 Kay Lane</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion>
	<postalCode>71115</postalCode><country>US</country></businessAddress></senderInfo>
)
let $recipientinfo := (
	<recipientInfo><businessAddress><businessName>{data($shipNode/CustomerName)}</businessName>
	<street1>{data($shipNode/CustomerAddressLine1)}</street1><street2>{data($shipNode/CustomerAddressLine2)}</street2>
	<city>{data($shipNode/CustomerCity)}</city><stateOrRegion>{data($shipNode/State)}</stateOrRegion>
	<postalCode>{data($shipNode/CustomerZip)}</postalCode><country>{data($shipNode/Country)}</country>
	</businessAddress>
	<licenseNumber state='{data($shipNode/State)}' agency=''>{data($shipNode/CustomerDea)}</licenseNumber>
	</recipientInfo>)

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = '321'
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>

let $docid := util:create-uuid()
let $shipid := util:create-uuid()
let $contid := data($shipNode/UniqueBoxID)

let $container := (<container><containerCode>{$contid}</containerCode><pedigreeHandle><serialNumber>{$docid}</serialNumber>
<itemSerialNumber></itemSerialNumber><productCode type='NDC'>{data($shipNode/NDC)}</productCode><quantity>{$quantity}</quantity><lot>{data($lot_Info/LotNumber)}</lot>
</pedigreeHandle></container>)

let $ped := (
<pedigree>
  <shippedPedigree id='{$shipid}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>1</version>
	</documentInfo>
	<initialPedigree>
	   {$productNode}
	   <itemInfo>
	     <lot>{data($lot_Info/LotNumber)}</lot> 
	     <expirationDate>{data($lot_Info/LotNumber)}</expirationDate> 
	     <quantity>{data($lot_Info/Quantity)}</quantity> 
	    </itemInfo>
	    <transactionInfo>
	   	{$mfgrInfo}
	         <recipientInfo><businessAddress>{$senderinfo/businessAddress//*}</businessAddress></recipientInfo>
	   	<transactionIdentifier>
			<identifier>{data($transInfo/TransactionId)}</identifier><identifierType>{data($transInfo/TransactionType)}</identifierType>
	         </transactionIdentifier>
		<transactionType>Sale</transactionType>
	   	<transactionDate>{data($transInfo/TransactionDate)}</transactionDate>
	    </transactionInfo>
	    
	 </initialPedigree>
	{$lotDetails}
 	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{data($shipNode/InvoiceNo)}</identifier><identifierType>InvoiceNumber</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{data($shipNode/InvoiceDate)}</transactionDate>
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
let $pedEnv := (<pedigreeEnvelope>
<version>1</version>
<serialNumber>{$envid}</serialNumber>
<date>{$date}</date>
<sourceRoutingCode>Morris And Dickson Co., LLC</sourceRoutingCode><destinationRoutingCode>{data($shipNode/CustomerName)}</destinationRoutingCode>
{$container}
{$ped}
</pedigreeEnvelope>
)
let $res := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pedEnv)
return $res

"),

tig:create-stored-procedure("InsertPTPDocument","
declare variable $name as string external;
declare variable $deaNumber as string external;
declare variable $notificationDesc as string external;
declare variable $notificationURI as string external;
declare variable $destination as string external;
declare variable $localFolder as string external;
declare variable $notifyURI as string external;
declare variable $userName as string external;
declare variable $pwd as string external;
declare variable $containerCodeMU as string external;
declare variable $shipmentHandleMU as string external;
declare variable $containerCodeDU as string external;
declare variable $shipmentHandleDU as string external;

tig:insert-document('tig:///ePharma_MD/PedigreeTradingPartner',
	<PedigreeTradingPartner>
		<name>{$name}</name>
		<deaNumber>{$deaNumber}</deaNumber>
		<notificationDescription>{$notificationDesc}</notificationDescription>
		<notificationURI>{$notificationURI}</notificationURI>
		<localFolder>{$localFolder}</localFolder>
		<destinationRoutingCode>{$destination}</destinationRoutingCode>
		<notificationInfo>
			<notifyURI>{$notifyURI}</notifyURI>
			<username>{$userName}</username>
			<password>{$pwd}</password>
		</notificationInfo>
		<configurationElements>
			<manualusecase>
				<config>
					<element>containerCode</element>
					<value>{$containerCodeMU}</value>
				</config>
				<config>
					<element>shipmentHandle</element>
					<value>{$shipmentHandleMU}</value>
				</config>
			</manualusecase>
			<dropshipusecase>
				<config>
					<element>containerCode</element>
					<value>{$containerCodeDU}</value>
				</config>
				<config>
					<element>shipmentHandle</element>
					<value>{$shipmentHandleDU}</value>
				</config>
			</dropshipusecase>
		</configurationElements>
	</PedigreeTradingPartner>)
"),

tig:create-stored-procedure("UpdatePTPDocument","
declare variable $name as string external;
declare variable $deaNumber as string external;
declare variable $notificationDesc as string external;
declare variable $notificationURI as string external;
declare variable $destination as string external;
declare variable $localFolder as string external;
declare variable $notifyURI as string external;
declare variable $userName as string external;
declare variable $pwd as string external;
declare variable $containerCodeMU as string external;
declare variable $shipmentHandleMU as string external;
declare variable $containerCodeDU as string external;
declare variable $shipmentHandleDU as string external;

let $deleteResult :=  tig:delete-document( document-uri(for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')[PedigreeTradingPartner/deaNumber =$deaNumber]
						  return $i))
return tig:insert-document('tig:///ePharma_MD/PedigreeTradingPartner',
	<PedigreeTradingPartner>
		<name>{$name}</name>
		<deaNumber>{$deaNumber}</deaNumber>
		<notificationDescription>{$notificationDesc}</notificationDescription>
		<notificationURI>{$notificationURI}</notificationURI>
		<localFolder>{$localFolder}</localFolder>
		<destinationRoutingCode>{$destination}</destinationRoutingCode>
		<notificationInfo>
			<notifyURI>{$notifyURI}</notifyURI>
			<username>{$userName}</username>
			<password>{$pwd}</password>
		</notificationInfo>
		<configurationElements>
			<manualusecase>
				<config>
					<element>containerCode</element>
					<value>{$containerCodeMU}</value>
				</config>
				<config>
					<element>shipmentHandle</element>
					<value>{$shipmentHandleMU}</value>
				</config>
			</manualusecase>
			<dropshipusecase>
				<config>
					<element>containerCode</element>
					<value>{$containerCodeDU}</value>
				</config>
				<config>
					<element>shipmentHandle</element>
					<value>{$shipmentHandleDU}</value>
				</config>
			</dropshipusecase>
		</configurationElements>
	</PedigreeTradingPartner>)
"),
tig:create-stored-procedure("ListPedigreeTradingPartners", "
for $i in collection ('tig:///ePharma_MD/PedigreeTradingPartner') return 
	<root>
		<TradingPartner>{data($i//name)}</TradingPartner>
		<DEANumber>{data($i//deaNumber)}</DEANumber>

	</root>"),
tig:create-stored-procedure("PedigreeTradingPartnersDetails", "

declare variable $deaNumber as string external;

for $i in collection ('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner[deaNumber = $deaNumber] 
return $i"),


tig:create-stored-procedure("deletePTP","
declare variable $deaNumber as string external;

for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')
where $i//deaNumber = $deaNumber
return tig:delete-document(document-uri($i))
")

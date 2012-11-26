
tig:drop-stored-procedure("PTPExists"),
tig:create-stored-procedure("PTPExists","
 declare variable $shipNode as node()* external;
 
 let $deaNumber := distinct-values($shipNode//CustomerDea)
 for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner
 where upper-case($i/deaNumber) = upper-case($deaNumber)
 return true()
"),

tig:drop-stored-procedure("GetInvoiceNo"),
tig:create-stored-procedure("GetInvoiceNo","
declare variable $shipNode as node()* external;

let $b := $shipNode//InvoiceNo
return data($b[1])

"),

tig:drop-stored-procedure("TPCustomization"),
tig:create-stored-procedure("TPCustomization","
declare variable $deaNumber as string external;
declare variable $usecase as string external;
let $config := <root>{(for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner
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
"),

tig:drop-stored-procedure("ReformatingPedShipData"),
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

tig:drop-stored-procedure("CreateManualShippedPedigree_MDNew"),
tig:create-stored-procedure("CreateManualShippedPedigree_MDNew","
import module namespace util = 'xquery:modules:util';
declare variable $xmlString as node()* external;


declare variable $signerId as string external;
declare variable $deaNumber as string external;
declare variable $sourceRoutingCode as string external;

let $pedshipdata := $xmlString//pedshipData
let $ind_pedship := (for $i in $pedshipdata return $i/pedship)[1]
let $values := tlsp:TPCustomization($ind_pedship/CustomerDea,'manualusecase')
let $drc := (for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner
where $i/deaNumber = $ind_pedship/CustomerDea
return data($i/destinationRoutingCode))

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')
let $envid := concat('urn:uuid:',util:create-uuid())

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $signerId

return <signerInfo><name>{concat(data($y/FirstName),' ',data($y/LastName))}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email>
</signerInfo>

let $md := (for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner where $i/deaNumber = $deaNumber return $i)

let $senderinfo := (
	<senderInfo><businessAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/businessAddress/line1)}</street1><street2>{data($md/businessAddress/line2)}</street2><city>{data($md/businessAddress/city)}</city><stateOrRegion>{data($md/businessAddress/state)}</stateOrRegion>
	<postalCode>{data($md/businessAddress/zip)}</postalCode><country>{data($md/businessAddress/country)}</country></businessAddress>
	<shippingAddress><businessName>{data($md/name)}</businessName>
	<street1>{data($md/shippingAddress/line1)}</street1><street2>{data($md/shippingAddress/line2)}</street2><city>{data($md/shippingAddress/city)}</city><stateOrRegion>{data($md/shippingAddress/state)}</stateOrRegion>
	<postalCode>{data($md/shippingAddress/zip)}</postalCode><country>{data($md/shippingAddress/country)}</country></shippingAddress>
	<licenseNumber>{data($md/deaNumber)}</licenseNumber>
	<contactInfo>
		<name>{data($md/contact)}</name>
		<telephone>{data($md/phone)}</telephone>
		<email>{data($md/email)}</email>
	</contactInfo>
	</senderInfo>
)

let $pedigree_data := <Result>{(for $shipNode in $pedshipdata/pedship

(:let $shipNode := $pedship_ndc/pedship:)


let $quantity := data($shipNode/QuantityPulled)
let $NDC := data($shipNode/NDC)

let $productNode := (<productInfo><drugName>{data($shipNode/DrugLabel)}</drugName><manufacturer>{data($shipNode/MFGName)}</manufacturer>
	   	   <productCode type='NDC'>{$NDC}</productCode><dosageForm>{data($shipNode/Form)}</dosageForm>
    	            <strength>{data($shipNode/Strength)}</strength><containerSize>{data($shipNode/ContainerSize)}</containerSize></productInfo> )


let $mfgrInfo := (<senderInfo><businessAddress><businessName>{data($shipNode/MFGName)}</businessName>
		 <street1>{data($shipNode/MFGAddressLine1)}</street1><street2>{data($shipNode/MFGAddressLine2)}</street2><city>{data($shipNode/MFGCity)}</city><stateOrRegion>{data($shipNode/State)}</stateOrRegion>
		<postalCode>{data($shipNode/MFGZip)}</postalCode><country>{data($shipNode/Country)}</country></businessAddress>
		<licenseNumber>{data($shipNode/MFGDea)}</licenseNumber>
		<contactInfo><name>{data($shipNode/MFGContactName)}</name><telephone>{data($shipNode/MFGContactTelephone)}</telephone>
		<email>{data($shipNode/MFGContactEMail)}</email>
		</contactInfo>
		</senderInfo> )

let $recipientinfo := (
	<recipientInfo><businessAddress><businessName>{data($shipNode/CustomerName)}</businessName>
	<street1>{data($shipNode/CustomerAddressLine1)}</street1>
	<street2>{data($shipNode/CustomerAddressLine2)}</street2>
	<city>{data($shipNode/CustomerCity)}</city><stateOrRegion>{data($shipNode/CustomerState)}</stateOrRegion>
	<postalCode>{data($shipNode/CustomerZip)}</postalCode><country>{data($shipNode/CustomerCountry)}</country>
	</businessAddress><shippingAddress><businessName>{data($shipNode/ShipToCustomerName)}</businessName>
	<street1>{data($shipNode/ShipToCustomerAddressLine1)}</street1>
	<street2>{data($shipNode/ShipToCustomerAddressLine2)}</street2>
	<city>{data($shipNode/ShipToCustomerCity)}</city><stateOrRegion>{data($shipNode/ShipToState)}</stateOrRegion>
	<postalCode>{data($shipNode/ShipToCustomerZip)}</postalCode><country>{data($shipNode/ShipToCountry)}</country>
	</shippingAddress>
	<licenseNumber>{data($shipNode/CustomerDea)}</licenseNumber>
	</recipientInfo>)


let $docid := concat('urn:uuid:',util:create-uuid())
let $shipid := concat('_',util:create-uuid())

let $container := (<pedigreeHandle><serialNumber>{$docid}</serialNumber></pedigreeHandle> )
return
(
<pedigree>
  <shippedPedigree id='{$shipid}'>
	<documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>20060331</version>
	</documentInfo>
	<initialPedigree>
	   {$productNode}
	   <itemInfo>
	     <lot>{data($shipNode/LotNo)}</lot> 
	     <expirationDate>{data($shipNode/ExpYYYYMMDD)}</expirationDate> 
	     <quantity>{data($shipNode/QuantityPulled)}</quantity> 
	    </itemInfo>
	    <transactionInfo>
	   	{$mfgrInfo}
	         <recipientInfo><businessAddress>{$senderinfo/businessAddress//*}</businessAddress><shippingAddress>{$senderinfo/shippingAddress//*}</shippingAddress></recipientInfo>
	   	<transactionIdentifier>
			<identifier>{data($shipNode/PONo)}</identifier><identifierType>PurchaseOrderNumber</identifierType>
	         </transactionIdentifier>
		<transactionType>Sale</transactionType>
	   	<transactionDate>{data($shipNode/PODateYYYYMMDD)}</transactionDate>
	    </transactionInfo>
	    <receivingInfo>
		<dateReceived>{data($shipNode/RecDateYYYYMMDD)}</dateReceived>
	    </receivingInfo>
	 </initialPedigree>
	<itemInfo>			
		<lot>{data($shipNode/LotNo)}</lot>
		<expirationDate>{data($shipNode/ExpYYYYMMDD)}</expirationDate>
		<quantity>{$quantity}</quantity>
	</itemInfo>
 	<transactionInfo>
	   {$senderinfo}
	   {$recipientinfo}
	   <transactionIdentifier>
		<identifier>{data($shipNode/PONumber)}</identifier><identifierType>PurchaseOrderNumber</identifierType>
	   </transactionIdentifier>
	   <transactionType>Sale</transactionType>
	   <transactionDate>{data($shipNode/InvoiceDateYYYYMMDD)}</transactionDate>
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
</pedigree> ,$container ))}</Result>

let $pedEnv := (<pedigreeEnvelope>
<version>20060331</version>
<serialNumber>{$envid}</serialNumber>
<date>{$date}</date>
<sourceRoutingCode>{$sourceRoutingCode}</sourceRoutingCode><destinationRoutingCode>{$drc}</destinationRoutingCode>
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
return $pedEnv

(:
let $res := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$pedEnv)
let $res1 := tlsp:pedigreeLevelSignature_MD($envid,$docid)
return $res1 
:)

"),
 
tig:drop-stored-procedure("InsertProcessedEnvelope"),
tig:create-stored-procedure("InsertProcessedEnvelope","
declare variable $envIds as node()* external;
declare variable $fileName as string external;

tig:insert-document('tig:///ePharma_MD/ProcessedEnvelope',
			<ProcessedEnvelope>
				<fileName>{$fileName}</fileName>
				{$envIds/envelopeId}
			</ProcessedEnvelope>)
"),
tig:drop-stored-procedure("RetrievePedTradingPartnerInfo"),
tig:create-stored-procedure("RetrievePedTradingPartnerInfo","
 declare variable $deaNumber as string external;
 <root>{
 for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner where $i/deaNumber = $deaNumber 
return ($i/notificationDescription, $i/notificationInfo, $i/localFolder) }</root>
"),
tig:drop-stored-procedure("InsertShippedPedigree"),
tig:create-stored-procedure("InsertShippedPedigree","
 declare variable $envNode as node() external; 
declare variable $fileName as string external;
 let $test := $envNode/node()
 let $envId := data($test/*:serialNumber)
 let $result := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$envNode)
 let $sign := tlsp:CreateSignatureToEnvelope_MD($envId)
 return $envId 
"),
tig:drop-stored-procedure("PEDSHIPManual_MD"),
tig:create-stored-procedure("PEDSHIPManual_MD","
 declare variable $shipNode as node() external;
 declare variable $signerId as string external;
 declare variable $deaNumber as string external;
 declare variable $sourceRoutingCode as string external;
 for $ship_data in $shipNode//pedshipData
 let $result := tlsp:CreateManualShippedPedigree_MDNew(<root>{$ship_data}</root>,$signerId,$deaNumber,$sourceRoutingCode)
 return $result 
")


 

tig:drop-stored-procedure("PTPExists"),
tig:create-stored-procedure("PTPExists","
 declare variable $shipNode as node()* external;
 
 let $deaNumber := distinct-values($shipNode//CustomerDea)
 for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner
 where upper-case($i/deaNumber) = upper-case($deaNumber)
 return true()
"),

(:Modifications made to be compatible with TL version :659 :)
tig:drop-stored-procedure("TPCustomization"),
tig:create-stored-procedure("TPCustomization","
declare variable $deaNumber as string external;
declare variable $usecase as string external;

let $ccode := (for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner
where $i/deaNumber = $deaNumber
return
if( $usecase = 'manualusecase') then 
data($i/configurationElements/manualusecase/config[element = 'containerCode']/value)
else if ( $usecase = 'dropshipusecase')then data($i/configurationElements/dropshipusecase/config[element = 'containerCode']/value)
else if ( $usecase = 'automatedusecase')then data($i/configurationElements/automatedusecase/config[element = 'containerCode']/value) 
else ()
)

let $shipment := (for $i in collection('tig:///ePharma_MD/PedigreeTradingPartner')/PedigreeTradingPartner
where $i/deaNumber = $deaNumber
return
if( $usecase = 'manualusecase') then 
data($i/configurationElements/manualusecase/config[element = 'shipmentHandle']/value)
else if ( $usecase = 'dropshipusecase')then data($i/configurationElements/dropshipusecase/config[element = 'shipmentHandle']/value) 
else if ( $usecase = 'automatedusecase')then data($i/configurationElements/automatedusecase/config[element = 'shipmentHandle']/value)
else ()
)

return 
<result>
{
<containerCode>{$ccode}</containerCode>,
<shipmentHandle>{$shipment}</shipmentHandle>
}
</result>
"),


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
 

tig:drop-stored-procedure("PEDSHIPManual_MD"),
tig:create-stored-procedure("PEDSHIPManual_MD","
 declare variable $shipNode as node() external;
 declare variable $signerId as string external;
 declare variable $deaNumber as string external;
 declare variable $sourceRoutingCode as string external;
 for $ship_data in $shipNode//pedshipData 
let $result := tlsp:CreateManualShippedPedigree_MDNew(<root>{$ship_data}</root>,$signerId,$deaNumber,$sourceRoutingCode) return $result 

"),









tig:drop-stored-procedure("DeletePedigreeEnvelope_MD"),
tig:create-stored-procedure("DeletePedigreeEnvelope_MD"," 
 declare variable $PONumber as string external;
 declare variable $InvoiceDate as string external;
let $x := for $i in collection('tig:///ePharma_MD/ShippedPedigree')
	where $i/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier = $PONumber
	and $i/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionDate = $InvoiceDate
	return
	tig:delete-document(document-uri($i))

return 
if($x) then
'true'
else
'false'

"),


tig:drop-stored-procedure("Delete_IfMatchingPEFound"),
tig:create-stored-procedure("Delete_IfMatchingPEFound","
declare variable $pedShipXml as node()* external;

let $poNumber := $pedShipXml//pedshipData/pedship/PONumber
let $invoiceDate := $pedShipXml//pedshipData/pedship/InvoiceDateYYYYMMDD
let $ndc :=$pedShipXml//pedshipData/pedship/NDC


let $pe := (for $i in collection('tig:///ePharma_MD/ShippedPedigree') 
		where $i/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier = $poNumber
		and $i/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionDate =  $invoiceDate
		return $i)

let $ndc_db :=  $pe/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:initialPedigree/*:productInfo/*:productCode

	
let $foundResult := (for $k in $ndc
			let $result :=( for $h in $ndc_db
					return let $x := if($h = $k)then
							   'Found'
				                          else 
			                                       'Not Found'
							return <ped>{$x}</ped>)
	            return <pedigreeFound>{$result}</pedigreeFound>)



let $foundStatus := <Status>{for $i in $foundResult
		return	let $y := if ($i//ped = 'Found')then
				'Success'
			else
				'Failure'
			return <x>{$y}</x>}</Status>
let  $peDeletionStatus := for $j in $foundStatus
		   return if($j/x = 'Failure')then
				'No Deletion'
	                   else 
				'Deletion'


let $Result := if($peDeletionStatus = 'Deletion')then
 		tlsp:DeletePedigreeEnvelope_MD(data($poNumber[1]),data($invoiceDate[1]))
	else
		'No Deletion'

let $root :=  <Root><NDC>{$ndc}</NDC><DBNDC>{$ndc_db}</DBNDC><FoundResult>{$foundResult}</FoundResult><FoundStatus>{$foundStatus}</FoundStatus><PEDeletionStatus>{$peDeletionStatus}</PEDeletionStatus></Root>
return $Result
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



tig:drop-stored-procedure("CreateSignatureToEnvelope_MD"),
tig:create-stored-procedure("CreateSignatureToEnvelope_MD","
declare xmlspace preserve;

import module namespace util = 'xquery:modules:util';
import module namespace xmlf = 'xquery:modules:xml';
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

declare variable $EnvelopeId as string external; 

declare variable $keyFile as xs:string {'C:/security/keys/SW_keystore'}; 
declare variable $keyPwd as xs:string {'md1841'}; 
declare variable $keyAlias as xs:string {'MDAlias'}; 

declare function local:createSignature($doc as node(),$local_key_file as xs:string,
	 $local_keyPwd as xs:string, $local_keyAlias as xs:string,$signURI as xs:string,
	 $signId as xs:string) as node(){ 

	let $signedDoc := local:signPedigreeNode($doc, $local_key_file, $local_keyPwd , $local_keyAlias, $signURI, $signId) 
	return 
		(:xmlf:parse(bin:as-string(binary{$signedDoc},'UTF-8')):)
		$signedDoc
}; 

(: Make this generic, right now using it for signing shippedPedigree node :)
update
for $pedigree in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $EnvelopeId ]/*:pedigree
let $pedNode := document{<pedigree xmlns=""urn:epcGlobal:Pedigree:xsd:1"">{$pedigree/*:shippedPedigree}</pedigree>}
let $pedigree_sign := local:createSignature($pedNode,
					$keyFile,$keyPwd,$keyAlias,
					concat('#',$pedigree/*:shippedPedigree/@id),
					concat('_',util:create-uuid()))
replace $pedigree with $pedigree_sign/*:pedigree
"),


tig:drop-stored-procedure("InsertShippedPedigree"),
tig:create-stored-procedure("InsertShippedPedigree","
 declare variable $envNode as node() external; 
 declare variable $fileName as string external; 
 let $test := $envNode/node() 
 let $envId := data($test/*:serialNumber) 
 let $result := tig:insert-document('tig:///ePharma_MD/ShippedPedigree',$envNode) 
 let $sign := tlsp:CreateSignatureToEnvelope_MD($envId) return $envId ")
 
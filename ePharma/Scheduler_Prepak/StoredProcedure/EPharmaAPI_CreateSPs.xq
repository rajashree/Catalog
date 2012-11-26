
tig:create-stored-procedure("CheckAndCreateSubscriber",
"
declare variable $subPartyId as string external;
declare variable $partyRole as string external;
declare variable $contactName as string external;
declare variable $title as string external;
declare variable $address as string external;
declare variable $phone as string external;
declare variable $email as string external;
declare variable $comments as string external;
declare variable $subsId as string external;
declare variable $certKey as string external;

let $result := for $i in collection('tig:///ePharma/SubscriptionInfo')/SubscriberAgreement
		where $i/PartyInfo/PartyID = $subPartyId
		return $i
return 
(
	if(empty($result)) then
		let $doc := <SubscriberAgreement>
			<SubscriberID>{data($subsId)}</SubscriberID>
			<SubscriptionStatus>{data(xs:string('active'))}</SubscriptionStatus>
			<SubscriptionStart>{data(fn:current-dateTime())}</SubscriptionStart>
			<SubscriptionEnd></SubscriptionEnd>
			<PartyRole>{data($partyRole)}</PartyRole>
			<PartyInfo>
				<PartyID>{data($subPartyId)}</PartyID>
				<PrimaryContactDetails>
				    <ContactName>{data($contactName)}</ContactName>
				    <Title>{data($title)}</Title>
				    <Address>{data($address)}</Address>
				    <Phone>{data($phone)}</Phone>
				    <Email>{data($email)}</Email>
			        </PrimaryContactDetails>
			</PartyInfo>
			<CertificateKey>{data($certKey)}</CertificateKey>
			<Comments>{data($comments)}</Comments>
			<LastUsed>{data(fn:current-dateTime())}</LastUsed>
		</SubscriberAgreement>
		return
			let $res := tig:insert-document('tig:///ePharma/SubscriptionInfo',$doc)
			return 'Successfully Created Subscriber. The certKey will be sent to the email address provided pending System Administrator approval.'
	else
		'Subscriber Id already present in the system.'
)
"),


tig:create-stored-procedure('updateLastUse',"
declare variable $sesId as string external;

update 
for $d in collection('tig:///EAGRFID/SysSessions')/session[sessionid=$sesId]
replace value of $d/lastuse with fn:current-dateTime()
"),



tig:create-stored-procedure('validateSession',"

declare variable $sesId as string external;

let $last := xs:dateTime(collection('tig:///EAGRFID/SysSessions')/session[sessionid = $sesId ]/lastuse)
let $since := fn:current-dateTime( ) - $last
return
        if($since < xdt:dayTimeDuration('PT15M'))
        then
            tlsp:updateLastUse($sesId)
        else
            'false'
"),



tig:create-stored-procedure("GetSession",
"
import module namespace util = 'xquery:modules:util';
declare variable $subCert as string external;
declare variable $pin as string external;
declare variable $userName as string external;
declare variable $password as string external;

let $usr := (for $i in collection('tig:///EAGRFID/SysUsers')/User
where $i/UserName = $userName
return $i )
return
(
if(fn:empty($usr)) then
(
	'User Name does not exist in the system'
)
else
(
	let $crt := (for $j in collection('tig:///ePharma/SubscriptionInfo')/SubscriberAgreement
			where $j/SubscriberID = $subCert and $j/SubPin = $pin
			return $j)
	return 
	(
		let $tp := (for $k in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
			    where $k/name = $crt/PartyInfo/PartyID 
			    and $k/name = $usr/BelongsToCompany
			    return $k)
		return 
		(
			let $result := (for $l in collection('tig:///EAGRFID/SysSessions')
					where $l/session/username = $userName
					return $l )
			return
			(
				let $sessId := util:create-unique-id( ) return
				(
				    if(fn:empty($result)) then
				    (
				        let $doc := <session>
					  <sessionid>{$sessId}</sessionid>
					  <userid>{$userName}</userid>
					  <username>{$userName}</username>
				  	  <accesslevel>Admin</accesslevel>
					  <sessionstart>{fn:current-dateTime()}</sessionstart>
				  	  <userip>{$subCert}</userip>
					  <lastuse>{fn:current-dateTime()}</lastuse>
					  <tp_company_nm>{$tp/name}</tp_company_nm>
					  <tp_company_id>{$tp/id}</tp_company_id>
	   				  </session>
					return
					(
					let $ss := tig:insert-document('tig:///EAGRFID/SysSessions',$doc)
					return $sessId
					)
				    )
				    else
				    (	
					let $rt := tig:delete-document(document-uri($result)) return
					(
					let $doc := <session>
					<sessionid>{$sessId}</sessionid>
					<userid>{$userName}</userid>
					<username>{$userName}</username>
					<accesslevel>Admin</accesslevel>
					<sessionstart>{fn:current-dateTime()}</sessionstart>
					<userip>{$subCert}</userip>
					<lastuse>{fn:current-dateTime()}</lastuse>
					<tp_company_nm>{$tp/name}</tp_company_nm>
					<tp_company_id>{$tp/id}</tp_company_id>
	   				</session>
					return
					(
						let $ss := tig:insert-document('tig:///EAGRFID/SysSessions',$doc)
						return $sessId
					)
					)
				    )
				)
			)
		)
	)
	)
)
"),



tig:create-stored-procedure("InsertPedigreeInRP","

declare variable $sessionId as string external;
declare variable $p as node() external;

let $isValid := tlsp:validateSession($sessionId)
return
(
	if($isValid = 'false') then 
		'Session is not valid'
	else
		insert document $p into  'tig:///ePharma/ReceivedPedigree'
)
"),


tig:create-stored-procedure("LogOut",
"
declare variable $subCert as string external;

for $i in collection('tig:///EAGRFID/SysSessions')
where $i/session/sessionid = $subCert
return tig:delete-document(document-uri($i))
"),


tig:create-stored-procedure("PedIn",
"
declare variable $pedigreeId as string external;
for $i in collection('tig:///ePharma/ShippedPedigree')/pedigree
return $i"),



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


tig:create-stored-procedure("CreateReceivedPedigreeForPedigrees_EPharmaAPI","


declare variable $signerId as string external;
declare variable $pedigreeEnvelopeId as string external;
import module namespace util = 'xquery:modules:util';

let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $signerId
return <signerInfo><name>{data($y/FirstName)}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email><url>{data($y/Email)}</url>
</signerInfo>

let $date := substring-before(fn:current-dateTime() cast as string,'T')
let $dateTime := substring-before(fn:current-dateTime() cast as string,'.')

for $k in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $pedigreeEnvelopeId]/*:pedigree

let $docid := util:create-uuid()
let $recvid := util:create-uuid()
let $ndc := $k//*:initialPedigree/*:productInfo/*:productCode
return

let $j :=

(tig:insert-document('tig:///ePharma/ShippedPedigree',<pedigree>
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
</pedigree> ))

return $recvid

"),


tig:create-stored-procedure("GetPedInXMLData","
declare variable $pedigreeID as string external;

let $initialPed := (for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigree
where $i/*:receivedPedigree/*:documentInfo/*:serialNumber=$pedigreeID
return $i//*:initialPedigree)
return 
<PedigreeData>
	<messageNm>PEDIN</messageNm>
	<pedigreeId>{data($initialPed/*:DocumentInfo/*:serialNumber)}</pedigreeId>
	<ProductInfo>
		
		<drugName>{data($initialPed/*:productInfo/*:drugName)}</drugName>
		<manufacturer>{data($initialPed/*:productInfo/*:manufacturer)}</manufacturer>
		<productCode>{data($initialPed/*:productInfo/*:productCode)}</productCode>
		<dosageForm>{data($initialPed/*:productInfo/*:dosageForm)}</dosageForm>
		<strength>{data($initialPed/*:productInfo/*:strength)}</strength>
		<containerSize>{data($initialPed/*:productInfo/*:containerSize)}</containerSize>
	</ProductInfo>
	<ItemInfo>
		<lot>{data($initialPed/*:itemInfo/*:lot)}</lot>
		<quantity>{data($initialPed/*:itemInfo/*:quantity)}</quantity>
		<expirationDate>{data($initialPed/*:itemInfo/*:expirationDate)}</expirationDate>
	</ItemInfo>
	<TransactionInfo>
		<Address>
			<businessId>{data($initialPed/*:transactionInfo/*:senderInfo/*:licenseNumber)}</businessId>
			<businessName>{data($initialPed/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</businessName>
			<street1>{data($initialPed/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1)}</street1>
			<street2>{data($initialPed/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2)}</street2>
			<city>{data($initialPed/*:transactionInfo/*:senderInfo/*:businessAddress/*:city)}</city>
			<stateOrRegion>{data($initialPed/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion)}</stateOrRegion>
			<postalCode>{data($initialPed/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode)}</postalCode>
			<country>{data($initialPed/*:transactionInfo/*:senderInfo/*:businessAddress/*:country)}</country>
		</Address>
		<TransactionId>{data($initialPed/*:transactionInfo/*:transactionIdentifier/*:identifier)}</TransactionId>
		<TransactionType>{data($initialPed/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</TransactionType>
		<TransactionDate>{concat(data($initialPed/*:transactionInfo/*:transactionDate),'T00:00:00')}</TransactionDate>
	</TransactionInfo>


</PedigreeData>
	
"),




tig:create-stored-procedure("CreateSignatureRP","
 declare xmlspace preserve;
 import module namespace util = 'xquery:modules:util'; 
import module namespace xmlf = 'xquery:modules:xml';
 declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
 declare variable $pedigreeId as string external;
 declare variable $keyFile as xs:string {'C:/security/keys/SW_keystore'}; 
declare variable $keyPwd as xs:string {'md1841'}; 
declare variable $keyAlias as xs:string {'MDAlias'}; 
declare function local:createSignature($doc as node(),$local_key_file as xs:string,
 $local_keyPwd as xs:string, $local_keyAlias as xs:string,$signURI as xs:string,
 $signId as xs:string) as node(){
 let $signedDoc := local:signPedigreeNode($doc, $local_key_file, $local_keyPwd , $local_keyAlias, $signURI, $signId)
 return (:xmlf:parse(bin:as-string(binary{$signedDoc},'UTF-8')):) $signedDoc }; 
(: Make this generic, right now using it for signing shippedPedigree node :) 
update for $pedigree in collection('tig:///ePharma/ShippedPedigree')/*:pedigree[*:receivedPedigree/*:documentInfo/*:serialNumber = $pedigreeId]

let $pedNode := document{<pedigree xmlns=&quot;urn:epcGlobal:Pedigree:xsd:1&quot;>{$pedigree/*:receivedPedigree}</pedigree>} 
let $pedigree_sign := local:createSignature($pedNode, $keyFile,$keyPwd,$keyAlias, concat('#',$pedigree/parent::*/@id), concat('_',util:create-uuid())) 
replace $pedigree with $pedigree_sign/*:pedigree "),


tig:create-stored-procedure("Ped-InPrePak","
declare namespace ePharma='PrepakSQLServer'; 
declare variable $xmlString as node() external; 
let $ped-in := ePharma:execute-stored-procedure((),
'PedigreeData','ePharma_Messages_INSERT',
(data($xmlString//TransactionInfo/Address/businessId),
data($xmlString//TransactionInfo/Address/businessName),
data($xmlString//TransactionInfo/Address/street1), 
data($xmlString//TransactionInfo/Address/street2),
data($xmlString//TransactionInfo/Address/city),
data($xmlString//TransactionInfo/Address/stateOrRegion), 
data($xmlString//TransactionInfo/Address/postalCode),
data($xmlString//TransactionInfo/TransactionId), 
data($xmlString//TransactionInfo/TransactionType),
data($xmlString//TransactionInfo/TransactionDate), 
data($xmlString//ProductInfo/productCode), 
data($xmlString//ProductInfo/drugName), 
data($xmlString//ProductInfo/strength),
data($xmlString//ProductInfo/dosageForm),
data($xmlString//ProductInfo/containerSize),
data($xmlString//ProductInfo/manufacturer), 
data($xmlString//pedigreeId) )) 
 for $j in $xmlString//ItemInfo 
  return 
  ePharma:execute-stored-procedure((),'PedigreeData',
    'ePharma_Messages_Lots_INSERT', 
    (data($ped-in), data($j/lot), 
     data($j/quantity))) 
")





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
			 { let $all_item_info  := local:getItemInfo($k) 
				return ($all_item_info)
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
			  { let $all_item_info  := local:getItemInfo($k) 
				return ($all_item_info)
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



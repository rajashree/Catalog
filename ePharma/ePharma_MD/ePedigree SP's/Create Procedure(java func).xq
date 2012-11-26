(: For Send Pedigree :)
tig:create-stored-procedure("SendPedigree",
"

declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $apnDocId as string external;
declare variable $mail as string external;
declare variable $user as string external;
declare variable $pwd as string external;

declare character-encoding 'UTF-8';

declare function local:to-string( $node as node()* ) as string
{
  bin:as-string( binary { $node }, 'UTF-8' )
};

for $i in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope
where $i/serialNumber = $apnDocId
return 
(
	for $j in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
	where $j/name = $i/destination
	return 
	(
		let $r := local:to-string($i) return
		local:smtpClientAttachement('admin@epharmasolution.com',$mail,'smtp.bizmail.yahoo.com',
		'Send APN','APN document has been attached to this email.',$r,$user,$pwd)		
	)
)
"),

(: Create Pedigree Signature :)
tig:create-stored-procedure("pedigreeSignature","
declare character-encoding 'UTF-8';
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';

declare variable $EnvelopeId as string external;

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
 for $pedigree in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber = $EnvelopeId ]/pedigree
 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
 where not( exists($pedigree/*:Signature) )
 replace  $pedigree  with  $pedigree_sign/pedigree
 
")



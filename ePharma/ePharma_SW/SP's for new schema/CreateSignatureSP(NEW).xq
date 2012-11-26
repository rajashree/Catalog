tig:drop-stored-procedure("CreateSignatureToReceivedPedigree"),

tig:create-stored-procedure("CreateSignatureToReceivedPedigree","
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

tig:drop-stored-procedure("pedigreeLevelSignature"),
tig:create-stored-procedure("pedigreeLevelSignature","
 import module namespace util = 'xquery:modules:util'; 
import module namespace xmlf = 'xquery:modules:xml'; 
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
 declare xmlspace preserve;
 declare variable $EnvelopeId as string external; 
declare variable $pedId as string external; 
declare variable $keyFile as xs:string {'C:/security/keys/SW_keystore'};
 declare variable $keyPwd as xs:string {'md1841'};
 declare variable $keyAlias as xs:string {'MDAlias'};
 declare function local:createSignature($doc as node(),$local_key_file as xs:string, $local_keyPwd as xs:string, $local_keyAlias as xs:string,$signURI as xs:string, $signId as xs:string) as node()
{
 let $signedDoc := local:signPedigreeNode($doc, $local_key_file, $local_keyPwd , $local_keyAlias, $signURI, $signId) return 
 $signedDoc };
 update for $pedigree in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $EnvelopeId
 and *:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber =$pedId ]/*:pedigree
 let $pedNode := document{<pedigree xmlns=""urn:epcGlobal:Pedigree:xsd:1"">{$pedigree/*:shippedPedigree}</pedigree>}
let $pedigree_sign := local:createSignature($pedNode,
					$keyFile,$keyPwd,$keyAlias,
					concat('#',$pedigree/*:shippedPedigree/@id),
					concat('_',util:create-uuid()))
replace $pedigree with $pedigree_sign/*:pedigree
"),
tig:drop-stored-procedure("CreateSignatureToEnvelope_PP"),
tig:create-stored-procedure("CreateSignatureToEnvelope_PP","
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
for $pedigree in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $EnvelopeId ]/*:pedigree
let $pedNode := document{<pedigree xmlns=""urn:epcGlobal:Pedigree:xsd:1"">{$pedigree/*:shippedPedigree}</pedigree>}
let $pedigree_sign := local:createSignature($pedNode,
					$keyFile,$keyPwd,$keyAlias,
					concat('#',$pedigree/*:shippedPedigree/@id),
					concat('_',util:create-uuid()))
replace $pedigree with $pedigree_sign/*:pedigree
")

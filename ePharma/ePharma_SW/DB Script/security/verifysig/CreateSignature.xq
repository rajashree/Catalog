
tig:drop-stored-procedure("CreateSignature"),
tig:create-stored-procedure("CreateSignature",
"
declare namespace bin = 'http://www.rainingdata.com/TigerLogic/binary-support';
declare variable $doc as node() external;
declare variable $keyFile as string external;
declare variable $keyPwd as string external;
declare variable $keyAlias as string external;
declare character-encoding 'UTF-8';

declare function local:to-string($node as node()* ) as string
{
  bin:as-string(binary{$node},'UTF-8')
};
declare function local:to-node($str as xs:string ) as node() 
{ bin:parse( binary{$str},'text/xml') treat as node() 
}; 

let $xmlStr := local:to-string($doc) 
let $signedDoc := local:signXMLDocument($xmlStr, $keyFile, $keyPwd, $keyAlias)
return  local:to-node(substring($signedDoc,39) )

")
tig:drop-stored-procedure("SendAPN"),
tig:create-stored-procedure("SendAPN",
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

for $i in collection('tig:///ePharma/APN')/APN
where $i/DocumentId = $apnDocId
return 
(
	for $j in collection('tig:///CatalogManager/TradingPartner')/TradingPartner
	where $j/name = $i/To/Name
	return 
	(
		let $r := local:to-string($i) return
		local:smtpClientAttachement('admin@epharmasolution.com',$mail,'smtp.bizmail.yahoo.com',
		'Send APN','APN document has been attached to this email.',$r,$user,$pwd)		
	)
)
")


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
tig:create-stored-procedure("DeleteMessage",
"
declare variable $MessageID as string external;
for $i in collection('tig:///ePharma/Alerts') 
where $i/AlertMessage/MessageID = $MessageID 
return tig:delete-document(document-uri($i))
"),
tig:create-stored-procedure("MessageDetails","
declare variable $MessageID as string external; 
let $pedID := for $i in collection('tig:///ePharma/Alerts')/AlertMessage[MessageID = $MessageID]
	     return data($i/Message/DocId)
let $ped := for $b in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $pedID] 
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

tig:create-stored-procedure("getSearchQueries2","
 declare variable $root_node as xs:string {'$root'};
 declare variable $common_str as xs:string {""
 
 declare function local:getStatus($pedID as xs:string?)  {

 for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedID] 
 for $status in $ps/Status 
 where $status/StatusChangedOn  = $ps/TimeStamp[1]
 return $status/StatusChangedTo/text()
   
};

 ""};
 declare variable $recvd_local_fn_1 {""declare function local:returnPedigrees($root as document-node()? ) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/*:pedigreeEnvelope/*:pedigree

 ""};

 declare variable $recvd_local_fn_3 {"" 
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


 declare variable $shipped_local_fn_1 {""declare function local:returnPedigrees($root as document-node()?) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root//*:pedigree/*:receivedPedigree
 ""};
 
 declare variable $shipped_local_fn_3 {"" 
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
					<Collection>tig:///ePharma/ReceivedPedigree</Collection>	
					<Collection>tig:///ePharma/ShippedPedigree</Collection>	
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
					<Collection>tig:///ePharma/ShippedPedigree</Collection>	
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

    declare function local:getRootWhereClause($root as node()?,$no as xs:integer,$keys as xs:string*,$values as xs:string*) {
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
      
     declare function local:getUserValReplaced($src as xs:string,$search4 as xs:string,$rep as xs:string) {
  	(: Replaces only the first occurrance :)
  	let $str1 as xs:string? := substring-before( $src , $search4 )
  	let $str2 as xs:string? := substring-after(  $src , $search4 )
  	return if( string-length($str1) = 0 and string-length($str2) = 0 ) then  $src  else string-join( ($str1,$rep,$str2),'' ) 
     };
	
      declare function local:getPedWhereClause($root as node()?,$no as xs:integer,$keys as xs:string*,$values as xs:string*) {
		
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
tig:create-stored-procedure("EnvelopeSearch","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma/ReceivedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope
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
tig:create-stored-procedure("getStatusOfPedigrees","

declare variable $envelopId as string external;

for $ped in collection ('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envelopId]
let $pedid := data($ped/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)
for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedid] 
for $status in $ps/Status 
where $status/StatusChangedOn  = $ps/TimeStamp[1]
return $status/StatusChangedTo/text()

"),
tig:create-stored-procedure("CreateReceivedPedigreeForPedigreesNew","

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

for $k in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree

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
{tlsp:CreateSignToReceivedPedigreeNew($pedigreeId)}
</pedigree> 

return $j

"),

(:tig:create-stored-procedure("CreateSignToReceivedPedigreeNew","
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
 

 for $pedigree in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree
  where $pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId
return
 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
(: where not( exists($pedigree/*:Signature) ):)
return $pedigree_sign/*:pedigree/*:Signature[2]
 
"):)
tig:create-stored-procedure("PedStatusElement","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber= $envelopeid]
let $status := (for $i in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'Received'
"),
tig:create-stored-procedure("EnvelopeDetails","
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
for $e in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber=$envelopeid]
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
	<status>{ tlsp:PedStatusElement($envelopeid, $pedID ) }</status>,
	<count>{data(count($e/*:pedigree))}</count>

}</output>

"),
tig:create-stored-procedure("EnvelopeDisplay","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/*:serialNumber)}</EnvelopeID>,
<Date>{data($e/*:date)}</Date>, 
<source>{data($e/*:sourceRoutingCode)}</source>,
<destination>{data($e/*:destinationRoutingCode)}</destination>
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
tig:create-stored-procedure("ShippedPedigreedetails","
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

for $e in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope/*:pedigree
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
"),tig:create-stored-procedure("getManufacturerDetails","
declare variable $catalogName as xs:string external;
declare variable $pedId as string external;
let $mfrName :=( for $n in collection(concat('tig:///ePharma/',$catalogName))/*:pedigreeEnvelope
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
tig:create-stored-procedure("getAuditTrailDetails","
declare variable $catalogName as xs:string external;
declare variable $serialId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
return $i/parent::*:*)
return ($k,$k//descendant::*:*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
return $i/parent::*:*)
return ($k,$k//descendant::*:*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else ('No collection found')
"),
tig:create-stored-procedure("InitialPedigreeDetails","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $shipId]
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
tig:create-stored-procedure("InitialPedigreeDetailsRP","

declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
declare variable $shipId as xs:string external;

for $e in collection('tig:///ePharma/ReceivedPedigree')/*:pedigreeEnvelope[*:serialNumber = $shipId]
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
tig:create-stored-procedure("GetStatusDetails","
declare variable $pedId as string external;
for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedId] 
for $status in $ps/Status   where $status/StatusChangedOn  = $ps/TimeStamp[1] return $status 
"),
tig:create-stored-procedure("getAttachmentDetails","
declare variable $catalogName as xs:string external;
declare variable $serialId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in 
collection(concat('tig:///ePharma/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
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
collection(concat('tig:///ePharma/',$catalogName))/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo[*:serialNumber = $serialId]
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
tig:create-stored-procedure("GetProcessedEnteredPedigrees","

for $i in collection('tig:///ePharma/PaperPedigree')
(:let $time := substring-before(fn:string(fn:adjust-dateTime-to-timezone(tig:last-update-time($i),xdt:dayTimeDuration('PT7H00M' ))),'.'):)
let $time := fn:adjust-dateTime-to-timezone(tig:last-update-time($i),xdt:dayTimeDuration('PT7H00M' ))
order by $time descending
return 
<output>
{
<pedigreeId>{data($i/initialPedigree/DocumentInfo/serialNumber)}</pedigreeId>, 
<InvoiceNumber>{data($i/initialPedigree/transactionInfo/transactionIdentifier/identifier)}</InvoiceNumber>,
<VendorName >{data($i/initialPedigree/transactionInfo/senderInfo/businessAddress/businessName)}</VendorName>,
<DrugName>{data($i/initialPedigree/productInfo/drugName)}</DrugName>,
<NDC>{data($i/initialPedigree/productInfo/productCode)}</NDC>,
<VendorLot>{for $k in $i/initialPedigree/itemInfo/lot
return concat(data($k),',')}</VendorLot>,
<processedDate>{$time}</processedDate>
}
</output>
"),
tig:create-stored-procedure("getSearchQueriesNew2",
"
 declare variable $root_node as xs:string {'$root'};
 declare variable $common_str as xs:string {""
 
 declare function local:getStatus($pedID as xs:string?) {

 for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedID] 
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

 
 declare variable $g_search_elts as node()
		{<Search>
		   <Scenarios>	
			<Scenario>	
				<Collections>
					
					<Collection>tig:///ePharma/ShippedPedigree</Collection>	
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
		$count_elements as xs:integer) as xs:string {
  
 let $first_local_fn as xs:string :=  xs:string($common_local_str ) 
 let $second_local_fn_start as xs:string := xs:string ( if( $scenario_3 ) then $recvd_start else ())
 let $ped_full_clause as xs:string := xs:string( if( not(empty($ped_where_clause)) and string-length($ped_where_clause) > 0 ) then concat(' where ', $ped_where_clause ) else '' ) 
 let $second_local_fn_end as xs:string := xs:string ( if( $scenario_3 ) then $recvd_end else () ) 
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
  return local:getQuery($col_uri/text(), $root_where_clause ,$ped_where_clause,$root_node, $index = 1 ,$common_str,$recvd_local_fn_1 ,$recvd_local_fn_3 ,$count_elts )
  
"),
tig:create-stored-procedure("getSearchResultsNew","

 declare variable $search_elt_names_ext as xs:string* external;
 declare variable $search_elt_values_ext as xs:string* external;   
 
 let $queries  as xs:string+ :=  tlsp:getSearchQueriesNew2( $search_elt_names_ext , $search_elt_values_ext )
 return <SearchInfo>{ 
 for $query in $queries
 return evaluate( $query )
 }
 </SearchInfo> 
"),

tig:create-stored-procedure("ShippingPedigreeDetails","

declare variable $serialNumber as string external;

declare function local:getItemInfo($node as node()){ 
 if( exists($node/*:shippedPedigree/*:itemInfo/*:quantity)) then 
$node/*:shippedPedigree/*:itemInfo/*:quantity
else if( exists($node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:quantity)) then 
$node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:quantity

else if (exists($node/*:shippedPedigree/*:repackagedPedigree ))then 
( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo/*:quantity )
else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:quantity) )then
( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:quantity) else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getItemInfo($node/*:shippedPedigree/*:pedigree) else local:getItemInfo($node/*:receivedPedigree/*:pedigree)

};
declare function local:getProductInfo($e as node() ){
let $item  := local:getItemInfo($e)
return


if(exists($e/*:shippedPedigree/*:repackagedPedigree)) then(
<drugName>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:drugName)}</drugName>,
<productCode>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode/@type )}</codeType>,

<quantity>{data(local:getItemInfo($e))}</quantity>,
<manufacturer>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:manufacturer)}</manufacturer>,
<dosageForm>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:containerSize)}</containerSize>
)
else(
<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,

<quantity>{data(local:getItemInfo($e))}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>
)
};


for $e in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree
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

tig:create-stored-procedure("getRepackagedInfo","
declare variable $serialNumber as string external;
let $allPedigrees := 
 (
 for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/descendant::*:*
 where $i/name() = 'pedigree'
 return $i
 )

 for $ped in $allPedigrees[*:shippedPedigree/*:documentInfo/*:serialNumber = $serialNumber]
 return $ped/*:shippedPedigree
"),
tig:create-stored-procedure("ReturnLotInfo","

declare function local:GiveResult($ProdInfo as node()){
let $lotNum := data($ProdInfo/LotInfo/lot)
for $i in collection('tig:///ePharma/PedigreeBank')/PedigreeBank
where $i/InventoryOnHand/LotInfo/SWLotNum =  $lotNum
return $i/InventoryOnHand/LotInfo/LotNumber
};

declare variable $PedShipNode as node()* external;
let $prodInfoNode := $PedShipNode//ProductInfo
return <lot>{(
for $prod in $prodInfoNode[UseVendorLot = 'N']/LotInfo 
return <LotInfo>{($prod/lot, $prod/Comment )}</LotInfo>
 ,
for $prod in $prodInfoNode[UseVendorLot = 'Y']/LotInfo 

let $vendorLotNum := local:GiveResult($prod)
return <LotInfo>{(<lot>{$vendorLotNum}</lot>, $prod/Comment )}</LotInfo>
)}</lot>
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
for $e in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree
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
tig:create-stored-procedure("ManufacturerDetails","

declare variable $pedId as string external;
let $mfrName :=( for $n in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber= $pedId]
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
tig:create-stored-procedure("getStatusOfPedigree","

declare variable $pedId as string external;
for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedId] 
for $status in $ps/Status 
where $status/StatusChangedOn  = $ps/TimeStamp[1]
return $status/StatusChangedTo/text()
"),
tig:create-stored-procedure("PedIdForFax","
import module namespace util = 'xquery:modules:util';
let $pedid := util:create-uuid()
return $pedid"),
tig:create-stored-procedure("deleteProcessedPedigree","
declare variable $id as string external;
for $i in collection('tig:///ePharma/PaperPedigree')
where $i/initialPedigree/DocumentInfo/serialNumber = $id
return tig:delete-document(document-uri($i))
"),
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
tig:create-stored-procedure("GetRPDetails","
declare variable $pedID as string external;

for $j in collection('tig:///ePharma/ShippedPedigree')/*:pedigree
where $j/*:receivedPedigree/*:documentInfo/*:serialNumber = $pedID
return $j
"),
tig:create-stored-procedure("CreateShippedPedigreenew","
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
let $pdenv := (for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
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
let $quantity1 :=(for $x in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice[ID = $despatchAdviseNo]/DespatchLine
	        where $x/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DeliveredQuantity)
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
	
	<contactInfo><name>{data($k/BuyerParty/BuyerContact/Name)}</name><title>{data($k/BuyerParty/BuyerContact/Title)}</title>
	<telephone>{data($k/BuyerParty/BuyerContact/Telephone)}</telephone><email>{data($k/BuyerParty/BuyerContact/E-Mail)}</email>
	</contactInfo>
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
let $docid := util:create-uuid()
(:let $shipid := util:create-unique-id('ID'):)
let $shipid := util:create-uuid()
let $contid := util:create-uuid()
let $res := tlsp:GetRPDetails($pedID)

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
(:let $ret := tig:insert-document('tig:///ePharma/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment($envid) :)
(:let $sig := tlsp:pedigreeSignature($envid):)
return $ret :)
return $pdenv
"),
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
let $pdenv := (for $k in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice
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
let $quantity1 :=(for $x in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice[ID = $despatchAdviseNo]/DespatchLine
	        where $x/Item/SellersItemIdentification/ID = $NDC
	        return data($x/DeliveredQuantity))
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
	<city>{data($k/SellerParty/Party/Address/CityName)}</city><stateOrRegion>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</stateOrRegion>
	<postalCode>{data($k/SellerParty/Party/Address/PostalZone)}</postalCode><country>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</country>
	</businessAddress>
	<shippingAddress><businessName>{data($k/SellerParty/ShippingContact/Name)}</businessName>
	<street1>{data($k/SellerParty/Party/Address/StreetName)}</street1><street2>{data($k/SellerParty/Party/Address/BuildingNumber)}</street2>
	<city>{data($k/SellerParty/Party/Address/CityName)}</city><stateOrRegion>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</stateOrRegion>
	<postalCode>{data($k/SellerParty/Party/Address/PostalZone)}</postalCode>
	<country>{data($k/SellerParty/Party/Address/CountrySubentityCode)}</country></shippingAddress>
	<contactInfo><name>{data($k/SellerParty/ShippingContact/Name)}</name><title>{data($k/SellerParty/ShippingContact/Title)}</title>
	<telephone>{data($k/SellerParty/ShippingContact/Telephone)}</telephone><email>{data($k/SellerParty/ShippingContact/E-Mail)}</email>
	</contactInfo>
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
let $docid := util:create-uuid()
let $shipid := util:create-uuid()
let $contid := util:create-uuid()
let $res := tlsp:GetRPDetails($pedID)

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
(:let $ret := tig:insert-document('tig:///ePharma/ShippedPedigree',$pdenv)
(: let $ins := tlsp:updatePedigreebankAfterShipment($envid) :)
return $ret :)
return $pdenv
"),
tig:create-stored-procedure("insertNode","
declare variable $n1 as node() external;
declare variable $uri as string external;
update for $b in doc($uri)//TimeStamp
replace $b with $n1/* 
"),
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
tig:create-stored-procedure("PedigreeDetails","
declare variable $TransactionNumber as string external ;
let $all :=
for $b in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope
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
tig:create-stored-procedure("ShippingEnvelopeSearch","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma/ShippedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope
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
(:tig:create-stored-procedure("pedigreeLevelSignature","
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
 for $pedigree in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber =  $EnvelopeId 
	and *:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = $pedId]/*:pedigree

 let $pedigree_sign  := <test>{local:createSignature($pedigree,$keyFile,$keyPwd,$keyAlias)}</test>
(: where not( exists($pedigree/*:Signature) ):)
replace  $pedigree/*:Signature  with  $pedigree_sign/*:pedigree/*:Signature[2]

 
"),:)
tig:create-stored-procedure("TPEmailID",
"
declare variable $documentID as string external;
distinct-values(
for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope
where $i/*:serialNumber = $documentID
return 
let $num := data($i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber) 
	for $j in collection('tig:///CatalogManager/TradingPartner')/TradingPartner 
	where data($i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber)  = data($j/businessId) 
	return 
		if(fn:exists(data($j/notifyURI))) 
		then  (data($j/notifyURI)) else (data($j/email)))
"),
tig:create-stored-procedure("ShippingStatusElement","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber= $envelopeid]
(:let $pedId :=data($e/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber):)
let $status := (for $i in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'NA'
"),
tig:create-stored-procedure("ShippingMgnrEnvelopesDisplay","
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

for $e in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber =  $envelopeid]
return 
 for $ped in $e/*:pedigree
 let $pedID as xs:string := string($ped/*:shippedPedigree/*:documentInfo/*:serialNumber)
 return <pedigree>{
       <pedigreeid>{ $pedID }</pedigreeid>,
       local:getProductInfo($ped),
       <TransactionDate>{data($ped/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</TransactionDate>,
       <count>{data(count($e/*:pedigree))}</count>,
       <status>{ tlsp:ShippingStatusElement($envelopeid, $pedID ) }</status>,
      <containerCode>{
       data( $e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:containerCode )
      }</containerCode>,
     <Quantity>{data($e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:pedigreeHandle/*:quantity)}</Quantity>,
     <LotNum>{data($e/*:container[*:pedigreeHandle/*:serialNumber = $pedID ]/*:pedigreeHandle/*:lot)}</LotNum>, 
     <Attachement>{data($ped//*:initialPedigree/*:attachment/*:mimeType)}</Attachement>
}</pedigree>
"),

tig:create-stored-procedure("ShippingMgnrEnvelopeDetails","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/*:serialNumber)}</EnvelopeID>,
<Date>{data($e/*:date)}</Date>, 
<source>{data($e/*:sourceRoutingCode)}</source>,
<destination>{data($e/*:destinationRoutingCode)}</destination>
}</output>
"),


tig:create-stored-procedure("GetBinaryImageForServlet","
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

")
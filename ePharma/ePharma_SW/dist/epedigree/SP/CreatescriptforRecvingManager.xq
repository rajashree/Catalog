tig:create-stored-procedure("getSearchQueries2",
"
 declare variable $root_node as xs:string {'$root'};
 declare variable $common_str as xs:string {""
 
 declare function local:getStatus($pedID as xs:string?) as xs:string {

 for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedID] 
 for $status in $ps/Status 
 where $status/StatusChangedOn  = $ps/TimeStamp[1]
 return $status/StatusChangedTo/text()
   
};

 ""};
 declare variable $recvd_local_fn_1 as xs:string {""declare function local:returnPedigrees($root as document-node()? ) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/PedigreeEnvelope/pedigree
 ""};

 declare variable $recvd_local_fn_3 as xs:string {"" 
 return 
 <Record>
 {
  <pedigreeID>{data($ped/shippedPedigree/documentInfo/serialNumber)}</pedigreeID>,
  <envelopID>{data($root/PedigreeEnvelope/serialNumber)}</envelopID>,
  <envelopeStatus>{local:getStatus($root/PedigreeEnvelope/serialNumber)}</envelopeStatus>,
  <dateRecieved>{$ped/shippedPedigree/transactionInfo/transactionDate/text()}</dateRecieved>,
  <tradingPartner>{$ped/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/shippedPedigree/transactionInfo/transactionIdentifier/identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/shippedPedigree/documentInfo/serialNumber )
  }</status>,
  <createdBy>System</createdBy>,
  <docURI>{$docURI}</docURI>
 }
 </Record>	
 };
 ""};


 declare variable $shipped_local_fn_1 as xs:string {""declare function local:returnPedigrees($root as document-node()?) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/pedigree/receivedPedigree
 ""};
 
 declare variable $shipped_local_fn_3 as xs:string {"" 
 return <Record>
 {
  <pedigreeID>{$ped/documentInfo/serialNumber/text()}</pedigreeID>,
  <envelopID>N/A</envelopID>,
  <envelopeStatus>N/A</envelopeStatus>,
  <dateRecieved>{$ped/receivingInfo/dateReceived/text()}</dateRecieved>,
  <tradingPartner>{$ped/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/documentInfo/serialNumber )
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
						<Path>PedigreeEnvelope/date</Path>
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
						<Path>PedigreeEnvelope/date</Path>
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
						<Path>PedigreeEnvelope/container/containercode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>No</PedPrefix>			
						<Path><![CDATA[<Dummy>{data($ped/shippedPedigree/@id)}</Dummy>]]></Path>
						<Operator>=</Operator>
						<Value>$root/PedigreeEnvelope/container[containercode = '$UserVal']/pedigreeHandle/serialNumber</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>NDC</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>PedigreeEnvelope/pedigree/shippedPedigree/initialPedigree/productInfo/productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>shippedPedigree/initialPedigree/productInfo/productCode</Path>
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
						<Path>PedigreeEnvelope/container/pedigreeHandle/lot</Path>
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
						<Path>PedigreeEnvelope/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
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
						<Path>PedigreeEnvelope/pedigree/shippedPedigree/documentInfo/serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>shippedPedigree/documentInfo/serialNumber</Path>
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
						<Path>PedigreeEnvelope/source</Path>
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
						<Path>local:getStatus( $root/PedigreeEnvelope/pedigree/shippedPedigree/documentInfo/serialNumber/text() )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/shippedPedigree/documentInfo/serialNumber )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>EnvelopeStatus</Name>
					<RootSelection>
					    <Query>			
						<RootPrefix>No</RootPrefix>
						<Path>local:getStatus( $root/PedigreeEnvelope/serialNumber )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>
				<Key>
					<Name>EnvelopeID</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>PedigreeEnvelope/serialNumber</Path>
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
						<Path>pedigree/receivedPedigree/receivingInfo/dateReceived</Path>
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
						<Path>pedigree/receivedPedigree/receivingInfo/dateReceived</Path>
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
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/initialPedigree/productInfo/productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>pedigree/shippedPedigree/initialPedigree/productInfo/productCode</Path>
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
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/initialPedigree/itemInfo/lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>		
						<PedPrefix>Yes</PedPrefix>		
						<Path>pedigree/shippedPedigree/initialPedigree/itemInfo/lot</Path>
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
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifier</Path>
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
						<Path>pedigree/receivedPedigree/documentInfo/serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>documentInfo/serialNumber</Path>
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
						<Path>pedigree/receivedPedigree/pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>pedigree/shippedPedigree/transactionInfo/senderInfo/businessAddress/businessName</Path>
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
						<Path>local:getStatus($root/pedigree/receivedPedigree/documentInfo/serialNumber/text())</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/documentInfo/serialNumber )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>EnvelopeStatus</Name>
					<RootSelection>
					</RootSelection>
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
(:Quick find options Resultqueries sp:)
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
tig:create-stored-procedure("RecEnvelopeSearch","

declare variable $search_elt_names_ext as xs:string* external;
declare variable $search_elt_values_ext as xs:string* external;

let $all_record := tlsp:getSearchResults2( $search_elt_names_ext , $search_elt_values_ext  )//Record[ starts-with(docURI,'tig:///ePharma/ReceivedPedigree/')  ]
let $distinct_eids := distinct-values( $all_record/envelopID )
for $envelope in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope
where $envelope/serialNumber = $distinct_eids
return <output> 
{
<envelopeId>{data($envelope/serialNumber)}</envelopeId>,
<dateRecieved>{data($envelope/date)}</dateRecieved>,
<source>{data($envelope/source)}</source>,
<destination>{data($envelope/destination)}</destination>,
<count>{count($envelope/pedigree)}</count>,
<createdBy>System</createdBy>
}

</output> 
"),
tig:create-stored-procedure("RecPedStatusElement","
declare variable $envelopeid as string external;
declare variable $pedId as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber= $envelopeid]
let $status := (for $i in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID=$pedId]
return 
for $status in $i/Status 
where $status/StatusChangedOn  = $i/TimeStamp[1]
return $status/StatusChangedTo/text())
return 
  if( exists( $status ) ) then $status else 'Received'
"),
tig:create-stored-procedure("RecEnvelopesDisplay","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber =  $envelopeid]
return 
 for $ped in $e/pedigree
 let $pedID as xs:string := string($ped/shippedPedigree/documentInfo/serialNumber)
 return <pedigree>{
       <pedigreeid>{ $pedID }</pedigreeid>,
       <DrugName>{data( $ped//initialPedigree/productInfo/drugName )}</DrugName>,
       <DrugCode>{data($ped//initialPedigree/productInfo/productCode )}</DrugCode>,
       <TransactionDate>{data($ped/shippedPedigree/transactionInfo/transactionDate)}</TransactionDate>,
       <count>{data(count($e/pedigree))}</count>,
       <status>{ tlsp:RecPedStatusElement($envelopeid, $pedID ) }</status>,
      <containerCode>{
       data( $e/container[pedigreeHandle/serialNumber = $pedID ]/containerCode )
      }</containerCode>,
     <Quantity>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/quantity)}</Quantity>,
     <LotNum>{data($e/container[pedigreeHandle/serialNumber = $pedID ]/pedigreeHandle/lot)}</LotNum>, 
     <Attachement>{data($ped//initialPedigree/attachment/mimeType)}</Attachement>
}</pedigree>
"),
tig:create-stored-procedure("RecEnvelopeDetails","
declare variable $envelopeid as string external;
for $e in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope[serialNumber = $envelopeid]
return <output>{
<EnvelopeID>{data($e/serialNumber)}</EnvelopeID>,
<Date>{data($e/date)}</Date>, 
<source>{data($e/source)}</source>,
<destination>{data($e/destination)}</destination>
}</output>
"),
tig:create-stored-procedure("RecGetProductId","
declare variable $pedId as string external;
let $ndc := (for $i in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
where  $i/documentInfo/serialNumber = $pedId
return data($i/pedigree/shippedPedigree/initialPedigree/productInfo/productCode) )

for $j in collection('tig:///EAGRFID/Products')/Product
where $j/NDC = $ndc
return data($j/ProductID)
"),
tig:create-stored-procedure("RecProductDetails","
(:declare variable $envId as string external;:)
declare variable $pedId as string external;
(:declare variable $ndc as string external;:)

<output>{
for $j in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
where $j/documentInfo/serialNumber = $pedId
return $j//initialPedigree/productInfo
}
<item>{
for $item in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
where $item/documentInfo/serialNumber = $pedId
return $item/itemInfo
}
</item>
<root>{
let $a := (for $n in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
           where $n/documentInfo/serialNumber = $pedId 
           return data($n//initialPedigree/productInfo/productCode))
for $i in collection('tig:///CatalogManager/ProductMaster')/Product 
where $i/NDC = $a  return $i
}</root>
<products>{
let $a := (for $n in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope/pedigree/shippedPedigree
           where $n/documentInfo/serialNumber = $pedId 
           return data($n//initialPedigree/productInfo/productCode))
for $k in collection('tig:///EAGRFID/Products')/Product 
where $k/NDC = $a return $k
}
</products></output>
"),
tig:create-stored-procedure("RecgetAuditTrailDetails","
declare variable $catalogName as xs:string external;
declare variable $serualId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree/shippedPedigree/documentInfo[ serialNumber = $serualId]
return $i/parent::*)
return ($k,$k//descendant::*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree/shippedPedigree/documentInfo[ serialNumber = $serualId]
return $i/parent::*)
return ($k,$k//descendant::*[exists(@id)])
(:return ($k,$k//descendant::*[exists(@id)],$k//initialPedigree):)
else ('No collection found')
"),

(: Attachment Details display sp:)
tig:create-stored-procedure("RecgetAttachmentDetails","
declare variable $catalogName as xs:string external;
declare variable $serialId as xs:string external;
if( $catalogName = 'ShippedPedigree') then 
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree//documentInfo[ serialNumber = $serialId]
return $i/parent::*)
return <data>{ 

<true>{exists($k//initialPedigree/attachment)}</true>,
<mimeType>{data($k//attachment/mimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</TransactionType>,
<Date>{data($k//shippedPedigree/transactionInfo/transactionDate)}</Date>,
<TransactionNo>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</TransactionNo>
}
</data>
else if ( $catalogName = 'ReceivedPedigree' ) then
let $k := ( for $i in collection(concat('tig:///ePharma/',$catalogName))//pedigree//documentInfo[ serialNumber = $serialId]
return $i/parent::*)
return <data>{ 

<true>{exists($k//initialPedigree/attachment)}</true>,
<mimeType>{data($k//attachment/mimeType)}</mimeType>,
<PedigreeID>{data($serialId)}</PedigreeID>,
<TransactionType>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifierType)}</TransactionType>,
<Date>{data($k//shippedPedigree/transactionInfo/transactionDate)}</Date>,
<TransactionNo>{data($k//shippedPedigree/transactionInfo/transactionIdentifier/identifier)}</TransactionNo>
}
</data>
else ('No collection found')
")

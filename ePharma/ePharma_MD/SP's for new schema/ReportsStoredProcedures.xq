
tig:create-stored-procedure("getReportCubes_MD","
<output>
{ 
for $i in collection('tig:///ePharma_MD/ReportCubes')//ReportCube/Cubes/Cube 
return <data> <key>{data($i/Key)}</key> <Name>{data($i/Name)}</Name> </data> }
</output>"),

tig:create-stored-procedure("getReportOuputFields_MD","
declare variable $cubeName as xs:string external;
<output>{
for $i at $count in collection('tig:///ePharma_MD/ReportOutputFieldMapping')//ReportOutputFieldMapping/Cubes/Cube[CubeName=$cubeName]/Fields/Field

return 
<data><key>{data($i/FieldKey)}</key>
<Name>{data($i/FieldName)}</Name>


</data>}
</output>
"),

tig:create-stored-procedure("getReportFilters_MD","
declare variable $cubeName as xs:string external;
for $i at $count in collection('tig:///ePharma_MD/ReportFilterMapping')//ReportFiltersMapping/Cubes/Cube[CubeName=$cubeName]/Filter
return 
if($i/Key = 'dateRange')
then
<data>
<key>{data($i/Key)}</key>
<filterName>{data($i/FilterName)}</filterName>
</data>
else if ( $i/Key = 'status' ) then
<data>
<key>{data($i/Key)}</key>
<filterName>{data($i/FilterName)}</filterName>
<status><value>Quarantined</value></status>
</data> else if ($i/Key = 'allStatus' ) then
<data>
<key>{data($i/Key)}</key>
<filterName>{data($i/FilterName)}</filterName>
<status>
<value>Received</value>
<value>Created Signed</value>
<value>Sent</value>
<value>Created Unsigned</value>
<value>Sent Problem</value>
<value>Authenticated</value>
<value>Cancel</value>
<value>Quarantined</value>
<value>Verified</value>
<value>Certified</value>
<value>Rejected</value></status>
</data>
else <data>
	<key>{data($i/Key)}</key>
	<filterName>{data($i/FilterName)}</filterName>
</data>


"),

tig:create-stored-procedure("getFilterFiledsMapping_MD","
declare variable $cubeName as xs:string external;
for $i in collection('tig:///ePharma_MD/ReportFilterMapping')
where $i/ReportFiltersMapping/Cubes/Cube/CubeName = $cubeName
return $i

"),
	
	
tig:create-stored-procedure("getOutputFieldsMapping_MD","
declare variable $cubeName as xs:string external;
for $i in collection('tig:///ePharma_MD/ReportOutputFieldMapping')/ReportOutputFieldMapping
return $i"),
	
tig:create-stored-procedure("getTradingPartnerList_MD","
distinct-values(for $i in collection('tig:///CatalogManager/TradingPartner')
return $i/TradingPartner/name/text())"),

tig:create-stored-procedure("getreportStatus_MD","
 declare variable $pedId as xs:string external; 
declare variable $status as xs:string external; 
let $stat :=( for $i in collection('tig:///ePharma_MD/PedigreeStatus' )/PedigreeStatus[PedigreeID = $pedId] 
let $x := $i/TimeStamp return for $p in $i/Status[StatusChangedOn = $x ] return $p/StatusChangedTo)
 return if($stat = $status ) then <data>true</data> else (<data>false</data>)")

tig:create-stored-procedure("getReportCubes","
<output>
{ 
for $i in collection('tig:///ePharma/ReportCubes')//ReportCube/Cubes/Cube 
return <data> <key>{data($i/Key)}</key> <Name>{data($i/Name)}</Name> </data> }
</output>"),

tig:create-stored-procedure("getReportOuputFields","
declare variable $cubeName as xs:string external;
<output>{
for $i at $count in collection('tig:///ePharma/ReportOutputFieldMapping')//ReportOutputFieldMapping/Cubes/Cube[CubeName=$cubeName]/Fields/Field

return 
<data><key>{data($i/FieldKey)}</key>
<Name>{data($i/FieldName)}</Name>


</data>}
</output>
"),

tig:create-stored-procedure("getReportFilters","
declare variable $cubeName as xs:string external;
for $i at $count in collection('tig:///ePharma/ReportFilterMapping')//ReportFiltersMapping/Cubes/Cube[CubeName=$cubeName]/Filter
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

tig:create-stored-procedure("getFilterFiledsMapping","
declare variable $cubeName as xs:string external;


for $i in collection('tig:///ePharma/ReportFilterMapping')
where $i/ReportFiltersMapping/Cubes/Cube/CubeName = $cubeName
return $i

"),
	tig:create-stored-procedure("getOutputFieldsMapping","
	declare variable $cubeName as xs:string external;
	for $i in collection('tig:///ePharma/ReportOutputFieldMapping')/ReportOutputFieldMapping
	return $i"),tig:create-stored-procedure("getTradingPartnerList","
distinct-values(for $i in collection('tig:///CatalogManager/TradingPartner')
return $i/TradingPartner/name/text())"),
tig:create-stored-procedure("getreportStatus","
 declare variable $pedId as xs:string external; 
declare variable $status as xs:string external; 
let $stat :=( for $i in collection('tig:///ePharma/PedigreeStatus' )/PedigreeStatus[PedigreeID = $pedId] 
let $x := $i/TimeStamp return for $p in $i/Status[StatusChangedOn = $x ] return $p/StatusChangedTo)
 return if($stat = $status ) then <data>true</data> else (<data>false</data>)")
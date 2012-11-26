declare variable $PedgID as string external;

let $m := collection('tig:///ePharma/SecurityDetails')/PwdAlias
where $m/ManfName = ( for $i in collection('tig:///ePharma/Pedigree')/Pedigree[DocumentId = $PedgID]
	        return data($i/Manufacturer/Name) ) 
return 
<output>
{$m/Pwd}
{$m/Alias}
{$m/Path}
</output>
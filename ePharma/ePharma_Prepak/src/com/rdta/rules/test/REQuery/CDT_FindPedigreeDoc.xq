(: tig:drop-stored-procedure("CDT_FindPedigreeDoc"), :)
tig:create-stored-procedure("CDT_FindPedigreeDoc","

declare variable $PedgID as string external;

for $i in collection('tig:///ePharma/Pedigree')/Pedigree
return 
if ($i/DocumentId = $PedgID ) then 'true' else 'false'

")
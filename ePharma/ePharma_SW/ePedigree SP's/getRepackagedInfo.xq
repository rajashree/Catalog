tig:drop-stored-procedure("getRepackagedInfo"),
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
")
tig:drop-stored-procedure("createNewPedBankDocNewschema_MD"),
tig:create-stored-procedure("createNewPedBankDocNewschema_MD", "
declare variable $NDC as string external;
declare variable $recId as string external;
declare variable $pedRcv as node()* external;

declare function local:getItemInfo($node as node()){
if(exists($node/*:receivedPedigree/*:receivingInfo/*:itemInfo) and $node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:lot = $pedRcv//Vendor_Lot_Number) then 
$node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:quantity 
else if( exists($node/*:shippedPedigree/*:itemInfo)and $node/*:shippedPedigree/*:itemInfo/*:lot = $pedRcv//Vendor_Lot_Number) then 
$node/*:shippedPedigree/*:itemInfo/*:quantity 
else if (exists($node/*:shippedPedigree/*:repackagedPedigree  ) and $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo/*:lot = $pedRcv//Vendor_Lot_Number) then 
( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo/*:quantity  )
else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo) and $node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:lot = $pedRcv//Vendor_Lot_Number) then 
( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:quantity ) else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getItemInfo($node/*:shippedPedigree/*:pedigree) else local:getItemInfo($node/*:receivedPedigree/*:pedigree)
};

let $Insertrepack := tlsp:CreateRepackageInstrDoc_MD($NDC,$recId,$pedRcv)
let $swp_ndc := $pedRcv/SWP_Stock_Code
let $lot := (for $j in $pedRcv//LotInfo
let $itemInfo := (for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$i}</pedigree>))

let $quantity :=  xs:integer( for $i in $itemInfo
		 return data($i))
let $res := tlsp:UpDateTotalInventory_MD($NDC, $quantity cast as integer )
return 
<LotInfo>
<LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
<SWLotNum>{data($j/SWP_Lot_Number)}</SWLotNum>
<ShippingInfo/>
<Quantity>{$quantity}</Quantity>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<ReceivedPedigreeID>{$recId}</ReceivedPedigreeID>
</LotInfo>)

let $PedigreeBank :=
(
for $s in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree 
where $s/*:documentInfo/*:serialNumber=$recId
return
<PedigreeBank>
<RepackagedInstructions>{$Insertrepack}</RepackagedInstructions>
<InventoryOnHand>
<NDC>{$NDC}</NDC>
<SWNDC>{data($swp_ndc)}</SWNDC>
<TotalInventory>{sum( ( for $i in $lot return xs:integer(data($i/Quantity )) ))}</TotalInventory>
{$lot}
</InventoryOnHand>
</PedigreeBank>
)
return insert document $PedigreeBank into  'tig:///ePharma_MD/PedigreeBank' 


")
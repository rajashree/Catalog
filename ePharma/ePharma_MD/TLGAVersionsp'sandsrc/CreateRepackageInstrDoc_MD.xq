tig:drop-stored-procedure("CreateRepackageInstrDoc_MD"),
tig:create-stored-procedure("CreateRepackageInstrDoc_MD","
declare variable $ndc as string external;
declare variable $recId as string external;
declare variable $pedRcv as node()* external;
import module namespace util = 'xquery:modules:util';

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


let $lot := (for $j in $pedRcv//LotInfo
let $itemInfo := (for $m in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigree/*:receivedPedigree[*:documentInfo/*:serialNumber= $recId]
	        return local:getItemInfo(<pedigree>{$m}</pedigree>))

let $quantity := xs:integer( for $k in $itemInfo
		   return data($k))
return <root>
	  <prevQuantity>{$quantity}</prevQuantity>
	  <LotNumber>{data($j/Vendor_Lot_Number)}</LotNumber>
	   <LotInfo>
		<Quantity/>
		<LotNumber>{data($j/SWP_Lot_Number)}</LotNumber>
		<SerialNumber/>
		<PreviousLotNumber>{data($j/Vendor_Lot_Number)}</PreviousLotNumber>
	    </LotInfo>
	</root>
)

let $docid := util:create-uuid()

let $repackNode := 
<RepackageInstructions>
	<DocumentId>{$docid}</DocumentId>
	<NewProduct>
	    <NDC>{data($pedRcv/SWP_Stock_Code)}</NDC>
	   <TransactionType/>
	  {$lot/LotInfo}		
	    <PreviousProduct>
		<NDC>{$ndc}</NDC>
		{$lot/LotNumber}
		<Quantity>{sum( ( for $i in $lot return xs:integer(data($i/prevQuantity )) ))}</Quantity>
		<SerialNumber/>
	   </PreviousProduct>
	</NewProduct>
</RepackageInstructions>

let $repack := insert document $repackNode into 'tig:///ePharma_MD/RepackageInstructions' 
return 
<ID>{$docid}</ID>
")
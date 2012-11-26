declare general-option 'experimental=true';
 tig:drop-stored-procedure("CDT_checkProductInventory"),  
tig:create-stored-procedure("CDT_checkProductInventory",
"
declare variable $input as node() external;

declare function local:getDt1($input as node()) as node()
{
	let $res := (
		let $k := collection('tig:///CardinalHealth/ProductInventory')/Product
		return 
		
			for $i in $input/PayLoad/Pedigree/Products/Product
			return 
			$k[NDC=$i/NDC]/UnitsInStock = $i/Quantity 
		)
		return 
	<op>{$res}</op>
};

for $t in local:getDt1($input)
return  if (contains(data($t),'false')) then ('false') else ('true')
")
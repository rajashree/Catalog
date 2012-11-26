tig:create-stored-procedure("InsertDocForAB","
declare variable $doc as node()* external;
let $i := tig:insert-document('tig:///ePharma_MD/PedigreeBank',$doc)
return data($doc//PedigreeBankId)

"),

(:to Check whether the NDC exists in the Pedigree Bank or not:)
tig:create-stored-procedure("ndcExists_MD","
 declare variable $ndc as string external;
 
 for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand 
 where upper-case($i/NDC) = upper-case($ndc)
 return true()
"),

(:to Check whether the NDC exists in the Pedigree Bank or not:)
tig:create-stored-procedure("ndcExists_MDNew","
 declare variable $ndc as string external;
 
 for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank
 where upper-case($i/InventoryOnHand/NDC) = upper-case($ndc)
 return data($i/PedigreeBankId)
"),

(:to Check whether the Bin Number exists in the Pedigree Bank or not:)
tig:create-stored-procedure("BinNumberExists_MD","
 declare variable $ndc as string external;
declare variable $binNo as string external;
 
 
for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[ upper-case(NDC) = upper-case($ndc)]
where $i/BinInfo/BinNumber = $binNo
return true()
"),

tig:create-stored-procedure("InsertPedigreeBankDocForMD","
import module namespace util = 'xquery:modules:util';
declare variable $pedrcv as node()* external;
let $bankId := util:create-uuid()
let $i := $pedrcv
return(
<PedigreeBank>
<PedigreeBankId>{$bankId}</PedigreeBankId>
<InventoryOnHand>
<NDC>{$pedrcv//NDC/text()}</NDC>
<ProductInfo>
<drugName>{$pedrcv//DrugLabel/text()}</drugName>
<dosageForm>{$pedrcv//Form/text()}</dosageForm>
<strength>{$pedrcv//Strength/text()}</strength>
<containerSize>{$pedrcv//ContainerSize/text()}</containerSize>
<Manufacturer>{$pedrcv//MFGName/text()}</Manufacturer>
</ProductInfo>
<TotalInventory>{$pedrcv//Quantity/text()}</TotalInventory>
<BinInfo>
<LotInfo>
<LotNumber>{$pedrcv//LotNo/text()}</LotNumber>
<Quantity>{$pedrcv//Quantity/text()}</Quantity>
<InitialQuantity>{$pedrcv//Quantity/text()}</InitialQuantity>
<ExpirationDate>{$pedrcv//ExpMMYY/text()}</ExpirationDate>
<BankTime>{$pedrcv//BankTimeMMDDYYhhmmss/text()}</BankTime>
<LocationID></LocationID>
<SerialNumber></SerialNumber>

<TransactionInfo>
<Address>
<businessName>{$pedrcv//MFGName/text()}</businessName>
<street1>{$pedrcv//MFGAddressLine1/text()}</street1>
<street2>{$pedrcv//MFGAddressLine2/text()}</street2>
<city>{$pedrcv//MFGCity/text()}</city>
<stateOrRegion>{$pedrcv//State/text()}</stateOrRegion>
<postalCode>{$pedrcv//MFGZip/text()}</postalCode>
<country>{$pedrcv//Country/text()}</country>
<licenseNumber>{$pedrcv//MFGDea/text()}</licenseNumber>
<contactName>{$pedrcv//MFGContactName/text()}</contactName>
<telephone>{$pedrcv//MFGContactTelephone/text()}</telephone>
<email>{$pedrcv//MFGContactEMail/text()}</email>

</Address>

<TransactionId>{$pedrcv//PONo/text()}</TransactionId>
<TransactionType>PurchaseOrderNumber</TransactionType>
<TransactionDate>{$pedrcv//PODateMMDDYY/text()}</TransactionDate>
</TransactionInfo>
<ReceivingInfo>
<ReceivedDate>{$pedrcv//RecDateMMDDYY/text()}</ReceivedDate>

</ReceivingInfo>
 
</LotInfo>
<BinNumber>{$pedrcv//BinLocation/text()}</BinNumber>
</BinInfo>
</InventoryOnHand>
</PedigreeBank>
)
")
,

tig:create-stored-procedure("UpDateTotalInventoryforAB_MD", "
declare variable $NDC as string external;
declare variable $value as integer external;
update
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank
where $i//NDC=$NDC
replace value of $i/InventoryOnHand/TotalInventory with $i/InventoryOnHand/TotalInventory + $value
"),


tig:create-stored-procedure("insertBinInfoNodetoPedigreeBank_MD", "
declare variable $NDC as string external;
declare variable $binInfo as node()* external;
update
let $qty := xs:integer($binInfo//Quantity)
let $test := tlsp:UpDateTotalInventoryforAB_MD($NDC,$qty)
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank/InventoryOnHand[NDC=$NDC]
insert
$binInfo after $i/BinInfo[1]"),


tig:create-stored-procedure("insertLotInfoForSameBin_MD", "
declare variable $NDC as string external;
declare variable $binno as string external;
declare variable $lotInfo as node()* external;
update
let $qty := xs:integer($lotInfo//Quantity)
let $test := tlsp:UpDateTotalInventoryforAB_MD($NDC,$qty)
for $i in collection('tig:///ePharma_MD/PedigreeBank')//PedigreeBank/InventoryOnHand[NDC=$NDC]/BinInfo
where $i/BinNumber=$binno 
insert
$lotInfo after $i/LotInfo[1]"),


tig:create-stored-procedure("createBinInfoForAB","

declare variable $pedrcv as node()* external;
let $i := $pedrcv
return(
<BinInfo>
<LotInfo>
<LotNumber>{$pedrcv//LotNo/text()}</LotNumber>
<Quantity>{$pedrcv//Quantity/text()}</Quantity>
<InitialQuantity>{$pedrcv//Quantity/text()}</InitialQuantity>
<ExpirationDate>{$pedrcv//ExpMMYY/text()}</ExpirationDate>
<BankTime>{$pedrcv//BankTimeMMDDYYhhmmss/text()}</BankTime>
<LocationID></LocationID>
<SerialNumber></SerialNumber>

<TransactionInfo>
<Address>
<businessName>{$pedrcv//MFGName/text()}</businessName>
<street1>{$pedrcv//MFGAddressLine1/text()}</street1>
<street2>{$pedrcv//MFGAddressLine2/text()}</street2>
<city>{$pedrcv//MFGcity/text()}</city>
<stateOrRegion>{$pedrcv//State/text()}</stateOrRegion>
<postalCode>{$pedrcv//MFGZip/text()}</postalCode>
<country>{$pedrcv//Country/text()}</country>
<lincenseNumber>{$pedrcv//MFGDea/text()}</lincenseNumber>
</Address>
<TransactionId>{$pedrcv//PONo/text()}</TransactionId>
<TransactionType>PurchaseOrderNumber</TransactionType>
<TransactionDate>{$pedrcv//PODateMMDDYY/text()}</TransactionDate>
</TransactionInfo>
<ReceivingInfo>
<ReceivedDate>{$pedrcv//RecDateMMDDYY/text()}</ReceivedDate>
</ReceivingInfo>
</LotInfo>
<BinNumber>{$pedrcv//BinLocation/text()}</BinNumber>
</BinInfo>

)

"),

tig:create-stored-procedure("createLotInfoForAB","

declare variable $pedrcv as node()* external;
let $i := $pedrcv
return(
<LotInfo>
<LotNumber>{$pedrcv//LotNo/text()}</LotNumber>
<Quantity>{$pedrcv//Quantity/text()}</Quantity>
<InitialQuantity>{$pedrcv//Quantity/text()}</InitialQuantity>
<ExpirationDate>{$pedrcv//ExpMMYY/text()}</ExpirationDate>
<BankTime>{$pedrcv//BankTimeMMDDYYhhmmss/text()}</BankTime>
<LocationID></LocationID>
<SerialNumber></SerialNumber>
<TransactionInfo>
<Address>
<businessName>{$pedrcv//MFGName/text()}</businessName>
<street1>{$pedrcv//MFGAddressLine1/text()}</street1>
<street2>{$pedrcv//MFGAddressLine2/text()}</street2>
<city>{$pedrcv//MFGcity/text()}</city>
<stateOrRegion>{$pedrcv//State/text()}</stateOrRegion>
<postalCode>{$pedrcv//MFGZip/text()}</postalCode>
<country>{$pedrcv//Country/text()}</country>
<lincenseNumber>{$pedrcv//MFGDea/text()}</lincenseNumber>
</Address>
<TransactionId>{$pedrcv//PONo/text()}</TransactionId>
<TransactionType>PurchaseOrderNumber</TransactionType>
<TransactionDate>{$pedrcv//PODateMMDDYY/text()}</TransactionDate>
</TransactionInfo>
<ReceivingInfo>
<ReceivedDate>{$pedrcv//RecDateMMDDYY/text()}</ReceivedDate>
</ReceivingInfo>

</LotInfo>
)

"),
tig:create-stored-procedure("InsertPedigreeBankAudit","
declare variable $pedigreeBankIDs as node()* external;
declare variable $fileName as string external;

tig:insert-document('tig:///ePharma_MD/PedigreeBankAudit',
			<PedigreeBankAudit>
				<fileName>{$fileName}</fileName>
				{$pedigreeBankIDs//PedigreeBankId}
			</PedigreeBankAudit>)
")
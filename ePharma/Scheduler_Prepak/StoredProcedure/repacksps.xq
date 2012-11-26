tig:create-stored-procedure("PEDSHIPRepackManual_PP","
declare variable $shipNode as node() external;
declare variable $signerId as string external;
declare variable $deaNumber as string external;
declare variable $sourceRoutingCode as string external;

for $ship_data in $shipNode
let $result := tlsp:creatingRepackagedPedigree_PrePack($ship_data,$signerId,$deaNumber,$sourceRoutingCode)
return $result
"),

tig:create-stored-procedure("creatingRepackagedPedigree_PrePack", "
import module namespace util = 'xquery:modules:util';
declare variable $xmlnode as node()* external;
declare variable $signerId as string external;
declare variable $deaNum as string external;
declare variable $sourceRoutingCode as string external;

let $PP := (for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner where $i/deaNumber = $deaNum return $i)
let $date := substring-before(fn:current-dateTime() cast as string,'T')
for $xmlString in $xmlnode//root
let $ptpStatus := tlsp:PTPExists_Repack(<output>{$xmlString}</output>)
return 
if($ptpStatus = 'true') then
(
let $envid := util:create-uuid()
let $dateTime :=substring-before(fn:current-dateTime() cast as string,'.')
let $signerInfo := for $y in collection('tig:///EAGRFID/SysUsers')/User
where $y/UserID = $signerId
return <signerInfo><name>{concat(data($y/FirstName),' ',data($y/LastName))}</name><title>{data($y/UserRole)}</title><telephone>{data($y/Phone)}</telephone>
<email>{data($y/Email)}</email>
</signerInfo>
let $values := tlsp:TPCustomization($xmlString//repack[1]/pedship[1]/CustomerDea,'manualusecase')
 let $pedigrees :=<root>{(for $c in $xmlString//PE/repack 
			let $repackNode := (for $i in $c/pedship[PickType = 'RepackProd'] return $i)
     let $rec := (<recipientInfo><businessAddress><businessName>{data($repackNode/CustomerName)}</businessName>
		 <street1>{data($repackNode/CustomerAddressStreet1)}</street1><street2>{data($repackNode/CustomerAddressStreet2)}</street2><city>{data($repackNode/CustomerCity)}</city><stateOrRegion>{data($repackNode/CustomerState)}</stateOrRegion>
		<postalCode>{data($repackNode/CustomerPostalCode)}</postalCode><country>{data($repackNode/CustomerCountry)}</country></businessAddress>
                   <shippingAddress>
                 <businessName>{data($repackNode/CustomerName)}</businessName>
		 <street1>{data($repackNode/ShipToCustomerAddressLine1)}</street1><street2>{data($repackNode/ShipToCustomerAddressLine2)}</street2><city>{data($repackNode/ShipToCustomerCity)}</city><stateOrRegion>{data($repackNode/ShipToState)}</stateOrRegion>
		<postalCode>{data($repackNode/ShipToCustomerZip)}</postalCode><country>{data($repackNode/ShipToCountry)}</country>
                </shippingAddress>
		<licenseNumber>{data($repackNode/CustomerDea)}</licenseNumber>
   </recipientInfo>) 
let $sender := (
	<senderInfo><businessAddress><businessName>{data($PP/name)}</businessName>
	<street1>{data($PP/businessAddress/line1)}</street1><street2>{data($PP/businessAddress/line2)}</street2><city>{data($PP/businessAddress/city)}</city><stateOrRegion>{data($PP/businessAddress/state)}</stateOrRegion>
	<postalCode>{data($PP/businessAddress/zip)}</postalCode><country>{data($PP/businessAddress/country)}</country></businessAddress>
	<shippingAddress><businessName>{data($PP/name)}</businessName>
	<street1>{data($PP/shippingAddress/line1)}</street1><street2>{data($PP/shippingAddress/line2)}</street2><city>{data($PP/shippingAddress/city)}</city><stateOrRegion>{data($PP/shippingAddress/state)}</stateOrRegion>
	<postalCode>{data($PP/shippingAddress/zip)}</postalCode><country>{data($PP/shippingAddress/country)}</country></shippingAddress>
	<licenseNumber>{data($PP/deaNumber)}</licenseNumber>
	<contactInfo>
		<name>{data($PP/contact)}</name>
		<telephone>{data($PP/phone)}</telephone>
		<email>{data($PP/email)}</email>
	</contactInfo>
</senderInfo>)	
  let $docid := util:create-uuid()
  let $shipid := util:create-uuid()
let $prev :=<root>{(   for $d in $c/pedship 
   return
 if($d/PickType = 'PrevProd') then 
  (
                  
                           let $receivedPedigree := (for $j in collection('tig:///ePharma/ShippedPedigree')/*:pedigree
                           where $j/*:receivedPedigree/*:documentInfo/*:serialNumber = $d/PedigreeId
                           return $j)
                            let $initialPed := (for $j in collection('tig:///ePharma/PaperPedigree')/*:initialPedigree
                            where $j/*:documentInfo/*:serialNumber =$d/PedigreeId
                            return $j)
                          let $mfgrInfo := (<senderInfo><businessAddress><businessName>{data($d/MFGName)}</businessName>
		                     <street1>{data($d/MfgStreet1)}</street1><street2>{data($d/MfgStreet2)}</street2><city>{data($d/MfgCity)}</city><stateOrRegion>{data($d/MfgState)}</stateOrRegion>
		<postalCode>{data($d/MfgPostalCode)}</postalCode><country>{data($d/MfgCountry)}</country></businessAddress>
		<licenseNumber>{data($d/MfgDEANumber)}</licenseNumber>
		<contactInfo><name>{data($d/MfgContactName)}</name><title>{data($d/MfgContactTitle)}</title>
		<email>{data($d/MfgContactEMail)}</email>
		</contactInfo>
		</senderInfo> )
                            let $recipientInfo := (
	   <recipientInfo><businessAddress><businessName>{data($PP/name)}</businessName>
	           <street1>{data($PP/businessAddress/line1)}</street1><street2>{data($PP/businessAddress/line2)}</street2><city>{data($PP/businessAddress/city)}</city><stateOrRegion>{data($PP/businessAddress/state)}</stateOrRegion>
	        <postalCode>{data($PP/businessAddress/zip)}</postalCode><country>{data($PP/businessAddress/country)}</country></businessAddress>
	<shippingAddress><businessName>{data($PP/name)}</businessName>
	<street1>{data($PP/shippingAddress/line1)}</street1><street2>{data($PP/shippingAddress/line2)}</street2><city>{data($PP/shippingAddress/city)}</city><stateOrRegion>{data($PP/shippingAddress/state)}</stateOrRegion>
	<postalCode>{data($PP/shippingAddress/zip)}</postalCode><country>{data($PP/shippingAddress/country)}</country></shippingAddress>
	<licenseNumber>{data($PP/deaNumber)}</licenseNumber>
	<contactInfo>
		<name>{data($PP/contact)}</name>
		<telephone>{data($PP/phone)}</telephone>
		<email>{data($PP/email)}</email>
	</contactInfo>
       </recipientInfo>
      )
   return
                         
                             if(exists($receivedPedigree)) then
                                   <res> <previousProducts><previousProductInfo><manufacturer>{data($receivedPedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:initialPedigree/*:productInfo/*:manufacturer)}</manufacturer><productCode type='NDC'>{data($receivedPedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:initialPedigree/*:productInfo/*:productCode)}</productCode>
		 	 	     </previousProductInfo>
                                        {$receivedPedigree/receivedPedigree/receivingInfo/itemInfo}
                                          <contactInfo>
 <name>{data($receivedPedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:name)}</name><title>{data($receivedPedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:title)}</title><telephone>{data($receivedPedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:telephone)}</telephone><email>{data($receivedPedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:email)}</email><url>{data($receivedPedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:url)}</url>
                      </contactInfo>
                                             </previousProducts>
          		     	         <previousPedigrees>	{$receivedPedigree}
  		                    </previousPedigrees> </res>
                        
                             else    if(exists($initialPed)) then
                                            <res> <previousProducts><previousProductInfo>
                                             {$initialPed/productInfo/manufacturer}
                                             {$initialPed/productInfo/productCode}
                   	                   </previousProductInfo> 
                                                {$initialPed/itemInfo}
                                               {$initialPed/transactionInfo/senderInfo/contactInfo}
                                                      </previousProducts>
          		                    	<previousPedigrees><initialPedigree>	
                                                        {$initialPed/documentInfo/serialNumber}
                           		                {$initialPed/productInfo}      	                          
   {$initialPed/itemInfo}

          		                    	                     {$initialPed/transactionInfo}</initialPedigree>
  	             	                      </previousPedigrees> </res>
                          
                                else 
                                           <res> <previousProducts><previousProductInfo>
                                              {$initialPed/productInfo/manufacturer}
                                               {$initialPed/productInfo/productCode}
                                            <manufacturer>{data($d/MFGName)}</manufacturer><productCode type='NDC'>{data($d/NDC)}</productCode>
                                           
               	                            </previousProductInfo> 
                                              <itemInfo>
		                             <lot>{data($d/LotNumberInitial)}</lot><expirationDate>{data($d/ExpiryDate)}</expirationDate><quantity>{data($d/Quantity)}
 	                                           </quantity> </itemInfo>
                                          {$recipientInfo/contactInfo}
                                                   </previousProducts>
          		                  	<previousPedigrees>	
                                                    <initialPedigree>
                                               <productInfo>
		                              <drugName>{data($d/LegendDrugName)}</drugName><manufacturer>{data($d/MFGName)}</manufacturer><productCode type='NDC'>{data($d/NDC)}</productCode>
		                          <dosageForm>{data($d/Dosage)}</dosageForm><strength>{data($d/Strength)}</strength><containerSize>{data($d/ContainerSize)}</containerSize>
                                       </productInfo>
                                     <itemInfo>
		                           <lot>{data($d/LotNumberInitial)}</lot><expirationDate>{data($d/ExpiryDate)}</expirationDate><quantity>{data($d/Quantity)}
 	                                </quantity> </itemInfo>
                                                 <transactionInfo>
                                        {$mfgrInfo}
                                          {$recipientInfo}     
                                          <transactionIdentifier>
			                <identifier>{data($d/Invoice)}</identifier><identifierType>InvoiceNumber</identifierType>
	                                   </transactionIdentifier>
		                              <transactionType>Sale</transactionType>
	   	                          <transactionDate>{data($d/PODateYYYYMMDD)}</transactionDate>
                                             </transactionInfo>
                                      </initialPedigree>
  		               </previousPedigrees> </res>
                             )  else ()
              )}</root> 
  return
  
                
      <pedigree>
         <shippedPedigree id='{$shipid}'>
	  <documentInfo>
	  <serialNumber>{$docid}</serialNumber>
	  <version>20060916</version>
	</documentInfo>
    
<repackagedPedigree>
       {$prev//previousProducts}
       {$prev//previousPedigrees}
<productInfo>
		<drugName>{data($repackNode/LegendDrugName)}</drugName><manufacturer>{data($repackNode/MFGName)}</manufacturer><productCode type='NDC'>{data($repackNode/NDC)}</productCode>
		<dosageForm>{data($repackNode/Dosage)}</dosageForm><strength>{data($repackNode/Strength)}</strength><containerSize>{data($repackNode/ContainerSize)}</containerSize>
            </productInfo>
         <itemInfo>
		<lot>{data($repackNode/LotNumberInitial)}</lot><expirationDate>{data($repackNode/ExpiryDate)}</expirationDate><quantity>{data($repackNode/Quantity)}
	 </quantity> </itemInfo>
</repackagedPedigree>

 <transactionInfo>
            {$sender}
            {$rec}

	  	<transactionIdentifier>
			<identifier>{data($repackNode/Invoice)}</identifier><identifierType>InvoiceNumber</identifierType>
	         </transactionIdentifier>
		<transactionType>Sale</transactionType>
	   	<transactionDate>{data($repackNode/PODateYYYYMMDD)}</transactionDate>
	    </transactionInfo>

 
<signatureInfo>
	  {$signerInfo}
	  <signatureDate>{$dateTime}</signatureDate>
	  <signatureMeaning>Certified</signatureMeaning>
	</signatureInfo>
</shippedPedigree>
<Signature>
	<SignedInfo>
		<CanonicalizationMethod Algorithm=''/>
		<SignatureMethod Algorithm=''/>
		<Reference>
			<DigestMethod Algorithm=''/>
			<DigestValue/>
		</Reference>
	</SignedInfo>
	<SignatureValue/>
</Signature>
</pedigree>)}</root>
let $envNode :=(
 <pedigreeEnvelope><version>20060916</version>
 <serialNumber>{$envid}</serialNumber>
 <date>{$date}</date>
 <sourceRoutingCode>{$sourceRoutingCode}</sourceRoutingCode>
 <destinationRoutingCode>{data($PP/destinationRoutingCode)}</destinationRoutingCode>
  
	
<container><containerCode>{if($values/containerCode = 'PONumber') then distinct-values(data($xmlString//repack[1]/pedship[1]/Invoice)) 
		else if($values/shipmentHandle = 'InvoiceNumber') then distinct-values(data($xmlString//repack[1]/pedship[1]/Invoice))
		else 'NA'}</containerCode>
		<shipmentHandle>{if($values/shipmentHandle = 'PONumber') then distinct-values(data($xmlString//repack[1]/pedship[1]/Invoice)) 
		else if($values/shipmentHandle = 'InvoiceNumber') then distinct-values(data($xmlString//repack[1]/pedship[1]/Invoice))
		else 'NA'}</shipmentHandle>
           { for $i in (1 to count($pedigrees/pedigree)) return
           <pedigreeHandle>
                            {$pedigrees/pedigree[$i]/shippedPedigree/documentInfo/serialNumber}
            </pedigreeHandle> 
}
		</container>

{$pedigrees/pedigree}</pedigreeEnvelope>)
return $envNode
)
else 'NOPTPExists'

"),
  
tig:create-stored-procedure("TPCustomization","
declare variable $deaNumber as string external;
declare variable $usecase as string external;
let $config := <root>{(for $i in collection('tig:///ePharma/PedigreeTradingPartner')/PedigreeTradingPartner
where $i/deaNumber = $deaNumber
return
if( $usecase = 'manualusecase') then $i/configurationElements/manualusecase/config 
else if ( $usecase = 'dropshipusecase')then $i/configurationElements/dropshipusecase/config 
else if ( $usecase = 'automatedusecase')then $i/configurationElements/automatedusecase/config 
else ()
)}</root>

let $ccode := for $i in $config/config where $i/element = 'containerCode' return data($i/value)
let $shipment := for $i in $config/config where $i/element = 'shipmentHandle' return data($i/value)
return 
<result>
{
<containerCode>{$ccode}</containerCode>,
<shipmentHandle>{$shipment}</shipmentHandle>
}
</result>
"),
tig:create-stored-procedure("PTPExists_Repack","
 declare variable $xmlNode as node()* external;
 for $shipNode in $xmlNode/root/PE
 let $deaNumber := distinct-values($shipNode/repack[1]/pedship[1]/CustomerDea)

return if(exists(for $i in collection('tig:///ePharma/PedigreeTradingPartner')/PedigreeTradingPartner
 	 where upper-case($i/deaNumber) = upper-case($deaNumber)
	 return $i)) then
	 'true'
	else 'false'
")

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>
<%@ page import="java.util.List"%>
 

<%
QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
String tp_company_nm = (String)request.getParameter("tp_company_nm");
if(tp_company_nm==null)tp_company_nm="";
    String pagenm =  request.getParameter("pagenm");
    if(pagenm==null)pagenm="pedigree";
     String tpGenId = (String)session.getAttribute("tpGenId");
    String imageLocation = CommonUtil.returnEmptyIfNull((String) request.getAttribute("ImageFileLocation"));
    System.out.println("imageLocation"+imageLocation);
    
    
 
%>
 
  
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<head>
<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" type="text/css" rel="stylesheet" >
<link href="/ePharma/assets/images/calendar-win2k-1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="gen_validatorv2.js" type="text/javascript"></script>
<script language="JavaScript" src="calendar.js"></script>
<script language="JavaScript" src="calendar-en.js"></script>
<script language="JavaScript" src="calendar-setup.js"></script>
<script language="JavaScript" src="callCalendar.js"></script>

 <script language = "Javascript">
/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "-";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strYear=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strDay=dtStr.substring(pos2+1)
	 
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert("The date format should be : yyyy-mm-dd")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date")
		return false
	}
return true
}

 

</script>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}



 
function validate_required(field,alerttxt)
{
with(field)
{
if(field==null||value=="")
  {alert(alerttxt);return false}
else {return true}
}
}

function validate_form(thisform)
{
 
  
with (document.forms[0])
{
 
if(!validateAddLotInfo())
{
return false;
}	
	
if (validate_required(document.forms[0].name,"DrugName must be filled out!")==false)
  {document.forms[0].name.focus();return false}

if (validate_required(document.forms[0].manufacturer,"Manufacturer must be filled out!")==false)
  {document.forms[0].manufacturer.focus();return false}

if (validate_required(document.forms[0].productCode,"ProductCode   must be filled out!")==false)
  {document.forms[0].productCode.focus();return false}

if (validate_required(document.forms[0].strength,"Strength must be filled out!")==false)
  {document.forms[0].strength.focus();return false}

if (validate_required(document.forms[0].containerSize,"ContainerSize must be filled out!")==false)
  {document.forms[0].containerSize.focus();return false}

 

if (validate_required(document.forms[0].contactName,"ContactName must be filled out!")==false)
  {document.forms[0].contactName.focus();return false}

if (validate_required(document.forms[0].dosageForm ,"DosageForm must be filled out!")==false)
  {document.forms[0].dosageForm.focus();return false}



if (validate_required(document.forms[0].transactionId ,"TransactionId must be filled out!")==false)
  {document.forms[0].transactionId.focus();return false}

if (validate_required(document.forms[0].transactionDate,"TransactionDate must be filled out!")==false)
  {document.forms[0].transactionDate.focus();return false}

  
if (validate_required(document.forms[0].tpName,"TradingPartner name must be filled out!")==false)
  {document.forms[0].tpName.focus();return false}

if (validate_required(document.forms[0].address1,"Address1   must be filled out!")==false)
  {document.forms[0].address1.focus();return false}


if (validate_required(document.forms[0].address2,"Address2   must be filled out!")==false)
  {document.forms[0].address2.focus();return false}


if (validate_required(document.forms[0].city,"City   must be filled out!")==false)
  {document.forms[0].city.focus();return false}


if (validate_required(document.forms[0].country,"Country   must be filled out!")==false)
  {document.forms[0].country.focus();return false}


if (validate_required(document.forms[0].state,"State   must be filled out!")==false)
  {document.forms[0].state.focus();return false}


if (validate_required(document.forms[0].zipCode,"ZipCode  must be filled out!")==false)
  {document.forms[0].zipCode.focus();return false}


}


var t=document.forms[0].check.checked;
 
 
if (t==true)
  {
  
if (document.forms[0].attachment.value==null ||document.forms[0].attachment.value=="" )
  {
  alert('Please Attach the Paper Pedigree!!');
  document.forms[0].attachment.focus();return false
  }
  }
  
  
var dt1=document.forms[0].transactionDate;
	if (isDate(dt1.value)==false){
		dt1.focus()
		return false
	}

 
 
 
 
}
 
 
function validateAddLotInfo(){

 




var lot=document.getElementsByName("lotNumber")

for(var k=0;k<lot.length;k++)
	{
  
  var lotno=lot[k].value;
 if(lotno==null || lot[k].value=="")
{

 alert('Lot Number must be filled out!!!')
 lot[k].focus();
 return false;
 
}
 
}


var iserial=document.getElementsByName("itemSerialNumber")

for(var m=0;m<iserial.length;m++)
	{
  
  var isn=iserial[m].value;
 if(isn==null || iserial[m].value=="")
{

 alert('Item Serial Number must be filled out!!!')
 iserial[m].focus();
 return false;
 
}
 
}
 
var s1=document.getElementsByName("quantity")

for(var i=0;i<s1.length;i++)
	{
  
  
 var s=isInteger(s1[i].value);
 
if(s==false|| s1[i].value=="")
{

 alert('Please enter Quantity as Integer!!')
 s1[i].focus();
 return false;
 
}
 
}
   
 
 
  var dt1=document.getElementsByName("expirationDate")
 
	for(var j=0;j<dt1.length;j++)
	{
	
	
	if (isDate(dt1[j].value)==false){
		dt1[j].focus()
	 
	 return false;
	}
	}
	
	 return true;
	
	 
 
 }
 
 
 
  function addLotInfo()
 {
	 
	
	var lotName = document.createElement('SPAN')
	lotName.innerText = 'Lot Number'
		
	var lotText = document.createElement('INPUT')
		lotText.type = 'TEXT'
		lotText.id = lotText.name = 'lotNumber'

	var qtyName = document.createElement('SPAN')
	qtyName.innerText = 'Quantity'
		
	var qtyText = document.createElement('INPUT')
		qtyText.type = 'TEXT'
		qtyText.id = qtyText.name = 'quantity'
	
    var itemSerialNumberName = document.createElement('SPAN')
	itemSerialNumberName.innerText = 'Item Serial Number'
		
	var itemSerialNumber = document.createElement('INPUT')
		itemSerialNumber.type = 'TEXT'
		itemSerialNumber.id = itemSerialNumber.name = 'itemSerialNumber'
    
	  var  expirationDateName = document.createElement('SPAN')
	 expirationDateName.innerText = 'Expiration Date'
		
	var expirationDate = document.createElement('INPUT')
		expirationDate.type = 'TEXT'
		expirationDate.id = expirationDate.name = 'expirationDate'
        

	var table = document.getElementById('dynamicTable');

	var newTD1,newTD2,newTD3,newTD4,newTD5,newTD6,newTD7,newTD8;
	var newTR = document.createElement('TR');
	var newTR1 = document.createElement('TR');
	newTR.className = 'tableRow_Off'
	
	newTR.appendChild( newTD1 = document.createElement('TD') );
	newTR.appendChild( newTD2 = document.createElement('TD') );
	newTR.appendChild( newTD3 = document.createElement('TD') );
	newTR.appendChild( newTD4 = document.createElement('TD') );
	
	newTD1.appendChild ( lotName );
	newTD2.appendChild ( lotText );
	newTD3.appendChild( qtyName );
	newTD4.appendChild( qtyText );
	
	

	
	newTR1.className = 'tableRow_Off'
	newTR1.appendChild( newTD5 = document.createElement('TD') );
	newTR1.appendChild( newTD6 = document.createElement('TD') );
	newTR1.appendChild( newTD7 = document.createElement('TD') );
	newTR1.appendChild( newTD8 = document.createElement('TD') );
	
	
	newTD1.className = newTD2.className = newTD3.className = newTD4.className = "tableRow_Off";
	
	
	newTD5.appendChild ( itemSerialNumberName );
	newTD6.appendChild ( itemSerialNumber );
	newTD7.appendChild(  expirationDateName );
	newTD8.appendChild( expirationDate  );
	
	newTD5.className = newTD6.className = newTD7.className = newTD8.className = "tableRow_Off";
	table.children[0].appendChild(newTR);
	table.children[0].appendChild(newTR1);

 }

 

//-->
</script>
</head>
<body >



 <% System.out.println("Initial Pedigree Frame1");
 String receivedFax=(String)session.getAttribute("operationType");
 System.out.println("rvc"+receivedFax);
 
 
 %>

<html:form  action="dist/epedigree/InitialPedigree.do" target="frame2" enctype="multipart/form-data">

  <html:hidden property="documentID"/>
  <table border="0" cellspacing="0" cellpadding="0" width="100%" >
         
         
        <tr> 
          <td width="100%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
          
         </tr> 		
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
              
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%" id='dynamicTable' >		
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Pedigree Details</td>
                </tr>		
					<TR class="tableRow_Off">
						<TD>* DrugName:</TD>
						<TD><html:text property="name"  /></TD>
						<TD>* Manufacturer :</TD>
						<TD>
						<html:text property="manufacturer"     />
						 </TD>
						 
					</TR>
					<TR class="tableRow_Off">
						
						<TD>* Product Code:</TD>
						<TD>
						<html:text property="productCode"    />
					
						 </TD>
						<TD>* Strength :</TD>
						<TD >
						<html:text property="strength"     />
					
						 </TD>
					</TR>
					<TR class="tableRow_Off">
						
						<TD>* Container Size:</td>
						<TD>
						<html:text property="containerSize"     />
					
						 
				 
						<TD>* Contact Name:</td>
						<TD>
							<html:text property="contactName"     />
						 </TD>
					</TR>
					 
					 <TR class="tableRow_Off">
						
						<TD>* Dosage Form :</td>
						<TD>
						<html:text property="dosageForm"   />
						  </TD>
						 <TD>* Transaction Id :</td>
						<TD>
						<html:text property="transactionId"    />
					  </TD>
					</TR>
					 
					  <TR class="tableRow_Off">
						
						
						<TD>* Transaction Date:</td>
						<TD>
						
						<html:text property="transactionDate" size="20" /><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('transactionDate', '%Y-%m-%d', '24', true);">
						
						 
						 </TD>
						 
						 
						 <TD>* Transaction Type :</td>
						<TD><html:select  property="transactionType" value="<bean:write property='transactionType' name='InitialPedigreeForm'/>">
																 
																 <logic:notPresent property="transactionType" name="InitialPedigreeForm">
																    
																   <option >Select</option>
																   <option >InvoiceNumber</option>
																    <option >PurchaseOrderNumber</option>
																  </logic:notPresent> 
																  <logic:present property="transactionType" name="InitialPedigreeForm">
																  
																  <option><bean:write property='transactionType' name='InitialPedigreeForm'/></option>
																  
																   <logic:notEqual property="transactionType" value="InvoiceNumber" name="InitialPedigreeForm">
																    
																  
																   <option >InvoiceNumber</option>
																   
																    
																  </logic:notEqual>
																  <logic:notEqual property="transactionType" value="PurchaseOrderNumber" name="InitialPedigreeForm">
																    
																   <option >PurchaseOrderNumber</option>
																    
																  </logic:notEqual>
																 
																</logic:present>
																  
														 
															</html:select></TD>
					</TR>
				 
				 
					
					  <TR class="tableRow_Off">
				 	<TD colspan=2>
						 Paper Pedigree Attachment &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   
				 	 
						<html:file property="attachment" onclick="displayImage();"/>
						  </TD>
						  		 
						  		 <TD>* Create Initial Pedigree :</td> <td><input type='checkbox' name='check' value="checked" />
					</TR>
					
					
					
					<TR class="tableRow_Off">
					 <TD>* Lot Number:</td>
						<TD>
						<html:text property="lotNumber"    />
					
						 </TD>
					 
					
						
						<TD>* Quantity :</td>
						<TD>
						<html:text property="quantity"        />
					 
					<TR class="tableRow_Off">
					 <TD>*Item Serial Number:</td>
						<TD>
						<html:text property="itemSerialNumber"     />
					
						 </TD>
					 
					
						<TD>* Expiration date:</td>
						<TD>
						<html:text property="expirationDate"    />
						
						 </TD>
						 </TR>
					 
					<%
			String lotNos[]=(String[])session.getAttribute("lotNos");
		    String quantity[]=(String[])session.getAttribute("quantity");
		    String itemSerialNumber[]=(String[])session.getAttribute("itemSerialNumber");
		    String expirationDate[]=(String[])session.getAttribute("expirationDate");
				for(int k=1 ;lotNos!=null && k<lotNos.length ;k++){
					
					%>
					<TR class="tableRow_Off">
					 <TD>* Lot Number:</td>
						<TD>
						<html:text property="lotNumber"  value="<%=lotNos[k]%>"  />
					
						 </TD>
					 
					
						
						<TD>* Quantity :</td>
						<TD>
						<html:text property="quantity"  value="<%=quantity[k]%>"     />
					 
					<TR class="tableRow_Off">
					 <TD>*Item Serial Number:</td>
						<TD>
						<html:text property="itemSerialNumber"  value="<%=itemSerialNumber[k] %>"  />
					
						 </TD>
					 
					
						<TD>* Expiration date:</td>
						<TD>
						<html:text property="expirationDate"  value="<%=expirationDate[k]%>" />
						
						 </TD>
					 
						<%
						}
						%>  
						  <TR class="tableRow_Off">
				 			<TD ><input type="button" name="Add LotInfo" value="AddLotInfo" onClick="addLotInfo();" /></TD>
						   <TD colspan=4>&nbsp;</TD>		 
					  </TR>
			</TABLE>
 

        


    <TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Trading Partner </td>
                </tr>		
					<TR class="tableRow_Off">
						<TD>*  Name:</TD>
						<TD>
						<html:text property="tpName"   />
						 </TD>
				     <TD>* Address1 :</TD>
						<TD>
						<html:text property="address1"    />
						 </TD>
					</TR>
					<TR class="tableRow_Off">
					 
				     <TD>* Address2 :</TD>
						<TD>
						<html:text property="address2"   />
						 </TD>
						<TD>* City :</TD>
						<TD >
						<html:text property="city"     />
						 </TD>
					</TR>
					<TR class="tableRow_Off">
						
						<TD>* State:</td>
						<TD>
						<html:text property="state"   />
						  </TD>
						<TD>* Zip Code:</td>
						<TD>
						<html:text property="zipCode"    />
						 </TD>
					</TR>
					<TR class="tableRow_Off">
						
						<TD>* Country :</td>
						<TD>
						<html:text property="country"   />
						  </TD>
						
						<TD colspan=2>&nbsp;</td> 
					</TR>
		 				
		 				
		 				<TR class="tableRow_Off">
						
						<TD colspan=2>* fields are mandatory</td>
						 
						
						<TD colspan=2>&nbsp;</td> 
					</TR>
				</TABLE>
				 
				<Center>  
           
          
										<html:hidden property="pagenm" value="<%=pagenm%>" />
										<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />
           
           <logic:notPresent property="documentID" name="InitialPedigreeForm">
            <html:hidden property="operationType" value="Save" />
                        <input type="hidden" name="receivedFax" value="<%=receivedFax%>" />
           <input type="submit" name="save"   value="Submit" onclick="return validate_form(this);" />
           </logic:notPresent>
           
           <%
           
          String docId=(String)session.getAttribute("DocId");
System.out.println("Frame1"+docId);
           %>
           
           <logic:present property="documentID" name="InitialPedigreeForm">
              <input type="hidden" name="receivedFax" value="<%=receivedFax%>" />
            <html:hidden property="operationType" value="Update" />
          <input type="submit" name="save"   value="Update" onclick="return validate_form(this);" />
           </logic:present>
            
           </Center>
             
            
         </td>
        </tr>
      </table> 
      </td>
        </tr>
       
      </table> 
     
 
		
	 

</html:form>

  
 <script language="JavaScript" type="text/JavaScript">
 window.refresh;

</script>

</body>


</html:html>
 

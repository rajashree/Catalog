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
String isUpdate=(String)request.getAttribute("updateSuccess");
if(isUpdate==null)isUpdate="";
System.out.println("isUpdate"+isUpdate);
String operationType=(String)request.getAttribute("operationType");
System.out.println("operationType"+operationType);
%>


<%
QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
String tp_company_nm = (String)request.getParameter("tp_company_nm");
if(tp_company_nm==null)tp_company_nm="";
    String pagenm =  request.getParameter("pagenm");
    if(pagenm==null)pagenm="pedigree";
     String tpGenId = (String)session.getAttribute("tpGenId");
    String imageLocation = CommonUtil.returnEmptyIfNull((String) request.getAttribute("ImageFileLocation"));
    System.out.println("imageLocation"+imageLocation);
    String sessionID = (String)session.getAttribute("sessionID");
    
%>

   <%@include file='topMenu.jsp'%>
<html:html>
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
var dtCh1= "/";
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


function isExpirationDate(dtStr1){
	
	var pos1=dtStr1.indexOf(dtCh1)
	
	var strMonth = dtStr1.substring(0,pos1)
	var strYear = dtStr1.substring(pos1+1)

	strYr1=strYear
	strMonth1=strMonth
		
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth1=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr1.charAt(0)=="0" && strYr1.length>1) strYr1=strYr1.substring(1)
	}
	month = parseInt(strMonth1)
	year = parseInt(strYr1)

	if (pos1==-1){
		alert("The date format should be : MM/YY")
		return false
	}
	if (strMonth.length<2 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	
	if (strYear.length != 2 || year==0 || year<0 || year>99){
		alert("Please enter a valid 2 digit year between 00 and 99")
		return false
	}
	
return true
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


function tpDetails(){

	var frm= document.forms[0];
	document.forms[0].operationType.value = "TPDetails"
	frm.action = "InitialPedigree.do"
	frm.submit();
}

function lookupNDC(){
	
	if (validate_required(document.forms[0].productCode,"Enter ProductCode")==false)
  	{document.forms[0].productCode.focus();return false}
  	 
   	if(document.forms[0].productCode.value.length <9) {
  	alert('ProductCode must contain 9 or more digits');
  	return false;
  	}
  	
  	var frm= document.forms[0];
	document.forms[0].operationType.value = "lookupNDC"
	
	frm.action = "InitialPedigree.do"
	frm.submit();

	  
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


function validateForAttachement(field)
{ 
with(field)
{
if(field==null||value=="")
  {
  	
  	if(confirm("No Paper Pedigrees Attached. Continue?")) {
		return true;
	}
  	return false
  }
  else 
  {
  	return true
  }
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
	
if(validateForAttachement(document.forms[0].attachment)==false)
  {document.forms[0].attachment.focus();return false}
  
if (validate_required(document.forms[0].name,"DrugName must be filled out!")==false)
  {document.forms[0].name.focus();return false}

if (validate_required(document.forms[0].productCode,"ProductCode   must be filled out!")==false)
  {document.forms[0].productCode.focus();return false}

if (validate_required(document.forms[0].strength,"Strength must be filled out!")==false)
  {document.forms[0].strength.focus();return false}
  
if (validate_required(document.forms[0].manufacturer,"Manufacturer must be filled out!")==false)
  {document.forms[0].manufacturer.focus();return false}

if (validate_required(document.forms[0].containerSize,"ContainerSize must be filled out!")==false)
  {document.forms[0].containerSize.focus();return false}
 
if (validate_required(document.forms[0].contactName,"ContactName must be filled out!")==false)
  {document.forms[0].contactName.focus();return false}

if (validate_required(document.forms[0].dosageForm ,"DosageForm must be filled out!")==false)
  {document.forms[0].dosageForm.focus();return false}


if (validate_required(document.forms[0].transactionId ,"Document Number must be filled out!")==false)
  {document.forms[0].transactionId.focus();return false}

if (validate_required(document.forms[0].transactionDate,"Document Date must be filled out!")==false)
  {document.forms[0].transactionDate.focus();return false}

  
if (validate_required(document.forms[0].tpName,"TradingPartner name must be filled out!")==false)
  {document.forms[0].tpName.focus();return false}

if (validate_required(document.forms[0].address1,"Address1   must be filled out!")==false)
  {document.forms[0].address1.focus();return false}


if (validate_required(document.forms[0].city,"City   must be filled out!")==false)
  {document.forms[0].city.focus();return false}


if (validate_required(document.forms[0].country,"Country   must be filled out!")==false)
  {document.forms[0].country.focus();return false}


if (validate_required(document.forms[0].state,"State   must be filled out!")==false)
  {document.forms[0].state.focus();return false}


if (validate_required(document.forms[0].zipCode,"ZipCode  must be filled out!")==false)
  {document.forms[0].zipCode.focus();return false}
  
  
  
  if (validate_required(document.forms[0].contactPhone,"Contact Phone  must be filled out!")==false)
  {document.forms[0].contactPhone.focus();return false}
  
   

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
	
	if(dt1[j].value=="" || dt1[j].value==null){
	}else
	if (isExpirationDate(dt1[j].value)==false){
		dt1[j].focus()
	 
	 return false;
	}
	}
	
	 return true;
	
	 
 
 }
 
 
 
    function addLotInfo()
 {
	 
	 var chkbox = document.createElement('INPUT'); 
        chkbox.type = 'checkbox'
        chkbox.id = chkbox.name = 'check'
     
	
	var qtyName = document.createElement('SPAN')
	qtyName.innerText = '* Shipped Quantity :'
    	
	var qtyText = document.createElement('INPUT')
		qtyText.type = 'TEXT'
		qtyText.id = qtyText.name = 'quantity'
		qtyText.tabIndex=20	
		

	var lotName = document.createElement('SPAN')
	lotName.innerText = '* Lot Number :'
		
		
	var lotText = document.createElement('INPUT')
		lotText.type = 'TEXT'
		lotText.id = lotText.name = 'lotNumber'
	    lotText.tabIndex=22
	    
	
   
         
	
      var  expirationDateName = document.createElement('SPAN')
	 expirationDateName.innerText = 'Expiration Date(MM/YY) :'
	
		

	var table = document.getElementById('dynamicTable');

	var newTD,newTD1,newTD2,newTD3,newTD4,newTD5,newTD6,newTD7,newTD8,newTD9;
	var newTR = document.createElement('TR');
	var newTR1 = document.createElement('TR');
	newTR.className = 'tableRow_Off'
	
	newTR.appendChild( newTD = document.createElement('TD') );
	newTR.appendChild( newTD1 = document.createElement('TD') );
	newTR.appendChild( newTD2 = document.createElement('TD') );
	newTR.appendChild( newTD3 = document.createElement('TD') );
	newTR.appendChild( newTD4 = document.createElement('TD') );
	

	newTD.appendChild( chkbox );
	newTD1.appendChild( qtyName );
	newTD2.appendChild( qtyText );
	newTD3.appendChild ( lotName );
	newTD4.appendChild ( lotText );
	
	//newTR1.className = 'tableRow_Off'
	//newTR1.appendChild( newTD5 = document.createElement('TD') );
	//newTR1.appendChild( newTD6 = document.createElement('TD') );
	//newTR1.appendChild( newTD7 = document.createElement('TD') );
	//newTR1.appendChild( newTD8 = document.createElement('TD') );
	//newTR1.appendChild( newTD9 = document.createElement('TD') );
	
	newTD.className = newTD1.className = newTD2.className = newTD3.className = newTD4.className = "tableRow_Off";
	
	
		
	//newTD5.className = newTD6.className = newTD7.className = newTD8.className = newTD9.className= "tableRow_Off";
	
	table.children[0].appendChild(newTR);
	//table.children[0].appendChild(newTR1);
	qtyText.focus();
   	
    
    
    
	if(document.getElementById('saveDataButton'))
	document.getElementById('saveDataButton').tabIndex=23;
   
	

 }
 
 function removeLotInfo()
 {
	alert("HI") 
	var allchecks = document.getElementsByName('check');
	var remtable = document.getElementById('dynamicTable');
	//alert(remtable.rows.length);
	
	var checkSel = false;
    //alert(allchecks.length);
    var length = 0;
    for(k =0;k<allchecks.length;k++){
    	 if( allchecks[k].checked ){
    	 	length = length + 1;
    	 }
    
    }
    alert("Length = "+length);
    for(i=0;true;){
   	 if ( length == 0 ){
    	break;
    }
  	 
  	 //if ( i >= allchecks.length ){
  	 	//break;
  	// }
  	else if( allchecks[i].checked ){
   		checkSel = true;
   		length = length -1;
   		remtable.deleteRow(9+i)
   		i=0;
    }
    else {
    	i++;
    }
   
 }
  alert("Length = "+length);
 
if( checkSel == false ){
  alert("Please Select The Message! "); 
  return false;
 }
 return true;
	
 }
//-->
</script>
<script language="JavaScript" type="text/JavaScript">

function accessdenied2() {
var frm= document.forms[0];
	document.forms[0].name.focus();
}
</script>

<script>



function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

function MM_openBrWindow(theURL,winName,features) { 
alert
  window.open(theURL,winName,features);
}

function newWindow1( frm  ) {
		   msgWindow = window.open( frm.submit(), "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
	  }
</script>




<script language="JavaScript" type="text/JavaScript">


function viewAttachment(){
 
           
        

 var docId="<%=(String)session.getAttribute("DocId")%>";

	document.InitForm.target="framea"
    document.InitForm.action = "<%=request.getContextPath()%>/servlet/showImageFromTL1?";
	document.InitForm.submit();	 
     
			


}
</script>






</head>

<body >



 <% System.out.println("Initial Pedigree Frame1");
 String receivedFax=(String)session.getAttribute("operationType");
 System.out.println("rvc"+receivedFax);
 
 %>


<html:form  action="dist/epedigree/InitialPedigree.do?pagenm=pedigree" enctype="multipart/form-data" >

  <html:hidden property="documentID"/>
  
    <TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
    <br><br>
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Trading Partner </td>
                </tr>		
					<TR class="tableRow_Off">
					
					<TD>*  Name:</TD>
					
 
					<td><html:select property="tpName" onchange="tpDetails();"><html:option value = "" >Select</html:option>
					<% List tpNamesss = (List)session.getAttribute("tpNames");
	  		  	       System.out.println("tp names in jsp: "+tpNamesss);
				       for(int i=0;i<tpNamesss.size();i++){
				    %>
				    <html:option value ="<%=tpNamesss.get(i).toString()%>"><%=tpNamesss.get(i).toString()%></html:option>
					<% } %>
					</html:select></td>
					
				     <TD>* Address1 :</TD>
						<TD>
						<html:text property="address1"    />
						 </TD>
					</TR>
					<TR class="tableRow_Off">
					 
				     <TD> Address2 :</TD>
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
						  
						  <TD>* Country :</td>
						<TD>
						<html:text property="country"   />
						  </TD>
						  
						
					</TR>
					<TR class="tableRow_Off">
						
						<TD>* Zip Code:</td>
						<TD>
						<html:text property="zipCode"    />
						 </TD>
						
						<TD colspan=2>&nbsp;</td> 
					</TR>
		 				
		 			<TR class="tableRow_Off">
						<TD>*  Contact Name :</TD>
						<TD>
						<html:text property="contactName"   />
						 </TD>
				     <TD> Contact Title :</TD>
						<TD>
						<html:text property="contactTitle"    />
						 </TD>
					</TR>
					
					<TR class="tableRow_Off">
						<TD>*  Contact Phone :</TD>
						<TD>
						<html:text property="contactPhone"   />
						</TD>
				     <TD> Contact E-Mail :</TD>
						<TD>
						<html:text property="contactEmail"    />
						 </TD>
					</TR>	
		 			
		 			
				</TABLE>
  
  
  <br>
  <TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
                    <td class="title" colspan="4">Transaction information </td>
                	</tr>		
					<TR class="tableRow_Off">
						<TD>* Document Type :</td>
						<TD><html:select  property="transactionType"  value="<bean:write property='transactionType' name='InitialPedigreeForm' />">
							 <logic:notPresent property="transactionType" name="InitialPedigreeForm">
							 <option >Invoice</option>
							 <option >PurchaseOrder</option>
							 </logic:notPresent> 
							 <logic:present property="transactionType" name="InitialPedigreeForm">
							 <option><bean:write property='transactionType' name='InitialPedigreeForm'/></option>
							 <logic:notEqual property="transactionType" value="Invoice" name="InitialPedigreeForm">
							 <option >Invoice</option>
							 </logic:notEqual>
							 <logic:notEqual property="transactionType" value="PurchaseOrder" name="InitialPedigreeForm">
							 <option  >PurchaseOrder</option>
							 </logic:notEqual>
							 </logic:present>
							</html:select>
						</TD>
															
					  <TD >* Document Number :</td>
						<TD>
						<html:text property="transactionId"   />
					  </TD>
															
					</TR>
					
					
					<TR class="tableRow_Off">
					
					  <TD>* Document Date :</td>
						<TD>
						<html:text property="transactionDate" size="20" /><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('transactionDate', '%Y-%m-%d', '24', true);">
					  </TD>
					  <TD colspan=2>&nbsp;</td>  
					</TR>
					
					
				</TABLE>
  
  <br>
  <table border="0" cellspacing="0" cellpadding="0" width="100%" >
       
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
              
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%" id='dynamicTable' >		
					<tr class="tableRow_Header"> 
                  	<td class="title" colspan="5">Pedigree Details</td>
                	</tr>	
                
                
                	<TR class="tableRow_Off">
						
						<TD>* Product Code(NDC) :</TD>
						<TD>
						<html:text property="productCode" tabindex="1"   />
						</TD>
						
						<TD>
						<input type="button" name="lndc" value="Lookup NDC" tabindex="2" onclick="lookupNDC();"/>
						</TD>
						<TD colspan=2>&nbsp;</td> 
					</TR>
                
                	
					<TR class="tableRow_Off">
						<TD>* Drug Name:</TD>
						<TD><html:text property="name"  tabindex="3"  /></TD> 
						
						<TD>* Shipped Quantity :</td>
						<TD colspan="2"><html:text property="quantity" tabindex="8"   />
						</td>
						
						
					</TR>
					
					
					<TR class="tableRow_Off">
					
					<TD>* Strength :</TD>
						<TD >
						<html:text property="strength"  tabindex="4"   />
					
						 </TD>
						 
						 <TD>* Lot Number:</td>
						<TD colspan="2"><html:text property="lotNumber"  tabindex="9"  />		
						 </TD>
						 
						 
					</TR>
					 
					 
					 <TR class="tableRow_Off">
						
						<TD>* Dosage Form :</td>
						<TD>
						<html:text property="dosageForm"  tabindex="5" />
						  </TD>
						  
						 <!--<TD> Expiration date(MM/YY) :</td>
						<TD><html:text property="expirationDate"  tabindex="10"  />
						</TD>-->
						 <TD colspan=1>&nbsp;</TD>	
						<TD colspan=2>&nbsp;</TD>	
						 
					</TR>
					
					<TR class="tableRow_Off">
					 
					  <TD>* Container Size:</td>
						<TD>
						<html:text property="containerSize"  tabindex="6"   />
						</TD>
						 <TD>* Manufacturer :</TD>
						<TD colspan="2">
						<html:text property="manufacturer"   tabindex="11"  />
						</TD> 
						
						
						 			  
					</TR>
					
					
					<%
						String lotNos[]=(String[])session.getAttribute("lotNos");
		    			String quantity[]=(String[])session.getAttribute("quantity");
		    			String expirationDate[]=(String[])session.getAttribute("expirationDate");
						for(int k=1 ;lotNos!=null && k<lotNos.length ;k++){
					
					%>
					
					<TR class="tableRow_Off"  >
					
						<TD>* Shipped Quantity :</td>
						<TD>
						<html:text property="quantity"  value="<%=quantity[k]%>"     />
						</TD>
						
					 	<TD>* Lot Number:</td>
						<TD>
						<html:text property="lotNumber"  value="<%=lotNos[k]%>"  />
						</TD>
					 
					 </TR>
					 
					
					<%
						}
					%>  
					
	  <%-- Disabling the Paper pedigree Attached pop up Window for Upade Data --%>	
		
		<%-- Start--%>		
				 		
		 <%String str=(String)session.getAttribute("test");
		 if(str==null || str.equals("") || str.equals("null")){%>
		  	 <TR class="tableRow_Off">
			  		<TD colspan=6 >
			 		  Paper Pedigree Attachment &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			  		  <html:file property="attachment" tabindex="7" />  (Only PDF Attachments accepted )
			 		</TD>
			 		
				</TR>
				
		  
			 		
				<%}else{%>
				
				 <TR class="tableRow_Off">
			  		<TD colspan=3 >
			 		  Paper Pedigree Attachment &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			  		  <html:file property="attachment" tabindex="7" />  (Only PDF Attachments accepted )
			 		</TD>
			 		<TD colspan=3 >
			 		  <input type="button" name="View Attachment" value="View Attachment" onClick="viewAttachment();"/> 
			 		</TD>
				</TR><%}%>
		   
		     
	  <%-- End --%>									
					<TR class="tableRow_Off">
						<TD colspan=2>* fields are mandatory</td>
						<TD colspan=4>&nbsp;</TD>
					</TR>
					
					<TR class="tableRow_Off">
				 			<TD ><input type="button" name="Add LotInfo" value="AddLotInfo" onClick="addLotInfo();" tabindex="12"   ></TD>
						  	<TD ><input type="button" name="Remove LotInfo" value="RemoveLotInfo" onClick="removeLotInfo();" ></TD>
						   <TD colspan=4>&nbsp;</TD>		 
					</TR>
					
			</TABLE>
 
	 
				<Center>  
           
          
										<html:hidden property="pagenm" value="<%=pagenm%>" />
										<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />
           
           <logic:notPresent property="documentID" name="InitialPedigreeForm">
            <html:hidden property="operationType" value="Save" />
                        <input type="hidden" name="receivedFax" value="<%=receivedFax%>" />
           <input type="submit" name="save" tabindex="13"  value="Save Data" target="frame2" onclick="return validate_form(this);" id="saveDataButton" />
           </logic:notPresent>
           
           <%
           
          String docId=(String)session.getAttribute("DocId");
		  System.out.println("Frame1"+docId);
		  
          %>
           
           <logic:present property="documentID" name="InitialPedigreeForm">
              <input type="hidden" name="receivedFax" value="<%=receivedFax%>" />
            <html:hidden property="operationType" value="Update" />
          <input type="submit"  name="save" tabindex="23" target="frame2"  value="Update Data" onclick="return validate_form(this);" id="UpdateDataButton"/>
           </logic:present>
          
           
           </Center>
             
            
         </td>
        </tr>
      </table> 
      </td>
        </tr>
       
      </table> 
     
<%@include file='onLoad.jsp'%> 	
</html:form>

  
 <script language="JavaScript" type="text/JavaScript">
 window.refresh;

</script>

</body>


</html:html>

<%
if(isUpdate.equals("true"))
{%>
<script language="JavaScript" type="text/JavaScript">
<!--
 window.onload = function() {
	  window.setTimeout( "alert('Updated Successfully')",10);
	  
	 }
	 
//-->
</script>
<%
request.removeAttribute("updateSuccess");
}%>



<%
String str = (String)session.getAttribute("test");
System.out.println("tesssssssssst\n\n\n\n"+str);
if(str==null)str="";
if(str.equals("process")){%>
<form name="InitForm" >
<input type="hidden" name="dbname" value="ePharma"/>
<input type="hidden" name="collname" value="PaperPedigree"/>
<input type="hidden" name="mimetype" value="application/pdf"/>
<input type="hidden" name="nodename" value="data"/>
<input type="hidden" name="topnode" value="initialPedigree"/>
<%} 


if(str==null || str.equals("") || str.equals("null")){
%>


<form name="InitForm" >
<input type="hidden" name="dbname" value="ePharma"/>
<input type="hidden" name="collname" value="PaperPedigree"/>
<input type="hidden" name="mimetype" value="application/pdf"/>
<input type="hidden" name="nodename" value="data"/>
<input type="hidden" name="topnode" value="initialPedigree"/>
<input type="hidden" name="DocumentInfo/serialNumber" value="<%=(String)session.getAttribute("DocId")%>"/>


<%}  if(str.equals("Update")){
%>


<form name="InitForm" >
<input type="hidden" name="dbname" value="ePharma"/>
<input type="hidden" name="collname" value="PaperPedigree"/>
<input type="hidden" name="mimetype" value="application/pdf"/>
<input type="hidden" name="nodename" value="data"/>
<input type="hidden" name="topnode" value="initialPedigree"/>
<input type="hidden" name="DocumentInfo/serialNumber" value="<%=(String)session.getAttribute("DocId")%>"/>


<%} 

 if(str.equals("received")){%>
<form name="InitForm" >
<input type="hidden" name="dbname" value="ePharma"/>
<input type="hidden" name="collname" value="ReceivedFax"/>
<input type="hidden" name="mimetype" value="application/pdf"/>
<input type="hidden" name="nodename" value="FileContents"/>
<input type="hidden" name="topnode" value="InboundPostRequest"/>

<%} 




 
 
String docId=(String)session.getAttribute("DocId");
System.out.println("ddddddddd"+docId);
if(docId==null)docId="";
else{
 

 if(str.equals("process")){%>
<input type="hidden" name="DocumentInfo/serialNumber" value="<%=docId%>"/>
<script language="JavaScript" type="text/JavaScript">
    	 
		 document.InitForm.target = "framea";
    	 document.InitForm.action = "<%=request.getContextPath()%>/servlet/showImageFromTL1?";
		 document.InitForm.submit();
     
       
</script>
<%}if(str.equals("received")){%>
<input type="hidden" name="InitialPedigreeId" value="<%=docId%>"/>
<script language="JavaScript" type="text/JavaScript">
 
		 document.InitForm.target = "framea";
    	 document.InitForm.action = "<%=request.getContextPath()%>/servlet/showImageFromTL1?";
		 document.InitForm.submit();
     
       
</script>
<%}if(str.equals("Update")){%>
<input type="hidden" name="DocumentInfo/serialNumber" value="<%=docId%>"/>
<%}%>




<%session.removeAttribute("test");}%>
</form> 

 
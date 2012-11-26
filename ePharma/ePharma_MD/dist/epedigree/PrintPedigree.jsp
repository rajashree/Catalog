
<%@ page session="true"%>
<%@ page autoFlush="true"%>
<%@ page import="com.rdta.rules.OperationType"%>
<%@ page language="java"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.commons.persistence.TLQueryRunner"%>
<%@ page import="com.rdta.epharma.reports.form.PedigreeReportForm"%>
<html>
<head>
  
  <meta http-equiv='Content-Type'
 content='text/html; charset=iso-8859-1'>
<link href='../../assets/epedigree1.css' rel='stylesheet' type='text/css'>

<script language="JavaScript" type="text/JavaScript">
function submitThis()
{
	alert('The document has been digitaly signed and submitted');
	this.window.close();
}
</script>

</head>

<%

		String xQuery = "";
		String apnId = request.getParameter("apnId");

		System.out.println("apnId.............."+apnId);
	    	xQuery = xQuery+ "tlsp:getPedigreeData('"+apnId+"')";
		TLQueryRunner queryRunner = new TLQueryRunner();
		String str = queryRunner.returnExecuteQueryStringsAsString(xQuery);
		System.out.println("str.............."+str);

	//String s = (String)request.getAttribute("htmlString");
	
	Node n = XMLUtil.parse(str);
	
	
	String companyNameFrom = XMLUtil.getValue(n,"companyNameFrom");
	String companyAddressFrom = XMLUtil.getValue(n,"companyAddressFrom");
	String LegendDrugName = XMLUtil.getValue(n,"LegendDrugName");
	String Strength = XMLUtil.getValue(n,"Strength");
	String DosageForm = XMLUtil.getValue(n,"DosageForm");
	String Quantity = XMLUtil.getValue(n,"Quantity");
	String QuantityType = XMLUtil.getValue(n,"QuantityType");
	if(QuantityType==null || QuantityType.equals(""))
		QuantityType="Cases";
	String NDC = XMLUtil.getValue(n,"companyAddressFrom");
	String ContainerSize = XMLUtil.getValue(n,"ContainerSize");
	String Manufacturer = XMLUtil.getValue(n,"Manufacturer");
	String ManufacturerAddress = XMLUtil.getValue(n,"ManufacturerAddress");
	String LotNumber = XMLUtil.getValue(n,"LotNumber");
	String ReferenceNumber = XMLUtil.getValue(n,"ReferenceNumber");
	String ReferenceDate = XMLUtil.getValue(n,"ReferenceDate");
	String ReferenceType = XMLUtil.getValue(n,"ReferenceType");
	String ParentEPC = XMLUtil.getValue(n,"ParentEPC");
	String IssueDate =  XMLUtil.getValue(n,"IssueDate");
	
	
	
	String companyNameTo = XMLUtil.getValue(n,"companyNameTo");
	String companyAddressTo = XMLUtil.getValue(n,"companyAddressTo");
	String ContactNameTo = XMLUtil.getValue(n,"ContactNameTo");
	String ContactPhoneTo = XMLUtil.getValue(n,"ContactPhoneTo");
	if(ContactPhoneTo==null || ContactPhoneTo.equals(""))
		ContactPhoneTo="(212) 223 1234";
	String ContactEmailTo = XMLUtil.getValue(n,"ContactEmailTo");

	if(ContactEmailTo==null || ContactEmailTo.equals(""))
		ContactEmailTo=ContactNameTo+"@cardinal.com";
	
	
	
		List list = XMLUtil.executeQuery(n,"custodyAll/Custody"); 
	
	
	
	

%>
<body leftmargin='0' topmargin='0' rightmargin='0' marginheight='0'
 marginwidth='0'>


            <table id='Table1' align='left' bgcolor='white' border='0'
 cellpadding='1' cellspacing='1' width='100%'>
              <tbody>
                <tr bgcolor='white'>
                  <td class='td-typeblack' colspan='3'>DH 2129 Prescription (Legend) Drug Pedigree Form &shy; (Optional until July 1,2006) <br>
                  <br>
                  <div style='text-align: center;'><big><%=companyNameTo%>&nbsp;<%=companyAddressTo%></big><br>
                  <br>
				PRESCRIPTION (LEGEND) DRUG PEDIGREE<br>HISTORY OF DRUG SALES AND DISTRIBUTIONS<br>
                  <br>
                  <div style='text-align: left; font-weight: normal;'><span style='font-weight: bold;'>Legend Drug Name, Strength, Dosage Form, Container Size</span>: 
                  <span style='text-decoration: underline;'><%=LegendDrugName%>,&nbsp; <%=Quantity%> 
 			&nbsp;<%=QuantityType%>, &nbsp;<%=DosageForm%><br>
                  </span><span style='font-weight: bold;'>Manufacturer</span>:
			<%=Manufacturer%><br>
                  <span style='font-weight: bold;'>NDC</span>:<%=NDC%><br>
                  <table style='left: 190px; width: 796px;' border='0' cellpadding='2' cellspacing='1'>
                    <tbody>
                      <tr>
                        <td class='tableData' style='vertical-align: top;'><br>
                        <table style='left: 195px; width: 456px;' border='1' cellpadding='2' cellspacing='0'>
                          <tbody>
                            <tr>
                              <th scope='col'>Lot Number</th>
                              <th scope='col'>Quantity</th>
                              <th scope='col'>Unique tracking #(RFID/UPC)</th>
                            </tr>
                            <tr>
                              <td align="center"><%=LotNumber%></td>
                              <td align="center"><%=Quantity%></td>
                              <td align="center"><%=ParentEPC%></td>
                            </tr>
                          </tbody>
                        </table>
                        <div style='height: 18px; margin-top: 4px;'><input type='checkbox'> This is a repackaged drug (requires repackager'spedigree information) </div>
                        </td>
                        <td style='vertical-align: top;'>PO Number: <%=ReferenceNumber%><br>Document Type: <%=ReferenceType%><br>Reference Date: <%=ReferenceDate%><br>
                        <br>(related to the sale by the wholesaler<br>identified above)<br>
                        </td>
                      </tr>
                    </tbody>
                  </table>

                  <br style='font-weight: bold;'>

                  <span style='font-weight: bold;'>1. WHOLESALER THAT
PURCHASED FROM THE MANUFACTURER / REPACKAGER<br>
                  <br>
                  </span>
                  <table style='width: 100%; text-align: left;'
 border='0' cellpadding='2' cellspacing='2'>
                    <tbody>
                      <tr>
                        <td style='vertical-align: top;' width='50%'><span
 style='font-weight: bold;'>Name</span>: <%=companyNameTo%><br>
                        <span style='font-weight: bold;'>Address</span>:
<%=companyAddressTo%><br><br>
                        <br>
                        <span style='font-weight: bold;'>Date Purchased
&amp; Reference* #</span>: <%=IssueDate%> ,<%=ReferenceNumber%><br>
                        <br>
                        <small><span style='font-style: italic;'>To
authenticate a subsequent transaction contact:</span></small><br>
                        <span style='font-weight: bold;'>Name</span>:
<%=ContactNameTo%><br>
                        <span style='font-weight: bold;'>Telephone
number</span>: <%=ContactPhoneTo%><br>
                        <span style='font-weight: bold;'>Email address</span>:
<%=ContactEmailTo%><br>
                        </td>
                        <td style='vertical-align: top;'><span
 style='font-weight: bold;'>Name</span>: <%=companyNameTo%><br>
                        <span style='font-weight: bold;'>Address</span>:<%=companyAddressTo%><br>
                        <span style='font-weight: bold;'><br>
                        <br>
                        <br>
                        <br>
                        </span><small><span style='font-style: italic;'>To
authenticate a subsequent transaction contact:</span></small><br>
                        <span style='font-weight: bold;'>Name</span>:<%=ContactNameTo%><br>
                        <span style='font-weight: bold;'>Telephone
number</span>:<%=ContactPhoneTo%><br>
                        <span style='font-weight: bold;'>Email address</span>:<%=ContactEmailTo%></td>
                      </tr>
                    </tbody>
                  </table>
                  
        	<%
        		 int count =0;
			for (int i=0 ; i<list.size(); i++)
			{
				String custodyName = XMLUtil.getValue((Node)list.get(i),"Name");
				String address = XMLUtil.getValue((Node)list.get(i),"Address");
				String contact = XMLUtil.getValue((Node)list.get(i),"Contact");
				String TransactionDate = XMLUtil.getValue((Node)list.get(i),"TransactionDate");
				String TransactionType = XMLUtil.getValue((Node)list.get(i),"TransactionType");
				String TransactionNumber = XMLUtil.getValue((Node)list.get(i),"TransactionNumber");
				String AuthenticatorName = XMLUtil.getValue((Node)list.get(i),"AuthenticatorName");
				String AuthenticatorEmail = XMLUtil.getValue((Node)list.get(i),"AuthenticatorEmail");
				String AuthenticatorPhone = XMLUtil.getValue((Node)list.get(i),"AuthenticatorPhone");
				System.out.println("custodyName..."+custodyName);
				//check null
				if(contact==null || contact.equals(""))
					contact = "John Plamer";
				
				if(AuthenticatorName==null || AuthenticatorName.equals(""))
					AuthenticatorName = contact;
				if(AuthenticatorPhone==null || AuthenticatorPhone.equals(""))
					AuthenticatorPhone = "(212) "+i+"7"+i+" 2222";
				if(AuthenticatorEmail==null || AuthenticatorEmail.equals(""))
					AuthenticatorEmail = contact+"@"+custodyName+".com";
					
					
		%>	
          
                  <span style='text-decoration: underline;'></span></div>
                  </div>
                  <br style='font-weight: bold;'>
		  <hr style='border: solid 1px #CCCCCC;' />
                  <span style='font-weight: bold;'><br>
                  </span>
	
                  <table style='width: 100%; text-align: left;' border='0' cellpadding='2' cellspacing='2'>
                    <tbody>
                    <% count = i +1;%>
                      <tr><td style='width: 50%; vertical-align: top; font-weight: bold;'>#<%=count%> above Sold to: <br>
                        </td>
                        <td style='vertical-align: top;'><span style='font-weight: bold;'>Shipped to:<br>
                        </span></td></tr>
                      <tr>
                        <td style='vertical-align: top;'><span style='font-weight: bold;'>2. Name</span>: <%=custodyName%><br>
                        <span style='font-weight: bold;'>Address</span>:<%=address%><br>
                        <br>
                        <span style='font-weight: bold;'>Date Purchased &amp; Reference* #</span>: <%=TransactionDate%> &nbsp;<%=TransactionNumber%><br>
                        <span style='font-weight: bold;'>Print Name of Recipient</span>:<%=contact%><br>
                        <span style='font-weight: bold;'>Signature of Recipient</span>:<br>
                        <span style='font-weight: bold;'>Name of Authenticator</span>: <%=AuthenticatorName%><br>
                        <span style='font-weight: bold;'>Signature of Authenicator</span>:<br>
                        <br><small><span style='font-style: italic;'>To authenticate a subsequent transaction contact:</span></small><br>
                        <span style='font-weight: bold;'>Name</span>:<%=AuthenticatorName%><br>
                        <span style='font-weight: bold;'>Telephone number</span>: <%=AuthenticatorPhone%><br>
                        <span style='font-weight: bold;'>Email address</span>:<%=AuthenticatorEmail%></td>
                        <td style='vertical-align: top;'><span style='font-weight: bold;'>Name</span>: <%=custodyName%><br>
                        <span style='font-weight: bold;'>Address</span>:<%=address%><span style='font-weight: bold;'><br>
                        </span><%=address%><br><br>
                        <span style='font-weight: bold;'>Date Received:<%=TransactionDate%>
                        </span>7/13/2005<br><span style='font-weight: bold;'>Reference:</span>
                         <%=TransactionType%>  <%=TransactionNumber%>
                        <span style='font-weight: bold;'><br>Print name of Recipient: </span>
                        <%=contact%><br>
                        <span style='font-weight: bold;'>Name of Authenticator</span>:<%=AuthenticatorName%><br>
                        <span style='font-weight: bold;'>Signature of Authenicator</span>:<br>
                        <br>   <small><span style='font-style: italic;'>To authenticate a subsequent transaction contact:</span></small><br>
                        <span style='font-weight: bold;'>Name</span>:<%=AuthenticatorName%><br>
                        <span style='font-weight: bold;'>Telephonenumber</span>: <%=AuthenticatorPhone%><br>
                        <span style='font-weight: bold;'>Email address</span>:<%=AuthenticatorEmail%><br>
                       </td>
                      </tr>
                    </tbody>
                  </table>
                 <%
                 	}
                 %> 
                  <span style='text-decoration: underline;'></span> </p>
                  </td>
                </tr>
                <tr>
                  <td align='left'><!-- Dashboard Start -->
                  <table style='top: 625px; height: 467px;' border='0'
 cellpadding='2' cellspacing='1' width='100%'>
                    <tbody>
                      <tr class='tableRow_On'>
                        <td style='height: 24px;' align='right'
 nowrap='nowrap' valign='top' width='25%'><strong></strong><br>
                        </td>
                        <td class='tableData'></td>
                      </tr>
<!-- new form --> <tr>
                        <td colspan='2'>
                        <table width='100%'>
                          <tbody>
                            <tr>
                              <td align='top' width='50%'>
                              <div style='height: 30px;'> <strong><%=count%>
above Sold to:</strong><br>
                              </div>
                              <table>
                                <tbody>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Name:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Address:</td>
                                    <td><textarea name='desc' cols='28'
 rows='2'></textarea></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>DatePurchased&amp;
Reference*#:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Print Name of Recipient:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Signature of Recipient:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Name of Authenicator:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Signature of Authenticator:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td colspan='2'></td>
                                  </tr>
                                  <tr>
                                    <td colspan='2'><em>To authenticate
a subsequent transaction contact:</em></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Name:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Telephone:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Email:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                </tbody>
                              </table>
                              </td>
                              <td width='50%'>
                              <div style='height: 30px;'> <strong>Shipped
to:</strong><br>
                              </div>
                              <table>
                                <tbody>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Name:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Address:</td>
                                    <td><textarea name='desc' cols='28'
 rows='2'></textarea></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>DateReceived#:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Reference*:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Print Name of Recipient:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Name of Authenicator:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'
 width='15%'>Signature of Authenticator:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td colspan='2'></td>
                                  </tr>
                                  <tr>
                                    <td colspan='2'><em>To authenticate
a subsequent transaction contact:</em></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Name:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Telephone:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                  <tr>
                                    <td align='right' valign='top'>Email:</td>
                                    <td><input value='' name='eDate'
 size='30' type='text'></td>
                                  </tr>
                                </tbody>
                              </table>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        </td>
                      </tr>
                      <tr>
                        <td colspan='2'></td>
                      </tr>
                      <tr>
                        <td colspan='2'></td>
                      </tr>
                      <tr class='tableRow_Header'>
                        <td colspan='2' class='type-whrite'><strong>I
swear or affirm that the information contained on this pedigree paper
is accurate and complete and that my company has authenticated prior
distributions, if required.</strong></td>
                      </tr>
			<TR>
			<TD><STRONG>Signature:</STRONG></TD>
			</TR>
			<TR>
			<td><img src="..\..\assets\images\sign1.jpg" width="144" height="50"></td>			
			</TR>
			
                      
                      <tr>
                        <td colspan='3'><input  onclick="javascript:submitThis();" value='Digitally Signed' type='submit'>
                        Date: <input value='' name='eDate' size='20' type='text'></td>
                      </tr>
                      <tr>
                        <td colspan='2'><br>
* Reference Number may be an invoice, purchase order, shipping document
number or similar unique identifier for the transaction.</td>
                      </tr>
                    </tbody>
                  </table>
                  </td>
                </tr>
              </tbody>
            </table>
            </td>
          </tr>
        </tbody>
      </table>
      </td>
    </tr>
  </tbody>
</table>
</body>
</html>
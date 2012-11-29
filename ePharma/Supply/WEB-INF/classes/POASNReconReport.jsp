<%@include file="DbOparetions.jsp" %>
<html>
<body bgcolor="white">
<%@include file="BuyerNav.jsp"%>
 <table  align="center" width=50% border=2>
  <th align="center" colspan=3><font size="6">ReConsolation Status Report</font></th>
<tr><td>OrderID</td><td>Difference in Quantity</td><td>Status</td></tr>

<%
com.rdta.tlapi.xql.Connection connection = TigConnect();
      com.rdta.tlapi.xql.Statement statement = getstat(connection);
      byte[] xmlResults;
  w=out;
  r=response;
  ses=session;
 
  String xQuery = "";
  xQuery=xQuery+"<results>{ ";
  xQuery=xQuery+"for $x in collection('tig:///SELLER/PO_RX')/POI/PO ";
  xQuery=xQuery+"for $y in collection('tig:///SELLER/ASN')/ASNI/ASN ";
  xQuery=xQuery+"where $x/OrderID = $y/OrderID ";
  xQuery=xQuery+"return";
  xQuery=xQuery+"<result>";
  xQuery=xQuery+"<o>{data($y/OrderID)}</o>";
  xQuery=xQuery+"{";
  xQuery=xQuery+"if(data($y/Items/Item/ItemQty)=data($x/Items/Item/ItemQty))";
   xQuery=xQuery+"then <qty>{0}</qty>";
   xQuery=xQuery+"else <qty>{data($y/Items/Item/ItemQty)-data($x/Items/Item/ItemQty)}</qty>";
  xQuery=xQuery+"}";
  xQuery=xQuery+"</result> ";
  xQuery=xQuery+"}</results>";
  
  xmlResults = tigread(statement, xQuery);
 
if(xmlResults==null)
   {
      String error=new String("Data not Available InDatabase");
      session.setAttribute("excp",error);
	response.sendRedirect("Failure");
    }
else
{
 java.io.InputStream result = new ByteArrayInputStream(xmlResults);
 
 		String nodeqty="";
         	String quantity=null;
 		
		String oid=null,status=null;
 		
		int q1=0;
        	try
		{
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		DocumentBuilder db=dbf.newDocumentBuilder();
		org.w3c.dom.Document doc=db.parse(result);

		NodeList found=doc.getElementsByTagName("result");
		org.w3c.dom.Node usernode,nodename;
		Node itemnode;
		Node userchildnode;
		NodeList usrchlnodes,itemnodes,itemnodes1;
		Node n1;
		int results=found.getLength();
		
		if(results>0)
		{
			for(int i=0;i<results;i++)
			{
				usernode=found.item(i);

				itemnodes=usernode.getChildNodes();
				
				for(int j=0;j<itemnodes.getLength();j++)
				{
				  nodename=itemnodes.item(j);
				  nodeqty=nodename.getNodeName();
				  if(nodeqty.equals("o"))
				    oid=nodename.getFirstChild().getNodeValue();
 				  if(nodeqty.equals("qty")) 
				    quantity=nodename.getFirstChild().getNodeValue();
				  
			        }
			        q1=Integer.parseInt(quantity);
			        if(q1==0) status="Success";
                                else
                                 if(q1<0) status="Failed-Less";
				 else
                                     status="Heigher";

			        
			       
	 %>
	<tr>
  	<td><% w.println(oid); %></td><td><% w.println(q1); %></td>
       <td><% w.println(status); %></td>
      </tr> 

	<% }
   
	 }//end for if
	}// end try  2
     catch(javax.xml.parsers.ParserConfigurationException pce)
     {
    	 String error=new String(pce.getMessage());
   			session.setAttribute("excp",error);
  	try{
   		 response.sendRedirect("Failure");}
	catch(Exception e){}
     }
     catch(org.xml.sax.SAXException pce)
     {
    	 String error=new String(pce.getMessage());
   			session.setAttribute("excp",error);
   		   	try{
   		 response.sendRedirect("Failure");}
	catch(Exception e){}

     }
     catch(java.io.IOException ioe)
     {
    	 String error=new String(ioe.getMessage());
   			session.setAttribute("excp",error);
   		   	try{
   		 response.sendRedirect("Failure");}
	catch(Exception e){}

     }
}
  %> 
    
 </table>
</body>
</html>

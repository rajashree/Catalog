<%@include file="DbOparetions.jsp" %>
<%!String itemid="";
	String itemqty="";
	String item_price="";
	String poid="";
	String item_desc=""; %>
<html>
<body bgcolor="white">
<%@include file="SellerNav.jsp"%>
<form action="Login" method="post">
<table align="center" border=2  width=50%>
<th colspan=2><font size=6>ASN Generation Form</font></th>

 <%
         String poid1=request.getParameter("oid");
	//out.println(poid1); 
	com.rdta.tlapi.xql.Connection connection = TigConnect();
      	com.rdta.tlapi.xql.Statement statement = getstat(connection);
      	byte[] xmlResults;
  	w=out;
  	String xQuery = "";
	
	xQuery =xQuery+"for $b in collection('tig:///SELLER/PO_RX')/POI/PO ";
        xQuery =xQuery+"where $b/OrderID ='"+poid1+"'";
	xQuery = xQuery + "return $b ";

  	xmlResults = tigread(statement, xQuery);
  	//String s=new String(xmlResults);
  	//out.println("Result is: "+s);
  	 
	
 	String nodename="";
		 
        String id1="";
	String userid="";
	
	String mailid="";
	
	String ord_desc="";
	String date="";
        
        try
	{
  		java.io.InputStream result = new ByteArrayInputStream(xmlResults);
	  DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
	  DocumentBuilder db=dbf.newDocumentBuilder();
	  org.w3c.dom.Document doc=db.parse(result);
	  NodeList found=doc.getElementsByTagName("PO");
	  Node usernode,itemnode;
	  Node userchildnode;
	  NodeList usrchlnodes,itemnodes,itemnodes1;
	  Node n1;
	  int results=found.getLength();
	  if(results>0)
	  {
	    for(int i=0;i<results;i++)
	    {
		usernode=found.item(i);
		nodename=usernode.getNodeName();
		if(nodename.equals("PO"))
		{
		    n1=usernode.getAttributes().getNamedItem("status");
		    id1=n1.getNodeValue();	
	            usrchlnodes=usernode.getChildNodes();
	            for(int j=0;j<usrchlnodes.getLength();j++)
	            {
			userchildnode=usrchlnodes.item(j);
			if(userchildnode!=null)
			{
	 		  nodename=userchildnode.getNodeName();
			  if(nodename.equals("OrderID"))
			  {
			     poid=userchildnode.getFirstChild().getNodeValue();
				if(!(poid.equals(poid1))) break;
			   }	
			  if(nodename.equals("Items"))
			  {
			     itemnodes=userchildnode.getChildNodes();
                             itemnode=itemnodes.item(0);
                             nodename=itemnode.getNodeName();
  			     if(nodename.equals("Item"))
			     {
                                itemnodes1=itemnode.getChildNodes();
                                for(int k=0;k<itemnodes1.getLength();k++)
                                {
                                   itemnode=itemnodes1.item(k);
                                   nodename=itemnode.getNodeName();
                                   if(nodename.equals("ItemID"))
                                       itemid=itemnode.getFirstChild().getNodeValue();
                                   if(nodename.equals("ItemDescrpition"))
                                       item_desc=itemnode.getFirstChild().getNodeValue();
                                   if(nodename.equals("ItemQty"))
                                       itemqty=itemnode.getFirstChild().getNodeValue();
                                   if(nodename.equals("ItemPrice"))
                                       item_price=itemnode.getFirstChild().getNodeValue();
                                 }//end for loop k
                             }//end for item 
                          }//end for items
			}//j== if  7 

		}//end for loop for j  6
 	   }//end if for node name 5  

      }//end for loop for i 
   }//end if for results  3
  }// end try  2
  catch(javax.xml.parsers.ParserConfigurationException pce)
  {
    	 String error=new String(pce.getMessage());
   			session.setAttribute("excp",error);
 try{
   		 response.sendRedirect("Failure.jsp");}
	catch(Exception e){}
  }
  catch(org.xml.sax.SAXException pce)
  {
    	  String error=new String(pce.getMessage());
   			session.setAttribute("excp",error);
   		  try{
   		 response.sendRedirect("Failure.jsp");}
	catch(Exception e){}

  }
  catch(IOException ioe)
  {
    	  String error=new String(ioe.getMessage());
   			session.setAttribute("excp",error);
   		  try{
   		 response.sendRedirect("Failure.jsp");}
	catch(Exception e){}

  } 
  catch(NumberFormatException e)
  {
    
	 String error=new String(e.getMessage());
	 session.setAttribute("excp",error);
          try{
   		 response.sendRedirect("Failure.jsp");}
	catch(Exception e1){}

         
  }
  catch(NullPointerException e)
  {
    try{
       String error=new String(e.getMessage());
       session.setAttribute("excp",error);
       response.sendRedirect("Failure.jsp");
       }catch(Exception e1){}
  }
  %>
  <tr>
   <td>ConsignmentId:</td>
   <td><input type="text" name="consignmentId"  size=10></td>
  </tr>

  <tr>
   <td>OrderId:</td>
   <td><input type="text" name="orderId" value=<% out.println(poid1); %> size=10></td>
  </tr>  
  <tr>
   <td>ShipmentDate:</td><td><input type="text" name="shipmentDate" size="10"></td>
   
  </tr> 
   <tr>
   <td>ItemId:</td>
   <td><input type="text" name="itemId" value=<% out.println(itemid); %> size=10></td>
  </tr> 


  <tr>
   <td>ItemDescription:</td>
   <td><input type="text" name="itemDescription" value=<% out.println(item_desc); %> size=10></td>
  </tr> 
  <tr>
   <td>ItemQuantity:</td>
   <td><input type="text" name="itemQuantity" value=<% out.println(itemqty); %> size=10  onBlur='checknum(itemQuantity)'></td>
  </tr> 
  
  <tr>
   <td>ItemPrice:</td>
   <td><input type="text" name="itemPrice" value=<% out.println(item_price); %> size=10 onBlur='checknum(itemPrice)'></td>
  </tr> 
   <tr><td><input type="hidden" name="choice" value=4></td></tr>
  <tr>
    <td align="center" colspan=2> <input type="submit" value="Generate"></td></tr>

</table>
</form>
</body>
</html>

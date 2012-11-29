<%@include file="DbOparetions.jsp" %>
   <html>
   <body bgcolor="white">
<%@include file="SellerNav.jsp"%> 
   <form action="ASNReconsolation" method="post" >
 
   <table align="center" border=2  width=50%>
  <th align="center" colspan=2><font size=6>ASNReconsolation</font></th>
  <tr>
   <td>PO_ID:</td><td align="right"><select name="poId">

<% 
      	com.rdta.tlapi.xql.Connection connection = TigConnect();
      	com.rdta.tlapi.xql.Statement statement = getstat(connection);
      	byte[] xmlResults;
  	w=out;
  	String xQuery = "";
        xQuery=xQuery+"for $b in collection('tig:///SELLER/ASN')/ASNI ";
	xQuery=xQuery+"return $b ";
	
	//xQuery =xQuery+"for $b in collection('tig:///SELLER/ASN')/ASNI ";
	//xQuery = xQuery + "return $b ";

  	xmlResults = tigread(statement, xQuery);
  	//String s=new String(xmlResults);
  	//out.println("Result is: "+s);
  	 
	
 	String nodename="";
		 
        String id1="";
	String userid="";
	String itemid="";
	String itemqty="";
	String item_price="";
	String mailid="";
	String poid="";
	String ord_desc="";
	String date="";
        String item_desc="";
        try
	{
  		java.io.InputStream result = new ByteArrayInputStream(xmlResults);
	  DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
	  DocumentBuilder db=dbf.newDocumentBuilder();
	  org.w3c.dom.Document doc=db.parse(result);
	  NodeList found=doc.getElementsByTagName("ASN");
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
		if(nodename.equals("ASN"))
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
			     poid=userchildnode.getFirstChild().getNodeValue();
			 } 
			  
  			                         
		    
		    }//end for loop for j  6
 	   }//end if for node name 5 %>  
  	
	
           

	    <option value=<% w.println(poid); %> ><% w.println(poid); %> </option>
           
          
<%
      }//end for loop for i 
   }//end if for results  3
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
  catch(IOException ioe)
  {
    	  String error=new String(ioe.getMessage());
   			session.setAttribute("excp",error);
   		  try{
   		 response.sendRedirect("Failure");}
	catch(Exception e){}

  } 
  catch(NumberFormatException e)
  {
    
	 String error=new String(e.getMessage());
	 session.setAttribute("excp",error);
          try{
   		 response.sendRedirect("Failure");}
	catch(Exception e1){}

         
  }
  catch(NullPointerException e)
  {
    try{
       String error=new String(e.getMessage());
       session.setAttribute("excp",error);
       response.sendRedirect("Failure");
       }catch(Exception e1){}
  }

 %>
    </select> </td>  
    </tr>
    <tr><td align="center" colspan=2> <input type="submit" value="Generate"></td></tr>
    </table>

    </form>
    </body>
</html>

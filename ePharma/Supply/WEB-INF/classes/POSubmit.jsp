<%@include file="DbOparetions.jsp" %>
   <html>
   <body bgcolor="white"> 
<%@include file="BuyerNav.jsp"%>
   <table align="center" border=2 width=50%>
   <th colspan=10 align="center"><font size="10">Item Details</font></th>
   <tr>
   <td>UserID</td><td>MailID</td><td>OrderID</td><td>OrderDescription</td>
   <td>DateOfPurchase</td> <td>ItemID</td><td>ItemDescription</td>
   <td>ItemQty</td><td>ItemPrice</td><td>POsubmit</td>
   </tr>

<% 
ses=session;
r=response;
      	com.rdta.tlapi.xql.Connection connection = TigConnect();
      	com.rdta.tlapi.xql.Statement statement = getstat(connection);
      	byte[] xmlResults;
  	w=out;
  	String xQuery = "";
	String s="unchecked";
	xQuery =xQuery+" for $b in collection('tig:///BUYER/PO')/POI ";
	xQuery =xQuery+" where $b/PO/@status = '"+s+"' ";
	xQuery = xQuery + " return $b ";

  	xmlResults = tigread(statement, xQuery);
        if(xmlResults==null)
          response.sendRedirect("pofailure");
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
	  NodeList found=doc.getElementsByTagName("PO");
	  Node usernode,itemnode;
	  Node userchildnode;
	  NodeList usrchlnodes,itemnodes,itemnodes1;
	  Node n1;
	  int results=found.getLength();
          int dummy=1;
	   
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
		    if(id1.equals("checked")) continue;

	            usrchlnodes=usernode.getChildNodes();
	            for(int j=0;j<usrchlnodes.getLength();j++)
	            {
			userchildnode=usrchlnodes.item(j);
			if(userchildnode!=null)
			{
	 		  nodename=userchildnode.getNodeName();
			  if(nodename.equals("UserID"))
			     userid=userchildnode.getFirstChild().getNodeValue();
			  if(nodename.equals("MailID"))
			     mailid=userchildnode.getFirstChild().getNodeValue();
                          if(nodename.equals("OrderID"))
			     poid=userchildnode.getFirstChild().getNodeValue();
			  if(nodename.equals("OrderDescription"))
			     ord_desc=userchildnode.getFirstChild().getNodeValue();
			  if(nodename.equals("DateOfPurchase"))
			     date=userchildnode.getFirstChild().getNodeValue();
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
          
 	   }//end if for node name 5 %>  
  	<form action="Login" method="post" name="log">
           
	<tr><td><input type="text" name="userId" value='<% w.println(userid); %>' size=6></td>
        <td><input type="text" name="mailId" value='<% w.println(mailid); %>' size=15></td>
        <td><input type="text" name="orderId" value='<% w.println(poid); %>' size=7></td>
        <td><input type="text" name="orderdesc" value='<% w.println(ord_desc); %>' size=15 ></td>
        <td><input type="text" name="date" value='<% w.println(date); %>' size=14></td>
        <td><input type="text" name="itemid" value='<% w.println(itemid); %>' size=6></td>
        <td><input type="text" name="itemdesc" value='<% w.println(item_desc); %>' size=15></td>
        <td><input type="text" name="itemqty" value='<% w.println(itemqty); %>' size=7></td>
        <td><input type="text" name="itemprice" value='<% w.println(item_price); %>' size=9></td>
	<td><input type="submit"  value="SubmitPO" ></td>
        <td><input type="hidden" name="choice" value=5></td>
	</tr>
	</form>
        
<%
      }//end for loop for i 
   }//end if for results  3
   else
     response.sendRedirect("pofailure");
  }// end try  2
  catch(javax.xml.parsers.ParserConfigurationException pce)
  {
    	 String error=new String(pce.getMessage());
   			session.setAttribute("excp",error+"sixth");
   	try{	 
       response.sendRedirect("Failure");}
       catch(Exception e){}
  }
  catch(org.xml.sax.SAXException pce)
  {
    	  String error=new String(pce.getMessage());
   			session.setAttribute("excp",error+"seventh");
   	try{	 
       response.sendRedirect("Failure");}
       catch(Exception e){}

  }
  catch(IOException ioe)
  {
    	  String error=new String(ioe.getMessage());
   			session.setAttribute("excp",error+"eight");
   	try{	 
       response.sendRedirect("Failure");}
       catch(Exception e){}

  } 
  catch(NumberFormatException e2)
  {
    
	 String error=new String(e2.getMessage());
	 session.setAttribute("excp",error+"ninth");
   	try{	 
       response.sendRedirect("Failure");}
       catch(Exception e){}

         
  }
  catch(NullPointerException e3)
  {
    try{
       String error=new String(e3.getMessage());
       session.setAttribute("excp",error+"ten");
   	try{	 
       response.sendRedirect("Failure");}
       catch(Exception e){}

       }catch(Exception e1){}
  }

 %>
   
    </table>
    </body>
</html>

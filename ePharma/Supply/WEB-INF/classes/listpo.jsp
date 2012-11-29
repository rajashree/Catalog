<%@include file="DbOparetions.jsp" %>
<html>
   <body bgcolor="white"> 
<%@include file="SellerNav.jsp"%>
   <h1 align="center"><font size=10>List Of Received Items</font></h1>
      <table align="center" border=2 width=50%>
      <th align=right>UserID</th><th align=right>mailID</th><th align=right>OrderID</th><th align=right>OrderDescription</th><th align=right>DateOfPurchase</th><th align=right>ItemID</th>
<th align=right>ItemDescription</th><th align=right>ItemQuantity</th><th align=right>ItemPrice</th>

<% 
      com.rdta.tlapi.xql.Connection connection = TigConnect();
      com.rdta.tlapi.xql.Statement statement = getstat(connection);
      byte[] xmlResults=null;
  w=out;
  r=response;
  ses=session;

String s="unchecked";
  String xQuery = "";

  xQuery ="for $b in collection('tig:///SELLER/PO_RX')/POI ";
xQuery =xQuery+" where $b/PO/@status = '"+s+"' ";
    	xQuery = xQuery + "return $b ";

    xmlResults = tigread(statement, xQuery);
      
     if(xmlResults==null)
	response.sendRedirect("ListPoFailure");
      else
	{
String userid="",itemid="",itemqty="",item_price="";
		 String mailid="";
		 String poid="";
		 String ord_desc="";
		 String date="";
         	 String item_desc="";
		 String stat="uncheck";
   //String s=new String(xmlResults);
  //out.println("Result is: "+s);
   
   
 		String nodename="";
         	String id1="";
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
					if(id1.equals("checked")) continue;	
					usrchlnodes=usernode.getChildNodes();
					for(int j=0;j<usrchlnodes.getLength();j++)
					{
						userchildnode=usrchlnodes.item(j);
						if(userchildnode!=null)
						{
							nodename=userchildnode.getNodeName();
							if(nodename.equals("UserID"))
							{
								
								userid=userchildnode.getFirstChild().getNodeValue();

							}
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
    }//end for item nodes 8
        }//end for items-- node  7
						}//j== if  6
					}//end for loop for j  5
				}//end if for node name 4  %>

<tr>
  <td><% w.println(userid); %></td><td><% w.println(mailid); %></td><td><% w.println(poid); %></td><td><% w.println(ord_desc); %></td>
       <td><% w.println(date); %></td> <td><% w.println(itemid); %></td><td><% w.println(item_desc); %></td><td><% w.println(itemqty); %></td><td><% w.println(item_price); %></td>
<% session.setAttribute("oid"+i,poid); %>
<td><a href=ASNGenerationForm.jsp?oid=<%= poid %> >GenareteASN</a></td>
      </tr> 
				  
<%
	  		
			}//end for loop for i 3
			
		}//end if for results  2
	}// end try  1
     catch(javax.xml.parsers.ParserConfigurationException pce)
     {
    	 String error=new String(pce.getMessage());
   			session.setAttribute("excp",error);
   		 response.sendRedirect("Failure");
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
		catch(Exception e ){}

     }
	finally{
		try{
              tigclose(connection);
			}catch(Exception e){}
		}  

	}
  %>
 </table>
 
 </body>
</html>

<%@include file="DbOparetions.jsp" %>
<html>
<body bgcolor="white">
<%@include file="SellerNav.jsp"%>
 <table  align="center" width=50% border=2>
  <th align="center"><font size="6">ReConsolation Details Available</font></th>
<%
com.rdta.tlapi.xql.Connection connection = TigConnect();
      com.rdta.tlapi.xql.Statement statement = getstat(connection);
      byte[] xmlResults;
  w=out;
  r=response;
  ses=session;
  String recid=request.getParameter("poId");
String xQuery = "";
  xQuery ="<results>{"; 
  xQuery =xQuery+"for $x in collection('tig:///SELLER/PO_RX')/POI/PO ";
  xQuery =xQuery+"for $y in collection('tig:///SELLER/ASN')/ASNI/ASN ";
  xQuery =xQuery+"where $x/OrderID = $y/OrderID and $x/OrderID='"+recid+"' ";
    	xQuery = xQuery + "return <result><qty1>{data($x/Items/Item/ItemQty)}</qty1>  ";
	xQuery = xQuery + "<qty2>{data($y/Items/Item/ItemQty)}</qty2></result> ";
    xQuery=xQuery+"}</results>";
  xmlResults = tigread(statement, xQuery);

 java.io.InputStream result = new ByteArrayInputStream(xmlResults);
 		String nodeqty="";
         	String quantity=null;
 		String quantity1=null;
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
		System.out.println(results);
		if(results>0)
		{
			for(int i=0;i<results;i++)
			{
				usernode=found.item(i);
				itemnodes=usernode.getChildNodes();
				System.out.println(itemnodes.getLength());
				for(int j=0;j<itemnodes.getLength();j++)
				{
				  nodename=itemnodes.item(j);
				  nodeqty=nodename.getNodeName();
				  System.out.println(nodeqty);
				  if(nodeqty.equals("qty1"))
				  {
					quantity=nodename.getFirstChild().getNodeValue();
				
				    System.out.println(quantity);
				  }
				  if(nodeqty.equals("qty2"))
				  quantity1=nodename.getFirstChild().getNodeValue();
			    }
			      int q1=Integer.parseInt(quantity);
			      int q2=Integer.parseInt(quantity1);
			      int dif;
			       if(q2<q1)  
       {
         dif= -(q2-q1);
         String difference=new String("LESS BY "+dif);
	 session.setAttribute("diff",difference);
        response.sendRedirect("POASNResult");
	}
	else
 	if(q2>q1)
	{
	 dif=q2-q1;
         String difference=new String("Higher By "+dif);
	 session.setAttribute("diff",difference);
        response.sendRedirect("POASNResult");
	}
	else
	 response.sendRedirect("ReconSuccess");
	 }
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
  %> 
    
 </table>
</body>
</html>

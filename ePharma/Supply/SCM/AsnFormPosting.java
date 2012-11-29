package SCM; 
import java.io.IOException;    
import java.io.InputStream;
import java.io.PrintWriter;  
import javax.servlet.http.*;

import com.rdta.tlapi.xql.DataSource;
import com.rdta.tlapi.xql.DataSourceFactory;
import com.rdta.tlapi.xql.DataSourceProperties;
import com.rdta.tlapi.xql.QueryOption;
import com.rdta.util.io.StreamHelper;
public class AsnFormPosting extends HttpServlet {   
	public com.rdta.tlapi.xql.Connection TigConnect(PrintWriter w)
    {
      com.rdta.tlapi.xql.Connection k=null;
      String host="localhost";
      try    
        {
         DataSourceProperties p=DataSourceFactory.getDataSourceProperties();
         p.getProperty(DataSourceProperties.REMOTE_HOST).setValue(host);
         p.getProperty(DataSourceProperties.REMOTE_PORT).setValue("3408");
         p.getProperty(DataSourceProperties.LISTEN_PORT).setValue("3444");
         DataSource ds=DataSourceFactory.getDataSource(p);
         k=ds.getConnection("admin","admin");
        // w.println("successc");
         return k;
         }
         catch(com.rdta.tlapi.xql.XQLException e)
          {
              System.out.println(e);
              return k;
          }
     }

public com.rdta.tlapi.xql.Statement getstat(com.rdta.tlapi.xql.Connection con,PrintWriter w)
 {
   com.rdta.tlapi.xql.Statement s=null;
   try
      {
        s=con.createStatement();
        //w.println("successs");
         return s;
       }
      catch(com.rdta.tlapi.xql.XQLException e)
       {
         System.out.println(e);
         return s;
        }
   }

byte[] tigread(com.rdta.tlapi.xql.Statement s,String Q,PrintWriter w)
 {
    byte d[]=null;
   // w.println("successer");  
        try
           {
          com.rdta.tlapi.xql.ResultSet r=null;
          r=s.execute(Q);
          if(r.next())
           {
            InputStream ris=r.getBinaryStream();
            d=StreamHelper.copy(ris);
            ris.close();
            r.close();
            }
           else
              System.out.println("Qury Returned no results");
            }
            catch(com.rdta.tlapi.xql.XQLException e)
             {
               System.out.println(e);
              }
             catch(IOException i)
              {
              System.out.println(i);  
              }
         return d;
   }
        

public void tigclose(com.rdta.tlapi.xql.Connection k)
    {
      try
        {
         k.logoff();
         k.close();
         }
       catch(com.rdta.tlapi.xql.XQLException e)
         {
            System.out.println(e);
         }
     }    
  
    

    public  void executeStatementStream(String query,com.rdta.tlapi.xql.Statement st,PrintWriter w,HttpServletResponse res) 
 {
    	  	
    	try
		{
         com.rdta.tlapi.xql.ResultSet resultSet;
         QueryOption qo = QueryOption.XMLSPACE;
         st.setQueryOption(qo, "preserve");  
         //w.println("successe");
               System.out.println("before calling excute method<br>");
         resultSet= st.execute(query);
         System.out.println("after excute<br>");
             if(resultSet==null)
            	 res.sendRedirect("Failure");
             else
            	 res.sendRedirect("success2");
		
         if ( resultSet.next() )
         	System.out.println("inserted sucessfully");
         else
         	System.out.println( "insertion failed" );
         
         }
              catch(Exception e)
		{
			System.out.println("Error  "+e);    
		}	        	
     }   
	 	 
  
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	{
		PrintWriter out=null;
		HttpSession sesion=req.getSession();
		com.rdta.tlapi.xql.Connection k=null;
		try{
		out=res.getWriter();
		int  shipmentId=Integer.parseInt(req.getParameter("consignmentId"));
		String poid=req.getParameter("orderId"); 
		String shipmentDate=req.getParameter("shipmentDate");
		int qty=Integer.parseInt(req.getParameter("itemQuantity"));
		int itemid=Integer.parseInt(req.getParameter("itemId"));
		String itemdet=req.getParameter("itemDescription");
		Float f=Float.valueOf(req.getParameter("itemPrice"));
		float price=f.floatValue();
		
		String s="unchecked";
		 k=TigConnect(out);  
		com.rdta.tlapi.xql.Statement st =getstat(k,out);
		com.rdta.tlapi.xql.ResultSet resultSet;	
   	    String xmlF = "<ASN status='"+s+"'>";
   	    xmlF = xmlF + "<ConsignmentID>"+shipmentId+"</ConsignmentID>";
   	    xmlF = xmlF + "<OrderID>"+poid+"</OrderID>";
   	    xmlF = xmlF + "<ShipmentDate>"+shipmentDate+"</ShipmentDate>";
   	    xmlF = xmlF + "<Items>";
   		xmlF = xmlF + "<Item>";
   		xmlF = xmlF + "<ItemID>"+itemid+"</ItemID>"; 
   		xmlF = xmlF + "<ItemDescription>"+itemdet+"</ItemDescription>";		 
   		xmlF = xmlF + "<ItemQty>"+qty+"</ItemQty>";
   		xmlF = xmlF + "<ItemPrice>"+price+"</ItemPrice>";      
   		xmlF = xmlF + "</Item>"+"</Items>";    
        xmlF = xmlF + "</ASN>";
        String FullQuery = "insert" +xmlF+ "into doc('tig:///SELLER/ASN/Asn.xml')/ASNI";
        resultSet=st.execute(FullQuery); 
         if(resultSet==null)
        	 res.sendRedirect("Failure");  
         else
        	 res.sendRedirect("success2");
         String xq="";
	       xq="update  for $b in collection('tig:///SELLER/PO_RX')/POI "; 
	       xq=xq+"replace value of $b/PO/@status with 'checked'";  
	       executeStatementStream(xq,st,out,res);
   	    
   	    
		}
		catch(IOException e)     
   	 {
   		 try{
   			 String error=new String(e.getMessage());
   			sesion.setAttribute("excp",error);
   		 res.sendRedirect("Failure");
   		 }catch(Exception e1){}
   		 
   	 }
		catch(NumberFormatException e)  
    	 {
    		 try{
    			 String error=new String(e.getMessage());
    			 sesion.setAttribute("excp",error);
        		 res.sendRedirect("Failure");
        		 }catch(Exception e1){}
    	 }
		 catch(NullPointerException e)  
    	 {
    		 try{
    			 String error=new String(e.getMessage());
    			 sesion.setAttribute("excp",error);
        		 res.sendRedirect("Failure");
        		 }catch(Exception e1){}
    	 }
		 catch(com.rdta.tlapi.xql.XQLException e)
         {
			 try{
    			 String error=new String(e.getMessage());
    			 sesion.setAttribute("excp",error);
        		 res.sendRedirect("Failure");
        		 }catch(Exception e1){}
         }
		finally
		{
			try{
	    		 out.close();
	    		 tigclose(k);
	    		 }catch(NullPointerException e)
	        	 {
	        		 try{
	        			 String error=new String(e.getMessage());
	        			 sesion.setAttribute("excp",error);
	            		 res.sendRedirect("Failure");
	            		 }catch(Exception e1){}
	        	 }
		}
	}
}

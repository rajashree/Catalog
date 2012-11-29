package SCM;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;

import com.rdta.tlapi.xql.*;



import java.sql.*;  
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import com.rdta.tlapi.xql.*;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
import com.rdta.util.io.StreamHelper;
import com.rdta.tlapi.xql.DataSourceProperties;
import com.rdta.tlapi.xql.DataSourceFactory;import com.rdta.tlapi.xql.DataSourceFactory;
 
    
	  // now the servlet part
    public class POposting extends HttpServlet{    
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
//             w.println("successc");
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
//            w.println("successs");
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
//        w.println("successer");
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
//        	w.println("successe");  	
        	try
    		{
             com.rdta.tlapi.xql.ResultSet resultSet;
             QueryOption qo = QueryOption.XMLSPACE;
             st.setQueryOption(qo, "preserve"); 
                     resultSet= st.execute(query);
            
                 if(resultSet==null)
                 	System.out.println("fail");
                 else
                	 res.sendRedirect("success.jsp");
    		
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
    	 	  
	  public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException
	     {   
	    	
	    	 HttpSession sesion=req.getSession();
	    	 com.rdta.tlapi.xql.Connection k=null;
	    	 PrintWriter out=null;
	    	 try{
	    		 String s="unchecked";
	    		 out=res.getWriter();
	    	 int uid=Integer.parseInt(req.getParameter("userId"));
	    	 String mailid=req.getParameter("mailId");
	    	 String poid=req.getParameter("orderId");
	    	 String ord_desc=req.getParameter("orderDescription");
	    	 String date=req.getParameter("dateofPurchase");

	    	 int item=Integer.parseInt(req.getParameter("itemId"));
	    	 String item_desc=req.getParameter("itemDescription");
	    	 int quantity=Integer.parseInt(req.getParameter("itemQuantity"));
	    	 Float f=Float.valueOf(req.getParameter("itemPrice"));
	    	 float price=f.floatValue();
	    	 
	    	  k=TigConnect(out);
	    	 com.rdta.tlapi.xql.Statement st =getstat(k,out);
	    	 
	    	 String xmlF = "<PO status='"+s+"'>";
     		
	    		xmlF = xmlF + "<UserID>"+uid+"</UserID>";  
	    		xmlF = xmlF + "<MailID>"+mailid+"</MailID>";
	    		xmlF = xmlF + "<OrderID>"+poid+"</OrderID>";
	    		xmlF = xmlF + "<OrderDescription>"+ord_desc+"</OrderDescription>";
	    		xmlF = xmlF + "<DateOfPurchase>"+date+"</DateOfPurchase>";
	    		xmlF = xmlF + "<Items>";
	    		xmlF = xmlF + "<Item>";
	    		xmlF = xmlF + "<ItemID>"+item+"</ItemID>"; 
	    		xmlF = xmlF + "<ItemDescrpition>"+item_desc+"</ItemDescrpition>";		 
	    		xmlF = xmlF + "<ItemQty>"+quantity+"</ItemQty>";
	    		xmlF = xmlF + "<ItemPrice>"+price+"</ItemPrice>";
	    		xmlF = xmlF + "</Item>"+"</Items>"; 
	                xmlF = xmlF + "</PO>";
	                String FullQuery = "insert" +xmlF+ "into doc('tig:///BUYER/PO/PO.xml')/POI";
	    	       executeStatementStream(FullQuery,st,out,res);

	    		
	    	 }  
	    	 catch(IOException e)  
	    	 {
	    		 try{
	    			 String error=new String(e.getMessage());
	    			sesion.setAttribute("excp",error);
	    		 res.sendRedirect("Failure.jsp");
	    		 }catch(Exception e1){}
	    		 
	    	 }   
	    	 catch(NumberFormatException e)
	    	 {
	    		 try{
	    			 String error=new String(e.getMessage());
	    			 sesion.setAttribute("excp",error);
	        		 res.sendRedirect("Failure.jsp");
	        		 }catch(Exception e1){}
	    	 }
	    	 catch(NullPointerException e)
	    	 {
	    		 try{
	    			 String error=new String(e.getMessage());
	    			 sesion.setAttribute("excp",error);
	        		 res.sendRedirect("Failure.jsp");
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
	 	            		 res.sendRedirect("Failure.jsp");
	 	            		 }catch(Exception e1){}
	 	        	 }
	 		    }
	    	 
	    	 
	     }
}
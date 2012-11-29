/**
 * 
 */
package SCM;

/**
 * @author anilreddy
 *
 */
import java.sql.*;
import java.io.*;
import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rdta.tlapi.xql.*;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
import com.rdta.util.io.StreamHelper;
import com.rdta.tlapi.xql.DataSourceProperties;
import com.rdta.tlapi.xql.DataSourceFactory;
public class TigPopost {

	public static com.rdta.tlapi.xql.Connection tigConnect()
	{
		com.rdta.tlapi.xql.Connection con=null;
		String host="LocalHost";
		try{
			DataSourceProperties properties=DataSourceFactory.getDataSourceProperties();
			properties.getProperty(DataSourceProperties.REMOTE_HOST).setValue(host);
			properties.getProperty(DataSourceProperties.REMOTE_PORT).setValue("3408");
			properties.getProperty(DataSourceProperties.LISTEN_PORT).setValue("3444");
			DataSource source=DataSourceFactory.getDataSource(properties);
			con=source.getConnection("admin","admin");
			return con;
		}
		catch(com.rdta.tlapi.xql.XQLException e)
		{
			e.printStackTrace();
			return con;
		}
	}// end for the Function Connection
	
	public static com.rdta.tlapi.xql.Statement getStat(com.rdta.tlapi.xql.Connection con)
	{
		com.rdta.tlapi.xql.Statement s=null;
		try
		{
		   s=con.createStatement();
	       return s;
	     }
	      catch(com.rdta.tlapi.xql.XQLException e)
	        {
	         System.out.println(e);
	          return s;
	         }
		}// end for Function Statement
	
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
       }// end for Connection close
	
	  public static void executeStatementStream(String query,com.rdta.tlapi.xql.Statement st) 
      {
      	    try
			{
	            com.rdta.tlapi.xql.ResultSet resultSet;
	            QueryOption qo = QueryOption.XMLSPACE;
	            st.setQueryOption(qo, "preserve");
                resultSet= st.execute(query);
                if(resultSet==null)
                      System.out.println("fail");
			
	            if ( resultSet.next() )
	            	System.out.println("inserted sucessfully");
	            else
	            	System.out.println( "insertion failed" );
	            
	          }
                 catch(Exception e)
			      {
				System.out.println("Error  "+e);
			     }	        	
          }// end for Statement Execution
	  
	  
	  // now the servlet part
	  
	  public static void main(String x[])
	     {   
	    	
	    	 
	    	 try{ 
	    	 String uid="01";
	    	 String mailid="Anil@yahoo.com";
	    	 String poid="001";
	    	 String ord_desc="nokia";
	    	 String date="25/06/06";
	    	 String item="001";
	    	 String item_desc="nokia";
	    	 int quantity=10;
	    	 int price=5;
	    	 com.rdta.tlapi.xql.Connection k =tigConnect();
	         com.rdta.tlapi.xql.Statement st =getStat(k);
	         String xmlF = "<PO status='uncheck'>";
	         xmlF = xmlF + "<UserID>"+uid+"</UserID>";  
	    		xmlF = xmlF + "<MailID>"+mailid+"</mailID>";
	    		xmlF = xmlF + "<OrderID>"+poid+"</OrderID>";
	    		xmlF = xmlF + "<OrderDescription>"+ord_desc+"</OrderDescription>";
	    		xmlF = xmlF + "<DateOfPurchase>"+date+"</DateOfPurchase>";
	    		xmlF = xmlF + "<Items><Item><ItemID>"+item+"</ItemID>";
	    		xmlF = xmlF + "<ItemDescription>"+item_desc+"</ItemDescription>";
	    		xmlF = xmlF + "<ItemQty>"+quantity+"</ItemQty>";
	    		xmlF = xmlF + "<ItemPrice>"+price+"</ItemPrice>";
	    		xmlF = xmlF + "</Item></Items></PO>";
	    		
	    		String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig:///first/tsp', "+xmlF+" )";
	    		StringBuffer str=new StringBuffer(FullQuery);
	    	       executeStatementStream(str.toString(),st);
	    	     
	    		
	    	 }
//	    	 catch(Exception e)  
//	    	 {
//	    		 try{
//	    			 String error=new String(e.getMessage());
//	    			sesion.setAttribute("excp",error);
//	    		 res.sendRedirect("Failure.jsp");
//	    		 }catch(Exception e1){}
//	    		
//	    	 }
	    	 catch(Exception e)
	    	 {
	    		
	    			e.printStackTrace();
	        		
	    	 }
	    	
	    	 
	    	 
	     }
	
	}


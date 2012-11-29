package SCM;     
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.http.*;  

import com.rdta.tlapi.xql.DataSource;
import com.rdta.tlapi.xql.DataSourceFactory;
import com.rdta.tlapi.xql.DataSourceProperties;
import com.rdta.util.io.StreamHelper;
public class Check extends HttpServlet {   
	public com.rdta.tlapi.xql.Connection TigConnect()
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
         return k;
         }
         catch(com.rdta.tlapi.xql.XQLException e)
          {
              System.out.println(e);
              return k;
          }
     }

public com.rdta.tlapi.xql.Statement getstat(com.rdta.tlapi.xql.Connection con)
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
   }  

 byte[] tigread(com.rdta.tlapi.xql.Statement s,String Q)
 {
    byte d[]=null;
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
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	{
		PrintWriter out=null;   
		HttpSession sesion=req.getSession();  
		com.rdta.tlapi.xql.Connection k=null;
		try{    
		String userName=req.getParameter("userName");
		String password=req.getParameter("password");
		String type=req.getParameter("categery");
		
		k=TigConnect();
	     com.rdta.tlapi.xql.Statement s=getstat(k);
	     byte[] xres;
	      String xq="";
	      xq="for $b in collection('tig:///Role/USER')/users/user ";
	      xq=xq+"where $b/UserName = '"+userName+"'";
	      xq=xq+"return $b/password/text()";
	      xres=tigread(s,xq);
	      if(xres==null)
	      {
	    	String error=new String("Invalid User"); 
	    	sesion.setAttribute("excp",error);
	      	    	  res.sendRedirect("Error.jsp"); 
	      }
	      String r=new String(xres);
	      System.out.println("result is: "+r);
  
	      
	   		out=res.getWriter();   
			 out.println("username is"+userName);   
			 out.println("Type is"+type);       
	
		if(password.equals(r))  
		{      
			sesion.setAttribute("userName",userName);
			if(type.equals("buyer"))  
			res.sendRedirect("BuyerHome.jsp");    
			if(type.equals("seller"))
				res.sendRedirect("SellerHome.jsp");  
		}
		else
		     res.sendRedirect("Error.jsp");
		}    

		catch(IOException e){
			try{
   			 String error=new String(e.getMessage());
   			sesion.setAttribute("excp",error);
   		 res.sendRedirect("Failure.jsp");
   		 }catch(Exception e1){}
			
		   }
		catch(NullPointerException e)
   	 {
   		 try{
   			 String error=new String("Invalid user");
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



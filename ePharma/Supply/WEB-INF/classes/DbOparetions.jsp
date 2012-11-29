<%@ page import="java.io.*,java.sql.*,java.util.*,java.util.Properties" %>
<%@ page import="java.io.ByteArrayInputStream,java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.BufferedInputStream,java.io.InputStream,java.io.IOException" %>
<%@ page import="com.rdta.tlapi.xql.*,com.rdta.tlapi.xql.Connection,com.rdta.tlapi.xql.Statement" %>
<%@ page import="com.rdta.util.io.StreamHelper,com.rdta.tlapi.xql.DataSourceProperties " %>
<%@ page import="com.rdta.tlapi.xql.DataSourceFactory,javax.xml.parsers.DocumentBuilderFactory,
javax.xml.parsers.DocumentBuilder,org.w3c.dom.*" %>
 <%!
	JspWriter w;
  HttpServletResponse r;
   HttpSession ses;  
  
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
                    String error=new String(e.getMessage());
   			ses.setAttribute("excp",error);
                  try{
   		 r.sendRedirect("Failure.jsp");}
			catch(Exception e1){}
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
                String error=new String(e.getMessage());
   			ses.setAttribute("excp",error);
                  try{
   		 r.sendRedirect("Failure.jsp");}
			catch(Exception e1){}
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
                      String error=new String(e.getMessage());
   			ses.setAttribute("excp",error);
                  try{
   		 r.sendRedirect("Failure.jsp");}
			catch(Exception e1){}
                     }
                    catch(IOException i)
                     {
                     String error=new String(i.getMessage());
   			ses.setAttribute("excp",error);
                  try{
   		 r.sendRedirect("Failure.jsp");}
			catch(Exception e1){}
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
                   String error=new String(e.getMessage());
   			ses.setAttribute("excp",error);
                  try{
   		 r.sendRedirect("Failure.jsp");}
			catch(Exception e1){}
                }
            }  


            

        
             %>

package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import com.rdta.tlapi.xql.*;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
import com.rdta.util.io.StreamHelper;
import com.rdta.tlapi.xql.DataSourceProperties;
import com.rdta.tlapi.xql.DataSourceFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;

public final class POSubmit_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


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


            

        
             
  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/DbOparetions.jsp");
    _jspx_dependants.add("/BuyerNav.jsp");
  }

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write(" ");
      out.write('\n');
      out.write("\n");
      out.write("   <html>\n");
      out.write("   <body bgcolor=\"white\"> \n");
      out.write("<table border=0 cellspacing=0 cellpadding=0 width=100% height=\"20%\">\n");
      out.write("<tr>\n");
      out.write("  <td align=right nowrap><font size=3>\n");
      out.write("  <a href=\"signIn\">logoff</a>|\n");
      out.write("  <a href=\"BuyerReport\">Reports</a>|\n");
      out.write("  <a href=\"BuyerHome\">Home</a>\n");
      out.write("</font></td></tr></table>\n");
      out.write("\n");
      out.write("   <table align=\"center\" border=2 width=50%>\n");
      out.write("   <th colspan=10 align=\"center\"><font size=\"10\">Item Details</font></th>\n");
      out.write("   <tr>\n");
      out.write("   <td>UserID</td><td>MailID</td><td>OrderID</td><td>OrderDescription</td>\n");
      out.write("   <td>DateOfPurchase</td> <td>ItemID</td><td>ItemDescription</td>\n");
      out.write("   <td>ItemQty</td><td>ItemPrice</td><td>POsubmit</td>\n");
      out.write("   </tr>\n");
      out.write("\n");
 
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
          
 	   }//end if for node name 5 
      out.write("  \n");
      out.write("  \t<form action=\"Login\" method=\"post\" name=\"log\">\n");
      out.write("           \n");
      out.write("\t<tr><td><input type=\"text\" name=\"userId\" value='");
 w.println(userid); 
      out.write("' size=6></td>\n");
      out.write("        <td><input type=\"text\" name=\"mailId\" value='");
 w.println(mailid); 
      out.write("' size=15></td>\n");
      out.write("        <td><input type=\"text\" name=\"orderId\" value='");
 w.println(poid); 
      out.write("' size=7></td>\n");
      out.write("        <td><input type=\"text\" name=\"orderdesc\" value='");
 w.println(ord_desc); 
      out.write("' size=15 ></td>\n");
      out.write("        <td><input type=\"text\" name=\"date\" value='");
 w.println(date); 
      out.write("' size=14></td>\n");
      out.write("        <td><input type=\"text\" name=\"itemid\" value='");
 w.println(itemid); 
      out.write("' size=6></td>\n");
      out.write("        <td><input type=\"text\" name=\"itemdesc\" value='");
 w.println(item_desc); 
      out.write("' size=15></td>\n");
      out.write("        <td><input type=\"text\" name=\"itemqty\" value='");
 w.println(itemqty); 
      out.write("' size=7></td>\n");
      out.write("        <td><input type=\"text\" name=\"itemprice\" value='");
 w.println(item_price); 
      out.write("' size=9></td>\n");
      out.write("\t<td><input type=\"submit\"  value=\"SubmitPO\" ></td>\n");
      out.write("        <td><input type=\"hidden\" name=\"choice\" value=5></td>\n");
      out.write("\t</tr>\n");
      out.write("\t</form>\n");
      out.write("        \n");

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

 
      out.write("\n");
      out.write("   \n");
      out.write("    </table>\n");
      out.write("    </body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

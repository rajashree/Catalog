package com;
import java.io.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.DataSource;
import com.rdta.tlapi.xql.DataSourceFactory;
import com.rdta.tlapi.xql.ResultSet;
import com.rdta.tlapi.xql.Statement;
import com.rdta.tlapi.xql.XQLConnectionException;
import com.rdta.tlapi.xql.XQLException;
import com.rdta.util.io.StreamHelper;

public class GetRecord extends HttpServlet {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection conn = null;      
    private String userName = "admin";
    private String password = "admin";
    private static String url = "xql:rdtaxql://localhost:3408";
    private Statement statement = null;
    private ResultSet rs=null;
    
 
    public void init(ServletConfig config) throws ServletException {
        super.init(config);       
    }

    public void doGet(HttpServletRequest request, HttpServletResponse  response)
        throws IOException, ServletException {
    	
    	HttpSession session=request.getSession(true);
    	String targetId = request.getParameter("id");
    	
    	response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
       
        String consid=new String(); 
        String sdate=new String();
        String itemid=new String();
        String itemdesc=new String();
        String itemq=new String();
        String itemp=new String();
        String status= new String();
    	
    	try
		{
				String XQuery="let $x :=(for $i in collection('tig:///Seller/ASN')/ASNI where $i//OrderID ='";
				XQuery=XQuery+targetId+"' return $i) return $x";
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();		  
			    DocumentBuilder parser = null;
					try 
					{
						parser = factory.newDocumentBuilder();
					}
					catch (ParserConfigurationException e)
					{
						session.setAttribute("status", e.getMessage());
	    				response.sendRedirect("Failure.jsp"); 
					}
				

				try
				{
					DataSource dataSource = (DataSource) DataSourceFactory.getDataSource(url,null);
					conn = dataSource.getConnection(userName, password);				
				}
				catch(XQLException ex)
				{
					session.setAttribute("status", ex.getMessage());
					response.sendRedirect("Failure.jsp");
				}
				try
				{
					statement = conn.createStatement();                
	               	rs=statement.execute(XQuery);
	               	Statement statement2= conn.createStatement();               	                
	               	ResultSet rs2=statement2.execute("tlsp:Reconcile('"+targetId+"')");
	            	if(rs2.next())
	               	{
	               		status=rs2.getString();
	               	}
				}
				catch(XQLException ex)
				{
					session.setAttribute("status", ex.getMessage());
					response.sendRedirect("Failure.jsp");
				}
               	
               
                if(rs.next())
                {
                	InputStream stream=rs.getStream();	    		
    				byte[] temp = null;
    				
    				try
    				{
    					temp = StreamHelper.copy(stream);					
    				}
    				catch (IOException e)
    				{
    					session.setAttribute("status", e.getMessage());
        				response.sendRedirect("Failure.jsp");
    				}
    				InputStream result = new ByteArrayInputStream(temp);				
    				
    				try
    				{
    					Document document = parser.parse(result);
    					Element root = document.getDocumentElement();
    					NodeList purchaseorders=root.getElementsByTagName("ASN"); 
    					
    				    for (int i = 0; i < purchaseorders.getLength(); i++)
    				    {	   
    				    	Element purchase = (Element) purchaseorders.item(i);   				    	
    				    	
    				    	 NodeList cons = purchase.getElementsByTagName("ConsignmentID");				        
    					     consid=cons.item(0).getFirstChild().getNodeValue();  
    					        
    				    	NodeList sds = purchase.getElementsByTagName("ShipmentDate");
    				    	sdate=sds.item(0).getFirstChild().getNodeValue();			    	
    				    	
    				    	NodeList ii = purchase.getElementsByTagName("ItemID");
    				    	itemid=ii.item(0).getFirstChild().getNodeValue();
    				    	
    				    	NodeList id = purchase.getElementsByTagName("ItemDescription");
    				    	itemdesc=id.item(0).getFirstChild().getNodeValue();
    				    	
    				    	NodeList iq = purchase.getElementsByTagName("ItemQty");
    				    	itemq=iq.item(0).getFirstChild().getNodeValue();
    				    	
    				    	NodeList ip = purchase.getElementsByTagName("ItemPrice");
    				    	itemp=ip.item(0).getFirstChild().getNodeValue();
    				   }
    				}
    				catch (SAXException e)
    				{
    					session.setAttribute("status", e.getMessage());
        				response.sendRedirect("Failure.jsp");
    				}
    				catch (IOException e)
    				{
    					session.setAttribute("status", e.getMessage());
        				response.sendRedirect("Failure.jsp");
    				}
                }                	
                else
                {
                	                	
                }                	
        }
		catch(XQLConnectionException ex)
        {
			session.setAttribute("status", ex.getMessage());
			response.sendRedirect("Failure.jsp");
		}        	
        catch (XQLException ex)
        {
        	session.setAttribute("status", ex.getMessage());
        	response.sendRedirect("Failure.jsp");
		}        
        finally
        {
	        try
	        {
	        	if(conn!=null)
	        		conn.close();	        	
	        	response.getWriter().write("<TABLE cellSpacing=1 cellPadding=1 width='75%' border=1><TR><TD width='25%'>OrderID</TD><td width='25%'>"+targetId+"</td><TD width='25%'>ConsignmentID</TD><td width='25%'>"+consid+"</td></tr><tr><td colspan=2 align='right'>Shipment Date</td><td colspan=2 align='left'>"+sdate+"</td></tr><tr><td>ItemID</td><td>"+itemid+"</td><td>Item Description</td><td>"+itemdesc+"</td></tr><tr><td>Item Qty</td><td>"+itemq+"</td><td>Item Price</td><td>"+itemp+"</td></tr><tr><td colspan=4><font size=3 color=black><b>Status</b></font></td></tr><td colspan=4 align='center'><font size=4 color=red><b>"+status+"</b></font></td></tr></TABLE>");	        
	        }
	        catch (XQLException ex)
	        {
	        	session.setAttribute("status", ex.getMessage());
	        	response.sendRedirect("Failure.jsp");
			}
        }

                     
        
               
    }
}
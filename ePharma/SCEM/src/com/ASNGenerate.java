package com;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rdta.tlapi.xql.*;

public class ASNGenerate extends HttpServlet
{
	private Connection conn = null;      
    private String userName = "admin";
    private String password = "admin";
    private static String url = "xql:rdtaxql://localhost:3408";
    private Statement statement1 = null;
    private Statement statement3 = null;
    private static final long serialVersionUID = 1L;
			
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String userid=req.getParameter("UserID");
		//String mailid=req.getParameter("MailID");
		String orderid=req.getParameter("OrderID");
		//String orderdescription=req.getParameter("OrderDescription");
		//String dop=req.getParameter("DateOfPurchase");
		String itemid=req.getParameter("ItemID");
		String itemdescription=req.getParameter("ItemDescription");
		//String itemqty=req.getParameter("ItemQty");
		//String itemprice=req.getParameter("ItemPrice");
		String rxitemqty=req.getParameter("Rx-ItemQty");
		String rxitemprice=req.getParameter("Rx-ItemPrice");
		String consignid=req.getParameter("ConsignmentID");		
		String sdate=req.getParameter("ShipmentDate");
		HttpSession session=req.getSession(true);
		
		try
		{
			String XQuery1="update for $b in collection('tig:///Seller/PO-RX')/POI[PO/UserID = "+userid+"]";
			XQuery1=XQuery1+"replace value of $b/PO/@status with 'checked'";
						
			String XQuery3="tig:insert-document('tig:///Seller/ASN',";
			XQuery3=XQuery3+"<ASNI><ASN status='unchecked'>";
			XQuery3=XQuery3+"<ConsignmentID>"+consignid+"</ConsignmentID>";			
			XQuery3=XQuery3+"<OrderID>"+orderid+"</OrderID>";
			XQuery3=XQuery3+"<ShipmentDate>"+sdate+"</ShipmentDate>";
			XQuery3=XQuery3+"<Items>";
			XQuery3=XQuery3+"<Item>";
			XQuery3=XQuery3+"<ItemID>"+itemid+"</ItemID>";
			XQuery3=XQuery3+"<ItemDescription>"+itemdescription+"</ItemDescription>";
			XQuery3=XQuery3+"<ItemQty>"+rxitemqty+"</ItemQty>";
			XQuery3=XQuery3+"<ItemPrice>"+rxitemprice+"</ItemPrice>";
			XQuery3=XQuery3+"</Item></Items></ASN></ASNI>)";
						
			try
			{
				DataSource dataSource = (DataSource) DataSourceFactory.getDataSource(url,null);
				conn = dataSource.getConnection(userName, password);				
			}
			catch(XQLException ex)
			{
				session.setAttribute("status", ex.getMessage());
				//res.sendRedirect("Failure.jsp");
			}				
           	statement1 = conn.createStatement();                
           	statement1.executeUpdate(XQuery1);
           	statement3 = conn.createStatement();
           	statement3.executeUpdate(XQuery3);        
           	session.setAttribute("status", "ASN Generation is Successfull");
			//res.sendRedirect("Failure.jsp");
        }
		catch(com.rdta.tlapi.xql.XQLConnectionException ex)
        {
			session.setAttribute("status", ex.getMessage());
			//res.sendRedirect("Failure.jsp");  
		}
        catch(com.rdta.tlapi.xql.XQLException ex)
        {
        	session.setAttribute("status", ex.getMessage());
			//res.sendRedirect("Failure.jsp"); 
        }	        
    }	
}

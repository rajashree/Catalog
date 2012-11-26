package com;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rdta.tlapi.xql.*;

public class POSubmit extends HttpServlet
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
		String mailid=req.getParameter("MailID");
		String orderid=req.getParameter("OrderID");
		String orderdescription=req.getParameter("OrderDescription");
		String dop=req.getParameter("DateOfPurchase");
		String itemid=req.getParameter("ItemID");
		String itemdescription=req.getParameter("ItemDescription");
		String itemqty=req.getParameter("ItemQty");
		String itemprice=req.getParameter("ItemPrice");
		HttpSession session=req.getSession(true);
		
		try
		{
			String XQuery1="update for $b in collection('tig:///Buyer/PO')/POI[PO/UserID = "+userid+"]";
			XQuery1=XQuery1+"replace value of $b/PO/@status with 'checked'";
						
			String XQuery3="tig:insert-document('tig:///Seller/PO-RX',";
			XQuery3=XQuery3+"<POI><PO status='unchecked'>";
			XQuery3=XQuery3+"<UserID>"+userid+"</UserID>";
			XQuery3=XQuery3+"<MailID>"+mailid+"</MailID>";
			XQuery3=XQuery3+"<OrderID>"+orderid+"</OrderID>";
			XQuery3=XQuery3+"<OrderDescription>"+orderdescription+"</OrderDescription>";
			XQuery3=XQuery3+"<DateOfPurchase>"+dop+"</DateOfPurchase>";
			XQuery3=XQuery3+"<Items>";
			XQuery3=XQuery3+"<Item>";
			XQuery3=XQuery3+"<ItemID>"+itemid+"</ItemID>";
			XQuery3=XQuery3+"<ItemDescrpition>"+itemdescription+"</ItemDescrpition>";
			XQuery3=XQuery3+"<ItemQty>"+itemqty+"</ItemQty>";
			XQuery3=XQuery3+"<ItemPrice>"+itemprice+"</ItemPrice>";
			XQuery3=XQuery3+"</Item></Items></PO></POI>)";
						
			try
			{
				DataSource dataSource = (DataSource) DataSourceFactory.getDataSource(url,null);
				conn = dataSource.getConnection(userName, password);				
			}
			catch(XQLException ex)
			{
				session.setAttribute("status", ex.getMessage());
				res.sendRedirect("Failure.jsp");
			}				
           	statement1 = conn.createStatement();                
           	statement1.executeUpdate(XQuery1);
           	statement3 = conn.createStatement();
           	statement3.executeUpdate(XQuery3);        
           	session.setAttribute("status", "POSubmit is Successfull");
			res.sendRedirect("Failure.jsp");
        }
		catch(com.rdta.tlapi.xql.XQLConnectionException ex)
        {
			session.setAttribute("status", ex.getMessage());
			res.sendRedirect("Failure.jsp");  
		}
        catch(com.rdta.tlapi.xql.XQLException ex)
        {
        	session.setAttribute("status", ex.getMessage());
			res.sendRedirect("Failure.jsp"); 
        }	        
    }	
}

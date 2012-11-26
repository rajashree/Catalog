package com;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rdta.tlapi.xql.*;

public class POGeneration extends HttpServlet
{
	private Connection conn = null;      
    private String userName = "admin";
    private String password = "admin";
    private static String url = "xql:rdtaxql://localhost:3408";
    private Statement statement = null;
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
			String XQuery="tig:insert-document('tig:///Buyer/PO',";
			XQuery=XQuery+"<POI><PO status='unchecked'>";
			XQuery=XQuery+"<UserID>"+userid+"</UserID>";
			XQuery=XQuery+"<MailID>"+mailid+"</MailID>";
			XQuery=XQuery+"<OrderID>"+orderid+"</OrderID>";
			XQuery=XQuery+"<OrderDescription>"+orderdescription+"</OrderDescription>";
			XQuery=XQuery+"<DateOfPurchase>"+dop+"</DateOfPurchase>";
			XQuery=XQuery+"<Items>";
			XQuery=XQuery+"<Item>";
			XQuery=XQuery+"<ItemID>"+itemid+"</ItemID>";
			XQuery=XQuery+"<ItemDescrpition>"+itemdescription+"</ItemDescrpition>";
			XQuery=XQuery+"<ItemQty>"+itemqty+"</ItemQty>";
			XQuery=XQuery+"<ItemPrice>"+itemprice+"</ItemPrice>";
			XQuery=XQuery+"</Item></Items></PO></POI>)";
			
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
			statement = conn.createStatement();                
           	statement.executeUpdate(XQuery);
           	session.setAttribute("status", "POGeneration is Successfull");
			//res.sendRedirect("Failure.jsp");
        }
		catch(com.rdta.tlapi.xql.XQLConnectionException e)
        {
			session.setAttribute("status", e.getMessage());
			//res.sendRedirect("Failure.jsp");
		}
        catch(com.rdta.tlapi.xql.XQLException e)
        {
        	session.setAttribute("status", e.getMessage());
			//res.sendRedirect("Failure.jsp"); 
        }	           
    }	
}

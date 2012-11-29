/**
 * 
 */
package SCM;

/**  
 * @author anilreddy         
 *
 */   
import java.io.IOException;
import javax.servlet.*; 
import javax.servlet.http.*;
public class suplychain extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
        int selection=1;
        ServletContext sc=getServletContext();
		selection=Integer.parseInt(req.getParameter("choice"));
		if(selection==1)
		{
			sc.getRequestDispatcher("/CheckXml").forward(req,res);
		}
//		else
//		if(selection==2)     
//		{
//			String home=req.getParameter("Type");    
//			if(home.equalsIgnoreCase("buyer"))
////				sc.getRequestDispatcher("/BuyerHome.jsp").forward(req,res);
//				res.sendRedirect("BuyerHome.jsp");
//			else
////				sc.getRequestDispatcher("/SellerHome.jsp").forward(req,res);
//				res.sendRedirect("SellerHome.jsp");
//		} 
		else
			if(selection==3)  
			{
				sc.getRequestDispatcher("/POorder").forward(req,res);
			}
			else 
				if(selection==4)
				{
					sc.getRequestDispatcher("/ASNGeneration").forward(req,res);
				}
				else
					if(selection==5)
					{
						sc.getRequestDispatcher("/POsubmit").forward(req,res);
					}
		if(selection==11)
	    {  
	    	int option=Integer.parseInt(req.getParameter("report"));
	    	switch(option)
	    	{
	    	case 1: sc.getRequestDispatcher("/POGenReport.jsp").forward(req,res);break;
	    	case 2: sc.getRequestDispatcher("/POSubmitReport.jsp").forward(req,res);break;
	    	case 3: sc.getRequestDispatcher("/POASNReconReport.jsp").forward(req,res);break;
	    	}
	    	
	    }
		    if(selection==12)  
		    {
		    	int option=Integer.parseInt(req.getParameter("report"));
		    	switch(option)
		    	{ 
		    	case 1: sc.getRequestDispatcher("/PORecReport.jsp").forward(req,res);break;
		    	case 2: sc.getRequestDispatcher("/ASNGenReport.jsp").forward(req,res);break;
		    	case 3: sc.getRequestDispatcher("/ASNReconReport.jsp").forward(req,res);break;
		    	}
		    	
		    }
	}

}


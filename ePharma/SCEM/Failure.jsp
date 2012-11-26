<% 
	if(session.getAttribute("type")==null)
    {
	    out.println("<font color='red' size='4'><b>Your Session Expired</b></font>");
	    out.println("<br><h3>Go <a href='Login.jsp'>Home</a></h3>");
	}
	else
	{
		String status=session.getAttribute("status").toString();
		out.println("<h3>"+status+"</h3>");
		if(status.equals("Access Violation for Seller"))
		{
			    out.println("<font size='3'><b>Logined as Buyer and trying to access the Seller Resources</b></font>");
			    session.setAttribute("type",null);
				session.setAttribute("status",null);
				session.invalidate();
				out.println("<br><br><font color='red' size='4'><b>Your Session is Invalidated</b></font>");
				out.println("<h3>Go <a href='Login.jsp'>Home</a></h3>");
		}
		else if(status.equals("Access Violation for Buyer"))
		{
			out.println("<font size='3'><b>Logined as Seller and trying to access the Buyer Resources</b></font>");
			session.setAttribute("type",null);
			session.setAttribute("status",null);
			session.invalidate();
			out.println("<br><br><font color='red' size='4'><b>Your Session is Invalidated</b></font>");
			out.println("<h3>Go <a href='Login.jsp'>Home</a></h3>");
		}		
		else if(status.equals("POGeneration is Successfull")||status.equals("POSubmit is Successfull"))
		{
			out.println("Return Back To <A href='Buyer.jsp'>Buyer Home</a>");
			session.setAttribute("status","");
		}
		else if(status.equals("ASN Generation is Successfull"))
		{
			out.println("Return Back To <A href='Seller.jsp'>Seller Home</a>");
			session.setAttribute("status","");
		}
	}
    
%>
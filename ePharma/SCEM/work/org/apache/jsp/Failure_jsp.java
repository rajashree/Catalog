package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class Failure_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

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

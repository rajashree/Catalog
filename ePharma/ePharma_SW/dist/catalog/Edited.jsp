<%
	String catalogName = (String)request.getAttribute("catalogName");
	System.out.println("The catalog name inside Edited.jsp is "+catalogName);
	
	String genId = (String)request.getAttribute("genId");
	System.out.println("The genId inside Edited.jsp is "+genId);
	
	String pagenm = (String)request.getAttribute("pagenm");
	System.out.println("The pagenm inside Edited.jsp is "+pagenm);
	
	String catalogId = (String)request.getAttribute("catalogId");
	System.out.println("The catalogId inside Edited.jsp is "+catalogId);
	
	String prodName = (String)request.getAttribute("prodName");
	System.out.println("The prodName inside Edited.jsp is "+prodName);
	
	String tp_company_nm = (String)request.getAttribute("tp_company_nm");
	System.out.println("The tp_company_nm inside Edited.jsp is "+tp_company_nm);
					
	System.out.println("Inside Edited.jsp...............");				
					
	RequestDispatcher view = request.getRequestDispatcher("/ShowMasterProductInfo.do");
	view.forward(request, response);

	
%>	
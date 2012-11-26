<%
	String catalogName = (String)request.getAttribute("catalogName");
	System.out.println("The catalog name inside Redirect.jsp is "+catalogName);
	
	String genId = (String)request.getAttribute("genId");
	System.out.println("The genId inside Redirect.jsp is "+genId);
	
	String pagename = (String)request.getAttribute("pagename");
	System.out.println("The pagename inside Redirect.jsp is "+pagename);
		
	request.setAttribute("hideMandatory", "true");
	request.setAttribute("pagename", pagename);
	
		
	RequestDispatcher view = request.getRequestDispatcher("/AddMasterCatalogDetails.do?tp_company_nm=&pagenm=Catalog");
	view.forward(request, response);

	
%>	

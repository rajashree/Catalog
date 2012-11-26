<%@ page session="true" %>
<%
  Integer i= (Integer)session.getAttribute("num");
	if(i != null)
  out.println("Num value in session is "+i.intValue());
	else 
		 out.println("Num value in session is null ");
%>
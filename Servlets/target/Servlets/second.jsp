

<html><body>
<%	String m=request.getParameter("user");
   	String n=request.getParameter("pass");
	if(m.equals("dev")&& n.equals("raj"))
		out.print("valid");
else
	out.print("Invalid");
%>

</body>
</html>


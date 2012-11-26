<html>
 <head>
  <title>Struts Redirect/Forward Example</title>
 </head>
<body>
<img src="images\tomcat-power.gif">
<br>
<%
String firstName = (String)request.getParameter("firstName");
if ( firstName == null ){
  firstName = "Not found in request";
}
String lastName = (String)request.getParameter("lastName");
if ( lastName == null ){
  lastName = "Not found in request";
}
%>
<b>First Name:</b> <%=firstName%><br>
<b>Last Name:</b> <%=lastName%><br>
</body>
</html>

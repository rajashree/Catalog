<html>
<body bgcolor="white">
 <table  align="center" border=2  width=50%>
  <th><font size="10">List Of Received Items</font></th>
  <% String e= (String)session.getAttribute("result"); %>
<tr><td><% out.println(e); %></td></tr>
 </table>
</body>
</html>

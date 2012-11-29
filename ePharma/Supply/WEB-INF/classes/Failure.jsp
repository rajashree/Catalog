<html>
<body bgcolor="white">
 <table  align="center" border=2  width=50%>
  <th><font size="10">Exception</font></th>
  <% String e= (String)session.getAttribute("excp"); %>
<tr><td>Error is</td><td><% out.println(e); %></td></tr>
 </table>
</body>
</html>

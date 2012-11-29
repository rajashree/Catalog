<html>
<body bgcolor="white">
<%@include file="SellerNav.jsp"%>
<form action="/Login" method="post">
 <table  align="center" width=50% border=2>
  <th align="center"><font size="6">PO ASN Reconcilation Result</font></th>
    <% String dif=(String)session.getAttribute("diff"); %> 
    <tr><td align="center"><% out.println("Difference is:"+dif);%></td> 
   </table>  
</form>
</body>
</html>

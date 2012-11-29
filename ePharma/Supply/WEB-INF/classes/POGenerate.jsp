<html>
<script language="javascript" src="myvalidate.js">
</script>
<body bgcolor="white">
<%@include file="BuyerNav.jsp"%>
<form action="Login" method="post" name="myform">
<table align="center" border=1  width=50%>
  <th colspan=2> <font size=6>  Purchase Order Information</font></th>
      
     <tr>
   <td>UserId:</td>
   <td><input type="text" name="userId" size=10></td>
  </tr>  
   
   <tr>
   <td>MailId:</td>
   <td><input type="text" name="mailId" size=10 onBlur='lose(mailId)'></td>
  </tr> 
  
      
  <tr>
   <td>OrderId:</td>
   <td><input type="text" name="orderId" size=10></td>
  </tr> 
  
  <tr>
   <td>OrderDescription:</td>
   <td><input type="text" name="orderDescription" size=10></td>
  </tr> 

 <tr>
   <td>DateofPurchase:</td>
   <td><input type="text" name="dateofPurchase" size=10></td>
  </tr> 

  <tr>
   <td>ItemId:</td>
   <td><input type="text" name="itemId" size=10></td>
  </tr> 


  <tr>
   <td>ItemDescription:</td>
   <td><input type="text" name="itemDescription" size=10></td>
  </tr> 
  <tr>
   <td>ItemQuantity:</td>
   <td><input type="text" name="itemQuantity" size=10 onBlur='checknum(itemQuantity)'></td>
  </tr> 
  
  <tr>
   <td>ItemPrice:</td>
   <td><input type="text" name="itemPrice" size=10 onBlur='checknum(itemPrice)'></td>
  </tr> 

  <tr><td><input type="hidden" name="choice" value=3></td></tr>
  <tr><td> <input type="button" value="Generate" onClick="validate2()"></td>
    <td> <input type="reset" value="Reset"></td></tr>

</table>
</form>
<body>
</html>

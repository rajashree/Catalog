<html>
  <body bgcolor="white">
<%@include file="BuyerNav.jsp"%>
    
<form action="Login" method=post>
    <table align="center" width=50% border=1>
       
          <th><font size=5>Buyer Report Generation</font></th>
      
      <tr>
      <td align="center">Report<select name="report">
                    <option value=1>PO Generate</option>
                    <option value=2>SubmitPO</option>
                    <option value=3>PO-ASN Reconsilation</option>
                   </select>    
       </td></tr>

    <tr><td align="center"><input type="submit" value="Generate Report"></td></tr>
     <tr><td><input type="hidden" value=11 name="choice"></td></tr>
               
    </table>
 </form>
  </body>

</html>

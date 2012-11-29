<html>
<script language="javascript" src="myvalidate.js">
</script>

<body bgcolor="white">
<form action="Login" method="post" name="myform">
<table align="center" ><tr><td><font size=6>Supply Chain USE-CASE</font></td></tr></table>
  <table border=2 align="center" width="50%" ><br><br>
    <th colspan=2 align="center"><h3>User Login Page</h3></th>
   <tr>
   <td> UserName:</td><td> <input type="text" name="userName" size="8"></td></tr>
   <tr>
   <td> PassWord:</td><td> <input type="password" name="password" size="8"></td></tr>
   <tr><td>Type:</td><td>
      <select name="categery">
       <option value=buyer>Buyer</option>
       <option value=seller>Seller</option>
      </select></td>
    </tr>
   <tr><td><input type="hidden" name="choice" value=1></td></tr>
   <tr><td colspan=2 align=center><input type="button" value="LOGIN" onclick="validate2()"></td></tr>
  </table>
</form>
</body>
</html>

<html>
<head>
<title>Details Page</title>

</head>
<body>

<%@ page import="java.sql.*" %>
<%@ page import="com.mysql.jdbc.Driver" %>
<br><br>
<TABLE cellSpacing=0 cellPadding=0 width="90%" align=center border=0 bgcolor="white" background="">
  <TR><TD vAlign=center align=right>
  	  
  	  <STRONG><A href="SignOut.jsp"><FONT color=navy>Logout
      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>
</TABLE><br>
<TABLE cellSpacing=0 cellPadding=0 width="90%" align=center border="0" bgcolor="white" background="">
  <TR><TD vAlign=center align=left>
  	  
  	  <STRONG><A><FONT color=navy>Welcome to EPostcard Administrator's Page
      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>
</TABLE><br>
<TABLE WIDTH="90%" BORDERCOLOR=midnightblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1 >

<TR><TD>
	<TABLE WIDTH="100%"  BORDER=1 CELLSPACING=0 CELLPADDING=0 >
		<TR bgcolor=lavenderblush >
			<TD align="center"><FONT color=navy><STRONG>SENDER</STRONG></FONT></TD>
			<TD align="center"><FONT color=navy><STRONG>MANAGER</STRONG></FONT></TD>
			<TD align="center"><FONT color=navy><STRONG>PRODUCT</STRONG></FONT></TD>
			<TD align="center"><FONT color=navy><STRONG>MANAGER REPLY</STRONG></FONT></TD>
		</TR>
		
<%
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		
			Driver d = new Driver();
			DriverManager.registerDriver(d);
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/Plantronics", "root", "");
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from ecard_customer;");
			
			String flag="No Result";
			
			while (rs.next())
			{
				if(rs.getString("status").equals("1"))
				{	System.out.println(rs.getString("status"));
					flag ="Approve";
				}else if(rs.getString("status").equals("2"))
					flag = "Deny";
				else flag = "No Result";
				
				
				out.println("<TR bgcolor=lavenderblush><TD>");
			
				String str1 = rs.getString("ecard_id");
				out.println(rs.getString("sender_email"));
				out.println("</TD><TD>");
				out.println(rs.getString("manager_email"));
				out.println("</TD><TD>");
				out.println(rs.getString("product_id"));
				out.println("</TD><TD>");
				out.println(flag);
				out.println("</TD>");
				
			}
			out.println("</TD></TR>");
			
		}catch(Exception ex){}
%>
			
		
		
		
	
	</TABLE>
</TD>
</TR><TR><TD align='right'><a href="http://localhost/Raje/xls.php">
<img border="0" src="Export.gif" width="65" height="38"></TD></TR>
</TABLE>	

</body>
</html>


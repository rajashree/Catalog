<html>
<body>
	<form action="./EL-sec.jsp" method="post">
		<label>Name:</label>
		<input type="text" name="name"/>
		<label>FirstName:</label>
		<input type="text" name="name"/>
		<label>Age:</label>
		<input type="text" name="age"/>
		<input type="submit" value="submit"/>
		
	</form>
	
	<% response.setHeader("site", "http://www.google.com");%>
	<% session.putValue("company","SourceN India Pvt. Ltd. from sessionScope");%>
</body>

</html>
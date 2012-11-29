<%@include file="../includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
	    <title>Admin page</title>
	</head>
	<body>
		${navCrumbs}

		<h1>Admin page</h1>
		
		<ul class="links">
		    <li><a href="<c:url value="/admin/retailers/list.do"/>">View Retailers</a></li>
		</ul>
	</body>
</html>
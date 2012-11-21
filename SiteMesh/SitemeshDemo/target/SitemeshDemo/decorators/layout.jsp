<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title><decorator:title default="Welcome!"/></title>
<style>
body {
	margin: 2em;
	color: white;
}
a {
	color: #CCCCCC;
}
#page-container {
	width: 685px;
	border: 2px solid black;
	background-color: #5E3732;
}
#content-container {
	height: 100%;
	text-align: left;
	padding: 2em;
	padding-top:0;
	background-color: white;
	color: black;
}
#page-header  {
	width: 100%;
	padding-top:1em;
	border-bottom: 1px solid gray;
	border-top: 1px solid gray;
	text-align: center;
}
#nav-container {
	height: 300px;
	width: 15%;
	border-right: 1px solid black;
	border-color: #666666 #cccccc #cccccc #666666;
	padding-top: 20px;
	padding-left: 10px;
	vertical-align: top;
}
#nav-container a {
	text-decoration: none;
	color: white;
}
.selected {
	padding: 2px;
	color: black;
	border: 1px yellow dotted;
	font-style: italic;
	font-weight: bold;
}
#nav-container a:visited {
	color: lightGray;
}
#nav-container a:hover {
	color: yellow;
}
#page-footer {
	width: 100%;
	padding: 5px;
	font-size: x-small;	
	border-top: 1px solid black;
	border-color: #666666 #cccccc #cccccc #666666;
	text-align: center;
}


</style>
    <decorator:head/>
</head>

<body>
	<table id="page-container" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="2" id="page-header">
			<%@ include file="/includes/header.jsp"%>
		</td>
	</tr><tr>
		<td id="nav-container">
			<%@ include file="/includes/navigation.jsp" %>
		</td>
		<td id="content-container">
			<decorator:body/>
		</td>
	</tr><tr>
		<td colspan="2" id="page-footer">
			<%@ include file="/includes/footer.jsp" %>
		</td>
	</tr></table>
</body>
</html>


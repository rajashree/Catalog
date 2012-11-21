<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<decorator:usePage id="thePage" />

<% String selection = thePage.getProperty("meta.selection"); %>


	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<% if(selection != null && "index".equals(selection)){ %>
				<a href="index.jsp" class="selected">Main</a>
			<% } else { %>
				<a href="index.jsp">Main</a>
			<% } %>
		</td>

	</tr><tr>
		<td>
			<% if(selection != null && "page1".equals(selection)){ %>
				<a href="index.jsp" class="selected">Page 1</a>
			<% } else { %>
				<a href="page1.jsp">Page 1</a>
			<% } %>
		</td>
	</tr><tr>
		<td>
			<% if(selection != null && "page2".equals(selection)){ %>
				<a href="page2.jsp" class="selected">Page 2</a>
			<% } else { %>
				<a href="page2.jsp">Page 2</a>
			<% } %>
		</td>
	</tr>
	</table>


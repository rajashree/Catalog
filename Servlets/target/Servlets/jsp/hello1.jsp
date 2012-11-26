<!-- hello1.jsp and hello2.jsp used for passing session when the cookie is not 
enable in browser -->

<%@ page session="true" %>
<%
  Integer num = new Integer(100);
  session.setAttribute("num",num);
 String url =response.encodeURL("hello2.jsp");
%>
<a href='<%=url%>'>hello2.jsp</a>


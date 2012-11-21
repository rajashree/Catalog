<%@page import="java.util.Date;"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="now" value="<%=new Date()%>"/>
Current Date : ${now}<br>
Formatted Date : <fmt:formatDate value="${now}" type="both" dateStyle="full" timeStyle="default"/><br>
Styles are long, medium, short, full, default <br>
with pattern : <fmt:formatDate value="${now}" pattern="dd/MM/yyyy ; hh:mm:ss"/> <br>
Parse Date : 
<c:catch var="e">
<fmt:parseDate type="date" value="${now}" dateStyle="long" var="i"/>
${i}
</c:catch>
<c:out value="${e}"></c:out>
<br>
Format message : 
<fmt:message key="Hello" /> <br>
Formatted Number :
<c:set var="num" value="100"/>
<fmt:formatNumber currencySymbol="$" type="currency" value="${num}"></fmt:formatNumber>


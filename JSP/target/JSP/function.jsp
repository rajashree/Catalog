<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="sample" value="This is a test text for <b>test</b>!!!"/>
The Text : <c:out value="${sample}" escapeXml="false"/> <br>
Length of the text : <c:out value="${fn:length(sample)}"></c:out> <br>
Escape xml : <c:out value="${fn:escapeXml(sample)}"/><br>
UpperCase : <c:out value="${fn:toUpperCase(sample)}"/><br>
Split : <c:out value="${fn:length(fn:split(sample,' '))}"/><br>
<c:set var="splitList" value="${fn:split(sample,' ')}"/>
Items are : 
<c:forEach var="i" items="${splitList}">
	<c:out value="${i}"/> ; 
</c:forEach>
<br>
Join : ${fn:join(splitList," , ")}<br>
Substring : ${fn:substring(sample,0,10)}

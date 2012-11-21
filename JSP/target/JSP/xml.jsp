<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<c:import url="emp.xml" var="url"/>
<c:import url="emp.xsl" var="xsl"/>

<x:parse xml="${url}" var="doc"/>

<h3>For Each:</h3>
<x:forEach select="$doc/employees/employee" var="i">
	Emp Name : <x:out select="$i/name"/><br/>
	Emp Age : <x:out select="$i/age"/><br/>
</x:forEach>

<h3>For Each with If</h3>
<x:forEach select="$doc/employees/employee" var="i">
	<x:if select="$i/name = 'dev'">
		The name is matched to dev!!!
	</x:if>
</x:forEach>

<h3>For Each with choose</h3>
<x:forEach select="$doc/employees/employee" var="i">
	<x:choose>
		<x:when select="$i/name = 'dev'">
			<x:out select="$i/name"/> - The name is matched to dev!!!<br/>	
		</x:when>
		<x:otherwise>
			<x:out select="$i/name"/> - The name doesn't match to dev!!!<br/>
		</x:otherwise>
	</x:choose>
</x:forEach>

<h3>XSL Transform</h3>
<x:transform xml="${url}" xslt="${xsl}"/>
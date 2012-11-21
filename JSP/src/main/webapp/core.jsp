 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="java.util.*" %> 
<h3>For c:import</h3>
<c:import url='C-Import.jsp'/>
 <c:set    var="a"     value="Devashree" />
  <c:out   value="${a}"   /><br/>
 
 <c:out   value="${210+49}"/><br/>
 <c:set var="empty_var" value=" "/>
 <%
 String str = "String testing";
 String[] arr = {"arr1","arr2","arr3"};
 
 Map<String,String> map = new LinkedHashMap<String,String>();
 map.put("1","map1");
 map.put("2","map2");
 map.put("3","map3");
 
 List<String> list = new ArrayList<String>();
 list.add("list1");
 list.add("list2");
 list.add("list3");
 
 Set<String> set = new LinkedHashSet<String>();
 set.add("set1");
 set.add("set2");
 set.add("set3");
 
 session.setAttribute("list",list);
 session.setAttribute("map",map);
 session.setAttribute("arr",arr);
 session.setAttribute("str",str);
 session.setAttribute("set",set);
 
 %>
 <h3>From session</h3>
String str : ${str}<br/>
List l: ${list}<br />
Map m: ${map}<br />
Set s: ${set}<br />
Array arr : ${fn:join(arr,",")}<br/>

Second element of the Map : ${map["2"]}<br/>
Second element of the Array : ${arr[1]}<br/>
Second element of the List : ${list[1]}<br/>

<h3>From iteration - forEach</h3>
Array Items : 
<c:forEach var="a" items="${arr}" varStatus="x">
whether its first ele -	${x.first},
whether its last ele - ${x.last},
current value -	${x.current} or ${a},
current index - ${x.index},
count -	${x.count};	

</c:forEach><br/>
List Items :
<c:forEach var="l" items="${list}">
${l}
</c:forEach><br/>
Map Items : 
<c:forEach var="m" items="${map}">
${m.key} = ${m.value}
</c:forEach><br/>
Set Items :
<c:forEach var="s" items="${set}">
${s}
</c:forEach><br/>
Printing 1 - 5 : 
<c:forEach var="i" begin="1" end="10" step="2"  varStatus="status">
	value : ${i}
	count-value : <c:out value="${status.count}"/><br/>
</c:forEach>

<h3>escapeXML</h3>
<c:set var="esc" value="<b>Devashree</b>"/>
without Escape EL: ${fn:escapeXml(esc) } <br/>
cout : <c:out value="${esc}"/><br/>
with Escape EL : ${esc}<br/>
cout : <c:out value="${esc}" escapeXml="false"/>

<h3>IF</h3>
<c:if test="${2<4}">
	<c:out value="2 is less than 4"/>
</c:if>

<h3>c:set using beans</h3>
<jsp:useBean id="emp" class="com.sourcen.Employee"></jsp:useBean>
<c:set target="${emp}" property="age" value="25"/>
<c:out value="${emp.age}"/>
<h3>Choose, When and Otherwise </h3>
<c:choose>
	<c:when test="${emp.age<25}">
		<c:out value="${emp.age} is less than 4"/>
	</c:when>
	<c:when test="${emp.age>25}">
		<c:out value="${emp.age} is greater than 8"/>
	</c:when>
	<c:otherwise>
		<c:out value="${emp.age} is the no. Both are equal"/>
	</c:otherwise>
</c:choose>


<h3>Remove bean - emp.age</h3>
<c:remove var="emp"/>

emp.age : ${emp.age}<br/>

<h3>ForTokens loop</h3>
<c:forTokens items="aaa;bbb;ccc,ddd,eee|fff" delims=",|" var="token" varStatus="status">
	value : ${token}
	count : ${status.count}<br/>
</c:forTokens>

<h3>Redirect</h3>

<%----<c:redirect url="http://www.google.com"></c:redirect>--%>

<h3>URL</h3>
<c:url var="url" value="EL-sec.jsp">
	<c:param name="name" value="devashree mega"/>
	<c:param name="age" value="333444444444"/>	
</c:url>
<a href="${url}">Click here for c:url test</a>




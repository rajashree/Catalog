<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<h3>Select Query</h3>
<sql:setDataSource var="db" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/hibernate?user=root"/>
<sql:query var="query1" sql="select * from user;" dataSource="${db}"/>
<c:forEach var="row" items="${query1.rows}">
	UID : <c:out value="${row.uid}"></c:out> ; 
	Name : <c:out value="${row.name}"/> ;  
	Password : <c:out value="${row.password}"></c:out><br>
</c:forEach>
<h3>Insert Query</h3>
<sql:update  var="query2" sql="insert into user values(111,'devashree mega','mega');" dataSource="${db}"></sql:update>
${query2}
<h3>Delete Query</h3>
<sql:update var="query3" sql="delete from user where uid='111';" dataSource="${db}"></sql:update>
${query3}
<h3>Update Query</h3>
<sql:update var="query4" sql="update user set name=? where uid='1';" dataSource="${db}">
	<sql:param value="raj"></sql:param>
</sql:update>
${query4}
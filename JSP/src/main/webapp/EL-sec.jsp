<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="emp" class="com.sourcen.Employee"></jsp:useBean>
<jsp:setProperty property="name" name="emp" value="Devashree Rajashree from pageScope"/>

<h3>Parameter value :</h3>
Name is ${param.name}<br>
Age is ${param.age}<br>
Names------------- is ${fn:join(paramValues.name,",")}<br>
PageScope : ${pageScope.emp.name}<br>
OR PageScope by  ------ ${emp.name}<br/>
<%request.setAttribute("reqName","Dev from requestScope");%>
<jsp:include page="EL-third.jsp"></jsp:include>
SessionScope : ${sessionScope.company}<br>
OR session by  ------ ${company}<br/>
ApplicationScope : ${applicationScope.emp.name}<br> 

Header Key : ${header.key}<br>
Header Value : ${header.value}
<br>
<h3>PageContext:</h3><br>

Request Protocol : ${pageContext.request.protocol}<br>
Response Locale : ${pageContext.response.locale}<br>
Session id : ${pageContext.session.id}<br>
ServletContext : ${pageContext.servletContext}<br>

<h3>ContextParam :</h3> <br>
Email set in context param : ${initParam.email}

<%
	Cookie cookie1 = new Cookie("name1111","Devashree M");
	response.addCookie(cookie1);
%>
<h3>Cookies : </h3> <br>
Name of cookie : ${cookie.name1111}<br>
Value of cookie : ${cookie.name1111.value}<br>



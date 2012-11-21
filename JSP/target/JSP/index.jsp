<%@ page language="java"%>

<%--Tag lib--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %> 
Hello Devi
<%--this hello is a comment --%>
<%--Decleration --%>
<%! int temp=10,i=11,j=19;
String hello= "Hello World!!!";%>
<%-- Expression --%> 
<h3><%=hello%></h3>
<%--Snipplet --%>
<% System.out.println("the temp value is "+temp);
temp = temp+10;
System.out.println("the temp value after processing "+temp);
%>
<%-- jsp:text --%>
<jsp:text>This is from jsp:text</jsp:text>
<%-- include directive--%>
<%@ include file="/test.jsp" %>
<%@ page import="java.util.*" %>
<%Date date = new Date();
System.out.println("the current date"+ date);
%>

<%-- Forward --%>
<%--<jsp:forward page="fwd.jsp">
	<jsp:param name="testVar1" value="devashree"/>
	<jsp:param name="testVar2" value="@sourcen"/>
</jsp:forward>
<%-- User Bean --%>
<jsp:useBean id="emp" scope="application" class="com.sourcen.Employee">
</jsp:useBean>
<%-- Set Property --%>
<%--<jsp:setProperty property="*" name="emp"/>--%>
<%-- OR --%>
<jsp:setProperty property="name" name="emp"/>
<jsp:setProperty property="compName" param="companyName" name="emp"/>
<jsp:setProperty property="age" name="emp"/>

<%--<jsp:setProperty property="name" name="emp" value="Devashree M"/>
<jsp:setProperty property="compName" name="emp" value="Sourcen"/>
<jsp:setProperty property="age" name="emp" value="2"/>

<%--Get property  --%>
<jsp:text>Get Property:-----</jsp:text><br/>
<jsp:getProperty property="name" name="emp"/><br/>
<jsp:getProperty property="compName" name="emp"/><br/>
<jsp:getProperty property="age" name="emp"/><br/>
<br/>
<form action="#" method="post">
<label>Name : </label><input name="name" type="text"/><br/>
<label>Company Name : </label> <input name="companyName"/><br/>
<label>Age : </label> <input name="age"/><br/>
<input type="submit" id="submit"/>
</form>

Information -> <br/>
Working with server: <%= application.getServerInfo() %><br>
Servlet Specification: <%= application.getMajorVersion() %>.<%= application.getMinorVersion() %> <br>


<a href="EL-first.jsp">EL Examples</a><br>

<a href="core.jsp">Core JSTL Example</a><br>

<a href="xml.jsp">XML JSTL Example</a><br>

<a href="sql.jsp">SQL JSTL Example</a><br>

<a href="function.jsp">Function JSTL Example</a><br>

<a href="fmt.jsp">Format JSTL Example</a>

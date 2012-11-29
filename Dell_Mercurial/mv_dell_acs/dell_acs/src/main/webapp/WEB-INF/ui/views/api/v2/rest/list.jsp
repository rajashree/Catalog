<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<html>
<head>
    <title>API List</title>

    <style type="text/css">
        body{
            font-family: Verdana, Arial,san-serif;
        }
    </style>
</head>
<body>
<ul>
    <c:forEach var="apiService" varStatus="true" items="${apiServices}">
        <li><a href="#${apiService.serviceName}">${apiService.serviceName}</a></li>
    </c:forEach>
</ul>

<c:forEach var="apiService" varStatus="true" items="${apiServices}">
    <a name="${apiService.serviceName}" style="font-size: 14px;"></a>

    <div id="apiService-${apiService.serviceName}" style="margin:20px 20px 50px 20px;padding: 10px;border: 5px solid #CCC;background: #FFF;">
        <span style="float: left;width:500px;padding: 20px 0px 20px 40px;display: block;"><strong>${apiService.serviceName}</strong></span>
        <a onclick="window.scrollTo(0, 0);" style="display:block;float: right;cursor: pointer;">Top</a>
        <br/>
        <c:forEach var="apiMethod" items="${apiService.methods}">
            <table width="90%" style="border: 1px solid #333" align="center">
                <tr>
                    <td>
                        <b>${apiMethod.methodName}</b>
                        <br/>
                        ${apiMethod.methodEndPoint}
                    </td>
                </tr>
                <tr>
                    <td><b>Parameters</b></td>
                </tr>
                <tr>
                    <td>
                        <c:set var="paramSize" value="${fn:length(apiMethod.parameters)}"/>
                        <c:choose>
                            <c:when test="${paramSize>0}">
                                <table width="100%" border="1px solid #EEE" >
                                    <tr style="background: #EEE;">
                                        <td width="30%">Name</td>
                                        <td width="30%">Type</td>
                                        <td width="20%">Required</td>
                                        <td width="20%">DefaultValue</td>
                                    </tr>
                                    <c:forEach var="apiParameter" items="${apiMethod.parameters}">
                                        <tr>
                                            <td>${apiParameter.paramName}</td>
                                            <td>${apiParameter.paramType}</td>
                                            <td>${apiParameter.required}</td>
                                            <td>${apiParameter.defaultValue}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:when>
                            <c:otherwise>
                                No Parameters found
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
            <br/><br/>
        </c:forEach>
    </div>
</c:forEach>

</body>
</html>
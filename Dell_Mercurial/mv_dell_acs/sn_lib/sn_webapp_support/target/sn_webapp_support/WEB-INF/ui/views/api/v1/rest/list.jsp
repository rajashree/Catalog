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

    <c:forEach var="apiService" varStatus="true" items="${apiServices}">
        <div id="apiService-${apiService.serviceName}" style="margin:20px 20px 50px 20px;padding: 10px;border: 5px solid #CCC;background: #FFF;">
                <div style="font-size: 14px;"><b>${apiService.serviceName}</b></div>
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
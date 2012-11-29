<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<html>
<head>
    <title>Pixel Tracking List</title>

    <style type="text/css">
        body {
            font-family: Verdana, Arial, san-serif;
        }
    </style>
</head>
<body>
<div id="breadcrumb">
    Admin >
    <%--Retailer List--%>
    <a href="<c:url value='/admin/retailers/list.do'/>">
        Retailers
    </a>
</div>

<div style="margin:20px 20px 50px 20px;padding: 10px;border: 5px solid #CCC;background: #FFF;">
    <div style="font-size: 14px;"><b>Supported Pixel Tracking Systems within Content Server</b></div>
    <br/>
    <c:forEach var="pixel" varStatus="true" items="${pixelTrackers}">


        <table width="90%" style="border: 1px solid #333" align="center">
            <tr>
                <td>
                    <b>${pixel.trackerName}</b>
                    <br/>
                </td>
            </tr>
        </table>
    </c:forEach>
    <br/>
</div>

</body>
</html>
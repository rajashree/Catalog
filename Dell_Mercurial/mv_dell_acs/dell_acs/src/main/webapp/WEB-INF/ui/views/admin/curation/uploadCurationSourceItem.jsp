<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%
    //Refresh the page every 30 seconds automatically.
    //response.setHeader("Refresh", "30");
%>
<head>
    <meta http-equiv="Cache-Control" content="no-cache" forua="true"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>Retailers list page</title>
    <%@include file="../../includes/header-scripts.jsp" %>
    <script type="text/javascript">
        function verifyExtension() {
            var value = document.getElementById("uploadFile").value;
            if (value != '') {
                value = value.toLowerCase();
                if (!value.match(/(\.csv|\.tab)$/)) {
                    alert("valid extensions are .csv .tab");
                    document.getElementById("uploadFile").focus();
                    return false;
                }
            }
            return true;
        }

    </script>
    <style type="text/css">
        .status-0 {
            background-color: #ffff00;
        }

        .status-1 {
            background-color: #00ccff;
        }

        .status-2, .status-8 {
            background-color: #adff2f;
        }

        .status-3, .status-4, .status-5, .status-9, .status-10 {
            background-color: #ff6666;
        }
    </style>
</head>
<body>

<fieldset>
    <legend><strong>Feed file upload</strong></legend>
    <div class="error_msg">
        <c:choose>
            <c:when test="${ not empty message}">
                <c:forEach items="${message}" var="sourceType">
                    <c:choose>
                        <c:when test="${sourceType.key=='error'}">
                            <b>Unable to import value for source type :</b><br>
                            <c:forEach items="${sourceType.value}" var="error">
                                <c:out value="${error.value}"/><br>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </c:forEach>
            </c:when>
            <c:when test="${not empty imported}">
                <font color="green">Successfully imported the source Type</font><br>
            </c:when>
            <c:when test="${not empty duplicateEntry}">
                <font color="red">Content for source type is already imported...</font><br>
            </c:when>
        </c:choose>
    </div>
    <form:form action="uploadCurationSourceItem.do"
               method="post"
               enctype="multipart/form-data"
               onSubmit="return verifyExtension();">
        <table class="">
            <tr>
                <td>File <br/></td>
                <td><input type="file" id="uploadFile" name="uploadFile"/></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Upload"/></td>
            </tr>
        </table>
    </form:form>
</fieldset>


</body>
</html>
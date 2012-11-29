<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>

<html>
<head>
    <meta http-equiv="Cache-Control" content="no-cache" forua="true"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>Test REST Endpoint Access</title>

    <style type="text/css">
        pre {outline: 1px solid #ccc; padding: 5px; margin: 5px; }
        .string { color: green; }
        .number { color: darkorange; }
        .boolean { color: blue; }
        .null { color: magenta; }
        .key { color: red; }
    </style>


    <script type="text/javascript">

        function jsonPrettyPrinter(jsonString){
            var str = JSON.stringify(jsonString, undefined, 4);
            output(str);
            output(syntaxHighlight(str));
        }

        function output(inp) {
            document.body.appendChild(document.createElement('pre')).innerHTML = inp;
        }

        function syntaxHighlight(json) {
            json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
            return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
                var cls = 'number';
                if (/^"/.test(match)) {
                    if (/:$/.test(match)) {
                        cls = 'key';
                    } else {
                        cls = 'string';
                    }
                } else if (/true|false/.test(match)) {
                    cls = 'boolean';
                } else if (/null/.test(match)) {
                    cls = 'null';
                }
                return '<span class="' + cls + '">' + match + '</span>';
            });
        }

        function testAPI(){
           $.ajax({
                url: basePath +'/verifyAPIKey.do',
                data: {accessKey: $('#accessKey').val(), secretKey: $('#secretKey').val(), requestURL: $('#requestURL').val()},
                success: function(data) {
                    if (data != null) {
                        console.log('',data);

                    }

                }
            });
        }
    </script>

</head>
<body>
<div id="breadcrumb">
    <a href="<c:url value='/admin/index.do'/>"> Admin</a> >
    <a href="<c:url value='/testAPIKey.do'/>"> Test REST </a>
</div>

<div style="margin:20px 20px 50px 20px;padding: 10px;border: 5px solid #CCC;background: #FFF;">
    <div style="font-size: 14px;"><b>Test REST Endpoint Access with API Key </b></div>
    <br/>

    <fieldset>
        <legend><strong>Request</strong></legend>
        URL : ${requestURL}

    </fieldset>

    <br/>
    <br/>
    <br/>

    <fieldset>
        <legend><strong>HTTP Response</strong></legend>
        HTTP Status :  ${httpStatus}
        <c:choose>
            <c:when test="${httpStatus == 200}">
                <pre>     ${wsResponse} </pre>
            </c:when>
            <c:otherwise> ${wsResponse} </c:otherwise>
        </c:choose>
    </fieldset>


</div>

</body>
</html>
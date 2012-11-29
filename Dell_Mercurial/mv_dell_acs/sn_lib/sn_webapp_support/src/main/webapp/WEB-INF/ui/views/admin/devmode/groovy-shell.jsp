<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<html>
<head>
    <title>Groovy Scripting Shell (DevMode)</title>
    <%@include file="devmode-header.jsp" %>
    <style type="text/css">
        #script {
            font-family: monospace;
            font-size: 14px;
        }

        .box {
            margin: 5px;
            padding: 5px;
        }

        .box .content {
            border: 1px solid #999;
            background: #DDDDDD;
        }

        .box .title {
            border: 1px solid #CCC;
            font-family: Verdana, Arial, sans-serif;
            background: #F1F1F1;
            font-size: 12px;
            padding: 5px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<%@include file="devmode-menus.jsp" %>
<div id="groovy-shell-editor">
    <form action="<c:url value='/admin/devmode/groovy-shell.do'/>" method="post">
        <textarea id="script" name="script" style="width:100%;" rows="30">${script}</textarea>
        <br/><br/>
        <button type="submit"> - Execute script -</button>
    </form>
</div>


<div id="shell-result" class="box">
    <div class="title">Result:</div>
    <div class="content">
        <pre>${result}</pre>
    </div>
</div>

<div id="shell-output" class="box">
    <div class="title">Log output:</div>
    <div class="content">
        <pre>${output}</pre>
    </div>
</div>
</body>
</html>
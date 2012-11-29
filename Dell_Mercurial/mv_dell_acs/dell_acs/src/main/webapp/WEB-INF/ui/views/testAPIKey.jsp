<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>

<html>
<head>
    <meta http-equiv="Cache-Control" content="no-cache" forua="true"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>Verify REST Access</title>
    <style type="text/css">
        pre {outline: 1px solid #ccc; padding: 5px; margin: 5px; }
        .string { color: green; }
        .number { color: darkorange; }
        .boolean { color: blue; }
        .null { color: magenta; }
        .key { color: red; }
        .access_key, .secret_key, .request_url, .auth_header {padding-bottom: 10px;}
        .access_key span, .secret_key span, .request_url span, .auth_header span{width: 100px;display: block; float: left;}
        .access_key input, .secret_key input, .request_url input, .auth_header input{margin-left: 10px; width: 70%;}
        .test{width: 100%; text-align:center;}
    </style>

    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
    <script type="text/javascript">
        var valid = true;
        //Credit for the JSON Pretty Printer goes to the author - http://jsfiddle.net/KJQ9K/
        function jsonPrettyPrinter(jsonString){
            var str = JSON.stringify(jsonString, undefined, 4);
            var prettyJSON = syntaxHighlight(str);
            $('#response-data').html(prettyJSON);
            $('#response-block').show();
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

        function testAPI(method){
           var basePath = '<c:url value="/"/>';
           $.ajax({
                url: basePath + 'verifyAPIKey.json',
                type: 'post',
                data: {method: method, accessKey: $('#accessKey').val(), secretKey: $('#secretKey').val(), requestURL: $('#requestURL').val()},
                success: function(data) {
                    if (data != null) {
                        jsonPrettyPrinter(data);
                    }

                }
            });
        }

        function generateAuthHeader(){
            var basePath = '<c:url value="/"/>';
            $.ajax({
                url: basePath + 'generateAuthHeader.json',
                type: 'post',
                data: {accessKey: $('#accessKey').val(), secretKey: $('#secretKey').val(), requestURL: $('#requestURL').val()},
                success: function(response) {
                    if (response != null) {
                        var header = "Authorization: " + response.data;
                        $('#authHeader').css("color", "black");
                        $('#authHeader').val(header);
                    }
                }
            });
        }
    </script>


</head>
<body>
<div id="breadcrumb">
    <a href="<c:url value='/admin/index.do'/>"> Admin</a> > Verify
</div>

<div style="margin:20px 20px 50px 20px;padding: 10px;border: 5px solid #CCC;background: #FFF;">
    <h2 style="font-size: 14px; font-weight: bold;">Verify REST Endpoint Access with API Key</h2>
    <br/>

        <div class="access_key">
            <span>Access Key</span>
            <input id="accessKey" name="accessKey" type="text"/>
        </div>

        <div class="secret_key">
            <span>Secret Key</span>
            <input id="secretKey" name="secretKey" type="text" />
        </div>


        <div class="request_url">
            <span>Request URL</span>
            <input id="requestURL" name="requestURL" type="text" />
        </div>

        <div class="auth_header">
            <span>Generated Auth Header</span>
            <input id="authHeader" name="authHeader" type="text" readonly="true" style="color: #808080;"
                   value="value will be populated on click of 'Generate Header' button">
        </div>
        <div  class="test">
            <input type="button" onclick="javascript:testAPI('GET');" value="GET">
            <input type="button" onclick="javascript:testAPI('POST');" value="POST">
            <input type="button" onclick="javascript:generateAuthHeader();" value="Generate Header">
        </div>
        <p>
            <strong>NOTE:</strong>
            <p>Use below extension to test the REST endpoints.</p>
            <ul>
                <li>Chrome browser <a href="https://chrome.google.com/webstore/detail/fhjcajmcbmldlhcimfajhfbgofnpcjmb?hl=en-US&utm_source=chrome-ntp-launcher">here</a>
                </li>
                <li>Firefox browser <a href="https://addons.mozilla.org/en-US/firefox/addon/modify-headers/">here</a></li>
            </ul>
        </p>


        <div id="response-block" style="display:none">
            <fieldset>
                <legend><strong>Response</strong></legend>
                <pre id="response-data"></pre>
            </fieldset>
        </div>
    </div>
</body>
</html>
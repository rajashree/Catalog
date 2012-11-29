<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%
    String appBasePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
    <head>
        <title>Test JS lib</title>
        <script type="text/javascript" src='<c:url value="/api/v1/rest/lib.do?type=js"/>'></script>
        <script type="text/javascript" src="http://crypto-js.googlecode.com/svn/tags/3.0.2/build/rollups/hmac-sha1.js"></script>

        <script type="text/javascript">
            var basePath = '<%=appBasePath%>';
            var CONTENT_SERVER_CONFIG = {
                SERVER_PATH:basePath,
                API_KEY:''
            };

            function _get(id) {
                return document.getElementById(id);
            }

            function updateSettings() {
                var newApiKey = _get("apiKey").value;
                cs.log.info("Changing API Key value to :="+newApiKey);
                var newServerUrl = _get("serverUrl").value;
                cs.log.info("Changing Server URL :="+newServerUrl);

                CONTENT_SERVER_CONFIG.SERVER_PATH = newServerUrl;
                CONTENT_SERVER_CONFIG.API_KEY = newApiKey;
            }

            cs.api.config.useJSONP = true;

        </script>
    </head>
    <body>

        <div>
            <label>API KEY:</label><br/>
            <input type="text" size="100" id="apiKey" name="apiKey" onchange="updateSettings()" /><br/><br/>
            <label>SERVER URL:</label><br/>
            <input type="text" size="100" id="serverUrl" name="serverUrl" value="<%=appBasePath%>" onchange="updateSettings();"/><br/><br/>
        </div>
        <b>Please use the debug console to run any API methods.</b>
        <br/>
        examples are
        <ul>
            <li>cs.api.v1.CampaignService.getCampaign(49);</li>
            <li>cs.api.v1.CampaignService.getCampaign(49,function(data){window.console.log("completed", data);});</li>
            <li>cs.api.tests.execute(cs.api.v1.CampaignService.getCampaign,[42], 50, 5);</li>
        </ul>
    </body>
</html>